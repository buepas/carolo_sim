package engine.io;

import sim.Sim;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class InfoWindow {

    private JFrame frame;

    private String fps;
    private JLabel fpsLabel;

    private String speed;
    private JLabel speedLabel;

    private String rotation;
    private JLabel rotLabel;

    private String penalty;
    private JLabel penaltyLabel;

    public void createWindow() {
        frame = new JFrame("Info / Stats");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout( new GridLayout( 7 , 1, 0, 0 ) );  // 2 rows 1 column

        // Info Label
        JLabel infoLabel = new JLabel("Carolo Simulation Live Feed", SwingConstants.LEFT);
        infoLabel.setPreferredSize(new Dimension(300, 30));
        infoLabel.setBorder(new EmptyBorder(10,10,10,10));
        panel.add(infoLabel);

        // FPS Label
        fpsLabel = new JLabel("0 frames / s",SwingConstants.LEFT);
        fpsLabel.setBorder(new EmptyBorder(0,10,0,10));
        panel.add(fpsLabel);

        // Speed Label
        speedLabel = new JLabel("0.00 units / s",SwingConstants.LEFT);
        speedLabel.setBorder(new EmptyBorder(0,10,0,10));
        panel.add(speedLabel);

        // Speed Label
        rotLabel = new JLabel("0.00 degrees of direction",SwingConstants.LEFT);
        rotLabel.setBorder(new EmptyBorder(0,10,0,10));
        panel.add(rotLabel);

        // Penalty Label
        penaltyLabel = new JLabel("0 penalty points",SwingConstants.LEFT);
        penaltyLabel.setBorder(new EmptyBorder(0,10,0,10));
        panel.add(penaltyLabel);

        // Recording Button
        JButton recordingButton = new JButton("Start Recording");
        recordingButton.setBounds(50, 375, 250, 50);

        recordingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                if (Sim.isRecording()) {
                    Sim.setRecording(false);
                    recordingButton.setText("Start Recording");
                } else {
                    Sim.setRecording(true);
                    recordingButton.setText("Stop Recording");
                }
            }
        });
        panel.add(recordingButton);

        // Stop Button
        JButton stopButtton = new JButton("Exit Simulation");
        stopButtton.setBounds(50, 375, 250, 50);

        stopButtton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                Sim.renderWindow.stop();
            }
        });
        panel.add(stopButtton);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setLocation(100, 400);
        frame.setVisible(true);

    }

    public void updateFPS(int newFps) {
        fps = Integer.toString(newFps);
        fpsLabel.setText(fps + " frames / s");
    }

    public void updateSpeed(float newSpeed) {
        speed = String.format("%.2f", newSpeed);
        speedLabel.setText(speed + " units / s");
    }

    public void updateRotation(float newRotation) {
        rotation = String.format("%.2f", newRotation);
        rotLabel.setText(rotation + " degrees of direction");
    }

    public void updatePenalty(float newPenalty) {
        penalty = String.format("%.2f", newPenalty);
        penaltyLabel.setText(penalty + " penalty points");
    }

}
