package com.example.opengl_testapplication

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var gLView: MyGLSurfaceView
    private lateinit var t:Thread

    public override fun onResume() {
        super.onResume()
    }

    public override fun onPause() {
        super.onPause()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceType", "ClickableViewAccessibility")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //устанаваливаем наш активити
        setContentView(R.layout.activity_main)

        //создаем обьект класса сцены с обьектом рендера
        gLView = MyGLSurfaceView(this)
        //добавляем к этому обьекту слушатель событий для нажатий на экран
        gLView.setOnTouchListener { _, event ->
            gLView.changeTriangleColor(
                floatArrayOf(
                    event.x,
                    event.y)
            )
            true
        }

        //переменная разрешающая рандомные события
        var enableRandom = false

        //поток создающий рандомные события в сцене
        t = Thread {
            while(true) {
                if(enableRandom) {
                    when (Random.nextInt(7)) {
                        0 -> gLView.changeBrightness(2f * Random.nextFloat())
                        1 -> gLView.changeScale(2f * Random.nextFloat())
                        2 -> gLView.changePos(Random.nextInt(-2, 2) * Random.nextFloat())
                        3 -> gLView.rotateTriangle(Random.nextFloat() * 360f)
                        4 -> gLView.flipBlur()
                        5 -> gLView.flipVign()
                    }
                }
                Thread.sleep((Random.nextFloat()*10000f).toLong())
            }
        }
        t.start()

        //прикрепляем обьект сцены к фрейму в активити
        val frame = findViewById<FrameLayout>(R.id.myFrameLayout)
        frame.addView(gLView)
        //устанавливаем что сцена должна рисоваться снизу под всему Ui элементами
        gLView.setZOrderMediaOverlay(true)
        //вызываем функцию создания ui обьектов
        createUi()

        //добавляем слушатели событий нажатия на определенные кнопки
        findViewById<Button>(R.id.blurButton).setOnClickListener {
            gLView.flipBlur()
        }
        findViewById<Button>(R.id.vignButton).setOnClickListener {
            gLView.flipVign()
        }
        findViewById<Button>(R.id.randomButton).setOnClickListener {
            enableRandom = !enableRandom
        }
    }


    @SuppressLint("ResourceType", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createUi()
    {
        //переменные для создания слайдеров
        val width = 200
        val height = 30
        var topMargin = 5

        //создание слайдеров и прикрпеление к ним слушателей событий
        var sb = SeekBar(this).also {
            it.max = 360
            it.min = 0
            it.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener{
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        gLView.rotateTriangle(progress.toFloat())
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    }
                }
            )
        }
        val params = RelativeLayout.LayoutParams(width,height)
        params.topMargin = topMargin
        addContentView(
            sb,params
        )
        //добавляем отступ вниз чтобы рисовать следующий слайдер
        topMargin+=height

        sb = SeekBar(this).also {
            it.max = 100
            it.min = 1
            it.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        gLView.changeBrightness(progress.toFloat()/50f)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    }

                }
            )
        }
        params.topMargin = topMargin
        addContentView(
            sb,params
        )
        topMargin+=height

        sb = SeekBar(this).also {
            it.max = 100
            it.min = 1
            it.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        gLView.changeScale(progress.toFloat()/30f)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    }

                }
            )
        }
        params.topMargin = topMargin
        addContentView(
            sb,params
        )
        topMargin+=height
        sb = SeekBar(this).also {
            it.max = 35
            it.min = -35
            it.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        gLView.changePos(progress.toFloat()/-20f)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    }

                }
            )
        }
        params.topMargin = topMargin
        addContentView(
            sb,params
        )
        //выводимое на активити текстовое поле
        val tb = TextView(this).also {
            it.text = "1 слайдер вращает фигуру\n2 слайдер меняет яркость\n" +
                    "3 слайдер меняет размер фиугры\n4 слайдер двигает фигуру"
        }
        //параметры текстового поля
        val textParams =
            //заполнение по ширине и высоте контента
            RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).also {
                //отступ снизу 240 пикселей
                it.topMargin = Resources.getSystem().displayMetrics.heightPixels-240
                //отступ слева 10 пикселей
                it.leftMargin = 10
            }
        //добавление текста на экран
        addContentView(
            tb,textParams
        )
    }
}
