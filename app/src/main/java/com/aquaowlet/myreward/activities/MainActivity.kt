package com.aquaowlet.myreward.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.aquaowlet.myreward.R

class MainActivity : AppCompatActivity() {

    private var mainTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainTextView = findViewById(R.id.main_tv)
        mainTextView?.text = "Hello Kotlin"

    }
}
