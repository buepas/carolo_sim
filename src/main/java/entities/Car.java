package entities;

import collision.PenaltyCalculator;
import engine.io.InputHandler;
import engine.models.RawModel;
import engine.models.TexturedModel;
import engine.render.objconverter.ObjFileLoader;
import engine.textures.ModelTexture;
import org.joml.Vector3f;
import sim.Sim;

import static org.lwjgl.glfw.GLFW.*;

/**
 * A simulated car that can be controlled with WASD.
 * Car specific settings are hardcoded into this class.
 *
 * @author M.Nadler
 */
public class Car extends Entity {

    private static TexturedModel carModel;
    private Vector3f direction = new Vector3f(0,0, -1);
    private final static float scale = 0.04f;
    private float speed;
    private final float topSpeed = scale * 120;
    private final float rotAngleDeg = 45; // max rotation per second in degrees
    private final float rotAngleRad = (float) Math.toRadians(rotAngleDeg);
    private PenaltyCalculator penaltyCalculator;

    public Car(Vector3f position, float rotX, float rotY, float rotZ) {
        super(carModel, new Vector3f(position.x, scale, position.z), rotX, rotY, rotZ, scale);
        penaltyCalculator = new PenaltyCalculator(this, Sim.getMapLoader());
    }

    public static void init() {
        loadCarModel();
    }

    private static void loadCarModel() {
        RawModel rawPlayer = Sim.loader.loadToVao(ObjFileLoader.loadObj("car"));
        carModel = new TexturedModel(rawPlayer, new ModelTexture(Sim.loader.loadTexture("red")));
    }

    public void update() {
        checkInputs();
        increasePosition(new Vector3f(direction).mul(speed));
        penaltyCalculator.updatePenalty(Sim.getInfoWindow());
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
                speed += 40 * scale * Sim.dt();
                if (speed > topSpeed) {
                    speed = topSpeed;
                }
                Sim.getInfoWindow().updateSpeed(speed);
            }
        }

        if (InputHandler.isKeyDown(GLFW_KEY_S)) {
            if (speed > 0) {
                speed -= 80 * scale * Sim.dt();
                if (speed < 0) {
                    speed = 0;
                }
                Sim.getInfoWindow().updateSpeed(speed);
            }
        }

        if (InputHandler.isKeyDown(GLFW_KEY_A) && InputHandler.isKeyDown(GLFW_KEY_D)) {
            return;
        }

        if (InputHandler.isKeyDown(GLFW_KEY_A)) {
            float rotRad = (float) (rotAngleRad * Sim.dt());
            float rotDeg = (float) (rotAngleDeg * Sim.dt());
            direction.rotateY(rotRad, direction);
            setRotY(getRotY() + rotDeg);
            Sim.getInfoWindow().updateRotation(getRotY());
        }

        if (InputHandler.isKeyDown(GLFW_KEY_D)) {
            float rotRad = -(float) (rotAngleRad * Sim.dt());
            float rotDeg = -(float) (rotAngleDeg * Sim.dt());
            direction.rotateY(rotRad, direction);
            setRotY(getRotY() + rotDeg);
            Sim.getInfoWindow().updateRotation(getRotY());
        }
    }

    public PenaltyCalculator getPenaltyCalculator() {
        return penaltyCalculator;
    }
}
