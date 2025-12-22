package com.example.investai_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.investai_mobile.ui.theme.InvestAI_MobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InvestAI_MobileTheme {
                MainScreen()
            }
        }
    }
}

/**
 * Типы вкладок нижнего меню.
 */
enum class BottomTab {
    HOME, SETTINGS, EXIT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    // Состояния: выбранная вкладка и показ диалога выхода
    val selectedTab = remember { mutableStateOf(BottomTab.HOME) }
    val showExitDialog = remember { mutableStateOf(false) }

    // Простейший "переключатель темы" только для экрана (темный/светлый фон)
    val isLightMode = remember { mutableStateOf(false) }

    val backgroundColor = if (isLightMode.value) {
        Color(0xFFFFFFFF) // светлый фон
    } else {
        Color(0xFF121212) // тёмно‑серый фон
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    if (tab == BottomTab.EXIT) {
                        // Для "Открытой двери" просто показываем диалог выхода
                        showExitDialog.value = true
                    } else {
                        selectedTab.value = tab
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when (selectedTab.value) {
                BottomTab.HOME -> HomeScreen()
                BottomTab.SETTINGS -> SettingsScreen(isLightMode = isLightMode)
                BottomTab.EXIT -> { /* сам контент не используем, только диалог */ }
            }
        }

        if (showExitDialog.value) {
            ExitDialog(
                onConfirm = {
                    showExitDialog.value = false
                    // В учебном примере просто закрываем активность
                    // В более сложном варианте можно очищать SharedPreferences
                },
                onDismiss = { showExitDialog.value = false }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InvestAI_MobileTheme {
        MainScreen()
    }
}

/**
 * Главный экран с заголовком InvestAI и полями "Акция", "Дата", "Low".
 * Это макет, модель пока не вызываем.
 */
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        verticalArrangement = Arrangement.Top
    ) {
        // Заголовок InvestAI
        Text(
            text = "InvestAI",
            color = Color(0xFFFBC02D), // жёлтый
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp),
            textAlign = TextAlign.Center
        )

        // Карточка с вводом данных
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E1E)
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // "Акция" слева, справа выбор тикера (пока только текст/заглушка)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Акция",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    OutlinedTextField(
                        value = "AAPL",
                        onValueChange = { /* пока не редактируем */ },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(0.5f),
                        label = { Text(text = "Тикер") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // "Дата" слева, справа поле даты (пока руками)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Дата",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    OutlinedTextField(
                        value = "2017-11-10",
                        onValueChange = { /* здесь позже можно сделать DatePicker */ },
                        modifier = Modifier.fillMaxWidth(0.5f),
                        label = { Text(text = "YYYY-MM-DD") }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Надпись "Прогнозирование"
        Text(
            text = "Прогнозирование",
            color = Color(0xFF2E7D32), // зелёный
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            textAlign = TextAlign.Center
        )

        // Карточка с выводом "Low"
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E1E)
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Low",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Text(
                        // пока заглушка вместо настоящего прогноза
                        text = "123.45",
                        color = Color(0xFFFBC02D),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        // Здесь в будущем будет вызов tflite‑модели
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Рассчитать прогноз (заглушка)")
                }
            }
        }
    }
}

/**
 * Экран настроек:
 * - Переключатель светлая/тёмная тема
 * - Кнопка выхода из аккаунта (пока только заглушка)
 */
@Composable
fun SettingsScreen(isLightMode: MutableState<Boolean>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Настройки",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        // Переключатель темы
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Светлый режим",
                color = Color.White,
                fontSize = 16.sp
            )
            Switch(
                checked = isLightMode.value,
                onCheckedChange = { isLightMode.value = it }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка "Выход из аккаунта" (красная)
        Button(
            onClick = {
                // Здесь можно очистить SharedPreferences и вернуть пользователя к регистрации
                // Сейчас это просто кнопка‑заглушка.
            },
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color(0xFFC62828) // красный
            )
        ) {
            Text(text = "Выйти из аккаунта")
        }
    }
}

/**
 * Нижнее меню с тремя иконками: Дом, Настройки, Открытая дверь (выход).
 */
@Composable
fun BottomNavigationBar(
    selectedTab: MutableState<BottomTab>,
    onTabSelected: (BottomTab) -> Unit
) {
    BottomAppBar(
        containerColor = Color(0xFF1E1E1E)
    ) {
        IconButton(onClick = { onTabSelected(BottomTab.HOME) }) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Главная",
                tint = if (selectedTab.value == BottomTab.HOME)
                    Color(0xFFFBC02D) else Color.White
            )
        }

        IconButton(onClick = { onTabSelected(BottomTab.SETTINGS) }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Настройки",
                tint = if (selectedTab.value == BottomTab.SETTINGS)
                    Color(0xFF2E7D32) else Color.White
            )
        }

        IconButton(onClick = { onTabSelected(BottomTab.EXIT) }) {
            Icon(
                imageVector = Icons.Default.DoorFront,
                contentDescription = "Выход",
                tint = Color(0xFFC62828)
            )
        }
    }
}

/**
 * Диалог подтверждения выхода из приложения.
 */
@Composable
fun ExitDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Вы точно хотите выйти?")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = "Да",
                    color = Color(0xFFC62828) // красный
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Нет",
                    color = Color(0xFF2E7D32) // зелёный
                )
            }
        }
    )
}