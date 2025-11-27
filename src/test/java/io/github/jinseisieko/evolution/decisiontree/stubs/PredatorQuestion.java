// src/test/java/io.github/jinseisieko/evolution/decisiontree/stubs/PredatorQuestion.java
package io.github.jinseisieko.evolution.decisiontree.stubs;

import io.github.jinseisieko.evolution.bindingcomponents.Question;

public final class PredatorQuestion extends  Question {
    private final double radius;

    public PredatorQuestion(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PredatorQuestion q)) return false;
        return Double.compare(q.radius, radius) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(radius);
    }
}