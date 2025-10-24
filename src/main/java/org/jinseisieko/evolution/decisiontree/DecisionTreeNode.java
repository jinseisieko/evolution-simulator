// src/main/java/org/decisiontree/DecisionTreeNode.java
package org.jinseisieko.evolution.decisiontree;

import org.jinseisieko.evolution.bindingcomponents.Answerer;

/**
 * Interface defining the core logic for traversing a decision tree node.
 * <p>
 * Implementing classes specify how a node processes an {@link Answerer}
 * and determines the next node to navigate to in the tree structure.
 * This interface abstracts the decision-making process inherent in each node,
 * allowing for flexible implementations of questions, outcomes, and branching logic.
 * <p>
 * The {@link #next(Answerer)} method is the central mechanism for tree traversal.
 * Based on the state or responses provided by the {@code answerer}, the node
 * implementing this interface decides its successor (e.g., a left child, a right child,
 * or potentially another arbitrary node).
 *
 * @author jinseisieko
 */
public interface DecisionTreeNode {

    /**
     * Determines the next node in the decision tree based on the provided {@link Answerer}.
     * <p>
     * This method encapsulates the logic of the node. For instance, a question node
     * would use the {@code answerer} to get an answer and then decide whether to return
     * its left or right child. An outcome node might simply return itself or a designated
     * end node.
     *
     * @param answerer The object providing answers or state information used for navigation. <p>
     * @return The next {@link Node} in the traversal. This can be a child node, another
     *         node in the tree, or {@code null} if no further navigation is possible
     *         or defined from this point. <p>
     *
     * @author jinseisieko
     */
    Node next(Answerer answerer);
}
