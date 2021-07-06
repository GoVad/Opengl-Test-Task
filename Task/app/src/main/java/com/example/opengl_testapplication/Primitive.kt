package com.example.opengl_testapplication

import android.opengl.GLES20

class Primitive (var coord:FloatArray, var color:FloatArray, var vertexCount:Int, var mode:Int){

    fun draw(prog:Int,mvp:FloatArray)
    {
        GLES20.glUseProgram(prog)

        var vertexHandler = MyGLUtils.setAttribPointer(prog,"vPosition",3,coord)
        var colorHandler = MyGLUtils.setAttribPointer(prog,"color",4,color)

        GLES20.glGetUniformLocation(prog,"uMVPMatrix").also{
            GLES20.glUniformMatrix4fv(it,1,false,mvp,0)
        }

        GLES20.glEnableVertexAttribArray(vertexHandler)
        GLES20.glEnableVertexAttribArray(colorHandler)
        GLES20.glDrawArrays(mode,0,vertexCount)
        GLES20.glDisableVertexAttribArray(vertexHandler)
        GLES20.glDisableVertexAttribArray(colorHandler)
    }

}