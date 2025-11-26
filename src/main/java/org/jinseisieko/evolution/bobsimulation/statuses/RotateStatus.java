// src/main/java/org/jinseisieko/evolution/bobsimulation/statuses/RotateStatus.java
package org.jinseisieko.evolution.bobsimulation.statuses;

import org.jinseisieko.evolution.base.Agent;
import org.jinseisieko.evolution.base.ResponsibleStatus;

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