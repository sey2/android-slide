import android.graphics.Paint
import android.graphics.Path
import com.example.slide.db.SlideEntity
import com.example.slide.model.Slide
import com.example.slide.drawing.PathData
import com.example.slide.drawing.PathOperation
import com.example.slide.drawing.PathOperationAdapter
import com.google.gson.GsonBuilder

class DrawingSlide(
    override val id: String,
    override var alpha: Int = 0,
    override var sideLength: Int,
    var backgroundColor: Int?,
    var path: Path = Path(),
    val paint: Paint,
    var isEditable: Boolean = true,
    var minX: Float = Float.MAX_VALUE,
    var minY: Float = Float.MAX_VALUE,
    var maxX: Float = Float.MIN_VALUE,
    var maxY: Float = Float.MIN_VALUE,
    var pathData: PathData = PathData(mutableListOf()),
    override var lastPosition: Pair<Float, Float> = Pair(0f, 0f)
) : Slide {
    override fun toEntity(): SlideEntity {
        return SlideEntity(
            id = id,
            sideLength = sideLength,
            backgroundColor = backgroundColor,
            alpha = alpha,
            type = "DrawingSlide",
            imageBytes = null,
            pathData = GsonBuilder()
                .registerTypeAdapter(PathOperation::class.java, PathOperationAdapter())
                .create()
                .toJson(pathData),
            minX = minX,
            minY = minY,
            maxX = maxX,
            maxY = maxY,
            lastX = lastPosition.first,
            lastY = lastPosition.second
        )
    }
}
