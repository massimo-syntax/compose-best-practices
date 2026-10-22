package com.example.roomrelationsmigrations.ui.onetomany

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomrelationsmigrations.data.onetomany.AppEntity
import com.example.roomrelationsmigrations.data.onetomany.AppWithUsers
import com.example.roomrelationsmigrations.data.onetomany.OneToManyDao
import com.example.roomrelationsmigrations.data.onetomany.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OneToManyUiState(
    val apps: List<AppEntity> = emptyList(),
    val users: List<UserEntity> = emptyList(),
    val appsWithUsers: List<AppWithUsers> = emptyList(),
)

@HiltViewModel
class OneToManyViewModel @Inject constructor(
    private val dao: OneToManyDao,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OneToManyUiState())
    val uiState: StateFlow<OneToManyUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                dao.getApps(),
                dao.getUsers(),
                dao.getAppsWithUsers()
            ) { apps, users, appsWithUsers ->
                OneToManyUiState(apps, users, appsWithUsers)
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun addRandomEntries() {
        val appNames = listOf("SocialApp", "Room Showcase", "TaskApp")
        val userNames = listOf(
            "Alice Smith", "Bob Jones", "Charlie Brown",
            "Diana Prince", "Ethan Hunt", "Fiona Gallagher",
            "George Clark", "Hannah Abbott", "Ian Malcolm"
        )

        val newApps = appNames.map { AppEntity(name = it) }

        viewModelScope.launch {
            val insertedAppIds = dao.insertApps(newApps)

            val newUsers = mutableListOf<UserEntity>()
            insertedAppIds.forEachIndexed { appIndex, appId ->
                val startIndex = appIndex * 3
                for (i in 0 until 3) {
                    val userName = userNames[startIndex + i]
                    newUsers.add(
                        UserEntity(
                            appId = appId,
                            name = userName
                        )
                    )
                }
            }

            dao.insertUsers(newUsers)
        }
    }

    fun deleteAllEntries() {
        viewModelScope.launch {
            dao.deleteAllData()
        }
    }
}
