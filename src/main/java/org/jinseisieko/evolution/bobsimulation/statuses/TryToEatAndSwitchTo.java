// src/main/java/org/jinseisieko/evolution/bobsimulation/statuses/TryToEatAndSwitchTo.java

package org.jinseisieko.evolution.bobsimulation.statuses;

import java.util.List;

import org.jinseisieko.evolution.base.Entity;
import org.jinseisieko.evolution.basic.Point;
import org.jinseisieko.evolution.base.Agent;
import org.jinseisieko.evolution.base.Food;
import org.jinseisieko.evolution.base.ResponsibleStatus;
import org.jinseisieko.evolution.bobsimulation.BobSimulation;

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
        for (Food food : foods) {
            double distance = Point.distanceBetween(agent.getPosition(), food.getPosition());
            if (distance < agent.getRadius() + food.getRadius() + 0.01) {
                food.beEatenBy(agent);
            }
            agent.setLocalStatus(nextStatus);
        }
    }

    public ResponsibleStatus getNextStatus() {
        return nextStatus;
    }

    public void setNextStatus(ResponsibleStatus nextStatus) {
        this.nextStatus = nextStatus;
    }

    
}