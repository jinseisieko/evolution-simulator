/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package org.jinseisieko.evolution.model;

import org.jinseisieko.evolution.bindingcomponents.Answerer;
import org.jinseisieko.evolution.bindingcomponents.Question;
import org.jinseisieko.evolution.bindingcomponents.Status;
import org.jinseisieko.evolution.decisiontree.DecisionTree;
import org.jinseisieko.evolution.decisiontree.Node;
import org.jinseisieko.evolution.decisiontree.OutcomeNode;
import org.jinseisieko.evolution.decisiontree.QuestionNode;

/**
 *
 * @author jinseisieko
 */
public class DecisionTreeBrain extends DecisionTree implements Brain {

    public DecisionTreeBrain(int depth) {
        super(depth);
    }

    @Override
    public Status decide(Answerer context) {
        return this.apply(context);
    }

    public static DecisionTreeBrain createRandom(int depth, Question[] questions, Status[] statuses) {
        DecisionTreeBrain brain = new DecisionTreeBrain(depth);
        java.util.Random random = new java.util.Random();
        brain.rebuildIndex();
        int nodeNumber = brain.getNodeNumber();
        int statusNumber =(int) Math.pow(2, depth);
        for (int i = 1; i < nodeNumber+1; i++) {
            Node node = brain.getNodeByIndex(i);
            if (i > nodeNumber - statusNumber) {
                if (node instanceof OutcomeNode oNode) {
                    int randomValue = random.nextInt(statuses.length);
                    oNode.setStatus(statuses[randomValue]);
                } else {
                    assert true; 
                }
            } else {
                if (node instanceof QuestionNode oNode) {
                    int randomValue = random.nextInt(questions.length);
                    oNode.setQuestion(questions[randomValue]);
                } else {
                    assert true; 
                }
            }
        }
        return brain;
    }

    // public static DecisionTreeBrain cross(DecisionTreeBrain tree1, DecisionTreeBrain tree2) {
    //     int depth = tree1.getDepth();
    //     DecisionTreeBrain brain = new DecisionTreeBrain(tree1.getRoot());
    //     brain.rebuildIndex();
        
        
    //     return brain;
    // }
}
