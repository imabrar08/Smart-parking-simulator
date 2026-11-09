/**
 * Experiment 5 - class Rectangle that implements the Resizable interface
 * and implements the resize methods.
 */
public class Rectangle implements Resizable {

    private int width;
    private int height;

    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void resizeWidth(int width) {
        if (width < 0) {
            throw new IllegalArgumentException("Width cannot be negative");
        }
        this.width = width;
    }

    @Override
    public void resizeHeight(int height) {
        if (height < 0) {
            throw new IllegalArgumentException("Height cannot be negative");
        }
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int area() {
        return width * height;
    }

    @Override
    public String toString() {
        return "Rectangle[" + width + " x " + height + "]";
    }
}
