// src/test/java/org/jinseisieko/evolution/decisiontree/QuestionTest.java
package io.github.jinseisieko.evolution.decisiontree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import io.github.jinseisieko.evolution.decisiontree.stubs.EnergyQuestion;
import io.github.jinseisieko.evolution.decisiontree.stubs.PredatorQuestion;

/**
 * Unit tests for implementations of the {@link Question} interface.
 * Validates equality, hash code consistency, and immutability contracts.
 *
 * @author jinseisieko
 */
class QuestionTest {

    // === Test EnergyQuestion ===

    /**
     * Verifies that two {@code EnergyQuestion} instances with the same threshold are equal.
     */
    @Test
    void energyQuestion_sameThreshold_shouldBeEqual() {
        EnergyQuestion q1 = new EnergyQuestion(0.5);
        EnergyQuestion q2 = new EnergyQuestion(0.5);
        assertEquals(q1, q2);
        assertEquals(q1.hashCode(), q2.hashCode());
    }

    /**
     * Confirms that {@code EnergyQuestion} instances with different thresholds are not equal.
     */
    @Test
    void energyQuestion_differentThresholds_shouldNotBeEqual() {
        EnergyQuestion q1 = new EnergyQuestion(0.4);
        EnergyQuestion q2 = new EnergyQuestion(0.6);
        assertNotEquals(q1, q2);
    }

    /**
     * Ensures that {@code EnergyQuestion} is not equal to questions of other types.
     */
    @Test
    void energyQuestion_vsPredatorQuestion_shouldNotBeEqual() {
        EnergyQuestion energy = new EnergyQuestion(0.5);
        PredatorQuestion predator = new PredatorQuestion(1.0);
        assertNotEquals(energy, predator);
    }

    // === Test PredatorQuestion ===

    /**
     * Validates that two {@code PredatorQuestion} instances with the same radius are equal.
     */
    @Test
    void predatorQuestion_sameRadius_shouldBeEqual() {
        PredatorQuestion q1 = new PredatorQuestion(2.0);
        PredatorQuestion q2 = new PredatorQuestion(2.0);
        assertEquals(q1, q2);
        assertEquals(q1.hashCode(), q2.hashCode());
    }

    /**
     * Confirms that hash codes are stable across multiple calls.
     */
    @Test
    void question_hashCode_shouldBeStable() {
        EnergyQuestion q = new EnergyQuestion(0.75);
        int h1 = q.hashCode();
        int h2 = q.hashCode();
        assertEquals(h1, h2);
    }

    // === Test bidirectional parent-child links ===

    /**
     * Verifies that assigning children via constructor establishes correct father links.
     */
    @Test
    void constructorWithChildren_shouldSetFatherReferencesInChildren() {
        Node left = new Node();
        Node right = new Node();
        QuestionNode parent = new QuestionNode(left, right);

        assertEquals(parent, left.getFather());
        assertEquals(parent, right.getFather());
    }

    /**
     * Confirms that replacing a child via {@code setLeftSon} updates the new child's father
     * and clears the old child's father reference.
     */
    @Test
    void setLeftSon_shouldMaintainBidirectionalConsistency() {
        QuestionNode parent = new QuestionNode(new Node(), new Node());
        Node oldChild = parent.getLeftSon();
        Node newChild = new Node();

        parent.setLeftSon(newChild);

        assertEquals(parent, newChild.getFather());
        assertNull(oldChild.getFather());
        assertEquals(newChild, parent.getLeftSon());
    }

    /**
     * Validates that moving a child from one {@code QuestionNode} to another
     * correctly updates all links.
     */
    @Test
    void childMovedBetweenQuestionNodes_shouldUpdateAllReferences() {
        QuestionNode parent1 = new QuestionNode(new Node(), new Node());
        QuestionNode parent2 = new QuestionNode(new Node(), new Node());
        Node child = new Node();

        parent1.setLeftSon(child);
        assertEquals(parent1, child.getFather());

        parent2.setRightSon(child);
        assertEquals(parent2, child.getFather());
        assertNull(parent1.getLeftSon());
        assertEquals(child, parent2.getRightSon());
    }

    // === Test traversal behavior ===

    /**
     * Ensures that {@code traverse(true)} returns the left child and {@code traverse(false)} the right,
     * consistent with {@code Node} contract.
     */
    @Test
    void traverse_shouldDelegateToInheritedLogicCorrectly() {
        Node left = new Node();
        Node right = new Node();
        QuestionNode node = new QuestionNode(left, right);

        assertEquals(left, node.traverse(true));
        assertEquals(right, node.traverse(false));
    }

    /**
     * Confirms that {@code traverse} returns {@code null} when the requested child is absent.
     */
    @Test
    void traverse_onPartiallyConstructedNode_shouldReturnNullForMissingChild() {
        QuestionNode node = new QuestionNode(null, new Node());
        assertNull(node.traverse(true));  // left is null
        assertNotNull(node.traverse(false)); // right is present
    }

    // === Test initialization state ===

    /**
     * Validates that {@code isInitialized()} returns {@code false} if any child is {@code null},
     * even if the question is set.
     */
    @Test
    void isInitialized_withMissingChild_shouldReturnFalse() {
        QuestionNode node = new QuestionNode(null, new Node());
        node.setQuestion(new EnergyQuestion(0.5));
        assertFalse(node.isInitialized());
    }

    /**
     * Confirms that {@code isInitialized()} returns {@code false} if the node has no father,
     * even with question and both children present.
     */
    @Test
    void isInitialized_withoutFather_shouldReturnFalse() {
        QuestionNode node = new QuestionNode(new Node(), new Node());
        node.setQuestion(new EnergyQuestion(0.5));
        assertFalse(node.isInitialized()); // no father
    }

    /**
     * Ensures that {@code isInitialized()} returns {@code true} only when:
     * - father ≠ null,
     * - leftSon ≠ null,
     * - rightSon ≠ null,
     * - question ≠ null.
     */
    @Test
    void isInitialized_withFullStructure_shouldReturnTrue() {
        Node left = new Node();
        Node right = new Node();
        QuestionNode node = new QuestionNode(left, right);
        Node father = new Node();
        father.setLeftSon(node); // establishes father link
        node.setQuestion(new EnergyQuestion(0.7));

        assertTrue(node.isInitialized());
    }

    // === Test question persistence during structural changes ===

    /**
     * Validates that replacing children does not affect the question reference.
     */
    @Test
    void setLeftSon_shouldNotAffectQuestion() {
        EnergyQuestion originalQuestion = new EnergyQuestion(0.4);
        QuestionNode node = new QuestionNode(new Node(), new Node());
        node.setQuestion(originalQuestion);

        node.setLeftSon(new Node());

        assertEquals(originalQuestion, node.getQuestion());
    }

    /**
     * Confirms that the question remains intact even when the node is moved
     * between different parents.
     */
    @Test
    void movingNodeBetweenParents_shouldPreserveQuestion() {
        EnergyQuestion q = new EnergyQuestion(0.9);
        QuestionNode node = new QuestionNode(new Node(), new Node());
        node.setQuestion(q);

        Node parent1 = new Node();
        Node parent2 = new Node();

        parent1.setLeftSon(node);
        assertEquals(q, node.getQuestion());

        parent2.setRightSon(node);
        assertEquals(q, node.getQuestion());
        assertEquals(parent2, node.getFather());
    }
}