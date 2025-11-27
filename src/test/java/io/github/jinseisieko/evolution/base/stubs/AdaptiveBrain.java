// src/test/java/io.github/jinseisieko/evolution/base/stubs/AdaptiveBrain.java
package io.github.jinseisieko.evolution.base.stubs;

import io.github.jinseisieko.evolution.bindingcomponents.Answerer;
import io.github.jinseisieko.evolution.bindingcomponents.Question;
import io.github.jinseisieko.evolution.bindingcomponents.Status;
import io.github.jinseisieko.evolution.decisiontree.stubs.EnergyQuestion;
import io.github.jinseisieko.evolution.decisiontree.stubs.MockStatus;
import io.github.jinseisieko.evolution.base.Brain;

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