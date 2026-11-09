import mypack.Department;
import mypack.DivisionByZeroException;
import mypack.SafeMath;
import mypack.Student;
import mypack.Vehicle;
import mypack.VehicleType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Console menu of the Campus Smart Parking Simulator. */
public class ParkingApp {

    /** Thrown when the input stream ends (Ctrl+Z / Ctrl+D) so the program can exit cleanly. */
    private static class EndOfInput extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    private final BufferedReader in;
    private final ParkingLot lot = new ParkingLot();
    private final List<Student> students = new ArrayList<>();
    private final List<Vehicle> vehicles = new ArrayList<>();
    private final List<Double> bills = new ArrayList<>();

    public ParkingApp(BufferedReader in) {
        this.in = in;
    }

    // ------------------------------------------------------------------- menu

    public void run() {
        System.out.println("==============================================================");
        System.out.println("        CAMPUS SMART PARKING SIMULATOR  (Java - OOP)");
        System.out.println("        Software-only micro project | no hardware needed");
        System.out.println("==============================================================");
        try {
            boolean running = true;
            while (running) {
                printMenu();
                String choice = ask("  Enter choice: ");
                switch (choice) {
                    case "1":  registerStudents();      break;
                    case "2":  loadSampleData();        break;
                    case "3":  showStudents();          break;
                    case "4":  parkVehicle();           break;
                    case "5":  exitVehicle();           break;
                    case "6":  undoLast();              break;
                    case "7":  System.out.print(lot.render()); break;
                    case "8":  System.out.println(lot.historyReport()); break;
                    case "9":  concurrentArrivals();    break;
                    case "10": resizeZone();            break;
                    case "11": analytics();             break;
                    case "12": billThreeVehicles();     break;
                    case "13": new ExperimentDemo(in, true).runAll(); break;
                    case "0":  running = false;         break;
                    default:   System.out.println("  Invalid choice - please enter a number from the menu.");
                }
            }
        } catch (EndOfInput e) {
            System.out.println("\n  (input ended)");
        }
        System.out.println("\n  Thank you for using the Smart Parking Simulator. Goodbye!");
    }

    private void printMenu() {
        System.out.println("\n  ------------------------- MENU -------------------------");
        System.out.println("   1. Register students / vehicle owners (N)      [Exp 3, 9]");
        System.out.println("   2. Load sample data (10 students)");
        System.out.println("   3. Show registered students                    [Exp 3]");
        System.out.println("   4. Park a vehicle                              [Exp 4, 2, 9]");
        System.out.println("   5. Vehicle exit and bill                       [Exp 10]");
        System.out.println("   6. Undo last parking (stack)                   [Exp 2]");
        System.out.println("   7. Show parking map + combined occupancy       [Exp 1, 4]");
        System.out.println("   8. Show stack of last 10 parked slots          [Exp 2]");
        System.out.println("   9. Simulate concurrent arrivals (threads)      [Exp 8]");
        System.out.println("  10. Resize a department zone                    [Exp 5]");
        System.out.println("  11. Occupancy and billing analytics             [Exp 7]");
        System.out.println("  12. Bill three vehicles (wrapper classes)       [Exp 10]");
        System.out.println("  13. RUN FULL DEMO of all 10 experiments");
        System.out.println("   0. Exit");
        System.out.println("  --------------------------------------------------------");
    }

    // ------------------------------------------------------------ registration

    private void registerStudents() {
        int n = readInt("  How many students (N) do you want to register? (1-10): ", 1, 10);
        List<Student> batch = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            System.out.println("\n  --- Student " + i + " of " + n + " ---");
            String usn = readUsn();
            String name = readNonEmpty("  Name: ");
            Department dept = readDepartment();
            String phone = readPhone();
            VehicleType type = readVehicleType();
            String plate = readPlate();
            Student s = new Student(usn, name, dept.name(), phone);
            addRegistration(s, plate, type);
            batch.add(s);
        }
        System.out.println("\n  Registered students:");
        printStudents(batch);
    }

    private void loadSampleData() {
        int added = 0;
        for (Vehicle v : SampleData.createVehicles()) {
            if (findStudent(v.getOwner().getUsn()) == null && findVehicle(vehicles, v.getNumber()) == null) {
                addRegistration(v.getOwner(), v.getNumber(), v.getType());
                added++;
            }
        }
        System.out.println("  Loaded " + added + " sample students with vehicles.");
    }

    private void addRegistration(Student s, String plate, VehicleType type) {
        students.add(s);
        vehicles.add(new Vehicle(s, plate, type));
    }

    private void showStudents() {
        if (students.isEmpty()) {
            System.out.println("  No students registered yet. Use option 1 or 2.");
            return;
        }
        printStudents(students);
        System.out.println();
        System.out.printf("  %-12s %-12s %-10s %-10s%n", "USN", "Vehicle", "Type", "Status");
        System.out.println("  " + "-".repeat(48));
        for (Vehicle v : vehicles) {
            System.out.printf("  %-12s %-12s %-10s %-10s%n", v.getOwner().getUsn(), v.getNumber(),
                    v.getType().getLabel(), v.isParked() ? lot.slotLabel(v.getSlotId()) : "outside");
        }
    }

    private void printStudents(List<Student> list) {
        Student.printHeader();
        for (Student s : list) {
            s.display();
        }
    }

    // ------------------------------------------------------------ park / exit

    private void parkVehicle() {
        if (vehicles.isEmpty()) {
            System.out.println("  Register students first (option 1 or 2).");
            return;
        }
        List<Vehicle> outside = outsideVehicles();
        if (outside.isEmpty()) {
            System.out.println("  All registered vehicles are already parked.");
            return;
        }
        System.out.println("  Vehicles waiting outside:");
        for (Vehicle v : outside) {
            System.out.println("   - " + v);
        }
        String key = ask("  Enter vehicle number or USN to park: ");
        Vehicle v = findVehicle(outside, key);
        if (v == null) {
            System.out.println("  No waiting vehicle matches '" + key + "'.");
            return;
        }
        int slot = lot.park(v);
        if (slot == ParkingLot.ZONE_FULL) {
            System.out.println("  " + v.getDepartment() + " zone is FULL or closed. Exit a vehicle or resize the zone (option 10).");
        } else {
            System.out.println("  PARKED " + v.getNumber() + " at " + lot.slotLabel(slot) + "  -> shape drawn: " + lot.shapeAt(slot).describe());
            System.out.println("  Slot " + slot + " pushed on the undo stack.");
            System.out.print(lot.render());
        }
    }

    private void exitVehicle() {
        List<Vehicle> parked = lot.parkedVehicles();
        if (parked.isEmpty()) {
            System.out.println("  No vehicles are parked.");
            return;
        }
        System.out.println("  Parked vehicles:");
        for (Vehicle v : parked) {
            System.out.println("   - " + v + " at " + lot.slotLabel(v.getSlotId()));
        }
        Vehicle v = findVehicle(parked, ask("  Enter vehicle number or USN leaving: "));
        if (v == null) {
            System.out.println("  No parked vehicle matches that input.");
            return;
        }
        String hoursText = ask("  Hours parked (1-72): ");
        try {
            Double fee = FeeCalculator.calculateFee(v.getType(), hoursText);   // Exp 10
            String slot = lot.slotLabel(v.getSlotId());
            lot.release(v);
            bills.add(fee);
            System.out.println("\n  ============ PARKING BILL ============");
            System.out.println("   Vehicle : " + v.getNumber() + " (" + v.getType().getLabel() + ")");
            System.out.println("   Owner   : " + v.getOwner().getName());
            System.out.println("   Slot    : " + slot + " (shape erased from map)");
            System.out.println("   Hours   : " + hoursText.trim() + " x Rs." + v.getType().getRatePerHour());
            System.out.printf("   TOTAL   : Rs. %.2f%n", fee);
            System.out.println("  ======================================");
        } catch (NumberFormatException e) {
            System.out.println("  Invalid hours ('" + hoursText + "'): " + e.getMessage() + ". Vehicle NOT released.");
        }
    }

    private void undoLast() {
        Vehicle v = lot.undoLastParking();
        if (v == null) {
            System.out.println("  Nothing to undo - the stack has no valid parking entries.");
        } else {
            System.out.println("  Undone: " + v.getNumber() + " removed from its slot (stack pop). Shape erased.");
        }
    }

    // ---------------------------------------------------------------- threads

    private void concurrentArrivals() {
        List<Vehicle> outside = outsideVehicles();
        if (outside.isEmpty()) {
            System.out.println("  No vehicles outside. Register or load sample data (option 1/2), or exit some vehicles.");
            return;
        }
        int count = Math.min(outside.size(), 6);
        simulateArrivals(lot, new ArrayList<>(outside.subList(0, count)));
    }

    /** Experiment 8 - every vehicle is a MyThread; the main thread keeps monitoring the gate. */
    public static void simulateArrivals(ParkingLot lot, List<Vehicle> list) {
        System.out.println("  " + list.size() + " vehicles arrive at the same time; each is a separate thread.");
        List<MyThread> threads = new ArrayList<>();
        int i = 0;
        for (Vehicle v : list) {
            threads.add(new MyThread("Vehicle-" + v.getNumber(), lot, v, 150 + 120 * (i % 3)));
            i++;
        }
        int tick = 1;
        try {
            while (anyAlive(threads)) {
                System.out.println("   [" + Thread.currentThread().getName() + "] gate monitor running... tick " + tick++);
                Thread.sleep(100);
            }
            for (MyThread t : threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("  All vehicle threads finished; main thread resumed.");
        System.out.print(lot.render());
    }

    private static boolean anyAlive(List<MyThread> threads) {
        for (MyThread t : threads) {
            if (t.isAlive()) {
                return true;
            }
        }
        return false;
    }

    // ----------------------------------------------------------- zones, stats

    private void resizeZone() {
        Department d = readDepartment();
        System.out.println("  Current: " + lot.getZone(d));
        int w = readInt("  New width  - slots per floor (0-" + ParkingLot.N + "): ", 0, ParkingLot.N);
        int h = readInt("  New height - floors (0-" + ParkingLot.FLOORS + "): ", 0, ParkingLot.FLOORS);
        System.out.println(lot.resizeZone(d, w, h));
        System.out.print(lot.render());
    }

    private void analytics() {
        System.out.println("  Zone occupancy (Experiment 7 - custom exception):");
        for (Department d : Department.values()) {
            try {
                double pct = lot.zoneOccupancyPercent(d);
                System.out.printf("   %-5s %d/%d slots  = %.1f%%%n", d, lot.occupiedIn(d), lot.getZone(d).capacity(), pct);
            } catch (DivisionByZeroException e) {
                System.out.println("   " + d + "   zone is closed (0 slots): " + e.getMessage());
            } finally {
                // finally runs for every zone, whether or not an exception occurred
            }
        }
        try {
            double total = 0;
            for (Double bill : bills) {
                total += bill;                                      // unboxing
            }
            double avg = SafeMath.calculateAverage(total, bills.size());
            System.out.printf("  Bills issued: %d | revenue: Rs. %.2f | average bill: Rs. %.2f%n", bills.size(), total, avg);
        } catch (DivisionByZeroException e) {
            System.out.println("  Average bill: " + e.getMessage() + " (no vehicle has been billed yet)");
        } finally {
            System.out.println("  (analytics finished)");
        }
    }

    private void billThreeVehicles() {
        String[] labels = new String[3];
        VehicleType[] types = new VehicleType[3];
        String[] hours = new String[3];
        for (int i = 0; i < 3; i++) {
            labels[i] = "Vehicle-" + (i + 1);
            System.out.println("  --- " + labels[i] + " ---");
            types[i] = readVehicleType();
            hours[i] = ask("  Hours parked (as text): ");
        }
        System.out.println();
        FeeCalculator.billThree(labels, types, hours);
    }

    // ----------------------------------------------------------------- lookup

    private List<Vehicle> outsideVehicles() {
        List<Vehicle> list = new ArrayList<>();
        for (Vehicle v : vehicles) {
            if (!v.isParked()) {
                list.add(v);
            }
        }
        return list;
    }

    private Student findStudent(String usn) {
        for (Student s : students) {
            if (s.getUsn().equalsIgnoreCase(usn)) {
                return s;
            }
        }
        return null;
    }

    private Vehicle findVehicle(List<Vehicle> list, String key) {
        for (Vehicle v : list) {
            if (v.getNumber().equalsIgnoreCase(key) || v.getOwner().getUsn().equalsIgnoreCase(key)) {
                return v;
            }
        }
        return null;
    }

    // ------------------------------------------------------------ input helpers

    private String ask(String prompt) {
        System.out.print(prompt);
        System.out.flush();
        try {
            String line = in.readLine();
            if (line == null) {
                throw new EndOfInput();
            }
            return line.trim();
        } catch (IOException e) {
            throw new EndOfInput();
        }
    }

    private String readNonEmpty(String prompt) {
        while (true) {
            String text = ask(prompt);
            if (!text.isEmpty()) {
                return text;
            }
            System.out.println("  Value cannot be empty.");
        }
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            String text = ask(prompt);
            try {
                int value = Integer.parseInt(text);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // fall through to the message below
            }
            System.out.println("  Please enter a whole number between " + min + " and " + max + ".");
        }
    }

    private String readUsn() {
        while (true) {
            String usn = ask("  USN (e.g. 1AB23CS001): ").toUpperCase();
            if (!usn.matches("[1-9][A-Z]{2}\\d{2}[A-Z]{2,3}\\d{3}")) {
                System.out.println("  Invalid USN format. Example: 1AB23CS001");
            } else if (findStudent(usn) != null) {
                System.out.println("  This USN is already registered.");
            } else {
                return usn;
            }
        }
    }

    /** Experiment 9 - Department.valueOf() converts the typed text into the enum constant. */
    private Department readDepartment() {
        while (true) {
            String text = ask("  Department (CSE / ISE / ECE / MECH): ");
            try {
                return Department.valueOf(text.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("  '" + text + "' is not a valid department. Choose CSE, ISE, ECE or MECH.");
            }
        }
    }

    private String readPhone() {
        while (true) {
            String phone = ask("  Phone (10 digits): ");
            if (phone.matches("[6-9]\\d{9}")) {
                return phone;
            }
            System.out.println("  Enter a valid 10-digit mobile number starting with 6-9.");
        }
    }

    private VehicleType readVehicleType() {
        int choice = readInt("  Vehicle type  1) Bike   2) Car   3) Bus/Truck : ", 1, 3);
        return VehicleType.values()[choice - 1];
    }

    private String readPlate() {
        while (true) {
            String plate = ask("  Vehicle number (e.g. KA01AB1234): ").toUpperCase().replace(" ", "");
            if (!plate.matches("[A-Z0-9-]{4,12}")) {
                System.out.println("  Use 4-12 letters/digits.");
            } else if (findVehicle(vehicles, plate) != null) {
                System.out.println("  This vehicle number is already registered.");
            } else {
                return plate;
            }
        }
    }
}
