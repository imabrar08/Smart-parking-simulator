package mypack;

/**
 * Experiment 9 - Enumeration called Department containing CSE, ISE, ECE and MECH.
 * Each department owns one ROW of the parking grid on every floor (its "zone").
 * The user types a department name and Department.valueOf() selects the constant.
 */
public enum Department {
    CSE("Computer Science & Engineering"),
    ISE("Information Science & Engineering"),
    ECE("Electronics & Communication"),
    MECH("Mechanical Engineering");

    private final String fullName;

    Department(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    /** Row of the parking grid reserved for this department. */
    public int zoneRow() {
        return ordinal();
    }

    /**
     * Experiment 9 - converts typed text into an enum constant using valueOf().
     * Throws IllegalArgumentException when the text is not a valid department.
     */
    public static Department fromInput(String text) {
        return Department.valueOf(text.trim().toUpperCase());
    }
}
