// src/main/java/org/jinseisieko/evolution/base/BasicSimulation.java
package org.jinseisieko.evolution.base;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

public interface BasicSimulation
{
    public void update(double deltaTime);
    public void draw(Graphics2D g2d);
    public List<Entity> getEntities();
    public List<Entity> getEntitiesNearby(double x, double y);
    public List<Entity> getEntitiesNearby(Point position);

}

