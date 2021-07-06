package com.example.opengl_testapplication

import android.content.res.Resources
import android.opengl.*
import java.nio.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.random.Random


class MyGLRenderer() : GLSurfaceView.Renderer {

    //текстуры прикрепляемые к фреймбуферам
    private var screenTex = IntArray(2)
    //фреймбуферы
    private var FBO = IntArray(2)

    //матрица проекции
    private var vPMatrix = FloatArray(16)
    //матрица проекции для передней текстуры
    private var triangleVMatrix = FloatArray(16)
    //матрицы вида и процирования для создания матрицы проекции
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    //переменные хранящие примитивы
    private lateinit var mPrimitiveFront:Primitive
    private lateinit var mPrimitiveBack:Primitive
    //переменная хранящая пострендер
    private lateinit var mPostRenderer:PostRender
    //обьект класса с программами
    private lateinit var programs:ProgramClass

    var angle = 0f
    var scale = 1f
    var xOffset = 0f
    //переменная разрешения пикинга
    var enablePicking = false
    //координаты мышки на текстуре
    var mouseCoord = FloatArray(2)

    //геттер пострендера
    fun getmPostRenderer(): PostRender {
        return mPostRenderer
    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        //цвет фона сцены
        GLES20.glClearColor(0.9f, 0.9f, 0.9f, 1.0f)

        //массивы для вершин и их цвета

        val coordsTriangle = floatArrayOf(
            0.0f, 0.622008459f, -0.1f,      // top
            -0.5f, -0.311004243f, -0.2f,    // bottom left
            0.5f, -0.311004243f, -0.3f      // bottom right
        )

        val backCoord = floatArrayOf(
            -1f,1f,1f,
            -1f,-1f,-1f,
            1f,-1f,-1f,
            1f,1f,1f,
        )

        val colorFront = floatArrayOf(
            0.9f,0f,0f,1f,
            0f,0.9f,0f,1f,
            0f,0f,0.9f,1f)

        val colorBack = floatArrayOf(
            0.4f,0f,0f,1f,
            0.4f,0f,0f,1f,
            0.4f,0f,0f,1f,
            0.4f,0f,0f,1f
        )

        //настройка примитивов
        mPrimitiveFront = Primitive(coordsTriangle,colorFront,3,GLES20.GL_TRIANGLES)
        mPrimitiveBack = Primitive(backCoord,colorBack,4,GLES20.GL_TRIANGLE_FAN)
        //настрйока постобработчика и класса с программами
        mPostRenderer = PostRender()
        programs = ProgramClass()

        mPostRenderer.enableBright = true

        //генерируем 2 пары буфера+текстуры
        GLES20.glGenFramebuffers(2,FBO,0)
        GLES20.glGenTextures(2,screenTex,0)
        //конфигурируем созданные фреймбуферы
        createFrameBuffer(FBO[0],screenTex[0])
        createFrameBuffer(FBO[1],screenTex[1])
    }

    //функция для создания фреймбуфера, для создания нужен id текстуры и id буфера
    private fun createFrameBuffer(fbo:Int, screenTex:Int){
        //биндим переданный буфер
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,fbo)
        //биндим переданную текстуру
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,screenTex)

        //задаем параметры фильтрации(!!!ОБЯЗАТЕЛЬНО!!!)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR)

        //прикрепляем пустую текстуру к id переданной в параметрах текстуры
        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D,
            0,
            GLES20.GL_RGBA,
            Resources.getSystem().displayMetrics.widthPixels,
            Resources.getSystem().displayMetrics.heightPixels-MyGLUtils.topOffset.toInt(),
            0,
            GLES20.GL_RGBA,
            GLES20.GL_UNSIGNED_BYTE,
            null
        )
        //биндим эту текстуру к фреймбуферу
        GLES20.glFramebufferTexture2D(
            GLES20.GL_FRAMEBUFFER,
            GLES20.GL_COLOR_ATTACHMENT0,
            GLES20.GL_TEXTURE_2D,
            screenTex,
            0
        )
        //биндим стандартные текстуру и буфер, т.к. настройка закончена
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)
    }

    private fun drawScene() {
        //устанавливаем точку взгляда, формируя матрицу
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -6f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        //создаем матрицу проекции с точки взгляда на сцену
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        //рисуем задний примитив согласно проекции
        mPrimitiveBack.draw(programs.simpleColorProgram,vPMatrix)

        //редактируем проекцию для примитива на переднем фоне
        triangleVMatrix = vPMatrix.copyOf()
        //увеличиваем проекцию согласно выбранному значению scale
        Matrix.scaleM(triangleVMatrix,0,scale,scale,scale)
        //Передвижение матрицы проекции на значение отступа xOffset
        Matrix.translateM(triangleVMatrix,0,xOffset,0f,0f)
        //задаем матрицу угол поворота по оси Z
        Matrix.rotateM(triangleVMatrix,0,angle,0f,0f,1f)
        //рисуем передний примитив
        mPrimitiveFront.draw(programs.simpleColorProgram,triangleVMatrix)
    }

    //стандартная функция отрисовки фрейма
    override fun onDrawFrame(unused: GL10) {
        //подключаем буфер постобработки
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,FBO[0])
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        //рисуем основную сцену
        drawScene()
        //если активен пикинг, то выполняем его с координатами мышки
        if(enablePicking)
            colorPicking(mouseCoord[0].toInt(),mouseCoord[1].toInt())
        //подключаем дефолтный буфер для отрисовки постобработки
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        //назначем активную текстуру
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
        //вызываем класс постобработчика
        mPostRenderer.postRender(screenTex[0],1)
    }

    //функция пикинга, нужна для определения нажатия на переднюю фигуру
    //принимает координаты пикселя для проверки на цвет
    private fun colorPicking(x:Int,y:Int) {
        //подключаем фреймбуфер хранящий текстуру для пикинга
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,FBO[1])
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        //сохранаяем изначальные цвета фигур
        val backColor = mPrimitiveBack.color.copyOf()
        val frontColor = mPrimitiveFront.color.copyOf()

        //задаем фигурам цвета для пикинга
        mPrimitiveFront.color = floatArrayOf(
            40f/256f,0f,0f,1f,
            40f/256f,0f,0f,1f,
            40f/256f,0f,0f,1f
        )
        mPrimitiveBack.color = floatArrayOf(
            120f/256f,0f,0f,1f,
            120f/256f,0f,0f,1f,
            120f/256f,0f,0f,1f,
            120f/256f,0f,0f,1f
        )
        //отрисовываем сцену
        drawScene()
        //возвращаем изначальные цвета фигурам
        mPrimitiveFront.color = frontColor
        mPrimitiveBack.color = backColor

        //выделяем память буферу с цветом пикселя
        var buffer = ByteBuffer.allocate(
            4
        )
        //считываем пиксель из буфера
        GLES20.glReadPixels(
            x,y,
            1,
            1,
            GLES20.GL_RGBA,
            GLES20.GL_UNSIGNED_BYTE,
            buffer
        )
        //проверка равен ли полученный пиксель цвету искомой фигуры
        if (buffer.get(0).toUByte().toFloat() / 256f == 40f / 256f)
            //если да, то назначем фигуре новый рандомный цвет
            mPrimitiveFront.color = floatArrayOf(
                Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f,
                Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f,
                Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f
            )
        //выключаем пикинг, чтобы сцена не рендарилась впустую
        enablePicking = false
    }

    //стандартная функция для создания матрицы проекции
    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    //функция для включения пикинга при нажатии на переднюю фигуру
    fun changeTriangleColor(mousePos: FloatArray)
    {
        mouseCoord = mousePos

    }

}