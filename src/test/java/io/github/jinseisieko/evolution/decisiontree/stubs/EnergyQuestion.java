// src/test/java/io.github/jinseisieko/evolution/decisiontree/stubs/EnergyQuestion.java
package io.github.jinseisieko.evolution.decisiontree.stubs;

import io.github.jinseisieko.evolution.bindingcomponents.Question;

public final class EnergyQuestion extends  Question {
    private final double threshold;

    public EnergyQuestion(double threshold) {
        this.threshold = threshold;
    }

    public double getThreshold() {
        return threshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnergyQuestion q)) return false;
        return Double.compare(q.threshold, threshold) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(threshold);
    }

    @Override
    public String toString() {
        return "EnergyQuestion{"+this.threshold+"}";
    }
}