// src/test/java/org/jinseisieko/evolution/base/AgentTest.java
package org.jinseisieko.evolution.base;

import org.jinseisieko.evolution.basic.Point;
import org.jinseisieko.evolution.base.stubs.AdaptiveBrain;
import org.jinseisieko.evolution.base.stubs.EnergyAwareAgent;
import org.jinseisieko.evolution.base.stubs.FirstStatus;
import org.jinseisieko.evolution.base.stubs.SecondStatus;
import org.jinseisieko.evolution.base.stubs.SimpleBrain;
import org.jinseisieko.evolution.base.stubs.TestAgent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Agent} abstract class, verified through a concrete subclass {@link TestAgent}.
 * <p>
 * This test suite validates the integration of core agent behaviors:
 * <ul>
 *   <li>Initialization of physical and decision-making state (position, energy, brain)</li>
 *   <li>Correct interaction between {@code Brain}, {@code Answerer}, and {@code Status}</li>
 *   <li>Energy consumption during brain activation</li>
 *   <li>Behavioral response to status via {@code statusActivity()}</li>
 *   <li>Proper timing and triggering of brain updates in {@code updateEntity(dt)}</li>
 * </ul>
 * The {@code TestAgent} implements a simple logic:
 * <ul>
 *   <li>Answers questions based on an internal integer {@code number}</li>
 *   <li>Sets speed to +10 for {@code FirstStatus}, -10 for {@code SecondStatus}</li>
 * </ul>
 *
 * @author jinseisieko
 */
class AgentTest {

    /**
     * Verifies the end-to-end behavior of an {@code Agent} subclass across multiple cycles:
     * <ol>
     *   <li>Initial state is correctly set (position, dynamics, energy = 1.0)</li>
     *   <li>Manual {@code useBrain()} consumes energy and sets correct {@code Status}</li>
     *   <li>Changing internal state alters brain decision outcome</li>
     *   <li>{@code statusActivity()} correctly maps status to speed</li>
     *   <li>{@code updateEntity(dt)} triggers brain update when {@code brainUpdateTime} is exceeded</li>
     *   <li>Energy is consumed only when brain is actually used</li>
     * </ol>
     * This test ensures that the abstract {@code Agent} correctly orchestrates brain usage,
     * status interpretation, and physical state updates in a time-aware manner.
     */
    @Test
    void testAgent_worksExactly() {
        Point point = new Point(0.0, 0.0);
        TestAgent ta = new TestAgent(point, 0.01, 3, 0.1, 0.01);
        assertTrue(ta.getBrain() instanceof SimpleBrain);
        assertEquals(0.0, ta.getX(), 1e-12);
        assertEquals(0.0, ta.getY(), 1e-12);
        assertEquals(0.0, ta.getSpeed(), 1e-12);
        assertEquals(0.0, ta.getAcceleration(), 1e-12);
        assertEquals(0.0, ta.getAngle(), 1e-12);
        assertEquals(0.0, ta.getAngularSpeed(), 1e-12);

        // statusActivity should not change physical state before brain is used
        ta.statusActivity();
        assertEquals(0.0, ta.getSpeed(), 1e-12);

        // First brain use: number=3 < 10 → FirstStatus, energy -= 0.01
        ta.useBrain();
        assertEquals(0.99, ta.getEnergy(), 1e-12);
        assertTrue(ta.getLocalStatus() instanceof FirstStatus);

        // Change state to flip decision
        ta.setNumber(10); // now 10 >= 10 → SecondStatus
        ta.useBrain();
        assertEquals(0.98, ta.getEnergy(), 1e-12);
        assertTrue(ta.getLocalStatus() instanceof SecondStatus);

        // Set number back to trigger FirstStatus, then updateEntity just over threshold
        ta.setNumber(2);
        ta.updateEntity(0.1 + 1e-12); // should trigger brain update
        assertEquals(10.0, ta.getSpeed(), 1e-12); // from FirstStatus
        assertTrue(ta.getLocalStatus() instanceof FirstStatus);

        // Partial update: no brain use, but statusActivity applies
        ta.updateEntity(0.05);
        assertEquals(0.97, ta.getEnergy(), 1e-12); // brain used again

        // Flip to SecondStatus via updateEntity
        ta.setNumber(100);
        ta.updateEntity(0.05 + 1e-12);
        assertEquals(0.96, ta.getEnergy(), 1e-12);
        assertTrue(ta.getLocalStatus() instanceof SecondStatus);
        assertEquals(-10.0, ta.getSpeed(), 1e-12); // from SecondStatus

        for (int i = 0; i < 10000; i++) {
            ta.updateEntity(0.1);
            if (!ta.isAlive()) break;
        }

        assertFalse(ta.isAlive());
    }

    /**
     * Verifies the behavior of an {@code EnergyAwareAgent}
     * to make decisions while continuously consuming energy over time.
     * <p>
     * This test ensures that:
     * <ol>
     *   <li>The agent is correctly initialized with zero physical state and full energy (1.0)</li>
     *   <li>Calling {@code statusActivity()} before any brain activation has no effect on physical properties</li>
     *   <li>Repeated calls to {@code updateEntity(dt)} gradually consume energy according to the internal logic</li>
     *   <li>The agent remains alive (energy > 0) during a short simulation (30 steps of 0.1 time units)</li>
     *   <li>After a sufficiently long simulation (3000 additional steps), the agent's energy drops to zero or below</li>
     * </ol>
     */
    @Test
    void energyAwareAgent_worksExactly() {
        Point point = new Point(0.0, 0.0);
        EnergyAwareAgent eaa = new EnergyAwareAgent(point, 0.01);
        assertTrue(eaa.getBrain() instanceof AdaptiveBrain);
        assertEquals(0.0, eaa.getX(), 1e-12);
        assertEquals(0.0, eaa.getY(), 1e-12);
        assertEquals(0.0, eaa.getSpeed(), 1e-12);
        assertEquals(0.0, eaa.getAcceleration(), 1e-12);
        assertEquals(0.0, eaa.getAngle(), 1e-12);
        assertEquals(0.0, eaa.getAngularSpeed(), 1e-12);

        eaa.statusActivity();
        assertTrue(eaa.getBrain() instanceof AdaptiveBrain);
        assertEquals(0.0, eaa.getX(), 1e-12);
        assertEquals(0.0, eaa.getY(), 1e-12);
        assertEquals(0.0, eaa.getSpeed(), 1e-12);
        assertEquals(0.0, eaa.getAcceleration(), 1e-12);
        assertEquals(0.0, eaa.getAngle(), 1e-12);
        assertEquals(0.0, eaa.getAngularSpeed(), 1e-12);

        for (int i = 0; i < 30; i++) {
            eaa.updateEntity(0.1);
        }
        
        assertTrue(eaa.getEnergy() > 0);

        for (int i = 0; i < 3000; i++) {
            eaa.updateEntity(0.1);
            if (!eaa.isAlive()) break;
        }

        assertFalse(eaa.isAlive());
    }
}