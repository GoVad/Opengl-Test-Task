package com.example.opengl_testapplication

import android.opengl.GLES20

class Primitive (private var coord:FloatArray, var color:FloatArray, private var vertexCount:Int, private var mode:Int){

    //класс примитива имеет лишь один метод - нарисовать примитив
    fun draw(prog:Int,mvp:FloatArray)
    {
        //используемая программа
        GLES20.glUseProgram(prog)

        //установка указателей на аттрибуты
        val vertexHandler = MyGLUtils.setAttribPointer(prog,"vPosition",3,coord)
        val colorHandler = MyGLUtils.setAttribPointer(prog,"color",4,color)

        //задаем юниформу для матрицы проекции
        GLES20.glGetUniformLocation(prog,"uMVPMatrix").also{
            GLES20.glUniformMatrix4fv(it,1,false,mvp,0)
        }

        //рисуем обьект из углов разного цвета с заданым
        //модом рисования(GL_TRIANGLES,GL_TRIANGLE_FAN и тд.)
        GLES20.glEnableVertexAttribArray(vertexHandler)
        GLES20.glEnableVertexAttribArray(colorHandler)
        GLES20.glDrawArrays(mode,0,vertexCount)
        GLES20.glDisableVertexAttribArray(vertexHandler)
        GLES20.glDisableVertexAttribArray(colorHandler)
    }

}