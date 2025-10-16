// src/test/java/org/jinseisieko/evolution/basic/MovingObjectTest.java

package org.jinseisieko.evolution.basic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for the {@link MovingObject} class, which represents an entity capable of changing
 * its position in a 2D toroidal space [0,1) × [0,1). The object delegates all spatial logic
 * (normalization, addition, wrapping) to the {@link Point} class and ensures encapsulation
 * through defensive copying and input validation.
 * <p>
 * Tests cover:
 * - Correct initialization and normalization of position via constructors.
 * - Safe mutation via {@code moveTo} and {@code shift} with toroidal arithmetic.
 * - Input validation (null checks) and meaningful error messages.
 * - Defensive copying in getters to prevent external mutation.
 * - Invariance under shift–inverse operations (group property of toroidal space).
 * 
 * @author jinseisieko
 */
class MovingObjectTest {

    // === Test constructors and initial state ===

    /**
     * Verifies that constructing a {@code MovingObject} from a {@code Point} correctly
     * initializes its position using normalized coordinates and stores a defensive copy
     * (i.e., changes to the original {@code Point} do not affect the object).
     */
    @Test
    void constructorWithPoint_shouldSetNormalizedPosition() {
        Point p = new Point(1.2, -0.3);
        MovingObject obj = new MovingObject(p);

        assertEquals(0.2, obj.getX(), 1e-10);
        assertEquals(0.7, obj.getY(), 1e-10);

        p.setX(10);
        assertEquals(0.2, obj.getX(), 1e-10);
    }

    /**
     * Confirms that the coordinate-based constructor correctly normalizes inputs
     * and initializes the internal position in the valid toroidal range.
     */
    @Test
    void constructorWithCoordinates_shouldNormalizeAndSetPosition() {
        MovingObject obj = new MovingObject(1.5, -0.4);
        assertEquals(0.5, obj.getX(), 1e-10);
        assertEquals(0.6, obj.getY(), 1e-10);
    }

    /**
     * Ensures that passing {@code null} to the {@code Point}-based constructor
     * throws an {@link IllegalArgumentException} with a non-empty, informative message.
     */
    @Test
    void constructorWithNullPoint_shouldThrowExceptionWithMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            MovingObject obj = new MovingObject((Point) null);
            obj.shift(0.0, 0.0);
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    // === Test moveTo operations ===

    /**
     * Validates that {@code moveTo(Point)} updates the position to the normalized
     * coordinates of the target point, respecting toroidal boundaries.
     */
    @Test
    void moveToWithPoint_shouldUpdateToNormalizedPosition() {
        MovingObject obj = new MovingObject(0.1, 0.2);
        Point target = new Point(1.8, -0.1);
        obj.moveTo(target);

        assertEquals(0.8, obj.getX(), 1e-10);
        assertEquals(0.9, obj.getY(), 1e-10);
    }

    /**
     * Confirms that the coordinate-based {@code moveTo(x, y)} correctly normalizes
     * the target coordinates before updating the position.
     */
    @Test
    void moveToWithCoordinates_shouldNormalizeAndSetPosition() {
        MovingObject obj = new MovingObject(0.0, 0.0);
        obj.moveTo(2.3, -1.7);
        assertEquals(0.3, obj.getX(), 1e-10);
        assertEquals(0.3, obj.getY(), 1e-10);
    }

    /**
     * Checks that {@code moveTo(null)} throws an {@link IllegalArgumentException}
     * with a meaningful error message.
     */
    @Test
    void moveToWithNull_shouldThrowExceptionWithMessage() {
        MovingObject obj = new MovingObject(0.5, 0.5);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            obj.moveTo((Point) null);
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    // === Test shift operations (toroidal displacement) ===

    /**
     * Verifies that {@code shift(Point)} applies toroidal vector addition:
     * the object’s position is updated by the normalized displacement vector,
     * with coordinates wrapped into [0,1) as needed.
     */
    @Test
    void shiftWithPoint_shouldApplyToroidalAddition() {
        MovingObject obj = new MovingObject(0.8, 0.9);
        Point delta = new Point(0.3, 0.2);
        obj.shift(delta);

        assertEquals(0.1, obj.getX(), 1e-10);
        assertEquals(0.1, obj.getY(), 1e-10);
    }

    /**
     * Confirms that the coordinate-based {@code shift(dx, dy)} correctly normalizes
     * the displacement vector before applying it.
     */
    @Test
    void shiftWithCoordinates_shouldNormalizeDeltaAndApply() {
        MovingObject obj = new MovingObject(0.9, 0.95);
        obj.shift(0.2, 0.1);
        assertEquals(0.1, obj.getX(), 1e-10);
        assertEquals(0.05, obj.getY(), 1e-10);
    }

    /**
     * Tests negative displacements that wrap around the lower boundary (0.0 → 1.0),
     * ensuring correct toroidal behavior in all quadrants.
     */
    @Test
    void shiftWithNegativeDelta_shouldWrapCorrectly() {
        MovingObject obj = new MovingObject(0.1, 0.2);
        obj.shift(-0.3, -0.4);
        assertEquals(0.8, obj.getX(), 1e-10);
        assertEquals(0.8, obj.getY(), 1e-10);
    }

    /**
     * Ensures that {@code shift(null)} throws an {@link IllegalArgumentException}
     * with a non-empty, descriptive message.
     */
    @Test
    void shiftWithNull_shouldThrowExceptionWithMessage() {
        MovingObject obj = new MovingObject(0.5, 0.5);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            obj.shift((Point) null);
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    // === Test encapsulation and defensive copying ===

    /**
     * Validates that {@code getPosition()} returns a defensive copy of the internal
     * {@code Point}, so external modifications do not affect the object’s state.
     */
    @Test
    void getPosition_shouldReturnDefensiveCopy() {
        MovingObject obj = new MovingObject(0.3, 0.7);
        Point pos = obj.getPosition();
        pos.setX(0.999);
        assertEquals(0.3, obj.getX(), 1e-10);
    }

    /**
     * Confirms that direct coordinate accessors ({@code getX}, {@code getY})
     * return normalized values consistent with the toroidal space invariant.
     */
    @Test
    void getX_and_getY_shouldReturnNormalizedCoordinates() {
        MovingObject obj = new MovingObject(1.25, -0.6); 
        assertEquals(0.25, obj.getX(), 1e-10);
        assertEquals(0.4, obj.getY(), 1e-10);
    }

    // === Test algebraic invariance under shift–inverse ===

    /**
     * Validates a fundamental property of the toroidal group: shifting by a vector
     * and then by its additive inverse must return the object to its original position.
     * This test uses diverse inputs, including values that cause multiple wraps,
     * to ensure robustness of the spatial model.
     */
    @ParameterizedTest
    @CsvSource({
        "0.5,   0.6,    0.1,    0.1",
        "0.0,   0.0,    0.1,    0.1",
        "0.0,   0.0,    1.1,    1.1",
        "1.5,   0.1,    0.3,    5.1",
        "9.5,  -0.6,   -0.1,   -0.1",
    })
    void shiftingByVectorAndItsInverse_returnsObjectToOriginalPosition(
        double initialX, double initialY,
        double offsetX, double offsetY
    ) {
        MovingObject obj = new MovingObject(new Point(initialX, initialY));
        double originalX = obj.getX();
        double originalY = obj.getY();
        Point shiftVector = new Point(offsetX, offsetY);
        Point inverseVector = shiftVector.inverse();
        obj.shift(shiftVector);
        obj.shift(inverseVector);
        assertEquals(originalX, obj.getX(), 1e-10);
        assertEquals(originalY, obj.getY(), 1e-10);
    }
}