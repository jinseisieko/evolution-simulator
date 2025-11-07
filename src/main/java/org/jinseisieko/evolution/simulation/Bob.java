// src/main/java/org/jinseisieko/evolution/simulation/Bob.java
package org.jinseisieko.evolution.simulation;

import java.awt.Graphics2D;

import org.jinseisieko.evolution.base.Agent;
import org.jinseisieko.evolution.base.BasicSimulation;
import org.jinseisieko.evolution.base.Brain;
import org.jinseisieko.evolution.basic.Point;
import org.jinseisieko.evolution.bindingcomponents.Question;
import org.jinseisieko.evolution.view.DrawStyle;
import org.jinseisieko.evolution.view.Drawable;

public class Bob extends Agent implements Drawable {

    public Bob(Point initialCoordinates, double radius, double brainUpdateTime, Brain brain, BasicSimulation simulation, double BRAIN_ENERGY_COST, double SPEED_ENERGY_COST, double ANGULAR_SPEED_ENERGY_COST, double EAT_FOOD_ENERGY_COST) {
        super(initialCoordinates, radius, brainUpdateTime, brain, simulation, BRAIN_ENERGY_COST, SPEED_ENERGY_COST, ANGULAR_SPEED_ENERGY_COST, EAT_FOOD_ENERGY_COST);
    }

    @Override
    public boolean answer(Question question) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void statusActivity() {
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
    
}

