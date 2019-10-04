package entities.camera;

import entities.Car;

public class BirdsEye extends Camera {

    /**
     * Create a new Camera that follows the car in fixed bird's eye (top down) perspective.
     *
     * @param car car to track
     */
    public BirdsEye(Car car) {
        super(car);
    }

    @Override
    public void update() {
        super.update();
        position.z = car.getPosition().z + offsetZ;
        position.x = car.getPosition().x + offsetX;
        position.y = car.getPosition().y + offsetY;
    }

    @Override
    public void mount() {

    }

    @Override
    public void unmount() {

    }

    @Override
    protected void resetCam() {
        pitch = 90;
        yaw = 0;

        offsetZ = 0;
        offsetY = 400;
        offsetX = 0;
    }
}
