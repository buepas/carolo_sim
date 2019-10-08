package entities.camera;

import engine.io.InputHandler;
import entities.Car;
import org.joml.Vector3f;
import sim.Sim;

import static org.lwjgl.glfw.GLFW.*;
import static sim.Sim.Stage.PLAYING;

/**
 * The Abstract Camera Class to derive different views.
 * Will always follow a car and define at least position, pitch and yaw.
 * Those variables are needed to calculate the View Matrix for the renderer.
 *
 * @author M.Nadler
 */
public abstract class Camera {

    // This is used by ALL camera types
    Vector3f position = new Vector3f(0, 0, 0);
    float pitch;
    float yaw;
    final Car car;

    // Offsets are used by some (multiple) camera types so we define them here
    float offsetX;
    float offsetY;
    float offsetZ;

    /**
     * Create a new Camera to track a car in a certain manner.
     *
     * @param car car to track
     */
    public Camera(Car car) {
        this.car = car;
        resetCam();
    }

    /**
     * Update function. Call this every frame to update position and transformation of camera.
     */
    public void update() {
        if (Sim.getActiveStages().size() == 1 && Sim.getActiveStages().get(0) == PLAYING) {
            isReset();
        }

        if(InputHandler.isKeyPressed(GLFW_KEY_1)) {
            Sim.switchCamera(1);
        } else if(InputHandler.isKeyPressed(GLFW_KEY_2)) {
            Sim.switchCamera(2);
        } else if(InputHandler.isKeyPressed(GLFW_KEY_3)) {
            Sim.switchCamera(3);
        } else if(InputHandler.isKeyPressed(GLFW_KEY_Q)) {
            Sim.switchCamera();
        }
    }

    /**
     * Check if a reset is requested by the player and reset the camera.
     */
    private void isReset() {
        if (InputHandler.isKeyPressed(GLFW_KEY_R)) {
            resetCam();
        }
    }

    public abstract void mount();

    public abstract void unmount();

    protected abstract void resetCam();

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

}
