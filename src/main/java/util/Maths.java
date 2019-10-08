package util;

import engine.render.MasterRenderer;
import entities.camera.Camera;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Math functions to create Transformation Matrices.
 *
 * @author M.Nadler
 */
public class Maths {

  /**
   * Matrix used to TRANSLATE (=move) an object for projection. This gets us from local coordinates
   * to world coordinates
   *
   * @param translation movement along the axis
   * @param rx Rotation around X axis
   * @param ry Rotation around Y axis
   * @param rz Rotation around Z axis
   * @param scale Scaling factor
   * @return A translation Matrix
   */
  public static Matrix4f createTransformationMatrix(
      Vector3f translation, float rx, float ry, float rz, Vector3f scale) {
    Matrix4f matrix = new Matrix4f();
    matrix.setTranslation(translation);
    matrix.rotateZYX(
        (float) Math.toRadians(rx), (float) Math.toRadians(ry), (float) Math.toRadians(rz));
    matrix.scale(scale.x, scale.y, scale.z);
    return matrix;
  }

  /**
   * Used to place and scale GUI and HUD elements.
   *
   * @param translation Position of the gui on screen
   * @param scale size of the gui
   * @return Matrix to transform the GUI
   */
  public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
    return new Matrix4f()
        .translate(translation.x, translation.y, 0)
        .scale(new Vector3f(scale.x, scale.y, 1f));
  }

  /**
   * View Matrix. Used to transform from World Coordinates to Camera Coordinates. Used and
   * calculated a lot!
   *
   * <p>Intuition: Shifts an object in the "opposite" directions of the camera.
   *
   * @param camera Camera to generate matrix for
   * @return Camera View Matrix
   */
  public static Matrix4f createViewMatrix(Camera camera) {
    Matrix4f matrix = new Matrix4f();
    matrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0));
    matrix.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0));
    // matrix.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1));
    Vector3f cameraPos = camera.getPosition();
    Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
    matrix.translate(negativeCameraPos);
    return matrix;
  }

  /**
   * Convert a 3D world coordinates to a 2D projection in screen coordinates.
   *
   * <p>Used to render GUI or Text in relation to 3D objects.
   *
   * @param position The 3D world coordinate to convert
   * @param camera The camera used for the transformation (use active camera)
   * @return 2D normalized device coordinates: [(0,0) (1,0), (0,1) (1,1)]
   */
  public static Vector2f worldToScreen(Vector3f position, Camera camera) {
    // Transforms world coodinates to normalized device coordinates.
    Vector4f loc =
        new Vector4f(position.x, position.y, position.z, 1f)
            .mul(Maths.createViewMatrix(camera))
            .mul(MasterRenderer.getProjectionMatrix());

    if (loc.w <= 0) {
      return null;
    }

    float x = (loc.x / loc.w + 1) / 2f;
    float y = 1 - (loc.y / loc.w + 1) / 2f;
    return new Vector2f(x - 0.15f, y);
  }
}
