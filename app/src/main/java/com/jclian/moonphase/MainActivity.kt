package com.jclian.moonphase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import java.util.*

class MainActivity : AppCompatActivity() {
    var timer: CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        timer = object : CountDownTimer(30_000, 1_000) {
            override fun onTick(millisUntilFinished: Long) {
                val moonPhaseView = findViewById<MoonPhaseView>(R.id.mpv)
                moonPhaseView.mPhase = (millisUntilFinished / 1000L).toInt()

            }

            override fun onFinish() {
            }
        }
        timer?.start()


    }

    fun clickRestart(view: android.view.View) {
        timer?.start()
    }
}
