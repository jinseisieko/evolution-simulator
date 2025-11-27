// src/test/java/io.github/jinseisieko/evolution/base/SimulationTest.java
package io.github.jinseisieko.evolution.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import io.github.jinseisieko.evolution.base.stubs.TestAgent;
import io.github.jinseisieko.evolution.basic.Point;

class SimulationTest {
    @Test
    void simulation_shouldWorkExactly() {
        Point point = new Point(0.0, 0.0);
        Agent agent = new TestAgent(point, 0.01, 3, 0.1, 0.01);
        Simulation simulation = new Simulation();
        simulation.addAgent(agent);
        simulation.update(0.1);
        assertFalse(simulation.getEntities().isEmpty());
        assertEquals(agent, simulation.getEntities().getFirst());
        assertEquals(simulation, agent.getSimulation());
        for (int i = 0; i < 1000; i++) {
            simulation.update(0.1);
        }
        assertTrue(simulation.getEntities().isEmpty());
        Agent agent1 = new TestAgent(point, 0.01, 3, 0.1, 1000);
        Agent agent2 = new TestAgent(point, 0.01, 3, 0.1, 0.01);
        simulation.addAgent(agent1);
        simulation.addAgent(agent2);
        for (int i = 0; i < 1000; i++) {
            simulation.update(0.1);
        }
        assertTrue(simulation.getEntities().isEmpty());
    }
}