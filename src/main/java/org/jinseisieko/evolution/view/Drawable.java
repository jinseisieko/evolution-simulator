// src/main/java/org/jinseisieko/evolution/view/Drawable.java
package org.jinseisieko.evolution.view;

import java.awt.Shape;

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
}