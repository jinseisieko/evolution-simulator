// src/main/java/org/jinseisieko/evolution/base/Simulation.java
package org.jinseisieko.evolution.base;

import org.jinseisieko.evolution.basic.Point;
import java.util.ArrayList;
import java.util.List;

public class Simulation implements BasicSimulation {

    private final List<Entity> entities;

    public Simulation() {
        this.entities = new ArrayList<>();
    }

    @Override
    public void update(double deltaTime) {
        for (Entity elem : entities) {
            elem.updateEntity(deltaTime);
        }
    }

    public void addAgent(Agent agent) {
        this.entities.add(agent);
    }

    public void addFood(Food food) {
        this.entities.add(food);
    }

    @Override
    public List<Entity> getEntities() {
        return entities;
    }

    @Override
    public List<Entity> getEntitiesNearby(double x, double y) {
        return entities;
    }

    @Override
    public List<Entity> getEntitiesNearby(Point position) {
        return entities;
    }
}