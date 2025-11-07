// src/main/java/org/jinseisieko/evolution/bobsimulation/BobSimulation.java
package org.jinseisieko.evolution.bobsimulation;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.jinseisieko.evolution.base.DrawableBasicSimulation;
import org.jinseisieko.evolution.base.Simulation;
import org.jinseisieko.evolution.basic.Point;
import org.jinseisieko.evolution.view.Drawable;

public class BobSimulation extends Simulation implements DrawableBasicSimulation {
    static final double RADIUS = 0.05;
    static final double MEAT_TIME_HEALTH_COST = 0.01;
    static final double MEAT_ENERGY_VALUE = 0.01;
    private final List<Drawable> drawables;

    public BobSimulation() {
        super();
        this.drawables = new ArrayList<>();
    }

    public void addAgent(Bob bob) {
        super.addAgent(bob);
        this.drawables.add(bob);
    }

    public void addFood(Meat meat) {
        super.addFood(meat);
        this.drawables.add(meat);
    }

    public void addMeat(Meat meat) {
        super.addFood(meat);
        this.drawables.add(meat);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void spawnMeat(double x, double y) {
        Meat meat = new Meat(new Point(x, y),  RADIUS, this, MEAT_TIME_HEALTH_COST, MEAT_ENERGY_VALUE);
        this.addMeat(meat);
    }
}