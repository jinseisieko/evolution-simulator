// src/main/java/org/jinseisieko/evolution/decisiontree/Question.java
package org.jinseisieko.evolution.decisiontree;

/**
 * Represents a yes/no question used in a binary decision tree.
 * <p>
 * This interface is designed to be implemented by immutable, value-based classes
 * that encapsulate a specific condition (e.g., "Is energy above 0.5?").
 * The actual evaluation is delegated to an external {@link Answerer},
 * ensuring separation between tree structure and domain logic (e.g., agent state).
 * <p>
 * Implementations <strong>must</strong> override {@link #equals(Object)} and {@link #hashCode()}
 * consistently, as questions may be used as keys in caches, sets, or during mutation comparison.
 * It is strongly recommended that all implementations be immutable and thread-safe.
 *
 * @author jinseisieko
 */
public interface Question {

    /**
     * Compares this question with another for structural equality.
     * <p>
     * Two questions are equal if they are of the same type and all their
     * defining parameters (e.g., threshold values, radii) are equal.
     * <p>
     * This method must be consistent with {@link #hashCode()}.
     *
     * @param obj the object to compare with <p>
     * @return {@code true} if the objects are equal, {@code false} otherwise <p>
     */
    @Override
    boolean equals(Object obj);

    /**
     * Returns a hash code value for this question.
     * <p>
     * This hash code must be consistent with {@link #equals(Object)} and should
     * be based on all fields that define the question's semantics.
     * <p>
     * A stable hash code enables efficient caching and comparison during
     * evolutionary operations (e.g., detecting duplicate questions).
     *
     * @return a hash code value <p>
     */
    @Override
    int hashCode();
}