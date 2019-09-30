package sim;

import collision.BoxMaster;
import engine.io.InfoWindow;
import engine.io.InputHandler;
import engine.io.Screenshot;
import engine.io.RenderWindow;
import engine.render.Loader;
import engine.render.MasterRenderer;
import engine.render.fontrendering.TextMaster;
import entities.camera.BirdsEye;
import entities.camera.Camera;
import entities.Car;
import entities.Entity;
import entities.camera.FirstPerson;
import entities.light.LightMaster;
import org.joml.Vector3f;
import sim.stages.Playing;
import terrain.MapLoader;
import terrain.Terrain;
import terrain.TerrainMaster;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static sim.Sim.Stage.PLAYING;

public class Sim extends Thread {

    public static RenderWindow renderWindow = new RenderWindow(1080, 600, 30, "Carolo Sim");
    private static InfoWindow infoWindow;
    public static Loader loader = new Loader();
    private MasterRenderer renderer;

    public static BirdsEye birdsEye;
    public static FirstPerson firstPerson;
    public static Camera camera;
    private static boolean recording;

    private static ArrayList<Stage> activeStages = new ArrayList<>();
    private static double dt;

    private static final List<Entity> entities = new CopyOnWriteArrayList<>();

    private static Car car;
    private static BoxMaster boxMaster;

    private static Terrain terrain;
    private static MapLoader mapLoader;

    private static String mapName;



    /**
     * Here we initialize all the Masters and other classes and generate the world.
     */
    @Override
    public void run() {
        this.setName("Game Loop"); // Set thread name

        mapName = "road6";

        // Create GLFW Window
        renderWindow.create();

        // Initiate the master renderer class
        renderer = new MasterRenderer();

        // Load and Generate Terrain
        mapLoader = new MapLoader(mapName);
        TerrainMaster.init();
        terrain = TerrainMaster.generateTerrain(mapName);

        // Init entities
        Car.init();

        // Initialize Info Window
        infoWindow = new InfoWindow();
        infoWindow.createWindow();

        // Load basic lights
        LightMaster.reset();

        car = new Car(new Vector3f(70,1,400), 0, 0, 0);
//        Entity block = new Entity (
//                new TexturedModel(
//                        Sim.loader.loadToVao(ObjFileLoader.loadObj("block")),
//                        new ModelTexture(Sim.loader.loadTexture("black"))
//                ),
//                new Vector3f(10,0.5f,10), 0,0,0, 0.1f
//        );
//
//        entities.add(block);

        // Init Stuff

        TextMaster.init();
        birdsEye = new BirdsEye(car);
        firstPerson = new FirstPerson(car);
        camera = birdsEye;

        // Collision
        boxMaster = new BoxMaster(mapName);

        activeStages.add(PLAYING);

    /*
    **************************************************************
    ---------HERE STARTS THE GAME LOOP!
    **************************************************************
    */

        // Variables for Time Invariance and Frame Rate control
        double timePerFrame = 1000 / 30.0;
        double secondTimer = 0;
        int frames = 0;
        double frameStartTime;
        while (!renderWindow.isClosed()) {

            // Note when we start the frame to calculate the duration later
            frameStartTime = System.nanoTime();

            // This will be true exactly once per second, independent of frame rate
            // Used for actions that need to be done infrequently. oncePerSecond can be used anywhere
            if (secondTimer > 1e9) {
//                oncePerSecond = true;
                secondTimer -= 1e9;
                infoWindow.updateFPS(frames);
                frames = 0;
            }

            // check each stage in sequence and render it if active
            // Order matters, later stages will be rendered on top of earlier stages


            /*InputHandler needs to be BEFORE polling (window.update()) so we still have access to
            the events of last Frame. Everything else should be after polling.*/
            InputHandler.update();
            renderWindow.update();

            if(recording) {
                new Screenshot();
            }

            if (activeStages.contains(PLAYING)) {
                Playing.update(renderer);
            }


            // Done with one frame

            renderWindow.swapBuffers();
//            oncePerSecond = false;

            // Calculate how long the current frame took to process
            double frameTime = (System.nanoTime() - frameStartTime) / 1e6;

            // If it was less than the allowed time, wait the rest
            if (frameTime < timePerFrame) {
                try {
                    Thread.sleep((long) (timePerFrame - frameTime));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Save the time needed for this frame to be used in the next frame
            dt = (System.nanoTime() - frameStartTime) / 1e9;

            // Count actual fps and keep a running timer
            frames++;
            secondTimer += (System.nanoTime() - frameStartTime);
        }

    /*
    **************************************************************
    ---------HERE ENDS THE GAME LOOP!
    **************************************************************
    */

        // Clean up memory and unbind openGL shader programs
        cleanUp();

        // Close and disconnect (still need a window close callback)
        renderWindow.kill();

    }

    private void cleanUp() {
        // Clean up memory and unbind openGL shader programs
        TextMaster.cleanUp();
//        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
    }

    public static List<Entity> getEntities() {
        return entities;
    }

    public static InfoWindow getInfoWindow() {
        return infoWindow;
    }

    public static Camera getActiveCamera() {
        return camera;
    }

    public static void switchCamera(int number) {
        if(number == 1) {
            camera = birdsEye;
        } else if(number == 2) {
            camera = firstPerson;
        }
    }

    public static void switchCamera() {
        if(camera instanceof BirdsEye) {
            camera = firstPerson;
        } else {
            camera = birdsEye;
        }
    }

    public static void setRecording(boolean recording) {
        Sim.recording = recording;
    }

    public static boolean isRecording() {
        return recording;
    }

    public static Car getCar() {
        return car;
    }

    public static ArrayList<Stage> getActiveStages() {
        return activeStages;
    }

    public static double dt() {
        return dt;
    }

    public static Terrain getTerrain() {
        return terrain;
    }

    public static BoxMaster getBoxMaster() {
        return boxMaster;
    }

    public static MapLoader getMapLoader() {
        return mapLoader;
    }

    // Valid Stages
    public enum Stage {
        PLAYING
    }


}
