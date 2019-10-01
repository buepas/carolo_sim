package sim.stages;

import engine.render.MasterRenderer;
import entities.Entity;
import entities.light.LightMaster;
import sim.Sim;

/**
 * MAIN GAME LOOP specification and rendering. Contains and manages the Game Loop while the player
 * is playing the game. All the rendering and updating is done here.
 */
public class Playing {

  /**
   * Game Loop. This runs every frame as long as the payer is playing the game. Include all
   * rendering and input handling here.
   *
   * @param renderer master renderer from game loop
   */
  public static void update(MasterRenderer renderer) {

    // Update positions of carm camera, and light
    Sim.getCar().update();
    Sim.getActiveCamera().update();
    LightMaster.update(Sim.getCar());

    // Render Terrain
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
  }

}
