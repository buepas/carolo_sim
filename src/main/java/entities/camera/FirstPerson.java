package entities.camera;

import collision.CornerBox;
import entities.Car;
import org.joml.Vector2f;

public class FirstPerson extends Camera {

    private float offsetFront = 2;

    /**
     * Create a new Camera.
     *
     * @param car car to track
     */
    public FirstPerson(Car car) {
        super(car);
        resetCam();
    }

    protected void correctCameraPos() {
        Vector2f carPos2D = new Vector2f(car.getPosition().x, car.getPosition().z);
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
