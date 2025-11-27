// src/main/java/io.github/jinseisieko/evolution/bobsimulation/statuses/TryToEatAndSwitchTo.java

package io.github.jinseisieko.evolution.bobsimulation.statuses;

import java.util.List;

import io.github.jinseisieko.evolution.base.Agent;
import io.github.jinseisieko.evolution.base.Entity;
import io.github.jinseisieko.evolution.base.Food;
import io.github.jinseisieko.evolution.base.ResponsibleStatus;
import io.github.jinseisieko.evolution.basic.Point;
import io.github.jinseisieko.evolution.bobsimulation.BobSimulation;

public class TryToEatAndSwitchTo implements ResponsibleStatus {
    private ResponsibleStatus nextStatus;

    public TryToEatAndSwitchTo(ResponsibleStatus nextStatus) {
        this.nextStatus = nextStatus;
    }

    @Override
    public void applyThisStatus(Agent agent, double dt) {
        BobSimulation simulation = (BobSimulation) agent.getSimulation();
        List<Entity> entities = simulation.getEntitiesNearby(agent.getPosition());
        List<Food> foods = entities.stream()
            .filter(e -> e instanceof Food)
            .map(e -> (Food) e)
            .toList();
        if (!foods.isEmpty()) {
            Food nearestFood = foods.getFirst();
            double cDistance = Double.MAX_VALUE; 
            for (Food food : foods) {
                double distance = Point.distanceBetween(agent.getPosition(), food.getPosition());
                if (cDistance > distance) {
                    nearestFood = food;
                    cDistance = distance;
                }
            }
            if (cDistance < agent.getRadius() + nearestFood.getRadius() + 0.01) nearestFood.beEatenBy(agent);
        }
        agent.setLocalStatus(nextStatus);
    }

    public ResponsibleStatus getNextStatus() {
        return nextStatus;
    }

    public void setNextStatus(ResponsibleStatus nextStatus) {
        this.nextStatus = nextStatus;
    }

    
}