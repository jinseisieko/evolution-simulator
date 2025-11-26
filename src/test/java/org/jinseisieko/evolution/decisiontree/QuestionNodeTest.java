// src/test/java/org/jinseisieko/evolution/decisiontree/QuestionNodeTest.java
package org.jinseisieko.evolution.decisiontree;

import org.jinseisieko.evolution.bindingcomponents.Answerer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jinseisieko.evolution.decisiontree.stubs.EnergyQuestion;
import org.jinseisieko.evolution.decisiontree.stubs.PredatorQuestion;

/**
 * Unit tests for the {@link QuestionNode} class, which represents an internal node
 * in a binary decision tree that evaluates a {@link Question} and branches accordingly.
 *
 * @author jinseisieko
 */
class QuestionNodeTest {

    // === Test construction and initialization ===

    /**
     * Verifies that a {@code QuestionNode} constructed without a question is not initialized.
     */
    @Test
    void constructorWithoutQuestion_shouldNotBeInitialized() {
        Node left = new Node();
        Node right = new Node();
        QuestionNode node = new QuestionNode(left, right);
        assertFalse(node.isInitialized());
        assertNull(node.getQuestion());
    }

    /**
     * Confirms that a {@code QuestionNode} becomes initialized after setting a question
     * and linking to a parent.
     */
    @Test
    void setQuestion_andLinkToParent_shouldMakeNodeInitialized() {
        Node left = new Node();
        Node right = new Node();
        QuestionNode node = new QuestionNode(left, right);
        Node parent = new Node();
        parent.setLeftSon(node); // establishes father link

        node.setQuestion(new EnergyQuestion(0.5));

        assertTrue(node.isInitialized());
    }

    // === Test question mutability ===

    /**
     * Validates that the question can be changed after construction.
     */
    @Test
    void setQuestion_shouldReplaceExistingQuestion() {
        QuestionNode node = new QuestionNode(new Node(), new Node());
        EnergyQuestion q1 = new EnergyQuestion(0.3);
        PredatorQuestion q2 = new PredatorQuestion(1.5);

        node.setQuestion(q1);
        assertEquals(q1, node.getQuestion());

        node.setQuestion(q2);
        assertEquals(q2, node.getQuestion());
    }

    /**
     * Ensures that setting a {@code null} question throws an exception.
     */
    @Test
    void setQuestion_withNull_shouldThrowException() {
        QuestionNode node = new QuestionNode(new Node(), new Node());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            node.setQuestion(null);
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    // === Test traversal logic ===

    /**
     * Validates that {@code next()} returns the left child when the answer is {@code false}.
     */
    @Test
    void next_withFalseAnswer_shouldReturnLeftChild() {
        Node left = new Node();
        Node right = new Node();
        QuestionNode node = new QuestionNode(new EnergyQuestion(0.5), left, right);

        Answerer falseAnswerer = q -> false;
        assertEquals(left, node.next(falseAnswerer));
    }

    /**
     * Validates that {@code next()} returns the right child when the answer is {@code true}.
     */
    @Test
    void next_withTrueAnswer_shouldReturnRightChild() {
        Node left = new Node();
        Node right = new Node();
        QuestionNode node = new QuestionNode(new EnergyQuestion(0.5), left, right);

        Answerer trueAnswerer = q -> true;
        assertEquals(right, node.next(trueAnswerer));
    }

    /**
     * Ensures that calling {@code next()} before setting a question throws an exception.
     */
    @Test
    void next_withoutQuestion_shouldThrowIllegalStateException() {
        QuestionNode node = new QuestionNode(new Node(), new Node());
        Answerer anyAnswerer = q -> true;

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            node.next(anyAnswerer);
        });
        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().toLowerCase().contains("question"));
    }

    /**
     * Confirms that {@code next()} throws {@code NullPointerException} if answerer is {@code null}.
     */
    @Test
    void next_withNullAnswerer_shouldThrowNullPointerExceptionWithMessage() {
        QuestionNode node = new QuestionNode(new EnergyQuestion(0.5), new Node(), new Node());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            node.next(null);
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }
}