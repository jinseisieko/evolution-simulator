// src/main/java/org/decisiontree/DTNode.java
package org.jinseisieko.evolution.decisiontree;

/**
 *
 * @author jinseisieko
 */
public interface DecisionTreeNode {
    Node next(Answerer answerer);
}
