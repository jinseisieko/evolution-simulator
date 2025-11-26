// src/main/java/org/jinseisieko/evolution/base/Simulation.java
package org.jinseisieko.evolution.base;

import java.util.ArrayList;
import java.util.List;

import org.jinseisieko.evolution.basic.Point;

public class Simulation implements BasicSimulation {

    private final List<Entity> entities;

    public Simulation() {
        this.entities = new ArrayList<>();
    }

    @Override
    public void update(double deltaTime) {
        List<Entity> removed = new ArrayList<>();
        for (Entity elem : entities) {
            elem.updateEntity(deltaTime);
            if (!elem.isAlive()) removed.add(elem);
        }
        for (Entity elem : removed) {
            removeEntity(elem);
        }
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void addAgent(Agent agent) {
        agent.setSimulation(this);
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