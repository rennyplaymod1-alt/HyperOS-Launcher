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

/** Ajustes persistentes expuestos como estado de Compose. */
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

    /** Modo Lite activado por defecto. */
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

    /** null = dock predeterminado del sistema. */
    var dock: List<String>? by mutableStateOf(readDock())
        private set

    var note by mutableStateOf(sp.getString("note", "") ?: "")
        private set

    private fun readDock(): List<String>? {
        val raw = sp.getString("dock", null) ?: return null
        if (raw == "-") return emptyList()
        return raw.split("|").filter { it.isNotEmpty() }
    }

    fun setColumns(v: Int) {
        columns = v.coerceIn(3, 6)
        sp.edit().putInt("columns", columns).apply()
    }

    fun updateColumns(v: Int) = setColumns(v)

    fun setIconDp(v: Int) {
        iconDp = v.coerceIn(40, 72)
        sp.edit().putInt("icon_dp", iconDp).apply()
    }

    fun updateIconDp(v: Int) = setIconDp(v)

    fun setTheme(v: Int) {
        theme = v.coerceIn(0, 2)
        sp.edit().putInt("theme", theme).apply()
    }

    fun updateTheme(v: Int) = setTheme(v)

    fun setAccent(v: Int) {
        accent = v
        sp.edit().putInt("accent", v).apply()
    }

    fun updateAccent(v: Int) = setAccent(v)

    fun setLite(v: Boolean) {
        lite = v
        sp.edit().putBoolean("lite", v).apply()
    }

    fun updateLite(v: Boolean) = setLite(v)

    fun setLabels(v: Boolean) {
        showLabels = v
        sp.edit().putBoolean("labels", v).apply()
    }

    fun setSwipeDown(v: Int) {
        swipeDown = v.coerceIn(0, 1)
        sp.edit().putInt("swipe_down", swipeDown).apply()
    }

    fun updateSwipeDown(v: Int) = setSwipeDown(v)

    fun setNote(v: String) {
        note = v
        sp.edit().putString("note", v).apply()
    }

    fun updateNote(v: String) = setNote(v)

    fun applyPreset(p: Preset) {
        setAccent(p.color)
        setTheme(p.theme)
    }

    fun hide(id: String) {
        hidden = hidden + id
        sp.edit().putStringSet("hidden", hidden).apply()

        dock?.let { currentDock ->
            if (id in currentDock) {
                setDock(currentDock - id)
            }
        }
    }

    fun unhide(id: String) {
        hidden = hidden - id
        sp.edit().putStringSet("hidden", hidden).apply()
    }

    fun setDock(ids: List<String>) {
        dock = ids.toList()
        sp.edit().putString(
            "dock",
            if (ids.isEmpty()) "-" else ids.joinToString("|")
        ).apply()
    }

    fun updateDock(ids: List<String>) = setDock(ids)

    fun resetAppearance() {
        setColumns(4)
        setIconDp(52)
        setTheme(0)
        setAccent(DEFAULT_ACCENT)
        setLite(true)
        setLabels(true)
        setSwipeDown(0)

        dock = null
        sp.edit().remove("dock").apply()
    }

    fun exportJson(): String = JSONObject().apply {
        put("v", 1)
        put("columns", columns)
        put("iconDp", iconDp)
        put("theme", theme)
        put("accent", accent)
        put("lite", lite)
        put("labels", showLabels)
        put("swipeDown", swipeDown)
    }.toString()

    fun importJson(text: String): Boolean = try {
        val obj = JSONObject(text)

        if (obj.optInt("v", 0) != 1) {
            throw IllegalArgumentException("Versión no soportada")
        }

        setColumns(obj.optInt("columns", columns))
        setIconDp(obj.optInt("iconDp", iconDp))
        setTheme(obj.optInt("theme", theme))
        setAccent(obj.optInt("accent", accent))
        setLite(obj.optBoolean("lite", lite))
        setLabels(obj.optBoolean("labels", showLabels))
        setSwipeDown(obj.optInt("swipeDown", swipeDown))

        true
    } catch (_: Exception) {
        false
    }
}
