package com.jetgame.tetris.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jetgame.tetris.logic.*

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewState: GameViewModel.ViewState,
    settings: GameSettings,
) {
    Card(
        modifier.padding(10.dp, 40.dp).fillMaxSize(),
        elevation = 0.dp,
    ) {
        Column(
            Modifier.padding(24.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Settings", style = typography.h4)

            // TODO - prob, all floats
            SettingsOption(
                Icons.Outlined.DashboardCustomize,
                "Nauts",
                settings.useNauts,
                viewState.useNauts,
            )
            SettingsOptionInt(
                Icons.Outlined.Percent,
                "Naut Probability",
                settings.setNautProbability,
                viewState.nautProbability,
            )
            SettingsOption(
                Icons.Outlined.ArrowDownward,
                "Ghost Block",
                settings.useGhostBlock,
                viewState.useGhostBlock,
            )
            SettingsOption(
                Icons.Outlined.GridView,
                "Grid Outline",
                settings.showGridOutline,
                viewState.showGridOutline,
            )
            SettingsOptionInt(
                Icons.Outlined.FastForward,
                "Game Speed",
                settings.setGameSpeed,
                viewState.gameSpeed,
            )
            SettingsOptionInt(
                Icons.Outlined.Height,
                "Grid Height",
                settings.setMatrixHeight,
                viewState.matrix.second,
            )
            SettingsOptionInt(
                Icons.Outlined.WidthWide,
                "Grid Width",
                settings.setMatrixWidth,
                viewState.matrix.first,
            )

            Spacer(Modifier.width(8.dp))
            Button({ settings.navigateBack() }) { Text("Close", style = typography.h6) }
        }
    }
}

@Composable
fun SettingsOption(icon: ImageVector, name: String, onClick: (Boolean) -> Unit, checked: Boolean) {

    Row(
        Modifier.fillMaxWidth().padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, name, Modifier.size(30.dp))
        Spacer(Modifier.width(20.dp))
        Text(name, style = typography.h6)

        Spacer(Modifier.weight(1f))

        Switch(checked, onCheckedChange = { onClick(it) })
    }
}

@Composable
fun SettingsOptionInt(
    icon: ImageVector,
    name: String,
    onClick: (Int) -> Unit,
    value: Int,
) {

    Row(
        Modifier.fillMaxWidth().padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, name, Modifier.size(30.dp))
        Spacer(Modifier.width(20.dp))
        Text(name, style = typography.h6)

        Spacer(Modifier.weight(1f))

        // TODO: make this functional, and set a limit to the range of permitted values.
        OutlinedButton(
            modifier = Modifier.defaultMinSize(minWidth = 40.dp),
            onClick = { onClick(value) },
            contentPadding = PaddingValues(0.dp),
            border = BorderStroke(0.dp, Color.Black)
        ) {
            Text(
                value.toString(),
                style = typography.h5,
            )
        }
    }
}
