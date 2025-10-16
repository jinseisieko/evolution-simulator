/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package org.jinseisieko.evolution.basic;

/**
 *
 * @author jinseisieko
 */
public class Circle extends MovingObject {

    private double radius;
    private static final ShapeType SHAPE_TYPE = ShapeType.CIRCLE;

    public Circle(Point center, double radius) {
        super(center);
        this.radius = radius;
    }
    public Circle(double x, double y, double radius) {
        super(x, y);
        this.radius = radius;
    }

    public Point getCenter() {
        return getPosition();
    }

    public double getRadius() {
        return radius;
    }

    /*public boolean contains(Point point)
    {
    }*/

}
