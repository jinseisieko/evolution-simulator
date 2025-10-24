/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package org.jinseisieko.evolution.model.stubs;

import org.jinseisieko.evolution.basic.Point;
import org.jinseisieko.evolution.bindingcomponents.Question;
import org.jinseisieko.evolution.model.Agent;

/**
 *
 * @author jinseisieko
 */
public class TestAgent extends Agent {

    private int number;

    public TestAgent(Point initialCoordinates, double size, int number, double brainUpdateTime, double BRAIN_ENERGY_COST) {
        super(initialCoordinates, size, brainUpdateTime, new SimpleBrain(), BRAIN_ENERGY_COST, 0.0, 0.0);
        this.number = number;
    }

    @Override
    public boolean answer(Question question) {
        if (question instanceof LessThanQuestion q) {
            return q.getNumber() > this.getNumber();
        }
        throw new IllegalArgumentException("There is not such type Question");
    }

    @Override
    public void statusActivity() {
        if (this.getLocalStatus() instanceof FirstStatus) {    
            this.setSpeed(10);        
        }
        else if (this.getLocalStatus() instanceof SecondStatus) {    
            this.setSpeed(-10);        
        }
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}
