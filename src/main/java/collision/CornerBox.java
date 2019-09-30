package collision;

import org.joml.Vector2f;
import org.joml.Vector3f;

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
//        scaleBox(scale);
    }

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

    public void moveBox(Vector2f newCenter) {
        Vector2f translation = newCenter.sub(center);

        center.add(translation);
        frontLeft.add(translation);
        frontRight.add(translation);
        backLeft.add(translation);
        backRight.add(translation);
    }

    public void rotateBox(float totalDegrees) {
        setCorners();

        float s = (float) Math.sin(Math.toRadians(-totalDegrees));
        float c = (float) Math.cos(Math.toRadians(-totalDegrees));

        frontLeft = rotatePoint(s, c, frontLeft, center);
        frontRight = rotatePoint(s, c, frontRight, center);
        backLeft = rotatePoint(s, c, backLeft, center);
        backRight = rotatePoint(s, c, backRight, center);

    }

    public static Vector2f rotatePoint(float s, float c, Vector2f p, Vector2f center) {
        p = new Vector2f(p).sub(center); // translate to center as origin

        // rotate
        float newX = p.x * c - p.y * s;
        float newY = p.x * s + p.y * c;
        Vector2f newP = new Vector2f(newX, newY);

        return newP.add(center); // translate back to origin
    }

//    public void scaleBox(Vector3f scale) {
//        Vector2f scale2D = new Vector2f(scale.x, scale.z); // scale is XYZ -> we need XZ
//        System.out.println(scale2D);
//        frontLeft = scalePoint(new Vector2f(frontLeft), scale2D);
//        frontRight = scalePoint(new Vector2f(frontRight), scale2D);
//        backLeft = scalePoint(new Vector2f(backLeft), scale2D);
//        backRight = scalePoint(new Vector2f(backRight), scale2D);
//    }
//
//    private Vector2f scalePoint(Vector2f point, Vector2f scale) {
//        point.x = (point.x - center.x) * scale.x + center.x;
//        point.y = (point.y - center.y) * scale.y + center.y;
//        return point;
//    }

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
