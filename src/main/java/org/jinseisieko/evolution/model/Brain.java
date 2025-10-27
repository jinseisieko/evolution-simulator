// src/main/java/org/jinseisieko/evolution/model/Brain.java
package org.jinseisieko.evolution.model;

import org.jinseisieko.evolution.bindingcomponents.Answerer;
import org.jinseisieko.evolution.bindingcomponents.Status;

/**
 * Represents a decision-making component (a "brain") that produces a {@link Status}
 * based on contextual input.
 * <p>
 * Implementations of this interface encapsulate a strategy for evaluating a given
 * {@link Answerer} context—typically representing an agent's perception of its environment—
 * and returning a resulting {@link Status} that determines the agent's behavior or state.
 * <p>
 * This interface is designed to support interchangeable decision mechanisms,
 * such as rule-based systems, neural networks, or decision trees, within evolutionary simulations.
 *
 * @author jinseisieko
 */
public interface Brain {
    /**
     * Makes a decision based on the provided context.
     * <p>
     * The method evaluates the current state of the environment (or agent) through the
     * {@code context} and returns a {@link Status} that represents the outcome of this evaluation.
     *
     * @param context an object that provides answers to queries about the current state;
     *                must not be {@code null} <p>
     * @return the resulting {@link Status} of the decision process; never {@code null} <p>
     */
    Status decide(Answerer context);
}