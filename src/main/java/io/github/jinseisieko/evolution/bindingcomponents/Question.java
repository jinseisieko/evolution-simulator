// src/main/java/io.github/jinseisieko/evolution/bindingcomponents/Question.java
package io.github.jinseisieko.evolution.bindingcomponents;

/**
 * Represents a yes/no query that can be evaluated in a given context.
 * <p>
 * A {@code Question} is an immutable condition that may be used by external components
 * to obtain a boolean response. It does not perform evaluation itself; instead,
 * it is passed to an {@link Answerer} that determines the result based on its internal state.
 * <p>
 * Implementations must correctly override {@link Object#equals(Object)} and {@link Object#hashCode()}
 * to support comparison and use in collections.
 *
 * @author jinseisieko
 */
public abstract class Question {
    /**
     * Compares this question with another object for equality.
     * <p>
     * Two questions are considered equal if they represent the same logical condition.
     *
     * @param obj the object to compare with <p>
     * @return {@code true} if the objects are equal, {@code false} otherwise <p>
     */
    @Override
    public abstract boolean equals(Object obj);

    /**
     * Returns a hash code value consistent with {@link #equals(Object)}.
     *
     * @return a hash code value <p>
     */
    @Override
    public abstract int hashCode();
}