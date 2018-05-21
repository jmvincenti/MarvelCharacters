package com.jmvincenti.marvelcharacters.data.api

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 * Generate the hash for the API
 */
class HashGenerator {
    companion object {
        fun getHash(timestamp: String, publicKey: String, privateKey: String): String {
            try {
                val value = timestamp + privateKey + publicKey
                val md5Encoder = MessageDigest.getInstance("MD5")
                val md5Bytes = md5Encoder.digest(value.toByteArray())

                val md5 = StringBuilder()
                for (i in md5Bytes.indices) {
                    md5.append(Integer.toHexString((md5Bytes[i].toInt() and 0xFF or 0x100)).substring(1, 3))
                }
                return md5.toString()
            } catch (e: NoSuchAlgorithmException) {
                throw Exception("failed to generate hash")
            }

        }
    }
}