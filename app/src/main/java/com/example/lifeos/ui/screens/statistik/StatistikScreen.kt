package com.example.lifeos.ui.screens.statistik

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifeos.core.strings.LifeOSStrings
import com.example.lifeos.core.strings.LocalStrings
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatistikScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    val viewModel: StatistikViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val dayFormatter = remember(strings.localeTag) {
        DateTimeFormatter.ofPattern("EEE", Locale.forLanguageTag(strings.localeTag))
    }
    val today = LocalDate.now().toEpochDay()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.statistik.statistikTitle) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.shared.back)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            StatTileGrid(state, strings)
            PomodoroLineChartCard(
                title = strings.statistik.statistikPomodoroChartTitle,
                data = state.pomodoroLast7Days,
                dayLabel = { day -> if (day == today) strings.statistik.statistikTodayLabel else LocalDate.ofEpochDay(day).format(dayFormatter) },
                todayEpochDay = today
            )
            HabitDonutCard(
                title = strings.statistik.statistikHabitChartTitle,
                completed = state.habitsCompletedToday,
                total = state.activeHabits,
                avgRate = state.habitRateLast7Days
                    .takeIf { it.isNotEmpty() }
                    ?.map { it.value.toDouble() }
                    ?.average()
                    ?.roundToInt()
                    ?.coerceIn(0, 100) ?: 0,
                strings = strings
            )
        }
    }
}

private data class StatTile(val label: String, val value: String, val icon: ImageVector)

@Composable
private fun StatTileGrid(state: StatistikUiState, strings: LifeOSStrings) {
    val tiles = listOf(
        StatTile(strings.statistik.statistikTotalNotes, state.totalNotes.toString(), Icons.AutoMirrored.Filled.Notes),
        StatTile(strings.statistik.statistikTotalPomodoro, state.totalPomodoroSessions.toString(), Icons.Filled.Timer),
        StatTile(strings.statistik.statistikActiveHabits, state.activeHabits.toString(), Icons.Filled.CheckCircle),
        StatTile(strings.statistik.statistikLongestStreak, strings.statistik.statistikStreakTemplate.format(state.longestStreak), Icons.Filled.LocalFireDepartment),
        StatTile(strings.statistik.statistikGoalsAchieved, state.goalsAchieved.toString(), Icons.Filled.Flag),
    )
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        tiles.chunked(2).forEach { rowTiles ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowTiles.forEach { tile ->
                    StatTileCard(tile, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun StatTileCard(tile: StatTile, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = tile.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.height(20.dp)
            )
            Text(
                text = tile.value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = tile.label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun PomodoroLineChartCard(
    title: String,
    data: List<DayValue>,
    dayLabel: (Long) -> String,
    todayEpochDay: Long,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(16.dp))
            DailyLineChart(data = data, dayLabel = dayLabel, todayEpochDay = todayEpochDay)
        }
    }
}

@Composable
private fun DailyLineChart(
    data: List<DayValue>,
    dayLabel: (Long) -> String,
    todayEpochDay: Long,
) {
    if (data.isEmpty()) return
    val lineColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.surfaceVariant
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val todayLabelColor = MaterialTheme.colorScheme.onSurface
    val maxValue = (data.maxOfOrNull { it.value } ?: 0f).coerceAtLeast(1f)
    val textMeasurer = rememberTextMeasurer()
    val valueLabelStyle = MaterialTheme.typography.labelSmall

    val reveal = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        reveal.animateTo(1f, animationSpec = tween(durationMillis = 900, easing = EaseOutCubic))
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        val chartTop = 22.dp.toPx()
        val baseline = size.height - 2.dp.toPx()
        val columnWidth = size.width / data.size
        fun xAt(index: Int) = columnWidth * (index + 0.5f)
        fun yAt(value: Float) = baseline - (value / maxValue).coerceIn(0f, 1f) * (baseline - chartTop)

        listOf(baseline, (baseline + chartTop) / 2f, chartTop).forEach { y ->
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        val linePath = Path()
        data.forEachIndexed { index, day ->
            val x = xAt(index)
            val y = yAt(day.value)
            if (index == 0) linePath.moveTo(x, y) else linePath.lineTo(x, y)
        }
        val areaPath = Path().apply {
            addPath(linePath)
            lineTo(xAt(data.lastIndex), baseline)
            lineTo(xAt(0), baseline)
            close()
        }

        clipRect(right = size.width * reveal.value) {
            drawPath(
                path = areaPath,
                brush = Brush.verticalGradient(
                    colors = listOf(lineColor.copy(alpha = 0.25f), lineColor.copy(alpha = 0f)),
                    startY = chartTop,
                    endY = baseline
                )
            )
            drawPath(
                path = linePath,
                color = lineColor,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
            data.forEachIndexed { index, day ->
                val isToday = day.dateEpochDay == todayEpochDay
                drawCircle(
                    color = lineColor,
                    radius = if (isToday) 5.dp.toPx() else 4.dp.toPx(),
                    center = Offset(xAt(index), yAt(day.value))
                )
            }
        }

        // Selective direct labels: only today and the week's peak, once the reveal settles.
        if (reveal.value >= 1f) {
            val maxIndex = data.indices.maxBy { data[it].value }
            val todayIndex = data.indexOfFirst { it.dateEpochDay == todayEpochDay }
            val labelIndices = buildSet {
                add(maxIndex)
                if (todayIndex >= 0) add(todayIndex)
            }
            labelIndices.forEach { index ->
                val isToday = index == todayIndex
                val layout = textMeasurer.measure(
                    text = AnnotatedString(data[index].value.toInt().toString()),
                    style = valueLabelStyle.copy(
                        color = if (isToday) todayLabelColor else labelColor,
                        fontWeight = if (isToday) FontWeight.SemiBold else FontWeight.Normal
                    )
                )
                drawText(
                    textLayoutResult = layout,
                    topLeft = Offset(
                        x = (xAt(index) - layout.size.width / 2f).coerceIn(0f, size.width - layout.size.width),
                        y = (yAt(data[index].value) - layout.size.height - 6.dp.toPx()).coerceAtLeast(0f)
                    )
                )
            }
        }
    }
    Spacer(Modifier.height(6.dp))
    Row(modifier = Modifier.fillMaxWidth()) {
        data.forEach { day ->
            val isToday = day.dateEpochDay == todayEpochDay
            Text(
                text = dayLabel(day.dateEpochDay),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun HabitDonutCard(
    title: String,
    completed: Int,
    total: Int,
    avgRate: Int,
    strings: LifeOSStrings,
) {
    val targetRate = if (total > 0) completed * 100f / total else 0f
    val animatedRate by animateFloatAsState(
        targetValue = targetRate,
        animationSpec = tween(durationMillis = 800, easing = EaseOutCubic),
        label = "habitDonutRate"
    )
    val arcColor = MaterialTheme.colorScheme.primary
    val trackColor = MaterialTheme.colorScheme.surfaceVariant

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(132.dp), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 14.dp.toPx()
                        val inset = strokeWidth / 2f
                        val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                        drawArc(
                            color = trackColor,
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = Offset(inset, inset),
                            size = arcSize,
                            style = Stroke(width = strokeWidth)
                        )
                        val sweep = (animatedRate / 100f).coerceIn(0f, 1f) * 360f
                        if (sweep > 0f) {
                            drawArc(
                                color = arcColor,
                                startAngle = -90f,
                                sweepAngle = sweep,
                                useCenter = false,
                                topLeft = Offset(inset, inset),
                                size = arcSize,
                                style = Stroke(
                                    width = strokeWidth,
                                    cap = if (sweep >= 360f) StrokeCap.Butt else StrokeCap.Round
                                )
                            )
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${animatedRate.roundToInt()}%",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = strings.statistik.statistikTodayLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(Modifier.width(20.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    DonutLegendRow(
                        color = arcColor,
                        text = "${strings.statistik.statistikDonutCompletedLabel}: $completed"
                    )
                    DonutLegendRow(
                        color = trackColor,
                        text = "${strings.statistik.statistikDonutRemainingLabel}: ${(total - completed).coerceAtLeast(0)}"
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = strings.statistik.statistikDonutTodayTemplate.format(completed, total),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = strings.statistik.statistikDonutAvgTemplate.format(avgRate),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun DonutLegendRow(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
