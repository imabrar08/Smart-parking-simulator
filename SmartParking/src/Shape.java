/**
 * Experiment 4 - class named Shape with member data and the two member
 * functions draw() and erase(). Circle, Triangle and Square override them
 * (polymorphism). A shape is drawn on the parking map when a vehicle parks
 * and erased when it leaves.
 */
public abstract class Shape {

    /** What an empty slot looks like on the map. */
    public static final String EMPTY = " . ";

    protected final String name;   // member data
    protected int row;             // member data
    protected int col;             // member data

    protected Shape(String name) {
        this.name = name;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public String getName() {
        return name;
    }

    /** Draws this shape into the given canvas at (row, col). */
    public abstract void draw(String[][] canvas);

    /** Erases this shape from the canvas. */
    public abstract void erase(String[][] canvas);

    /** One-line description used in messages. */
    public abstract String describe();
}
