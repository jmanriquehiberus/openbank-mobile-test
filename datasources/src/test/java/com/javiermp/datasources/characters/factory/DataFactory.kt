package com.javiermp.datasources.characters.factory

import com.javiermp.datasources.BuildConfig
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * Factory class for data instances
 */
class DataFactory {

    companion object Factory {

        fun getRandomString(length: Int): String {
            val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            return (1..length)
                .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("");
        }

        fun getRandomInt(): Int {
            return ThreadLocalRandom.current().nextInt(0, 1000 + 1)
        }

        fun getHash(timestamp: String): String {
            val md = MessageDigest.getInstance("MD5")
            val input = "$timestamp${BuildConfig.PRIVATE_KEY}${BuildConfig.API_KEY}"
            return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0').toLowerCase(
                Locale.ROOT)
        }
    }
}