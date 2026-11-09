import mypack.Department;
import mypack.DivisionByZeroException;
import mypack.IntStack;
import mypack.MatrixUtil;
import mypack.Student;
import mypack.Vehicle;
import mypack.VehicleType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs every experiment (1-10) one after the other, then the integrated
 * micro project (Experiment 11). Ideal for the viva / live demonstration.
 */
public class ExperimentDemo {

    private final BufferedReader in;
    private final boolean pause;

    public ExperimentDemo(BufferedReader in, boolean pause) {
        this.in = in;
        this.pause = pause;
    }

    public void runAll() {
        System.out.println("\n==============================================================");
        System.out.println("   FULL DEMO - all 10 experiments inside the Parking Simulator");
        System.out.println("==============================================================");
        exp1Matrix();
        exp2Stack();
        exp3Student();
        exp4Shapes();
        exp5Resizable();
        exp6Package();
        exp7Exception();
        exp8Threads();
        exp9Enum();
        exp10Wrapper();
        exp11MicroProject();
        System.out.println("\n  >>> Demo complete: all 10 experiments + micro project executed. <<<");
    }

    private void title(String text) {
        System.out.println("\n--------------------------------------------------------------");
        System.out.println(" " + text);
        System.out.println("--------------------------------------------------------------");
    }

    private void next() {
        if (!pause) {
            return;
        }
        System.out.print("\n  [Press Enter to continue] ");
        System.out.flush();
        try {
            in.readLine();
        } catch (IOException e) {
            // ignore - just continue
        }
    }

    // ---------------------------------------------------------------- Exp 1
    private void exp1Matrix() {
        title("EXPERIMENT 1 : Add TWO matrices of order N (Floor 1 + Floor 2 occupancy)");
        int[][] floor1 = {{1, 0, 1, 0}, {0, 1, 1, 0}, {1, 0, 0, 1}, {0, 0, 1, 1}};
        int[][] floor2 = {{0, 1, 0, 1}, {1, 1, 0, 0}, {0, 0, 1, 1}, {1, 1, 0, 0}};
        MatrixUtil.print(floor1, "Floor 1 (1 = occupied):");
        MatrixUtil.print(floor2, "Floor 2 (1 = occupied):");
        int[][] sum = MatrixUtil.addMatrices(floor1, floor2);
        MatrixUtil.print(sum, "Combined = Floor 1 + Floor 2:");
        System.out.println("  Total occupied across both floors = " + MatrixUtil.sum(sum));
        next();
    }

    // ---------------------------------------------------------------- Exp 2
    private void exp2Stack() {
        title("EXPERIMENT 2 : Stack class holding a maximum of 10 integers");
        IntStack stack = new IntStack();
        for (int slot = 101; slot <= 111; slot++) {
            if (stack.push(slot)) {
                System.out.println("  push(" + slot + ")  OK");
            } else {
                System.out.println("  push(" + slot + ")  REJECTED - stack is FULL (max 10)");
            }
        }
        stack.display();
        System.out.println("  pop()  -> " + stack.pop() + "   (undo of the latest parking)");
        System.out.println("  pop()  -> " + stack.pop());
        System.out.println("  peek() -> " + stack.peek() + "   (next slot to be undone)");
        stack.display();
        next();
    }

    // ---------------------------------------------------------------- Exp 3
    private void exp3Student() {
        title("EXPERIMENT 3 : Student class - N objects printed with headings");
        List<Vehicle> vehicles = SampleData.createVehicles();
        int n = 4;
        Student[] students = new Student[n];
        for (int i = 0; i < n; i++) {
            students[i] = vehicles.get(i * 2).getOwner();     // N Student objects
        }
        Student.printHeader();
        for (Student s : students) {
            s.display();
        }
        next();
    }

    // ---------------------------------------------------------------- Exp 4
    private void exp4Shapes() {
        title("EXPERIMENT 4 : Shape class + Circle / Triangle / Square (polymorphism)");
        Shape[] shapes = {new Circle(), new Square(), new Triangle()};   // base class references
        String[][] canvas = {{Shape.EMPTY, Shape.EMPTY, Shape.EMPTY}};
        for (int i = 0; i < shapes.length; i++) {
            shapes[i].setPosition(0, i);
            System.out.println("  draw()  : " + shapes[i].describe());
            shapes[i].draw(canvas);                 // same call, different behaviour per sub class
        }
        System.out.println("  Map row : | " + canvas[0][0] + " | " + canvas[0][1] + " | " + canvas[0][2] + " |");
        for (Shape s : shapes) {
            System.out.println("  erase() : " + s.getName());
            s.erase(canvas);
        }
        System.out.println("  Map row : | " + canvas[0][0] + " | " + canvas[0][1] + " | " + canvas[0][2] + " |");
        next();
    }

    // ---------------------------------------------------------------- Exp 5
    private void exp5Resizable() {
        title("EXPERIMENT 5 : Resizable interface implemented by Rectangle");
        Resizable r = new Rectangle(4, 2);
        System.out.println("  Start          : " + r);
        r.resizeWidth(6);
        System.out.println("  resizeWidth(6) : " + r);
        r.resizeHeight(3);
        System.out.println("  resizeHeight(3): " + r);
        ParkingZone zone = new ParkingZone(Department.CSE, 4, 2);
        System.out.println("  Parking zone   : " + zone);
        Resizable asInterface = zone;
        asInterface.resizeWidth(2);
        System.out.println("  After resizeWidth(2): " + zone);
        next();
    }

    // ---------------------------------------------------------------- Exp 6
    private void exp6Package() {
        title("EXPERIMENT 6 : Package 'mypack' imported with  import mypack.*;");
        System.out.println("  Package name of Student     : " + Student.class.getPackageName());
        System.out.println("  Classes inside mypack       : Student, IntStack, MatrixUtil, Department,");
        System.out.println("                                VehicleType, Vehicle, SafeMath,");
        System.out.println("                                DivisionByZeroException");
        System.out.println("  Used by this program        : Main, ParkingLot, ParkingApp, FeeCalculator ...");
        next();
    }

    // ---------------------------------------------------------------- Exp 7
    private void exp7Exception() {
        title("EXPERIMENT 7 : Custom exception DivisionByZero (try, catch, throw, finally)");
        int[] vehicleCounts = {4, 0};
        for (int count : vehicleCounts) {
            try {
                System.out.println("  try     : average fee of Rs.120 collected from " + count + " vehicles");
                if (count == 0) {
                    throw new DivisionByZeroException("No vehicles billed - cannot divide by 0");   // throw
                }
                System.out.println("            average = Rs." + mypack.SafeMath.calculateAverage(120, count));
            } catch (DivisionByZeroException e) {
                System.out.println("  catch   : " + e.getMessage());
            } finally {
                System.out.println("  finally : always executed (cleanup / closing message)");
            }
        }
        next();
    }

    // ---------------------------------------------------------------- Exp 8
    private void exp8Threads() {
        title("EXPERIMENT 8 : MyThread - main thread and child threads run concurrently");
        ParkingLot lot = new ParkingLot();
        List<Vehicle> vehicles = SampleData.createVehicles().subList(0, 5);
        ParkingApp.simulateArrivals(lot, new ArrayList<>(vehicles));
        next();
    }

    // ---------------------------------------------------------------- Exp 9
    private void exp9Enum() {
        title("EXPERIMENT 9 : enum Department  +  valueOf()");
        for (Department d : Department.values()) {
            System.out.println("  " + d + " -> " + d.getFullName());
        }
        String[] inputs = {"ece", "MECH", "xyz"};
        for (String text : inputs) {
            try {
                Department d = Department.valueOf(text.toUpperCase());
                System.out.println("  Input '" + text + "' -> Department.valueOf() = " + d + "  (zone row " + d.zoneRow() + ")");
            } catch (IllegalArgumentException e) {
                System.out.println("  Input '" + text + "' -> invalid department, valueOf() raised an exception");
            }
        }
        next();
    }

    // --------------------------------------------------------------- Exp 10
    private void exp10Wrapper() {
        title("EXPERIMENT 10 : Strings -> wrapper objects, autoboxing / unboxing");
        String[] labels = {"KA01AB1001", "KA01AB1002", "KA02CD2002"};
        VehicleType[] types = {VehicleType.CAR, VehicleType.BIKE, VehicleType.BUS};
        System.out.println("  Hours read as Strings: \"3\", \"5\", \"2\"");
        FeeCalculator.billThree(labels, types, new String[]{"3", "5", "2"});
        System.out.println("\n  Invalid input test: \"3\", \"abc\", \"2\"");
        FeeCalculator.billThree(labels, types, new String[]{"3", "abc", "2"});
        next();
    }

    // --------------------------------------------------------------- Exp 11
    private void exp11MicroProject() {
        title("EXPERIMENT 11 : MICRO PROJECT - everything working together");
        ParkingLot lot = new ParkingLot();
        List<Vehicle> vehicles = SampleData.createVehicles();

        System.out.println("  Step 1: all 10 registered vehicles enter and park (shapes drawn)");
        for (Vehicle v : vehicles) {
            lot.park(v);
        }
        System.out.print(lot.render());
        System.out.println(lot.historyReport());

        System.out.println("\n  Step 2: undo the last parking (stack pop) -> " + lot.undoLastParking());
        System.out.print(lot.render());

        System.out.println("\n  Step 3: try to shrink ISE zone to 1 slot while 2 vehicles are parked");
        System.out.println(lot.resizeZone(Department.ISE, 1, 2));

        System.out.println("\n  Step 4: KA02CD2001 leaves after \"5\" hours -> bill");
        Vehicle leaving = vehicles.get(3);
        Double fee = FeeCalculator.calculateFee(leaving.getType(), "5");
        lot.release(leaving);
        System.out.printf("  Fee = Rs. %.2f   (slot freed, shape erased)%n", fee);

        System.out.println("\n  Step 5: close the MECH zone (resize to 0 slots) after its cars leave");
        for (Vehicle v : lot.parkedVehicles()) {
            if (v.getDepartment() == Department.MECH) {
                lot.release(v);
            }
        }
        System.out.println(lot.resizeZone(Department.MECH, 0, 2));
        System.out.print(lot.render());

        System.out.println("\n  Step 6: analytics per zone (exception handling)");
        for (Department d : Department.values()) {
            try {
                System.out.printf("   %-5s occupancy = %.1f%%%n", d, lot.zoneOccupancyPercent(d));
            } catch (DivisionByZeroException e) {
                System.out.println("   " + d + "   cannot compute: " + e.getMessage());
            } finally {
                // finally always runs for every zone
            }
        }
        next();
    }
}
