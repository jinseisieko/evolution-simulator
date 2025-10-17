/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package org.jinseisieko.evolution.decisiontree;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jinseisieko
 */
public class DecisionTree {

    private RootQuestionNode root;
    private List<OutcomeNode> leafs = new ArrayList<>();

    public DecisionTree(int depth) {
        this.root = generate(depth);
        
    }

    private static RootQuestionNode generate(int depth) {
        RootQuestionNode root = new RootQuestionNode(null, null, null);
        root.setLeftSon(nextNodeGeneration(1, depth, root));
        root.setRightSon(nextNodeGeneration(1, depth, root));
        return root;
    }

    private static Node nextNodeGeneration(
        int currentDepth, int depth, Node father
    ) {
        if (currentDepth == depth) {
            return new OutcomeNode(father); 
        }
        QuestionNode node = new QuestionNode(father, null, null);
        currentDepth++;
        node.setLeftSon(nextNodeGeneration(currentDepth, depth, node));
        node.setRightSon(nextNodeGeneration(currentDepth, depth, node));
        return node;
    }

    public RootQuestionNode getRoot() {
        return this.root;
    }
}
