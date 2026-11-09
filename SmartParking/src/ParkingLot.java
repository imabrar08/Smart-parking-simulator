import mypack.Department;
import mypack.DivisionByZeroException;
import mypack.IntStack;
import mypack.MatrixUtil;
import mypack.SafeMath;
import mypack.Vehicle;
import mypack.VehicleType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * The parking lot: 2 floors, each an N x N grid (rows = departments, columns = slots).
 *
 * Uses: Experiment 1 (matrices), 2 (undo stack), 4 (shapes), 5 (resizable zones),
 *       7 (exception in analytics) and 9 (Department enum).
 *
 * Mutating methods are synchronized because several vehicle threads
 * (Experiment 8) may try to park at the same time.
 */
public class ParkingLot {

    public static final int N = 4;          // order of every occupancy matrix (N x N)
    public static final int FLOORS = 2;

    public static final int ZONE_FULL = -1;
    public static final int ALREADY_PARKED = -2;

    private final int[][][] occupancy = new int[FLOORS][N][N];      // 0 = free, 1 = occupied
    private final Vehicle[][][] vehicleAt = new Vehicle[FLOORS][N][N];
    private final Shape[][][] shapeAt = new Shape[FLOORS][N][N];
    private final String[][][] canvas = new String[FLOORS][N][N];   // what the map shows
    private final Map<Department, ParkingZone> zones = new EnumMap<>(Department.class);
    private final IntStack history = new IntStack();                // Experiment 2

    public ParkingLot() {
        for (int f = 0; f < FLOORS; f++) {
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < N; c++) {
                    canvas[f][r][c] = Shape.EMPTY;
                }
            }
        }
        for (Department d : Department.values()) {
            zones.put(d, new ParkingZone(d, N, FLOORS));
        }
    }

    // ------------------------------------------------------------------ slot ids

    /** Slot id is an int so it fits in the IntStack: floor*100 + row*10 + col. */
    public static int slotId(int floor, int row, int col) {
        return (floor + 1) * 100 + row * 10 + col;
    }

    private static int floorOf(int id) {
        return id / 100 - 1;
    }

    private static int rowOf(int id) {
        return (id / 10) % 10;
    }

    private static int colOf(int id) {
        return id % 10;
    }

    /** Human readable slot name, e.g. 112 -> "F1-CSE-S3". */
    public String slotLabel(int id) {
        return "F" + (floorOf(id) + 1) + "-" + Department.values()[rowOf(id)] + "-S" + (colOf(id) + 1);
    }

    public Shape shapeAt(int id) {
        return shapeAt[floorOf(id)][rowOf(id)][colOf(id)];
    }

    // ------------------------------------------------------------- park / release

    /** Parks the vehicle in the first free slot of its department zone. */
    public synchronized int park(Vehicle v) {
        if (v.isParked()) {
            return ALREADY_PARKED;
        }
        Department dept = v.getDepartment();
        ParkingZone zone = zones.get(dept);
        int row = dept.zoneRow();
        for (int f = 0; f < zone.getHeight(); f++) {
            for (int c = 0; c < zone.getWidth(); c++) {
                if (occupancy[f][row][c] == 0) {
                    occupancy[f][row][c] = 1;
                    vehicleAt[f][row][c] = v;
                    Shape s = createShape(v.getType());     // Experiment 4 (polymorphism)
                    s.setPosition(row, c);
                    s.draw(canvas[f]);
                    shapeAt[f][row][c] = s;
                    int id = slotId(f, row, c);
                    v.setSlotId(id);
                    history.pushOverwriteOldest(id);        // Experiment 2
                    return id;
                }
            }
        }
        return ZONE_FULL;
    }

    private Shape createShape(VehicleType type) {
        Shape s;
        switch (type) {
            case BIKE:
                s = new Circle();
                break;
            case CAR:
                s = new Square();
                break;
            default:
                s = new Triangle();
                break;
        }
        return s;
    }

    /** Frees the slot of a parked vehicle and erases its shape from the map. */
    public synchronized boolean release(Vehicle v) {
        if (!v.isParked()) {
            return false;
        }
        int id = v.getSlotId();
        int f = floorOf(id);
        int r = rowOf(id);
        int c = colOf(id);
        shapeAt[f][r][c].erase(canvas[f]);
        shapeAt[f][r][c] = null;
        vehicleAt[f][r][c] = null;
        occupancy[f][r][c] = 0;
        v.setSlotId(Vehicle.NOT_PARKED);
        return true;
    }

    /**
     * Experiment 2 - undo: pops slot numbers off the stack until it finds one whose
     * vehicle is still parked there (older entries may be stale if that car left).
     */
    public synchronized Vehicle undoLastParking() {
        while (!history.isEmpty()) {
            int id = history.pop();
            Vehicle v = vehicleAt[floorOf(id)][rowOf(id)][colOf(id)];
            if (v != null && v.getSlotId() == id) {
                release(v);
                return v;
            }
        }
        return null;
    }

    public synchronized String historyReport() {
        int[] ids = history.toArray();
        if (ids.length == 0) {
            return "  Stack is empty - no parking events recorded";
        }
        StringBuilder sb = new StringBuilder("  Last " + ids.length + " parked slots (max " + IntStack.MAX + "):\n");
        for (int i = ids.length - 1; i >= 0; i--) {
            sb.append(i == ids.length - 1 ? "   TOP -> " : "          ");
            sb.append(String.format("%d  (%s)", ids[i], slotLabel(ids[i])));
            if (i > 0) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    public IntStack getHistory() {
        return history;
    }

    // ---------------------------------------------------------------- resizing

    /** Experiment 5 - resizes a department zone through the Resizable interface. */
    public synchronized String resizeZone(Department dept, int newWidth, int newHeight) {
        if (newWidth < 0 || newWidth > N || newHeight < 0 || newHeight > FLOORS) {
            return "  Not allowed: width must be 0-" + N + " and floors 0-" + FLOORS;
        }
        int row = dept.zoneRow();
        for (int f = 0; f < FLOORS; f++) {
            for (int c = 0; c < N; c++) {
                boolean outside = c >= newWidth || f >= newHeight;
                if (outside && occupancy[f][row][c] == 1) {
                    return "  BLOCKED: slot " + slotLabel(slotId(f, row, c))
                            + " is occupied by " + vehicleAt[f][row][c].getNumber()
                            + ". Remove the vehicle first.";
                }
            }
        }
        Resizable zone = zones.get(dept);       // use the zone through the interface
        zone.resizeWidth(newWidth);
        zone.resizeHeight(newHeight);
        return "  Resized -> " + zones.get(dept);
    }

    public ParkingZone getZone(Department d) {
        return zones.get(d);
    }

    // ------------------------------------------------------- matrices and stats

    /** Occupancy matrix of one floor (0-based), returned as a copy. */
    public synchronized int[][] floorMatrix(int floor) {
        int[][] copy = new int[N][N];
        for (int r = 0; r < N; r++) {
            System.arraycopy(occupancy[floor][r], 0, copy[r], 0, N);
        }
        return copy;
    }

    /** Experiment 1 - adds the Floor 1 and Floor 2 matrices. */
    public synchronized int[][] combinedOccupancy() {
        return MatrixUtil.addMatrices(floorMatrix(0), floorMatrix(1));
    }

    public synchronized int occupiedIn(Department d) {
        int count = 0;
        for (int f = 0; f < FLOORS; f++) {
            for (int c = 0; c < N; c++) {
                count += occupancy[f][d.zoneRow()][c];
            }
        }
        return count;
    }

    public synchronized int totalOccupied() {
        return MatrixUtil.sum(combinedOccupancy());
    }

    public synchronized int totalCapacity() {
        int total = 0;
        for (ParkingZone z : zones.values()) {
            total += z.capacity();
        }
        return total;
    }

    /**
     * Experiment 7 - occupancy % of a zone. A zone resized to 0 slots has capacity 0,
     * so this raises the custom DivisionByZeroException.
     */
    public double zoneOccupancyPercent(Department d) throws DivisionByZeroException {
        return SafeMath.divide(occupiedIn(d) * 100.0, zones.get(d).capacity());
    }

    public synchronized List<Vehicle> parkedVehicles() {
        List<Vehicle> list = new ArrayList<>();
        for (int f = 0; f < FLOORS; f++) {
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < N; c++) {
                    if (vehicleAt[f][r][c] != null) {
                        list.add(vehicleAt[f][r][c]);
                    }
                }
            }
        }
        return list;
    }

    // --------------------------------------------------------------- rendering

    /** Draws both floors, then the combined occupancy matrix (Floor 1 + Floor 2). */
    public synchronized String render() {
        StringBuilder sb = new StringBuilder();
        for (int f = 0; f < FLOORS; f++) {
            sb.append("\n  FLOOR ").append(f + 1).append("\n");
            sb.append("          ");
            for (int c = 0; c < N; c++) {
                sb.append('S').append(c + 1).append("    ");
            }
            sb.append("\n         +").append("-----+".repeat(N)).append("\n");
            for (Department d : Department.values()) {
                ParkingZone z = zones.get(d);
                sb.append(String.format("   %-5s |", d));
                for (int c = 0; c < N; c++) {
                    boolean closed = c >= z.getWidth() || f >= z.getHeight();
                    sb.append(' ').append(closed ? " x " : canvas[f][d.zoneRow()][c]).append(" |");
                }
                sb.append('\n');
            }
            sb.append("         +").append("-----+".repeat(N)).append("\n");
        }
        sb.append("\n  Legend: (o)=Bike  [#]=Car  /^\\=Bus/Truck  .=free  x=closed zone\n");

        sb.append("\n  Combined occupancy = Floor 1 matrix + Floor 2 matrix  (Experiment 1)\n");
        int[][] total = combinedOccupancy();
        for (Department d : Department.values()) {
            sb.append(String.format("   %-5s[ ", d));
            for (int c = 0; c < N; c++) {
                sb.append(total[d.zoneRow()][c]).append(' ');
            }
            sb.append("]\n");
        }
        sb.append("  Occupied slots: ").append(totalOccupied()).append(" / ").append(totalCapacity()).append(" open slots\n");
        return sb.toString();
    }
}
