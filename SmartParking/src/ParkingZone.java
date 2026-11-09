import mypack.Department;

/**
 * Experiment 5 - a department's parking zone IS-A Rectangle (so it is Resizable).
 * width  = usable slots per row on each floor
 * height = number of usable floors
 */
public class ParkingZone extends Rectangle {

    private final Department department;

    public ParkingZone(Department department, int slotsPerFloor, int floors) {
        super(slotsPerFloor, floors);
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }

    /** Number of slots currently open in this zone. */
    public int capacity() {
        return area();
    }

    @Override
    public String toString() {
        return department + " zone [" + getWidth() + " slots x " + getHeight() + " floors = " + capacity() + " slots]";
    }
}
