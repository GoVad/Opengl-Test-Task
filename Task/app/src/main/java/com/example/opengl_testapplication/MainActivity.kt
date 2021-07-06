package com.example.opengl_testapplication

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    private lateinit var gLView: MyGLSurfaceView
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceType", "ClickableViewAccessibility")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        gLView = MyGLSurfaceView(this)
        gLView.setOnTouchListener { _, event ->
            gLView.changeTriangleColor(
                floatArrayOf(
                    event.x,
                    event.y)
            )
            true
        }
        var frame = findViewById<FrameLayout>(R.id.myFrameLayout)
        frame.addView(gLView)
        gLView.setZOrderMediaOverlay(true)

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
        var params = RelativeLayout.LayoutParams(200,30)
        params.topMargin = 5
        addContentView(
            sb,params
        )

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
        params.topMargin = 40
        addContentView(
            sb,params
        )

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
        params.topMargin = 75
        addContentView(
            sb,params
        )

        findViewById<Button>(R.id.blurButton).setOnClickListener {
            gLView.flipBlur()
        }
        findViewById<Button>(R.id.vignButton).setOnClickListener {
            gLView.flipVign()
        }
    }

}
