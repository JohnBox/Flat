package com.example.johnbox.flat

import android.app.Activity
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.support.annotation.IdRes
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import java.util.*

class Settings : Activity() {

    private val sound: CheckBox by lazyBind(R.id.sound)
    private val vibration: CheckBox by lazyBind(R.id.vibration)
    private val en: CheckBox by lazyBind(R.id.en)
    private val ua: CheckBox by lazyBind(R.id.ua)
    private val def: CheckBox by lazyBind(R.id.def)
    private val alt: CheckBox by lazyBind(R.id.alt)
    private val normal: CheckBox by lazyBind(R.id.normal)
    private val timer: CheckBox by lazyBind(R.id.timer)
    private lateinit var bgColors: IntArray
    private lateinit var fgColors: IntArray
    private lateinit var settings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        settings = getDefaultSharedPreferences(applicationContext)
        updateTheme()
        sound.isChecked = settings.getBoolean("sound", true)
        vibration.isChecked = settings.getBoolean("vibration", true)
        val enLang = settings.getBoolean("enLang", true)
        if (enLang) {
            en.isChecked = true
            ua.isChecked = false
        } else {
            en.isChecked = false
            ua.isChecked = true
        }
        val defTheme = settings.getBoolean("defTheme", true)
        if (defTheme) {
            def.isChecked = true
            alt.isChecked = false
        } else {
            def.isChecked = false
            alt.isChecked = true
        }
        val normalMode = settings.getBoolean("normalMode", true)
        if (normalMode) {
            normal.isChecked = true
            timer.isChecked = false
        } else {
            normal.isChecked = false
            timer.isChecked = true
        }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }

    private fun updateTheme() {
        val defTheme = settings.getBoolean("defTheme", true)
        if (defTheme) {
            bgColors = applicationContext.resources.getIntArray(R.array.nbg)
            fgColors = applicationContext.resources.getIntArray(R.array.nfg)
        } else {
            bgColors = applicationContext.resources.getIntArray(R.array.abg)
            fgColors = applicationContext.resources.getIntArray(R.array.afg)
        }
        setHeader()
        setContent()
    }

    private fun updateLocale() {
        val enLang = settings.getBoolean("enLang", true)
        val lang = if (enLang) "en" else "uk"
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config,
                baseContext.resources.displayMetrics)
        recreate()
    }

    fun onSoundClick(view: View) {
        val check = view as CheckBox
        settings.edit().putBoolean("sound", check.isChecked).apply()
    }

    fun onVibrationClick(view: View) {
        val check = view as CheckBox
        settings.edit().putBoolean("vibration", check.isChecked).apply()
    }

    fun onEnLandClick(view: View) {
        val check = view as CheckBox
        if (check.isChecked) {
            settings.edit().putBoolean("enLang", check.isChecked).apply()
            ua.isChecked = !check.isChecked
            updateLocale()
        } else {
            en.isChecked = true
        }
    }

    fun onUaLangClick(view: View) {
        val check = view as CheckBox
        if (check.isChecked) {
            settings.edit().putBoolean("enLang", !check.isChecked).apply()
            en.isChecked = !check.isChecked
            updateLocale()
        } else {
            ua.isChecked = true
        }
    }

    fun onDefThemeClick(view: View) {
        val check = view as CheckBox
        if (check.isChecked) {
            settings.edit().putBoolean("defTheme", check.isChecked).apply()
            alt.isChecked = !check.isChecked
            updateTheme()
        } else {
            def.isChecked = true
        }
    }

    fun onAltThemeClick(view: View) {
        val check = view as CheckBox
        if (check.isChecked) {
            settings.edit().putBoolean("defTheme", !check.isChecked).apply()
            def.isChecked = !check.isChecked
            updateTheme()
        } else {
            alt.isChecked = true
        }
    }

    fun onNormalModeClick(view: View) {
        val check = view as CheckBox
        if (check.isChecked) {
            settings.edit().putBoolean("normalMode", check.isChecked).apply()
            timer.isChecked = !check.isChecked
        } else {
            normal.isChecked = true
        }
    }

    fun onTimerModeClick(view: View) {
        val check = view as CheckBox
        if (check.isChecked) {
            settings.edit().putBoolean("normalMode", !check.isChecked).apply()
            normal.isChecked = !check.isChecked
        } else {
            timer.isChecked = true
        }
    }

    private fun setHeader() {
        val f = bind<Button>(R.id.f)
        val l = bind<Button>(R.id.l)
        val a = bind<Button>(R.id.a)
        val t = bind<Button>(R.id.t)
        f.setBackgroundColor(fgColors[0])
        f.setTextColor(bgColors[0])
        l.setBackgroundColor(fgColors[1])
        l.setTextColor(bgColors[1])
        a.setBackgroundColor(fgColors[2])
        a.setTextColor(bgColors[2])
        t.setBackgroundColor(fgColors[3])
        t.setTextColor(bgColors[3])
    }

    private fun setContent() {
        val sv = bind<LinearLayout>(R.id.sound_vibration)
        val l = bind<LinearLayout>(R.id.language)
        val t = bind<LinearLayout>(R.id.theme)
        val m = bind<LinearLayout>(R.id.mode)
        sv.setBackgroundColor(bgColors[0])
        l.setBackgroundColor(bgColors[1])
        t.setBackgroundColor(bgColors[2])
        m.setBackgroundColor(bgColors[3])
    }

    private fun <T: View> Activity.bind(@IdRes id: Int): T {
        @Suppress("UNCHECKED_CAST")
        return findViewById(id)
    }

    private fun <T: View> Activity.lazyBind(@IdRes id: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy { findViewById<T>(id) }
    }
}
