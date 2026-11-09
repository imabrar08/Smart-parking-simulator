import mypack.DivisionByZeroException;
import mypack.SafeMath;
import mypack.VehicleType;

/**
 * Experiment 10 - reads values as Strings, converts them into wrapper objects
 * (Integer / Double), and calculates the total and average using
 * autoboxing / unboxing. Here the values are "hours parked" and the fee.
 */
public class FeeCalculator {

    private FeeCalculator() {
    }

    /** String -> Integer wrapper object. Throws NumberFormatException on bad input. */
    public static Integer parseHours(String text) {
        Integer hours = Integer.valueOf(text.trim());          // String -> wrapper object
        if (hours <= 0 || hours > 72) {                        // unboxing in the comparison
            throw new NumberFormatException("hours must be between 1 and 72");
        }
        return hours;
    }

    /** Fee = rate x hours, returned as a Double wrapper. */
    public static Double calculateFee(VehicleType type, String hoursText) {
        Integer hours = parseHours(hoursText);
        Double fee = (double) type.getRatePerHour() * hours;   // unboxing, then autoboxing
        return fee;
    }

    /**
     * Experiment 10 - bill THREE vehicles. Hours are read as Strings,
     * converted to Integer objects, then totals and averages are calculated.
     */
    public static void billThree(String[] labels, VehicleType[] types, String[] hoursText) {
        System.out.println("  Vehicle          Type        Hours        Fee (Rs.)");
        System.out.println("  " + "-".repeat(50));
        try {
            Integer totalHours = 0;
            Double totalFee = 0.0;
            for (int i = 0; i < 3; i++) {
                Integer hours = parseHours(hoursText[i]);       // String -> Integer
                Double fee = calculateFee(types[i], hoursText[i]);
                totalHours = totalHours + hours;                // unbox, add, autobox
                totalFee = totalFee + fee;                      // unbox, add, autobox
                System.out.printf("  %-16s %-10s %6d %14.2f%n", labels[i], types[i].getLabel(), hours, fee);
            }
            Double avgHours = SafeMath.calculateAverage(totalHours, 3);
            Double avgFee = SafeMath.calculateAverage(totalFee, 3);
            System.out.println("  " + "-".repeat(50));
            System.out.printf("  TOTAL   : %d hours, Rs. %.2f%n", totalHours, totalFee);
            System.out.printf("  AVERAGE : %.2f hours, Rs. %.2f per vehicle%n", avgHours, avgFee);
        } catch (NumberFormatException e) {
            System.out.println("  Invalid hours entered -> " + e.getMessage());
        } catch (DivisionByZeroException e) {
            System.out.println("  " + e.getMessage());
        } finally {
            System.out.println("  (billing routine finished)");
        }
    }
}
