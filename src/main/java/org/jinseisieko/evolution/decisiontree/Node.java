// src/main/java/org/jinseisieko/evolution/decisiontree/Node.java
package org.jinseisieko.evolution.decisiontree;

/**
 * Represents a node in a binary decision tree. Each node may have a parent (father)
 * and up to two children: a left child and a right child. This class provides basic
 * navigation and structural integrity methods for tree traversal and management.
 * <p>
 * The class maintains bidirectional links: when a child is assigned via
 * {@link #setLeftSon(Node)} or {@link #setRightSon(Node)}, the child's
 * {@code father} reference is automatically updated to point back to this node.
 * Similarly, setting a {@code father} does not automatically update the parent's
 * child references — that must be done explicitly to avoid ambiguity.
 *
 * @author jinseisieko
 */
public class Node {

    private Node father;
    private Node leftSon;
    private Node rightSon;

    /**
     * Constructs an empty node with no parent and no children.
     * <p>
     * All internal references ({@code father}, {@code leftSon}, {@code rightSon})
     * are initialized to {@code null}.
     *
     * @author jinseisieko
     */
    public Node() {
        this.father = null;
        this.leftSon = null;
        this.rightSon = null;
    }

    /**
     * Constructs a node with the specified children. The parent reference is initially {@code null}.
     * <p>
     * This constructor establishes bidirectional links: after assignment,
     * both children will have their {@code father} reference set to this node.
     * If a child is {@code null}, no action is taken for that branch.
     *
     * @param leftSon  the left child node, or {@code null} if absent <p>
     * @param rightSon the right child node, or {@code null} if absent <p>
     *
     * @author jinseisieko
     */
    public Node(Node leftSon, Node rightSon) {
        this();
        setLeftSon(leftSon);
        setRightSon(rightSon);
    }

    /**
     * Returns the parent (father) of this node.
     * <p>
     * The returned reference may be {@code null} if this node is the root
     * of the tree or has not been linked to a parent.
     *
     * @return the parent node, or {@code null} if this node has no parent <p>
     *
     * @author jinseisieko
     */
    public final Node getFather() {
        return father;
    }

    /**
     * Returns the left child (leftSon) of this node.
     * <p>
     * The returned reference may be {@code null} if this node has no left child
     * (e.g., it is a leaf or only has a right branch).
     *
     * @return the left child node, or {@code null} if absent <p>
     *
     * @author jinseisieko
     */
    public final Node getLeftSon() {
        return leftSon;
    }

    /**
     * Sets the left child and maintains bidirectional consistency.
     * <p>
     * If the new child is non-{@code null}, its {@code father} is set to this node.
     * If the child previously belonged to another parent, it is automatically
     * removed from that parent's left or right slot.
     *
     * @param leftSon the new left child, or {@code null} to clear the link
     */
    public final void setLeftSon(Node leftSon) {
        // Отвязать текущего левого сына (если есть)
        if (this.leftSon != null) {
            // Убедимся, что он больше не считает нас отцом
            if (this.leftSon.father == this) {
                this.leftSon.father = null;
            }
        }

        this.leftSon = leftSon;

        if (leftSon != null) {
            // Отвязать его от старого отца (если был)
            ChildRole oldRole = leftSon.getRoleInFather();
            if (leftSon.father != null) {
                if (oldRole == ChildRole.LEFT) {
                    leftSon.father.leftSon = null;
                } else if (oldRole == ChildRole.RIGHT) {
                    leftSon.father.rightSon = null;
                }
            }
            // Установить нового отца
            leftSon.father = this;
        }
    }

    /**
     * Sets the right child and maintains bidirectional consistency.
     * <p>
     * Behaves analogously to {@link #setLeftSon(Node)}.
     *
     * @param rightSon the new right child, or {@code null} to clear the link
     */
    public final void setRightSon(Node rightSon) {
        if (this.rightSon != null) {
            if (this.rightSon.father == this) {
                this.rightSon.father = null;
            }
        }

        this.rightSon = rightSon;

        if (rightSon != null) {
            ChildRole oldRole = rightSon.getRoleInFather();
            if (rightSon.father != null) {
                if (oldRole == ChildRole.LEFT) {
                    rightSon.father.leftSon = null;
                } else if (oldRole == ChildRole.RIGHT) {
                    rightSon.father.rightSon = null;
                }
            }
            rightSon.father = this;
        }
    }

    /**
     * Returns the right child (rightSon) of this node.
     * <p>
     * The returned reference may be {@code null} if this node has no right child
     * (e.g., it is a leaf or only has a left branch).
     *
     * @return the right child node, or {@code null} if absent <p>
     *
     * @author jinseisieko
     */
    public final Node getRightSon() {
        return rightSon;
    }

    /**
     * Traverses to one of the children based on the given direction.
     * <p>
     * If {@code goLeft} is {@code true}, returns the left child;
     * otherwise, returns the right child. The result may be {@code null}
     * if the corresponding child does not exist.
     * <p>
     * This method is useful for generic tree navigation without explicit
     * branching logic.
     *
     * @param goLeft {@code true} to navigate to the left child, {@code false} for the right child <p>
     * @return the selected child node, or {@code null} if that child is absent <p>
     *
     * @author jinseisieko
     */
    public Node traverse(boolean goLeft) {
        return goLeft ? getLeftSon() : getRightSon();
    }

    /**
     * Checks whether this node is fully initialized with non-null parent and both children.
     * <p>
     * <strong>Note:</strong> This method is meaningful only for internal nodes
     * in a full binary tree. Root nodes (no father) and leaf nodes (no children)
     * will return {@code false}.
     * <p>
     * It is primarily intended for validation or debugging purposes.
     *
     * @return {@code true} if {@code father}, {@code leftSon}, and {@code rightSon}
     *         are all non-{@code null}; {@code false} otherwise <p>
     *
     * @author jinseisieko
     */
    public boolean isInitialized() {
        return this.father != null && this.leftSon != null && this.rightSon != null;
    }

    /**
     * Returns the role of this node relative to its father.
     *
     * @return {@code ChildRole.LEFT} if this node is the left son of its father,
     *         {@code ChildRole.RIGHT} if it is the right son,
     *         {@code ChildRole.NONE} if it has no father or is not linked as a child <p>
     */
    private ChildRole getRoleInFather() {
        if (this.father == null) {
            return ChildRole.NONE;
        }
        if (this.father.leftSon == this) {
            return ChildRole.LEFT;
        }
        if (this.father.rightSon == this) {
            return ChildRole.RIGHT;
        }
        return ChildRole.NONE; // inconsistent state
    }

    // Вспомогательный enum
    private enum ChildRole {
        LEFT, RIGHT, NONE
    }
}