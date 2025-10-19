// src/test/java/org/jinseisieko/evolution/decisiontree/stubs/EnergyQuestion.java
package org.jinseisieko.evolution.decisiontree.stubs;

import org.jinseisieko.evolution.decisiontree.Question;

public final class EnergyQuestion implements Question {
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
}