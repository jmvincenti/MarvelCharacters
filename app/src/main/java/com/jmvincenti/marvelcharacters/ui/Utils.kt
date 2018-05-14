package com.jmvincenti.marvelcharacters.ui

import android.content.Context
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri


/**
 * TODO: Add a class header comment! 😘
 */
class Utils {
    companion object {
        fun openLink(context : Context, url : String) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(browserIntent)
        }
    }
}