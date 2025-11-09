// src/main/java/org/jinseisieko/evolution/base/ResponsibleQuestion.java
package org.jinseisieko.evolution.base;

import org.jinseisieko.evolution.bindingcomponents.Question;

public abstract class ResponsibleQuestion extends Question {
    public abstract boolean apply(Agent agent);
}