// src/main/java/io.github/jinseisieko/evolution/Main.java
package io.github.jinseisieko.evolution;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import io.github.jinseisieko.evolution.base.DrawableBasicSimulation;
import io.github.jinseisieko.evolution.bobsimulation.BobSimulation;
import io.github.jinseisieko.evolution.view.SimulationView;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Эволюционный симулятор");
            DrawableBasicSimulation simulation = BobSimulation.create();
            SimulationView view = new SimulationView(simulation, frame);

            frame.add(view);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}