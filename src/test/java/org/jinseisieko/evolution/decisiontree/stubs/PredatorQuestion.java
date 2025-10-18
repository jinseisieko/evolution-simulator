// src/test/java/org/jinseisieko/evolution/decisiontree/stubs/PredatorQuestion.java
package org.jinseisieko.evolution.decisiontree.stubs;

import org.jinseisieko.evolution.decisiontree.Question;

public final class PredatorQuestion implements Question {
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