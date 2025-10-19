// src/test/java/org/jinseisieko/evolution/decisiontree/RootQuestionNodeTest.java
package org.jinseisieko.evolution.decisiontree;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jinseisieko.evolution.decisiontree.stubs.EnergyQuestion; // Assuming you have stub questions
import org.jinseisieko.evolution.decisiontree.stubs.PredatorQuestion;

/**
 * Unit tests for the {@link RootQuestionNode} class, which represents the root
 * of a binary decision tree. Verifies its behavior regarding initialization,
 * question management, and traversal logic, ensuring it functions correctly
 * as the entry point of the tree.
 *
 * @author jinseisieko
 */
class RootQuestionNodeTest {

    // === Test construction and initialization ===

    /**
     * Verifies that a {@code RootQuestionNode} constructed without a question is not initialized.
     * A root node needs a question, left child, and right child to be initialized.
     */
    @Test
    void constructorWithoutQuestion_shouldNotBeInitialized() {
        Node left = new Node();
        Node right = new Node();
        RootQuestionNode rootNode = new RootQuestionNode(left, right);
        assertFalse(rootNode.isInitialized());
        assertNull(rootNode.getQuestion());
    }

    /**
     * Confirms that a {@code RootQuestionNode} becomes initialized after setting a question.
     * The root node does not require a parent (father) to be initialized.
     */
    @Test
    void setQuestion_shouldMakeNodeInitialized() {
        Node left = new Node();
        Node right = new Node();
        RootQuestionNode rootNode = new RootQuestionNode(left, right);

        rootNode.setQuestion(new EnergyQuestion(0.5));

        assertTrue(rootNode.isInitialized());
    }

    /**
     * Verifies that a {@code RootQuestionNode} constructed with a question and children is initialized.
     */
    @Test
    void constructorWithQuestionAndChildren_shouldBeInitialized() {
        Node left = new Node();
        Node right = new Node();
        EnergyQuestion question = new EnergyQuestion(0.5);
        RootQuestionNode rootNode = new RootQuestionNode(question, left, right);

        assertTrue(rootNode.isInitialized());
        assertEquals(question, rootNode.getQuestion());
        assertEquals(left, rootNode.getLeftSon());
        assertEquals(right, rootNode.getRightSon());
    }

    /**
     * Ensures that constructing a {@code RootQuestionNode} with a {@code null} question throws an exception.
     */
    @Test
    void constructorWithNullQuestion_shouldThrowException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            RootQuestionNode rootNode = new RootQuestionNode(null, new Node(), new Node());
            rootNode.getQuestion();
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    // === Test question mutability ===

    /**
     * Validates that the question can be changed after construction.
     */
    @Test
    void setQuestion_shouldReplaceExistingQuestion() {
        RootQuestionNode rootNode = new RootQuestionNode(new Node(), new Node());
        EnergyQuestion q1 = new EnergyQuestion(0.3);
        PredatorQuestion q2 = new PredatorQuestion(1.5);

        rootNode.setQuestion(q1);
        assertEquals(q1, rootNode.getQuestion());

        rootNode.setQuestion(q2);
        assertEquals(q2, rootNode.getQuestion());
    }

    /**
     * Ensures that setting a {@code null} question throws an exception.
     */
    @Test
    void setQuestion_withNull_shouldThrowException() {
        RootQuestionNode rootNode = new RootQuestionNode(new Node(), new Node());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            rootNode.setQuestion(null);
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    // === Test traversal logic ===

    /**
     * Validates that {@code next()} returns the left child when the answer is {@code false}.
     * The traversal logic should be inherited from QuestionNode.
     */
    @Test
    void next_withFalseAnswer_shouldReturnLeftChild() {
        Node left = new Node();
        Node right = new Node();
        RootQuestionNode rootNode = new RootQuestionNode(new EnergyQuestion(0.5), left, right);

        Answerer falseAnswerer = q -> false; // Mock answerer always returns false
        assertEquals(left, rootNode.next(falseAnswerer));
    }

    /**
     * Validates that {@code next()} returns the right child when the answer is {@code true}.
     * The traversal logic should be inherited from QuestionNode.
     */
    @Test
    void next_withTrueAnswer_shouldReturnRightChild() {
        Node left = new Node();
        Node right = new Node();
        RootQuestionNode rootNode = new RootQuestionNode(new EnergyQuestion(0.5), left, right);

        Answerer trueAnswerer = q -> true; // Mock answerer always returns true
        assertEquals(right, rootNode.next(trueAnswerer));
    }

    /**
     * Ensures that calling {@code next()} before setting a question throws an exception.
     * The traversal logic should be inherited from QuestionNode.
     */
    @Test
    void next_withoutQuestion_shouldThrowIllegalStateException() {
        RootQuestionNode rootNode = new RootQuestionNode(new Node(), new Node());
        Answerer anyAnswerer = q -> true;

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            rootNode.next(anyAnswerer);
        });
        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().toLowerCase().contains("question"));
    }

    /**
     * Confirms that {@code next()} throws {@code IllegalArgumentException} if answerer is {@code null}.
     * The traversal logic should be inherited from QuestionNode.
     */
    @Test
    void next_withNullAnswerer_shouldThrowIllegalArgumentException() {
        RootQuestionNode rootNode = new RootQuestionNode(new EnergyQuestion(0.5), new Node(), new Node());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            rootNode.next(null);
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    // === Test initialization checks (specific to RootQuestionNode) ===

    /**
     * Verifies that {@code isInitialized()} returns false if the left child is null.
     */
    @Test
    void isInitialized_withoutLeftChild_shouldReturnFalse() {
        RootQuestionNode rootNode = new RootQuestionNode(new EnergyQuestion(0.5), null, new Node());
        assertFalse(rootNode.isInitialized());
    }

    /**
     * Verifies that {@code isInitialized()} returns false if the right child is null.
     */
    @Test
    void isInitialized_withoutRightChild_shouldReturnFalse() {
        RootQuestionNode rootNode = new RootQuestionNode(new EnergyQuestion(0.5), new Node(), null);
        assertFalse(rootNode.isInitialized());
    }

    /**
     * Verifies that {@code isInitialized()} returns false if the question is null.
     */
    @Test
    void isInitialized_withoutQuestion_shouldReturnFalse() {
        RootQuestionNode rootNode = new RootQuestionNode(new Node(), new Node()); // Question is null
        assertFalse(rootNode.isInitialized());
    }

    /**
     * Verifies that {@code isInitialized()} returns true when left child, right child, and question are set,
     * even though the father (parent) is null (as expected for a root node).
     */
    @Test
    void isInitialized_withChildrenAndQuestion_shouldReturnTrue() {
        RootQuestionNode rootNode = new RootQuestionNode(new EnergyQuestion(0.5), new Node(), new Node());
        assertTrue(rootNode.isInitialized());
    }
}