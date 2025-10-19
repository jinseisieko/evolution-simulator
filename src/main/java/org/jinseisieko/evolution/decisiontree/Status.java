// src/main/java/org/decisiontree/Status.java
package org.jinseisieko.evolution.decisiontree;

/**
 * Marker interface representing the outcome or result of executing a decision tree.
 * <p>
 * Implementing classes define the specific status or result returned
 * after an entity (e.g., an Entity in the simulation) traverses a decision tree.
 * This could represent a successful completion with a specific action, a failure,
 * or an intermediate status requiring further processing.
 * <p>
 * The primary purpose of this marker interface is to provide a standardized type
 * for the decision tree execution logic (e.g., {@link DecisionTree#apply(Answerer)})
 * to return information about the traversal result. The specific nature of the
 * status is defined by the implementing class, allowing for flexible and diverse
 * outcomes based on the tree's structure and the simulation's needs.
 *
 * @author jinseisieko
 */
public interface Status {
    // This interface serves as a marker for different status types.
    // Specific implementations carry the actual result information.
}