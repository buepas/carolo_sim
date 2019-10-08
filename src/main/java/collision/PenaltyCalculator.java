package collision;

import engine.io.InfoWindow;
import entities.Car;
import org.joml.Vector2f;
import terrain.MapLoader;

/**
 * Keep track of the penalty for a specific car and map.
 * Run {@link #updatePenalty(InfoWindow)} each frame, or how ever often you need it updated.
 *
 * @author M.Nadler
 */
public class PenaltyCalculator {

    private final Car car;
    private final MapLoader mapLoader;

    private int currentPenalty;

    /**
     * Create a new penalty calculator for a car and map.
     * Use #updatePenalty(InfoWindow) to check and display penalty.
     *
     * @param car the car to check the penalty for
     * @param mapLoader the map loader that holds the current map
     */
    public PenaltyCalculator(Car car, MapLoader mapLoader) {
        this.car = car;
        this.mapLoader = mapLoader;
    }


    /**
     * Converts the world coordinates to map coordinates (pixels on the image),
     * then grabs and returns the intensity of the red color channel at this location.
     *
     * @param coords world coordinates
     * @return the red color value at that point [0,255]
     */
    private int getPenaltyForWorldCoords(Vector2f coords) {
        return mapLoader.getRed(mapLoader.worldToMap(coords));
    }

    /**
     * Grabs all the points of the car's cornerbox (4 corners plus middle) and checks their
     * current penalty (red color intensity on the map). Then returns the highest value among the corners.
     *
     * @return the maximum penalty that any of the car's points are violating. [0,255]
     */
    private int getMaxPenalty() {
        int maxPen = 0;

        // Loop over corner and center points and get the highest penalty
        for (Vector2f point : car.getCornerBox().getAllPoints()) {
            int pen = getPenaltyForWorldCoords(point);
            if (pen > maxPen) {
                maxPen = pen;
            }
        }
        return maxPen;
    }

    /**
     * Check all the corners and the center of the car if they are in a penalty zone. Save the highest penalty
     * and send it to the provided InfoWindow to display in the GUI.
     *
     * @param infoWindow the infowindow to show the penalty
     */
    public void updatePenalty(InfoWindow infoWindow) {
        currentPenalty = getMaxPenalty();
        if (infoWindow != null) {
            infoWindow.updatePenalty(currentPenalty);
        }
    }

    public int getCurrentPenalty() {
        return currentPenalty;
    }
}
