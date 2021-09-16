package com.github.serivesmejia.accelgameoflife


import org.joml.Matrix4f
import org.joml.Vector2f
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.glViewport
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL

/**
 * Create a window using the desktop GLFW API
 * @property initialTitle the initial title of this window
 * @property initialSize the initial size of this window
 * @property vsync whether this window will use vertical synchronization or not.
 */
class Window(initialTitle: String = "LIFE",
             private val initialSize: Vector2f = Vector2f(640f, 480f),
             val vsync: Boolean = true) {

    /**
     * Long native pointer of this window
     */
    var ptr = 0L
        private set

    /**
     * Get whether this window is currently visible
     * Use show() or hide() functions to update visibility
     */
    val isVisible: Boolean
        get() = glfwGetWindowAttrib(ptr, GLFW_VISIBLE) == GLFW_TRUE

    /**
     * Get whether this window is currently focused
     */
    val isFocused: Boolean
        get() = glfwGetWindowAttrib(ptr, GLFW_FOCUSED) == GLFW_TRUE

    /**
     * Get or set this window title
     *
     * Note that when setting the window title
     * manually with the glfw function, there's
     * no way to retrieve the current title, so
     * any independent change won't be reflected
     * on the value of this variable.
     */
    var title: String = initialTitle
        set(value) {
            glfwSetWindowTitle(ptr, value)
            field = value
        }

    /**
     * Gets the current position of this window as a Vector2
     */
    var position: Vector2f
        set(value) {
            glfwSetWindowPos(ptr, value.x.toInt(), value.y.toInt())
        }
        get() {
            val x = BufferUtils.createIntBuffer(1)
            val y = BufferUtils.createIntBuffer(1)
            glfwGetWindowPos(ptr, x, y)

            return Vector2f(x.get(0).toFloat(), y.get(0).toFloat())
        }

    /**
     * Gets the current size of this window as a Vector2
     */
    var size: Vector2f
        set(value) {
            glfwSetWindowSize(ptr, value.x.toInt(), value.y.toInt())
        }
        get() {
            val w = BufferUtils.createIntBuffer(1)
            val h = BufferUtils.createIntBuffer(1)
            glfwGetWindowSize(ptr, w, h)

            return Vector2f(
                w.get(0).toFloat(),
                h.get(0).toFloat()
            )
        }

    val aspectRatio: Float get() {
        val s = size
        return s.x / s.y
    }

    var projectionMatrix = Matrix4f()
        private set

    /**
     * Initializes glfw and creates this window
     */
    fun create() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set()

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw IllegalStateException("Unable to initialize GLFW")

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

        // Create the window
        ptr = glfwCreateWindow(
            initialSize.x.toInt(),
            initialSize.y.toInt(),
            title, NULL, NULL
        )

        if (ptr == NULL)
            throw RuntimeException("Failed to create the GLFW window")

        // Make the OpenGL context current
        glfwMakeContextCurrent(ptr)
        if(vsync) {
            // Enable v-sync
            glfwSwapInterval(1)
        }

        // Make the window visible
        glfwShowWindow(ptr)

        glfwSetWindowSizeCallback(ptr) { _, w: Int, h: Int ->
            glViewport(0, 0, w, h)
            updateProjectionMatrix()
        }

        updateProjectionMatrix()

        GL.createCapabilities()
    }

    fun updateProjectionMatrix() {
        val currSize = size

        projectionMatrix = Matrix4f().ortho2D(
            -currSize.x / 2, currSize.x / 2,
            -currSize.y / 2, currSize.y / 2
        )
    }

    /**
     * Shows this window
     */
    fun show() {
        glfwShowWindow(ptr)
    }

    /**
     * Hides this window
     */
    fun hide() {
        glfwHideWindow(ptr)
    }

    /**
     * Centers the window in the screen
     */
    fun center() {
        // Get the thread stack and push a new frame
        stackPush().use { stack ->
            val pWidth = stack.mallocInt(1) // int*
            val pHeight = stack.mallocInt(1) // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(ptr, pWidth, pHeight)

            // Get the resolution of the primary monitor
            val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())

            // Center the window
            glfwSetWindowPos(
                ptr,
                (vidMode!!.width() - pWidth[0]) / 2,
                (vidMode.height() - pHeight[0]) / 2
            )
        }
    }

    /**
     * Closes this window, terminates glfw.
     */
    fun destroy() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(ptr)
        glfwDestroyWindow(ptr)

        // Terminate GLFW and free the error callback
        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

}