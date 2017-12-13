package com.example.johnbox.flat

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.preference.PreferenceManager
import android.support.annotation.IdRes
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import java.util.*

class Tutorial : Activity() {
    private lateinit var bgColors: IntArray
    private lateinit var fgColors: IntArray
    private lateinit var settings: SharedPreferences
    private val winColorIndex = Random().nextInt(4)
    private var step = 0
    private val size = 4
    private var showTutorial = false
    private val background: LinearLayout by lazyBind(R.id.background)
    private val grid: GridLayout by lazyBind(R.id.grid)
    private val tutorial: TextView by lazyBind(R.id.tutorial)
    private val steps: LinearLayout by lazyBind(R.id.steps)
    private val s1: Button by lazyBind(R.id.step1)
    private val s2: Button by lazyBind(R.id.step2)
    private val s3: Button by lazyBind(R.id.step3)
    private var displayWidth: Int = 0
    private val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    )
    private lateinit var vibrator: Vibrator
    private lateinit var player: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_tutorial)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
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
        displayWidth = windowManager.defaultDisplay.width
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        player = MediaPlayer.create(this, R.raw.click)
        setHeader()
        initTutorial()
    }

    private fun initTutorial() {
        tutorial.setOnClickListener(tapTutorial)
        background.setBackgroundColor(bgColors[winColorIndex])
        val margin = 4
        val width = displayWidth - (2*margin) - (margin * 2 * size)
        val bSize = width / size
        val gridSize = size * size
        grid.columnCount = size
        grid.rowCount = size
        grid.setPadding(margin, margin, margin, margin)
        s1.layoutParams = params
        s1.minimumWidth = bSize; s1.width= bSize
        s1.minimumHeight = bSize; s1.height = bSize
        s1.isClickable = false
        s2.layoutParams = params
        s2.minimumWidth = bSize; s2.width= bSize
        s2.minimumHeight = bSize; s2.height = bSize
        s2.isClickable = false

        s3.layoutParams = params
        s3.minimumWidth = bSize; s3.width= bSize
        s3.minimumHeight = bSize; s3.height = bSize
        s3.isClickable = false

        var i = 1
        while (i <= gridSize ) {
            val b = Button(this)
            b.id = i
            b.isSoundEffectsEnabled = true
            b.setOnClickListener(onClick)
            b.minimumWidth = bSize; b.width= bSize
            b.minimumHeight = bSize; b.height = bSize
            params.setMargins(margin,margin,margin,margin)
            b.layoutParams = params
            b.textSize = 24f
            b.text = ""
            b.tag = winColorIndex
            setButtonColor(b, winColorIndex)
            grid.addView(b)
            i++
        }
    }

    private val onClick = View.OnClickListener {
        val soundOn = settings.getBoolean("sound", true)
        val vibrationOn = settings.getBoolean("vibration", true)
        if (soundOn) {
            if (player.isPlaying) {
                player.stop()
                player.release()
                player = MediaPlayer.create(this, R.raw.click)
            }
            player.start()
        }
        if (vibrationOn)
            vibrator.vibrate(50)
        val cid: Int = it.id
        when (step) {
            0 -> stepZero(cid)
            1 -> stepOne(cid)
            2 -> stepTwo(cid)
        }
        showStep()
    }

    private fun stepZero(cid: Int) {
        val it = bind<Button>(cid)
        val colorIndex = it.tag as Int
        val nextColorIndex = nextColor(colorIndex)
        setButtonColor(it, nextColorIndex)
        it.tag = nextColorIndex
        incStep()
    }

    private fun stepOne(cid: Int) {
        var colorIndex: Int
        var nextColorIndex: Int
        val lid = if (cid % size != 1) cid-1 else -1
        if (lid != -1) {
            val l = bind<Button>(lid)
            colorIndex = l.tag as Int
            nextColorIndex = nextColor(colorIndex)
            setButtonColor(l, nextColorIndex)
            l.tag = nextColorIndex
        }
        val rid = if (cid % size != 0) cid+1 else -1
        if (rid != -1) {
            val r = bind<Button>(rid)
            colorIndex = r.tag as Int
            nextColorIndex = nextColor(colorIndex)
            setButtonColor(r, nextColorIndex)
            r.tag = nextColorIndex
        }
        val tid = if (cid - size > 0) cid-size else -1
        if (tid != -1) {
            val t = bind<Button>(tid)
            colorIndex = t.tag as Int
            nextColorIndex = nextColor(colorIndex)
            setButtonColor(t, nextColorIndex)
            t.tag = nextColorIndex
        }
        val bid = if (cid + size <= size * size) cid+size else -1
        if (bid != -1) {
            val b = bind<Button>(bid)
            colorIndex = b.tag as Int
            nextColorIndex = nextColor(colorIndex)
            setButtonColor(b, nextColorIndex)
            b.tag = nextColorIndex
        }
        incStep()
    }

    private fun stepTwo(cid: Int) {
        var colorIndex: Int
        var nextColorIndex: Int
        val lid = if ((cid % size != 1) and (cid % size != 2)) cid-2 else -1
        if (lid != -1) {
            val l = bind<Button>(lid)
            colorIndex = l.tag as Int
            nextColorIndex = nextColor(colorIndex)
            setButtonColor(l, nextColorIndex)
            l.tag = nextColorIndex
        }
        val rid = if ((cid % size != size - 1) and (cid % size != 0)) cid+2 else -1
        if (rid != -1) {
            val r = bind<Button>(rid)
            colorIndex = r.tag as Int
            nextColorIndex = nextColor(colorIndex)
            setButtonColor(r, nextColorIndex)
            r.tag = nextColorIndex
        }
        val tid = if (cid - 2*size > 0) cid-2*size else -1
        if (tid != -1) {
            val t = bind<Button>(tid)
            colorIndex = t.tag as Int
            nextColorIndex = nextColor(colorIndex)
            setButtonColor(t, nextColorIndex)
            t.tag = nextColorIndex
        }
        val bid = if (cid + 2*size <= size * size) cid+2*size else -1
        if (bid != -1) {
            val b = bind<Button>(bid)
            colorIndex = b.tag as Int
            nextColorIndex = nextColor(colorIndex)
            setButtonColor(b, nextColorIndex)
            b.tag = nextColorIndex
        }
        incStep()
    }

    private fun setButtonColor(button: Button, colorIndex: Int) =
            button.setBackgroundColor(fgColors[colorIndex])

    private fun incStep() {
        step = (step + 1) % 3
    }

    private fun nextColor(colorIndex: Int): Int = (colorIndex + 1) % 4

    private val tapTutorial = View.OnClickListener {
        if (!showTutorial) {
            tutorial.textSize = 22f
            tutorial.text = getString(R.string.tutorial)
            showTutorial = true
        } else {
            tutorial.text = getString(R.string.enjoy)
            grid.visibility = View.VISIBLE
            steps.visibility = View.VISIBLE
            showStep()
            tutorial.setOnClickListener(null)
        }
    }

    private fun showStep() {
        when (step) {
            0 -> {
                s1.setBackgroundColor(fgColors[winColorIndex])
                s2.setBackgroundColor(bgColors[winColorIndex])
                s3.setBackgroundColor(bgColors[winColorIndex])
            }
            1 -> {
                s1.setBackgroundColor(bgColors[winColorIndex])
                s2.setBackgroundColor(fgColors[winColorIndex])
                s3.setBackgroundColor(bgColors[winColorIndex])
            }
            2 -> {
                s1.setBackgroundColor(bgColors[winColorIndex])
                s2.setBackgroundColor(bgColors[winColorIndex])
                s3.setBackgroundColor(fgColors[winColorIndex])
            }
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

    private fun <T: View> Activity.bind(@IdRes id: Int): T {
        @Suppress("UNCHECKED_CAST")
        return findViewById(id)
    }

    private fun <T: View> Activity.lazyBind(@IdRes id: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy { findViewById<T>(id) }
    }
}
