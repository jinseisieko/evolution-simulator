// src/main/java/org/jinseisieko/evolution/bobsimulation/questions/SpeedQuestion.java
package org.jinseisieko.evolution.bobsimulation.questions;

import java.util.Objects;

import org.jinseisieko.evolution.base.Agent;
import org.jinseisieko.evolution.base.ResponsibleQuestion;

public class SpeedQuestion extends ResponsibleQuestion {
    private double threshold; 

    public SpeedQuestion(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean apply(Agent agent) {
        return agent.getSpeed() > threshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpeedQuestion that)) return false;
        return Double.compare(that.threshold, threshold) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), threshold);
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
    
}