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

    /**
     * Constructs a point with the specified coordinates in toroidal space.
     * <p>
     * Both input coordinates are immediately normalized to the range [0, 1)
     * using modular arithmetic to respect the periodic boundary conditions.
     *
     * @param x the initial X coordinate (any real number) <p>
     * @param y the initial Y coordinate (any real number) <p>
     *
     * @author jinseisieko
     */
    public Point(double x, double y) {
        this.x = norm(x);
        this.y = norm(y);
    }

    /**
     * Returns the normalized X coordinate of this point.
     * <p>
     * The value is guaranteed to be in the interval [0, 1).
     *
     * @return the X coordinate in [0, 1) <p>
     *
     * @author jinseisieko
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the X coordinate of this point to the specified value.
     * <p>
     * The input is automatically normalized to the range [0, 1) to maintain
     * consistency with the toroidal space model.
     *
     * @param x the new X coordinate (any real number) <p>
     *
     * @author jinseisieko
     */
    public void setX(double x) {
        this.x = norm(x);
    }

    /**
     * Returns the normalized Y coordinate of this point.
     * <p>
     * The value is guaranteed to be in the interval [0, 1).
     *
     * @return the Y coordinate in [0, 1) <p>
     *
     * @author jinseisieko
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the Y coordinate of this point to the specified value.
     * <p>
     * The input is automatically normalized to the range [0, 1) to maintain
     * consistency with the toroidal space model.
     *
     * @param y the new Y coordinate (any real number) <p>
     *
     * @author jinseisieko
     */
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
     * @param other the point to add <p>
     * @return this point, after the addition (for method chaining) <p>
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
     *         the original point is unchanged. <p>
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
     * Computes the shortest Euclidean distance from this point to another point
     * in the 2D toroidal space [0,1) × [0,1).
     * <p>
     * Due to the periodic boundary conditions, the distance accounts for wrap-around:
     * moving past 1.0 in any direction wraps to 0.0, and vice versa.
     * The result is the minimal possible distance considering all toroidal paths.
     *
     * @param other the target point to measure distance to <p>
     * @return the toroidal Euclidean distance between this point and {@code other} <p>
     * 
     * @author jinseisieko
     */
    public double distanceTo(Point other) {
        if (other == null) {
            throw new IllegalArgumentException("Other point cannot be null");
        }
        return Point.distanceBetween(this, other);
    }

    /**
     * Computes the shortest Euclidean distance between two points in the 2D toroidal space [0,1) × [0,1).
     * <p>
     * This static method calculates distance while respecting periodic boundaries:
     * the space wraps around at 0.0 and 1.0 in both dimensions, so the shortest path
     * may cross the edge of the unit square.
     *
     * @param point1 the first point <p>
     * @param point2 the second point <p>
     * @return the toroidal Euclidean distance between {@code point1} and {@code point2} <p>
     * @throws IllegalArgumentException if either {@code point1} or {@code point2} is {@code null} <p>
     * 
     * @author jinseisieko
     */
    static public double distanceBetween(Point point1, Point point2) {
        if (point1 == null || point2 == null) {
            throw new IllegalArgumentException("points cannot be null");
        }
        double dx = norm(point2.x - point1.x);
        double dy = norm(point2.y - point1.y);
        dx = Math.min(dx, 1.0 - dx);
        dy = Math.min(dy, 1.0 - dy);
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Normalizes a coordinate to the [0, 1) interval, accounting for toroidal periodicity.
     * <p>
     * Examples: norm(1.3) = 0.3, norm(-0.2) = 0.8.
     * 
     * @param coordinate any real number <p>
     * @return the equivalent value in [0, 1) <p>
     *  
     * @author jinseisieko
     */
    static public double norm(double coordinate) {
        coordinate %= 1.0;
        if (coordinate < 0) coordinate += 1.0;
        return coordinate;
    }
}