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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jetgame.tetris.logic.*

@Composable
fun SettingsScreen(modifier: Modifier = Modifier, settings: GameSettings) {
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

            SettingsOption(
                Icons.Outlined.DashboardCustomize,
                "Nauts",
                settings.useNauts,
            )
            SettingsOptionInt(
                Icons.Outlined.Percent,
                "Naut Probability",
                settings.setNautProbability,
            )
            SettingsOption(
                Icons.Outlined.ArrowDownward,
                "Ghost Block",
                settings.useGhostBlock,
            )
            SettingsOption(
                Icons.Outlined.GridView,
                "Grid Outline",
                settings.showGridOutline,
            )
            SettingsOptionInt(
                Icons.Outlined.FastForward,
                "Game Speed",
                settings.setGameSpeed,
            )
            SettingsOptionInt(
                Icons.Outlined.Height,
                "Grid Height",
                settings.setMatrixHeight,
            )
            SettingsOptionInt(
                Icons.Outlined.WidthWide,
                "Grid Width",
                settings.setMatrixWidth,
            )

            Spacer(Modifier.width(8.dp))
            Button({ settings.navigateBack() }) { Text("Close", style = typography.h6) }
        }
    }
}

@Composable
fun SettingsOption(
    icon: ImageVector,
    name: String,
    onClick: (Boolean) -> Unit,
) {

    Row(
        Modifier.fillMaxWidth().padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, name, Modifier.size(30.dp))
        Spacer(Modifier.width(20.dp))
        Text(name, style = typography.h6)

        Spacer(Modifier.weight(1f))

        Switch(checked = true, onCheckedChange = { onClick(it) })
    }
}

@Composable
fun SettingsOptionInt(
    icon: ImageVector,
    name: String,
    onClick: (Float) -> Unit,
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
            onClick = { onClick(1f) },
            contentPadding = PaddingValues(0.dp),
            border = BorderStroke(0.dp, Color.Black)
        ) {
            Text(
                "1",
                style = typography.h5,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    SettingsScreen(settings = GameSettings({}, {}, {}, {}, {}, {}, {}, {}))
}
