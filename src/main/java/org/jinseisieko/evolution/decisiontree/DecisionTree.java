// src/main/java/org/jinseisieko/evolution/decisiontree/DecisionTree.java
package org.jinseisieko.evolution.decisiontree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.jinseisieko.evolution.bindingcomponents.Answerer;
import org.jinseisieko.evolution.bindingcomponents.Status;

/**
 * Represents a binary decision tree structure.
 * <p>
 * This class manages the root node of the tree and provides the primary
 * method for executing the tree's logic against an {@link Answerer}.
 * Execution starts from the root and proceeds down the tree by evaluating
 * questions at each internal node until a leaf node ({@link OutcomeNode}) is reached.
 * The result of the execution is the {@link Status} object held by the final leaf node.
 * <p>
 * The tree structure is defined by its nodes ({@link Node}, {@link QuestionNode},
 * {@link OutcomeNode}, {@link RootQuestionNode}) and their interconnections.
 * This class does not directly manage the creation or modification of that structure,
 * but it provides the entry point for traversing it.
 * <p>
 * It also supports indexing of nodes for fast lookup by a specific numbering scheme:
 * nodes are numbered level by level, from left to right, starting with the root at 1.
 * For a node with number i, its left child is 2*i and its right child is 2*i + 1.
 *
 * @author jinseisieko
 */
public class DecisionTree {

    private final RootQuestionNode root;
    private Node[] indexedNodes; // Array for fast node lookup, indexed by node number (1-based)
    private boolean indexValid; // Flag to indicate if the indexedNodes array is up-to-date
    private int depth;

    /**
     * Constructs a decision tree with a specified depth.
     * <p>
     * This constructor builds a full binary tree where internal nodes are
     * {@link QuestionNode}s and leaf nodes are {@link OutcomeNode}s.
     * The depth determines the number of levels from the root to any leaf.
     * For example, a depth of 1 creates a tree with a root node and two outcome leaf nodes.
     * <p>
     * <strong>Warning:</strong> Creating trees with large depths can consume significant
     * memory and time due to the exponential growth in the number of nodes (2^(depth+1) - 1).
     * The indexing array will also consume memory proportional to the maximum possible
     * number of nodes for that depth.
     *
     * @param depth The depth of the tree. Must be greater than 0. <p>
     * @throws IllegalArgumentException if {@code depth} is less than or equal to 0. <p>
     *
     * @author jinseisieko
     */
    public DecisionTree(int depth) {
        if (depth <= 0) {
            throw new IllegalArgumentException("Depth cannot be less than or equal to zero. Got: " + depth);
        }
        this.root = generateFullBinaryTree(depth);
        this.indexValid = false; // Index needs to be built after construction
        this.depth = depth;
    }

    /**
     * Gets the root node of this decision tree.
     * <p>
     * The root node serves as the entry point for traversing the tree structure.
     *
     * @return The root {@link RootQuestionNode} of the tree. <p>
     *
     * @author jinseisieko
     */
    public RootQuestionNode getRoot() {
        return root;
    }

    /**
     * Checks if the internal node index is currently up-to-date with the tree structure.
     * <p>
     * The index is considered valid only after a successful call to {@link #rebuildIndex()}.
     * Accessing nodes by index using {@link #getNodeByIndex(int)} requires the index to be valid.
     *
     * @return {@code true} if the index is up-to-date, {@code false} otherwise. <p>
     *
     * @author jinseisieko
     */
    public boolean isIndexValid() {
        return this.indexValid;
    }

    /**
     * Rebuilds the internal index array for fast node lookup.
     * <p>
     * This method should be called after the tree structure has been modified
     * (e.g., through mutation operations) to ensure the index reflects the current state.
     * The indexing process performs a breadth-first search (BFS) to map node numbers
     * to their corresponding {@link Node} objects.
     * <p>
     * This implementation avoids creating an intermediate List view by directly
     * calculating the required size and copying elements into a new array.
     *
     * @author jinseisieko
     */
    public void rebuildIndex() {
        List<Node> nodeList = new ArrayList<>();
        nodeList.add(null); // Index 0 is unused, reserve it.

        Queue<Node> queue = new LinkedList<>();
        if (root != null) {
            queue.add(root);
        }

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            int currentIndex = nodeList.size(); // The index this node will get

            // Ensure the list is large enough
            while (nodeList.size() <= currentIndex) {
                nodeList.add(null);
            }

            nodeList.set(currentIndex, current);

            if (current != null) {
                queue.add(current.getLeftSon());
                queue.add(current.getRightSon());
            }
        }

        // Find the last non-null node index to determine the required array size
        int lastNonNullIndex = nodeList.size() - 1;
        while (lastNonNullIndex > 0 && nodeList.get(lastNonNullIndex) == null) {
            lastNonNullIndex--;
        }

        // Create the final array of the exact required size
        int requiredSize = lastNonNullIndex + 1;
        this.indexedNodes = new Node[requiredSize];

        // Copy elements from the list to the array
        for (int i = 0; i < requiredSize; i++) {
            this.indexedNodes[i] = nodeList.get(i);
        }

        this.indexValid = true;
    }


    /**
     * Gets the node associated with the given index number in the tree's numbering scheme.
     * <p>
     * The numbering scheme is level-by-level, left-to-right, starting with the root at 1.
     * For a node with number i, its left child is 2*i and its right child is 2*i + 1.
     * This method provides O(1) access time if the index is up-to-date.
     * If the index is not up-to-date, it throws an exception.
     *
     * @param nodeIndex The 1-based index of the node to retrieve. <p>
     * @return The {@link Node} at the specified index, or {@code null} if the index is valid
     *         but points to an unassigned location (e.g., in a pruned tree). <p>
     * @throws IllegalArgumentException if {@code nodeIndex} is less than 1. <p>
     * @throws IllegalStateException if the internal index is not up-to-date. Call {@link #rebuildIndex()} first. <p>
     *
     * @author jinseisieko
     */
    public Node getNodeByIndex(int nodeIndex) {
        if (!indexValid) {
             throw new IllegalStateException("Node index is not up-to-date. Call rebuildIndex() after modifying the tree structure.");
        }
        if (nodeIndex < 1) {
            throw new IllegalArgumentException("Node index must be greater than 0. Got: " + nodeIndex);
        }
        if (nodeIndex >= indexedNodes.length) {
            // Index is beyond the current size of the array.
            // This means the node does not exist in the tree.
            return null;
        }
        return indexedNodes[nodeIndex];
    }


    /**
     * Executes the decision tree logic based on answers provided by the given {@link Answerer}.
     * <p>
     * Traversal starts at the root node. At each {@link QuestionNode}, the tree asks the
     * {@code answerer} the node's question. Based on the boolean answer, it moves to the
     * left child (for {@code false}) or the right child (for {@code true}). This process
     * continues until an {@link OutcomeNode} is reached. The {@link Status} object stored
     * in the final leaf node is returned as the result of the execution.
     * <p>
     * The execution path is determined solely by the tree structure and the answers from
     * the {@code answerer}. This method does not modify the tree's structure.
     *
     * @param answerer The strategy for providing answers to the questions in the tree. Must not be {@code null}. <p>
     * @return The {@link Status} object from the leaf node reached during traversal. <p>
     * @throws IllegalArgumentException if {@code answerer} is {@code null}. <p>
     * @throws RuntimeException if the tree structure is invalid (e.g., contains a cycle or
     *                          a path that does not lead to an {@link OutcomeNode}). This is an unexpected state. <p>
     *
     * @author jinseisieko
     */
    public Status apply(Answerer answerer) {
        if (answerer == null) {
            throw new IllegalArgumentException("Answerer cannot be null");
        }
        Node currentNode = this.root;

        // Traverse the tree until an OutcomeNode is reached
        while (!(currentNode instanceof OutcomeNode)) {
            if (!(currentNode instanceof DecisionTreeNode)) {
                 // This should not happen if the tree is built correctly from Node subclasses
                 throw new RuntimeException("Tree structure error: Encountered a node that is not an OutcomeNode or a DecisionTreeNode.");
            }
            DecisionTreeNode decisionNode = (DecisionTreeNode) currentNode;
            // The 'next' method handles the logic for QuestionNode and OutcomeNode.
            // For OutcomeNode, it returns null, which will break the loop.
            Node nextNode = decisionNode.next(answerer);
            if (nextNode == null) {
                 // This can happen if a QuestionNode's 'next' method returns null unexpectedly
                 // or if the tree is malformed. An OutcomeNode's 'next' correctly returns null.
                 if (currentNode instanceof OutcomeNode finalOutcome) {
                    // This is the expected end condition if the last node processed was an OutcomeNode
                    // However, the loop condition should prevent this. Let's handle it gracefully.
                    // Cast to OutcomeNode to get the status.
                    return finalOutcome.getStatus();
                 } else {
                     throw new RuntimeException("Tree structure error: Traversal reached a dead end (next node is null) at a non-leaf node: " + currentNode);
                 }
            }
            currentNode = nextNode;
        }

        // At this point, currentNode is guaranteed to be an OutcomeNode due to the loop condition
        OutcomeNode finalOutcome = (OutcomeNode) currentNode;
        return finalOutcome.getStatus();
    }

    /**
     * Generates a full binary tree of a given depth with default uninitialized nodes.
     * <p>
     * This is a helper method used by the constructor {@link #DecisionTree(int)}.
     * It recursively creates the structure: internal nodes are {@link QuestionNode}s
     * (except the root which is a {@link RootQuestionNode}), and leaf nodes are
     * {@link OutcomeNode}s. The nodes are linked together, but questions and statuses
     * are not set by this method.
     *
     * @param depth The target depth of the tree. <p>
     * @return The root {@link RootQuestionNode} of the generated tree. <p>
     *
     * @author jinseisieko
     */
    private static RootQuestionNode generateFullBinaryTree(int depth) {
        RootQuestionNode root = new RootQuestionNode(); // Creates an uninitialized root
        root.setLeftSon(buildSubtree(depth, 1));
        root.setRightSon(buildSubtree(depth, 1));
        return root;
    }

    /**
     * Recursively builds a subtree for the tree generation process.
     *
     * @param targetDepth The overall target depth of the main tree. <p>
     * @param currentDepth The current depth of the node being created in the recursion. <p>
     * @param parent The parent node for the node being created. <p>
     * @return A {@link Node} which is either an internal {@link QuestionNode} or a leaf {@link OutcomeNode}. <p>
     *
     * @author jinseisieko
     */
    private static Node buildSubtree(int targetDepth, int currentDepth) {
        if (currentDepth >= targetDepth) {
            // Create a leaf node at the target depth
            OutcomeNode leaf = new OutcomeNode(); // Creates an uninitialized leaf
            // Setting parent link is handled by setLeftSon/setRightSon in the parent
            return leaf;
        } else {
            // Create an internal node and recurse for its children
            QuestionNode internal = new QuestionNode(); // Creates an uninitialized internal node
            internal.setLeftSon(buildSubtree(targetDepth, currentDepth + 1));
            internal.setRightSon(buildSubtree(targetDepth, currentDepth + 1));
            // Setting parent link is handled by setLeftSon/setRightSon in the parent
            return internal;
        }
    }

    /**
     * Depth of this decision tree. The depth is how many transitions from the root to the leaf need to be made.
     * 
     * @return Depth <p>
     *
     * @author jinseisieko
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Node number of this decision tree. The node number is the number of all verticles in this tree
     * 
     * @return Node number <p>
     * 
     * @author jinseisieko 
     */
    public int getNodeNumber() {
        return ((int) Math.pow(2, this.depth+1)) - 1;
    }
    
    /**
     * Status number of this decision tree. The status number is the number of all outcomes nodes in this tree
     * 
     * @return Status number <p>
     * 
     * @author jinseisieko 
     */
    public int getStatusNumber() {
        return (int) Math.pow(2, this.depth);
    }

    /**
     * Check is this tree initialized.
     * Check are all tree nodes initialized.
     * 
     * @return true if tree is initialized <p>
     * @throws IllegalStateException if tree is not indexed ({@link rebuildIndex()} should be called before this method) <p>
     * 
     * @author jinseisieko 
     */
    public boolean isInitialized() {
        boolean answer = true;
        if (!this.indexValid) {
            throw  new IllegalStateException("To check initialization tree should be indexed");
        }
        for (int i = 1; i < this.getNodeNumber() + 1; i++) {
            answer &= this.getNodeByIndex(i).isInitialized();
        }
        return answer;
    }
}
