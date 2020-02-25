package com.brent.navigation

import android.util.Log

internal object NavLog {

    private val nameSpace = "Navigation"

    var debugLogEnabled = false

    fun e(message: String) {
        Log.e(nameSpace, message)
    }

    fun d(message: String) {
        if(debugLogEnabled) {
            Log.d(nameSpace, message)
        }
    }

    fun i(message: String) {
        if(debugLogEnabled) {
            Log.i(nameSpace, message)
        }
    }
}