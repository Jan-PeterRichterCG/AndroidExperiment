package eu.jprichter.squaresandroots.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;

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

    @Inject
    IKernel kernel;

    private MaxRootPreferenceStringValidator maxRootPreferenceStringValidator;
    private MinRootPreferenceStringValidator minRootPreferenceStringValidator;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        maxRootPreferenceStringValidator = new MaxRootPreferenceStringValidator();
        minRootPreferenceStringValidator = new MinRootPreferenceStringValidator();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Ln.d("XXXXXXXXXXXXXXXXX Settings Activity resumed");

        String maxRootString = getPreferenceScreen().getSharedPreferences().getString(
                getResources().getString(R.string.key_pref_max_root), "");

        Preference pref_max_root = findPreference(getResources().getString(R.string.key_pref_max_root));
        pref_max_root.setSummary(getResources().getString(R.string.pref_max_root_summary_prefix)
                + maxRootString);

        pref_max_root.setOnPreferenceChangeListener(maxRootPreferenceStringValidator);

        String minRootString = getPreferenceScreen().getSharedPreferences().getString(
                getResources().getString(R.string.key_pref_min_root), "");

        Preference pref_min_root = findPreference(getResources().getString(R.string.key_pref_min_root));
        pref_min_root.setSummary(getResources().getString(R.string.pref_min_root_summary_prefix)
                + minRootString);

        pref_min_root.setOnPreferenceChangeListener(minRootPreferenceStringValidator);

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        Ln.d("XXXXXXXXXXXXXXXXX SharedPreference " + key + " changed: " +
                sharedPreferences.getString(key, ""));

        String keyPrefMaxRoot = getResources().getString(R.string.key_pref_max_root);
        if (key.equals(keyPrefMaxRoot)) {

            String maxRootPrefString = sharedPreferences.getString(keyPrefMaxRoot, "");

            Preference pref = findPreference(keyPrefMaxRoot);
            pref.setSummary(getResources().getString(R.string.pref_max_root_summary_prefix)
                    + maxRootPrefString);

            Ln.d("XXXXXXXXXXXXXXXXX SharedPreference " + keyPrefMaxRoot + " changed: " + maxRootPrefString);

            kernel.setMaxRoot(Integer.valueOf(maxRootPrefString));

            return;
        }

        String keyPrefMinRoot = getResources().getString(R.string.key_pref_min_root);
        if (key.equals(keyPrefMinRoot)) {

            String minRootPrefString = sharedPreferences.getString(keyPrefMinRoot, "");

            Preference pref = findPreference(keyPrefMinRoot);
            pref.setSummary(getResources().getString(R.string.pref_max_root_summary_prefix)
                    + minRootPrefString);

            Ln.d("XXXXXXXXXXXXXXXXX SharedPreference " + keyPrefMinRoot + " changed: " + minRootPrefString);

            kernel.setMinRoot(Integer.valueOf(minRootPrefString));

            return;
        }
    }

}
