package com.javiermp.datasources.remote.provider

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.javiermp.datasources.remote.serializer.DateDeserializer
import java.util.Date

/**
 * Provide "make" methods to create instances of [OpenbankMobileTestService]
 * and its related dependencies, such as OkHttpClient, Gson, etc.
 */
object GsonProvider {

    fun makeGson(): Gson {
        val dateDeserializer = DateDeserializer()
        dateDeserializer.addDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        dateDeserializer.addDateFormat("yyyy/MM/dd")
        dateDeserializer.addDateFormat("yyyy-MM-dd")
        return GsonBuilder()
                .setLenient()
                .registerTypeAdapter(Date::class.java, dateDeserializer)
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
    }
}