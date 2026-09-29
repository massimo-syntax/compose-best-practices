package com.example.canvas.screen

import androidx.lifecycle.ViewModel
import com.example.canvas.domain.model.ChartData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class ChartViewModel : ViewModel() {

    private val _chartData = MutableStateFlow(getRandomEntries(5))
    val chartData: StateFlow<List<ChartData>> = _chartData.asStateFlow()

    fun refreshChartData(count: Int = 5) {
        _chartData.update { getRandomEntries(count) }
    }


}

private val chartData = ('A'..'Z').map { start ->
    ChartData(
        tag = "$start${start + 1}${start + 2}",
        dataValue = Random.nextInt(0, 1001) // upper bound is exclusive, so this gives 0..1000
    )
}

private fun getRandomEntries(count: Int): List<ChartData> =
    chartData.shuffled().take(count)