package com.example.myapplication.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper  {
//    fun setLocale(context: Context, language: String): Context {
//        val locale = Locale(language)
//        Locale.setDefault(locale)
//
//        val resources: Resources = context.resources
//        val config = Configuration(resources.configuration)
//        config.setLocale(locale)
//
//        return context.createConfigurationContext(config)
//    }

    fun setLocale(context: Context , language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context. resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    fun getCurrentLanguage(context: Context): String {
        return context.resources.configuration.locales.get(0).language
    }
}