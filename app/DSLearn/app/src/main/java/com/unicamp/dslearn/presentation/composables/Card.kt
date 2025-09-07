package com.unicamp.dslearn.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unicamp.dslearn.core.model.ExercisesModel
import com.unicamp.dslearn.ui.theme.DSLearnTheme

@Composable
fun Card(
    modifier: Modifier = Modifier,
    name: String,
    theory: String,
    exercises: List<ExercisesModel>,
    onCardClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .padding(vertical = 12.dp, horizontal = 24.dp),
    ) {
        Text(
            maxLines = 1,
            text = name,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = theory,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CardPreview() {
    DSLearnTheme {
        Card(
            name = "Array",
            theory = "Um array é uma coleção de itens armazenados em locais de memória contíguos.",
            exercises = listOf()
        ) { }
    }
}
