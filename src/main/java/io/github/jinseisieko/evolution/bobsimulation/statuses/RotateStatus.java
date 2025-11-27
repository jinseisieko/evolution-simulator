// src/main/java/io.github/jinseisieko/evolution/bobsimulation/statuses/RotateStatus.java
package io.github.jinseisieko.evolution.bobsimulation.statuses;

import io.github.jinseisieko.evolution.base.Agent;
import io.github.jinseisieko.evolution.base.ResponsibleStatus;

public class RotateStatus implements ResponsibleStatus {
    private double angularSpeed; 

    public RotateStatus(double angularSpeed) {
        this.angularSpeed = angularSpeed;
    }

    @Override
    public void applyThisStatus(Agent agent, double dt) {
        agent.setAngularSpeedRad(angularSpeed);
    }

    public double getAngularSpeed() {
        return angularSpeed;
    }

    public void setAngularSpeed(double angularSpeed) {
        this.angularSpeed = angularSpeed;
    }
    
}