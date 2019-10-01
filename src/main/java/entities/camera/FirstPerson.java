package entities.camera;

import collision.CornerBox;
import entities.Car;
import org.joml.Vector2f;

public class FirstPerson extends Camera {

    /**
     * Create a new Camera that follows the car in first person mode.
     * This should be modeled like the camera that sits on the car.
     *
     * @param car car to track
     */
    public FirstPerson(Car car) {
        super(car);
    }

    private void correctCameraPos() {
        Vector2f carPos2D = new Vector2f(car.getPosition().x, car.getPosition().z);
        float offsetFront = 2; // offset from the center of the car towards the front of the car
        Vector2f camPos2D = new Vector2f(car.getPosition().x, car.getPosition().z - offsetFront);

        float s = (float) Math.sin(Math.toRadians(-car.getRotY()));
        float c = (float) Math.cos(Math.toRadians(-car.getRotY()));

        Vector2f newPos = CornerBox.rotatePoint(s, c, camPos2D, carPos2D);

        position.x = newPos.x;
        position.z = newPos.y;
        position.y = car.getPosition().y + offsetY;
        yaw = -car.getRotY();
    }

    @Override
    public void update() {
        super.update();
        correctCameraPos();
    }

    @Override
    protected void resetCam() {
        pitch = 20f;
        yaw = 0;
        offsetY = car.getScale().y * 160f;
        correctCameraPos();
    }
}
