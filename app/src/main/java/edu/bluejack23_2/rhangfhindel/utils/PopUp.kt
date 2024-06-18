package edu.bluejack23_2.rhangfhindel.utils

import android.content.Context
import android.widget.Toast

object PopUp {
    fun shortDuration(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun longDuration(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}