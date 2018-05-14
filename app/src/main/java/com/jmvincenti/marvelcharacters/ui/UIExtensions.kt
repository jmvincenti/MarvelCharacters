package com.jmvincenti.marvelcharacters.ui

import android.content.Context
import com.jmvincenti.marvelcharacters.R
import com.jmvincenti.marvelcharacters.data.model.ApiImage

/**
 * Utils extensions for UI
 */


/**
 * Get the full image path depending screen size
 */
fun ApiImage.getFullPath(context: Context): String = "$path/${context.getString(R.string.image_landscape_variant)}.$extension"
