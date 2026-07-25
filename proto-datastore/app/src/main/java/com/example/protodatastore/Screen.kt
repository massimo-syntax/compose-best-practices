package com.example.protodatastore

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.core.IOException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

@Composable
fun Screen(){

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var value by remember{ mutableStateOf(false) }
    var favs by remember{ mutableStateOf("") }

    // also add some items
    suspend fun updateShowCompleted(completed: Boolean) {
        val favorite = Favorite.newBuilder().setId(123).setTitle("title").build()
        val favorite2 = Favorite.newBuilder().setId(321).setTitle("title 2").build()
        val favList = if (completed) listOf( favorite, favorite2 ) else emptyList()
        context.userPreferencesStore.updateData { preferences ->
            preferences.toBuilder()
                .setShowCompleted(completed)
                .addAllFavs( favList )
                .build()
        }
    }

    suspend fun deleteFavs(){
        context.userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().clearFavs().build()
        }
    }


    LaunchedEffect(Unit) {
        context.userPreferencesStore.data
            .catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                if (exception is IOException) {
                    Log.e("datastore error", "Error reading sort order preferences.", exception)
                    emit(UserPreferences.getDefaultInstance())
                } else {
                    throw exception
                }
            }
            .collect {
                value = it.showCompleted
                favs = it.favsList.toString()
        }
    }



    Column(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Text("Screen")
        Text(favs)
        Button({
            scope.launch {
                // simulate toggle, that is just a demo, easy to understand
                deleteFavs()
                updateShowCompleted(!value)
            }
        }){
            Text("$value")
        }
    }
}