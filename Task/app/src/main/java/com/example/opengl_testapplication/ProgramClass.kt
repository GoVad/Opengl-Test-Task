package com.example.opengl_testapplication

class ProgramClass {
    private var vertShader =
        "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
                "attribute vec4 color;"+
                "varying vec4 vColor;"+
                "void main() {" +
                "  gl_Position = uMVPMatrix * vPosition;" +
                "  vColor = color;" +
                "}"

    private val fragShader =
        "precision mediump float;"+
                "varying vec4 vColor;"+
                "void main() {"+
                "gl_FragColor = vColor;" +
                "}"

    var simpleColorProgram: Int = 0

    init {
        simpleColorProgram = MyGLUtils.generateProgram(fragShader,vertShader)
    }
}