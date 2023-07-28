package com.example.slide.drawing

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter


class PathOperationAdapter : TypeAdapter<PathOperation>() {
    override fun write(out: JsonWriter, value: PathOperation) {
        out.beginObject()
        when (value) {
            is PathOperation.MoveTo -> {
                out.name("type").value("MoveTo")
                out.name("x").value(value.x)
                out.name("y").value(value.y)
            }
            is PathOperation.LineTo -> {
                out.name("type").value("LineTo")
                out.name("x").value(value.x)
                out.name("y").value(value.y)
            }
            is PathOperation.QuadTo -> {
                out.name("type").value("QuadTo")
                out.name("x").value(value.lastX)
                out.name("y").value(value.lastY)
                out.name("endX").value(value.endX)
                out.name("endY").value(value.endY)
            }
        }
        out.endObject()
    }

    override fun read(input: JsonReader): PathOperation {
        var type: String? = null
        var x = 0f
        var y = 0f
        var endX = 0f
        var endY = 0f

        input.beginObject()
        while (input.hasNext()) {
            when (input.nextName()) {
                "type" -> type = input.nextString()
                "x" -> x = input.nextDouble().toFloat()
                "y" -> y = input.nextDouble().toFloat()
                "endX" -> endX = input.nextDouble().toFloat()
                "endY" -> endY = input.nextDouble().toFloat()
            }
        }
        input.endObject()

        return when (type) {
            "MoveTo" -> PathOperation.MoveTo(x, y)
            "LineTo" -> PathOperation.LineTo(x, y)
            "QuadTo" -> PathOperation.QuadTo(x, y, endX, endY)
            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }
}
