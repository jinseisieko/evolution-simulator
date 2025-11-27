// src/main/java/io.github/jinseisieko/evolution/bobsimulation/Bob.java
package io.github.jinseisieko.evolution.bobsimulation;

import java.awt.Color;
import java.awt.Graphics2D;

import io.github.jinseisieko.evolution.base.Agent;
import io.github.jinseisieko.evolution.base.BasicSimulation;
import io.github.jinseisieko.evolution.base.Brain;
import io.github.jinseisieko.evolution.base.ResponsibleQuestion;
import io.github.jinseisieko.evolution.base.ResponsibleStatus;
import io.github.jinseisieko.evolution.basic.Point;
import io.github.jinseisieko.evolution.bindingcomponents.Question;
import io.github.jinseisieko.evolution.bobsimulation.statuses.RotateStatus;
import io.github.jinseisieko.evolution.view.DrawStyle;
import io.github.jinseisieko.evolution.view.Drawable;

public class Bob extends Agent implements Drawable {
    static final DrawStyle DRAW_STYLE = DrawStyle.filled(Color.BLUE);

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
        g2d.fillOval(pixelX, pixelY, pixelSize, pixelSize);
    }

    @Override
    public boolean answer(Question question) {
        if (question instanceof ResponsibleQuestion responsibleQuestion) {
            return this.answer(responsibleQuestion);
        } else throw new UnsupportedOperationException("This class works only with ResponsibleQuestion");
    }
    
}

