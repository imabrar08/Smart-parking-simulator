package mypack;

/**
 * Experiment 7 - helper methods that RAISE the custom DivisionByZeroException.
 */
public class SafeMath {

    private SafeMath() {
    }

    /** Divides a by b; throws DivisionByZeroException when b is zero. */
    public static double divide(double a, double b) throws DivisionByZeroException {
        if (b == 0) {
            throw new DivisionByZeroException("Division by zero: denominator is 0 (nothing to divide by)");
        }
        return a / b;
    }

    /** Average = total / count; throws DivisionByZeroException when count is zero. */
    public static double calculateAverage(double total, int count) throws DivisionByZeroException {
        if (count == 0) {
            throw new DivisionByZeroException("Cannot calculate an average over 0 items");
        }
        return total / count;
    }
}
