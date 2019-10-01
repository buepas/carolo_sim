package engine.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.stb.STBImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memFree;
import static util.IoUtil.ioResourceToByteBuffer;

/**
 * Set up the GLFW Game Window.
 *
 * <p>Controls
 */
public class RenderWindow {
  public static final Logger logger = LoggerFactory.getLogger(RenderWindow.class);

  private final String title;
  private int width;
  private int height;
  private long window;
  private boolean fullscreen;

  /**
   * Create a new Game Window. We only ever need one.
   *
   * @param width x axis screen resolution
   * @param height y axis screen resolution
   * @param title title of the window
   */
  public RenderWindow(int width, int height, String title) {
    setSize(width, height);
    this.title = title;
    setFullscreen(false);
  }

  /** Calls the openGL binds to create a game window and sets openGL parameters. */
  public void create() {
    if (!glfwInit()) {
      System.err.println("Error: Couldn't initialize GLFW");
      System.exit(-1);
    }

    // Set openGL version to 4.00 core
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
//    glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
    glfwWindowHint(GLFW_VERSION_MINOR, 0);
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    window = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : 0, 0);
    if (window == 0) {
      System.err.println("Error: Window couldn't be created"); // No or old openGL is usually the
      // cause for this error
      System.exit(-1);
    }

    // Set up input handler properties

    // So we don't skip over very quick key presses
    glfwSetInputMode(window, GLFW_STICKY_KEYS, GLFW_TRUE);

    // add all the callback functions from the Input handler to the window
    glfwSetKeyCallback(window, InputHandler.keyboard);
    glfwSetMouseButtonCallback(window, InputHandler.mouse);
    glfwSetScrollCallback(window, InputHandler.scrollCallback);
    glfwSetCursorPosCallback(window, InputHandler.cursorPosCallback);
    glfwSetCharCallback(window, InputHandler.charCallback);

    // To tell openGL we are now in this window
    glfwMakeContextCurrent(window);
    GL.createCapabilities();

    // Set window dimensions on main monitor if fullscreen is not used
    if (!fullscreen) {
      GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
      if (videoMode != null) {
        glfwSetWindowPos(
            window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
      }
      glfwShowWindow(window);
    }

//    setIcon("logo32");
  }

  /**
   * Returns true if the window is about to be closed or closed already.
   *
   * @return true if the window is about to be closed or closed already
   */
  public boolean isClosed() {
    return glfwWindowShouldClose(window);
  }

  /**
   * Processes all pending events. Runs this: {@link GLFW#glfwPollEvents()}
   *
   * @see GLFW#glfwPollEvents()
   */
  public void update() {
    glfwPollEvents();
  }

  /** Manually terminate the window. */
  public void kill() {
    glfwTerminate();
  }

  /**
   * Sets the value of the close flag of the specified window. This can be used to override the
   * user's attempt to close the window, or to signal that it should be closed.
   */
  public void stop() {
    glfwSetWindowShouldClose(window, true);
  }

  /**
   * Swaps the front and back buffers of the specified window when rendering with OpenGL or OpenGL
   * ES. If the swap interval is greater than zero, the GPU driver waits the specified number of
   * screen updates before swapping the buffers.
   */
  public void swapBuffers() {
    glfwSwapBuffers(window);
  }

  /** Returns system time in seconds. */
  private double getTime() {
    return (double) System.nanoTime() / 1e9;
  }

  /**
   * Read and assign an image to the window and tool bar.
   *
   * @param filename filname of the icon
   */
  private void setIcon(String filename) {
    String resPath = "/assets/textures/" + filename + ".png";

    // Read image to Buffer
    ByteBuffer buffer = ioResourceToByteBuffer(resPath, 8 * 1024);

    // Create GLFW Image and set it as icon
    try (GLFWImage.Buffer icons = GLFWImage.malloc(1)) {
      IntBuffer w = memAllocInt(1);
      IntBuffer h = memAllocInt(1);
      IntBuffer comp = memAllocInt(1);

      ByteBuffer pixels32 = STBImage.stbi_load_from_memory(buffer, w, h, comp, 4);
      icons.position(0).width(w.get(0)).height(h.get(0)).pixels(pixels32);

      icons.position(0);
      glfwSetWindowIcon(window, icons);

      STBImage.stbi_image_free(pixels32);

      memFree(comp);
      memFree(h);
      memFree(w);
    }
  }

  // Getters
  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  /**
   * Returns the GLFW window as a number.
   *
   * @return the GLFW window as a number
   */
  public long getWindow() {
    return window;
  }


  public void setSize(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public void setFullscreen(boolean fullscreen) {
    this.fullscreen = fullscreen;
  }
}
