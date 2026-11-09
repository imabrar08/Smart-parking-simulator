package mypack;

/**
 * Experiment 7 - user-defined (custom) checked exception for division by zero.
 * It is raised with "throw" in SafeMath and handled with try / catch / finally.
 */
public class DivisionByZeroException extends Exception {

    private static final long serialVersionUID = 1L;

    public DivisionByZeroException(String message) {
        super(message);
    }
}
