// src/test/java/org/jinseisieko/evolution/basic/PointTest.java
package org.jinseisieko.evolution.basic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Point} class, which represents a point in a 2D toroidal space [0,1) × [0,1).
 * All coordinates are normalized modulo 1, and operations respect periodic boundary conditions.
 * 
 * @author jinseisieko
 */
class PointTest {

    // === Test the static normalization utility method ===

    /**
     * Verifies that the static {@code Point.norm()} method correctly maps any real number
     * into the interval [0, 1) using modular arithmetic.
     * Covers typical values, overflows (>1), negative inputs, and integer boundaries.
     */
    @ParameterizedTest
    @CsvSource({
        "0.0,   0.0",   // identity
        "0.5,   0.5",   // mid-range
        "1.0,   0.0",   // wraps to zero
        "1.3,   0.3",   // positive overflow
        "2.7,   0.7",   // multiple wraps
        "-0.1,  0.9",   // negative input wraps forward
        "-0.5,  0.5",
        "-1.0,  0.0",   // exact negative integer
        "-1.2,  0.8",   // negative overflow
        "3.0,   0.0"    // large positive integer
    })
    void norm_normalizesCoordinate(double input, double expected) {
        assertEquals(expected, Point.norm(input), 1e-12);
    }

    /**
     * Ensures the constructor automatically normalizes input coordinates using {@code norm()}.
     * This guarantees that every {@code Point} instance starts in a valid state.
     */
    @Test
    void constructor_normalizesCoordinates() {
        Point p = new Point(1.2, -0.3);
        assertEquals(0.2, p.getX(), 1e-12);  // 1.2 % 1 → 0.2
        assertEquals(0.7, p.getY(), 1e-12);  // -0.3 → 0.7
    }

    /**
     * Confirms that setter methods ({@code setX}, {@code setY}) also apply normalization,
     * preserving the toroidal invariant even after mutation.
     */
    @Test
    void setters_normalizeCoordinates() {
        Point p = new Point(0.0, 0.0);
        p.setX(1.5);   // → 0.5
        p.setY(-0.4);  // → 0.6
        assertEquals(0.5, p.getX(), 1e-12);
        assertEquals(0.6, p.getY(), 1e-12);
    }

    // === Test in-place addition with toroidal wrap-around ===

    /**
     * Validates that {@code addPoint()} performs vector addition with toroidal wrapping:
     * - Coordinates are summed and normalized modulo 1.
     * - The operation modifies the current object (in-place mutation).
     * - The method returns {@code this} to support method chaining.
     * Includes cases with and without wrap-around.
     */
    @ParameterizedTest
    @CsvSource({
        "0.2, 0.3, 0.4, 0.5, 0.6, 0.8",     // normal addition
        "0.8, 0.9, 0.3, 0.2, 0.1, 0.1",     // wrap-around in both axes
        "0.0, 0.0, 0.0, 0.0, 0.0, 0.0",     // identity element
        "0.9, 0.9, 0.2, 0.2, 0.1, 0.1"      // double wrap
    })
    void addPoint_addsAndWraps(double x1, double y1, double x2, double y2, double expectedX, double expectedY) {
        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);
        Point result = p1.addPoint(p2);

        assertEquals(expectedX, result.getX(), 1e-12);
        assertEquals(expectedY, result.getY(), 1e-12);
        // Ensure the method modifies and returns the same instance (method chaining)
        assertSame(p1, result);
    }

    // === Test additive inverse ===

    /**
     * Checks that {@code inverse()} returns a new point such that:
     *   p + p.inverse() ≡ (0, 0) (mod 1)
     * Also verifies:
     * - Correct inverse coordinates (e.g., (0.3,0.4) → (0.7,0.6))
     * - A new object is created (original is unchanged)
     * - The group property holds: sum with inverse yields origin
     */
    @ParameterizedTest
    @CsvSource({
        "0.0, 0.0, 0.0, 0.0",   // origin is self-inverse
        "0.3, 0.4, 0.7, 0.6",   // standard case
        "0.0, 0.5, 0.0, 0.5",   // edge on one axis
        "1.0, 1.0, 0.0, 0.0",   // normalized to (0,0) before inversion
        "0.2, 0.8, 0.8, 0.2"    // asymmetric case
    })
    void inverse_returnsAdditiveInverse(double x, double y, double expectedX, double expectedY) {
        Point p = new Point(x, y);
        Point inv = p.inverse();

        assertEquals(expectedX, inv.getX(), 1e-12);
        assertEquals(expectedY, inv.getY(), 1e-12);
        // Must be a new instance (no mutation of original)
        assertNotSame(p, inv);
        // Verify group property: p + inv = (0,0)
        Point sum = new Point(p.getX(), p.getY()).addPoint(inv);
        assertEquals(0.0, sum.getX(), 1e-12);
        assertEquals(0.0, sum.getY(), 1e-12);
    }

    /**
     * Explicitly tests that the inverse of the origin (0,0) is itself,
     * which is a fundamental identity in toroidal space.
     */
    @Test
    void inverse_ofOriginIsOrigin() {
        Point origin = new Point(0.0, 0.0);
        Point inv = origin.inverse();
        assertEquals(0.0, inv.getX(), 1e-12);
        assertEquals(0.0, inv.getY(), 1e-12);
    }

    /**
     * Validates the involution property: applying {@code inverse()} twice
     * returns the original point (i.e., inverse is its own inverse).
     * This is a key mathematical property of additive inverses in modular arithmetic.
     */
    @ParameterizedTest
    @CsvSource({
        "0.0,   0.0",
        "0.3,   0.4",
        "0.0,   0.5",
        "1.0,   1.0",
        "0.2,   0.8",
        "0.5,   0.5",
        "1.0,   0.0",
        "1.3,   0.3",
        "2.7,   0.7",
        "-0.1,  0.9",
        "-0.5,  0.5",
        "-1.0,  0.0",
        "-1.2,  0.8",
        "3.0,   0.0"
    })
    void inverse_ofInversedIsTheSame(double x, double y) {
        Point point = new Point(x, y);
        Point inv = point.inverse();
        Point twiceInv = inv.inverse();
        // Applying inverse twice should yield the original point
        assertEquals(point.getX(), twiceInv.getX(), 1e-12);
        assertEquals(point.getY(), twiceInv.getY(), 1e-12);
    }

    // === Test integer boundary handling ===

    /**
     * Ensures that all integer inputs (positive, negative, large magnitude)
     * are correctly normalized to 0.0, as required by modulo-1 arithmetic.
     * This is critical for robustness in simulations with large displacements.
     */
    @ParameterizedTest
    @ValueSource(doubles = {0.0, 1.0, -1.0, 2.0, -2.0, 1000.0, -1000.0})
    void point_handlesIntegerBoundaries(double coord) {
        Point p = new Point(coord, coord);
        assertEquals(0.0, p.getX(), 1e-12);
        assertEquals(0.0, p.getY(), 1e-12);
    }

    // === Test distance methods ===

    /**
     * Validates that {@code distanceTo()} computes the shortest Euclidean distance
     * from a given point to the origin (0,0) in the 2D toroidal space [0,1) × [0,1).
     * <p>
     * The test verifies correct handling of periodic boundaries: when the direct
     * distance exceeds 0.5 in any dimension, the method must consider the wrapped
     * path (e.g., distance from 0.8 to 0.0 is 0.2, not 0.8). All test cases use
     * precomputed minimal distances that respect toroidal geometry.
     */
    @ParameterizedTest
    @CsvSource({
        "0.3, 0.0, 0.3",
        "0.0, 0.25, 0.25",
        "0.8, 0.0, 0.2",
        "0.0, 0.74, 0.26",
        "0.9, 0.9, 0.1414213562373095",
        "0.5, 0.5, 0.7071067811865476",
        "0.0, 0.0, 0.0"
    })
    void distanceTo_origin(double x, double y, double expectedDistance)
    {
        Point point = new Point(x, y);
        double distance = point.distanceTo(new Point(0.0, 0.0));
        assertEquals(distance, expectedDistance, 1e-12);
    }

    /**
     * Confirms that the toroidal distance is symmetric: the distance from A to B
     * equals the distance from B to A. This property must hold in any metric space,
     * including the toroidal one.
     */
    @ParameterizedTest
    @CsvSource({
        "0.1, 0.2, 0.4, 0.5",
        "0.9, 0.1, 0.2, 0.8",
        "0.3, 0.7, 0.6, 0.2",
        "0.0, 0.0, 0.5, 0.5",
        "0.8, 0.9, 0.1, 0.1"
    })
    void distanceTo_isSymmetric(double x1, double y1, double x2, double y2) {
        Point a = new Point(x1, y1);
        Point b = new Point(x2, y2);
        double d1 = a.distanceTo(b);
        double d2 = b.distanceTo(a);
        assertEquals(d1, d2, 1e-12);
    }

    /**
     * Ensures that the static method {@code distanceBetween} produces identical results
     * to the instance method {@code distanceTo} for the same pair of points.
     * This guarantees consistency between the two APIs.
     */
    @ParameterizedTest
    @CsvSource({
        "0.1, 0.2, 0.4, 0.5",
        "0.9, 0.1, 0.2, 0.8",
        "0.3, 0.7, 0.6, 0.2",
        "0.0, 0.0, 0.5, 0.5",
        "0.8, 0.9, 0.1, 0.1"
    })
    void distanceBetween_equalsDistanceTo(double x1, double y1, double x2, double y2) {
        Point a = new Point(x1, y1);
        Point b = new Point(x2, y2);
        double d1 = a.distanceTo(b);
        double d2 = Point.distanceBetween(a, b);
        assertEquals(d1, d2, 1e-12);
    }

    /**
     * Verifies that {@code distanceBetween} throws an {@link IllegalArgumentException}
     * with a non-empty message,
     * when either of the input points is {@code null}, ensuring robust input validation.
     */
    @Test
    void distanceBetween_throwsOnNullInput() {
        Point p = new Point(0.5, 0.5);
        IllegalArgumentException exFirst = assertThrows(IllegalArgumentException.class, () -> {
            Point.distanceBetween(null, p);
        });
        IllegalArgumentException exSecond = assertThrows(IllegalArgumentException.class, () -> {
            Point.distanceBetween(p, null);
        });
        IllegalArgumentException exAll = assertThrows(IllegalArgumentException.class, () -> {
            Point.distanceBetween(null, null);
        });
        assertNotNull(exFirst.getMessage());
        assertFalse(exFirst.getMessage().isBlank());
        assertNotNull(exSecond.getMessage());
        assertFalse(exSecond.getMessage().isBlank());
        assertNotNull(exAll.getMessage());
        assertFalse(exAll.getMessage().isBlank());
    }

    /**
     * Confirms that {@code distanceTo} throws an {@link IllegalArgumentException}
     * when the argument is {@code null}, maintaining consistent error handling
     * with the static counterpart {@code distanceBetween}.
     */
    @Test
    void distanceTo_throwsOnNullInput() {
        Point p = new Point(0.3, 0.7);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            p.distanceTo(null);
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    /**
     * Validates that the distance from any point to itself is exactly zero,
     * which is a fundamental property of any valid metric.
     */
    @ParameterizedTest
    @CsvSource({
        "0.0, 0.0",
        "0.3, 0.4",
        "0.8, 0.9",
        "1.5, -0.2",  // will be normalized to (0.5, 0.8)
        "0.5, 0.5"
    })
    void distanceTo_selfIsZero(double x, double y) {
        Point p = new Point(x, y);
        assertEquals(0.0, p.distanceTo(p), 1e-12);
        assertEquals(0.0, Point.distanceBetween(p, p), 1e-12);
    }

    /**
     * Checks that the maximum possible toroidal distance in [0,1) × [0,1)
     * is √(0.5² + 0.5²) ≈ 0.7071, achieved between points like (0,0) and (0.5,0.5).
     * No two points can be farther apart in this space.
     */
    @Test
    void maximumDistance_isCorrect() {
        Point a = new Point(0.0, 0.0);
        Point b = new Point(0.5, 0.5);
        double maxDist = Math.sqrt(0.5 * 0.5 + 0.5 * 0.5); // ≈ 0.7071067811865476
        assertEquals(maxDist, a.distanceTo(b), 1e-12);
        assertEquals(maxDist, Point.distanceBetween(a, b), 1e-12);
    }
}