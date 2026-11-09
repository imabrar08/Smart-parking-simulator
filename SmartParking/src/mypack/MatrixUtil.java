package mypack;

/**
 * Experiment 1 - adds TWO matrices of suitable order N.
 * In the parking project the two matrices are the occupancy grids of Floor 1 and Floor 2.
 */
public class MatrixUtil {

    private MatrixUtil() {
    }

    /** Returns a + b. Both matrices must have the same order. */
    public static int[][] addMatrices(int[][] a, int[][] b) {
        if (a.length != b.length || a[0].length != b[0].length) {
            throw new IllegalArgumentException("Matrices must have the same order");
        }
        int[][] result = new int[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }
        return result;
    }

    /** Sum of all elements. */
    public static int sum(int[][] m) {
        int total = 0;
        for (int[] row : m) {
            for (int v : row) {
                total += v;
            }
        }
        return total;
    }

    /** Pretty-prints a matrix with an optional row label and a title. */
    public static void print(int[][] m, String title, String[] rowLabels) {
        System.out.println("  " + title);
        for (int i = 0; i < m.length; i++) {
            StringBuilder sb = new StringBuilder("   ");
            if (rowLabels != null) {
                sb.append(String.format("%-5s", rowLabels[i]));
            }
            sb.append("[ ");
            for (int j = 0; j < m[i].length; j++) {
                sb.append(m[i][j]).append(' ');
            }
            sb.append(']');
            System.out.println(sb);
        }
    }

    public static void print(int[][] m, String title) {
        print(m, title, null);
    }
}
