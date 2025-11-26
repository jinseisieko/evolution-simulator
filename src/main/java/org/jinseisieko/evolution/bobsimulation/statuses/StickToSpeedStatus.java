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
        if (dt <= 0) {
            return;
        }

        double current = agent.getSpeed();
        double desiredAcceleration = (speed - current) / dt;

        double actualAcceleration;
        if (desiredAcceleration > maxAcceleration) {
            actualAcceleration = maxAcceleration;
        } else if (desiredAcceleration < -maxAcceleration) {
            actualAcceleration = -maxAcceleration;
        } else {
            actualAcceleration = desiredAcceleration;
        }

        agent.setAcceleration(actualAcceleration);
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