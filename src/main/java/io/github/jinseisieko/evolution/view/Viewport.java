// src/main/java/io.github/jinseisieko/evolution/view/Viewport.java
package io.github.jinseisieko.evolution.view;

import java.awt.Graphics2D;
import java.util.List;

/**
 * Represents a viewport for rendering objects that implement the {@link Drawable} interface.
 * <p>
 * The {@code Viewport} is responsible for:
 * <ul>
 *   <li>Converting logical coordinates (in the range [0.0, 1.0)) to pixel coordinates on the screen.</li>
 *   <li>Applying a visual {@link DrawStyle} to each object before rendering.</li>
 *   <li>Delegating the actual drawing to the {@code Drawable} object based on its shape type and size.</li>
 * </ul>
 * <p>
 * This class is used by the {@link SimulationView} component to visualize the current state of the simulation.
 *
 * @author jinseisieko
 */
public class Viewport {
    private final int width;
    private final int height;
    private final int posX;
    private final int posY;
    private Graphics2D graphics2D;

    /**
     * Constructs a new {@code Viewport} with the specified dimensions and screen position.
     * <p>
     * Note: The {@code Graphics2D} context must be provided separately via
     * {@link #updateGraphics2D(Graphics2D)} before any drawing operations.
     *
     * @param width  the width of the viewport in pixels <p>
     * @param height the height of the viewport in pixels <p>
     * @param posX   the X coordinate of the top-left corner of the viewport on the screen <p>
     * @param posY   the Y coordinate of the top-left corner of the viewport on the screen <p>
     *
     * @author jinseisieko
     */
    public Viewport(int width, int height, int posX, int posY) {
        this.graphics2D = null;
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * Updates the {@code Graphics2D} context used for subsequent rendering operations.
     * <p>
     * This method must be called before invoking any {@code draw} method.
     *
     * @param graphics2D the rendering context to use; must not be {@code null} <p>
     * @throws IllegalArgumentException if {@code graphics2D} is {@code null} <p>
     *
     * @author jinseisieko
     */
    public void updateGraphics2D(Graphics2D graphics2D) {
        if (graphics2D == null) {
            throw new IllegalArgumentException("graphics2D cannot be null");
        }
        this.graphics2D = graphics2D;
    }

    /**
     * Renders a single {@link Drawable} object.
     * <p>
     * This method converts the object's logical position and size into pixel coordinates,
     * applies its visual style, and delegates the actual drawing to the object's
     * {@link Drawable#draw(Graphics2D, int, int, int)} method.
     *
     * @param drawable the object to render; must not be {@code null} <p>
     * @throws IllegalStateException if {@link #updateGraphics2D(Graphics2D)} has not been called <p>
     *
     * @author jinseisieko
     */
    public void draw(Drawable drawable) {
        
        // Apply the object's visual style
        applyStyle(drawable.getStyle());
        // Convert logical coordinates and size to pixel values
        int pixelX = toPixelX(drawable.getX());
        int pixelY = toPixelY(drawable.getY());
        int pixelSize = toPixelSize(drawable.getSize());

        // Delegate actual rendering to the drawable object
        drawable.draw(graphics2D, pixelX, pixelY, pixelSize);

        
    }

    /**
     * Renders a list of {@link Drawable} objects.
     * <p>
     * Each object is drawn in the order it appears in the list.
     *
     * @param drawables the list of objects to render; must not be {@code null} <p>
     *
     * @author jinseisieko
     */
    public void drawAll(List<Drawable> drawables) {
        if (this.graphics2D == null) {
            throw new IllegalStateException("graphics2D must be updated before drawing");
        }
        for (Drawable drawable : drawables) {
            draw(drawable);
        }
        // Clear reference to avoid accidental reuse without reinitialization
        this.graphics2D = null;
    }

    /**
     * Applies the specified drawing style to the current {@code Graphics2D} context.
     *
     * @param style the visual style to apply; must not be {@code null} <p>
     *
     * @author jinseisieko
     */
    private void applyStyle(DrawStyle style) {
        style.apply(graphics2D);
    }

    /**
     * Converts a logical X coordinate (in the range [0.0, 1.0)) to a pixel X coordinate.
     *
     * @param x the logical X coordinate <p>
     * @return the corresponding pixel X coordinate, relative to the screen origin <p>
     *
     * @author jinseisieko
     */
    private int toPixelX(double x) {
        return posX + (int) Math.round(x * width);
    }

    /**
     * Converts a logical Y coordinate (in the range [0.0, 1.0)) to a pixel Y coordinate.
     *
     * @param y the logical Y coordinate <p>
     * @return the corresponding pixel Y coordinate, relative to the screen origin <p>
     *
     * @author jinseisieko
     */
    private int toPixelY(double y) {
        return posY + (int) Math.round(y * height);
    }

    /**
     * Converts a logical size (e.g., diameter or side length) to a pixel size.
     * <p>
     * The conversion uses the average of the viewport's width and height to maintain
     * consistent scaling regardless of aspect ratio.
     *
     * @param size the logical size (unitless, typically in [0.0, 1.0)) <p>
     * @return the corresponding pixel size, rounded to the nearest integer <p>
     *
     * @author jinseisieko
     */
    private int toPixelSize(double size) {
        return (int) Math.round(size * (width + height) / 2.0);
    }
}