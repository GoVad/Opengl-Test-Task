package com.example.opengl_testapplication

class Plane (var coord:FloatArray,var color:FloatArray){

    private var t1: Triangle ?= null
    private var t2: Triangle ?= null

    init {
        val coord1 = coord.copyOfRange(0,9)
        val coord2 = coord.copyOfRange(0,3).plus(coord.copyOfRange(6,12))
        t1 = Triangle(coord1,color)
        t2 = Triangle(coord2,color)
    }

    fun draw(mvpMatrix: FloatArray)
    {
        t1?.draw(mvpMatrix)
        t2?.draw(mvpMatrix)
    }
}