import android.graphics.Paint
import android.graphics.Path
import com.example.slide.model.Slide

class DrawingSlide(
    override val id: String,
    override var alpha: Int = 0,
    override var sideLength: Int,
    val path: Path,
    val paint: Paint,
    var isEditable: Boolean = true,
    var minX: Float = Float.MAX_VALUE,
    var minY: Float = Float.MAX_VALUE,
    var maxX: Float = Float.MIN_VALUE,
    var maxY: Float = Float.MIN_VALUE,
) : Slide
