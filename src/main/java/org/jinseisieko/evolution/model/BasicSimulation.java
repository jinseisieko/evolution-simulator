// src/main/java/org/jinseisieko/simulation/Simulation.java
package org.jinseisieko.evolution.model;

import java.awt.Graphics2D;

public interface BasicSimulation
{
    public void update(double deltaTime);
    public void draw(Graphics2D g2d);
}

