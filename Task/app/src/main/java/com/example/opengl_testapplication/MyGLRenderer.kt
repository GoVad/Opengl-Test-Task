package com.example.opengl_testapplication

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES20
import android.opengl.GLSurfaceView

class MyGLRenderer : GLSurfaceView.Renderer {

    private lateinit var mTriangle:Triangle
    private lateinit var mPlane:Plane

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // Set the background frame color
        GLES20.glClearColor(0.9f, 0.9f, 0.9f, 1.0f)

        val coordsTriangle = floatArrayOf(
            -0.5f,-0.5f,-0.6f,
            0.5f,-0.5f,-0.6f,
            0f,0.5f,0.2f
        )

        val coordsPlane = floatArrayOf(
            -0.8f,-0.8f,-0.5f,
            0.8f,-0.8f,-0.5f,
            0.8f,0.8f,-0.5f,
            -0.8f,0.8f,-0.5f
        )

        val color1 = floatArrayOf(0.5f,0f,0f,1f)
        val color2 = floatArrayOf(0.5f,0.5f,0f,1f)

        mTriangle = Triangle(coordsTriangle,color1)
        mPlane = Plane(coordsPlane,color2)

    }

    override fun onDrawFrame(unused: GL10) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        mPlane.draw()
        mTriangle.draw()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }
}