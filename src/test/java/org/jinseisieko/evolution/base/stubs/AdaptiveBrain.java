// src/test/java/org/jinseisieko/evolution/base/stubs/AdaptiveBrain.java
package org.jinseisieko.evolution.base.stubs;

import org.jinseisieko.evolution.bindingcomponents.Answerer;
import org.jinseisieko.evolution.bindingcomponents.Question;
import org.jinseisieko.evolution.bindingcomponents.Status;
import org.jinseisieko.evolution.decisiontree.stubs.EnergyQuestion;
import org.jinseisieko.evolution.decisiontree.stubs.MockStatus;
import org.jinseisieko.evolution.base.Brain;

public final class AdaptiveBrain implements Brain {
    private final Question energyQuestion;
    private final Question speedQuestion;

    public AdaptiveBrain() {
        this.energyQuestion = new EnergyQuestion(0.5);
        this.speedQuestion = new SpeedQuestion(5.0);
    }

    @Override
    public Status decide(Answerer context) {
        if (!context.answer(energyQuestion)) {
            return new MockStatus("LowEnergy");
        }
        if (context.answer(speedQuestion)) {
            return new AccelerateStatus();
        }
        return new MockStatus("Cruise");
    }
}