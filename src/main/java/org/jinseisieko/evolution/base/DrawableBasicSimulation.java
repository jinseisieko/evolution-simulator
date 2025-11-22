// src/main/java/org/jinseisieko/evolution/base/DrawableBasicSimulation.java
package org.jinseisieko.evolution.base;

import java.util.List;

import org.jinseisieko.evolution.view.Drawable;

public interface DrawableBasicSimulation extends BasicSimulation {
    public List<Drawable> getDrawables();
}