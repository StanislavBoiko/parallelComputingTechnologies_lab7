import mpi.MPI;

public class Lab7 {

    final static int MATRIXSIZE = 3000;
    final static int MASTER = 0;

    public static void main(String[] args) {

        MPI.Init(args);

        final int rank = MPI.COMM_WORLD.Rank();
//        final int size = MPI.COMM_WORLD.Size();
//        System.out.println(size);
        final int size = 12;



        final int rowsPerProcess = MATRIXSIZE / size;

        int[][] matrixA = new int[MATRIXSIZE][MATRIXSIZE];
        int[][] matrixB = new int[MATRIXSIZE][MATRIXSIZE];
        int[][] result = new int[MATRIXSIZE][MATRIXSIZE];
        int[][] matrixAPart = new int[rowsPerProcess][MATRIXSIZE];
        int[][] resultPart = new int[rowsPerProcess][MATRIXSIZE];

        final long start;

        if (rank == MASTER) {
            matrixA = Utilitary.generateRandomMatrix(MATRIXSIZE,1000);
            matrixB = Utilitary.generateRandomMatrix(MATRIXSIZE,1000);

            start = System.currentTimeMillis();
        } else {
            start = 0;
        }

        MPI.COMM_WORLD.Bcast(matrixB, 0, matrixB.length, MPI.OBJECT, MASTER);
        MPI.COMM_WORLD.Scatter(matrixA, 0, rowsPerProcess, MPI.OBJECT,
                matrixAPart, 0, rowsPerProcess, MPI.OBJECT, MASTER);

        for (int i = 0; i < rowsPerProcess; i++) {
            for (int j = 0; j < MATRIXSIZE; j++) {
                int sum = 0;
                for (int k = 0; k < MATRIXSIZE; k++) {
                    sum += matrixAPart[i][k] * matrixB[k][j];
                }
                resultPart[i][j] = sum;
            }
        }

        MPI.COMM_WORLD.Gather(resultPart, 0, rowsPerProcess, MPI.OBJECT,
                result, 0, rowsPerProcess, MPI.OBJECT, MASTER);

        final long finish = System.currentTimeMillis();
        if (rank == MASTER) {

            System.out.println("matrix: " + MATRIXSIZE + "x" + MATRIXSIZE + "\n time: " + (finish - start) + " ms");;
        }

        MPI.Finalize();
    }
}