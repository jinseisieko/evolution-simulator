// src/test/java/org/jinseisieko/evolution/base/stubs/TestAgent.java

package org.jinseisieko.evolution.base.stubs;

import org.jinseisieko.evolution.basic.Point;
import org.jinseisieko.evolution.bindingcomponents.Question;
import org.jinseisieko.evolution.base.Agent;

/**
 *
 * @author jinseisieko
 */
public class TestAgent extends Agent {

    private int number;

    public TestAgent(Point initialCoordinates, double size, int number, double brainUpdateTime, double BRAIN_ENERGY_COST) {
        super(initialCoordinates, size, brainUpdateTime, new SimpleBrain(), null, BRAIN_ENERGY_COST, 0.0, 0.0, 0.0);
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
    public void statusActivity(double st) {
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
