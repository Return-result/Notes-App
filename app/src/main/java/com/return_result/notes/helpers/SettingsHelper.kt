package com.return_result.notes.helpers

import android.content.Context
import androidx.preference.PreferenceManager
import com.return_result.notes.R

class SettingsHelper(private val context: Context) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    private fun getPreferenceValue(key: Int, defaultValue: Int): String {
        val actualKey = context.getString(key)
        val defaultValueString = context.getString(defaultValue)
        return preferences.getString(actualKey, defaultValueString).toString()
    }

    fun getView() = getPreferenceValue(R.string.viewKey, R.string.listKey)

    fun getCardType() = getPreferenceValue(R.string.cardTypeKey, R.string.elevatedKey)

    fun getMaxLines() = getPreferenceValue(R.string.maxLinesToDisplayInNoteKey, R.string.eight).toInt()

    fun getMaxItems() = getPreferenceValue(R.string.maxItemsToDisplayInListKey, R.string.four).toInt()

    fun getShowDateCreated() = preferences.getBoolean(context.getString(R.string.showDateCreatedKey), true)
}