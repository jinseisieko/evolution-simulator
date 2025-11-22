// src/main/java/org/jinseisieko/evolution/bobsimulation/BobSimulation.java
package org.jinseisieko.evolution.bobsimulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jinseisieko.evolution.base.DecisionTreeBrain;
import org.jinseisieko.evolution.base.DrawableBasicSimulation;
import org.jinseisieko.evolution.base.Simulation;
import org.jinseisieko.evolution.basic.Point;
import org.jinseisieko.evolution.bindingcomponents.Question;
import org.jinseisieko.evolution.bindingcomponents.Status;
import org.jinseisieko.evolution.bobsimulation.questions.DistanceToTheNearestFoodQuestion;
import org.jinseisieko.evolution.bobsimulation.questions.EnergyQuestion;
import org.jinseisieko.evolution.bobsimulation.questions.SpeedQuestion;
import org.jinseisieko.evolution.bobsimulation.statuses.RotateStatus;
import org.jinseisieko.evolution.bobsimulation.statuses.StickToSpeedStatus;
import org.jinseisieko.evolution.bobsimulation.statuses.TryToEatAndSwitchTo;
import org.jinseisieko.evolution.view.Drawable;

public class BobSimulation extends Simulation implements DrawableBasicSimulation {
    static final double FOOD_RADIUS = 0.01;
    static final double BOB_RADIUS = 0.01;
    static final double BRAIN_UPDATE_TIME = 0.5;
    static final int BRAIN_DEPTH = 10;
    static final double BRAIN_ENERGY_COST = 0.01;
    static final double SPEED_ENERGY_COST = 0.01;
    static final double ANGULAR_SPEED_ENERGY_COST = 0.01;
    static final double EAT_FOOD_ENERGY_COST = 0.01;
    static final double MEAT_TIME_HEALTH_COST = 0.01;
    static final double MEAT_ENERGY_VALUE = 0.1;
    static Question[] questions = {
        new DistanceToTheNearestFoodQuestion(0.1),
        new DistanceToTheNearestFoodQuestion(0.05),
        new DistanceToTheNearestFoodQuestion(0.025),
        new DistanceToTheNearestFoodQuestion(BOB_RADIUS),
        new EnergyQuestion(0.5),
        new EnergyQuestion(0.25),
        new EnergyQuestion(0.1),
        new EnergyQuestion(0.75),
        new SpeedQuestion(0.05),
        new SpeedQuestion(0.02),
        new SpeedQuestion(0.03),
        new SpeedQuestion(0.04)
    };
    static Status[] statuses = {
        new RotateStatus(0.1),
        new RotateStatus(0.2),
        new RotateStatus(0.3),
        new StickToSpeedStatus(0.03, 0.01),
        new StickToSpeedStatus(0.04, 0.01),
        new StickToSpeedStatus(0.05, 0.01),
        new TryToEatAndSwitchTo(new RotateStatus(0.3)),
        new TryToEatAndSwitchTo(new StickToSpeedStatus(0.03, 0.01)),
        new TryToEatAndSwitchTo(new TryToEatAndSwitchTo(new RotateStatus(0.3)))
    };
    private final List<Drawable> drawables;

    public BobSimulation() {
        super();
        this.drawables = new ArrayList<>();
    }

    public void addAgent(Bob bob) {
        super.addAgent(bob);
        this.drawables.add(bob);
    }

    public void addFood(Meat meat) {
        super.addFood(meat);
        this.drawables.add(meat);
    }

    public void addMeat(Meat meat) {
        super.addFood(meat);
        this.drawables.add(meat);
    }

    public void spawnMeat(double x, double y) {
        Meat meat = new Meat(new Point(x, y),  FOOD_RADIUS, this, MEAT_TIME_HEALTH_COST, MEAT_ENERGY_VALUE);
        this.addMeat(meat);
    }

    public void spawnRandomBob(double x, double y) {
        Bob bob = new Bob(new Point(x, y), BOB_RADIUS, BRAIN_UPDATE_TIME, DecisionTreeBrain.createRandom(BRAIN_DEPTH, questions, statuses), this, BRAIN_ENERGY_COST, SPEED_ENERGY_COST, ANGULAR_SPEED_ENERGY_COST, EAT_FOOD_ENERGY_COST);
        this.addAgent(bob);
    }

    @Override
    public List<Drawable> getDrawables() {
        return drawables;
    }

    public static BobSimulation create() {
        BobSimulation simulation = new BobSimulation();
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            simulation.spawnRandomBob(r.nextDouble()%1.0, r.nextDouble()%1.0);
        }
        for (int i = 0; i < 10; i++) {
            simulation.spawnMeat(r.nextDouble()%1.0, r.nextDouble()%1.0);
        }
        return simulation;
    }
}