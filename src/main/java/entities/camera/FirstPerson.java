package entities.camera;

import collision.CornerBox;
import engine.io.InputHandler;
import entities.Car;
import gui.Viewport;
import org.joml.Vector2f;
import sim.Sim;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

/**
 * First person camera or perspective camera.
 * Drag with the left mouse to specify a viewport. Only the active viewpoint will be recorded.
 *
 * @author M.Nadler
 */
public class FirstPerson extends Camera {

    private Viewport viewport = new Viewport();

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
        handleViewport();
    }

    @Override
    public void mount() {
        Sim.getGuiMaster().addGuis(viewport.getBarList());
    }

    @Override
    public void unmount() {
        viewport.clear();
        Sim.getGuiMaster().removeGuis(viewport.getBarList());
    }

    private void handleViewport() {
        if (InputHandler.isMousePressed(GLFW_MOUSE_BUTTON_1)) {
            viewport.startCreation(new Vector2f((float) InputHandler.getMouseX(), (float) InputHandler.getMouseY()));
        } else if (InputHandler.isMouseReleased(GLFW_MOUSE_BUTTON_1)) {
            viewport.endCreation(new Vector2f((float) InputHandler.getMouseX(), (float) InputHandler.getMouseY()));
        } else if (InputHandler.isKeyPressed(GLFW_KEY_R)) {
            viewport.clear();
        } else if (InputHandler.isMouseDown(GLFW_MOUSE_BUTTON_1)) {
            viewport.whileCreation(new Vector2f((float) InputHandler.getMouseX(), (float) InputHandler.getMouseY()));
        }
    }

    @Override
    protected void resetCam() {
        pitch = 20f;
        yaw = 0;
        offsetY = car.getScale().y * 160f;
        correctCameraPos();
    }

    public Viewport getViewport() {
        return viewport;
    }
}
