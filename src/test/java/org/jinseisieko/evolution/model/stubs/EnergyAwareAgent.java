// src/test/java/org/jinseisieko/evolution/model/stubs/EnergyAwareAgent.java
package org.jinseisieko.evolution.model.stubs;

import org.jinseisieko.evolution.basic.Point;
import org.jinseisieko.evolution.bindingcomponents.Question;
import org.jinseisieko.evolution.decisiontree.stubs.EnergyQuestion;
import org.jinseisieko.evolution.decisiontree.stubs.MockStatus;
import org.jinseisieko.evolution.model.Agent;

public class EnergyAwareAgent extends Agent {
    public EnergyAwareAgent(Point initialCoordinates, double size) {
        super(
            initialCoordinates,
            size,
            2.0, // brainUpdateTime
            new AdaptiveBrain(),
            0.02, // BRAIN_ENERGY_COST
            0.01, // SPEED_ENERGY_COST
            0.005 // ANGULAR_SPEED_ENERGY_COST
        );
    }

    @Override
    public boolean answer(Question question) {
        if (question instanceof EnergyQuestion eq) {
            return this.getEnergy() >= eq.getThreshold();
        }
        if (question instanceof SpeedQuestion sq) {
            return this.getSpeed() >= sq.getThreshold();
        }
        if (question == null) {
            throw new IllegalArgumentException("Question cannot be null");
        }
        throw new IllegalArgumentException("Unknown question type: " + question.getClass());
    }

    @Override
    public void statusActivity() {
        if (this.getLocalStatus() instanceof AccelerateStatus) {
            this.setAcceleration(2.0);
        } else if (this.getLocalStatus() instanceof MockStatus) {
            String desc = this.getLocalStatus().toString();
            if (desc.contains("LowEnergy")) {
                this.setSpeed(0.0);
                this.setAcceleration(0.0);
            } else if (desc.contains("Cruise")) {
                this.setAcceleration(0.5);
            }
        }
    }
}