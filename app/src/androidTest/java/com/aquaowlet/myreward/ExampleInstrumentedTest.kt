/*
 * Created by Eric Hongming Lin on 20/05/18 3:05 AM
 * Copyright (c) 2018. All right reserved
 *
 * Last modified 5/05/18 6:53 PM
 */

package com.aquaowlet.myreward

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.aquaowlet.myreward", appContext.packageName)
    }
}
