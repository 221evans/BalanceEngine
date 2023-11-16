package balance;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;


import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import static org.lwjgl.glfw.GLFW.*;

public class Window {

    int width, height;
    private String title;
    private long glfwWindow;

    private float r, g, b, a;
    private boolean fadeToBlack = false;

    private static Window window = null;
    private Window(){

        this.width = 1920;
        this.height = 1080;
        this.title ="Balance";

        r = 1;
        b = 1;
        g = 1;
        a = 1;

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
    }

    public void loop(){

        while (!glfwWindowShouldClose(glfwWindow)){
            // poll events
            glfwPollEvents();

            glClearColor(r,b,g,a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (fadeToBlack){
                r = Math.max(r  - 0.01f, 0);
                b = Math.max(b  - 0.01f, 0);
                g = Math.max(g - 0.01f, 0);
            }

            if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)){
                fadeToBlack = true;
            }

            glfwSwapBuffers(glfwWindow);
        }
    }


}