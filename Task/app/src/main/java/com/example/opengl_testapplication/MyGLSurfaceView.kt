package  com.example.opengl_testapplication

import android.content.Context
import android.content.res.Resources
import android.opengl.GLSurfaceView
import kotlin.concurrent.thread
import kotlin.coroutines.*
import kotlin.random.Random

class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {

    //обьект класса исполняющего отрисовку
    private val renderer: MyGLRenderer

    //инициализация класса сцены с установкой обьекта исполняющего отрисовку и версии OpenglES
    init {
        setEGLContextClientVersion(2)
        renderer = MyGLRenderer()
        setRenderer(renderer)
    }
    //функция для включения/выключения эффекта блюра
    fun flipBlur() {
        var postRenderer = renderer.getmPostRenderer()
        postRenderer.enableBlur=!postRenderer.enableBlur
    }
    //функция для включения/выключения эффекта виньетки
    fun flipVign() {
        var postRenderer = renderer.getmPostRenderer()
        postRenderer.enableVign=!postRenderer.enableVign
    }
    //функция для вращения треугольника на определенный угол по оси Z
    fun rotateTriangle(angle:Float) {
        renderer.angle = angle
    }
    //функция установки значения яркости
    fun changeBrightness(bright: Float) {
        renderer.getmPostRenderer().setBrightness(bright)
    }
    //функция увеличения/уменьшения текстуры переднего плана на scale
    fun changeScale(scale:Float){
        renderer.scale = scale
    }

    //функция для смены цвета передней текстуры (де факто разрешает пикинг)
    fun changeTriangleColor(mousePos: FloatArray) {
        //смена ориентации позиционирования мышки с левого верхнего угла на левый нижний угол
        mousePos[1] =
            Resources.getSystem().displayMetrics.heightPixels-MyGLUtils.topOffset-mousePos[1]
        //присваивание координат проверяемого пикселя
        renderer.mouseCoord = mousePos
        //поднимаем флаг разрешения пикинга
        renderer.enablePicking = true
    }
    //функция для задания смещения позиции передней текстуры на xOffset относительно
    //начальной позиции
    fun changePos(xOffset:Float) {
        renderer.xOffset = xOffset
    }
}