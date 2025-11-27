// src/main/java/io.github/jinseisieko/evolution/view/DrawStyle.java
package io.github.jinseisieko.evolution.view;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

/**
 * Represents a reusable drawing style for Java Swing's Graphics2D context.
 * <p>
 * A DrawStyle encapsulates visual attributes such as fill color, stroke color,
 * stroke width, and opacity. It can be applied to a Graphics2D object to
 * consistently render shapes, text, or images with predefined aesthetics.
 * <p>
 * This class supports advanced styling options including:
 * <ul>
 *   <li>Flat colors and gradients for fills and strokes</li>
 *   <li>Variable stroke widths and dash patterns</li>
 *   <li>Transparency via alpha compositing</li>
 * </ul>
 * <p>
 * Styles are immutable by default to ensure thread safety and predictable behavior.
 * Use the {@code builder()} method to construct complex styles fluently.
 *
 * @author jinseisieko
 */
public final class DrawStyle {

    private final Paint fillColor;
    private final Paint strokeColor;
    private final float strokeWidth;
    private final float opacity;
    private final Stroke stroke;

    /**
     * Private constructor to enforce immutability and use of factory methods.
     *
     * @param fillColor   paint used for filling shapes (may be Color, GradientPaint, etc.) <p>
     * @param strokeColor paint used for stroking outlines <p>
     * @param strokeWidth width of stroke in pixels <p>
     * @param opacity     alpha value from 0.0 (transparent) to 1.0 (opaque) <p>
     * @param stroke      stroke definition (BasicStroke, etc.) <p>
     *
     * @author jinseisieko
     */
    private DrawStyle(
            Paint fillColor,
            Paint strokeColor,
            float strokeWidth,
            float opacity,
            Stroke stroke) {
        this.fillColor = fillColor != null ? fillColor : Color.BLACK;
        this.strokeColor = strokeColor != null ? strokeColor : Color.BLACK;
        this.strokeWidth = Math.max(0.1f, strokeWidth); // Prevent zero-width strokes
        this.opacity = Math.max(0.0f, Math.min(1.0f, opacity)); // Clamp to [0,1]
        this.stroke = stroke != null ? stroke : new BasicStroke(strokeWidth);
    }

    /**
     * Returns the paint used for filling shapes.
     *
     * @return the fill paint; never null
     *
     * @author jinseisieko
     */
    public Paint fillColor() {
        return fillColor;
    }

    /**
     * Returns the paint used for stroking outlines.
     *
     * @return the stroke paint; never null <p>
     *
     * @author jinseisieko
     */
    public Paint strokeColor() {
        return strokeColor;
    }

    /**
     * Returns the width of the stroke in pixels.
     *
     * @return stroke width, always ≥ 0.1 <p>
     *
     * @author jinseisieko
     */
    public float strokeWidth() {
        return strokeWidth;
    }

    /**
     * Returns the opacity level (alpha) of the style.
     *
     * @return opacity between 0.0 (fully transparent) and 1.0 (fully opaque) <p>
     *
     * @author jinseisieko
     */
    public float opacity() {
        return opacity;
    }

    /**
     * Applies this draw style to the given Graphics2D context.
     * <p>
     * Sets fill/stroke paints, stroke, and composite (opacity).
     * Note: This method does not modify rendering hints or transformations.
     * If anti-aliasing or affine transforms are needed, configure them
     * separately on the Graphics2D instance before or after applying the style.
     *
     * @param g2d the Graphics2D context to configure <p>
     *
     * @author jinseisieko
     */
    public void apply(Graphics2D g2d) {
        g2d.setPaint(fillColor);
        g2d.setStroke(stroke);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
    }

    /**
     * Creates a new DrawStyle with solid fill and stroke using the same color.
     * <p>
     * Equivalent to using the same color for both fill and outline.
     *
     * @param color the color to use for both fill and stroke <p>
     * @return a new DrawStyle instance <p>
     *
     * @author jinseisieko
     */
    public static DrawStyle solid(Color color) {
        return new Builder()
                .fillColor(color)
                .strokeColor(color)
                .build();
    }

    /**
     * Creates a new DrawStyle with only fill color set; stroke defaults to black.
     *
     * @param color the fill color <p>
     * @return a new DrawStyle instance <p>
     *
     * @author jinseisieko
     */
    public static DrawStyle filled(Color color) {
        return new Builder()
                .fillColor(color)
                .build();
    }

    /**
     * Creates a new DrawStyle with only stroke color and width set; fill defaults to black.
     *
     * @param color the stroke color <p>
     * @param width the stroke width (pixels) <p>
     * @return a new DrawStyle instance <p>
     *
     * @author jinseisieko
     */
    public static DrawStyle outlined(Color color, float width) {
        return new Builder()
                .strokeColor(color)
                .strokeWidth(width)
                .build();
    }

    /**
     * Creates a new DrawStyle with gradient fill.
     * <p>
     * Gradient runs from point (x1,y1) to (x2,y2) with specified start and end colors.
     *
     * @param x1 start X coordinate <p>
     * @param y1 start Y coordinate <p>
     * @param x2 end X coordinate <p>
     * @param y2 end Y coordinate <p>
     * @param c1 start color <p>
     * @param c2 end color <p>
     * @return a new DrawStyle instance <p>
     *
     * @author jinseisieko
     */
    public static DrawStyle gradientFill(float x1, float y1, float x2, float y2, Color c1, Color c2) {
        return new Builder()
                .fillColor(new GradientPaint(x1, y1, c1, x2, y2, c2))
                .build();
    }

    /**
     * Creates a new DrawStyle with texture fill.
     * <p>
     * Uses the provided image as a repeating pattern for filling shapes.
     *
     * @param image the image to use as texture <p>
     * @return a new DrawStyle instance <p>
     *
     * @author jinseisieko
     */
    public static DrawStyle texturedFill(BufferedImage image) {
        if (image == null) throw new IllegalArgumentException("Texture image cannot be null");
        TexturePaint texture = new TexturePaint(image, new Rectangle(0, 0, image.getWidth(), image.getHeight()));
        return new Builder()
                .fillColor(texture)
                .build();
    }

    /**
     * Returns a new builder for constructing complex DrawStyle instances.
     *
     * @return a new Builder instance <p>
     *
     * @author jinseisieko
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Fluent builder for creating DrawStyle instances.
     * <p>
     * Allows chaining multiple configuration calls before calling {@code build()}.
     *
     * @author jinseisieko
     */
    public static class Builder {
        private Paint fillColor = Color.BLACK;
        private Paint strokeColor = Color.BLACK;
        private float strokeWidth = 1.0f;
        private float opacity = 1.0f;
        private Stroke stroke = null;

        /**
         * Sets the fill paint (color, gradient, texture).
         *
         * @param paint the paint to use for filling <p>
         * @return this builder for method chaining <p>
         *
         * @author jinseisieko
         */
        public Builder fillColor(Paint paint) {
            this.fillColor = paint;
            return this;
        }

        /**
         * Sets the stroke paint (color, gradient, texture).
         *
         * @param paint the paint to use for stroking <p>
         * @return this builder for method chaining <p>
         *
         * @author jinseisieko
         */
        public Builder strokeColor(Paint paint) {
            this.strokeColor = paint;
            return this;
        }

        /**
         * Sets the stroke width in pixels.
         *
         * @param width stroke width, must be > 0 <p>
         * @return this builder for method chaining <p>
         *
         * @author jinseisieko
         */
        public Builder strokeWidth(float width) {
            this.strokeWidth = Math.max(0.1f, width);
            return this;
        }

        /**
         * Sets the opacity (alpha) level.
         *
         * @param opacity value from 0.0 (transparent) to 1.0 (opaque); NaN is treated as 0.0 <p>
         * @return this builder for method chaining <p>
         *
         * @author jinseisieko
         */
        public Builder opacity(float opacity) {
            if (Float.isNaN(opacity) || opacity < 0.0f) {
                this.opacity = 0.0f;
            } else if (opacity > 1.0f) {
                this.opacity = 1.0f;
            } else {
                this.opacity = opacity;
            }
            return this;
        }

        /**
         * Sets a custom stroke (e.g., dashed line).
         *
         * @param stroke the stroke definition <p>
         * @return this builder for method chaining <p>
         *
         * @author jinseisieko
         */
        public Builder stroke(Stroke stroke) {
            this.stroke = stroke;
            return this;
        }

        /**
         * Builds and returns a new immutable DrawStyle instance.
         *
         * @return the constructed DrawStyle <p>
         *
         * @author jinseisieko
         */
        public DrawStyle build() {
            if (stroke == null) {
                stroke = new BasicStroke(strokeWidth);
            }
            return new DrawStyle(
                    fillColor,
                    strokeColor,
                    strokeWidth,
                    opacity,
                    stroke);
        }
    }
}