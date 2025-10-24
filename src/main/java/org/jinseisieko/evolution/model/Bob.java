/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package org.jinseisieko.evolution.model;

import org.jinseisieko.evolution.basic.Point;
import org.jinseisieko.evolution.bindingcomponents.Question;

/**
 *
 * @author jinseisieko
 */
public class Bob extends Agent {
    public Bob(Point initialCoordinates, double size, double brainUpdateTime, Brain brain, double BRAIN_ENERGY_COST) {
        super(initialCoordinates, size, brainUpdateTime, brain, BRAIN_ENERGY_COST);
    }

    @Override
    public boolean answer(Question question) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void statusActivity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
