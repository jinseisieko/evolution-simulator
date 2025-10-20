/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package org.jinseisieko.evolution.model;

import org.jinseisieko.evolution.basic.Circle;
import org.jinseisieko.evolution.basic.Point;

/**
 *
 * @author jinseisieko
 */
public class Entity extends Circle {

    private double angle;
    private double speed;
    private double acceleration;
    private double angularAcceleration;

    public Entity(Point initialCoordinates, double size) {
        super(initialCoordinates, size);
        
        this.speed = 0;
        this.acceleration = 0;
        this.angularAcceleration = 0;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngleRad(double angleRad) {
        this.angle = angleRad;
    }

    public void setAnglDeg(double angleDeg) {
        this.angle = Math.toRadians(angleDeg);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public double getAngularAcceleration() {
        return angularAcceleration;
    }

    public void setAngularAcceleration(double angularAcceleration) {
        this.angularAcceleration = angularAcceleration;
    }

    public void updateEntity() {
        this.angle = (this.angle + this.angularAcceleration) % 2*Math.PI;
        this.speed += this.acceleration;
        this.shift(new Point(
            this.speed*Math.cos(this.angle),
            this.speed*Math.sin(this.angle)
        ));
    }
    

}
