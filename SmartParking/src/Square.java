/** Experiment 4 - Square sub class of Shape. A square marks a CAR. */
public class Square extends Shape {

    public Square() {
        super("Square");
    }

    @Override
    public void draw(String[][] canvas) {
        canvas[row][col] = "[#]";
    }

    @Override
    public void erase(String[][] canvas) {
        canvas[row][col] = EMPTY;
    }

    @Override
    public String describe() {
        return "Square [#]   = Car";
    }
}
