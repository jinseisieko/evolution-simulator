// src/main/java/org/jinseisieko/evolution/model/Agent.java
package org.jinseisieko.evolution.model;

import org.jinseisieko.evolution.basic.Point;
import org.jinseisieko.evolution.bindingcomponents.Answerer;
import org.jinseisieko.evolution.bindingcomponents.Question;
import org.jinseisieko.evolution.bindingcomponents.Status;

/**
 * Абстрактный класс, который обозначает всех сущностей которые имеют мозг, а также состаяния
 * Каждое состояние должно обрабатываться в statusActivity() и как-то влиять на поведение агента.
 * Также наследники должны реализовать функции answer() чтобы она умела отвечать на опредеенные вопросы 
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
     * Конструктор класса
     */
    public Agent(Point initialCoordinates, double size, double brainUpdateTime, Brain brain, double BRAIN_ENERGY_COST, double SPEED_ENERGY_COST, double ANGULAR_SPEED_ENERGY_COST) {
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
     * Функция которая возращает обект мозга, нужна для возможных манипуляций с объектом или извлечения данных их мозга
     */
    public Brain getBrain() {
        return brain;
    }

    /**
     * Возращает текущий статус агента
     */
    public Status getLocalStatus() {
        return localStatus;
    }

    /**
     * устанавливает текущий статус агента
     */
    public void setLocalStatus(Status localStatus) {
        if (localStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.localStatus = localStatus;
    }

    /**
     * Возвраящает период времения обновления мозга
     */
    public double getBrainUpdateTime() {
        return brainUpdateTime;
    }

    /**
     * Устанавливает период времение когда будет работать мозг
     */
    public void setBrainUpdateTime(double brainUpdateTime) {
        if (brainUpdateTime <= 0) {
            throw new IllegalArgumentException("Brain update time should be more than zero");
        }
        this.brainUpdateTime = brainUpdateTime;
    }  

    /**
     * Возращает текущую энергию 
     */
    public double getEnergy() {
        return energy;
    }


    /**
     * Устанавливает текущуюю энергию
     */
    public void setEnergy(double energy) {
        if (energy < 0) {
            throw new IllegalArgumentException("Energy cannot be less than zero");
        }
        this.energy = energy;
    }

    /**
     * Функции ответа на вопрос, дожна быть реализованна для всех возможных вопросов в системе
     */
    @Override
    public abstract boolean answer(Question question);

    /**
     * Реализует действия которые выполняются при определенном статусе
     * Некоторые действия могут уменьшать энергию
     */
    public abstract void statusActivity();
    
    /**
     * Вункции работы мозга
     * Мозг требует энергию
     */
    public void useBrain() {
        this.localStatus = this.brain.decide(this);
        this.energy -= this.BRAIN_ENERGY_COST;
    }


    /**
     * Обновление сущности
     * Выполнения мозга и обновление позиции и уменьшениее енергии за совершенные действия
     */
    @Override
    public void updateEntity(double dt) {
        this.brainTimer += dt;
        if (this.brainTimer >= this.brainUpdateTime) {
            this.useBrain();
            this.brainTimer = 0;
        }
        this.statusActivity();
        this.energy -= SPEED_ENERGY_COST * this.getSpeed();
        this.energy -= ANGULAR_SPEED_ENERGY_COST * this.getAngularSpeed();
        super.updateEntity(dt);

    }
}
