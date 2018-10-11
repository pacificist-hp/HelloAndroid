package com.android.pacificist.helloandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.w("heping", "3 + 4 = " + sum(3, 4))
    }

    private fun sum(a: Int, b: Int): Int {
        return a + b
    }
}
