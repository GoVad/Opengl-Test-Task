package com.example.opengl_testapplication

import android.content.res.Resources
import android.opengl.GLES20

class PostRender {

    //переменная с id программы
    private var prog = 0

    //флаги разрешения фильтров
    var enableBright = false
    var enableBlur = false
    var enableVign = false

    //переменная яркости и её сеттер
    private var brightVal = 1f
    fun setBrightness(nVal:Float)
    {
        brightVal = nVal
    }

    //переменная радиуса виньетки и её сеттер
    private var vignRadius = 0.9f
    fun setVignetteRadius(nVal:Float)
    {
        vignRadius = nVal
    }


    //угловой шейдер для пострендернига
    private val vertexShaderCode =
                "attribute vec4 vPosition;" +
                "attribute vec2 texCoord;"+
                "varying vec2 vTexCoord;"+
                "void main() {" +
                "gl_Position = vPosition;" +
                "  vTexCoord = texCoord;" +
                "}"

    //фрагментный шейдер с функциями создания фильтров
    private val fragmentShaderCode =
        "precision mediump float;"+
                "uniform sampler2D tex;"+
                "varying vec2 vTexCoord;" +

                "uniform int enBlur;"+
                "uniform int enBright;"+
                "uniform int enVign;"+

                "uniform vec2 resolution;" +
                "uniform float brightValue;" +
                "uniform float vignRadius;" +

                "vec4 blur(vec4 color){" +
                "float th = 1.0/resolution.y;" +
                "float tw = 1.0/resolution.x;" +
                    "for(float i = -4.0; i <=4.0;i+=1.0)" +
                    "   for(float j = -4.0; j <=4.0;j+=1.0)" +
                    "       color+=texture2D(tex,vec2(vTexCoord.x+tw*i,vTexCoord.y+th*j));" +
                    "color/=81.0;" +
                    "return color;" +
                "}" +

                "vec4 brightness(vec4 color){" +
                "return color*brightValue;" +
                "}" +

                "vec4 vignette(vec4 color){" +
                "vec2 pos = (gl_FragCoord.xy/resolution)-vec2(0.5);"+
                "float dist = length(pos);"+
                "float soft = 0.55;"+
                "float vign = smoothstep(vignRadius,vignRadius-soft,dist);"+
                "color.rgb = color.rgb-(1.0-vign);"+
                "return color;" +
                "}" +

                "void main() {" +
                "vec4 color = texture2D(tex,vTexCoord);" +
                "if(enBlur==1)color = blur(color);" +
                "if(enVign==1)color = vignette(color);" +
                "if(enBright==1)color = brightness(color);" +
                "gl_FragColor = color;" +
                "}"

    //массив точек для наложения текстуры
    private val fullscreenCoords = floatArrayOf(
        -1f,1f,
        -1f,-1f,
        1f,-1f,
        1f,1f
    )
    //координаты углов экрана
    var texCoords = floatArrayOf(
        0f,1f,
        0f,0f,
        1f,0f,
        1f,1f
    )

    //иницализация обьекта класса инициализирует для него программу
    init {
        prog = MyGLUtils.generateProgram(fragmentShaderCode,vertexShaderCode)
    }

    //функция пострендеринга
    fun postRender(screenTex:Int,texUnit:Int) {
        //создаем хендлеры указателей
        val vertHand = MyGLUtils.setAttribPointer(prog,"vPosition",2,fullscreenCoords)
        val texCoordHand = MyGLUtils.setAttribPointer(prog,"texCoord",2,texCoords)

        //указываем используемую программу
        GLES20.glUseProgram(prog)

        //биндим текстуру через юнит к программе
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,screenTex)
        //задаем различные юниформы необходимые шейдерам
        //текстура
        GLES20.glGetUniformLocation(prog,"tex").also {
            GLES20.glUniform1i(it,texUnit)
        }
        //яркость
        GLES20.glGetUniformLocation(prog,"brightValue").also {
            GLES20.glUniform1f(it, brightVal)
        }
        //радиус виньетки
        GLES20.glGetUniformLocation(prog,"vignRadius").also {
            GLES20.glUniform1f(it, vignRadius)
        }
        //флаг доступности блюра
        GLES20.glGetUniformLocation(prog,"enBlur").also {
            GLES20.glUniform1i(it, if(enableBlur) 1 else 0)
        }
        //м яркости
        GLES20.glGetUniformLocation(prog,"enBright").also {
            GLES20.glUniform1i(it, if(enableBright) 1 else 0)
        }
        //флаг доступности виньетки
        GLES20.glGetUniformLocation(prog,"enVign").also {
            GLES20.glUniform1i(it, if(enableVign) 1 else 0)
        }
        //разрешение экрана
        GLES20.glGetUniformLocation(prog,"resolution").also {
            GLES20.glUniform2f(it,
                Resources.getSystem().displayMetrics.widthPixels.toFloat(),
                Resources.getSystem().displayMetrics.heightPixels.toFloat()-MyGLUtils.topOffset,

            )
        }
        //разрешаем работму с указателями
        GLES20.glEnableVertexAttribArray(vertHand)
        GLES20.glEnableVertexAttribArray(texCoordHand)
        //рисуем квадрат из треугольников
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,4)
        //выключаем работу с указателями
        GLES20.glDisableVertexAttribArray(vertHand)
        GLES20.glDisableVertexAttribArray(texCoordHand)
    }
}