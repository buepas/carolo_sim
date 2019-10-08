package collision;

/*
 Old class to generate bounding boxes for each pixel... was too expensive.
 Now we use cornerbox and just check the four corners of the car as well as the center.
 */

import sim.Sim;
import terrain.MapLoader;

import java.util.ArrayList;

@Deprecated
public class BoxMaster {

    private MapLoader mapLoader;
//    private ArrayList<BoundingBox> boxList;

    public BoxMaster(String fileName) {
        mapLoader = new MapLoader(fileName);
//        boxList = new ArrayList<>();
    }


//    public void populateBoxes() {
//        for (int r = 0; r < mapLoader.getWidth(); r++) {
//            for (int c = 0; c < mapLoader.getWidth(); c++) {
//                int red = mapLoader.getBlue(r,c);
//                if (red > 0) {
//                    float[] corners = mapLoader.mapToWorld(c, r);
//                    boxList.add(
//                        new BoundingBox(
//                            corners[0],
//                            corners[1],
//                            corners[2],
//                            corners[3],
//                            red // Color intensity determines penalty
//                        )
//                    );
//                }
//            }
//        }
//        consolidate();
//    }

//    public int testCollision(BoundingBox boxToCheck) {
//        int maxPenalty = 0;
//        for (BoundingBox boundingBox : boxList) {
//            if (boxToCheck.isOverlapping(boundingBox)) {
//                int penalty = boundingBox.getValue();
//                if (penalty > maxPenalty) {
//                    maxPenalty = penalty;
//                }
//            }
//        }
//        return maxPenalty;
//    }


    /**
     * Greedy Algorithm to consolidate collision boxes to larger squares.
     * Will not be fully optimal, but fast.
     */
//    public void consolidate() {
//        boolean done = false;
//        while (!done) {
//            done = true;
//            int i = 0;
//            while(i < boxList.size() - 1) {
//                if (tryToMergeBoxes(boxList.get(i), boxList.get(i + 1))) {
//                    done = false;
//                } else {
//                    i++;
//                }
//            }
//        }
//    }

    /**
     * Try to merge two boxes if they are compatible to be merged.
     * The result must be a new, rectangle with the same area as both original boxes.
     * The second box will be deleted.
     *
     * @param a first bounding box
     * @param b second bounding box
     * @return true if the boxes could be merged; false otherwise
     */
//    public boolean tryToMergeBoxes(BoundingBox a, BoundingBox b) {
//        // Try horizontal merge
//        if (a.getMaxX() == b.getMinX() && a.getMinY() == b.getMinY() && a.getMaxY() == b.getMaxY()) {
//            a.setMaxX(b.getMaxX());
//            boxList.remove(b);
//            return true;
//        }
//
//        // Try Vertical merge
//        if (a.getMaxY() == b.getMinY() && a.getMinX() == b.getMinX() && a.getMaxX() == b.getMaxX()) {
//            a.setMaxY(b.getMaxY());
//            boxList.remove(b);
//            return true;
//        }
//
//        return false;
//    }

}
