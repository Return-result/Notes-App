package com.return_result.notes.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.return_result.notes.R
import com.return_result.notes.helpers.ExportHelper
import com.return_result.notes.miscellaneous.Constants
import com.return_result.notes.miscellaneous.openLink

class Settings : PreferenceFragmentCompat() {

    private lateinit var mContext: Context
    private lateinit var exportHelper: ExportHelper

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exportHelper = ExportHelper(mContext, this)

        val themePreference: ListPreference? = findPreference(mContext.getString(R.string.themeKey))

        val maxItemsPref: EditTextPreference? = findPreference(mContext.getString(R.string.maxItemsToDisplayInListKey))
        val maxLinesPref: EditTextPreference? = findPreference(mContext.getString(R.string.maxLinesToDisplayInNoteKey))

        val exportNotesPref: Preference? = findPreference(mContext.getString(R.string.exportNotesToAFileKey))
        val importNotesPref: Preference? = findPreference(mContext.getString(R.string.importNotesFromAFileKey))

//        val ratePref: Preference? = findPreference(mContext.getString(R.string.rateKey))
//        val githubPref: Preference? = findPreference(mContext.getString(R.string.githubKey))
//        val librariesPref: Preference? = findPreference(mContext.getString(R.string.librariesKey))

        exportNotesPref?.setOnPreferenceClickListener {
            exportHelper.exportBackup()
            return@setOnPreferenceClickListener true
        }

        importNotesPref?.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/xml"
            }

            startActivityForResult(intent, Constants.RequestCodeImportFile)
            return@setOnPreferenceClickListener true
        }

//        githubPref?.setOnPreferenceClickListener {
//            openLink(Github)
//            return@setOnPreferenceClickListener true
//        }

//        ratePref?.setOnPreferenceClickListener {
//            openLink(PlayStore)
//            return@setOnPreferenceClickListener true
//        }

//        librariesPref?.setOnPreferenceClickListener {
//            val builder = MaterialAlertDialogBuilder(mContext)
//            builder.setTitle(R.string.libraries)
//            builder.setItems(R.array.libraries) { dialog, which ->
//                when (which) {
//                    0 -> openLink(PrettyTime)
//                    2 -> openLink(MaterialComponents)
//                }
//            }
//            builder.setNegativeButton(R.string.cancel, null)
//            builder.show()
//            return@setOnPreferenceClickListener true
//        }

        maxItemsPref?.setOnPreferenceChangeListener { _, newValue ->
            return@setOnPreferenceChangeListener newValue.toString().isNotEmpty()
        }

        maxLinesPref?.setOnPreferenceChangeListener { _, newValue ->
            return@setOnPreferenceChangeListener newValue.toString().isNotEmpty()
        }

        themePreference?.setOnPreferenceChangeListener { _, newValue ->
            when (newValue) {
                getString(R.string.darkKey) -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                getString(R.string.lightKey) -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                getString(R.string.followSystemKey) -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            return@setOnPreferenceChangeListener true
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.RequestCodeExportFile && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                exportHelper.writeFileToUri(uri)
            }
        }
        if (requestCode == Constants.RequestCodeImportFile && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                val inputStream = mContext.contentResolver.openInputStream(uri)
                if (inputStream != null) {
                    exportHelper.importBackup(inputStream)
                }
            }
        }
    }

    companion object {
//        private const val Github = "https://github.com/OmGodse/Notally"
//        private const val PlayStore = "https://play.google.com/store/apps/details?id=com.omgodse.notally"
//        private const val PrettyTime = "https://github.com/ocpsoft/prettytime"
//        private const val MaterialComponents = "https://github.com/material-components/material-components-android"
    }
}