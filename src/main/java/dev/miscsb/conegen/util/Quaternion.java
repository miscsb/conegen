package dev.miscsb.conegen.util;

import org.ejml.data.DenseMatrix64F;

/**
 * Quaternion class for dealing with the quaternion extension of the complex numbers, equipped
 * with basic operations.
 * @author antimony
 */
public class Quaternion {
    /**
     * The identity quaternion (1, 0, 0, 0); the quaternion returned by {@code Quaternion(1, 0, 0, 0)}
     */
    public static Quaternion IDENTITY = new Quaternion(1, 0, 0, 0);
    protected double scalar;
    protected double[] vector;

    /**
     * Constructs a quaternion from a given scalar and vector part
     * @param scalar the scalar part of the quaternion
     * @param vector the vector part of the quaternion
     */
    public Quaternion(double scalar, double[] vector) {
        this.scalar = scalar;
        this.vector = vector;
    }

    /**
     * Constructs a quaternion w + xi + yj + zk from the given coefficients. 
     * @param w the real part
     * @param x the coefficient of i
     * @param y the coefficient of j
     * @param z the coefficient of k
     */
    public Quaternion(double w, double x, double y, double z) {
        this(w, new double[] {x, y, z});
    }

    /**
     * Constructs the identity quaternion (1, 0, 0, 0)
     */
    public Quaternion() {
        this(1, 0, 0, 0);
    }

    /**
     * Returns the string representation of this quaternion
     */
    @Override
    public String toString() {
        return String.format("%f + %fi + %fj + %fk", 
            this.scalar, 
            this.vector[0], 
            this.vector[1], 
            this.vector[2]
        );
    }

    /**
     * Adds two quaternions
     * @param qa the first addend
     * @param qb the second addend
     * @return the resulting quaternion after addition
     */
    public static Quaternion add(Quaternion qa, Quaternion qb) {
        return new Quaternion(qa.scalar + qb.scalar, MatrixUtil.vectorAdd(qa.vector, qb.vector));
    }

    /**
     * Subtracts one quaternion from another quaternion
     * @param qa the subtrahend
     * @param qb the minuend
     * @return the resulting quaternion after subtraction
     */
    public static Quaternion subtract(Quaternion qa, Quaternion qb) {
        return new Quaternion(qa.scalar - qb.scalar, MatrixUtil.vectorSubtract(qa.vector, qb.vector));
    }

    /**
     * Scales the given quaternion by a scalar.
     * @param q the quaternion
     * @param s the scalar
     * @return the scaled quaternion
     */
    public static Quaternion scale(Quaternion q, double s) {
        return new Quaternion(q.scalar * s, MatrixUtil.vectorScale(q.vector, s));
    }

    /**
     * Multiplies two quaternions
     * @param qa the first quaternion
     * @param qb the second quaternion
     * @return the product of the two quaternions
     */
    public static Quaternion multiply(Quaternion qa, Quaternion qb) {
        double scalar = qa.scalar * qb.scalar - MatrixUtil.vectorDotProduct(qa.vector, qb.vector);
        double[] vector = MatrixUtil.vectorAdd(
            MatrixUtil.vectorScale(qa.vector, qb.scalar),
            MatrixUtil.vectorScale(qb.vector, qa.scalar),
            MatrixUtil.vectorCrossProduct(qa.vector, qb.vector)
        );
        return new Quaternion(scalar, vector);
    }

    /**
     * Multiplies the given quaternions, 1 by 1. For example, calling multiply(a, b, c) will return the value of abc.
     * @param qs the quaternions to multiply, in order
     * @return the result of the multiplications; if no quaternions are passed, then the identity quaternion will be returned
     */
    public static Quaternion multiply(Quaternion... qs) {
        Quaternion product = Quaternion.IDENTITY;
        for (Quaternion q : qs) {
            product = Quaternion.multiply(product, q);
        }
        return product;
    }

    /**
     * Calculates the inner product of two quaternions.
     * @param qa the first quaternion
     * @param qb the second quaternion
     * @return the inner product of the passed quaternions
     */
    public static double innerProduct(Quaternion qa, Quaternion qb) {
        return MatrixUtil.vectorDotProduct(qa.toArray(), qb.toArray());
    }

    /**
     * Gets the norm of a quaternion.
     * @param q the quaternion
     * @return the norm of the quaternion
     */
    public static double norm(Quaternion q) {
        return MatrixUtil.length(q.toArray());
    }

    /**
     * Normalizes a quaternion (divides a quaternion by its norm).
     * @param q the quaternion
     * @return the resulting unit quaternion
     */
    public static Quaternion normalized(Quaternion q) {
        return scale(q, 1. / norm(q));
    }

    /**
     * Finds the conjugate of a quaternion
     * @param q the quaternion
     * @return the conjugate of the passed quaternion
     */
    public static Quaternion conjugate(Quaternion q) {
        return new Quaternion(q.scalar, MatrixUtil.vectorScale(q.vector, -1));
    }

    /**
     * Finds the inverse of a quaternion
     * @param q the quaternion
     * @return the inverse of the passed quaternion
     */
    public static Quaternion inverse(Quaternion q) {
        return scale(
            conjugate(q),
            1. / MatrixUtil.lengthSquared(q.toArray())
        );
    }

    /**
     * Represents a quaternion as its equivalent rotation matrix.
     * @param q the quaternion
     * @return the equivalent rotation matrix
     */
    public static DenseMatrix64F asRotationMatrix(Quaternion q) {
        double w = q.scalar;
        double[] vector = q.vector;
        double x = vector[0];
        double y = vector[1];
        double z = vector[2];

        return new DenseMatrix64F(new double[][] {
            { norm(q),      2*(x*y-w*z),        2*(w*y+x*z) },
            { 2*(x*y+w*z),  w*w-x*x+y*y-z*z,    2*(y*z-w*x) },
            { 2*(x*z-w*y),  2*(w*x+y*z),        w*w-x*x-y*y+z*z}
        });
    }

    /**
     * Gets the specified coefficient (0 -> w, 1 -> x, 2 -> y, 3 -> z).
     * @param n the coefficient index
     * @return the coefficent value
     */
    public double get(int n) {
        return n == 0 ? this.scalar : this.vector[n-1];
    }

    /**
     * Converts this quaternion to an equivalent vector (w, x, y, z)
     * @return the vector representation of this quaternion.
     */
    protected double[] toArray() {
        double[] coeff = new double[4];
        coeff[0] = this.scalar;
        System.arraycopy(this.vector, 0, coeff, 1, 3);
        return coeff;
    }
}
