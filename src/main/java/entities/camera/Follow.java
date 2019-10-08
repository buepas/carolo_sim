package entities.camera;

import engine.io.InputHandler;
import entities.Car;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_2;

/**
 * 3rd Person camera that will follow the car at an angle and rotate with it.
 * Left mouse to control the pitch (angle towards car) and right mouse to control the yaw (angle around car).
 * Scroll up and down to control the distance of the camera from the car.
 *
 * @author M.Nadler
 */
public class Follow extends Camera {

    private float distanceFromCar;
    private float angleAroundCar;

    /**
     * Create a new Camera that follows the car in 3rd person.
     *
     * @param car car to track
     */
    public Follow(Car car) {
        super(car);
    }

    @Override
    public void update() {
        super.update();
        calculatePitch();
        calculateZoom();
        calculateAngleAroundCar();
        float horizontalDistance = calculateHorizontalDistance();
        offsetY = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance);
        yaw = 180 - car.getRotY() + angleAroundCar;
    }

    @Override
    public void mount() {

    }

    @Override
    public void unmount() {

    }

    private void calculateCameraPosition(float horizDistance) {
        float theta = -car.getRotY() + angleAroundCar;
        offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));

        // Apply all the math to the camera position
        position.x = car.getPosition().x + offsetX;
        position.z = car.getPosition().z - offsetZ;
        position.y = car.getPosition().y + offsetY;
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromCar * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromCar * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateZoom() {
        float zoomLevel = (float) (InputHandler.getMouseScrollY() * 3f);
        distanceFromCar -= zoomLevel;

        if (distanceFromCar < 5) {
            distanceFromCar = 5;
        }
        if (distanceFromCar > 1000) {
            distanceFromCar = 1000;
        }
    }

    private void calculatePitch() {
        if (InputHandler.isMouseDown(GLFW_MOUSE_BUTTON_1)) {
            float pitchChange = (float) (InputHandler.getCursorPosDy() * 0.2f);
            pitch -= pitchChange;
            if (pitch > 60) {
                pitch = 60;
            } else if (pitch < 10) {
                pitch = 10;
            }
        }
    }

    private void calculateAngleAroundCar() {
        if (InputHandler.isMouseDown(GLFW_MOUSE_BUTTON_2)) {
            float angleChange = (float) (InputHandler.getCursorPosDx() * 0.2f);
            angleAroundCar += angleChange;
        }
    }

    @Override
    protected void resetCam() {
        distanceFromCar = 200;
        angleAroundCar = 180;

        pitch = 35;
        yaw = 0;
    }


}
