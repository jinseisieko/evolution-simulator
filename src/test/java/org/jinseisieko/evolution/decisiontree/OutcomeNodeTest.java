// src/test/java/org/jinseisieko/evolution/decisiontree/OutcomeNodeTest.java
package org.jinseisieko.evolution.decisiontree;

import org.jinseisieko.evolution.bindingcomponents.Answerer;
import org.jinseisieko.evolution.bindingcomponents.Status;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jinseisieko.evolution.decisiontree.stubs.MockStatus; // Assuming you have a MockStatus for testing

/**
 * Unit tests for the {@link OutcomeNode} class, which represents a leaf node
 * in a binary decision tree holding a final {@link Status}.
 *
 * @author jinseisieko
 */
class OutcomeNodeTest {

    // === Test construction and initialization ===

    /**
     * Verifies that a {@code OutcomeNode} constructed without a status has a null status initially.
     */
    @Test
    void constructorWithoutStatus_shouldInitializeWithNullStatus() {
        OutcomeNode node = new OutcomeNode();
        assertNull(node.getStatus());
        // isInitialized should be false because status is null
        assertFalse(node.isInitialized());
    }

    /**
     * Confirms that a {@code OutcomeNode} can be constructed with a specific status.
     */
    @Test
    void constructorWithStatus_shouldSetStatus() {
        MockStatus status = new MockStatus("Test Outcome");
        OutcomeNode node = new OutcomeNode(status);
        assertEquals(status, node.getStatus());
    }

    /**
     * Ensures that constructing an {@code OutcomeNode} with a {@code null} status throws an exception.
     */
    @Test
    void constructorWithNullStatus_shouldThrowException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            OutcomeNode node = new OutcomeNode((Status) null);
            node.getStatus();
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    // === Test status mutability ===

    /**
     * Validates that the status can be changed after construction using setStatus.
     */
    @Test
    void setStatus_shouldReplaceExistingStatus() {
        OutcomeNode node = new OutcomeNode();
        MockStatus status1 = new MockStatus("Initial Outcome");
        MockStatus status2 = new MockStatus("Updated Outcome");

        node.setStatus(status1);
        assertEquals(status1, node.getStatus());

        node.setStatus(status2);
        assertEquals(status2, node.getStatus());
    }

    /**
     * Ensures that setting a {@code null} status throws an exception.
     */
    @Test
    void setStatus_withNull_shouldThrowException() {
        OutcomeNode node = new OutcomeNode();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            node.setStatus(null);
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    // === Test initialization check ===

    /**
     * Verifies that {@code isInitialized()} returns false if the father is null.
     */
    @Test
    void isInitialized_withoutFather_shouldReturnFalse() {
        OutcomeNode node = new OutcomeNode(new MockStatus("Valid Status"));
        // Node is created without a father, so node.getFather() is null
        assertFalse(node.isInitialized());
    }

    /**
     * Verifies that {@code isInitialized()} returns false if the status is null.
     */
    @Test
    void isInitialized_withoutStatus_shouldReturnFalse() {
        OutcomeNode node = new OutcomeNode(); // Status is null
        Node parent = new Node();
        parent.setLeftSon(node); // Sets node.father = parent
        assertFalse(node.isInitialized());
    }

    /**
     * Verifies that {@code isInitialized()} returns true only when both father and status are set.
     */
    @Test
    void isInitialized_withFatherAndStatus_shouldReturnTrue() {
        OutcomeNode node = new OutcomeNode(new MockStatus("Valid Status"));
        Node parent = new Node();
        parent.setLeftSon(node); // Sets node.father = parent
        assertTrue(node.isInitialized());
    }

    // === Test traversal logic ===

    /**
     * Validates that {@code next()} always returns null, signifying the end of the path.
     */
    @Test
    void next_shouldAlwaysReturnNull() {
        OutcomeNode node = new OutcomeNode(new MockStatus("Any Outcome"));
        Answerer mockAnswerer = q -> true; // Answerer is not used by OutcomeNode
        Node result = node.next(mockAnswerer);
        assertNull(result);

        // Test again with a different mock answerer
        Answerer anotherMockAnswerer = q -> false;
        result = node.next(anotherMockAnswerer);
        assertNull(result);
    }

    /**
     * Confirms that {@code next()} handles a null answerer gracefully (returns null).
     * Note: This test assumes the implementation in OutcomeNode.next does not explicitly check for null.
     * If Node methods are called on the result (which is null), it might lead to NPE later,
     * but the `next` method itself just returns null.
     * If explicit null check is added to OutcomeNode.next, this test would verify it.
     */
    @Test
    void next_withNullAnswerer_shouldReturnNull() { // Or throw NPE if explicitly checked, adjust based on OutcomeNode impl
        OutcomeNode node = new OutcomeNode(new MockStatus("Any Outcome"));
        Node result = node.next(null);
        assertNull(result);
    }
}