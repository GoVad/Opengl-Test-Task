package com.example.opengl_testapplication

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import kotlin.math.abs

class MyGLRenderer : GLSurfaceView.Renderer {

    private lateinit var mTriangle:Triangle
    private lateinit var mPlane:Plane

    private var vPMatrix = FloatArray(16)
    private var triangleVMatrix = FloatArray(16)
    private var planeVMatrix = FloatArray(16)

    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        GLES20.glClearColor(0.9f, 0.9f, 0.9f, 1.0f)

        val coordsTriangle = floatArrayOf(
            0.0f, 0.622008459f, -0.1f,      // top
            -0.5f, -0.311004243f, -0.2f,    // bottom left
            0.5f, -0.311004243f, -0.3f      // bottom right
        )

        val coordsPlane = floatArrayOf(
            -5f,-5f,0.5f,
            5f,-5f,0.5f,
            5f,5f,0.5f,
            -5f,5f,0.5f
        )

        val color1 = floatArrayOf(0.5f,0f,0f,1f)
        val color2 = floatArrayOf(0.5f,0.5f,0f,1f)

        mTriangle = Triangle(coordsTriangle,color1)
        mPlane = Plane(coordsPlane,color2)
    }

    override fun onDrawFrame(unused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -6f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        triangleVMatrix = vPMatrix.copyOf()
        planeVMatrix = vPMatrix.copyOf()

        mPlane.draw(planeVMatrix)
        mTriangle.draw(triangleVMatrix)
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }



}