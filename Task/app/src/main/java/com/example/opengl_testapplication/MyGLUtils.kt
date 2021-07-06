package com.example.opengl_testapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import androidx.core.graphics.scale
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class MyGLUtils {
    companion object{

        val topOffset = 120f

//        fun createTexture(bm: Bitmap):Int
//        {
//            var textures = IntArray(1)
//            GLES20.glGenTextures(1,textures,0)
//            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0])
//            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR)
//            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR)
//
//            GLUtils.texImage2D(
//                GLES20.GL_TEXTURE_2D,
//                0,
//                bm,
//                0
//            )
//            bm.recycle()
//            return textures[0]
//        }

        private fun genByteBuf(arr:FloatArray): FloatBuffer
        {
            return ByteBuffer.allocateDirect(arr.size * 4).run {
                order(ByteOrder.nativeOrder())
                asFloatBuffer().apply {
                    put(arr)
                    position(0)
                }
            }
        }

        private fun loadShader(type: Int, shaderCode: String): Int {
            return GLES20.glCreateShader(type).also { shader ->
                GLES20.glShaderSource(shader, shaderCode)
                GLES20.glCompileShader(shader)
            }
        }

        fun generateProgram(fragCode:String,vertCode:String):Int
        {
            val vertexShader = MyGLUtils.loadShader(GLES20.GL_VERTEX_SHADER, vertCode)
            val fragmentShader =  MyGLUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragCode)

            return GLES20.glCreateProgram().also {
                GLES20.glAttachShader(it, vertexShader)
                GLES20.glAttachShader(it, fragmentShader)
                GLES20.glLinkProgram(it)
            }
        }

        fun setAttribPointer(prog:Int,name:String,vertexCount:Int,arr:FloatArray):Int
        {
            return GLES20.glGetAttribLocation(prog,name).also{
                GLES20.glVertexAttribPointer(
                    it,
                    vertexCount,
                    GLES20.GL_FLOAT,
                    false,
                    vertexCount*4,
                    genByteBuf(arr)
                )
            }
        }
    }
}