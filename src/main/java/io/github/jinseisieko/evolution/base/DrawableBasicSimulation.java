// src/main/java/io.github/jinseisieko/evolution/base/DrawableBasicSimulation.java
package io.github.jinseisieko.evolution.base;

import java.util.List;

import io.github.jinseisieko.evolution.view.Drawable;

public interface DrawableBasicSimulation extends BasicSimulation {
    public List<Drawable> getDrawables();
}