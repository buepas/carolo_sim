package gui;

import engine.render.GuiRenderer;

import java.util.ArrayList;

public class GuiMaster {
    private ArrayList<GuiTexture> guis = new ArrayList<>();
    private GuiRenderer guiRenderer;

    public GuiMaster() {
        guiRenderer = new GuiRenderer();
    }

    public void addGui(GuiTexture gui) {
        if(!guis.contains(gui)) {
            guis.add(gui);
        }
    }

    public void addGuis(ArrayList<GuiTexture> guiList) {
        for (GuiTexture guiTexture : guiList) {
            addGui(guiTexture);
        }
    }

    public void removeGui(GuiTexture gui) {
        guis.remove(gui);
    }

    public void removeGuis(ArrayList<GuiTexture> guiList) {
        for (GuiTexture guiTexture : guiList) {
            removeGui(guiTexture);
        }
    }

    public void render() {
        guiRenderer.render(guis);
    }

}
