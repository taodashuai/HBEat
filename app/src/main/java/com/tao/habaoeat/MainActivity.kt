package com.tao.habaoeat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tsc = Executors.newSingleThreadScheduledExecutor()
        tsc.scheduleAtFixedRate({ runOnUiThread { eatview.invalidate() } }, 200, 200, TimeUnit.MILLISECONDS)
    }
}
