// src/test/java/org/jinseisieko/evolution/view/DrawStyleTest.java
package org.jinseisieko.evolution.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Unit tests for the {@link DrawStyle} class, which encapsulates visual rendering attributes
 * for Java Swing's {@code Graphics2D} context. The class provides immutable, reusable styles
 * for fill color, stroke color, stroke width, and opacity.
 * 
 * @author jinseisieko
 */
class DrawStyleTest {

    // === Test factory methods and basic properties ===

    /**
     * Verifies that {@code solid(Color)} creates a style with identical fill and stroke colors,
     * default stroke width (1.0), and full opacity.
     */
    @Test
    void solid_shouldSetSameFillAndStrokeColor() {
        Color color = Color.MAGENTA;
        DrawStyle style = DrawStyle.solid(color);
        assertEquals(color, style.fillColor());
        assertEquals(color, style.strokeColor());
        assertEquals(1.0f, style.strokeWidth(), 1e-5f);
        assertEquals(1.0f, style.opacity(), 1e-5f);
    }

    /**
     * Confirms that {@code filled(Color)} sets fill color while keeping stroke as black.
     */
    @Test
    void filled_shouldSetFillColorAndDefaultStroke() {
        Color fill = Color.CYAN;
        DrawStyle style = DrawStyle.filled(fill);
        assertEquals(fill, style.fillColor());
        assertEquals(Color.BLACK, style.strokeColor());
    }

    /**
     * Validates that {@code outlined(Color, width)} sets stroke color and width,
     * while fill remains black.
     */
    @Test
    void outlined_shouldSetStrokeColorAndWidth() {
        Color stroke = Color.ORANGE;
        float width = 3.5f;
        DrawStyle style = DrawStyle.outlined(stroke, width);
        assertEquals(Color.BLACK, style.fillColor());
        assertEquals(stroke, style.strokeColor());
        assertEquals(width, style.strokeWidth(), 1e-5f);
    }

    /**
     * Ensures that {@code gradientFill} creates a style with a {@code GradientPaint}
     * as the fill paint.
     */
    @Test
    void gradientFill_shouldUseGradientPaintAsFill() {
        DrawStyle style = DrawStyle.gradientFill(0, 0, 100, 100, Color.RED, Color.BLUE);
        assertTrue(style.fillColor() instanceof GradientPaint);
        assertEquals(Color.BLACK, style.strokeColor());
    }

    /**
     * Ensures that {@code gradientFill} creates a style with a {@code GradientPaint}
     * that interpolates correctly between the specified points and colors.
     * Validates by rendering a small image and checking pixel colors at key locations.
     */
    @Test
    void gradientFill_shouldInterpolateCorrectlyBetweenPoints() {
        DrawStyle style = DrawStyle.gradientFill(0, 0, 100, 100, Color.RED, Color.BLUE);
        BufferedImage image = new BufferedImage(101, 101, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        style.apply(g2d);
        g2d.fillRect(0, 0, 101, 101);
        g2d.dispose();

        // At (0,0) → should be close to RED
        Color pixelAtStart = new Color(image.getRGB(0, 0), true);
        assertEquals(Color.RED.getRed(), pixelAtStart.getRed(), 1);
        assertEquals(Color.RED.getGreen(), pixelAtStart.getGreen(), 1);
        assertEquals(Color.RED.getBlue(), pixelAtStart.getBlue(), 1);

        // At (100,100) → should be close to BLUE
        Color pixelAtEnd = new Color(image.getRGB(100, 100), true);
        assertEquals(Color.BLUE.getRed(), pixelAtEnd.getRed(), 1);
        assertEquals(Color.BLUE.getGreen(), pixelAtEnd.getGreen(), 1);
        assertEquals(Color.BLUE.getBlue(), pixelAtEnd.getBlue(), 1);

        // At midpoint (50,50) → should be purple-ish (mix of red and blue)
        Color pixelAtMid = new Color(image.getRGB(50, 50), true);
        int midR = pixelAtMid.getRed();
        int midG = pixelAtMid.getGreen();
        int midB = pixelAtMid.getBlue();
        
        // Green should be near zero; red and blue should be high and similar
        assertTrue(midG < 10, "Midpoint should have low green component");
        assertTrue(midR > 100, "Midpoint should have significant red");
        assertTrue(midB > 100, "Midpoint should have significant blue");
    }

    /**
     * Checks that {@code texturedFill} uses a {@code TexturePaint} for filling.
     */
    @Test
    void texturedFill_shouldUseTexturePaintAsFill() {
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        DrawStyle style = DrawStyle.texturedFill(img);
        assertTrue(style.fillColor() instanceof TexturePaint);
        assertEquals(Color.BLACK, style.strokeColor());
    }

    /**
     * Verifies that passing {@code null} to {@code texturedFill} throws an exception.
     */
    @Test
    void texturedFill_withNullImage_shouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            DrawStyle.texturedFill(null);
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    // === Test builder functionality ===

    /**
     * Confirms that the builder correctly constructs a style with custom attributes.
     */
    @Test
    void builder_shouldConstructStyleWithCustomProperties() {
        DrawStyle style = DrawStyle.builder()
                .fillColor(Color.GREEN)
                .strokeColor(Color.YELLOW)
                .strokeWidth(2.5f)
                .opacity(0.6f)
                .build();

        assertEquals(Color.GREEN, style.fillColor());
        assertEquals(Color.YELLOW, style.strokeColor());
        assertEquals(2.5f, style.strokeWidth(), 1e-5f);
        assertEquals(0.6f, style.opacity(), 1e-5f);
    }

    // === Test apply() method on Graphics2D ===

    /**
     * Validates that {@code apply()} correctly configures {@code Graphics2D}:
     * fill paint, stroke, and composite (opacity).
     */
    @Test
    void apply_shouldConfigureGraphics2DCorrectly() {
        DrawStyle style = DrawStyle.builder()
                .fillColor(Color.PINK)
                .strokeColor(Color.GRAY)
                .strokeWidth(4.0f)
                .opacity(0.3f)
                .build();

        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        style.apply(g2d);

        assertEquals(Color.PINK, g2d.getPaint());
        assertEquals(0.3f, ((AlphaComposite) g2d.getComposite()).getAlpha(), 1e-5f);

        Stroke stroke = g2d.getStroke();
        assertTrue(stroke instanceof BasicStroke);
        assertEquals(4.0f, ((BasicStroke) stroke).getLineWidth(), 1e-5f);

        g2d.dispose();
    }

    /**
     * Checks that opacity is clamped to [0.0, 1.0] during construction.
     */
    @ParameterizedTest
    @ValueSource(floats = {-1.0f, 2.0f, Float.NaN, Float.POSITIVE_INFINITY})
    void opacity_shouldBeClampedToValidRange(float invalidOpacity) {
        DrawStyle style = DrawStyle.builder().opacity(invalidOpacity).build();
        float actual = style.opacity();
        assertTrue(actual >= 0.0f && actual <= 1.0f, "Opacity must be in [0,1], was: " + actual);
    }

    /**
     * Confirms that stroke width is clamped to minimum 0.1f.
     */
    @Test
    void strokeWidth_shouldNotBeZeroOrNegative() {
        DrawStyle style = DrawStyle.builder().strokeWidth(-5.0f).build();
        assertTrue(style.strokeWidth() >= 0.1f);
    }
}