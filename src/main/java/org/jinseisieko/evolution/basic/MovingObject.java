// src/main/java/org/jinseisieko/evolution/basic/MovingObject.java
package org.jinseisieko.evolution.basic;

/**
 * Represents an object that can change its position in a two-dimensional space.
 * This class is intended for use in evolution simulations where entities (e.g., organisms)
 * need to move or be repositioned over time.
 * <p>
 * The internal position is stored as a {@link Point} and is protected from external
 * modification through defensive copying in getters.
 *
 * @author jinseisieko
 */
public class MovingObject {

    private Point position;

    /**
     * Constructs a new {@code MovingObject} with the specified initial position.
     *
     * @param point the initial position; must not be {@code null} <p>
     * @throws IllegalArgumentException if {@code point} is {@code null} <p>
     * 
     * @author jinseisieko
     */
    public MovingObject(Point point) {
        if (point == null) {
            throw new IllegalArgumentException("Initial position cannot be null");
        }
        this.position = new Point(point.getX(), point.getY()); // defensive copy
    }

    /**
     * Constructs a new {@code MovingObject} with the specified initial coordinates.
     *
     * @param x the initial x-coordinate <p>
     * @param y the initial y-coordinate <p>
     * 
     * @author jinseisieko
     */
    public MovingObject(double x, double y) {
        this(new Point(x, y));
    }

    /**
     * Moves this object to the specified position.
     *
     * @param position the new position; must not be {@code null} <p>
     * @throws IllegalArgumentException if {@code position} is {@code null} <p>
     * 
     * @author jinseisieko
     */
    public void moveTo(Point position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        this.position.setX(position.getX());
        this.position.setY(position.getY());
    }

    /**
     * Moves this object to the specified coordinates.
     *
     * @param x the new x-coordinate <p>
     * @param y the new y-coordinate <p>
     * 
     * @author jinseisieko
     */
    public void moveTo(double x, double y) {
        moveTo(new Point(x, y));
    }

    /**
     * Shifts this object's position by the given displacement vector.
     *
     * @param offset the displacement vector; must not be {@code null} <p>
     * @throws IllegalArgumentException if {@code offset} is {@code null} <p>
     * 
     * @author jinseisieko
     */
    public void shift(Point offset) {
        if (offset == null) {
            throw new IllegalArgumentException("Offset cannot be null");
        }
        this.position.addPoint(offset);
    }

    /**
     * Shifts this object's position by the given offsets.
     *
     * @param dx the offset along the x-axis <p>
     * @param dy the offset along the y-axis <p>
     * 
     * @author jinseisieko
     */
    public void shift(double dx, double dy) {
        shift(new Point(dx, dy));
    }

    /**
     * Returns a copy of the current position of this object.
     * The returned {@code Point} is a defensive copy to prevent external modification
     * of the internal state.
     *
     * @return a new {@code Point} representing the current position <p>
     * 
     * @author jinseisieko
     */
    public Point getPosition() {
        return new Point(position.getX(), position.getY());
    }

    /**
     * Returns the current x-coordinate of this object.
     *
     * @return the x-coordinate of the current position <p>
     * 
     * @author jinseisieko
     */
    public double getX() {
        return position.getX();
    }

    /**
     * Returns the current y-coordinate of this object.
     *
     * @return the y-coordinate of the current position <p>
     * 
     * @author jinseisieko
     */
    public double getY() {
        return position.getY();
    }
}