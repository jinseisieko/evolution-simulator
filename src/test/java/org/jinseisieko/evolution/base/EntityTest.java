// src/test/java/org/jinseisieko/evolution/base/EntityTest.java
package org.jinseisieko.evolution.base;

import org.jinseisieko.evolution.basic.Point;
import org.jinseisieko.evolution.basic.ShapeType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for the {@link Entity} class, which extends {@link org.jinseisieko.evolution.basic.Circle}
 * and represents a mobile agent in a 2D toroidal simulation space.
 * <p>
 * An {@code Entity} has:
 * <ul>
 *   <li>Position and radius (inherited from {@code Circle})</li>
 *   <li>Orientation ({@code angle} in radians, normalized to [0, 2π))</li>
 *   <li>Linear dynamics: speed and acceleration</li>
 *   <li>Angular dynamics: angular acceleration</li>
 * </ul>
 * The {@code updateEntity(dt)} method integrates motion using Euler integration,
 * updating position based on current speed/direction and angle based on angular acceleration.
 * All state mutations respect the underlying toroidal geometry via the {@code Circle.shift()} method.
 *
 * @author jinseisieko
 */
class EntityTest {

    // === Constructor and initialization ===

    /**
     * Verifies that the constructor accepting a {@code Point} correctly initializes all fields:
     * - Position is copied (defensive copy) and matches input
     * - Speed, acceleration, and angular acceleration default to zero
     * - Radius is set as provided
     * - Shape type is {@code CIRCLE}
     * - The internal center is a distinct object from the input {@code Point}
     */
    @Test
    void constructorWithPoint_initializesAllFieldsCorrectly() {
        Point coordinates = new Point(0.5, 0.5);
        Entity entity = new Entity(coordinates, 0.01);
        assertEquals(coordinates.getX(), entity.getX(), 1e-12);
        assertEquals(coordinates.getY(), entity.getY(), 1e-12);
        assertEquals(0, entity.getSpeed(), 1e-12);
        assertEquals(0, entity.getAcceleration(), 1e-12);
        assertEquals(0, entity.getAngularSpeed(), 1e-12);
        // Defensive copy: center must not be the same reference
        assertNotEquals(entity.getCenter(), coordinates);
        assertEquals(coordinates.getX(), entity.getCenter().getX(), 1e-12);
        assertEquals(coordinates.getY(), entity.getCenter().getY(), 1e-12);
        assertEquals(0.01, entity.getRadius(), 1e-12);
        assertEquals(ShapeType.CIRCLE, entity.getShapeType());
    }

    /**
     * Confirms that the convenience constructor (x, y, radius) behaves identically
     * to the {@code Point}-based constructor, ensuring consistent initialization.
     */
    @Test
    void constructorWithCoordinates_initializesAllFieldsCorrectly() {
        Entity entity = new Entity(0.5, 0.5, 0.01);
        assertEquals(0.5, entity.getX(), 1e-12);
        assertEquals(0.5, entity.getY(), 1e-12);
        assertEquals(0, entity.getSpeed(), 1e-12);
        assertEquals(0, entity.getAcceleration(), 1e-12);
        assertEquals(0, entity.getAngularSpeed(), 1e-12);
        assertEquals(0.5, entity.getCenter().getX(), 1e-12);
        assertEquals(0.5, entity.getCenter().getY(), 1e-12);
        assertEquals(0.01, entity.getRadius(), 1e-12);
        assertEquals(ShapeType.CIRCLE, entity.getShapeType());
    }

    // === Linear motion setters and getters ===

    /**
     * Validates that {@code setAcceleration()} stores the provided value exactly,
     * and {@code getAcceleration()} returns it without modification.
     * Covers positive, negative, and large-magnitude inputs.
     */
    @ParameterizedTest
    @CsvSource({
        "0.1, 0.1",
        "0.5, 0.5",
        "-0.1, -0.1",
        "10, 10",
        "-10, -10"
    })
    void setAcceleration_storesValueExactly(double input, double expected) {
        Entity entity = new Entity(0.1, 0.1, 0.01);
        entity.setAcceleration(input);
        assertEquals(expected, entity.getAcceleration(), 1e-12);
    }

    /**
     * Validates that {@code setSpeed()} stores the provided value exactly,
     * and {@code getSpeed()} returns it without modification.
     * Covers positive, negative, and large-magnitude inputs.
     */
    @ParameterizedTest
    @CsvSource({
        "0.1, 0.1",
        "0.5, 0.5",
        "-0.1, -0.1",
        "10, 10",
        "-10, -10"
    })
    void setSpeed_storesValueExactly(double input, double expected) {
        Entity entity = new Entity(0.1, 0.1, 0.01);
        entity.setSpeed(input);
        assertEquals(expected, entity.getSpeed(), 1e-12);
    }

    // === Angle management (radians and degrees) ===

    /**
     * Ensures that {@code setAngleRad()} normalizes any input angle into the canonical range [0, 2π).
     * Negative angles are wrapped forward, and values ≥2π are reduced modulo 2π.
     * The getter {@code getAngle()} returns the normalized value.
     */
    @ParameterizedTest
    @CsvSource({
        "0.0, 0.0",
        "1.5707963267948966, 1.5707963267948966", // π/2
        "3.141592653589793, 3.141592653589793",   // π
        "6.283185307179586, 0.0",                  // 2π → 0
        "7.853981633974483, 1.5707963267948966",  // 2π + π/2 → π/2
        "-1.5707963267948966, 4.71238898038469",  // -π/2 → 3π/2
        "-3.141592653589793, 3.141592653589793",  // -π → π
        "-6.283185307179586, 0.0",                 // -2π → 0
        "12.566370614359172, 0.0",                 // 4π → 0
        "-12.566370614359172, 0.0"                 // -4π → 0
    })
    void setAngleRad_normalizesTo_0_to_2Pi(double input, double expected) {
        Entity entity = new Entity(0.1, 0.1, 0.01);
        entity.setAngleRad(input);
        assertEquals(expected, entity.getAngle(), 1e-12);
    }

    /**
     * Verifies that {@code setAngleDeg()} correctly converts degrees to radians
     * and applies the same normalization as {@code setAngleRad()}.
     * Includes full rotations (360°), negative angles, and non-integer degrees.
     */
    @ParameterizedTest
    @CsvSource({
        "0.0, 0.0",
        "90.0, 1.5707963267948966",
        "180.0, 3.141592653589793",
        "360.0, 0.0",
        "450.0, 1.5707963267948966", // 360+90 → 90
        "-90.0, 4.71238898038469",   // -90 → 270°
        "-180.0, 3.141592653589793",
        "-360.0, 0.0",
        "720.0, 0.0",
        "-720.5, 6.274458660919615"  // ≈ -0.5° → 359.5° in radians
    })
    void setAngleDeg_convertsAndNormalizesCorrectly(double degrees, double expectedRad) {
        Entity entity = new Entity(0.1, 0.1, 0.01);
        entity.setAngleDeg(degrees);
        assertEquals(expectedRad, entity.getAngle(), 1e-12);
    }

    /**
     * Confirms that extreme angle values (very large positive/negative) are always
     * normalized into [0, 2π), ensuring numerical robustness.
     */
    @ParameterizedTest
    @ValueSource(doubles = {
        4 * Math.PI,
        -4 * Math.PI,
        1000,
        -1000,
        2 * Math.PI + 0.1,
        -2 * Math.PI - 0.1
    })
    void setAngleRad_handlesExtremeValues(double inputAngle) {
        Entity entity = new Entity(0, 0, 0.01);
        entity.setAngleRad(inputAngle);
        double angle = entity.getAngle();
        assertTrue(angle >= 0 && angle < 2 * Math.PI,
            "Angle " + angle + " is not in [0, 2π)");
    }

    // === Angular acceleration ===

    /**
     * Validates that {@code setAngularSpeedRad()} stores the provided value exactly,
     * without normalization (angular acceleration is not an angle, so no wrapping).
     */
    @ParameterizedTest
    @CsvSource({
        "0.0, 0.0",
        "1.5707963267948966, 1.5707963267948966",
        "3.141592653589793, 3.141592653589793",
        "6.283185307179586, 6.283185307179586",
        "-1.5707963267948966, -1.5707963267948966",
        "-12.566370614359172, -12.566370614359172"
    })
    void setAngularSpeedRad_storesValueExactly(double input, double expected) {
        Entity entity = new Entity(0.1, 0.1, 0.01);
        entity.setAngularSpeedRad(input);
        assertEquals(expected, entity.getAngularSpeed(), 1e-12);
    }

    /**
     * Verifies that {@code setAngularAccelerationDeg()} correctly converts degrees to radians
     * and stores the result without normalization.
     */
    @ParameterizedTest
    @CsvSource({
        "0.0, 0.0",
        "90.0, 1.5707963267948966",
        "180.0, 3.141592653589793",
        "360.0, 6.283185307179586",
        "450.0, 7.853981633974483",
        "-90.0, -1.5707963267948966",
        "-360.0, -6.283185307179586",
        "720.0, 12.566370614359172",
        "-720.5, -12.57509726062376"
    })
    void setAngularAccelerationDeg_convertsCorrectly(double degrees, double expectedRad) {
        Entity entity = new Entity(0.1, 0.1, 0.01);
        entity.setAngularSpeedDeg(degrees);
        assertEquals(Math.toRadians(degrees), entity.getAngularSpeed(), 1e-12);
    }

    // === Motion integration ===

    /**
     * Tests that {@code updateEntity(dt)} correctly integrates linear motion:
     * - Position changes by (speed * cos(angle) * dt, speed * sin(angle) * dt)
     * - The underlying {@code Circle.shift()} ensures toroidal wrapping
     * - Speed is updated by acceleration * dt
     * - Angle is updated by angularAcceleration * dt (then normalized)
     * This test uses known analytical results over three consecutive updates.
     */
    @ParameterizedTest
    @CsvSource({
        "0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0",
        "0.0, 0.0, 0.0, 10.0, 0.0, 0.006, 0.0, 0.0",
        "0.0, 0.0, 1.5707963267948966, 10.0, 0.0, 0.0, 0.006, 1.5707963267948966",
        "0.9, 0.9, 0.5, 0.0, 10.0, 0.9, 0.9, 0.8",
        "0.0, 0.0, 0.0, 10.0, 1.0, 0.00599820011, 0.00013998366, 0.03"
    })
    void updateEntity_threeTimes_matchesExpectedState(
        double x,
        double y,
        double angle,
        double acceleration,
        double angularAcceleration,
        double expectedX,
        double expectedY,
        double expectedAngle) {
        Entity entity = new Entity(x, y, 0.01);
        entity.setAngleRad(angle);
        entity.setAngularSpeedRad(angularAcceleration);
        entity.setAcceleration(acceleration);
        entity.updateEntity(0.01);
        entity.updateEntity(0.01);
        entity.updateEntity(0.01);
        assertEquals(expectedX, entity.getX(), 1e-10);
        assertEquals(expectedY, entity.getY(), 1e-10);
        assertEquals(expectedAngle, entity.getAngle(), 1e-10);
    }

    /**
     * Validates basic directional movement: an entity facing angle θ
     * moves in the direction (cos θ, sin θ) when speed > 0.
     * Uses dt = 0.1 and speed = 10 → displacement = 1.0 unit.
     */
    @Test
    void updateEntity_movesInDirectionOfAngle() {
        Entity entity = new Entity(0, 0, 0.01);
        entity.setAngleRad(0);          // face +X
        entity.setSpeed(10);
        entity.updateEntity(0.1);       // move 1.0 in X
        assertEquals(0.0, entity.getX(), 1e-12);
        assertEquals(0.0, entity.getY(), 1e-12);

        entity.setAngleRad(Math.PI / 2); // face +Y
        entity.updateEntity(0.1);        // move 1.0 in Y
        assertEquals(0.0, entity.getX(), 1e-12);
        assertEquals(0.0, entity.getY(), 1e-12);
    }

    /**
     * Confirms that calling {@code updateEntity(0)} leaves all state unchanged,
     * ensuring robustness against zero time steps.
     */
    @Test
    void updateEntity_withZeroDt_preservesState() {
        Entity entity = new Entity(1, 1, 0.01);
        entity.setSpeed(5);
        entity.setAcceleration(10);
        entity.setAngleRad(Math.PI / 4);
        entity.setAngularSpeedRad(1);

        double initialX = entity.getX();
        double initialY = entity.getY();
        double initialAngle = entity.getAngle();
        double initialSpeed = entity.getSpeed();

        entity.updateEntity(0.0);

        assertEquals(initialX, entity.getX(), 1e-12);
        assertEquals(initialY, entity.getY(), 1e-12);
        assertEquals(initialAngle, entity.getAngle(), 1e-12);
        assertEquals(initialSpeed, entity.getSpeed(), 1e-12);
    }

    /**
     * Verifies that angular acceleration is integrated correctly over multiple steps:
     * angle += angularAcceleration * dt on each update.
     * No normalization issues should occur during accumulation.
     */
    @Test
    void updateEntity_accumulatesAngleWithAngularAcceleration() {
        Entity entity = new Entity(0, 0, 0.01);
        entity.setAngleRad(0);
        entity.setAngularSpeedRad(2.0); // rad/s²
        double dt = 0.5;

        entity.updateEntity(dt); // angle = 0 + 2*0.5 = 1.0
        assertEquals(1.0, entity.getAngle(), 1e-12);

        entity.updateEntity(dt); // angle = 1.0 + 2*0.5 = 2.0
        assertEquals(2.0, entity.getAngle(), 1e-12);

        entity.updateEntity(dt); // angle = 2.0 + 2*0.5 = 3.0
        assertEquals(3.0, entity.getAngle(), 1e-12);
    }

    // === Inheritance and identity ===

    /**
     * Confirms that {@code Entity} is a subclass of {@code Circle},
     * inheriting position, radius, and shape type correctly.
     */
    @Test
    void entity_isSubclassOfCircle() {
        Entity entity = new Entity(1.4, 2.2, 0.5);
        assertTrue(entity instanceof org.jinseisieko.evolution.basic.Circle);
        assertEquals(0.4, entity.getX(), 1e-12);  // normalized by Point
        assertEquals(0.2, entity.getY(), 1e-12);  // normalized by Point
        assertEquals(0.5, entity.getRadius(), 1e-12);
        assertEquals(ShapeType.CIRCLE, entity.getShapeType());
    }
}