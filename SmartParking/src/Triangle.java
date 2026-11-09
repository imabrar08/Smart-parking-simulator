/** Experiment 4 - Triangle sub class of Shape. A triangle marks a BUS / TRUCK. */
public class Triangle extends Shape {

    public Triangle() {
        super("Triangle");
    }

    @Override
    public void draw(String[][] canvas) {
        canvas[row][col] = "/^\\";
    }

    @Override
    public void erase(String[][] canvas) {
        canvas[row][col] = EMPTY;
    }

    @Override
    public String describe() {
        return "Triangle /^\\ = Bus/Truck";
    }
}
