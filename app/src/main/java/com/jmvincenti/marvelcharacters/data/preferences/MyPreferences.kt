package com.jmvincenti.marvelcharacters.data.preferences

import com.orhanobut.hawk.Hawk

class MyPreferences {

    companion object {
        private const val MAX_CHARACTER_TAG = "com.jmvincenti.marvelcharacters.maxcharacters"
        var maxCharacter: Int
            get() {
                return Hawk.get(MAX_CHARACTER_TAG, 100)
            }
            set(value) {
                Hawk.put(MAX_CHARACTER_TAG, value)
            }
    }

}