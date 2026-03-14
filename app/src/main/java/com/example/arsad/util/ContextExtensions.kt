package com.example.arsad.util

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

fun Context.getLocalizedContext(lang: String): Context {
    val locale = Locale(lang)
    Locale.setDefault(locale)
    val config = Configuration(resources.configuration)
    config.setLocale(locale)
    return createConfigurationContext(config)
}