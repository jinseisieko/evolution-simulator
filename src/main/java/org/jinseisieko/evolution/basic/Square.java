// src/main/java/org/jinseisieko/evolution/basic/Square.java
package org.jinseisieko.evolution.basic;

/**
 *
 * @author jinseisieko
 */
public class Square extends MovingObject implements Shape {
    private double halfSize;
    private static final ShapeType SHAPE_TYPE = ShapeType.SQUARE;

    public Square(Point center, double halfSize) {
        super(center);
        if (halfSize < 0) {
            throw new IllegalArgumentException("HalfSize cannot be less than zero");
        }
        this.halfSize = halfSize;
    }

    public Square(double x, double y, double halfSize) {
        super(x, y);
        if (halfSize < 0) {
            throw new IllegalArgumentException("halfSize cannot be less than zero");
        }
        this.halfSize = halfSize;
    }

    public Point getCenter() {
        return this.getPosition();
    }

    public double getHalfSize() {
        return halfSize;
    }

    public void setHalfSize(double halfSize) {
        if (halfSize < 0) {
            throw new IllegalArgumentException("HalfSize cannot be less than zero");
        }
        this.halfSize = halfSize;
    }

    public double getSideLenght() {
        return this.halfSize*2;
    }

    @Override
    public ShapeType getShapeType() {
        return SHAPE_TYPE;
    }

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
