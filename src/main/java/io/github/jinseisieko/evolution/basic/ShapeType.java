// src/main/java/io.github/jinseisieko/evolution/basic/ShapeType.java
package io.github.jinseisieko.evolution.basic;

/**
 * Enumerates the supported geometric shape types that can be rendered by the {@link io.github.jinseisieko.evolution.view.Viewport}.
 * <p>
 * Each constant corresponds to a visual primitive (e.g., circle, square) that a {@link io.github.jinseisieko.evolution.shape.Drawable}
 * object may use to represent itself during rendering. The {@code Viewport} uses this type to decide
 * which drawing routine (e.g., {@code drawOval}, {@code drawRect}) to invoke.
 * <p>
 * This enum is intentionally minimal and extensible—new shape types can be added as needed,
 * provided the {@code Viewport} is updated to handle them in its rendering logic.
 *
 * @author jinseisieko
 */
public enum ShapeType {
    /**
     * A circular shape, rendered as an oval with equal width and height.
     * Typically used for io.githubanisms or particles where orientation is irrelevant.
     */
    CIRCLE,

    /**
     * A square shape, rendered as a rectangle with equal width and height.
     * May be used to represent agents with directional or grid-aligned properties.
     */
    SQUARE
}