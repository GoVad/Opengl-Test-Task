package com.example.opengl_testapplication

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class MyGLUtils {
    companion object{

        //смещение от верхушки экрана т.к. 120 пикселей занимает
        //возвращение на предыдущую страницу
        const val topOffset = 120f

        //функция генерации массива байтов из массива типа float
        //необходимо для аттрибутов угловых шейдеров
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
        //функция загрузки шейдера определенного типа с определенным кодом
        private fun loadShader(type: Int, shaderCode: String): Int {
            return GLES20.glCreateShader(type).also { shader ->
                GLES20.glShaderSource(shader, shaderCode)
                GLES20.glCompileShader(shader)
            }
        }
        //функция создания программы из шейдеров
        fun generateProgram(fragCode:String,vertCode:String):Int
        {
            //создание углового и фрагментного шейдеров
            val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertCode)
            val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragCode)
            //и создание на их основе программы
            return GLES20.glCreateProgram().also {
                GLES20.glAttachShader(it, vertexShader)
                GLES20.glAttachShader(it, fragmentShader)
                GLES20.glLinkProgram(it)
            }
        }
        //создание указателя для углового шейдера и возвращение его хендлера
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