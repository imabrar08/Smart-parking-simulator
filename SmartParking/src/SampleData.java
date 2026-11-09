import mypack.Student;
import mypack.Vehicle;
import mypack.VehicleType;

import java.util.ArrayList;
import java.util.List;

/** Ready-made students and vehicles so the project can be demonstrated instantly. */
public class SampleData {

    private SampleData() {
    }

    private static final String[][] ROWS = {
        // USN, Name, Branch, Phone, VehicleType, Vehicle No
        {"1AB23CS001", "Aarav Sharma",  "CSE",  "9845012301", "CAR",  "KA01AB1001"},
        {"1AB23CS014", "Priya Nair",    "CSE",  "9845012302", "BIKE", "KA01AB1002"},
        {"1AB23CS032", "Rohan Gowda",   "CSE",  "9845012303", "BIKE", "KA01AB1003"},
        {"1AB23IS005", "Sneha Patil",   "ISE",  "9845012304", "CAR",  "KA02CD2001"},
        {"1AB23IS019", "Imran Pasha",   "ISE",  "9845012305", "BUS",  "KA02CD2002"},
        {"1AB23EC008", "Divya Rao",     "ECE",  "9845012306", "BIKE", "KA03EF3001"},
        {"1AB23EC021", "Karthik Hegde", "ECE",  "9845012307", "CAR",  "KA03EF3002"},
        {"1AB23EC040", "Ananya Iyer",   "ECE",  "9845012308", "BIKE", "KA03EF3003"},
        {"1AB23ME003", "Vikram Joshi",  "MECH", "9845012309", "BUS",  "KA04GH4001"},
        {"1AB23ME017", "Meera Reddy",   "MECH", "9845012310", "CAR",  "KA04GH4002"},
    };

    /** Creates a fresh list of 10 vehicles (each with its Student owner). */
    public static List<Vehicle> createVehicles() {
        List<Vehicle> list = new ArrayList<>();
        for (String[] r : ROWS) {
            Student s = new Student(r[0], r[1], r[2], r[3]);
            list.add(new Vehicle(s, r[5], VehicleType.valueOf(r[4])));
        }
        return list;
    }
}
