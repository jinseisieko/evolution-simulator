// src/main/java/org/jinseisieko/evolution/base/ResponsibleQuestion.java
package org.jinseisieko.evolution.base;

import org.jinseisieko.evolution.bindingcomponents.Question;
import org.jinseisieko.evolution.bindingcomponents.Status;

public abstract class ResponsibleQuestion extends Question {
    public abstract Status apply(Agent agent);
}