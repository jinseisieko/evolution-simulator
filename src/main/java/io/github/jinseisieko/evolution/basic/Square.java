// src/main/java/io.github/jinseisieko/evolution/basic/Square.java
package io.github.jinseisieko.evolution.basic;

/**
 * Represents an axis-aligned square region in a 2D toroidal space [0,1) × [0,1),
 * defined by a center point and a half-size (distance from center to any side).
 * The square inherits positional behavior from {@link MovingObject},
 * allowing it to be moved or shifted over time.
 * <p>
 * This class supports spatial queries such as point containment and intersection
 * with other squares, all computed using toroidal axis-aligned bounding box (AABB)
 * logic to respect periodic boundary conditions.
 *
 * @author jinseisieko
 */
public class Square extends MovingObject implements Shape {

    private double halfSize;
    private static final ShapeType SHAPE_TYPE = ShapeType.SQUARE;

    /**
     * Constructs a square with the specified center and half-size.
     *
     * @param center   the center point of the square; must not be {@code null} <p>
     * @param halfSize the half-size of the square (distance from center to side; must be ≥ 0) <p>
     * @throws IllegalArgumentException if {@code center} is {@code null} or {@code halfSize} is negative <p>
     *
     * @author jinseisieko
     */
    public Square(Point center, double halfSize) {
        super(center);
        if (halfSize < 0) {
            throw new IllegalArgumentException("HalfSize cannot be less than zero");
        }
        this.halfSize = halfSize;
    }

    /**
     * Constructs a square from coordinate values and a half-size.
     *
     * @param x        the x-coordinate of the center <p>
     * @param y        the y-coordinate of the center <p>
     * @param halfSize the half-size of the square (distance from center to side; must be ≥ 0) <p>
     * @throws IllegalArgumentException if {@code halfSize} is negative <p>
     *
     * @author jinseisieko
     */
    public Square(double x, double y, double halfSize) {
        super(x, y);
        if (halfSize < 0) {
            throw new IllegalArgumentException("halfSize cannot be less than zero");
        }
        this.halfSize = halfSize;
    }

    /**
     * Returns the center point of this square.
     * <p>
     * The returned point is a defensive copy to prevent external modification
     * of the internal position.
     *
     * @return a new {@code Point} representing the center of the square <p>
     *
     * @author jinseisieko
     */
    public Point getCenter() {
        return getPosition();
    }

    /**
     * Returns the half-size of this square (distance from center to any side).
     *
     * @return the half-size (≥ 0) <p>
     *
     * @author jinseisieko
     */
    public double getHalfSize() {
        return halfSize;
    }

    /**
     * Sets the half-size of this square.
     * <p>
     * The value must be non-negative to maintain geometric validity.
     *
     * @param halfSize the new half-size (must be ≥ 0) <p>
     * @throws IllegalArgumentException if {@code halfSize} is negative <p>
     *
     * @author jinseisieko
     */
    public void setHalfSize(double halfSize) {
        if (halfSize < 0) {
            throw new IllegalArgumentException("HalfSize cannot be less than zero");
        }
        this.halfSize = halfSize;
    }

    /**
     * Returns the full side length of this square.
     *
     * @return the side length, equal to twice the half-size <p>
     *
     * @author jinseisieko
     */
    public double getSideLenght() {
        return this.halfSize * 2;
    }

    /**
     * Returns the geometric shape type associated with this class.
     *
     * @return {@link ShapeType#SQUARE} <p>
     *
     * @author jinseisieko
     */
    @Override
    public ShapeType getShapeType() {
        return SHAPE_TYPE;
    }

    /**
     * Checks whether this square contains the specified point (including the boundary).
     * <p>
     * The test uses toroidal axis-aligned logic: for each dimension, the minimal
     * wrapped distance from the point to the center is compared against the half-size.
     * The point is inside if it lies within the half-size range in both X and Y dimensions.
     *
     * @param point the point to test; must not be {@code null} <p>
     * @return {@code true} if the point lies inside or on the square, {@code false} otherwise <p>
     * @throws IllegalArgumentException if {@code point} is {@code null} <p>
     *
     * @author jinseisieko
     */
    public boolean contains(Point point) {
        if (point == null) {
            throw new IllegalArgumentException("Point cannot be null");
        }
        double diffX = Math.abs(point.getX() - this.getX());
        double diffY = Math.abs(point.getY() - this.getY());
        double dx = Math.min(diffX, 1 - diffX);
        double dy = Math.min(diffY, 1 - diffY);
        return dx - this.getHalfSize() <= 1e-12 && dy - this.getHalfSize() <= 1e-12;
    }

    /**
     * Checks whether this square intersects another square (including edge contact).
     * <p>
     * Two squares intersect if, in both X and Y dimensions, the toroidal distance
     * between their centers is less than or equal to the sum of their half-sizes
     * (within floating-point tolerance).
     *
     * @param other the other square to test; must not be {@code null} <p>
     * @return {@code true} if the squares overlap or touch, {@code false} otherwise <p>
     * @throws IllegalArgumentException if {@code other} is {@code null} <p>
     *
     * @author jinseisieko
     */
    public boolean intersects(Square other) {
        if (other == null) {
            throw new IllegalArgumentException("Other square cannot be null");
        }
        double diffX = Math.abs(other.getX() - this.getX());
        double diffY = Math.abs(other.getY() - this.getY());
        double dx = Math.min(diffX, 1 - diffX);
        double dy = Math.min(diffY, 1 - diffY);
        return dx - (this.getHalfSize() + other.getHalfSize()) <= 1e-12 && dy - (this.getHalfSize() + other.getHalfSize()) <= 1e-12;
    }
}