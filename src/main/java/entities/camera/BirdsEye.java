package entities.camera;

import engine.io.InputHandler;
import entities.Car;
import sim.Sim;
import util.Maths;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_2;

public class BirdsEye extends Camera {


    /**
     * Create a new Camera.
     *
     * @param car car to track
     */
    public BirdsEye(Car car) {
        super(car);
        System.out.println(pitch);
        resetCam();
        System.out.println(pitch);
    }

    protected void correctCameraPos() {
        position.z = car.getPosition().z + offsetZ;
        position.x = car.getPosition().x + offsetX;
        position.y = car.getPosition().y + offsetY;
    }

    @Override
    public void update() {
        super.update();
        calculateZoom();
        calculatePitch();
        calculateYaw();
        calculatePan();
        correctCameraPos(); // Follow player for example
    }

    /**
     * Update pan for the current frame. Camera can be panned with the arrow keys or by holding down
     * the middle mouse button and moving the mouse This will update camera position and make sure the
     * pan never goes above the maximum distance from the character
     */
    private void calculatePan() {
        float speed = (float) (panSpeed * Sim.dt()); // panSpeed is in seconds, so
        // we multiply by frame delta
        if (InputHandler.isKeyDown(GLFW_KEY_LEFT)) {
            offsetX -= speed;
        } else if (InputHandler.isKeyDown(GLFW_KEY_RIGHT)) {
            offsetX += speed;
        } else if (InputHandler.isKeyDown(GLFW_KEY_UP)) {
            offsetY += speed;
        } else if (InputHandler.isKeyDown(GLFW_KEY_DOWN)) {
            offsetY -= speed;
        }

        if (InputHandler.isMouseDown(GLFW_MOUSE_BUTTON_1)) {
            offsetX += (float) (InputHandler.getCursorPosDx() * 0.1f);
            offsetY -= (float) (InputHandler.getCursorPosDy() * 0.1f);
        }

        if (offsetX > maxOffsetX) {
            offsetX = maxOffsetX;
        } else if (offsetX < -maxOffsetX) {
            offsetX = -maxOffsetX;
        } else if (offsetY > maxOffsetY) {
            offsetY = maxOffsetY;
        } else if (offsetY < -maxOffsetY) {
            offsetY = -maxOffsetY;
        }
    }

    /**
     * Camera can be zoomed in and out by scrolling the mouse wheel. This will update camera position
     * and make sure the zoom never goes above or below the allowed distances
     */
    private void calculateZoom() {
        float zoomLevel = (float) (InputHandler.getMouseScrollY() * 2f);
        offsetZ -= zoomLevel;
        if (offsetZ < minZoom) {
            offsetZ = minZoom;
        }
        if (offsetZ > maxZoom) {
            offsetZ = maxZoom;
        }
    }

    /**
     * Camera can be pitched up and down while holding the left mouse button. This will update update
     * and bound the pitch. "Updating" of the position based on pitch is done in {@link
     * Maths#createViewMatrix(Camera)}
     *
     * <p>Pitch is also used for Ray Casting calculations.
     */
    private void calculatePitch() {
        if (InputHandler.isMouseDown(GLFW_MOUSE_BUTTON_2)) {
            float pitchChange = (float) (InputHandler.getCursorPosDy() * 0.2f);
            pitch += pitchChange;
            if (pitch > maxPitch) {
                pitch = maxPitch;
            } else if (pitch < minPitch) {
                pitch = minPitch;
            }
        }
    }

    /**
     * Camera can be yawed left and right while holding the right mouse button. This will update
     * update and bound the yaw. "Updating" of the position based on yaw is done in {@link
     * Maths#createViewMatrix(Camera)}
     *
     * <p>Yaw is also used for Ray Casting calculations.
     */
    private void calculateYaw() {
        if (InputHandler.isMouseDown(GLFW_MOUSE_BUTTON_2)) {
            float yawChange = (float) (InputHandler.getCursorPosDx() * 0.2f);
            yaw += yawChange;
            if (yaw < -75) {
                yaw = -75;
            } else if (yaw > 75) {
                yaw = 75;
            }
        }
    }

    @Override
    protected void resetCam() {
        pitch = 90;
        yaw = 0;
        System.out.println(pitch);

        offsetZ = 0;
        offsetY = 600;
        offsetX = 0;
    }
}
