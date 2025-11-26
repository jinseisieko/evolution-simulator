// src/test/java/org/jinseisieko/evolution/base/stubs/SpeedQuestion.java
package org.jinseisieko.evolution.base.stubs;

import org.jinseisieko.evolution.bindingcomponents.Question;

public final class SpeedQuestion extends Question {
    private final double threshold;

    public SpeedQuestion(double threshold) {
        this.threshold = threshold;
    }

    public double getThreshold() {
        return threshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpeedQuestion that)) return false;
        return Double.compare(that.threshold, threshold) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(threshold);
    }
}