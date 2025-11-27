// src/test/java/io.github/jinseisieko/evolution/base/stubs/FirstStatus.java
package io.github.jinseisieko.evolution.base.stubs;

import io.github.jinseisieko.evolution.basic.Point;
import io.github.jinseisieko.evolution.bindingcomponents.Question;
import io.github.jinseisieko.evolution.decisiontree.stubs.EnergyQuestion;
import io.github.jinseisieko.evolution.decisiontree.stubs.MockStatus;
import io.github.jinseisieko.evolution.base.Agent;

public class EnergyAwareAgent extends Agent {
    public EnergyAwareAgent(Point initialCoordinates, double size) {
        super(
            initialCoordinates,
            size,
            2.0, // brainUpdateTime
            new AdaptiveBrain(),
            null,
            0.02, // BRAIN_ENERGY_COST
            0.01, // SPEED_ENERGY_COST
            0.005, // ANGULAR_SPEED_ENERGY_COST
            0.01 // EAT_FOOD_ENERGY_COST
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
    public void statusActivity(double dt) {
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