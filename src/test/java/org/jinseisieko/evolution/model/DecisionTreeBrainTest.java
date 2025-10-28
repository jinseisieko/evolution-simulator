// src/test/java/org/jinseisieko/evolution/model/DecisionTreeBrain.java
package org.jinseisieko.evolution.model;

import org.jinseisieko.evolution.bindingcomponents.Question;
import org.jinseisieko.evolution.bindingcomponents.Status;
import org.jinseisieko.evolution.decisiontree.Node;
import org.jinseisieko.evolution.decisiontree.OutcomeNode;
import org.jinseisieko.evolution.decisiontree.QuestionNode;
import org.jinseisieko.evolution.decisiontree.RootQuestionNode;
import org.jinseisieko.evolution.decisiontree.stubs.EnergyQuestion;
import org.jinseisieko.evolution.decisiontree.stubs.MockStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecisionTreeBrainTest {
    @Test
    void createRandom_shloudWorkExactly() {
        Question[] questions = {new EnergyQuestion(1.0), new EnergyQuestion(2.0)};
        Status[] statuses = {new MockStatus("1"), new MockStatus("2")};
        int depth = 15;
        DecisionTreeBrain brain = DecisionTreeBrain.createRandom(depth, questions, statuses);
        brain.rebuildIndex();
        int nodeNumber = brain.getNodeNumber();
        int eq1 = 0;
        int eq2 = 0;
        int ms1 = 0;
        int ms2 = 0;
        for (int i = 1; i < nodeNumber + 1; i++) {
            Node node = brain.getNodeByIndex(i);
            switch (node) {
                case RootQuestionNode rNode -> {
                    EnergyQuestion q = (EnergyQuestion) rNode.getQuestion();
                    if ("EnergyQuestion{1.0}".equals(q.toString())) eq1++;
                    if ("EnergyQuestion{2.0}".equals(q.toString())) eq2++;
                }
                case QuestionNode qNode -> {
                    EnergyQuestion q = (EnergyQuestion) qNode.getQuestion();
                    if ("EnergyQuestion{1.0}".equals(q.toString())) eq1++;
                    if ("EnergyQuestion{2.0}".equals(q.toString())) eq2++;
                }
                case OutcomeNode oNode -> {
                    MockStatus s = (MockStatus) oNode.getStatus();
                    if ("MockStatus{1}".equals(s.toString())) ms1++;
                    if ("MockStatus{2}".equals(s.toString())) ms2++;
                }
                default -> assertTrue(false, "Unknown type of Node");
            }
        }  
        assertEquals(nodeNumber, eq1+eq2+ms1+ms2);
        assertTrue(eq1 != 0 && eq2 != 0 && ms1 != 0 && ms2 != 0);
    }
}