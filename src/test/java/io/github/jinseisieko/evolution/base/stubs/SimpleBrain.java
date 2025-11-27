// src/test/java/io.github/jinseisieko/evolution/base/stubs/SimpleBrain.java
package io.github.jinseisieko.evolution.base.stubs;

import io.github.jinseisieko.evolution.base.Brain;
import io.github.jinseisieko.evolution.bindingcomponents.Answerer;
import io.github.jinseisieko.evolution.bindingcomponents.Question;
import io.github.jinseisieko.evolution.bindingcomponents.Status;

public final class SimpleBrain implements Brain {
    private final Question q;

    public SimpleBrain()
    {
        this.q = new LessThanQuestion(10);
    }

    @Override
    public Status decide(Answerer context) {
        if (context.answer(q)) {
            return new FirstStatus();
        } else {
            return new SecondStatus();
        }
    }
} 