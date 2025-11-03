// src/main/java/org/jinseisieko/evolution/base/Food.java
package org.jinseisieko.evolution.base;

import org.jinseisieko.evolution.basic.Point;

public class Food extends Entity { 
    private double health;
    private final double TIME_HEALTH_COST;

    public Food(Point initialCoordinates, double radius, BasicSimulation simulation, double TIME_HEALTH_COST) {
        super(initialCoordinates, radius, simulation);
        this.health = 1.0;
        this.TIME_HEALTH_COST = TIME_HEALTH_COST;
    }

    @Override
    public void updateEntity(double dt) {
        this.health -= TIME_HEALTH_COST;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }
}