import com.example.slide.MySlideApplication
import com.example.slide.factory.SlideCreationFactory
import com.example.slide.model.Slide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class SlideRepository {
    private val slideDatabase = MySlideApplication.instance.slideDatabase

    fun saveSlides(slides: List<Slide>) {
        CoroutineScope(Dispatchers.IO).launch {
            slides.forEach { slide ->
                val slideEntity = slide.toEntity()
                slideDatabase.slideDao().insert(slideEntity)
            }
        }
    }

    suspend fun loadSlides(): List<Slide> {
        val slideEntities = withContext(Dispatchers.IO) {
            slideDatabase.slideDao().getAll()
        }
        return slideEntities.map { SlideCreationFactory.createSlideFromEntity(it) }
    }

    fun createRandomSlide(sideLength: Int): Slide {
        return when (Random.nextInt(3)) {
            0 -> SlideCreationFactory.createSquareSlide(sideLength)
            1 -> SlideCreationFactory.createImageSlide(sideLength)
            2 -> SlideCreationFactory.createDrawingSlide(sideLength)
            else -> throw IllegalArgumentException("Unknown slide type")
        }
    }

    fun clearSlides() {
        CoroutineScope(Dispatchers.IO).launch {
            slideDatabase.slideDao().deleteAll()
        }
    }
}