package gui;

import engine.io.Screenshot;
import org.joml.Vector2f;
import org.joml.Vector2i;
import sim.Sim;

import javax.swing.text.View;
import java.util.ArrayList;

/**
 * Will restrict the visible openGL render window by adding black bars on the 4 sides.
 * Can be used to crop the viewable area to cut out horizon and car hood for example.
 *
 * @author M.Nadler
 */
public class Viewport {

    private Vector2f topLeft = new Vector2f();
    private Vector2f bottomRight = new Vector2f();

    private boolean creating;
    private float alpha;

    private ArrayList<GuiTexture> bars = new ArrayList<>();

    // Bars
    private GuiTexture top = new GuiTexture(
            Sim.loader.loadTexture("black"),
            new Vector2f(0,0),
            new Vector2f(1,1),
            0
    );
    private GuiTexture bottom = new GuiTexture(
            Sim.loader.loadTexture("black"),
            new Vector2f(0,0),
            new Vector2f(1,1),
            0
    );
    private GuiTexture left = new GuiTexture(
            Sim.loader.loadTexture("black"),
            new Vector2f(0,0),
            new Vector2f(1,1),
            0
    );
    private GuiTexture right = new GuiTexture(
            Sim.loader.loadTexture("black"),
            new Vector2f(0,0),
            new Vector2f(1,1),
            0
    );


    public Viewport() {
        bars.add(top);
        bars.add(bottom);
        bars.add(left);
        bars.add(right);
        clear();
    }


    public void startCreation (Vector2f clickCoords) {
        topLeft = clickCoords;
        bottomRight = clickCoords;
        alpha = 1;
        creating = true;
        updateBars();
    }

    public void whileCreation(Vector2f mouseCoords) {
        if (creating) {
            bottomRight = mouseCoords;
            updateBars();
        }
    }

    public void endCreation (Vector2f clickCoords) {
        bottomRight = clickCoords;
        alpha = 1;
        creating = false;
        updateBars();
        if (topLeft.x == bottomRight.x && topLeft.y == bottomRight.y) {
            clear();
        }
    }

    public void clear() {
        alpha = 0;
        topLeft = new Vector2f();
        bottomRight = new Vector2f(Sim.renderWindow.getWidth(), Sim.renderWindow.getHeight());
        updateBars();
    }

    private void updateBars() {
        float w = Sim.renderWindow.getWidth();
        float h = Sim.renderWindow.getHeight();

        top.setAlpha(alpha);
        top.setPosition(new Vector2f(0, 2 - topLeft.y * 2 / h));

        bottom.setAlpha(alpha);
        bottom.setPosition(new Vector2f(0, (h-bottomRight.y) * 2 / h - 2));

        left.setAlpha(alpha);
        left.setPosition(new Vector2f(  topLeft.x * 2 / w - 2,0));

        right.setAlpha(alpha);
        right.setPosition(new Vector2f(2 - (w - bottomRight.x) * 2 / w, 0));

        // Update screenshot area
        Screenshot.setTopLeft(new Vector2i((int) topLeft.x, (int) topLeft.y));
        Screenshot.setWidth((int) (bottomRight.x - topLeft.x));
        Screenshot.setHeight((int) (bottomRight.y - topLeft.y));
    }

    public ArrayList<GuiTexture> getBarList() {
            return bars;
    }

}
