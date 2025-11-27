package io.github.jinseisieko.evolution.decisiontree;

import io.github.jinseisieko.evolution.bindingcomponents.Question;

/**
 * Represents the root node of a binary decision tree.
 * <p>
 * This node functions like a standard {@link QuestionNode}, posing a yes/no question
 * to determine the next traversal step. However, its initialization requirements differ
 * slightly: it does not require a parent (father) node to be considered initialized,
 * as it is the topmost node in the tree structure.
 *
 * @author jinseisieko
 */
public class RootQuestionNode extends QuestionNode implements DecisionTreeNode {

    /**
     * Constructs an empty root question node with no children and no question.
     * <p>
     * All internal references ({@code leftSon}, {@code rightSon}) and the question
     * are initialized to {@code null}. The {@code father} reference is also {@code null}
     * by default, which is consistent with being a root node.
     *
     * @author jinseisieko
     */
    public RootQuestionNode() {
        super(); // Initializes leftSon, rightSon, and question to null
    }

    /**
     * Constructs a root question node with the specified children but no initial question.
     * <p>
     * The question is initially {@code null} and must be assigned later via
     * {@link #setQuestion(Question)} before the node can be used for decision traversal.
     * Children are automatically linked to this node as their father.
     *
     * @param leftSon  the child for a {@code false} answer; may be {@code null} during construction <p>
     * @param rightSon the child for a {@code true} answer; may be {@code null} during construction <p>
     *
     * @author jinseisieko
     */
    public RootQuestionNode(Node leftSon, Node rightSon) {
        super(leftSon, rightSon); // Initializes children, question to null
    }

    /**
     * Constructs a fully initialized root question node with a question and children.
     * <p>
     * Equivalent to calling the no-question constructor followed by {@code setQuestion}.
     *
     * @param question  the initial question; must not be {@code null} <p>
     * @param leftSon   the left child <p>
     * @param rightSon  the right child <p>
     * @throws IllegalArgumentException if {@code question} is {@code null} <p>
     *
     * @author jinseisieko
     */
    public RootQuestionNode(Question question, Node leftSon, Node rightSon) {
        super(question, leftSon, rightSon); // Calls the parent constructor which sets question and children
    }

    /**
     * Checks whether this root node is fully initialized and ready for traversal.
     * <p>
     * A {@code RootQuestionNode} is considered initialized if it has non-{@code null}
     * left and right children and a non-{@code null} question. Unlike standard
     * {@link QuestionNode}s, it does not require a parent ({@code father}) node
     * to be initialized, as it is the root of the tree.
     *
     * @return {@code true} if {@code leftSon}, {@code rightSon}, and {@code question}
     *         are all non-{@code null}; {@code false} otherwise. <p>
     *
     * @author jinseisieko
     */
    @Override
    public boolean isInitialized() {
        return
            this.getLeftSon() != null &&
            this.getRightSon() != null &&
            this.getQuestion() != null;
    }
}
