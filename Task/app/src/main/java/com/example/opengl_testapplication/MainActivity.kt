package com.example.opengl_testapplication

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private lateinit var gLView: GLSurfaceView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gLView = MyGLSurfaceView(this)
        setContentView(gLView)
    }
}