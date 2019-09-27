package sim;

import engine.io.InputHandler;
import engine.io.Window;
import engine.models.RawModel;
import engine.models.TexturedModel;
import engine.render.Loader;
import engine.render.MasterRenderer;
import engine.render.fontrendering.TextMaster;
import engine.render.objconverter.ObjFileLoader;
import engine.textures.ModelTexture;
import entities.camera.BirdsEye;
import entities.camera.Camera;
import entities.Car;
import entities.Entity;
import entities.camera.FirstPerson;
import entities.light.LightMaster;
import org.joml.Vector3f;
import sim.stages.Playing;
import terrain.Terrain;
import terrain.TerrainMaster;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static sim.Sim.Stage.PLAYING;

public class Sim extends Thread {

    public static Window window = new Window(1080, 600, 30, "Carolo Sim");
    public static Loader loader = new Loader();
    private MasterRenderer renderer;

    public static BirdsEye birdsEye;
    public static FirstPerson firstPerson;
    public static Camera camera;

    private static List<Stage> activeStages = new CopyOnWriteArrayList<>();
    private static double dt;

    private static final List<Entity> entities = new CopyOnWriteArrayList<>();

    private static Car car;

    private static Terrain terrain;



    /**
     * Here we initialize all the Masters and other classes and generate the world.
     */
    @Override
    public void run() {
        this.setName("Game Loop"); // Set thread name

        // Create GLFW Window
        window.create();

        // Initiate the master renderer class
        renderer = new MasterRenderer();

        // Init entities
        Car.init();

        // Load basic lights
        LightMaster.reset();

//        Fps fpsCounter = new Fps();

        car = new Car(new Vector3f(20,0.5f,800), 0, 0, 0);
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
        TerrainMaster.init();
        TextMaster.init();
        birdsEye = new BirdsEye(car);
        System.out.println(birdsEye.getPitch());
        firstPerson = new FirstPerson(car);
        System.out.println(birdsEye.getPitch());
        camera = birdsEye;

        System.out.println(getActiveCamera().getPitch());

        terrain = TerrainMaster.generateTerrain("road3");

        activeStages.add(PLAYING);

    /*
    **************************************************************
    ---------HERE STARTS THE GAME LOOP!
    **************************************************************
    */

        // Variables for Time Invariance and Frame Rate control
        double timePerFrame = 1000 / 60.0;
        double secondTimer = 0;
        int frames = 0;
        double frameStartTime;
        while (!window.isClosed()) {

            // Note when we start the frame to calculate the duration later
            frameStartTime = System.nanoTime();

            // This will be true exactly once per second, independent of frame rate
            // Used for actions that need to be done infrequently. oncePerSecond can be used anywhere
            if (secondTimer > 1e9) {
//                oncePerSecond = true;
                secondTimer -= 1e9;
//                fpsCounter.updateString("" + frames); // Display Frame counter
                frames = 0;
            }

            // check each stage in sequence and render it if active
            // Order matters, later stages will be rendered on top of earlier stages


            /*InputHandler needs to be BEFORE polling (window.update()) so we still have access to
            the events of last Frame. Everything else should be after polling.*/
            InputHandler.update();
            window.update();

            // Toggle mute sound

            if (activeStages.contains(PLAYING)) {
                Playing.update(renderer);
            }


            // Done with one frame

            window.swapBuffers();
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
        window.kill();

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

    public static Car getCar() {
        return car;
    }

    public static List<Stage> getActiveStages() {
        return activeStages;
    }

    public static double dt() {
        return dt;
    }

    public static Terrain getTerrain() {
        return terrain;
    }

    // Valid Stages
    public enum Stage {
        PLAYING
    }


}
