// src/main/java/org/jinseisieko/evolution/Main.java
package org.jinseisieko.evolution;

import org.jinseisieko.evolution.simulation.Simulation;
import org.jinseisieko.evolution.view.SimulationView;
import org.jinseisieko.evolution.simulation.SimulationFactory;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // SwingUtilities.invokeLater(() -> {
        //     JFrame frame = new JFrame("Эволюционный симулятор");
        //     Simulation simulation = SimulationFactory.create();
        //     SimulationView view = new SimulationView(simulation, frame);

        //     frame.add(view);
        //     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //     frame.setResizable(false);
        //     frame.pack();
        //     frame.setLocationRelativeTo(null);
        //     frame.setVisible(true);
        // });
    }
}