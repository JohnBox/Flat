package com.example.johnbox.flat

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.annotation.IdRes
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

class SelectLevel : Activity() {
    private val background: LinearLayout by lazyBind(R.id.background)
    private lateinit var bgColors: IntArray
    private lateinit var fgColors: IntArray
    private lateinit var settings: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_select_level)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        settings = PreferenceManager.getDefaultSharedPreferences(applicationContext)
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

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }

    fun startGameActivity(view: View) {
        val intent = Intent(this, Game::class.java)
        val size = view.tag as String
        intent.putExtra("size", size)
        startActivity(intent)
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
        val normal = bind<Button>(R.id.normal)
        val hard = bind<Button>(R.id.hard)
        val hardcore = bind<Button>(R.id.hardcore)
        val ysnp = bind<Button>(R.id.ysnp)
        normal.setBackgroundColor(bgColors[0])
        hard.setBackgroundColor(bgColors[1])
        hardcore.setBackgroundColor(bgColors[2])
        ysnp.setBackgroundColor(bgColors[3])
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
