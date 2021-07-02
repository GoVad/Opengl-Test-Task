package com.example.opengl_testapplication

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Triangle(coord:FloatArray, mcolor:FloatArray)
{

    private val vertexShaderCode =
        "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = vPosition;" +
                "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"

    private var mProgram: Int
    private var color = mcolor
    private var Coord = coord
    private var COORDS_PER_VERTEX = 3

    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount: Int = coord.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    private var vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(Coord.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                // add the coordinates to the FloatBuffer
                put(Coord)
                // set the buffer to read the first coordinate
                position(0)
            }
        }

    init {

        val vertexShader: Int = Loader.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int =  Loader.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
        mProgram = GLES20.glCreateProgram().also {

            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }
    }

    fun draw() {
        GLES20.glUseProgram(mProgram)
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )

            // get handle to fragment shader's vColor member
            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->
                // Set color for drawing the triangle
                GLES20.glUniform4fv(colorHandle, 1, color, 0)
            }

            // Draw the triangle
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(it)
        }
    }
}
