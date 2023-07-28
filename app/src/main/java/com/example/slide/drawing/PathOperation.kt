package com.example.slide.drawing

sealed class PathOperation {
    data class MoveTo(var x: Float = 0f, var y: Float = 0f) : PathOperation()
    data class LineTo(var x: Float = 0f, var y: Float = 0f) : PathOperation()
    data class QuadTo(
        var lastX: Float = 0f,
        var lastY: Float = 0f,
        var endX: Float = 0f,
        var endY: Float = 0f
    ) : PathOperation()
}
