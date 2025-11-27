// src/main/java/io.github/jinseisieko/evolution/base/ResponsibleQuestion.java
package io.github.jinseisieko.evolution.base;

import io.github.jinseisieko.evolution.bindingcomponents.Question;

public abstract class ResponsibleQuestion extends Question {
    public abstract boolean apply(Agent agent);
}