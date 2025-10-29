// src/main/java/org/jinseisieko/evolution/base/Simulation.java
package org.jinseisieko.evolution.base;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Simulation implements BasicSimulation {

    private final List<Agent> agents;

    public Simulation() {
        this.agents = new ArrayList<>();
    }

    @Override
    public void update(double deltaTime) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void draw(Graphics2D g2d) {
        throw new UnsupportedOperationException("Not supported");
    }

    public void addAgent(Agent agent) {
        this.agents.add(agent);
    }

    @Override
    public List<Agent> getAgents() {
        return agents;
    }

    @Override
    public List<Agent> getAgentsNearby(double x, double y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Agent> getAgentsNearby(Point position) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}