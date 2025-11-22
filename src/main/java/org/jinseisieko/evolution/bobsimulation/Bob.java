// src/main/java/org/jinseisieko/evolution/bobsimulation/Bob.java
package org.jinseisieko.evolution.bobsimulation;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jinseisieko.evolution.base.Agent;
import org.jinseisieko.evolution.base.BasicSimulation;
import org.jinseisieko.evolution.base.Brain;
import org.jinseisieko.evolution.base.ResponsibleQuestion;
import org.jinseisieko.evolution.base.ResponsibleStatus;
import org.jinseisieko.evolution.basic.Point;
import org.jinseisieko.evolution.bindingcomponents.Question;
import org.jinseisieko.evolution.bobsimulation.statuses.RotateStatus;
import org.jinseisieko.evolution.view.DrawStyle;
import org.jinseisieko.evolution.view.Drawable;

public class Bob extends Agent implements Drawable {
    static final DrawStyle DRAW_STYLE = DrawStyle.filled(Color.BLACK);

    public Bob(Point initialCoordinates, double radius, double brainUpdateTime, Brain brain, BasicSimulation simulation, double BRAIN_ENERGY_COST, double SPEED_ENERGY_COST, double ANGULAR_SPEED_ENERGY_COST, double EAT_FOOD_ENERGY_COST) {
        super(initialCoordinates, radius, brainUpdateTime, brain, simulation, BRAIN_ENERGY_COST, SPEED_ENERGY_COST, ANGULAR_SPEED_ENERGY_COST, EAT_FOOD_ENERGY_COST);
        this.setLocalStatus(new RotateStatus(0.01));
    }

    public boolean answer(ResponsibleQuestion question) {
        return question.apply(this);
    }

    @Override
    public void statusActivity(double dt) {
        ((ResponsibleStatus) getLocalStatus()).applyThisStatus(this, dt);
    }

    @Override
    public double getSize() {
        return this.getRadius();
    }

    @Override
    public DrawStyle getStyle() {
        return DRAW_STYLE;
    }

    @Override
    public void draw(Graphics2D g2d, int pixelX, int pixelY, int pixelSize) {
        g2d.drawOval(pixelX-pixelSize, pixelY-pixelSize, pixelX+pixelSize, pixelY+pixelSize);
    }

    @Override
    public boolean answer(Question question) {
        if (question instanceof ResponsibleQuestion responsibleQuestion) {
            return this.answer(responsibleQuestion);
        } else throw new UnsupportedOperationException("This class works only with ResponsibleQuestion");
    }
    
}

