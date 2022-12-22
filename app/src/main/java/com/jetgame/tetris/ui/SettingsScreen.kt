package com.jetgame.tetris.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jetgame.tetris.logic.GameSettings

@Composable
fun SettingsScreen(modifier: Modifier = Modifier, settings: GameSettings) {
    Card(
        modifier.padding(20.dp, 40.dp).fillMaxSize(),
        elevation = 0.dp,
    ) {
        Column(
            Modifier.padding(24.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Settings", style = MaterialTheme.typography.h4)

            SettingsOption(
                Icons.Outlined.DashboardCustomize,
                "Nauts",
            )
            SettingsOption(
                Icons.Outlined.ArrowDownward,
                "Ghost Block",
            )
            SettingsOption(
                Icons.Outlined.FastForward,
                "Game Speed",
            )
            SettingsOption(
                Icons.Outlined.GridView,
                "Grid Outline",
            )
            SettingsOption(
                Icons.Outlined.Height,
                "Grid Height",
            )
            SettingsOption(
                Icons.Outlined.WidthWide,
                "Grid Width",
            )

            Spacer(Modifier.width(8.dp))
            Button(onClick = { settings.navigateBack() }) {
                Text("Close", style = MaterialTheme.typography.h6)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    SettingsScreen(settings = GameSettings({}, {}, {}, {}, {}))
}

@Composable
fun SettingsOption(
    icon: ImageVector,
    name: String,
    onClick: () -> Unit = {},
    indicator: @Composable () -> Unit = {
        //            todo
        Switch(checked = true, onCheckedChange = {})
    }
) {

    Row(
        Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, name, Modifier.size(30.dp))
        Spacer(Modifier.width(25.dp))
        Text(name, style = MaterialTheme.typography.h6)
        Spacer(Modifier.weight(1f))

        indicator()
    }
}
