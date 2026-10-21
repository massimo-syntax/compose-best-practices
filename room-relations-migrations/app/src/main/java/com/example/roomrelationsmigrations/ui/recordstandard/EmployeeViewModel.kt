package com.example.roomrelationsmigrations.ui.recordstandard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomrelationsmigrations.data.recordstandard.EmployeeDao
import com.example.roomrelationsmigrations.data.recordstandard.EmployeeEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val employeeDao: EmployeeDao
) : ViewModel() {

    val employees: StateFlow<List<EmployeeEntity>> = employeeDao.getAllEmployees()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addRandomEmployee() {
        val names = listOf(
            "Alex Smith",
            "Jordan Lee",
            "Taylor Swift",
            "Morgan Freeman",
            "Chris Evans",
            "Sam Wilson",
            "Pat Brown",
            "Riley Davis",
            "Casey Jones",
            "Jamie Miller"
        )
        val randomName = names.random()
        viewModelScope.launch {
            employeeDao.insertEmployee(
                EmployeeEntity(name = randomName)
            )
        }
    }

    fun deleteEmployee(employee: EmployeeEntity) {
        viewModelScope.launch {
            employeeDao.deleteEmployee(employee)
        }
    }
}
