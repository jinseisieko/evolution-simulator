// src/test/java/io.github/jinseisieko/evolution/basic/SquareTest.java
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
 * Unit tests for the {@link Square} class, which represents an axis-aligned square region
 * in a 2D toroidal space [0,1) × [0,1). The square extends {@link MovingObject} and implements
 * the {@link Shape} interface, adding a half-size parameter that defines the distance
 * from the center to any side. It supports spatial queries such as point containment
 * and intersection with other squares, all computed using toroidal distance.
 * 
 * @author jinseisieko
 */
class SquareTest {

    // === Test constructors and initial state ===

    /**
     * Verifies that constructing a {@code Square} from a {@code Point} and half-size
     * correctly initializes its center using normalized coordinates and stores a defensive copy
     * (i.e., changes to the original {@code Point} do not affect the square).
     */
    @Test
    void constructorWithPoint_shouldSetNormalizedCenterAndHalfSize() {
        Point p = new Point(1.2, -0.3);
        Square square = new Square(p, 0.25);

        assertEquals(0.2, square.getX(), 1e-10);
        assertEquals(0.7, square.getY(), 1e-10);
        assertEquals(0.25, square.getHalfSize(), 1e-10);

        p.setX(10);
        assertEquals(0.2, square.getX(), 1e-10);
    }

    /**
     * Confirms that the coordinate-based constructor correctly normalizes inputs
     * and initializes center.
     */
    @Test
    void constructorWithCoordinates_shouldNormalizeAndSetCenterAndHalfSize() {
        Square square = new Square(1.5, -0.4, 0.3);
        assertEquals(0.5, square.getX(), 1e-10);
        assertEquals(0.6, square.getY(), 1e-10);
        assertEquals(0.3, square.getHalfSize(), 1e-10);
    }

    /**
     * Ensures that passing a negative half-size to the constructor
     * throws an {@link IllegalArgumentException} with a non-empty, informative message,
     * preserving geometric validity.
     */
    @Test
    void constructorWithNegativeHalfSize_shouldThrowExceptionWithMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            Square square = new Square(0.0, 0.0, -0.5);
            square.getHalfSize();
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    // === Test inherited movement behavior ===

    /**
     * Validates that {@code Square} correctly inherits {@code moveTo} behavior from {@code MovingObject},
     * updating its center while preserving the half-size.
     */
    @Test
    void moveTo_shouldUpdateCenterAndPreserveHalfSize() {
        Square square = new Square(0.1, 0.2, 0.15);
        Point target = new Point(1.8, -0.1);
        square.moveTo(target);

        assertEquals(0.8, square.getX(), 1e-10);
        assertEquals(0.9, square.getY(), 1e-10);
        assertEquals(0.15, square.getHalfSize(), 1e-10);
    }

    /**
     * Confirms that {@code shift} updates the center using toroidal arithmetic
     * and leaves the half-size unchanged.
     */
    @Test
    void shift_shouldApplyToroidalAdditionToCenterAndPreserveHalfSize() {
        Square square = new Square(0.8, 0.9, 0.2);
        Point delta = new Point(0.3, 0.2);
        square.shift(delta);

        assertEquals(0.1, square.getX(), 1e-10);
        assertEquals(0.1, square.getY(), 1e-10);
        assertEquals(0.2, square.getHalfSize(), 1e-10);
    }

    // === Test half-size mutability ===

    /**
     * Validates that {@code setHalfSize} updates the half-size when given a non-negative value.
     */
    @Test
    void setHalfSize_shouldUpdateHalfSize() {
        Square square = new Square(0.0, 0.0, 0.5);
        square.setHalfSize(0.75);
        assertEquals(0.75, square.getHalfSize(), 1e-10);
        square.setHalfSize(0.34);
        assertEquals(0.34, square.getHalfSize(), 1e-10);
    }

    /**
     * Ensures that calling {@code setHalfSize} with a negative argument
     * throws an {@link IllegalArgumentException} with a non-empty, informative message.
     */
    @Test
    void setNegativeHalfSize_shouldThrowExceptionWithMessage() {
        Square square = new Square(0.0, 0.0, 0.5);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            square.setHalfSize(-0.1);
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    // === Test shape type and side length ===

    /**
     * Confirms that {@code getShapeType()} always returns {@link ShapeType#SQUARE},
     * as expected for this class.
     */
    @Test
    void getShapeType_shouldReturnSquare() {
        Square square = new Square(0.0, 0.0, 0.1);
        assertEquals(ShapeType.SQUARE, square.getShapeType());
        square.moveTo(0.1, 0.1);
        assertEquals(ShapeType.SQUARE, square.getShapeType());
        square.shift(0.1, 0.2);
        assertEquals(ShapeType.SQUARE, square.getShapeType());
        square.setHalfSize(0.4);
        assertEquals(ShapeType.SQUARE, square.getShapeType());
    }

    /**
     * Validates that {@code getSideLenght()} correctly returns twice the half-size.
     */
    @Test
    void getSideLenght_shouldReturnTwiceHalfSize() {
        Square square = new Square(0.0, 0.0, 0.3);
        assertEquals(0.6, square.getSideLenght(), 1e-10);
        square.setHalfSize(0.25);
        assertEquals(0.5, square.getSideLenght(), 1e-10);
    }

    // === Test containment logic ===

    /**
     * Validates that {@code contains} returns {@code true} for points inside or on the boundary
     * of the square, using toroidal axis-aligned bounding box (AABB) logic.
     */
    @ParameterizedTest
    @CsvSource({
        "0.0, 0.0, 0.3, 0.0, 0.0",
        "0.0, 0.0, 0.3, 0.3, 0.0",
        "0.0, 0.0, 0.3, 0.0, 0.3",
        "0.0, 0.0, 0.3, 0.2, 0.1",
        "0.8, 0.8, 0.3, 0.0, 0.9",
        "0.9, 0.9, 0.2, 0.1, 0.1",
        "0.01, 0.5, 0.1, 0.95, 0.5"
    })
    void contains_shouldReturnTrueForPointsInsideOrOnBoundary(
        double cx, double cy, double halfSize, double px, double py
    ) {
        Square square = new Square(cx, cy, halfSize);
        Point point = new Point(px, py);
        assertTrue(square.contains(point));
    }

    /**
     * Validates that {@code contains} returns {@code false} for points strictly outside
     * the square, even when wrap-around paths are considered.
     */
    @ParameterizedTest
    @CsvSource({
        "0.0, 0.0, 0.3, 0.4, 0.0",   // outside right
        "0.0, 0.0, 0.3, 0.0, 0.4",   // outside top
        "0.0, 0.0, 0.3, 0.4, 0.4",   // outside diagonal
        "0.8, 0.8, 0.2, 0.3, 0.3", 
        "0.9, 0.9, 0.1, 0.2, 0.2" 
    })
    void contains_shouldReturnFalseForPointsOutside(
        double cx, double cy, double halfSize, double px, double py
    ) {
        Square square = new Square(cx, cy, halfSize);
        Point point = new Point(px, py);
        assertFalse(square.contains(point));
    }

    /**
     * Ensures that calling {@code contains(null)} throws an {@link IllegalArgumentException}
     * with a non-empty, informative message.
     */
    @Test
    void contains_withNullPoint_shouldThrowExceptionWithMessage() {
        Square square = new Square(0.5, 0.5, 0.2);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            square.contains(null);
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    // === Test intersection logic ===

    /**
     * Validates that {@code intersects} returns {@code true} when two squares overlap
     * or touch (including via toroidal wrap-around), using AABB intersection logic.
     */
    @ParameterizedTest
    @CsvSource({
        "0.0, 0.0, 0.3, 0.2, 0.0, 0.2",   // overlapping horizontally
        "0.0, 0.0, 0.3, 0.0, 0.3, 0.3",   // tangent vertically
        "0.9, 0.9, 0.2, 0.2, 0.2, 0.2",   // wrap-around intersection
        "0.5, 0.5, 0.4, 0.0, 0.0, 0.4",   // large squares intersecting across center
        "0.0, 0.0, 0.2, 0.0, 0.4, 0.2",   // touching vertically
        "0.8, 0.8, 0.3, 0.2, 0.7, 0.1"
    })
    void intersects_shouldReturnTrueForOverlappingOrTouchingSquares(
        double x1, double y1, double h1,
        double x2, double y2, double h2
    ) {
        Square s1 = new Square(x1, y1, h1);
        Square s2 = new Square(x2, y2, h2);
        assertTrue(s1.intersects(s2));
    }

    /**
     * Validates that {@code intersects} returns {@code false} when two squares are
     * strictly separated, even when considering toroidal wrap-around.
     */
    @ParameterizedTest
    @CsvSource({
        "0.0, 0.0, 0.2, 0.5, 0.0, 0.2",   // separated horizontally (gap = 0.1)
        "0.0, 0.0, 0.2, 0.0, 0.5, 0.2",   // separated vertically
        "0.0, 0.0, 0.2, 0.5, 0.5, 0.2",   // separated diagonally
        "0.9, 0.9, 0.1, 0.11, 0.11, 0.1", // wrap-around still too far (dx=0.2 > 0.1+0.1)
        "0.8, 0.8, 0.3, 0.3, 0.9, 0.1",
    })
    void intersects_shouldReturnFalseForSeparatedSquares(
        double x1, double y1, double h1,
        double x2, double y2, double h2
    ) {
        Square s1 = new Square(x1, y1, h1);
        Square s2 = new Square(x2, y2, h2);
        assertFalse(s1.intersects(s2));
    }

    /**
     * Ensures that calling {@code intersects(null)} throws an {@link IllegalArgumentException}
     * with a non-empty message.
     */
    @Test
    void intersects_withNullSquare_shouldThrowExceptionWithMessage() {
        Square square = new Square(0.5, 0.5, 0.2);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            square.intersects(null);
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    // === Test defensive copying ===

    /**
     * Validates that {@code getCenter()} returns a defensive copy of the internal position,
     * so external modifications do not affect the square’s state.
     */
    @Test
    void getCenter_shouldReturnDefensiveCopy() {
        Square square = new Square(0.3, 0.7, 0.1);
        Point center = square.getCenter();
        center.setX(0.999);
        assertEquals(0.3, square.getX(), 1e-10);
        center.setY(0.999);
        assertEquals(0.3, square.getX(), 1e-10);
    }
}