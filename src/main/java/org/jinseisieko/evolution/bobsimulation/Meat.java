// src/main/java/org/jinseisieko/evolution/bobsimulation/Meat.java
package org.jinseisieko.evolution.bobsimulation;

import java.awt.Graphics2D;

import org.jinseisieko.evolution.base.BasicSimulation;
import org.jinseisieko.evolution.base.Food;
import org.jinseisieko.evolution.basic.Point;
import org.jinseisieko.evolution.basic.ShapeType;
import org.jinseisieko.evolution.view.DrawStyle;
import org.jinseisieko.evolution.view.Drawable;

public class Meat extends Food implements Drawable {

    public Meat(Point initialCoordinates, double radius, BasicSimulation simulation, double TIME_HEALTH_COST, double ENERGY_VALUE) {
        super(initialCoordinates, radius, simulation, TIME_HEALTH_COST, ENERGY_VALUE);
    }

    @Override
    public double getX() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getY() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DrawStyle getStyle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void draw(Graphics2D g2d, int pixelX, int pixelY, int pixelSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ShapeType getShapeType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}