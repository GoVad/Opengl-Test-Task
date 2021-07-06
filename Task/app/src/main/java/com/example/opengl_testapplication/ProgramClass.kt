package com.example.opengl_testapplication


class ProgramClass {
    //угловой шейдер нужный для отрисовки обьекта с цветными углами
    //согласно матрице проекции
    private var vertShader =
        "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
                "attribute vec4 color;"+
                "varying vec4 vColor;"+
                "void main() {" +
                "  gl_Position = uMVPMatrix * vPosition;" +
                "  vColor = color;" +
                "}"
    //обычный фрагментный шейдер устанавливающий переданный цвет
    private val fragShader =
        "precision mediump float;"+
                "varying vec4 vColor;"+
                "void main() {"+
                "gl_FragColor = vColor;" +
                "}"

    var simpleColorProgram: Int = 0

    init {
        //при создании класса инициализируем лишь одну программу
        simpleColorProgram = MyGLUtils.generateProgram(fragShader,vertShader)
    }
}