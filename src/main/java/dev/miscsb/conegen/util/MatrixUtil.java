package dev.miscsb.conegen.util;

import java.util.Arrays;

/**
 * Utilily class for basic operations on matrixes and vectors.
 * @author antimony
 */
public class MatrixUtil {
    /**
     * Converts a point to a unit vector (used for constructing a unit vector that represents an axis of rotation).
     * @param p the point on the axis
     * @return a vector of length 3
     */
    public static double[] toUnitVector(Point3D p) {
        double d = length(new double[] {p.x, p.y, p.z});
        return new double[] {
            p.x / d,
            p.y / d,
            p.z / d
        };
    }

    /**
     * Creates a unit vector (used for constructing a unit vector that represents an axis of rotation).
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @param z the z-coordinate of the point
     * @return a vector of length 3
     */
    public static double[] toUnitVector(double x, double y, double z) {
        return toUnitVector(new double[] {x, y, z});
    }

    /**
     * Creates a unit vector (used for constructing a unit vector that represents an axis of rotation).
     * @param v the original vector
     * @return a vector of length 3
     */
    public static double[] toUnitVector(double[] v) {
        double d = length(v);
        return new double[] {
            v[0] / d,
            v[1] / d,
            v[2] / d
        };
    }

    /**
     * Calculates the dot product of two vectors.
     * @param a the first vector
     * @param b the second vector
     * @return value of a·b
     * @throws IllegalArgumentException if the vectors are not of equal length
     */
    public static double vectorDotProduct(double[] a, double[] b) {
        int length = a.length;
        if (b.length != length) throw new IllegalArgumentException();

        double sum = 0;
        for (int i = 0; i < length; i++) {
            sum += a[i] * b[i];
        }

        return sum;
    }

    /**
     * Scales the vector by the specified scalar.
     * @param a the vector
     * @param s the scalar
     * @return the scaled vector
     */
    public static double[] vectorScale(double[] a, double s) {
        double[] scaled = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            scaled[i] = a[i] * s;
        }
        return scaled;
    }

    /**
     * Calculates the cross product of two vectors a and b.
     * @param a the first vector
     * @param b the second vector
     * @return the vector representing a x b
     * @throws IllegalArgumentException if the vectors are not of equal length
     */
    public static double[] vectorCrossProduct(double[] a, double[] b) {
        if (a.length != 3 || b.length != 3) throw new IllegalArgumentException();
        return new double[] {
            a[1]*b[2] - a[2]*b[1],
            a[2]*b[0] - a[0]*b[2],
            a[0]*b[1] - a[1]*b[0]
        };
    }

    /**
     * Calculates the sum of two vectors a and b.
     * @param a the first vector
     * @param b the second vector
     * @return the vector representing a + b
     * @throws IllegalArgumentException if the vectors are not of equal length
     */
    public static double[] vectorAdd(double[] a, double[] b) {
        int length = a.length;
        if (b.length != length) throw new IllegalArgumentException();

        double[] sum = new double[length];
        for (int i = 0; i < length; i++) {
            sum[i] = a[i] + b[i];
        }

        return sum;
    }

    /**
     * Calculates the sum of all of the passed vectors.
     * @param vectors the vectors to add
     * @return the sum of all of the passed vectors
     * @throws IllegalArgumentException if all of the vectors are not of equal length
     */
    public static double[] vectorAdd(double[]... vectors) {
        int length = vectors[0].length;
        for (double[] vector : vectors) {
            if (vector.length != length) throw new IllegalArgumentException();
        }
        double[] sum = new double[length];
        for (int i = 0; i < length; i++) {
            for (double[] vector : vectors) {
                sum[i] += vector[i];
            }
        }

        return sum;
    }

    /**
     * Calculates the difference of two vectors a and b.
     * @param a the subtrahend vector
     * @param b the minuend vector
     * @return the vector representing a - b
     * @throws IllegalArgumentException if the vectors are not of equal length
     */
    public static double[] vectorSubtract(double[] a, double[] b) {
        int length = a.length;
        if (b.length != length) throw new IllegalArgumentException();

        double[] sum = new double[length];
        for (int i = 0; i < length; i++) {
            sum[i] = a[i] - b[i];
        }

        return sum;
    }

    /**
     * Finds the dimensions of a matrix (rows x columns).
     * @param A the matrix
     * @return the dimensions of the passed matrix
     */
    public static int[] dimensions(double[][] A) {
        int m = A.length;
        if (m == 0) return new int[] {0, 0};
        int n = A[0].length;
        return new int[] {m, n};
    }

    /**
     * Finds the square of the magnitude of the passed vector.
     * @param u the vector
     * @return the square of the length of the vector
     */
    public static double lengthSquared(double[] u) {
        double sum = 0;
        for (double val : u) {
            sum += val * val;
        }

        return sum;
    }

    /**
     * Finds the magnitude of the passed vector.
     * @param u the vector
     * @return the length of the vector
     */
    public static double length(double... u) {
        return Math.sqrt(lengthSquared(u));
    }

    public static boolean colinear(double[] v1, double[] v2) {
        if (v1.length != v2.length) return false;
        double factor = Double.NaN;
        boolean multiplierFound = false;
        for (int i = 0; i < v1.length; i++) {
            if (Math.abs(v2[i]) < 1E-6) {
                if (Math.abs(v1[i]) > 1E-6) return false;
            } else if (!multiplierFound) {
                factor = v2[i] / v1[i];
                multiplierFound = true;
            } else {
                if (Math.abs(v2[i] - factor * v1[i]) > 1E-6) return false;
            }
        }
        return true;
    }

    /**
     * Creates a keep copy of the passed matrix
     * @param A the matrix to copy
     * @return a new, identical matrix
     */
    public static double[][] matrixCopy(double[][] A) {
        int[] dimensions = dimensions(A);
        int m = dimensions[0];
        int n = dimensions[1];

        double[][] copy = new double[m][n];
        for (int i = 0; i < m; i++) {
            System.arraycopy(A[i], 0, copy[i], 0, n);
        }

        return copy;
    }

    /**
     * Generates a blank matrix n x n.
     * @param n the size of the matrix
     * @return the square matrix
     */
    public static double[][] squareMatrix(int n) {
        return new double[n][n];
    }

    /**
     * Generates the identity matrix n x n
     * @param n the size of the matrix
     * @return the identity matrix
     */
    public static double[][] identityMatrix(int n) {
        double[][] A = squareMatrix(n);
        for (int i = 0; i < n; i++) {
            A[i][i] = 1;
        }
        return A;
    }

    /**
     * Gets a column from a matrix (0-indexed).
     * @param A the matrix
     * @param j the column number
     * @return a vector representing the values in column j of matrix A
     */
    public static double[] col(double[][] A, int j) {
        int m = dimensions(A)[0];
        double[] col = new double[m];
        for (int i = 0; i < m; i++) {
            col[i] = A[i][j];
        }
        return col;
    }

    /**
     * Gets a row from a matrix (0-indexed).
     * @param A the matrix
     * @param i the row number
     * @return a vector representing the values in row i of matrix A
     */
    public static double[] row(double[][] A, int j) {
        int n = dimensions(A)[1];
        double[] row = new double[n];
        System.arraycopy(A[j], 0, row, 0, n);
        return row;
    }

    /**
     * Multiplies the matrix by a scalar
     * @param A the matrix
     * @param s the scalar
     * @return the scaled matrix
     */
    public static double[][] matrixScalarMultiply(double[][] A, double s) {
        double[][] scaled = matrixCopy(A);
        int[] dimensions = dimensions(A);
        int m = dimensions[0];
        int n = dimensions[1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                scaled[i][j] = A[i][j] * s;
            }
        }
        return scaled;
    }

    /**
     * Adds two matrices A and B.
     * @param A the first addend matrix
     * @param B the second addend matrix
     * @return the sum matrix
     * @throws IllegalArgumentException if the matrices do not have the same dimensions
     */
    public static double[][] matrixAdd(double[][] A, double[][] B) {
        if (!Arrays.equals(dimensions(A), dimensions(B))) throw new IllegalArgumentException();
        double[][] sum = matrixCopy(A);
        int m = dimensions(A)[0];
        for (int i = 0; i < m; i++) {
            sum[i] = vectorAdd(A[i], B[i]);
        }
        return sum;
    }

    /**
     * Adds two matrices A and B.
     * @param A the first addend matrix
     * @param B the second addend matrix
     * @return the sum matrix
     * @throws IllegalArgumentException if the matrices do not have the same dimensions
     */
    public static double[][] matrixSubtract(double[][] A, double[][] B) {
        if (!Arrays.equals(dimensions(A), dimensions(B))) throw new IllegalArgumentException();
        double[][] sum = matrixCopy(A);
        int m = dimensions(A)[0];
        for (int i = 0; i < m; i++) {
            sum[i] = vectorSubtract(A[i], B[i]);
        }
        return sum;
    }

    /**
     * Multiplies two matrices A and B.
     * @param A the first factor
     * @param B the second factor
     * @return the value of AB
     */
    public static double[][] matrixMultiply(double[][] A, double[][] B) {
        int m = dimensions(A)[0];
        int n = dimensions(B)[1];
        double[][] AB = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                AB[i][j] = vectorDotProduct(row(A, i), col(B, j));
            }
        }
        return AB;
    }
}
