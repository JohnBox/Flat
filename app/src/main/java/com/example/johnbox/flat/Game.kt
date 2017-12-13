package com.example.johnbox.flat

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Vibrator
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.support.annotation.IdRes
import android.view.View
import android.widget.*
import java.util.*

class Game : Activity() {
    private var clickCount = 0
    private var winColorIndex = -1
    private var size = 0
    private var step = 0
    private var gameOver = false
    private var normalMode = true
    private var timer = false
    private var timerCount = 10
    private var displayWidth: Int = 0
    private val background: LinearLayout by lazyBind(R.id.background)
    private val grid: GridLayout by lazyBind(R.id.grid)
    private val score: TextView by lazyBind(R.id.score)
    private val win: TextView by lazyBind(R.id.win)
    private val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    )

    private lateinit var vibrator: Vibrator
    private lateinit var player: MediaPlayer
    private lateinit var bgColors: IntArray
    private lateinit var fgColors: IntArray
    private lateinit var settings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_game)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        settings = getDefaultSharedPreferences(applicationContext)
        val defTheme = settings.getBoolean("defTheme", true)
        if (defTheme) {
            bgColors = applicationContext.resources.getIntArray(R.array.nbg)
            fgColors = applicationContext.resources.getIntArray(R.array.nfg)
        } else {
            bgColors = applicationContext.resources.getIntArray(R.array.abg)
            fgColors = applicationContext.resources.getIntArray(R.array.afg)
        }
        normalMode = settings.getBoolean("normalMode", true)
        if (normalMode) {
        } else {
            timer = true
            win.alpha = 1f
        }
        val extraSize = intent.extras.get("size") as String
        size = extraSize.toInt()
        grid.rowCount = size
        grid.columnCount = size
        displayWidth = windowManager.defaultDisplay.width
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        player = MediaPlayer.create(this, R.raw.click)
        setHeader()
        initGame()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }

    private fun initGame() {
        winColorIndex = Random().nextInt(4)
        background.setBackgroundColor(bgColors[winColorIndex])
        val margin = 4
        val width = displayWidth - (2*margin) - (margin * 2 * size)
        val bSize = width / size
        val gridSize = size * size
        grid.setPadding(margin, margin, margin, margin)
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
            val colorIndex = Random().nextInt(4)
            b.tag = colorIndex
            setButtonColor(b, colorIndex)
            grid.addView(b)
            i++
        }
    }

    private val onClick = View.OnClickListener {
        if (gameOver) return@OnClickListener
        if (timer) startTimer()
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
        clickCount++
        val cid: Int = it.id
        when (step) {
            0 -> stepZero(cid)
            1 -> stepOne(cid)
            2 -> stepTwo(cid)
        }
        score.text = clickCount.toString()
        if (checkWin()) gameOver(false)
    }

    private fun startTimer() {
        object : CountDownTimer(30000,1000) {
            override fun onTick(p0: Long) {
                if (gameOver) {
                    cancel()
                    return
                }
                if (timerCount == 0) {
                    cancel()
                    gameOver(true)
                    return
                }
                win.text = timerCount.toString()
                timerCount--
            }
            override fun onFinish() {
                gameOver(true)
            }
        }.start()
        timer = false
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

    private fun checkWin(): Boolean {
        var bid = 1
        var win = true
        while (bid <= size * size) {
            val b = bind<Button>(bid)
            val colorIndex = b.tag as Int
            if (colorIndex != winColorIndex) {
                win = false
                break
            }
            bid++
        }
        return win
    }

    private fun gameOver(lose: Boolean) {
        gameOver = true
        if (lose)
            win.text = getString(R.string.lose)
        else
            win.text = getString(R.string.win)
        saveScore()
        grid.animate().alpha(0f).setDuration(1000).start()
        win.animate().alpha(100f).setDuration(1000).start()
    }

    private fun saveScore() {
        var mode = if (normalMode)
            "normal"
        else
            "timer"
        mode += size.toString()
        val prev = settings.getInt(mode, 0)
        if (clickCount > prev)
            settings.edit().putInt(mode, clickCount).apply()
    }

    private fun incStep() {
        step = (step + 1) % 3
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

    private fun nextColor(colorIndex: Int): Int = (colorIndex + 1) % 4

    private fun setButtonColor(button: Button, colorIndex: Int) =
            button.setBackgroundColor(fgColors[colorIndex])

    private fun <T: View> Activity.bind(@IdRes id: Int): T {
        @Suppress("UNCHECKED_CAST")
        return findViewById(id)
    }

    private fun <T: View> Activity.lazyBind(@IdRes id: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy { findViewById<T>(id) }
    }

}
