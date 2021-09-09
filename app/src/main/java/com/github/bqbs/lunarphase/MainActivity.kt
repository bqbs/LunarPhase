package com.github.bqbs.lunarphase

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import com.github.bqbs.lunarphase.R

class MainActivity : AppCompatActivity() {
    var timer: CountDownTimer? = null
    var mLunarPhaseView: LunarPhaseView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mLunarPhaseView = findViewById<LunarPhaseView>(R.id.mpv)

        timer = object : CountDownTimer(30_000, 100L) {
            override fun onTick(millisUntilFinished: Long) {
                mLunarPhaseView?.mPhase = (millisUntilFinished / 100L).toInt()
            }

            override fun onFinish() {
            }
        }
        timer?.start()


    }

    fun clickRestart(view: View) {
        timer?.start()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        Log.d("MainActivity", "onConfigurationChanged#${newConfig.orientation}")
        super.onConfigurationChanged(newConfig)
        mLunarPhaseView?.mRotate =
            if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                90
            } else {
                0
            }
    }
}
