// src/test/java/org/jinseisieko/evolution/view/stubs/DrawableStub.java
package io.github.jinseisieko.evolution.view.stubs;

import java.awt.Graphics2D;

import io.github.jinseisieko.evolution.basic.ShapeType;
import io.github.jinseisieko.evolution.view.DrawStyle;
import io.github.jinseisieko.evolution.view.Drawable;

public class DrawableStub implements Drawable {
    private final double x;
    private final double y;
    private final double size;
    private final DrawStyle style;

    public DrawableStub(double x, double y, double size, DrawStyle style) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.style = style;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getSize() {
        return size;
    }

    @Override
    public DrawStyle getStyle() {
        return style;
    }

    @Override
    public void draw(Graphics2D g2d, int pixelX, int pixelY, int pixelSize) {
        int radius = pixelSize / 2;
        g2d.fillOval(pixelX - radius, pixelY - radius, pixelSize, pixelSize);
    }

    @Override
    public ShapeType getShapeType() {
        return ShapeType.CIRCLE;
    }
    
}