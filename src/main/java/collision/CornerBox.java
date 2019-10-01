package collision;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * A 2D (X-Z) box that moves and rotates with an object.
 * Has 4 corner points and a center that can be queried at any time to do collision calculations.
 */
public class CornerBox {

    private float[] offsets;

    private Vector2f frontLeft = new Vector2f();
    private Vector2f frontRight = new Vector2f();
    private Vector2f backLeft = new Vector2f();
    private Vector2f backRight = new Vector2f();

    private Vector2f center = new Vector2f();


    public CornerBox(float[] bb, Vector3f centerPos, float rotation, Vector3f scale) {
        if (bb.length < 6) {
            return;
        }

        // Offsets from the BoundingBox are read directly from the obj data while parsing
        // Scaling happens here and only once. Rescaling an object is not implemented
        bb[0] *= scale.x;
        bb[1] *= scale.x;
        bb[2] *= scale.y;
        bb[3] *= scale.y;
        bb[4] *= scale.z;
        bb[5] *= scale.z;
        offsets = bb;

        setCorners();

        center.x = centerPos.x;
        center.y = centerPos.z;

        rotateBox(rotation);
    }

    /**
     * Assuming the center, set the coordinates for all the corners using the offsets.
     * After this, a rotation is necessary if the object has any Y-spin.
     */
    private void setCorners() {
        frontLeft.x = center.x + offsets[0];
        frontLeft.y = center.y + offsets[4];

        frontRight.x = center.x + offsets[1];
        frontRight.y = center.y + offsets[4];

        backLeft.x = center.x + offsets[0];
        backLeft.y = center.y + offsets[5];

        backRight.x = center.x + offsets[1];
        backRight.y = center.y + offsets[5];
    }

    /**
     * Move the box to the new position of the entity. Does not change rotation.
     *
     * @param newCenter position to move to in world coordinates
     */
    public void moveBox(Vector2f newCenter) {
        Vector2f translation = newCenter.sub(center);

        center.add(translation);
        frontLeft.add(translation);
        frontRight.add(translation);
        backLeft.add(translation);
        backRight.add(translation);
    }

    /**
     * First resets the box to zero rotation and then applies the provided rotation.
     *
     * @param totalDegrees degrees of rotation to set the box at
     */
    public void rotateBox(float totalDegrees) {
        setCorners();

        float s = (float) Math.sin(Math.toRadians(-totalDegrees));
        float c = (float) Math.cos(Math.toRadians(-totalDegrees));

        frontLeft = rotatePoint(s, c, frontLeft, center);
        frontRight = rotatePoint(s, c, frontRight, center);
        backLeft = rotatePoint(s, c, backLeft, center);
        backRight = rotatePoint(s, c, backRight, center);

    }

    /**
     * Take a point and rotate it around another point.
     * Pre-calculate the sine and cosine to be more efficient when rotating multiple points around the same angle.
     *
     * @param s sine of angle (in radians) to rotate around
     * @param c cosine of angle (in radians) to rotate around
     * @param p point to rotate (new position will be returned)
     * @param center point to rotate around
     * @return The rotated point p
     */
    public static Vector2f rotatePoint(float s, float c, Vector2f p, Vector2f center) {
        p = new Vector2f(p).sub(center); // translate to center as origin

        // rotate
        float newX = p.x * c - p.y * s;
        float newY = p.x * s + p.y * c;
        Vector2f newP = new Vector2f(newX, newY);

        return newP.add(center); // translate back to origin
    }

    public Vector2f getFrontLeft() {
        return frontLeft;
    }

    public Vector2f getFrontRight() {
        return frontRight;
    }

    public Vector2f getBackLeft() {
        return backLeft;
    }

    public Vector2f getBackRight() {
        return backRight;
    }

    public Vector2f getCenter() {
        return center;
    }

    /**
     *
     * Returns all points tracked by the corner box.
     * 4 Corners and center.
     *
     * @return An array with all 4 corner points as well as the center.
     */
    public Vector2f[] getAllPoints() {
        return new Vector2f[]{
                frontLeft,
                frontRight,
                backLeft,
                backRight,
                center
        };
    }

    @Override
    public String toString() {
        return "FL " + frontLeft.x + " / " + frontLeft.y +
                " FR " + frontRight.x + " / " + frontRight.y +
                " BL " + backLeft.x + " / " + backLeft.y +
                " BR " + backRight.x + " / " + backRight.y +
                " C " + center.x + " / " + center.y;
    }
}
