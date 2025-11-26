// src/test/java/org/jinseisieko/evolution/base/stubs/LessThanQuestion.java
package org.jinseisieko.evolution.base.stubs;

import org.jinseisieko.evolution.bindingcomponents.Question;

public final class LessThanQuestion extends Question {
    private final int number;

    public LessThanQuestion(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LessThanQuestion q)) return false;
        return Double.compare(q.number, number) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(number);
    }
        
}