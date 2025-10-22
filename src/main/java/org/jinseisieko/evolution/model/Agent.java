/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package org.jinseisieko.evolution.model;

import org.jinseisieko.evolution.basic.Point;
import org.jinseisieko.evolution.decisiontree.Answerer;
import org.jinseisieko.evolution.decisiontree.Question;
import org.jinseisieko.evolution.decisiontree.Status;

/**
 *
 * @author jinseisieko
 */
public class Agent extends Entity implements Answerer {

    private final Brain brain;
    private Status localStatus;
    private double brainUpdateTime;
    private double brainTimer = 0.0;
    private double energy; // [0, 1]
    private final double BRAIN_ENERGY_COST = 0.01;

    public Agent(Point initialCoordinates, double size, double brainUpdateTime, Brain brain) {
        super(initialCoordinates, size);
        this.brainUpdateTime = brainUpdateTime;
        this.brain = brain;
        this.energy = 1.0;
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
    public boolean answer(Question question) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private void useBrain() {
        this.localStatus = this.brain.decide(this);
        this.energy -= this.BRAIN_ENERGY_COST;
    }

    private void statusActivity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateEntity(double dt) {
        this.brainTimer += dt;
        super.updateEntity(dt);
        if (this.brainTimer >= this.brainUpdateTime) {
            this.useBrain();
        }
        this.statusActivity();
    }

}
