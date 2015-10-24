package eu.jprichter.squaresandroots.ui;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.google.inject.Inject;

import eu.jprichter.squaresandroots.R;
import eu.jprichter.squaresandroots.kernel.IKernel;
import roboguice.activity.RoboPreferenceActivity;
import roboguice.util.Ln;

/**
 * The Activity to handle preferences settings
 * Created by jrichter on 18.10.2015.
 */
public class SettingsActivity extends RoboPreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject IKernel kernel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        Ln.d("XXXXXXXXXXXXXXXXX Settings Activity resumed");


        String maxRootString = getPreferenceScreen().getSharedPreferences().getString(
                getResources().getString(R.string.key_pref_max_root),"");

        Preference pref = findPreference(getResources().getString(R.string.key_pref_max_root));
        pref.setSummary(getResources().getString(R.string.pref_max_root_summary_prefix)
                + maxRootString);

    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        Ln.d("XXXXXXXXXXXXXXXXX Preference " + key + " changed: " +
                sharedPreferences.getString(key, ""));

        String keyPrefMaxRoot = getResources().getString(R.string.key_pref_max_root);
        if (key.equals(keyPrefMaxRoot)) {

            String maxRootPrefString = sharedPreferences.getString(keyPrefMaxRoot, "");

            Preference pref = findPreference(keyPrefMaxRoot);
            pref.setSummary(getResources().getString(R.string.pref_max_root_summary_prefix)
                    + maxRootPrefString);

           Ln.d("XXXXXXXXXXXXXXXXX Preference " + keyPrefMaxRoot + " changed: " + maxRootPrefString);

           kernel.setMaxRoot(Integer.valueOf(maxRootPrefString));
        }
    }
}
