package io.github.hyperultra.launcher

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.json.JSONObject

data class Preset(val name: String, val color: Int, val theme: Int)

val DEFAULT_ACCENT: Int = 0xFF3478F6.toInt()

val PRESETS = listOf(
    Preset("Azul Hyper", DEFAULT_ACCENT, 0),
    Preset("Coral", 0xFFFF6B57.toInt(), 1),
    Preset("Bosque", 0xFF2E9E6B.toInt(), 2),
    Preset("Violeta", 0xFF7C5CFC.toInt(), 2),
    Preset("Grafito", 0xFF5F6B7A.toInt(), 2),
)

/** Ajustes persistentes (SharedPreferences) expuestos como estado de Compose. */
class LauncherSettings(context: Context) {
    private val sp = context.applicationContext.getSharedPreferences(
        "launcher_settings",
        Context.MODE_PRIVATE
    )

    var columns by mutableIntStateOf(sp.getInt("columns", 4))
        private set

    var iconDp by mutableIntStateOf(sp.getInt("icon_dp", 52))
        private set

    /** 0 = sistema, 1 = claro, 2 = oscuro */
    var theme by mutableIntStateOf(sp.getInt("theme", 0))
        private set

    var accent by mutableIntStateOf(sp.getInt("accent", DEFAULT_ACCENT))
        private set

    /** Modo Lite: sin desenfoque ni animaciones. Activado por defecto. */
    var lite by mutableStateOf(sp.getBoolean("lite", true))
        private set

    var showLabels by mutableStateOf(sp.getBoolean("labels", true))
        private set

    /** 0 = Centro de control, 1 = buscar apps */
    var swipeDown by mutableIntStateOf(sp.getInt("swipe_down", 0))
        private set

    var hidden: Set<String> by mutableStateOf(
        sp.getStringSet("hidden", emptySet())?.toSet() ?: emptySet()
    )
        private set

    /** null = dock por defecto (según apps predeterminadas del sistema). */
    var dock: List<String>? by mutableStateOf(readDock())
        private set

    var note by mutableStateOf(sp.getString("note", "") ?: "")
        private set

    private fun readDock(): List<String>? {
        val raw = sp.getString("dock", null) ?: return null
        if (raw == "-") return emptyList()
        return raw.split("|").filter { it.isNotEmpty() }
    }

    fun updateColumns(v: Int) {
        columns = v.coerceIn(3, 6)
        sp.edit().putInt("columns", columns).apply()
    }

    fun updateIconDp(v: Int) {
        iconDp = v.coerceIn(40, 72)
        sp.edit().putInt("icon_dp", iconDp).apply()
    }

    fun updateTheme(v: Int) {
        theme = v.coerceIn(0, 2)
        sp.edit().putInt("theme", theme).apply()
    }

    fun updateAccent(v: Int) {
        accent = v
        sp.edit().putInt("accent", v).apply()
    }

    fun updateLite(v: Boolean) {
        lite = v
        sp.edit().putBoolean("lite", v).apply()
    }

    fun setLabels(v: Boolean) {
        showLabels = v
        sp.edit().putBoolean("labels", v).apply()
    }

    fun updateSwipeDown(v: Int) {
        swipeDown = v.coerceIn(0, 1)
        sp.edit().putInt("swipe_down", swipeDown).apply()
    }

    fun updateNote(v: String) {
        note = v
        sp.edit().putString("note", v).apply()
    }

    fun applyPreset(p: Preset) {
        updateAccent(p.color)
        updateTheme(p.theme)
    }

    fun hide(id: String) {
        hidden = hidden + id
        sp.edit().putStringSet("hidden", hidden).apply()
        dock?.let { d ->
            if (id in d) updateDock(d - id)
        }
    }

    fun unhide(id: String) {
        hidden
