package com.example.opengl_testapplication

import android.content.res.Resources
import android.opengl.GLES20
import android.opengl.GLUtils
import java.nio.*


class Triangle(var coord:FloatArray, var color: FloatArray)
{
    private var coordCount = 3
    private var coordStride = 3*4
    var mProgram:Int = 0
    private var vign = false
    private var myTex = IntArray(1)

    private var coordBuf = ByteBuffer.allocateDirect(coord.size*4).run {
        order(ByteOrder.nativeOrder())
        asFloatBuffer().apply {
            put(coord)
            position(0)
        }
    }

    private val vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}"

    private val fragmentShaderCode =
        "precision mediump float;"+

                "uniform vec4 vColor;" +
                "uniform float brightness;"+
                "uniform vec2 resolution;"+
                "uniform int enableVign;"+
                "uniform int enableBlur;"+
                "uniform sampler2D tex;"+

                "vec4 applyVignette(vec4 color){"+
                "vec2 pos = (gl_FragCoord.xy/resolution)-vec2(0.5);"+
                "float dist = length(pos);"+
                "float r = 0.9;"+
                "float soft = 0.55;"+
                "float vign = smoothstep(r,r-soft,dist);"+
                "color.rgb = color.rgb-(1.0-vign);"+
                "return color;"+
                "}"+

                "vec4 applyBlur(vec4 color){"+
                "return color+texture2D(tex,gl_FragCoord.xy);"+
                "}"+

                "void main() {"+
                "vec4 result = vColor;"+
                "if(brightness!=1.) result *=brightness;"+
                "if(enableVign==1) result = applyVignette(result);"+
                "if(enableBlur==1) result = applyBlur(result);"+
                "gl_FragColor = result;" +
                "}"

    init {
        GLES20.glGenTextures(1,myTex,0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,myTex[0])

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGB,
            Resources.getSystem().displayMetrics.widthPixels,
            Resources.getSystem().displayMetrics.heightPixels,
            0,
            GLES20.GL_RGB,
            GLES20.GL_UNSIGNED_BYTE,
            null
        )

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR)

        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader =  loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        mProgram = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }

    fun draw(mvpMatrix: FloatArray) {
        GLES20.glUseProgram(mProgram)

        GLES20.glGetAttribLocation(mProgram, "vPosition").also {

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)

            GLES20.glVertexAttribPointer(
                it,
                coordCount,
                GLES20.GL_FLOAT,
                false,
                coordStride,
                coordBuf
            )

            // get handle to fragment shader's vColor member
            GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->
                // Set color for drawing the triangle
                GLES20.glUniform4fv(colorHandle, 1, color, 0)
            }

            // get handle to shape's transformation matrix
            GLES20.glGetUniformLocation(mProgram, "uMVPMatrix").also {vPMatrixHandle->
                GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)
            }

            GLES20.glGetUniformLocation(mProgram, "brightness").also {brightness->
                GLES20.glUniform1f(brightness, 1f)
            }

            GLES20.glGetUniformLocation(mProgram, "resolution").also {resolution->
                GLES20.glUniform2f(resolution, Resources.getSystem().displayMetrics.widthPixels.toFloat(),
                    Resources.getSystem().displayMetrics.heightPixels.toFloat())
            }

            GLES20.glGetUniformLocation(mProgram, "enableVign").also {ev->
                GLES20.glUniform1i(ev, 0)
            }
            GLES20.glGetUniformLocation(mProgram, "tex").also {tex->
                GLES20.glUniform1i(tex, myTex[0])
            }

            GLES20.glGetUniformLocation(mProgram, "enableBlur").also {eb->
                GLES20.glUniform1i(eb, 1)
            }

            // Draw the triangle
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3)

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(it)
        }
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,GLES20.GL_COLOR_ATTACHMENT0,
            GLES20.GL_TEXTURE_2D,myTex[0],0)

    }



}
