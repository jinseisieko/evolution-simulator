// src/main/java/org/jinseisieko/evolution/bobsimulation/Meat.java
package org.jinseisieko.evolution.bobsimulation;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jinseisieko.evolution.base.BasicSimulation;
import org.jinseisieko.evolution.base.Food;
import org.jinseisieko.evolution.basic.Point;
import org.jinseisieko.evolution.view.DrawStyle;
import org.jinseisieko.evolution.view.Drawable;

public class Meat extends Food implements Drawable {
    static final DrawStyle DRAW_STYLE = DrawStyle.filled(Color.ORANGE);

    public Meat(Point initialCoordinates, double radius, BasicSimulation simulation, double TIME_HEALTH_COST, double ENERGY_VALUE) {
        super(initialCoordinates, radius, simulation, TIME_HEALTH_COST, ENERGY_VALUE);
    }

    @Override
    public double getSize() {
        return this.getRadius();
    }

    @Override
    public DrawStyle getStyle() {
        return DRAW_STYLE;
    }

    @Override
    public void draw(Graphics2D g2d, int pixelX, int pixelY, int pixelSize) {
        g2d.drawOval(pixelX-pixelSize, pixelY-pixelSize, pixelX+pixelSize, pixelY+pixelSize);
    }
}