package sim.stages;

import engine.io.InputHandler;
import engine.render.Loader;
import engine.render.MasterRenderer;
import engine.render.fontrendering.TextMaster;
import entities.Entity;
import entities.light.LightMaster;
import sim.Sim;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

/**
 * MAIN GAME LOOP specification and rendering. Contains and manages the Game Loop while the player
 * is playing the game. All the rendering and updating is done here.
 */
public class Playing {

  private static boolean firstloop = true;

  /**
   * * Initialize Game Menu. Will load the texture files and other GUI elements needed for this
   * stage. This needs to be called once before using the stage.
   *
   * @param loader main loader
   */
  public static void init(Loader loader) {

  }

  /**
   * Game Loop. This runs every frame as long as the payer is playing the game. Include all
   * rendering and input handling here.
   *
   * @param renderer master renderer from game loop
   */
  public static void update(MasterRenderer renderer) {
    if (firstloop) {
      TextMaster.removeAll();
      firstloop = false;
    }

    double x = 2 * (InputHandler.getMouseX() / Sim.window.getWidth()) - 1;
    double y = 1 - 2 * (InputHandler.getMouseY() / Sim.window.getHeight());

//    List<GuiTexture> guis = new ArrayList<>();


    // ESC = Game Menu
    if (InputHandler.isKeyPressed(GLFW_KEY_ESCAPE)) {
//      Sim.addActiveStage(Sim.Stage.GAMEMENU);
      Sim.window.stop();
    }


    // Update positions of camera, player and 3D Mouse Pointer
    Sim.getCar().update();
    Sim.getActiveCamera().update();

    LightMaster.update(Sim.getCar());

    // Prepare and render the terrains
//    TerrainFlat[][] terrainChunks = Game.getTerrainChunks();
//    for (int i = 0; i < Game.getMap().getTerrainRows(); i++) {
//      for (int j = 0; j < Game.getMap().getTerrainCols(); j++) {
//        renderer.processTerrain(terrainChunks[j][i]);
//      }
//    }

    renderer.processTerrain(Sim.getTerrain());

    // Prepare and Render the entities
    renderer.processEntity(Sim.getCar());
    for (Entity entity : Sim.getEntities()) {
      if (entity != null) {
        renderer.processEntity(entity);
      }
    }

    // Render other stuff, order is important
    renderer.render(LightMaster.getLightsToRender(), Sim.getActiveCamera());

    // GUI goes over everything else and then text on top of GUI

    TextMaster.render();
  }

  /** Delete all text objects from this stage. */
  public static void done() {
    firstloop = true;
  }
}
