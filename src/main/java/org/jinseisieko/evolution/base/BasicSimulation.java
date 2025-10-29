// src/main/java/org/jinseisieko/evolution/base/BasicSimulation.java
package org.jinseisieko.evolution.base;

import java.awt.Graphics2D;

public interface BasicSimulation
{
    public void update(double deltaTime);
    public void draw(Graphics2D g2d);
}

