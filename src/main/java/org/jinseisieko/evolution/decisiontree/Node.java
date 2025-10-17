// src/main/java/org/decisiontree/Node.java
package org.jinseisieko.evolution.decisiontree;

/**
 *
 * @author jinseisieko
 */
public class Node {
    private Node father;
    private Node leftSon;
    private Node rightSon;

    public Node(Node father, Node leftSon, Node rightSon)
    {
        this.father = father;
        this.leftSon = leftSon;
        this.rightSon = rightSon;
    }

    public Node getFather() {
        return father;
    }

    public void setFather(Node father) {
        this.father = father;
    }

    public Node getLeftSon() {
        return leftSon;
    }

    public void setLeftSon(Node leftSon) {
        this.leftSon = leftSon;
    }

    public Node getRightSon() {
        return rightSon;
    }

    public void setRightSon(Node rightSon) {
        this.rightSon = rightSon;
    }

    public Node traverse(boolean left)
    {
        if (left) {
            return getLeftSon();
        } else {
            return getRightSon();
        }
    }
}
