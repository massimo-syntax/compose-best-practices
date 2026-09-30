package com.example.canvas.screen

import android.R
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        // https://www.youtube.com/watch?v=zde9-P9SUoI&t=150s
        Surface(
            Modifier
            .background(Color.Red)
            .padding(ChartUtils.CHART_PADDING)
        ) {
            LineChart(chartData)
        }
    }
}


@Composable
fun LineChart(chartData: List<ChartData>){
    val height = ChartUtils.DEFAULT_CHART_HEIGHT


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
            .background(color = Color.Green)
            .height(height)
    ) {

        val lablesYWidth = 100
        val lablesBottom = 50

        val gridHeight = height.toPx() - lablesBottom
        val gridWidth = size.width - lablesYWidth  // entire width

        val maxValue = values.maxByOrNull {
            it.toFloat().roundToInt()
        } ?: 0f

        //[0,100,200, .... (maxValue=850) 900]
        val valueLabels = getAxisValues(maxValue = maxValue.toInt())

        val xAxisSpacing = gridWidth / (values.size-1)
        val yAxisSpacing = gridHeight / valueLabels.size-1


        // draw the vertical lines
        for(i in 0 until chartData.size){
            val xOffset = xAxisSpacing * i
            drawLine(
                color = Color.Black,
                start = Offset(xOffset, 0f),
                end = Offset(xOffset, gridHeight),
                strokeWidth = 1f
            )
        }

        // draw the horizontal lines
        for(i in 0 until valueLabels.size){
            val xOffset = 0f
            val yOffset = yAxisSpacing * i
            drawLine(
                color = Color.DarkGray,
                start = Offset(xOffset, yOffset),
                end = Offset(gridWidth, yOffset),
                strokeWidth = 1f
            )
        }
        // last line
        drawLine(
            color = Color.DarkGray,
            start = Offset(0f, gridHeight),
            end = Offset(gridWidth, gridHeight),
            strokeWidth = 1f
        )

        // lables bottom
        for(i in 0 until chartData.size){
            val xOffset = xAxisSpacing * i
            val yOffset = gridHeight + 40
            drawContext.canvas.nativeCanvas.drawText(
                tags[i],
                xOffset,
                yOffset,
                Paint().apply{
                    color = Color.Black.toArgb()
                    textAlign = Paint.Align.LEFT
                    textSize = 10.sp.toPx()
                }
            )
        }

        val labelsReversed = valueLabels.reversed()
        // lables aside
        for(i in 0 until valueLabels.size){
            val xOffset = gridWidth + 10
            val yOffset = (yAxisSpacing * i) + yAxisSpacing //  last - 0 at bottom
            drawContext.canvas.nativeCanvas.drawText(
                labelsReversed[i].toString(),
                xOffset,
                yOffset,
                Paint().apply{
                    color = Color.Black.toArgb()
                    textAlign = Paint.Align.LEFT
                    textSize = 10.sp.toPx()
                }
            )
        }

        // create offsets for the dots
        values.forEachIndexed { i, it ->
            val offsetRight = i * xAxisSpacing
            val offsetTop = getOffsetTop(it, maxValue, gridHeight)
            offsets.add(Offset(x = offsetRight, y = offsetTop))
        }

        // draw circles
        offsets.forEachIndexed { i, it ->
            drawCircle(
                color = Color.Blue,
                radius = 5.dp.toPx(),
                center = it
            )


            drawContext.canvas.nativeCanvas.drawText(
                values[i].toString(),
                it.x,
                it.y + 15.sp.toPx(),
                Paint().apply{
                    color = Color.Black.toArgb()
                    textAlign = Paint.Align.LEFT
                    textSize = 10.sp.toPx()
                }
            )

        }


        // draw lines between dots
        // draw the horizontal lines
        for(i in 0 until chartData.size - 1){
            val startXOffset = offsets[i].x
            val startYOffset = offsets[i].y
            val endXOffset = offsets[i+1].x
            val endYOffset = offsets[i+1].y

            drawLine(
                color = Color.Cyan,
                // offsets directly here can be also good
                start = Offset(startXOffset, startYOffset),
                end = Offset(endXOffset, endYOffset),
                strokeWidth = 3f
            )
        }
    }

}