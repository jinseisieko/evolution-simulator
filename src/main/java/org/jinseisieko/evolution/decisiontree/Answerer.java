// src/main/java/org/jinseisieko/evolution/decisiontree/Answerer.java
package org.jinseisieko.evolution.decisiontree;

/**
 * Defines a strategy for answering questions posed by nodes in a decision tree.
 * <p>
 * This interface decouples the decision tree logic from the source of answers,
 * allowing the same tree to be evaluated in different contexts—such as agent-based
 * simulation, testing environments, or static rule sets. Implementations may
 * encapsulate dynamic state (e.g., an agent’s sensory input) or provide fixed
 * responses (e.g., for debugging or evolutionary strategies).
 * <p>
 * The contract assumes binary decisions: each question must be answered with
 * {@code true} or {@code false}. For non-binary logic, a different interface
 * or tree structure would be required.
 *
 * @author jinseisieko
 */
public interface Answerer {

    /**
     * Provides a boolean response to the given question.
     * <p>
     * The interpretation of the question and the logic used to produce the answer
     * are implementation-specific. Common use cases include querying an agent's
     * internal state, evaluating environmental conditions, or returning predefined
     * values for testing.
     *
     * @param question the question to be answered; must not be {@code null} <p>
     * @return {@code true} if the condition represented by the question holds,
     *         {@code false} otherwise <p>
     * @throws NullPointerException if {@code question} is {@code null} <p>
     * @throws UnsupportedOperationException if the implementation cannot handle
     *         the type of the provided question <p>
     *
     * @author jinseisieko
     */
    boolean answer(Question question);
}