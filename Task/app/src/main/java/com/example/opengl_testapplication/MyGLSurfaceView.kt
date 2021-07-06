package  com.example.opengl_testapplication

import android.content.Context
import android.content.res.Resources
import android.opengl.GLSurfaceView
import android.util.Log

class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer: MyGLRenderer

    init {
        setEGLContextClientVersion(2)
        renderer = MyGLRenderer()
        setRenderer(renderer)
    }

    fun flipBlur() {
        var postRenderer = renderer.getmPostRenderer()
        postRenderer.enableBlur=!postRenderer.enableBlur
    }

    fun flipVign() {
        var postRenderer = renderer.getmPostRenderer()
        postRenderer.enableVign=!postRenderer.enableVign
    }

    fun rotateTriangle(angle:Float) {
        renderer.angle = angle
    }

    fun changeBrightness(bright: Float) {
        renderer.getmPostRenderer().setBrightness(bright)
    }

    fun changeScale(scale:Float){
        renderer.scale = scale
    }

    fun changeTriangleColor(mousePos: FloatArray) {
        mousePos[1] =
            Resources.getSystem().displayMetrics.heightPixels-MyGLUtils.topOffset-mousePos[1]
        renderer.changeTriangleColor(mousePos)
    }
}