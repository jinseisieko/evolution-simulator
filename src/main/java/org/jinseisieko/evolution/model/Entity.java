// src/main/java/org/jinseisieko/evolution/model/Entity.java
package org.jinseisieko.evolution.model;

import org.jinseisieko.evolution.basic.Circle;
import org.jinseisieko.evolution.basic.Point;

/**
 * Represents a mobile agent in a 2D toroidal simulation space, extending the geometric {@link Circle}
 * to support dynamic motion with orientation, linear acceleration, and angular acceleration.
 * <p>
 * An {@code Entity} occupies a circular area defined by its center (inherited from {@code Circle})
 * and radius, and it possesses physical state variables:
 * <ul>
 *   <li><b>Angle</b>: orientation in radians, always normalized to the interval [0, 2π)</li>
 *   <li><b>Speed</b>: current linear velocity magnitude</li>
 *   <li><b>Acceleration</b>: rate of change of linear speed</li>
 *   <li><b>Angular acceleration</b>: rate of change of orientation</li>
 * </ul>
 * The entity’s position is updated via Euler integration in the {@code updateEntity(dt)} method,
 * which respects the underlying toroidal geometry through the inherited {@code shift()} operation.
 * All motion is confined to the unit torus [0,1) × [0,1), with automatic wrap-around at boundaries.
 *
 * @author jinseisieko
 */
public class Entity extends Circle {

    private double angle;
    private double speed;
    private double acceleration;
    private double angularAcceleration;

    /**
     * Constructs an entity at the specified initial coordinates with a given size (radius).
     * <p>
     * The position is inherited from the {@code Circle} superclass and automatically normalized
     * to the toroidal space [0,1) × [0,1) via the {@link Point} constructor.
     * All dynamic properties—speed, acceleration, and angular acceleration—are initialized to zero.
     *
     * @param initialCoordinates the starting position (any real coordinates; will be normalized) <p>
     * @param size the radius of the entity’s circular body (must be positive) <p>
     *
     * @author jinseisieko
     */
    public Entity(Point initialCoordinates, double size) {
        super(initialCoordinates, size);
        this.speed = 0;
        this.acceleration = 0;
        this.angularAcceleration = 0;
    }

    /**
     * Constructs an entity at the specified (x, y) coordinates with a given size (radius).
     * <p>
     * This is a convenience constructor that internally creates a {@link Point} from the provided
     * coordinates, ensuring automatic normalization to the toroidal space [0,1) × [0,1).
     * All dynamic state variables are initialized to zero.
     *
     * @param x the initial X coordinate (any real number) <p>
     * @param y the initial Y coordinate (any real number) <p>
     * @param size the radius of the entity’s circular body <p>
     *
     * @author jinseisieko
     */
    public Entity(double x, double y, double size) {
        this(new Point(x, y), size);
    }

    /**
     * Returns the current orientation of the entity.
     * <p>
     * The angle is guaranteed to be in the canonical range [0, 2π) due to normalization
     * performed in the setter methods.
     *
     * @return the orientation in radians, in [0, 2π) <p>
     *
     * @author jinseisieko
     */
    public double getAngle() {
        return this.angle;
    }

    /**
     * Sets the orientation of the entity using an angle in radians.
     * <p>
     * The input angle is normalized modulo 2π to ensure it lies in the interval [0, 2π).
     * Negative angles are wrapped forward (e.g., −π/2 becomes 3π/2).
     *
     * @param angleRad the new orientation in radians (any real number) <p>
     *
     * @author jinseisieko
     */
    public void setAngleRad(double angleRad) {
        this.angle = angleRad % (2 * Math.PI);
        if (this.angle < 0) {
            this.angle += (2 * Math.PI);
        }
    }

    /**
     * Sets the orientation of the entity using an angle in degrees.
     * <p>
     * The input is converted to radians and then normalized to [0, 2π) using the same logic
     * as {@link #setAngleRad(double)}.
     *
     * @param angleDeg the new orientation in degrees (any real number) <p>
     *
     * @author jinseisieko
     */
    public void setAngleDeg(double angleDeg) {
        this.setAngleRad(Math.toRadians(angleDeg));
    }

    /**
     * Returns the current linear speed of the entity.
     * <p>
     * Speed may be positive, negative, or zero, representing motion magnitude and direction
     * relative to the current orientation.
     *
     * @return the current speed (units per time step) <p>
     *
     * @author jinseisieko
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets the linear speed of the entity.
     * <p>
     * The value is stored exactly as provided and will be used in the next motion update.
     *
     * @param speed the new speed (any real number) <p>
     *
     * @author jinseisieko
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Returns the current linear acceleration of the entity.
     * <p>
     * Acceleration affects how speed changes over time during simulation steps.
     *
     * @return the current acceleration (units per time²) <p>
     *
     * @author jinseisieko
     */
    public double getAcceleration() {
        return acceleration;
    }

    /**
     * Sets the linear acceleration of the entity.
     * <p>
     * This value is used during {@code updateEntity(dt)} to increment speed by {@code acceleration × dt}.
     *
     * @param acceleration the new acceleration (any real number) <p>
     *
     * @author jinseisieko
     */
    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    /**
     * Returns the current angular acceleration of the entity.
     * <p>
     * Angular acceleration determines how quickly the orientation changes over time.
     * Unlike angle, angular acceleration is not normalized and can be any real number.
     *
     * @return the current angular acceleration (radians per time²) <p>
     *
     * @author jinseisieko
     */
    public double getAngularAcceleration() {
        return angularAcceleration;
    }

    /**
     * Sets the angular acceleration of the entity using a value in radians per time².
     * <p>
     * This value is used during {@code updateEntity(dt)} to update the angle by
     * {@code angularAcceleration × dt}, after which the angle is normalized to [0, 2π).
     *
     * @param angularAccelerationRad the new angular acceleration in radians per time² <p>
     *
     * @author jinseisieko
     */
    public void setAngularAccelerationRad(double angularAccelerationRad) {
        this.angularAcceleration = angularAccelerationRad;
    }

    /**
     * Sets the angular acceleration of the entity using a value in degrees per time².
     * <p>
     * The input is converted to radians and stored as angular acceleration.
     *
     * @param angularAccelerationDeg the new angular acceleration in degrees per time² <p>
     *
     * @author jinseisieko
     */
    public void setAngularAccelerationDeg(double angularAccelerationDeg) {
        this.setAngularAccelerationRad(Math.toRadians(angularAccelerationDeg));
    }

    /**
     * Advances the entity’s state by a time step {@code dt} using explicit Euler integration.
     * <p>
     * This method performs three updates in sequence:
     * <ol>
     *   <li>Updates the orientation: {@code angle += angularAcceleration × dt}, then normalizes to [0, 2π)</li>
     *   <li>Updates the speed: {@code speed += acceleration × dt}</li>
     *   <li>Shifts the position by {@code (speed × cos(angle) × dt, speed × sin(angle) × dt)},
     *       with automatic toroidal wrap-around via the inherited {@code shift()} method</li>
     * </ol>
     * The order ensures that motion during this step uses the angle and speed values
     * valid at the beginning of the time interval.
     *
     * @param dt the time step duration (should be non-negative) <p>
     *
     * @author jinseisieko
     */
    public void updateEntity(double dt) {
        this.setAngleRad(this.angle + this.angularAcceleration * dt);
        this.speed += this.acceleration * dt;
        this.shift(new Point(
            this.speed * Math.cos(this.angle) * dt,
            this.speed * Math.sin(this.angle) * dt
        ));
    }
}