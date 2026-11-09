package mypack;

/**
 * Experiment 2 - Stack class that holds a MAXIMUM of 10 integers.
 * In the parking project it stores the last 10 parked slot numbers,
 * so "Undo last parking" simply pops the most recent one.
 */
public class IntStack {

    public static final int MAX = 10;

    private final int[] data = new int[MAX];
    private int top = -1;

    /** Pushes a value. Returns false (and does nothing) when the stack is full. */
    public boolean push(int value) {
        if (isFull()) {
            return false;
        }
        data[++top] = value;
        return true;
    }

    /** Pushes a value; when the stack is full the OLDEST entry is dropped (keeps the last 10). */
    public void pushOverwriteOldest(int value) {
        if (isFull()) {
            for (int i = 1; i < MAX; i++) {
                data[i - 1] = data[i];
            }
            top--;
        }
        data[++top] = value;
    }

    /** Removes and returns the top value. */
    public int pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack underflow: nothing to pop");
        }
        return data[top--];
    }

    /** Returns the top value without removing it. */
    public int peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return data[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isFull() {
        return top == MAX - 1;
    }

    public int size() {
        return top + 1;
    }

    /** Copy of the contents from bottom to top. */
    public int[] toArray() {
        int[] copy = new int[size()];
        System.arraycopy(data, 0, copy, 0, copy.length);
        return copy;
    }

    /** Prints the stack from top to bottom. */
    public void display() {
        if (isEmpty()) {
            System.out.println("  Stack is empty");
            return;
        }
        StringBuilder sb = new StringBuilder("  TOP -> ");
        for (int i = top; i >= 0; i--) {
            sb.append(data[i]);
            if (i > 0) {
                sb.append(" | ");
            }
        }
        sb.append("  <- BOTTOM   (size ").append(size()).append("/").append(MAX).append(")");
        System.out.println(sb);
    }
}
