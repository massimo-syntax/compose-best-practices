package com.example.testmvicleanarchitecture.feature_addedit.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Predefined modern theme color tags
val PRESET_COLORS = listOf(
    "#F5F5F0", // Linen
    "#EFECE6", // Warm Sand
    "#EAEFE9", // Sage Paper
    "#F5EBE6", // Terracotta
    "#F3EAEB", // Muted Rose
    "#ECEFF1", // Slate Dusk
    "#1A1A1A"  // Charcoal Ink
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    noteId: Long = 0L,
    onNavigateBack: () -> Unit,
    viewModel: AddNoteViewModel = hiltViewModel(
        creationCallback = { factory: AddNoteViewModel.Factory ->
            factory.create(noteId)
        }
    )
) {
    // Collect StateFlow from ViewModel with lifecycle awareness
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current

    val isDark = uiState.colorHex.equals("#1A1A1A", ignoreCase = true)
    val contentTextColor = if (isDark) Color.White else Color(0xFF1A1A1A)
    val placeholderColor = if (isDark) Color.White.copy(alpha = 0.4f) else Color(0xFFA1A19C)

    val backgroundColor by animateColorAsState(
        targetValue = try {
            Color(android.graphics.Color.parseColor(uiState.colorHex))
        } catch (e: Exception) {
            Color(0xFFF5F5F0)
        },
        label = "bgColorAnim"
    )

    val formattedDate = remember(uiState.createdAt) {
        SimpleDateFormat("MMMM dd, yyyy • HH:mm", Locale.getDefault()).format(Date(uiState.createdAt))
    }

    // Handle error messages
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    // Back Arrow: triggers onNavigateBack() popping back to HomeScreenKey
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = contentTextColor
                        )
                    }
                },
                actions = {
                    // Pin toggle
                    IconButton(onClick = viewModel::onTogglePin) {
                        Icon(
                            imageVector = if (uiState.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                            contentDescription = "Pin",
                            tint = if (uiState.isPinned) MaterialTheme.colorScheme.primary else contentTextColor.copy(alpha = 0.6f)
                        )
                    }
                    // Save Button: writes to Room and pops back to HomeScreen
                    IconButton(onClick = { viewModel.saveNote { onNavigateBack() } }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save Note",
                            tint = contentTextColor,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        containerColor = backgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            // Paper Tone Swatches
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(PRESET_COLORS) { colorHex ->
                    val color = Color(android.graphics.Color.parseColor(colorHex))
                    val isSelected = uiState.colorHex.equals(colorHex, ignoreCase = true)

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 2.5.dp else 1.dp,
                                color = if (isSelected) Color(0xFF1A1A1A) else Color(0xFFDCD9D0),
                                shape = CircleShape
                            )
                            .clickable { viewModel.onColorChange(colorHex) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = if (colorHex.equals("#1A1A1A", true)) Color.White else Color(0xFF1A1A1A),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Text(
                text = formattedDate,
                style = MaterialTheme.typography.labelSmall,
                color = contentTextColor.copy(alpha = 0.5f),
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )

            // Title TextField: Large Font Serif, Frameless, No Border
            BasicTextField(
                value = uiState.title,
                onValueChange = viewModel::onTitleChange,
                textStyle = TextStyle(
                    color = contentTextColor,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 34.sp
                ),
                cursorBrush = SolidColor(contentTextColor),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (uiState.title.isEmpty()) {
                            Text(
                                text = "Note Title",
                                style = TextStyle(
                                    color = placeholderColor,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                        innerTextField()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description TextField: Expands to Fill Rest of Space, Frameless, No Border
            BasicTextField(
                value = uiState.content,
                onValueChange = viewModel::onContentChange,
                textStyle = TextStyle(
                    color = contentTextColor.copy(alpha = 0.9f),
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.Normal
                ),
                cursorBrush = SolidColor(contentTextColor),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (uiState.content.isEmpty()) {
                            Text(
                                text = "Start typing your note description...",
                                style = TextStyle(
                                    color = placeholderColor,
                                    fontSize = 16.sp
                                )
                            )
                        }
                        innerTextField()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}
