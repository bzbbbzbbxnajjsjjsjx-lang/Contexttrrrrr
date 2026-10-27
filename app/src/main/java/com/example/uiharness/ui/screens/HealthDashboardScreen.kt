package com.example.uiharness.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.example.uiharness.ui.theme.AppTheme
import com.example.uiharness.ui.theme.Radius
import com.example.uiharness.ui.theme.Spacing

enum class HealthRange(val label: String) {
    DAY("Gün"),
    WEEK("Hafta"),
    MONTH("Ay"),
}

data class HealthDashboardUiState(
    val dateLabel: String,
    val stepsToday: Int,
    val stepGoal: Int,
    val sleepHours: Float,
    val restingHeartRate: Int,
    val weeklySteps: List<Int>,
    val selectedRange: HealthRange,
)

// Sağlık panosu: günlük özet kartları, zaman aralığı seçici ve haftalık grafik.
@Composable
fun HealthDashboardScreen(
    state: HealthDashboardUiState,
    onRangeSelected: (HealthRange) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        Column {
            Text(
                text = state.dateLabel,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(text = "Bugün", style = MaterialTheme.typography.headlineMedium)
        }

        StepsCard(state)

        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
            MetricCard(
                label = "Uyku",
                value = "${state.sleepHours} sa",
                modifier = Modifier.weight(1f),
            )
            MetricCard(
                label = "Dinlenme nabzı",
                value = "${state.restingHeartRate} bpm",
                modifier = Modifier.weight(1f),
            )
        }

        RangeSelector(selected = state.selectedRange, onSelected = onRangeSelected)

        WeeklyChartCard(values = state.weeklySteps)
    }
}

@Composable
private fun StepsCard(state: HealthDashboardUiState) {
    val progress = (state.stepsToday.toFloat() / state.stepGoal).coerceIn(0f, 1f)
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            Text(text = "Adım", style = MaterialTheme.typography.titleMedium)
            Text(text = state.stepsToday.toString(), style = MaterialTheme.typography.displaySmall)
            Text(
                text = "Hedef ${state.stepGoal}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            ProgressTrack(progress = progress)
        }
    }
}

@Composable
private fun ProgressTrack(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Spacing.sm)
            .clip(RoundedCornerShape(Radius.sm))
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary),
        )
    }
}

@Composable
private fun MetricCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(text = value, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
private fun RangeSelector(selected: HealthRange, onSelected: (HealthRange) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        HealthRange.entries.forEach { range ->
            val rangeModifier = Modifier.weight(1f)
            if (range == selected) {
                FilledTonalButton(onClick = { onSelected(range) }, modifier = rangeModifier) {
                    Text(range.label)
                }
            } else {
                OutlinedButton(onClick = { onSelected(range) }, modifier = rangeModifier) {
                    Text(range.label)
                }
            }
        }
    }
}

@Composable
private fun WeeklyChartCard(values: List<Int>) {
    val max = (values.maxOrNull() ?: 1).toFloat()
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            Text(text = "Haftalık adım", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Spacing.xl * 4),
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                verticalAlignment = Alignment.Bottom,
            ) {
                values.forEachIndexed { index, value ->
                    val isLatest = index == values.lastIndex
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight((value / max).coerceIn(0.02f, 1f))
                            .clip(RoundedCornerShape(Radius.sm))
                            .background(
                                if (isLatest) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.primaryContainer
                                },
                            ),
                    )
                }
            }
        }
    }
}

fun sampleHealthDashboardState(): HealthDashboardUiState = HealthDashboardUiState(
    dateLabel = "Cuma, 27 Ekim",
    stepsToday = 6412,
    stepGoal = 10000,
    sleepHours = 7.2f,
    restingHeartRate = 58,
    weeklySteps = listOf(7200, 9800, 5400, 10200, 8600, 6100, 6412),
    selectedRange = HealthRange.WEEK,
)

@Preview(showBackground = true)
@Composable
private fun HealthDashboardScreenPreview() {
    AppTheme {
        HealthDashboardScreen(
            state = sampleHealthDashboardState(),
            onRangeSelected = {},
        )
    }
}
