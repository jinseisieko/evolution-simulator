/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jinseisieko.evolution.decisiontree;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jinseisieko
 */
public class DecisionTreeTest {
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20})
    void constructur_shouldCreateRightDepth(int depth) {
        DecisionTree decisionTree = new DecisionTree(depth);
        Node node = decisionTree.getRoot();
        int dl = 0;
        while (node.traverse(true) != null) { 
            dl++;
            node = node.traverse(true);
        }
        int dr = 0;
        node = decisionTree.getRoot();
        while (node.traverse(false) != null) { 
            dr++;
            node = node.traverse(false);
        }
        node = decisionTree.getRoot();
        int dRandom = 0;
        boolean left = Math.random() < 0.5;
        while (node.traverse(left) != null) { 
            dRandom++;
            node = node.traverse(left);
            left = Math.random() < 0.5;
        }
        assertEquals(dl, depth);
        assertEquals(dr, depth);
        assertEquals(dRandom, depth);

    }
}
