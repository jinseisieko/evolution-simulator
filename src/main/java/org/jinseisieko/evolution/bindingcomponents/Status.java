// src/main/java/org/jinseisieko/evolution/bindingcomponents/Status.java
package org.jinseisieko.evolution.bindingcomponents;

/**
 * Represents a terminal outcome or result of a decision process.
 * <p>
 * A {@code Status} is a marker interface used to denote the final state or action
 * produced after evaluating a sequence of conditions. It carries no behavior itself,
 * but concrete implementations may encode specific instructions, states, or events.
 * <p>
 * This interface enables flexible and type-safe handling of different outcomes
 * without coupling the evaluation logic to particular result types.
 *
 * @author jinseisieko
 */
public interface Status {
    // Marker interface for distinct outcome types.
}