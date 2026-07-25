package com.example.lifeos.ui.screens.notes

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.notes.DrawingFileStore
import com.example.lifeos.data.notes.NoteRepository
import com.example.lifeos.data.notes.NoteType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class DrawStroke(
    val points: List<Offset>,
    val color: Color,
    val widthPx: Float,
)

val drawingColors = listOf(
    Color(0xFF000000),
    Color(0xFFE53935),
    Color(0xFF1E88E5),
    Color(0xFF43A047),
    Color(0xFFFB8C00),
    Color(0xFF8E24AA),
)

val drawingWidths = listOf(4f, 8f, 14f)

@HiltViewModel
class DrawingNoteViewModel @Inject constructor(
    private val repository: NoteRepository,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    // "noteId" matches the nav argument key declared for this route in LifeOSNavHost;
    // -1L is the route's "no note" default (Hilt reads it straight from SavedStateHandle).
    private val noteId: Long? = savedStateHandle.get<Long>("noteId")?.takeIf { it != -1L }

    var title by mutableStateOf("")
        private set
    var strokes by mutableStateOf<List<DrawStroke>>(emptyList())
        private set
    var selectedColor by mutableStateOf(drawingColors.first())
        private set
    var selectedWidth by mutableFloatStateOf(drawingWidths[1])
        private set
    var backgroundBitmap by mutableStateOf<ImageBitmap?>(null)
        private set
    var isLoading by mutableStateOf(noteId != null)
        private set

    val isEditing: Boolean get() = noteId != null

    init {
        if (noteId != null) {
            viewModelScope.launch {
                repository.getNoteById(noteId)?.let { note -> title = note.title }
                withContext(Dispatchers.IO) {
                    DrawingFileStore.load(context, noteId)
                }?.let { bitmap -> backgroundBitmap = bitmap.asImageBitmap() }
                isLoading = false
            }
        }
    }

    fun onTitleChange(value: String) {
        title = value
    }

    fun onColorSelected(value: Color) {
        selectedColor = value
    }

    fun onWidthSelected(value: Float) {
        selectedWidth = value
    }

    fun addStroke(stroke: DrawStroke) {
        strokes = strokes + stroke
    }

    fun undo() {
        if (strokes.isNotEmpty()) {
            strokes = strokes.dropLast(1)
        } else {
            backgroundBitmap = null
        }
    }

    fun clear() {
        strokes = emptyList()
        backgroundBitmap = null
    }

    fun save(canvasSize: IntSize, onSaved: () -> Unit) {
        if (title.isBlank() && strokes.isEmpty() && backgroundBitmap == null) return
        if (canvasSize.width <= 0 || canvasSize.height <= 0) return
        viewModelScope.launch {
            val savedNoteId = repository.saveNote(noteId, title, "", "DEFAULT", NoteType.DRAWING.name)
            val bitmap = renderToBitmap(canvasSize, backgroundBitmap, strokes)
            withContext(Dispatchers.IO) {
                DrawingFileStore.save(context, savedNoteId, bitmap)
            }
            onSaved()
        }
    }

    fun delete(onDeleted: () -> Unit) {
        val id = noteId ?: return
        viewModelScope.launch {
            repository.deleteNote(id)
            withContext(Dispatchers.IO) { DrawingFileStore.delete(context, id) }
            onDeleted()
        }
    }
}

private fun renderToBitmap(size: IntSize, background: ImageBitmap?, strokes: List<DrawStroke>): Bitmap {
    val bitmap = Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap.asImageBitmap())
    val drawScope = CanvasDrawScope()
    drawScope.draw(
        density = Density(1f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = canvas,
        size = Size(size.width.toFloat(), size.height.toFloat())
    ) {
        drawRect(color = Color.White)
        background?.let { drawImage(it) }
        strokes.forEach { stroke -> drawStroke(stroke) }
    }
    return bitmap
}

fun androidx.compose.ui.graphics.drawscope.DrawScope.drawStroke(stroke: DrawStroke) {
    if (stroke.points.size > 1) {
        val path = Path().apply {
            moveTo(stroke.points.first().x, stroke.points.first().y)
            stroke.points.drop(1).forEach { point -> lineTo(point.x, point.y) }
        }
        drawPath(
            path = path,
            color = stroke.color,
            style = Stroke(width = stroke.widthPx, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    } else if (stroke.points.size == 1) {
        drawCircle(color = stroke.color, radius = stroke.widthPx / 2, center = stroke.points.first())
    }
}
