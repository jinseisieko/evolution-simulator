// src/main/java/org/jinseisieko/evolution/bobsimulation/statuses/StickToSpeedStatus.java
package org.jinseisieko.evolution.bobsimulation.statuses;

import org.jinseisieko.evolution.base.Agent;
import org.jinseisieko.evolution.base.ResponsibleStatus;

public class StickToSpeedStatus implements ResponsibleStatus {
    private double speed;
    private double maxAcceleration;

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

    public double getMaxAcceleration() {
        return maxAcceleration;
    }

    public void setMaxAcceleration(double maxAcceleration) {
        this.maxAcceleration = maxAcceleration;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

}