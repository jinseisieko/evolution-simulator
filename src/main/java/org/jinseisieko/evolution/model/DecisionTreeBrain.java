// src/main/java/org/jinseisieko/evolution/model/DecisionTreeBrain.java
package org.jinseisieko.evolution.model;

import org.jinseisieko.evolution.bindingcomponents.Answerer;
import org.jinseisieko.evolution.bindingcomponents.Question;
import org.jinseisieko.evolution.bindingcomponents.Status;
import org.jinseisieko.evolution.decisiontree.DecisionTree;
import org.jinseisieko.evolution.decisiontree.Node;
import org.jinseisieko.evolution.decisiontree.OutcomeNode;
import org.jinseisieko.evolution.decisiontree.QuestionNode;
import org.jinseisieko.evolution.decisiontree.RootQuestionNode;

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
     * @param depth The depth of the decision tree; must be greater than 0 <p>
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
     * @param depth The depth of the tree; must be greater than 0 <p>
     * @param root  The root node of the tree; must not be {@code null} and must be fully initialized <p>
     * @throws IllegalArgumentException if {@code depth <= 0}, {@code root} is {@code null},
     *                                  or the tree is not fully initialized <p>
     *
     * @author jinseisieko
     */
    public DecisionTreeBrain(int depth, RootQuestionNode root) {
        super(depth, root);
    }

    @Override
    public Status decide(Answerer context) {
        return this.apply(context);
    }

    public static DecisionTreeBrain createRandom(int depth, Question[] questions, Status[] statuses) {
        DecisionTreeBrain brain = new DecisionTreeBrain(depth);
        java.util.Random random = new java.util.Random();
        brain.rebuildIndex();
        int nodeNumber = brain.getNodeNumber();
        int statusNumber =(int) Math.pow(2, depth);
        for (int i = 1; i < nodeNumber+1; i++) {
            Node node = brain.getNodeByIndex(i);
            if (i > nodeNumber - statusNumber) {
                if (node instanceof OutcomeNode oNode) {
                    int randomValue = random.nextInt(statuses.length);
                    oNode.setStatus(statuses[randomValue]);
                } else {
                    assert true; 
                }
            } else {
                if (node instanceof QuestionNode oNode) {
                    int randomValue = random.nextInt(questions.length);
                    oNode.setQuestion(questions[randomValue]);
                } else {
                    assert true; 
                }
            }
        }
        return brain;
    }

    /**
     * Performs crossover between two decision tree brains to produce a new hybrid brain.
     * <p>
     * The method selects a random depth level (between 0 and {@code depth}) and
     * traverses both parent trees to a randomly chosen node at that level.
     * Then, it copies one randomly selected subtree (left or right child) from the second tree
     * and grafts it onto a randomly selected branch (left or right) of the corresponding node
     * in a copy of the first tree.
     * <p>
     * 
     * Both input trees must have the same depth; otherwise, the behavior is undefined.
     *
     * @param tree1 The first parent decision tree brain (provides the base structure) <p>
     * @param tree2 The second parent decision tree brain (provides the genetic material for crossover) <p>
     * @return A new {@link DecisionTreeBrain} instance resulting from the crossover operation <p>
     * @throws IllegalArgumentException if either input tree is {@code null} or if their depths differ <p>
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
        DecisionTreeBrain brain = new DecisionTreeBrain(depth, tree1.getRoot());
        brain.rebuildIndex();
        int randomValue = Math.max(1, random.nextInt(depth + 1));
        Node currentBrain = brain.getRoot();
        Node currentTree2 = tree2.getRoot();
        for (int i = 0; i < randomValue - 1; i++) {
            if (random.nextBoolean()) {
                currentBrain = currentBrain.getLeftSon();
            } else {
                currentBrain = currentBrain.getRightSon();
            }
            if (random.nextBoolean()) {
                currentTree2 = currentTree2.getLeftSon();
            } else {
                currentTree2 = currentTree2.getRightSon();
            }
        }
        if (random.nextBoolean()) {
            if (random.nextBoolean()) {
                currentBrain.setLeftSon(currentTree2.getLeftSon().copy());
            } else {
                currentBrain.setLeftSon(currentTree2.getRightSon().copy());
            }
        } else {
            if (random.nextBoolean()) {
                currentBrain.setRightSon(currentTree2.getLeftSon().copy());
            } else {
                currentBrain.setRightSon(currentTree2.getRightSon().copy());
            }
        }
        brain.rebuildIndex();
        return brain;
    }
}
