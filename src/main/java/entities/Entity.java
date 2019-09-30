package entities;

import collision.CornerBox;
import engine.models.TexturedModel;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * All 3D entities are derived or spawned from this class. Defines position, bounding box, rotation
 * and scale of an object as well as the model, texture (with index if applicable) and if it is
 * destroyed.
 */
public class Entity {

  private TexturedModel model;
  private Vector3f position;
  private float rotX;
  private float rotY;
  private float rotZ;
  private Vector3f scale;
  private int textureIndex = 0;

  private boolean destroyed;

  protected CornerBox cornerBox;

  /**
   * Default constructor if no texture atlas is used.
   *
   * @param model Textured Model
   * @param position World Coordinates
   * @param rotX X Rotation
   * @param rotY Y Rotation
   * @param rotZ Z Rotation
   * @param scale Scaling factor
   */
  public Entity(
          TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
    this(model, -1, position, rotX, rotY, rotZ, scale);
  }

  /**
   * Constructor if texture atlas is used.
   *
   * @param model Textured Model
   * @param index Texture index in texture atlas
   * @param position World Coordinates
   * @param rotX X Rotation
   * @param rotY Y Rotation
   * @param rotZ Z Rotation
   * @param scale Scaling factor
   */
  public Entity(
      TexturedModel model,
      int index,
      Vector3f position,
      float rotX,
      float rotY,
      float rotZ,
      float scale) {
    if (index > -1) {
      this.textureIndex = index;
    }

    this.model = model;

    this.position = position;
    this.rotX = rotX;
    this.rotY = rotY;
    this.rotZ = rotZ;
    this.scale = new Vector3f(scale, scale, scale);


     // Set bounding box which is stored in the raw model
    if (model != null && model.getRawModel().getBoundingCoords().length == 6) {
      float[] boundingCoords = model.getRawModel().getBoundingCoords();
      cornerBox = new CornerBox(boundingCoords, position, rotY, this.scale);
    }
  }



  /**
   * Call this after you reposition the entity. If you use proper setters, you probably never have
   * to call it manually.
   */
  private void moveCornerBox() {
    if (cornerBox == null) {
      return;
    }
    cornerBox.moveBox(new Vector2f(getPosition().x, getPosition().z));
  }


  /**
   * Move this entity in world space. Updates Corner Box.
   *
   * @param velocity velocity to move
   */
  public void increasePosition(Vector3f velocity) {
    position.add(velocity);
    moveCornerBox();
  }



  /**
   * Move this entity to a new point X,Y,Z in the world. Updates Corner Box.
   *
   * @param position x,y,z world coordinates
   */
  public void setPosition(Vector3f position) {
    this.position = position;
    moveCornerBox();
  }

  public float getRotX() {
    return rotX;
  }

  /**
   * Rotate this entity around the X axis.
   *
   * @param rotX Degrees to rotate
   */
  public void setRotX(float rotX) {
    this.rotX = rotX % 360;
  }

  public float getRotY() {
    return rotY;
  }

  /**
   * Set new rotation around the Y axis. Updates CornerBox.
   *
   * @param rotY Degrees to rotate
   */
  public void setRotY(float rotY) {
      this.rotY = rotY % 360;
      if(cornerBox != null) {
        cornerBox.rotateBox(this.rotY);
      }
  }

  public float getRotZ() {
    return rotZ;
  }

  /**
   * Rotate this entity around the Z axis.
   *
   * @param rotZ Degrees to rotate
   */
  public void setRotZ(float rotZ) {
    this.rotZ = rotZ  % 360;
  }

  public Vector3f getScale() {
    return scale;
  }

//  /**
//   * Scale this unit up (scale greater than 1) or down (scale less than 1) by a scaling factor.
//   *
//   * @param scale Scaling factor
//   */
//  public void setScale(Vector3f scale) {
////    cornerBox.scaleBox(this.scale.negate());
//    this.scale = scale;
////    cornerBox.scaleBox(scale);
//    // Scale needs a rework to be used
//  }

  public boolean isDestroyed() {
    return destroyed;
  }

  public void setDestroyed(boolean destroyed) {
    this.destroyed = destroyed;
  }

  public CornerBox getCornerBox() {
    return cornerBox;
  }

  public TexturedModel getModel() {
    return model;
  }

  public void setModel(TexturedModel model) {
    this.model = model;
  }

  public Vector3f getPosition() {
    return position;
  }

  public void setTextureIndex(int textureIndex) {
    this.textureIndex = textureIndex;
  }

  /**
   * Returns col in the texture atlas.
   *
   * @return col in the texture atlas.
   */
  public float getTextureXOffset() {
    int column = textureIndex % model.getTexture().getNumberOfRows();
    return (float) column / (float) model.getTexture().getNumberOfRows();
  }

  /**
   * Returns row in the texture atlas.
   *
   * @return row in the texture atlas.
   */
  public float getTextureYOffset() {
    int row = textureIndex / model.getTexture().getNumberOfRows();
    return (float) row / (float) model.getTexture().getNumberOfRows();
  }


}
