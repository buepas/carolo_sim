package engine.io;

import sim.Sim;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import static org.lwjgl.glfw.GLFW.glfwFocusWindow;

/**
 * Window to display infos, stats and controls for/about the running simulation.
 *
 * @author M.Nadler
 */
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

        // Set up frame and panel
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
        rotLabel = new JLabel("0.00° direction angle",SwingConstants.LEFT);
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
                // Immediately switch to the render window so you can drive
                glfwFocusWindow(Sim.renderWindow.getWindow());

                // Start/Stop the recording
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


        // Pack and display frame
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setLocation(100, 400);
        frame.setVisible(true);

    }

    /**
     * Update the fps display in the Info Window.
     *
     * @param newFps new FPS to display
     */
    public void updateFPS(int newFps) {
        fps = Integer.toString(newFps);
        fpsLabel.setText(fps + " frames / s");
    }

    /**
     * Update the speed display in the Info Window.
     *
     * @param newSpeed new speed to display
     */
    public void updateSpeed(float newSpeed) {
        speed = String.format("%.2f", newSpeed);
        speedLabel.setText(speed + " units / s");
    }

    /**
     * Update the rotation display in the Info Window.
     *
     * @param newRotation new angle to display
     */
    public void updateRotation(float newRotation) {
        rotation = String.format("%.2f", newRotation);
        rotLabel.setText(rotation + "° direction angle");
    }

    /**
     * Update the penalty display in the Info Window.
     *
     * @param newPenalty new penalty to display
     */
    public void updatePenalty(int newPenalty) {
        penalty = Integer.toString(newPenalty);
        penaltyLabel.setText(penalty + " penalty points");
    }

}
