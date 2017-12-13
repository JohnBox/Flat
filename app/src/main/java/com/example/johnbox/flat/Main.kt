package com.example.johnbox.flat

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.annotation.IdRes
import android.view.View
import android.widget.Button
import java.util.*

class Main : Activity() {
    private lateinit var bgColors: IntArray
    private lateinit var fgColors: IntArray
    private lateinit var settings: SharedPreferences
    private var update = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        updateLocale()
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
        update = true
    }

    override fun onResume() {
        super.onResume()
        updateTheme()
        if (update) {
            update = false
            recreate()
        }
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
    }

    fun startTutorialActivity(view: View) = startClassActivity(Tutorial::class.java)
    fun startSelectLevelActivity(view: View) = startClassActivity(SelectLevel::class.java)
    fun startSettingsActivity(view: View) = startClassActivity(Settings::class.java)
    fun startRecordsActivity(view: View) = startClassActivity(Records::class.java)

    private fun startClassActivity(klass: Class<*>) {
        startActivity(Intent(this, klass))
        overridePendingTransition(0,0)
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
        val t = bind<Button>(R.id.tutorial)
        val p = bind<Button>(R.id.play)
        val s = bind<Button>(R.id.settings)
        val r = bind<Button>(R.id.records)
        t.setBackgroundColor(bgColors[0])
        p.setBackgroundColor(bgColors[1])
        s.setBackgroundColor(bgColors[2])
        r.setBackgroundColor(bgColors[3])
    }

    private fun <T: View> Activity.bind(@IdRes id: Int): T {
        @Suppress("UNCHECKED_CAST")
        return findViewById(id)
    }
}
