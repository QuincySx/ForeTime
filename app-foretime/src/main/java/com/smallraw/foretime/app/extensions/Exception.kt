package com.smallraw.foretime.app.extensions

import android.util.Log

fun Exception.printCallStack() {
    val stackElements = this.stackTrace
    Log.e("Stack", "-----------------------------------")
    for (i in stackElements.indices) {
        val buffer = StringBuffer()
        buffer.append(stackElements[i].className).append("\t")
        buffer.append(stackElements[i].fileName).append("\t")
        buffer.append(stackElements[i].lineNumber).append("\t")
        buffer.append(stackElements[i].methodName)

        Log.e("Stack", buffer.toString())
    }
    Log.e("Stack", "-----------------------------------")
}