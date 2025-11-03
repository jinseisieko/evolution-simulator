// src/main/java/org/jinseisieko/evolution/base/Entity.java
package org.jinseisieko.evolution.base;

import org.jinseisieko.evolution.basic.Circle;
import org.jinseisieko.evolution.basic.Point;

/**
 * Represents a mobile agent in a 2D toroidal simulation space, extending the geometric {@link Circle}
 * to support dynamic motion with orientation, linear acceleration, and constant angular speed.
 * <p>
 * An {@code Entity} occupies a circular area defined by its center (inherited from {@code Circle})
 * and radius, and it possesses physical state variables:
 * <ul>
 *   <li><b>Angle</b>: orientation in radians, always normalized to the interval [0, 2π)</li>
 *   <li><b>Speed</b>: current linear velocity magnitude</li>
 *   <li><b>Acceleration</b>: rate of change of linear speed</li>
 *   <li><b>Angular speed</b>: rate of change of orientation (radians per time unit)</li>
 * </ul>
 * The entity’s position and orientation are updated via Euler integration in the {@code updateEntity(dt)} method,
 * which respects the underlying toroidal geometry through the inherited {@code shift()} operation.
 * All motion is confined to the unit torus [0,1) × [0,1), with automatic wrap-around at boundaries.
 * This class is managed within a {@link BasicSimulation} context.
 *
 * @author jinseisieko
 */
public class Entity extends Circle {

    private double angle;
    private double speed;
    private double acceleration;
    private double angularSpeed;
    private final BasicSimulation simulation;

    /**
     * Constructs an entity at the specified initial coordinates with a given size (radius).
     * <p>
     * The position is inherited from the {@code Circle} superclass and automatically normalized
     * to the toroidal space [0,1) × [0,1) via the {@link Point} constructor.
     * All dynamic properties—speed, acceleration, and angular speed—are initialized to zero.
     *
     * @param initialCoordinates the starting position (any real coordinates; will be normalized)
     * @param radius the radius of the entity’s circular body (must be positive)
     * @param simulation The simulation context this entity belongs to.
     *
     * @author jinseisieko
     */
    public Entity(Point initialCoordinates, double radius, BasicSimulation simulation) {
        super(initialCoordinates, radius);
        this.speed = 0;
        this.acceleration = 0;
        this.angularSpeed = 0;
        this.simulation = simulation;
    }

    /**
     * Constructs an entity at the specified (x, y) coordinates with a given size (radius).
     * <p>
     * This is a convenience constructor that internally creates a {@link Point} from the provided
     * coordinates, ensuring automatic normalization to the toroidal space [0,1) × [0,1).
     * All dynamic state variables are initialized to zero.
     *
     * @param x the initial X coordinate (any real number)
     * @param y the initial Y coordinate (any real number)
     * @param radius the radius of the entity’s circular body
     * @param simulation The simulation context this entity belongs to.
     *
     * @author jinseisieko
     */
    public Entity(double x, double y, double radius, BasicSimulation simulation) {
        this(new Point(x, y), radius, simulation);
    }

    /**
     * Returns the current orientation of the entity.
     * <p>
     * The angle is guaranteed to be in the canonical range [0, 2π) due to normalization
     * performed in the setter methods.
     *
     * @return the orientation in radians, in [0, 2π)
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
     * @param angleRad the new orientation in radians (any real number)
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
     * @param angleDeg the new orientation in degrees (any real number)
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
     * @return the current speed (units per time step)
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
     * @param speed the new speed (any real number)
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
     * @return the current acceleration (units per time²)
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
     * @param acceleration the new acceleration (any real number)
     *
     * @author jinseisieko
     */
    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    /**
     * Returns the current angular speed of the entity.
     * <p>
     * Angular speed determines how quickly the orientation changes over time.
     * Positive values rotate counter-clockwise; negative values rotate clockwise.
     *
     * @return the current angular speed (radians per time unit)
     *
     * @author jinseisieko
     */
    public double getAngularSpeed() {
        return angularSpeed;
    }

    /**
     * Sets the angular speed of the entity using a value in radians per time unit.
     * <p>
     * This value is used during {@code updateEntity(dt)} to update the angle by
     * {@code angularSpeed × dt}, after which the angle is normalized to [0, 2π).
     *
     * @param angularSpeedRad the new angular speed in radians per time unit
     *
     * @author jinseisieko
     */
    public void setAngularSpeedRad(double angularSpeedRad) {
        this.angularSpeed = angularSpeedRad;
    }

    /**
     * Sets the angular speed of the entity using a value in degrees per time unit.
     * <p>
     * The input is converted to radians and stored as angular speed.
     *
     * @param angularSpeedDeg the new angular speed in degrees per time unit
     *
     * @author jinseisieko
     */
    public void setAngularSpeedDeg(double angularSpeedDeg) {
        this.setAngularSpeedRad(Math.toRadians(angularSpeedDeg));
    }

    /**
     * Advances the entity’s state by a time step {@code dt} using explicit Euler integration.
     * <p>
     * This method performs three updates in sequence:
     * <ol>
     *   <li>Updates the orientation: {@code angle += angularSpeed × dt}, then normalizes to [0, 2π)</li>
     *   <li>Updates the speed: {@code speed += acceleration × dt}</li>
     *   <li>Shifts the position by {@code (speed × cos(angle) × dt, speed × sin(angle) × dt)},
     *       with automatic toroidal wrap-around via the inherited {@code shift()} method</li>
     * </ol>
     * The order ensures that motion during this step uses the angle and speed values
     * valid at the beginning of the time interval.
     *
     * @param dt the time step duration (should be non-negative)
     *
     * @author jinseisieko
     */
    public void updateEntity(double dt) {
        this.setAngleRad(this.angle + this.angularSpeed * dt);
        this.speed += this.acceleration * dt;
        this.shift(new Point(
            this.speed * Math.cos(this.angle) * dt,
            this.speed * Math.sin(this.angle) * dt
        ));
    }

    /**
     * Retrieves the simulation context to which this entity belongs.
     * <p>
     * This method provides access to the {@link BasicSimulation} instance managing this entity.
     *
     * @return The simulation instance.
     *
     * @author jinseisieko
     */
    public BasicSimulation getSimulation() {
        return simulation;
    }
}