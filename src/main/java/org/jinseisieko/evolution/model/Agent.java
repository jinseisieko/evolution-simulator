// src/main/java/org/jinseisieko/evolution/model/Agent.java
package org.jinseisieko.evolution.model;

import org.jinseisieko.evolution.basic.Point;
import org.jinseisieko.evolution.bindingcomponents.Answerer;
import org.jinseisieko.evolution.bindingcomponents.Question;
import org.jinseisieko.evolution.bindingcomponents.Status;

/**
 *
 * @author jinseisieko
 */
public abstract class Agent extends Entity implements Answerer {
    private final Brain brain;
    private Status localStatus;
    private double brainUpdateTime;
    private double brainTimer = 0.0;
    private double energy; // [0, 1]
    private final double BRAIN_ENERGY_COST;

    public Agent(Point initialCoordinates, double size, double brainUpdateTime, Brain brain, double BRAIN_ENERGY_COST) {
        super(initialCoordinates, size);
        if (brainUpdateTime <= 0) {
            throw new IllegalArgumentException("Brain update time should be more than zero");
        }
        this.brainUpdateTime = brainUpdateTime;
        if (brain == null) {
            throw new IllegalArgumentException("Brain cannot be null");
        }
        this.brain = brain;
        this.energy = 1.0;
        if (BRAIN_ENERGY_COST < 0) {
            throw new IllegalArgumentException("BRAIN_ENERGY_COST cannot be less than zero");
        }
        this.BRAIN_ENERGY_COST = BRAIN_ENERGY_COST;
    }

    public Brain getBrain() {
        return brain;
    }

    public Status getLocalStatus() {
        return localStatus;
    }

    public void setLocalStatus(Status localStatus) {
        this.localStatus = localStatus;
    }

    public double getBrainUpdateTime() {
        return brainUpdateTime;
    }

    public void setBrainUpdateTime(double brainUpdateTime) {
        this.brainUpdateTime = brainUpdateTime;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    @Override
    public abstract boolean answer(Question question);

    public abstract void statusActivity();
    
    public void useBrain() {
        this.localStatus = this.brain.decide(this);
        this.energy -= this.BRAIN_ENERGY_COST;
    }

    @Override
    public void updateEntity(double dt) {
        this.brainTimer += dt;
        if (this.brainTimer >= this.brainUpdateTime) {
            this.useBrain();
            this.brainTimer = 0;
        }
        this.statusActivity();
        super.updateEntity(dt);
    }

}
