package com.jetgame.tetris.logic

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.media.AudioManager
import android.media.SoundPool
import android.view.View
import android.view.WindowManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.jetgame.tetris.R

fun Offset(x: Int, y: Int) = androidx.compose.ui.geometry.Offset(x.toFloat(), y.toFloat())

data class Interactive
constructor(
    val onMove: (Direction) -> Unit,
    val onRotate: () -> Unit,
    val onRestart: () -> Unit,
    val onPause: () -> Unit,
    val onMute: () -> Unit
)

fun combinedInteractive(
    onMove: (Direction) -> Unit = {},
    onRotate: () -> Unit = {},
    onRestart: () -> Unit = {},
    onPause: () -> Unit = {},
    onMute: () -> Unit = {}
) = Interactive(onMove, onRotate, onRestart, onPause, onMute)

enum class Direction {
    Left,
    Up,
    Right,
    Down
}

fun Direction.toOffset() =
    when (this) {
        Direction.Left -> -1 to 0
        Direction.Up -> 0 to -1
        Direction.Right -> 1 to 0
        Direction.Down -> 0 to 1
    }

val defaultFontFamily =
    FontFamily(
        Font(R.font.mukta, FontWeight.Normal),
        Font(R.font.mukta, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.mukta_medium, FontWeight.Medium),
        Font(R.font.mukta_semibold, FontWeight.SemiBold),
        Font(R.font.mukta_bold, FontWeight.Bold)
    )

val LedFontFamily =
    FontFamily(
        Font(R.font.unidream_led, FontWeight.Light),
        Font(R.font.unidream_led, FontWeight.Normal),
        Font(R.font.unidream_led, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.unidream_led, FontWeight.Medium),
        Font(R.font.unidream_led, FontWeight.Bold)
    )

val NextMatrix = 4 to 2
const val ScoreEverySpirit = 12

fun calculateScore(lines: Int) =
    when (lines) {
        1 -> 100
        2 -> 300
        3 -> 700
        4 -> 1500
        else -> 0
    }

object StatusBarUtil {

    fun transparentStatusBar(activity: Activity) {
        with(activity) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            val vis = window.decorView.systemUiVisibility
            window.decorView.systemUiVisibility = option or vis
            window.statusBarColor = Color.TRANSPARENT
        }
    }
}

@SuppressLint("StaticFieldLeak")
object SoundUtil {

    private var _context: Context? = null
    private val sp: SoundPool by lazy {
        SoundPool.Builder().setMaxStreams(4).setMaxStreams(AudioManager.STREAM_MUSIC).build()
    }
    private val _map = mutableMapOf<SoundType, Int>()

    fun init(context: Context) {
        _context = context
        Sounds.forEach { _map[it] = sp.load(_context, it.res, 1) }
    }

    fun release() {
        _context = null
        sp.release()
    }

    fun play(isMute: Boolean, sound: SoundType) {
        if (!isMute) {
            sp.play(requireNotNull(_map[sound]), 1f, 1f, 0, 0, 1f)
        }
    }
}

sealed class SoundType(val res: Int) {
    object Move : SoundType(R.raw.move)
    object Rotate : SoundType(R.raw.rotate)
    object Start : SoundType(R.raw.start)
    object Drop : SoundType(R.raw.drop)
    object Clean : SoundType(R.raw.clean)
}

val Sounds =
    listOf(SoundType.Move, SoundType.Rotate, SoundType.Start, SoundType.Drop, SoundType.Clean)
