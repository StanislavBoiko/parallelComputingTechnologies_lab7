import java.util.Random;

public class Utilitary {

    public static int[][] generateRandomMatrix(int len, int maxSize) {
        final Random random = new Random();
        final int[][] matrix = new int[len][len];

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                matrix[i][j] = random.nextInt(maxSize) + 2;
            }
        }

        return matrix;
    }

}