// src/main/java/io.github/jinseisieko/evolution/basic/Circle.java
package io.github.jinseisieko.evolution.basic;

/**
 * Represents a circular region in a 2D toroidal space [0,1) × [0,1), defined by a center point
 * and a radius. The circle inherits positional behavior from {@link MovingObject},
 * allowing it to be moved or shifted over time.
 * <p>
 * This class supports spatial queries such as point containment and intersection with other circles,
 * all computed using toroidal Euclidean distance to respect periodic boundary conditions.
 *
 * @author jinseisieko
 */
public class Circle extends MovingObject implements Shape {

    private double radius;
    private static final ShapeType SHAPE_TYPE = ShapeType.CIRCLE;

    /**
     * Constructs a circle with the specified center and radius.
     *
     * @param center the center point of the circle; must not be {@code null} <p>
     * @param radius the radius of the circle (non-negative) <p>
     * @throws IllegalArgumentException if {@code center} is {@code null} <p>
     *
     * @author jinseisieko
     */
    public Circle(Point center, double radius) {
        super(center);
        if (radius < 0) {
            throw new IllegalArgumentException("Radius cannot be less than zero");
        }
        this.radius = radius;
    }

    /**
     * Constructs a circle from coordinate values and a radius.
     *
     * @param x      the x-coordinate of the center <p>
     * @param y      the y-coordinate of the center <p>
     * @param radius the radius of the circle (non-negative) <p>
     *
     * @author jinseisieko
     */
    public Circle(double x, double y, double radius) {
        super(x, y);
        if (radius < 0) {
            throw new IllegalArgumentException("Radius cannot be less than zero");
        }
        this.radius = Math.max(0.0, radius);
    }

    /**
     * Returns the center point of this circle.
     * <p>
     * The returned point is a defensive copy to prevent external modification
     * of the internal position.
     *
     * @return a new {@code Point} representing the center of the circle <p>
     *
     * @author jinseisieko
     */
    public Point getCenter() {
        return getPosition();
    }

    /**
     * Returns the radius of this circle.
     *
     * @return the radius (≥ 0) <p>
     *
     * @author jinseisieko
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Sets the radius of this circle.
     * <p>
     * Negative values are clamped to zero to maintain geometric validity.
     *
     * @param radius the new radius (any real number; must not be less than zero) <p>
     *
     * @author jinseisieko
     */
    public void setRadius(double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("Radius cannot be less than zero");
        }
        this.radius = Math.max(0.0, radius);
    }

    /**
     * Returns the geometric shape type associated with this class.
     *
     * @return {@link ShapeType#CIRCLE} <p>
     *
     * @author jinseisieko
     */
    @Override
    public ShapeType getShapeType() {
        return SHAPE_TYPE;
    }

    /**
     * Checks whether this circle contains the specified point (including the boundary).
     * <p>
     * The test uses toroidal Euclidean distance: if the distance from the point
     * to the center is less than or equal to the radius (within floating-point tolerance),
     * the point is considered inside.
     *
     * @param point the point to test; must not be {@code null} <p>
     * @return {@code true} if the point lies inside or on the circle, {@code false} otherwise <p>
     * @throws IllegalArgumentException if {@code point} is {@code null} <p>
     *
     * @author jinseisieko
     */
    public boolean contains(Point point) {
        if (point == null) {
            throw new IllegalArgumentException("Point cannot be null");
        }
        double distanceToCenter = point.distanceTo(this.getCenter());
        return (distanceToCenter - this.radius) <= 1e-12;
    }

    /**
     * Checks whether this circle intersects another circle (including tangency).
     * <p>
     * Two circles intersect if the toroidal distance between their centers
     * is less than or equal to the sum of their radii (within floating-point tolerance).
     *
     * @param other the other circle to test; must not be {@code null} <p>
     * @return {@code true} if the circles overlap or touch, {@code false} otherwise <p>
     * @throws IllegalArgumentException if {@code other} is {@code null} <p>
     *
     * @author jinseisieko
     */
    public boolean intersects(Circle other) {
        if (other == null) {
            throw new IllegalArgumentException("Other circle cannot be null");
        }
        double distanceBetweenCenters = this.getCenter().distanceTo(other.getCenter());
        return (distanceBetweenCenters - (this.radius + other.radius)) <= 1e-12;
    }
}