package balance;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;


import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import static org.lwjgl.glfw.GLFW.*;

public class Window {

    int width, height;
    private String title;
    private long glfwWindow;

    public float r, g, b, a;
    private boolean fadeToBlack = false;

    private static Window window = null;

private static Scene currentScene;
    private Window(){

        this.width = 1920;
        this.height = 1080;
        this.title ="Balance";

        r = 1;
        b = 1;
        g = 1;
        a = 1;

    }

    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene = new LevelEditorScene();
                // current scene.init
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
              assert false: "Unknown scene " + newScene + " ";
        }
    }

    public static Window get(){

        if(Window.window == null) {

            Window.window = new Window();
        }
        return Window.window;

    }

    public void run(){
        System.out.println("Hello LWJGL " + Version.getVersion() +  "!");

        init();
        loop();

        // free the memory

        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        /* Terminate GLFW and the free the error call back */

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        //setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialise GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialise GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);



        //Create the window

        glfwWindow  = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create GLFW Window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable V-synch
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        Window.changeScene(0);
    }

    public void loop(){
        float beginTime = Time.getTime();
        float endTime = Time.getTime();
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)){
            // poll events
            glfwPollEvents();

            glClearColor(r,b,g,a);
            glClear(GL_COLOR_BUFFER_BIT);
            if(dt >= 0) {
                currentScene.update(dt);
            }


            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }


}