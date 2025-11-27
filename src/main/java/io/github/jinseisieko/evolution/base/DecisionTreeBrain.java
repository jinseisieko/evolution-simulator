// src/main/java/io.github/jinseisieko/evolution/base/DecisionTreeBrain.java
package io.github.jinseisieko.evolution.base;

import io.github.jinseisieko.evolution.bindingcomponents.Answerer;
import io.github.jinseisieko.evolution.bindingcomponents.Question;
import io.github.jinseisieko.evolution.bindingcomponents.Status;
import io.github.jinseisieko.evolution.decisiontree.DecisionTree;
import io.github.jinseisieko.evolution.decisiontree.Node;
import io.github.jinseisieko.evolution.decisiontree.OutcomeNode;
import io.github.jinseisieko.evolution.decisiontree.QuestionNode;
import io.github.jinseisieko.evolution.decisiontree.RootQuestionNode;

/**
 * Represents a decision-making brain implemented as a binary decision tree.
 * <p>
 * This class extends {@link DecisionTree} and implements the {@link Brain} interface,
 * providing a concrete mechanism for making decisions based on an {@link Answerer} context.
 * It supports random initialization and genetic crossover operations, making it suitable
 * for use in evolutionary algorithms.
 *
 * @author jinseisieko
 */
public class DecisionTreeBrain extends DecisionTree implements Brain {

    /**
     * Constructs a new decision tree brain with the specified depth.
     * <p>
     * The tree is initialized as a full binary tree with uninitialized {@link QuestionNode}s
     * and {@link OutcomeNode}s. Questions and statuses must be assigned separately
     * before the brain can be used for decision making.
     *
     * @param depth The depth of the decision tree; must be greater than 0. <p>
     * @throws IllegalArgumentException if {@code depth <= 0}. <p>
     *
     * @author jinseisieko
     */
    public DecisionTreeBrain(int depth) {
        super(depth);
    }

    /**
     * Constructs a decision tree brain with the specified depth and a pre-built root node.
     * <p>
     * The provided root must form a fully initialized tree of the given depth.
     *
     * @param depth The depth of the tree; must be greater than 0. <p>
     * @param root  The root node of the tree; must not be {@code null} and must be fully initialized. <p>
     * @throws IllegalArgumentException if {@code depth <= 0}, {@code root} is {@code null},
     *                                  or the tree is not fully initialized. <p>
     *
     * @author jinseisieko
     */
    public DecisionTreeBrain(int depth, RootQuestionNode root) {
        super(depth, root);
    }

    /**
     * Executes the decision tree using the provided {@link Answerer} context and returns the resulting {@link Status}.
     * <p>
     * This method delegates to the inherited {@link DecisionTree#apply(Answerer)} method,
     * which traverses the tree from the root to a leaf {@link OutcomeNode} based on answers
     * provided by the context.
     *
     * @param context the object that answers questions posed during tree traversal; must not be {@code null}. <p>
     * @return the {@link Status} associated with the leaf node reached during traversal. <p>
     * @throws IllegalArgumentException if {@code context} is {@code null}. <p>
     * @throws RuntimeException if the tree structure is malformed (e.g., missing children or questions). <p>
     * 
     * @author jinseisieko
     */
    @Override
    public Status decide(Answerer context) {
        return this.apply(context);
    }

    /**
     * Creates a new randomly initialized decision tree brain of the specified depth.
     * <p>
     * Internal {@link QuestionNode}s are assigned random questions from the provided array,
     * and leaf {@link OutcomeNode}s are assigned random statuses from the provided array.
     * The tree is assumed to be a complete binary tree of the given depth.
     *
     * @param depth     the depth of the tree (must be ≥ 1). <p>
     * @param questions an array of available questions to assign to internal nodes; must not be empty or {@code null}. <p>
     * @param statuses  an array of available statuses to assign to leaf nodes; must not be empty or {@code null}. <p>
     * @return a new, fully initialized {@link DecisionTreeBrain} with random content. <p>
     * @throws IllegalArgumentException if {@code depth <= 0}, or if either array is {@code null} or empty. <p>
     * @throws IllegalStateException    if the internal node indexing fails or the tree structure is inconsistent. <p>
     * 
     * @author jinseisieko
     */
    public static DecisionTreeBrain createRandom(int depth, Question[] questions, Status[] statuses) {
        if (depth <= 0) {
            throw new IllegalArgumentException("Depth must be greater than 0");
        }
        if (questions == null || questions.length == 0) {
            throw new IllegalArgumentException("Questions array must not be null or empty");
        }
        if (statuses == null || statuses.length == 0) {
            throw new IllegalArgumentException("Statuses array must not be null or empty");
        }

        DecisionTreeBrain brain = new DecisionTreeBrain(depth);
        java.util.Random random = new java.util.Random();
        brain.rebuildIndex();

        int nodeNumber = brain.getNodeNumber();
        int statusNumber = brain.getStatusNumber();

        for (int i = 1; i <= nodeNumber; i++) {
            Node node = brain.getNodeByIndex(i);
            if (i > nodeNumber - statusNumber) {
                // This is a leaf (outcome) node
                if (node instanceof OutcomeNode oNode) {
                    oNode.setStatus(statuses[random.nextInt(statuses.length)]);
                } else {
                    throw new IllegalStateException("Expected OutcomeNode at index " + i);
                }
            } else {
                // This is an internal (question) node
                if (node instanceof QuestionNode qNode) {
                    qNode.setQuestion(questions[random.nextInt(questions.length)]);
                } else {
                    throw new IllegalStateException("Expected QuestionNode at index " + i);
                }
            }
        }
        return brain;
    }

    /**
     * Performs crossover between two decision tree brains to produce a new hybrid brain.
     * <p>
     * The method selects a random depth level (between 1 and {@code depth}) and
     * traverses both parent trees to a randomly chosen node at that level.
     * Then, it copies one randomly selected subtree (left or right child) from the second tree
     * and grafts it onto a randomly selected branch (left or right) of the corresponding node
     * in a deep copy of the first tree.
     * <p>
     * Both input trees must have the same depth; otherwise, an exception is thrown.
     *
     * @param tree1 The first parent decision tree brain (provides the base structure).
     * @param tree2 The second parent decision tree brain (provides the genetic material for crossover).
     * @return A new {@link DecisionTreeBrain} instance resulting from the crossover operation.
     * @throws IllegalArgumentException if either input tree is {@code null} or if their depths differ.
     * @throws IllegalStateException    if node indexing or copying fails during the operation.
     * 
     * @author jinseisieko
     */
    public static DecisionTreeBrain cross(DecisionTreeBrain tree1, DecisionTreeBrain tree2) {
        if (tree1 == null || tree2 == null) {
            throw new IllegalArgumentException("Input trees must not be null");
        }
        if (tree1.getDepth() != tree2.getDepth()) {
            throw new IllegalArgumentException("Trees must have the same depth for crossover");
        }

        int depth = tree1.getDepth();
        java.util.Random random = new java.util.Random();

        // Create a deep copy of tree1 as the base for the offspring
        DecisionTreeBrain brain = new DecisionTreeBrain(depth, tree1.getRoot());
        brain.rebuildIndex();

        // Choose a random level (1 = root, depth = leaves)
        int level = Math.max(1, random.nextInt(depth + 1));

        Node currentBrain = brain.getRoot();
        Node currentTree2 = tree2.getRoot();

        // Traverse to the chosen level in both trees
        for (int i = 1; i < level; i++) {
            boolean goLeft = random.nextBoolean();
            currentBrain = goLeft ? currentBrain.getLeftSon() : currentBrain.getRightSon();
            goLeft = random.nextBoolean();
            currentTree2 = goLeft ? currentTree2.getLeftSon() : currentTree2.getRightSon();

            // Safety check: ensure nodes exist (should not happen in full trees)
            if (currentBrain == null || currentTree2 == null) {
                throw new IllegalStateException("Tree structure is incomplete at level " + i);
            }
        }

        // Select a random child from tree2
        Node donorSubtree = random.nextBoolean()
            ? currentTree2.getLeftSon().copy()
            : currentTree2.getRightSon().copy();

        // Graft it onto a random branch of the corresponding node in the offspring
        if (random.nextBoolean()) {
            currentBrain.setLeftSon(donorSubtree);
        } else {
            currentBrain.setRightSon(donorSubtree);
        }

        brain.rebuildIndex();
        return brain;
    }
}