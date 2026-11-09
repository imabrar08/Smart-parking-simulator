package mypack;

/**
 * Experiment 3 - Student class with USN, Name, Branch and Phone as variables.
 * In the parking project a Student is the owner of a registered vehicle.
 */
public class Student {

    private final String usn;
    private final String name;
    private final String branch;
    private final String phone;

    public Student(String usn, String name, String branch, String phone) {
        this.usn = usn;
        this.name = name;
        this.branch = branch;
        this.phone = phone;
    }

    public String getUsn() {
        return usn;
    }

    public String getName() {
        return name;
    }

    public String getBranch() {
        return branch;
    }

    public String getPhone() {
        return phone;
    }

    /** Prints the table heading used when listing students. */
    public static void printHeader() {
        System.out.printf("  %-12s %-18s %-8s %-12s%n", "USN", "Name", "Branch", "Phone");
        System.out.println("  " + "-".repeat(53));
    }

    /** Prints this student as one table row. */
    public void display() {
        System.out.printf("  %-12s %-18s %-8s %-12s%n", usn, name, branch, phone);
    }

    @Override
    public String toString() {
        return name + " (" + usn + ", " + branch + ")";
    }
}
