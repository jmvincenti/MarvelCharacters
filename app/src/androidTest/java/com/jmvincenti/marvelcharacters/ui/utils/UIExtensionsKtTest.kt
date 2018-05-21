package com.jmvincenti.marvelcharacters.ui.utils

import android.test.mock.MockContext
import com.jmvincenti.marvelcharacters.R
import com.jmvincenti.marvelcharacters.data.model.ApiImage
import org.junit.Assert
import org.junit.Test


class UIExtensionsKtTest {


    @Test
    fun testGetLandscapePath() {
        val path = "test_path"
        val extension = "test_extension"
        val api = ApiImage(path, extension)
        val context = MockContext()
        val landscapeVariant = context.getString(R.string.image_landscape_variant)
        val landscapeLargeariant = context.getString(R.string.image_landscape_large_variant)
        val portraitVariant = context.getString(R.string.image_portrait_large_variant)
        Assert.assertEquals("test_path/$landscapeVariant.test_extension", api.getLandscapePath(context))
        Assert.assertEquals("test_path/$landscapeLargeariant.test_extension", api.getLandscapeLargePath(context))
        Assert.assertEquals("test_path/$portraitVariant.test_extension", api.getPortraitLargePath(context))
    }
}