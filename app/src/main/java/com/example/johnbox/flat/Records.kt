package com.example.johnbox.flat

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.annotation.IdRes
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class Records : Activity() {
    private lateinit var bgColors: IntArray
    private lateinit var fgColors: IntArray
    private lateinit var settings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_records)
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
        val t1 = bind<TextView>(R.id.t1)
        val t2 = bind<TextView>(R.id.t2)
        val t3 = bind<TextView>(R.id.t3)
        val t4 = bind<TextView>(R.id.t4)
        val t5 = bind<TextView>(R.id.t5)
        val t6 = bind<TextView>(R.id.t6)
        val t7 = bind<TextView>(R.id.t7)
        val t8 = bind<TextView>(R.id.t8)
        val s1 = bind<TextView>(R.id.s1)
        val s2 = bind<TextView>(R.id.s2)
        val s3 = bind<TextView>(R.id.s3)
        val s4 = bind<TextView>(R.id.s4)
        val s5 = bind<TextView>(R.id.s5)
        val s6 = bind<TextView>(R.id.s6)
        val s7 = bind<TextView>(R.id.s7)
        val s8 = bind<TextView>(R.id.s8)
        t1.setBackgroundColor(bgColors[0])
        s1.setBackgroundColor(bgColors[0])
        t5.setBackgroundColor(bgColors[0])
        s5.setBackgroundColor(bgColors[0])
        t2.setBackgroundColor(bgColors[1])
        s2.setBackgroundColor(bgColors[1])
        t6.setBackgroundColor(bgColors[1])
        s6.setBackgroundColor(bgColors[1])
        t3.setBackgroundColor(bgColors[2])
        s3.setBackgroundColor(bgColors[2])
        t7.setBackgroundColor(bgColors[2])
        s7.setBackgroundColor(bgColors[2])
        t4.setBackgroundColor(bgColors[3])
        s4.setBackgroundColor(bgColors[3])
        t8.setBackgroundColor(bgColors[3])
        s8.setBackgroundColor(bgColors[3])
        val normal6 = settings.getInt("normal6",0)
        val normal7 = settings.getInt("normal7",0)
        val normal8 = settings.getInt("normal8",0)
        val normal9 = settings.getInt("normal9",0)
        s1.text = normal6.toString()
        s2.text = normal7.toString()
        s3.text = normal8.toString()
        s4.text = normal9.toString()
        val timer6 = settings.getInt("timer6",0)
        val timer7 = settings.getInt("timer7",0)
        val timer8 = settings.getInt("timer8",0)
        val timer9 = settings.getInt("timer9",0)
        s5.text = timer6.toString()
        s6.text = timer7.toString()
        s7.text = timer8.toString()
        s8.text = timer9.toString()
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
