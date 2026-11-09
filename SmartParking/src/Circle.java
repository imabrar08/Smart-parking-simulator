/** Experiment 4 - Circle sub class of Shape. A circle marks a BIKE. */
public class Circle extends Shape {

    public Circle() {
        super("Circle");
    }

    @Override
    public void draw(String[][] canvas) {
        canvas[row][col] = "(o)";
    }

    @Override
    public void erase(String[][] canvas) {
        canvas[row][col] = EMPTY;
    }

    @Override
    public String describe() {
        return "Circle (o)   = Bike";
    }
}
