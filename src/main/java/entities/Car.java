package entities;

import engine.io.InputHandler;
import engine.models.RawModel;
import engine.models.TexturedModel;
import engine.render.objconverter.ObjFileLoader;
import engine.textures.ModelTexture;
import org.joml.Vector3f;
import sim.Sim;

import static org.lwjgl.glfw.GLFW.*;

public class Car extends Entity {

    private static TexturedModel carModel;
    private Vector3f direction = new Vector3f(1,0, 0);
    private static float scale = 5;
    private float speed;
    private float topSpeed = scale * 1.5f;
    private float rotAngleDeg = 40; // max rotation per second in degrees
    private float rotAngleRad = (float) Math.toRadians(rotAngleDeg);

    public Car(Vector3f position, float rotX, float rotY, float rotZ) {
        super(carModel, new Vector3f(position.x, scale, position.z), rotX, rotY, rotZ, scale);
    }

    public static void init() {
        loadCarModel();
    }

    private static void loadCarModel() {
        RawModel rawPlayer = Sim.loader.loadToVao(ObjFileLoader.loadObj("block"));
        carModel = new TexturedModel(rawPlayer, new ModelTexture(Sim.loader.loadTexture("red")));
    }

    public void update() {
        checkInputs();
        Vector3f velocity = new Vector3f();
        direction.mul(speed, velocity);
        increasePosition(velocity);
    }

    /**
     * Check for Keyboard and Mouse inputs and process them
     *
     */
    private void checkInputs() {

        if (InputHandler.isKeyDown(GLFW_KEY_W) && InputHandler.isKeyDown(GLFW_KEY_S)) {
            return;
        }

        if (InputHandler.isKeyDown(GLFW_KEY_W)) {
            if (speed < topSpeed) {
                speed += .3 * scale * Sim.dt();
                if (speed > topSpeed) {
                    speed = topSpeed;
                }
            }
        }

        if (InputHandler.isKeyDown(GLFW_KEY_S)) {
            if (speed > 0) {
                speed -= .6 * scale * Sim.dt();
                if (speed < 0) {
                    speed = 0;
                }
            }
        }

        if (InputHandler.isKeyDown(GLFW_KEY_A) && InputHandler.isKeyDown(GLFW_KEY_D)) {
            return;
        }

        if (InputHandler.isKeyDown(GLFW_KEY_A)) {
            float rotRad = (float) (rotAngleRad * Sim.dt());
            float rotDeg = (float) (rotAngleDeg * Sim.dt());
            direction.rotateY(rotRad, direction);
            increaseRotation(0, rotDeg,0);
        }

        if (InputHandler.isKeyDown(GLFW_KEY_D)) {
            float rotRad = -(float) (rotAngleRad * Sim.dt());
            float rotDeg = -(float) (rotAngleDeg * Sim.dt());
            direction.rotateY(rotRad, direction);
            increaseRotation(0, rotDeg,0);
        }
    }
}
