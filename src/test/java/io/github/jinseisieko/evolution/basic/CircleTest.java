// src/test/java/io.github/jinseisieko/evolution/basic/CircleTest.java
package io.github.jinseisieko.evolution.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for the {@link Circle} class, which represents a circular region in a 2D toroidal
 * space [0,1) × [0,1). The circle extends {@link MovingObject} and adds a radius parameter,
 * enabling spatial queries such as point containment and intersection with other circles.
 * 
 * @author jinseisieko
 */
class CircleTest {

    // === Test constructors and initial state ===

    /**
     * Verifies that constructing a {@code Circle} from a {@code Point} and radius
     * correctly initializes its center using normalized coordinates and stores a defensive copy
     * (i.e., changes to the original {@code Point} do not affect the circle).
     */
    @Test
    void constructorWithPoint_shouldSetNormalizedCenterAndRadius() {
        Point p = new Point(1.2, -0.3);
        Circle circle = new Circle(p, 0.25);

        assertEquals(0.2, circle.getX(), 1e-10);
        assertEquals(0.7, circle.getY(), 1e-10);
        assertEquals(0.25, circle.getRadius(), 1e-10);

        p.setX(10);
        assertEquals(0.2, circle.getX(), 1e-10);
    }

    /**
     * Confirms that the coordinate-based constructor correctly normalizes inputs
     * and initializes center.
     */
    @Test
    void constructorWithCoordinates_shouldNormalizeAndSetCenterAndRadius() {
        Circle circle = new Circle(1.5, -0.4, 0.3);
        assertEquals(0.5, circle.getX(), 1e-10);
        assertEquals(0.6, circle.getY(), 1e-10);
        assertEquals(0.3, circle.getRadius(), 1e-10);
    }

    /**
     * Ensures that negative radius values
     * throws an {@link IllegalArgumentException} with a non-empty, informative message,
     * preserving geometric validity.
     */
    @Test
    void constructorWithNegativeRadius_shouldThrowExceptionWithMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            Circle circle = new Circle(0.0, 0.0, -0.5);
            circle.getRadius();
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    // === Test inherited movement behavior ===

    /**
     * Validates that {@code Circle} correctly inherits {@code moveTo} behavior from {@code MovingObject},
     * updating its center while preserving the radius.
     */
    @Test
    void moveTo_shouldUpdateCenterAndPreserveRadius() {
        Circle circle = new Circle(0.1, 0.2, 0.15);
        Point target = new Point(1.8, -0.1);
        circle.moveTo(target);

        assertEquals(0.8, circle.getX(), 1e-10);
        assertEquals(0.9, circle.getY(), 1e-10);
        assertEquals(0.15, circle.getRadius(), 1e-10);
    }

    /**
     * Confirms that {@code shift} updates the center using toroidal arithmetic
     * and leaves the radius unchanged.
     */
    @Test
    void shift_shouldApplyToroidalAdditionToCenterAndPreserveRadius() {
        Circle circle = new Circle(0.8, 0.9, 0.2);
        Point delta = new Point(0.3, 0.2);
        circle.shift(delta);

        assertEquals(0.1, circle.getX(), 1e-10);
        assertEquals(0.1, circle.getY(), 1e-10);
        assertEquals(0.2, circle.getRadius(), 1e-10);
    }

    // === Test radius mutability ===

    /**
     * Validates that {@code setRadius} updates the radius.
     */
    @Test
    void setRadius_shouldUpdateRasius() {
        Circle circle = new Circle(0.0, 0.0, 0.5);
        circle.setRadius(0.75);
        assertEquals(0.75, circle.getRadius(), 1e-10);
        circle.setRadius(0.34);
        assertEquals(0.34, circle.getRadius(), 1e-10);
    }

    /**
     * {@code setRadius} with negative argument
     * throws an {@link IllegalArgumentException} with a non-empty, informative message,
     * preserving geometric validity.
     */
    @Test
    void setNegativeRadius_shouldThrowExceptionWithMessage() {
        Circle circle = new Circle(0.0, 0.0, 0.5);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            circle.setRadius(-0.1);
            circle.getRadius();
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());        
    }

    // === Test shape type ===

    /**
     * Confirms that {@code getShapeType()} always returns {@link ShapeType#CIRCLE},
     * as expected for this class.
     */
    @Test
    void getShapeType_shouldReturnCircle() {
        Circle circle = new Circle(0.0, 0.0, 0.1);
        assertEquals(ShapeType.CIRCLE, circle.getShapeType());
        circle.moveTo(0.1, 0.1);
        assertEquals(ShapeType.CIRCLE, circle.getShapeType());
        circle.shift(0.1, 0.2);
        assertEquals(ShapeType.CIRCLE, circle.getShapeType());
        circle.setRadius(0.4);
        assertEquals(ShapeType.CIRCLE, circle.getShapeType());
    }

    // === Test containment logic ===

    /**
     * Validates that {@code contains} returns {@code true} for points inside or on the boundary
     * of the circle, using toroidal distance.
     */
    @ParameterizedTest
    @CsvSource({
        "0.0, 0.0, 0.3, 0.0, 0.0",   // center
        "0.0, 0.0, 0.3, 0.3, 0.0",   // on boundary (right)
        "0.0, 0.0, 0.3, 0.0, 0.3",   // on boundary (top)
        "0.0, 0.0, 0.3, 0.2, 0.1",   // inside
        "0.8, 0.8, 0.3, 0.0, 0.9",   // wrap-around: (0.8,0.8) to (0.0,0.9) ≈ 0.22 < 0.3
        "0.9, 0.9, 0.3, 0.1, 0.1"
    })
    void contains_shouldReturnTrueForPointsInsideOrOnBoundary(
        double cx, double cy, double radius, double px, double py
    ) {
        Circle circle = new Circle(cx, cy, radius);
        Point point = new Point(px, py);
        assertTrue(circle.contains(point));
    }

    /**
     * Validates that {@code contains} returns {@code false} for points strictly outside
     * the circle, even when wrap-around paths are considered.
     */
    @ParameterizedTest
    @CsvSource({
        "0.0, 0.0, 0.3, 0.4, 0.0",   // outside (right)
        "0.0, 0.0, 0.3, 0.0, 0.4",   // outside (top)
        "0.0, 0.0, 0.3, 0.3, 0.3",   // outside (diagonal)
        "0.8, 0.8, 0.2, 0.0, 0.9",   // wrap-around: distance ≈ 0.22 > 0.2 → outside
        "0.9, 0.9, 0.3, 0.2, 0.2"
    })
    void contains_shouldReturnFalseForPointsOutside(
        double cx, double cy, double radius, double px, double py
    ) {
        Circle circle = new Circle(cx, cy, radius);
        Point point = new Point(px, py);
        assertFalse(circle.contains(point));
    }

    /**
     * Ensures that calling {@code contains(null)} throws an {@link IllegalArgumentException}
     * with a non-empty, informative message.
     */
    @Test
    void contains_withNullPoint_shouldThrowExceptionWithMessage() {
        Circle circle = new Circle(0.5, 0.5, 0.2);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            circle.contains(null);
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    // === Test intersection logic ===

    /**
     * Validates that {@code intersects} returns {@code true} when two circles overlap
     * or touch (including via toroidal wrap-around).
     */
    @ParameterizedTest
    @CsvSource({
        "0.0, 0.0, 0.3, 0.2, 0.0, 0.2",   // overlapping
        "0.0, 0.0, 0.3, 0.3, 0.0, 0.3",   // tangent (touching)
        "0.9, 0.9, 0.2, 0.1, 0.1, 0.2",   // wrap-around intersection
        "0.5, 0.5, 0.4, 0.0, 0.0, 0.4",   // large circles intersecting across center
        "0.0, 0.0, 0.2, 0.0, 0.4, 0.2"
    })
    void intersects_shouldReturnTrueForOverlappingOrTouchingCircles(
        double x1, double y1, double r1,
        double x2, double y2, double r2
    ) {
        Circle c1 = new Circle(x1, y1, r1);
        Circle c2 = new Circle(x2, y2, r2);
        assertTrue(c1.intersects(c2));
    }

    /**
     * Validates that {@code intersects} returns {@code false} when two circles are
     * strictly separated, even when considering toroidal wrap-around.
     */
    @ParameterizedTest
    @CsvSource({
        "0.0, 0.0, 0.2, 0.5, 0.0, 0.2",   // separated horizontally
        "0.0, 0.0, 0.2, 0.0, 0.5, 0.2",   // separated vertically
        "0.0, 0.0, 0.2, 0.5, 0.5, 0.2",   // separated diagonally
        "0.9, 0.9, 0.1, 0.1, 0.1, 0.1"    // wrap-around still too far
    })
    void intersects_shouldReturnFalseForSeparatedCircles(
        double x1, double y1, double r1,
        double x2, double y2, double r2
    ) {
        Circle c1 = new Circle(x1, y1, r1);
        Circle c2 = new Circle(x2, y2, r2);
        assertFalse(c1.intersects(c2));
    }

    /**
     * Ensures that calling {@code intersects(null)} throws an {@link IllegalArgumentException}
     * with a non-empty message.
     */
    @Test
    void intersects_withNullCircle_shouldThrowExceptionWithMessage() {
        Circle circle = new Circle(0.5, 0.5, 0.2);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            circle.intersects(null);
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    // === Test defensive copying ===

    /**
     * Validates that {@code getCenter()} returns a defensive copy of the internal position,
     * so external modifications do not affect the circle’s state.
     */
    @Test
    void getCenter_shouldReturnDefensiveCopy() {
        Circle circle = new Circle(0.3, 0.7, 0.1);
        Point center = circle.getCenter();
        center.setX(0.999);
        assertEquals(0.3, circle.getX(), 1e-10);
        center.setY(0.999);
        assertEquals(0.3, circle.getX(), 1e-10);
    }
}