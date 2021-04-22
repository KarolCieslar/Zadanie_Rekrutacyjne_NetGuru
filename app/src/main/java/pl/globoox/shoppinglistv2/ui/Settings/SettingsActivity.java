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

        }
    }


}


