// src/main/java/org/jinseisieko/evolution/bobsimulation/statuses/StickToSpeedStatus.java
package org.jinseisieko.evolution.bobsimulation.statuses;

import org.jinseisieko.evolution.base.Agent;
import org.jinseisieko.evolution.base.ResponsibleStatus;

public class StickToSpeedStatus implements ResponsibleStatus {
    private final double speed;
    private final double maxAcceleration;

    public StickToSpeedStatus(double speed, double maxAcceleration) {
        this.speed = speed;
        this.maxAcceleration = maxAcceleration;
    }

    @Override
    public void applyThisStatus(Agent agent, double dt) {
        double current = agent.getSpeed();
        if (current < speed) {
            agent.setAcceleration(maxAcceleration * (speed - current));
        }
    }

}