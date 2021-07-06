package com.example.opengl_testapplication

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var gLView: MyGLSurfaceView
    lateinit var t:Thread

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
                    var a = Random.nextInt(7)
                    when (a) {
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
        var frame = findViewById<FrameLayout>(R.id.myFrameLayout)
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
        var params = RelativeLayout.LayoutParams(width,height)
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
    }
}
