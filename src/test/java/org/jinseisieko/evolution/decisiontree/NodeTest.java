// src/test/java/org/jinseisieko/evolution/decisiontree/NodeTest.java
package org.jinseisieko.evolution.decisiontree;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Node} class, which represents a node in a binary decision tree.
 * The tree maintains automatic bidirectional links: when a node is assigned as a child
 * via {@link Node#setLeftSon(Node)} or {@link Node#setRightSon(Node)}, its {@code father}
 * reference is updated internally. Manual father assignment is not allowed.
 *
 * @author jinseisieko
 */
class NodeTest {

    // === Test constructors and initial state ===

    /**
     * Verifies that the default constructor initializes all internal references
     * ({@code father}, {@code leftSon}, {@code rightSon}) to {@code null}.
     */
    @Test
    void defaultConstructor_shouldInitializeAllReferencesToNull() {
        Node node = new Node();
        assertNull(node.getFather());
        assertNull(node.getLeftSon());
        assertNull(node.getRightSon());
    }

    /**
     * Confirms that the two-argument constructor correctly links the provided children
     * and automatically establishes bidirectional parent-child references.
     */
    @Test
    void constructorWithChildren_shouldEstablishBidirectionalLinks() {
        Node left = new Node();
        Node right = new Node();
        Node parent = new Node(left, right);

        assertEquals(parent, left.getFather());
        assertEquals(parent, right.getFather());
        assertEquals(left, parent.getLeftSon());
        assertEquals(right, parent.getRightSon());
    }

    // === Test child assignment and bidirectional linking ===

    /**
     * Validates that assigning a node as the left child via {@code setLeftSon}
     * updates both the parent's left reference and the child's {@code father} field.
     */
    @Test
    void setLeftSon_shouldUpdateChildsFatherReference() {
        Node parent = new Node();
        Node child = new Node();

        parent.setLeftSon(child);

        assertEquals(parent, child.getFather());
        assertEquals(child, parent.getLeftSon());
    }

    /**
     * Validates that assigning a node as the right child via {@code setRightSon}
     * updates both the parent's right reference and the child's {@code father} field.
     */
    @Test
    void setRightSon_shouldUpdateChildsFatherReference() {
        Node parent = new Node();
        Node child = new Node();

        parent.setRightSon(child);

        assertEquals(parent, child.getFather());
        assertEquals(child, parent.getRightSon());
    }

    /**
     * Ensures that setting a child to {@code null} via {@code setLeftSon(null)}
     * clears both the parent's left reference and the child's {@code father} link.
     */
    @Test
    void setLeftSonToNull_shouldClearChildsFatherReference() {
        Node parent = new Node();
        Node child = new Node();

        parent.setLeftSon(child);
        assertEquals(parent, child.getFather());
        assertEquals(child, parent.getLeftSon());

        parent.setLeftSon(null);
        assertNull(parent.getLeftSon());
        assertNull(child.getFather());
    }

    /**
     * Confirms that reassigning a child from one parent to another correctly
     * updates all links: the new parent gains the child, the old parent loses it,
     * and the child's {@code father} points to the new parent.
     */
    @Test
    void setLeftSon_shouldOverwritePreviousFather() {
        Node oldParent = new Node();
        Node newParent = new Node();
        Node child = new Node();

        oldParent.setLeftSon(child);
        assertEquals(oldParent, child.getFather());
        assertEquals(child, oldParent.getLeftSon());

        newParent.setLeftSon(child);
        assertEquals(newParent, child.getFather());
        assertEquals(child, newParent.getLeftSon());
        assertNull(oldParent.getLeftSon());
    }

    /**
     * Verifies that assigning the same child twice in a row does not corrupt
     * the internal state or break bidirectional links.
     */
    @Test
    void setLeftSonTwiceWithSameChild_shouldPreserveConsistency() {
        Node parent = new Node();
        Node child = new Node();

        parent.setLeftSon(child);
        parent.setLeftSon(child);

        assertEquals(parent, child.getFather());
        assertEquals(child, parent.getLeftSon());
    }

    /**
     * Validates that moving a child from the left slot to the right slot of the same parent
     * correctly updates all references: left becomes {@code null}, right points to the child,
     * and the child's {@code father} remains unchanged.
     */
    @Test
    void childMovedFromLeftToRight_shouldUpdateAllLinks() {
        Node parent = new Node();
        Node child = new Node();

        parent.setLeftSon(child);
        assertEquals(child, parent.getLeftSon());
        assertNull(parent.getRightSon());
        assertEquals(parent, child.getFather());

        parent.setRightSon(child);
        assertNull(parent.getLeftSon());
        assertEquals(child, parent.getRightSon());
        assertEquals(parent, child.getFather());
    }

    // === Test tree navigation ===

    /**
     * Confirms that the {@code traverse} method returns the correct child
     * based on the boolean direction flag: {@code true} for left, {@code false} for right.
     */
    @Test
    void traverse_shouldReturnCorrectChildBasedOnDirection() {
        Node left = new Node();
        Node right = new Node();
        Node parent = new Node(left, right);

        assertEquals(left, parent.traverse(true));
        assertEquals(right, parent.traverse(false));
    }

    /**
     * Ensures that {@code traverse} safely returns {@code null} when the requested
     * child does not exist (e.g., missing left or right son).
     */
    @Test
    void traverseOnMissingChild_shouldReturnNull() {
        Node node = new Node();
        assertNull(node.traverse(true));
        assertNull(node.traverse(false));

        Node parent = new Node();
        Node left = new Node();
        parent.setLeftSon(left);
        assertEquals(left, parent.traverse(true));
        assertNull(parent.traverse(false));
    }

    // === Test partial tree structures ===

    /**
     * Validates that a node with only one child (left or right) maintains consistent links
     * and does not require both children to be present.
     */
    @Test
    void nodeWithOnlyOneChild_shouldWorkCorrectly() {
        Node parent = new Node();
        Node left = new Node();

        parent.setLeftSon(left);

        assertEquals(left, parent.getLeftSon());
        assertNull(parent.getRightSon());
        assertEquals(parent, left.getFather());
    }

    // === Test initialization state ===

    /**
     * Confirms that {@code isInitialized()} returns {@code true} only for internal nodes
     * that have a non-{@code null} father and both non-{@code null} children.
     * Root nodes and leaf nodes must return {@code false}.
     */
    @Test
    void isInitialized_shouldReturnTrueOnlyForFullyLinkedInternalNode() {
        Node root = new Node();
        Node left = new Node();
        Node right = new Node();
        root.setLeftSon(left);
        root.setRightSon(right);

        assertFalse(root.isInitialized()); // no father
        assertFalse(left.isInitialized()); // no children
        assertFalse(right.isInitialized());

        Node grandpa = new Node();
        Node parent = new Node();
        Node childL = new Node();
        Node childR = new Node();

        grandpa.setLeftSon(parent);
        parent.setLeftSon(childL);
        parent.setRightSon(childR);

        assertTrue(parent.isInitialized()); // has father + both children
        assertFalse(grandpa.isInitialized()); // missing children
        assertFalse(childL.isInitialized());
        assertFalse(childR.isInitialized());
    }

    /**
     * Ensures that {@code isInitialized()} returns {@code false} if any of the three
     * required links ({@code father}, {@code leftSon}, {@code rightSon}) is {@code null}.
     */
    @Test
    void isInitialized_shouldReturnFalseIfAnyLinkIsNull() {
        Node node = new Node();
        node.setLeftSon(new Node());
        assertFalse(node.isInitialized()); // missing father and rightSon

        node = new Node();
        Node parent = new Node();
        parent.setLeftSon(node);
        assertFalse(node.isInitialized()); // missing children

        node = new Node();
        parent = new Node();
        parent.setLeftSon(node);
        Node leftChild = new Node();
        node.setLeftSon(leftChild);
        assertFalse(node.isInitialized()); // missing rightSon
    }
}