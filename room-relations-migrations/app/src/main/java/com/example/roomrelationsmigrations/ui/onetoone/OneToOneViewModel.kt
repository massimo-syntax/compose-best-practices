package com.example.roomrelationsmigrations.ui.onetoone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomrelationsmigrations.data.onetoone.OneToOneDao
import com.example.roomrelationsmigrations.data.onetoone.PersonEntity
import com.example.roomrelationsmigrations.data.onetoone.PersonProfileEntity
import com.example.roomrelationsmigrations.data.onetoone.ProfileEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

data class OneToOneUiState(
    val persons: List<PersonEntity> = emptyList(),
    val profiles: List<ProfileEntity> = emptyList(),
    val personsProfiles: List<PersonProfileEntity> = emptyList(),
)

@HiltViewModel
class OneToOneViewModel @Inject constructor(
    private val dao: OneToOneDao,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OneToOneUiState())
    val uiState: StateFlow<OneToOneUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                dao.getPersons(),
                dao.getProfiles(),
                    dao.getPersonsWithProfiles()
            ) { persons, profiles , personsProfiles ->
                OneToOneUiState(persons, profiles, personsProfiles)
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun addRandomEntries() {
        val personNames = listOf(
            "Alice Smith", "Bob Jones", "Charlie Brown", "Diana Prince", "Ethan Hunt"
        )
        val profileNames = listOf(
            "Developer", "Designer", "Product Owner", "QA Tester", "DevOps Admin"
        )
        val newPersons = personNames.map { PersonEntity(name = it) }
        viewModelScope.launch {
            // first insert persons
            val insertedPersonIds = dao.insertPersons(newPersons)
            // then having the ids the profile is already created with its personId
            val newProfiles = insertedPersonIds.mapIndexed { index, personId ->
                ProfileEntity(
                    personId = personId,
                    profileName = profileNames[index]
                )
            }
            // then, add all the new profiles as well
            delay(1.seconds)
            dao.insertProfiles(newProfiles)

            // persons with profiles is already observed @ init
        }
    }



    fun deleteAllEntries() {
        viewModelScope.launch {
            dao.deleteAllData()
        }
    }
}
