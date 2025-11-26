// src/main/java/org/jinseisieko/evolution/base/BasicSimulation.java
package org.jinseisieko.evolution.base;

import org.jinseisieko.evolution.basic.Point;
import java.util.List;

public interface BasicSimulation
{
    public void update(double deltaTime);
    public List<Entity> getEntities();
    public List<Entity> getEntitiesNearby(double x, double y);
    public List<Entity> getEntitiesNearby(Point position);

}

