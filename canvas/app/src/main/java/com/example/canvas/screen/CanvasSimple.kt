package com.example.canvas.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.canvas.domain.model.ChartData
import com.example.canvas.utils.ChartUtils
import kotlin.math.roundToInt

@Composable
fun CanvasSimple(
    viewModel: ChartViewModel = viewModel()
){
    val chartData by viewModel.chartData.collectAsStateWithLifecycle()

    Column(
        Modifier.statusBarsPadding()
    ) {
        Text("thats going to be a canvas")
        LineChart(chartData)
    }
}


@Composable
fun LineChart(chartData: List<ChartData>){
    val height = ChartUtils.DEFAULT_CHART_HEIGHT
    val paddingX = ChartUtils.CHART_PADDING
    val paddingY = ChartUtils.CHART_PADDING

    val tags = chartData.map{ it.tag }
    val values = chartData.map{ it.dataValue.toFloat() }

    // getAxisValues(850) // [0, 100, 200, 300, 400, 500, 600, 700, 800, 900]
    fun getAxisValues(maxValue: Int, step: Int = 100): List<Int> {
        val roundedMax = ((maxValue + step - 1) / step) * step // round up to next multiple of step
        return (0..roundedMax step step).toList()
    }

    // yes !
    fun getOffsetTop(value: Float, maxValue: Float, chartHeight: Float): Float =
        (chartHeight * (1f - value / maxValue))

    val offsets = remember{ mutableListOf<Offset>() }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .padding(ChartUtils.CHART_PADDING)
    ) {

        val gridHeight = height.toPx()
        val gridWith = size.width   // entire width

        val maxValue = values.maxByOrNull {
            it.toFloat().roundToInt()
        } ?: 0f

        val verticalLinesDistance = (maxValue.toInt() / 100).toFloat()
        //[0,100,200, .... (maxValue=850) 900]
        val valueLabels = getAxisValues(maxValue = maxValue.toInt())

        val xAxisSpacing = gridWith / (values.size-1)
        val yAxisSpacing = gridHeight / valueLabels.size-1

        values.forEachIndexed { i, it ->
            val offsetRight = i * xAxisSpacing
            val offsetTop = getOffsetTop(it, maxValue, gridHeight)
            offsets.add(Offset(x = offsetRight, y = offsetTop))
        }

        // draw the vertical lines
        for(i in 0 until chartData.size){
            val xOffset = xAxisSpacing * i
            drawLine(
                color = Color.Black,
                start = Offset(xOffset, 0f),
                end = Offset(xOffset, gridHeight),
                strokeWidth = 2f
            )
        }

        // draw the horizontal lines
        for(i in 0 until valueLabels.size){
            val xOffset = 0f
            val yOffset = yAxisSpacing * i
            drawLine(
                color = Color.DarkGray,
                start = Offset(xOffset, yOffset),
                end = Offset(gridWith, yOffset),
                strokeWidth = 2f
            )
        }

    }




}