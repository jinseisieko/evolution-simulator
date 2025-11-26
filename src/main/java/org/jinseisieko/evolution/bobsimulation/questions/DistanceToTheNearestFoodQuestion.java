// src/main/java/org/jinseisieko/evolution/bobsimulation/questions/DistanceToTheNearestFoodQuestion.java
package org.jinseisieko.evolution.bobsimulation.questions;

import java.util.List;
import java.util.Objects;

import org.jinseisieko.evolution.base.Agent;
import org.jinseisieko.evolution.base.Entity;
import org.jinseisieko.evolution.base.Food;
import org.jinseisieko.evolution.base.ResponsibleQuestion;
import org.jinseisieko.evolution.basic.Point;
import org.jinseisieko.evolution.bobsimulation.BobSimulation;

public class DistanceToTheNearestFoodQuestion extends ResponsibleQuestion {
    private double threshold; 

    public DistanceToTheNearestFoodQuestion(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean apply(Agent agent) {
        BobSimulation simulation = (BobSimulation) agent.getSimulation();
        List<Entity> entities = simulation.getEntitiesNearby(agent.getPosition());
        List<Food> foods = entities.stream()
            .filter(e -> e instanceof Food)
            .map(e -> (Food) e)
            .toList();
        double cDistance = Double.MAX_VALUE; 
        for (Food food : foods) {
            double distance = Point.distanceBetween(agent.getPosition(), food.getPosition());
            if (cDistance > distance) {
                cDistance = distance;
            }
        }
        return cDistance < threshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DistanceToTheNearestFoodQuestion that)) return false;
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