package terrain;

import engine.textures.TerrainTexture;
import engine.textures.TerrainTexturePack;
import sim.Sim;

/**
 * Generate and hold info for terrains.
 *
 * @author M.Nadler
 */
public class TerrainMaster {

    private static TerrainTexturePack texturePack;

    public static void init() {
        // Prepare Textures
        TerrainTexture lightblue = new TerrainTexture(Sim.loader.loadTexture("lightblue"));
        TerrainTexture red = new TerrainTexture(Sim.loader.loadTexture("red"));
        TerrainTexture white = new TerrainTexture(Sim.loader.loadTexture("white"));
        TerrainTexture black = new TerrainTexture(Sim.loader.loadTexture("black"));
        TerrainTexture yellow = new TerrainTexture(Sim.loader.loadTexture("yellow"));

        texturePack = new TerrainTexturePack(black, black, red, white);
    }

    public static Terrain generateTerrain(String blendMapFile) {
        TerrainTexture blendMap = new TerrainTexture(Sim.loader.loadTexture(blendMapFile));
        return new Terrain(0,0, texturePack, blendMap);
    }

}
