// src/main/java/io.github/jinseisieko/evolution/basic/Shape.java
package io.github.jinseisieko.evolution.basic;

/**
 * Represents an object that have shape (circle, square)
 * 
 * @author jinseisieko
 */
public interface Shape {
    /**
     * Returns the geometric shape used to represent this object visually.
     *
     * @return the shape type (e.g., CIRCLE, SQUARE) <p>
     */
    ShapeType getShapeType();
}
