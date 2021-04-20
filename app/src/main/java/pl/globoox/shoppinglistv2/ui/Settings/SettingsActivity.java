package pl.globoox.shoppinglistv2.ui.Settings;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import pl.globoox.shoppinglistv2.R;

public class SettingsActivity extends PreferenceActivity {

    int alarmSoundTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load settings fragmet
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainSettingsFragment()).commit();
    }


    public static class MainSettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            bindSummaryValue(findPreference("phoneNumber1"));
            bindSummaryValue(findPreference("phoneNumber2"));
            bindSummaryValue(findPreference("smsText"));
            bindSummaryValue(findPreference("alarm_vibration_list"));
            bindSummaryValue(findPreference("alarm_sound_list"));
        }
    }


    private static void bindSummaryValue(Preference preference) {
        preference.setOnPreferenceChangeListener(listener);
        listener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
    }


    private static Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            // LIST PREFERENCE
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set summary
                preference.setSummary(index > -1
                        ? listPreference.getEntries()[index]
                        : null);

                // EDITTEXT PREFERENCE
            } else if (preference instanceof EditTextPreference) {
                preference.setSummary(stringValue);
            }

            return true;
        }
    };
}


