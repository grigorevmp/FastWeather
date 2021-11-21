package com.grigorevmp.fastweather.ui.settings

import android.os.Bundle
import android.view.View
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.grigorevmp.fastweather.R
import com.grigorevmp.fastweather.common.utils.Utils

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val screen = preferenceScreen
        val listPreference =
            findPreference<ListPreference>(Utils.getString(R.string.theme_preferences_key))
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
            screen.removePreference(listPreference)
        }
        listPreference?.setOnPreferenceChangeListener { _, newValue ->
            listPreference.value = newValue.toString()
            true
        }
    }
}