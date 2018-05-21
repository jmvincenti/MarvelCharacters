package com.jmvincenti.marvelcharacters.ui.utils

import android.content.Context
import com.jmvincenti.marvelcharacters.R
import com.jmvincenti.marvelcharacters.data.model.ApiImage

/**
 * UiUtils extensions for UI
 */


/**
 * Get the full image path depending screen size
 */
fun ApiImage.getLandscapePath(context: Context): String = "$path/${context.getString(R.string.image_landscape_variant)}.$extension"
fun ApiImage.getPortraitLargePath(context: Context): String = "$path/${context.getString(R.string.image_portrait_large_variant)}.$extension"
fun ApiImage.getLandscapeLargePath(context: Context): String = "$path/${context.getString(R.string.image_landscape_large_variant)}.$extension"
