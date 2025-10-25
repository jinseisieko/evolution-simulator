// src/main/java/org/jinseisieko/evolution/model/Agent.java
package org.jinseisieko.evolution.model;

import org.jinseisieko.evolution.basic.Point;
import org.jinseisieko.evolution.bindingcomponents.Answerer;
import org.jinseisieko.evolution.bindingcomponents.Question;
import org.jinseisieko.evolution.bindingcomponents.Status;

/**
 * Abstract base class for all entities that possess a brain and maintain a status.
 * <p>
 * Each concrete subclass must implement {@link #statusActivity()} to define how the current
 * status influences the agent's behavior. Additionally, subclasses must implement
 * {@link #answer(Question)} to respond to questions posed during decision-making.
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
    private final double SPEED_ENERGY_COST;
    private final double ANGULAR_SPEED_ENERGY_COST;

    /**
     * Constructs an agent with the specified initial state and energy cost parameters.
     *
     * @param initialCoordinates the starting position of the agent (will be normalized to [0,1)) <p>
     * @param size the radius of the agent's body <p>
     * @param brainUpdateTime the interval (in seconds) between brain updates; must be positive <p>
     * @param brain the decision-making component; must not be null <p>
     * @param BRAIN_ENERGY_COST energy consumed each time the brain is used; must be non-negative <p>
     * @param SPEED_ENERGY_COST energy consumed per unit of linear speed per one time unit; must be non-negative <p>
     * @param ANGULAR_SPEED_ENERGY_COST energy consumed per unit of angular speed per one time unit; must be non-negative <p>
     * @throws IllegalArgumentException if any numeric parameter is invalid or if brain is null <p>
     *
     * @author jinseisieko
     */
    public Agent(
            Point initialCoordinates,
            double size,
            double brainUpdateTime,
            Brain brain,
            double BRAIN_ENERGY_COST,
            double SPEED_ENERGY_COST, 
            double ANGULAR_SPEED_ENERGY_COST
        ) {
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

        if (SPEED_ENERGY_COST < 0) {
            throw new IllegalArgumentException("SPEED_ENERGY_COST cannot be less than zero");
        }
        this.SPEED_ENERGY_COST = SPEED_ENERGY_COST;

        if (ANGULAR_SPEED_ENERGY_COST < 0) {
            throw new IllegalArgumentException("ANGULAR_SPEED_ENERGY_COST cannot be less than zero");
        }
        this.ANGULAR_SPEED_ENERGY_COST = ANGULAR_SPEED_ENERGY_COST;
    }

    /**
     * Returns the brain instance associated with this agent.
     * <p>
     * This method allows external components to inspect or interact with the agent's decision logic.
     *
     * @return the agent's brain; never null <p>
     *
     * @author jinseisieko
     */
    public Brain getBrain() {
        return brain;
    }

    /**
     * Returns the current status of the agent.
     * <p>
     * The status is determined by the brain during {@link #useBrain()} and interpreted
     * in {@link #statusActivity()} to drive behavior.
     *
     * @return the current status, or {@code null} if not yet set <p>
     *
     * @author jinseisieko
     */
    public Status getLocalStatus() {
        return localStatus;
    }

    /**
     * Sets the current status of the agent.
     *
     * @param localStatus the new status; must not be null <p>
     * @throws IllegalArgumentException if {@code localStatus} is null <p>
     *
     * @author jinseisieko
     */
    public void setLocalStatus(Status localStatus) {
        if (localStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.localStatus = localStatus;
    }

    /**
     * Returns the interval between successive brain updates.
     *
     * @return brain update period in seconds (always > 0) <p>
     *
     * @author jinseisieko
     */
    public double getBrainUpdateTime() {
        return brainUpdateTime;
    }

    /**
     * Sets the interval between brain updates.
     *
     * @param brainUpdateTime the new update period in seconds; must be greater than zero <p>
     * @throws IllegalArgumentException if {@code brainUpdateTime} is not positive <p>
     *
     * @author jinseisieko
     */
    public void setBrainUpdateTime(double brainUpdateTime) {
        if (brainUpdateTime <= 0) {
            throw new IllegalArgumentException("Brain update time should be more than zero");
        }
        this.brainUpdateTime = brainUpdateTime;
    }

    /**
     * Returns the current energy level of the agent.
     * <p>
     * Energy is a normalized value in the range [0, 1], where 0 indicates exhaustion.
     *
     * @return current energy level (≥ 0) <p>
     *
     * @author jinseisieko
     */
    public double getEnergy() {
        return energy;
    }

    /**
     * Sets the current energy level of the agent.
     *
     * @param energy the new energy level; must be non-negative <p>
     * @throws IllegalArgumentException if {@code energy} is negative <p>
     *
     * @author jinseisieko
     */
    public void setEnergy(double energy) {
        if (energy < 0) {
            throw new IllegalArgumentException("Energy cannot be less than zero");
        }
        this.energy = energy;
    }

    /**
     * Evaluates a given question using the agent's internal state.
     * <p>
     * Concrete subclasses must implement this method to support all questions used by the brain.
     *
     * @param question the question to evaluate; must not be null <p>
     * @return the boolean answer based on the agent's current state <p>
     *
     * @author jinseisieko
     */
    @Override
    public abstract boolean answer(Question question);

    /**
     * Executes behavior associated with the current status.
     * <p>
     * This method is called every simulation step and should modify the agent's physical
     * state (e.g., speed, acceleration) according to {@link #getLocalStatus()}.
     * Energy consumption due to movement is handled separately in {@link #updateEntity(double)}.
     *
     * @author jinseisieko
     */
    public abstract void statusActivity();

    /**
     * Invokes the brain to determine a new status based on the agent's current state.
     * <p>
     * This operation consumes a fixed amount of energy as defined by {@code BRAIN_ENERGY_COST}.
     *
     * @author jinseisieko
     */
    public void useBrain() {
        this.localStatus = this.brain.decide(this);
        this.energy -= this.BRAIN_ENERGY_COST;
    }

    /**
     * Updates the agent's state over the given time interval.
     * <p>
     * This method:
     * <ul>
     *   <li>Triggers brain execution if enough time has passed since the last update</li>
     *   <li>Applies status-driven behavior via {@link #statusActivity()}</li>
     *   <li>Consumes energy proportional to current speed and angular speed</li>
     *   <li>Updates position and orientation using physics from the superclass</li>
     * </ul>
     *
     * @param dt the time step duration in seconds <p>
     *
     * @author jinseisieko
     */
    @Override
    public void updateEntity(double dt) {
        this.brainTimer += dt;
        if (this.brainTimer >= this.brainUpdateTime) {
            this.useBrain();
            this.brainTimer = 0;
        }
        this.statusActivity();
        this.energy -= SPEED_ENERGY_COST * this.getSpeed() * dt;
        this.energy -= ANGULAR_SPEED_ENERGY_COST * this.getAngularSpeed() * dt;
        super.updateEntity(dt);
    }
}