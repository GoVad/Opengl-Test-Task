package com.example.opengl_testapplication

class Plane (coord:FloatArray,color:FloatArray){

    private var Coord = FloatArray(12)
    private var Color = FloatArray(4)

    init {
        Coord = coord
        Color = color
    }

    fun draw()
    {
        val coord1 = Coord.copyOfRange(0,9)
        val coord2 = Coord.copyOfRange(0,3).plus(Coord.copyOfRange(6,12))
        Triangle(coord1,Color).draw()
        Triangle(coord2,Color).draw()
    }
}