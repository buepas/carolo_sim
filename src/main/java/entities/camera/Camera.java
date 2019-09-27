package entities.camera;

import engine.io.InputHandler;
import entities.Car;
import org.joml.Vector3f;
import sim.Sim;
import util.Maths;

import static org.lwjgl.glfw.GLFW.*;
import static sim.Sim.Stage.PLAYING;

/**
 * The main camera of the game that stays around the player character. It can be - moved (pan):
 * Middle Mouse Button or Arrow Keys - tilted (pitch): Left Mouse Button - turned (yaw): Right Mouse
 * Button - zoomed: Scroll Wheel - reset: R
 *
 * <p>All the movement options have maximum and minimum values to keep the character somewhat in
 * frame. If more than one button is pressed at the same time, the camera can be moved along all
 * selected axes
 *
 * <p>Currently we only have one camera, but support for multiple cameras is definitely possible and
 * relatively easy to accomplish.
 *
 * <p>This object will be used in a lot of places to calculate the transformation of the World
 * Coordinates. See here: {@link Maths#createViewMatrix(Camera)}
 */
public abstract class Camera {

    protected static Vector3f position = new Vector3f(0, 0, 0);
    protected float pitch;
    protected float yaw;
    protected final Car car;
    protected float panSpeed = 20;
    protected float offsetX;
    protected float offsetY;
    protected float offsetZ;
    protected float minZoom = -999;
    protected float maxZoom = 999;
    protected float maxOffsetX = 999;
    protected float maxOffsetY = 999;
    protected float maxPitch = 999;
    protected float minPitch = -999;
    private float roll; // Not used right now, but we might

    /**
     * Create a new Camera.
     *
     * @param car car to track
     */
    public Camera(Car car) {
        this.car = car;
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
        } else if(InputHandler.isKeyPressed(GLFW_KEY_Q)) {
            Sim.switchCamera();
        }

    }

    /**
     * Check if a reset is requested by the player and reset the camera.
     */
    private void isReset() {
        if (InputHandler.isKeyDown(GLFW_KEY_R)) {
            resetCam();
        }
    }

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

    public float getRoll() {
        return roll;
    }

}
