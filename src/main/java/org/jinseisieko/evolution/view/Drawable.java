// src/main/java/org/jinseisieko/evolution/view/Drawable.java
package org.jinseisieko.evolution.view;

import java.awt.Graphics2D;

import org.jinseisieko.evolution.basic.Shape;

/**
 * Represents an object that can be rendered by a {@link Viewport}.
 * <p>
 * Any entity that wishes to appear on screen must implement this interface
 * and provide its current position, size, shape type, and visual style.
 * 
 * @author jinseisieko
 */
public interface Drawable extends Shape {
    
    /**
     * Returns the X coordinate of the object in logical (simulation) space,
     * typically in the range [0.0, 1.0) for a toroidal world.
     *
     * @return logical X coordinate <p>
     */
    double getX();

    /**
     * Returns the Y coordinate of the object in logical (simulation) space,
     * typically in the range [0.0, 1.0).
     *
     * @return logical Y coordinate <p>
     */
    double getY();

    /**
     * Returns the size of the object in logical units.
     * This value is used to scale the object proportionally to the viewport.
     *
     * @return logical size (e.g., diameter or side length) <p>
     */
    double getSize();

    /**
     * Returns the visual style used to render this object.
     *
     * @return the draw style (fill, stroke, opacity, etc.) <p>
     */
    DrawStyle getStyle();

    /**
     * Renders this object using the provided {@code Graphics2D} context at the specified pixel coordinates and size.
     * <p>
     * The method is responsible for drawing the object's shape (e.g., circle or square) using the pixel-based
     * position and size supplied by the {@link Viewport}. The visual appearance should respect the style returned
     * by {@link #getStyle()}, although the {@code Viewport} typically applies the style before invoking this method.
     *
     * @param g2d       the {@code Graphics2D} context to draw on; must not be {@code null} <p>
     * @param pixelX    the X coordinate in screen pixels where the object should be rendered <p>
     * @param pixelY    the Y coordinate in screen pixels where the object should be rendered <p>
     * @param pixelSize the size (e.g., diameter or side length) in screen pixels <p>
     *
     * @author jinseisieko
     */
    void draw(Graphics2D g2d, int pixelX, int pixelY, int pixelSize);
}