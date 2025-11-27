// src/main/java/io.github/jinseisieko/evolution/base/BasicSimulation.java
package io.github.jinseisieko.evolution.base;

import io.github.jinseisieko.evolution.basic.Point;
import java.util.List;

public interface BasicSimulation
{
    public void update(double deltaTime);
    public List<Entity> getEntities();
    public List<Entity> getEntitiesNearby(double x, double y);
    public List<Entity> getEntitiesNearby(Point position);

}

