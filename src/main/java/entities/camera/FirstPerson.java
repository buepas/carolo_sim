package entities.camera;

import entities.Car;

public class FirstPerson extends Camera {


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
        position.z = car.getPosition().z + offsetZ;
        position.x = car.getPosition().x + offsetX;
        position.y = car.getPosition().y + offsetY;
        yaw = -car.getRotY()+90;
    }

    @Override
    public void update() {
        super.update();
        correctCameraPos(); // Follow player for example
    }

    @Override
    protected void resetCam() {
        pitch = 19f;
        yaw = 0;

        offsetZ = 0;
        offsetY = car.getScale().y * 1.8f;
        offsetX = 0;
    }
}
