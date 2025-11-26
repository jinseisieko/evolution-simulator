// src/main/java/org/jinseisieko/evolution/decisiontree/OutcomeNode.java
package org.jinseisieko.evolution.decisiontree;

import org.jinseisieko.evolution.bindingcomponents.Answerer;
import org.jinseisieko.evolution.bindingcomponents.Status;

/**
 * Represents a leaf node in a binary decision tree that holds a final {@link Status}.
 * <p>
 * An {@code OutcomeNode} signifies the end of a decision path within the tree.
 * It does not have children and provides a fixed {@link Status} result when
 * the tree traversal reaches this point. The {@link #next(Answerer)} method
 * always returns {@code null}, indicating that the traversal terminates here.
 * <p>
 * This node implements the {@link DecisionTreeNode} interface to define its
 * specific behavior during tree execution.
 *
 * @author jinseisieko
 */
public class OutcomeNode extends Node implements DecisionTreeNode {

    private Status status;

    /**
     * Constructs an {@code OutcomeNode} with its {@code status} initially set to {@code null}.
     * <p>
     * Note: A node with a {@code null} status is not considered fully initialized
     * according to the {@link #isInitialized()} method. The status must be set
     * using {@link #setStatus(Status)} before the node is used in a complete tree.
     *
     * @author jinseisieko
     */
    public OutcomeNode() {
        super(); // Calls the parent Node constructor, initializing father, leftSon, rightSon to null
        this.status = null;
    }

    /**
     * Constructs an {@code OutcomeNode} with the specified {@link Status}.
     * <p>
     * The provided status must not be {@code null}.
     *
     * @param status The final status result this node represents. Cannot be {@code null}. <p>
     * @throws IllegalArgumentException if {@code status} is {@code null}. <p>
     *
     * @author jinseisieko
     */
    public OutcomeNode(Status status) {
        super(); // Calls the parent Node constructor
        setStatus(status); // Validates and sets the status
    }

    /**
     * Gets the {@link Status} associated with this outcome node.
     * <p>
     * The returned status may be {@code null} if it has not been set yet
     * (e.g., after using the default constructor).
     *
     * @return The status of this node, or {@code null} if not set. <p>
     *
     * @author jinseisieko
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the {@link Status} for this outcome node.
     * <p>
     * This method enforces that the status cannot be {@code null}.
     *
     * @param status The new status for this node. Cannot be {@code null}. <p>
     * @throws IllegalArgumentException if {@code status} is {@code null}. <p>
     *
     * @author jinseisieko
     */
    public final void setStatus(Status status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = status;
    }

    /**
     * Defines the traversal logic for this node.
     * <p>
     * An {@code OutcomeNode} always returns {@code null}, indicating that
     * the decision tree traversal ends at this node. The result of the traversal
     * up to this point is represented by the {@link #getStatus()}.
     *
     * @param answerer The {@link Answerer} object used for traversal (not used by this node). <p>
     * @return Always returns {@code null}, signifying the end of the path. <p>
     *
     * @author jinseisieko
     */
    @Override
    public Node next(Answerer answerer) {
        // An OutcomeNode terminates the traversal.
        return null;
    }

    /**
     * Checks if this node is fully initialized.
     * <p>
     * For an {@code OutcomeNode}, being initialized means it has a non-null
     * parent ({@code father}) and a non-null {@link Status}. Unlike internal nodes,
     * it does not require children.
     *
     * @return {@code true} if {@code father} and {@code status} are both non-{@code null};
     *         {@code false} otherwise. <p>
     *
     * @author jinseisieko
     */
    @Override
    public boolean isInitialized() {
        return
            this.getFather() != null &&
            this.getStatus() != null;
    }
}