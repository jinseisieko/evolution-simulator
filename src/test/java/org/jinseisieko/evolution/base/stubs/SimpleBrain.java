// src/test/java/org/jinseisieko/evolution/base/stubs/SimpleBrain.java
package org.jinseisieko.evolution.base.stubs;

import org.jinseisieko.evolution.bindingcomponents.Answerer;
import org.jinseisieko.evolution.bindingcomponents.Question;
import org.jinseisieko.evolution.bindingcomponents.Status;
import org.jinseisieko.evolution.base.Brain;

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