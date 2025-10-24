// src/main/java/org/jinseisieko/evolution/decisiontree/QuestionNode.java
package org.jinseisieko.evolution.decisiontree;

import org.jinseisieko.evolution.bindingcomponents.Answerer;
import org.jinseisieko.evolution.bindingcomponents.Question;

/**
 * Represents an internal (non-leaf) node in a binary decision tree that poses a yes/no question.
 * <p>
 * This node may be constructed without an initial question to support evolutionary operations
 * such as structural initialization, crossover, or delayed question assignment. The question
 * can be set or replaced later via {@link #setQuestion(Question)}.
 * <p>
 * The node is considered fully initialized only when it has a non-{@code null} question,
 * a father, and both children.
 *
 * @author jinseisieko
 */
public class QuestionNode extends Node implements DecisionTreeNode {

    private Question question;

    /**
     * Constructs an empty question node with no parent, no children, and no question.
     * <p>
     * All internal references ({@code father}, {@code leftSon}, {@code rightSon})
     * are initialized to {@code null}.
     *
     * @author jinseisieko
     */
    public QuestionNode() {
        super();
        this.question = null; // explicitly uninitialized
    }

    /**
     * Constructs a question node with the specified children but no initial question.
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
    public QuestionNode(Node leftSon, Node rightSon) {
        super(leftSon, rightSon);
        this.question = null; // explicitly uninitialized
    }

    /**
     * Constructs a fully initialized question node with a question and children.
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
    public QuestionNode(Question question, Node leftSon, Node rightSon) {
        this(leftSon, rightSon);
        setQuestion(question);
    }

    /**
     * Returns the current question associated with this node.
     * <p>
     * May return {@code null} if the question has not been assigned yet.
     *
     * @return the current question, or {@code null} if uninitialized <p>
     *
     * @author jinseisieko
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * Replaces the current question with a new one.
     * <p>
     * This method is essential for mutation and initialization in evolutionary algorithms.
     *
     * @param question the new question; must not be {@code null} <p>
     * @throws IllegalArgumentException if {@code question} is {@code null} <p>
     *
     * @author jinseisieko
     */
    public final void setQuestion(Question question) {
        if (question ==  null) {
            throw new IllegalArgumentException("Question cannot be null");
        }
        this.question = question;
    }

    /**
     * Determines the next node by evaluating the question using the provided {@code Answerer}.
     * <p>
     * Throws an {@link IllegalStateException} if the question has not been set.
     *
     * @param answerer the strategy for answering questions; must not be {@code null} <p>
     * @return the next node in the traversal path <p>
     * @throws IllegalStateException if the question is {@code null} <p>
     * @throws NullPointerException if {@code answerer} is {@code null} <p>
     *
     * @author jinseisieko
     */
    @Override
    public Node next(Answerer answerer) {
        if (this.question == null) {
            throw new IllegalStateException("Question has not been assigned to this node");
        }
        if (answerer == null){
            throw new IllegalArgumentException("Answerer cannot be null");
        }
        boolean answer = answerer.answer(this.question);
        return this.traverse(!answer);
    }

    /**
     * Checks whether this node is fully initialized and ready for traversal.
     * <p>
     * A {@code QuestionNode} is initialized only when it has a non-{@code null} father,
     * both non-{@code null} children, and a non-{@code null} question.
     *
     * @return {@code true} if all required fields are set, {@code false} otherwise <p>
     *
     * @author jinseisieko
     */
    @Override
    public boolean isInitialized() {
        return getFather() != null
            && getLeftSon() != null
            && getRightSon() != null
            && getQuestion() != null;
    }
}