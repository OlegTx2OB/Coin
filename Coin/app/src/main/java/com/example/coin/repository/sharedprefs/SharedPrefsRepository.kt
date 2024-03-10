package com.example.coin.repository.sharedprefs

import android.content.Context
import java.time.LocalDate

private const val SHARED_PREFS_NAME = "MySharedPrefs"

private const val CURRENCY_NAME = "currency_name"
private const val PERIOD_MONTH = "period_month"
private const val PERIOD_YEAR = "period_year"

fun spGetCurrencyName(context: Context): String {
    return getFromSharedPrefs(CURRENCY_NAME, "$", context) as String
}

fun spGetMonth(context: Context): Int {
    return getFromSharedPrefs(PERIOD_MONTH, 0, context) as Int
}

fun spGetYear(context: Context): Int {
    return getFromSharedPrefs(PERIOD_YEAR, LocalDate.now().year, context) as Int
}

fun spSaveCurrencyName(value: String, context: Context) {
    putIntoSharedPrefs(CURRENCY_NAME, value, context)
}

fun spSaveMonth(value: Int, context: Context) {
    putIntoSharedPrefs(PERIOD_MONTH, value, context)
}

fun spSaveYear(value: Int, context: Context) {
    putIntoSharedPrefs(PERIOD_YEAR, value, context)
}

private fun getFromSharedPrefs(key: String, defaultValue: Any, context: Context): Any {
    val sP = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    return if (defaultValue is String) {
        sP.getString(key, defaultValue) ?: defaultValue
    } else {
        sP.getInt(key, defaultValue as Int)
    }
}

private fun putIntoSharedPrefs(key: String, value: Any, context: Context) {
    val sP = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    val editor = sP.edit()
    if (value is String) {
        editor.putString(key, value)
    } else {
        editor.putInt(key, value as Int)
    }
    editor.apply()
}