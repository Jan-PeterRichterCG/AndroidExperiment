package eu.jprichter.squaresandroots.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;

import com.google.inject.Inject;

import eu.jprichter.squaresandroots.R;
import eu.jprichter.squaresandroots.kernel.IKernel;
import eu.jprichter.squaresandroots.ui.StatisticsDrawableView;
import roboguice.activity.RoboPreferenceActivity;
import roboguice.inject.InjectView;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        maxRootPreferenceStringValidator = new MaxRootPreferenceStringValidator();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Ln.d("XXXXXXXXXXXXXXXXX Settings Activity resumed");

        String maxRootString = getPreferenceScreen().getSharedPreferences().getString(
                getResources().getString(R.string.key_pref_max_root), "");

        Preference pref = findPreference(getResources().getString(R.string.key_pref_max_root));
        pref.setSummary(getResources().getString(R.string.pref_max_root_summary_prefix)
                + maxRootString);

        pref.setOnPreferenceChangeListener(maxRootPreferenceStringValidator);

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
        }
    }

}
