package com.example.simplestdatastore

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


// never forget the manifest,
class MyApp : Application() {
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(applicationContext)
    }
}
private val Context.dataStore by preferencesDataStore(
    name = PREFS_NAME
)
class AppContainer(context: Context) {
    val prefsDataSource: PreferencesDataSource by lazy {
        PreferencesDataSource(context.dataStore)
    }
    val userPrefRepo = UserPreferencesRepository(prefsDataSource)
}

val myViewmodelFactory = viewModelFactory {
    initializer<SettingsViewModel> {
        SettingsViewModel(myAppProvides.appContainer.userPrefRepo)
    }
    initializer<MainActivityViewModel> {
        MainActivityViewModel(myAppProvides.appContainer.userPrefRepo)
    }

//    initializer<AnotherViewModel> {
//        TaskDetailViewModel(application.taskRepository)
//    }
}
private val CreationExtras.myAppProvides: MyApp
    get() = this[APPLICATION_KEY] as? MyApp
        ?: error("`APPLICATION_KEY` in `CreationExtras` is missing or not a `MyApp`.")

class UserPreferencesRepository(private val dataSource: PreferencesDataSource){
    val userPrefsObserver = dataSource.userPreferencesFlow
    suspend fun updateTheme(theme: SelectedTheme) = dataSource.saveTheme(theme)
    suspend fun saveName(username: String) = dataSource.saveName(username)
    suspend fun clearAll() = dataSource.clearAllData()
    fun getPrefsSync() = dataSource.getUserPrefsOnceSync()
}

class SettingsViewModel (
    private val prefRepo: UserPreferencesRepository
) : ViewModel(){

    val userPreferences = prefRepo.userPrefsObserver
        .map{
            Log.wtf("flow viewmodel", it.toString())
            UIState(false, it)
        }
        .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
            UIState(
                loading = true,
                data = UserPreferences(null , "no glitching, loading here, sync there")
            )
    )

    fun updateTheme(theme: SelectedTheme) {
        viewModelScope.launch {
            prefRepo.updateTheme(theme)
        }
    }

    fun updateName(name: String) {
        viewModelScope.launch {
            prefRepo.saveName(name)
        }
    }

    fun clearData(){
        viewModelScope.launch {
            prefRepo.clearAll()
        }
    }

}

data class UIState(
    val loading: Boolean ,
    val data: UserPreferences
)


class MainActivityViewModel(
    private val userPrefsRepo: UserPreferencesRepository
) : ViewModel(){
    fun getPrefsSync() = userPrefsRepo.getPrefsSync()
}


