package com.aquaowlet.myreward.activities

import android.app.Fragment
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.aquaowlet.myreward.R
import com.aquaowlet.myreward.fragments.BlankFragment

class MainActivity : AppCompatActivity(), BlankFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        mainTextView?.text = "onFragmentInteraction"
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var mainTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        mainTextView = findViewById(R.id.main_tv)
//        mainTextView?.text = "Hello Kotlin"

    }
}
