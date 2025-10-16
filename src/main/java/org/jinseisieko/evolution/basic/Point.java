// src/main/java/org/jinseisieko/evolution/basic/Point.java
package org.jinseisieko.evolution.basic;

/**
 * Represents a point in a 2D toroidal space where both coordinates are confined to the interval [0, 1).
 * <p>
 * The space is topologically equivalent to a torus: moving past 1.0 in any direction wraps around to 0.0,
 * and moving below 0.0 wraps around to 1.0. All coordinates are automatically normalized using modular arithmetic.
 * <p>
 * This class supports basic spatial operations such as addition and inversion, all performed modulo 1
 * to respect the periodic boundary conditions of the environment.
 *
 * @author jinseisieko
 */
public class Point {
    private double x;
    private double y;

    public Point(double x,double y) {
        this.x = norm(x);
        this.y = norm(y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = norm(x);
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = norm(y);
    }
    
    /**
     * Adds another point to this one using toroidal (mod 1) arithmetic.
     * <p>
     * Each coordinate is summed and normalized to stay within [0, 1), respecting
     * the periodic boundary conditions of the torus. This operation modifies
     * the current point.
     *
     * @param other the point to add
     * @return this point, after the addition (for method chaining)
     * 
     * @author jinseisieko
     */
    public Point addPoint(Point other) {
        this.x = norm(this.x + other.x);
        this.y = norm(this.y + other.y);
        return this;
    }

    /**
     * Returns the additive inverse of this point on the unit torus [0,1) × [0,1).
     * <p>
     * The inverse point {@code p'} satisfies {@code (this + p') ≡ (0,0) (mod 1)}.
     * For each coordinate {@code c} of this point, the corresponding coordinate
     * of the inverse is computed as {@code (1.0 - c) % 1.0}, which ensures the
     * result lies in the interval [0, 1).
     * <p>
     * This operation is equivalent to reflecting the point through the origin
     * in the periodic space of the torus.
     *
     * @return a new {@code Point} representing the inverse of this point;
     *         the original point is unchanged.
     * 
     * @author jinseisieko
     */
    public Point inverse() {
        double invX = norm(1.0 - this.x);
        double invY = norm(1.0 - this.y);
        if (invX < 0) invX += 1.0;
        if (invY < 0) invY += 1.0;
        return new Point(invX, invY);
    }

    /**
     * Normalizes a coordinate to the [0, 1) interval, accounting for toroidal periodicity.
     * Examples: norm(1.3) = 0.3, norm(-0.2) = 0.8.
     * 
     * @param coordinate any real number
     * @return the equivalent value in [0, 1)
     *  
     * @author jinseisieko
     */
    static public double norm(double coordinate) {
        coordinate %= 1.0;
        if (coordinate < 0) coordinate += 1.0;
        return coordinate;
    }
}
