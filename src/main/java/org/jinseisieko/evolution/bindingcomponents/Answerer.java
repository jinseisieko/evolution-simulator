// src/main/java/org/jinseisieko/evolution/bindingcomponents/Answerer.java
package org.jinseisieko.evolution.bindingcomponents;

/**
 * Evaluates a {@link Question} and provides a boolean answer based on its internal state.
 * <p>
 * An {@code Answerer} encapsulates the logic or data needed to respond to arbitrary questions.
 * It is stateful and typically tied to a specific entity or context (e.g., an agent in a simulation).
 * <p>
 * The same {@code Answerer} may be queried multiple times with different questions,
 * and its responses may vary depending on its current state.
 *
 * @author jinseisieko
 */
public interface Answerer {
    /**
     * Evaluates the given question and returns a boolean answer.
     * <p>
     * The result reflects the current state of the {@code Answerer} and the semantics
     * of the provided question.
     *
     * @param question the question to evaluate; must not be {@code null} <p>
     * @return the boolean answer to the question <p>
     * @throws NullPointerException if {@code question} is {@code null} <p>
     */
    boolean answer(Question question);
}