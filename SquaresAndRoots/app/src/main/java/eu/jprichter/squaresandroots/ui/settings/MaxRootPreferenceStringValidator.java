package eu.jprichter.squaresandroots.ui.settings;

import android.content.Context;
import android.preference.Preference;

import com.google.inject.Inject;

import eu.jprichter.squaresandroots.kernel.IKernel;
import roboguice.RoboGuice;
import roboguice.util.Ln;

/**
 * A Validator for the maxRoot preference entered as a String
 *
 * Created by jrichter on 28.10.2015.
 */
public class MaxRootPreferenceStringValidator implements
        Preference.OnPreferenceChangeListener {

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        Ln.d("XXXXXXXXXXXXXXXXX Preference changed: " + newValue);

        int newIntValue;
        boolean error = false;

        try{
            newIntValue = Integer.valueOf(newValue.toString());

            if(newIntValue < IKernel.MIN_MAX_ROOT || newIntValue > IKernel.MAX_MAX_ROOT)
                error = true;

        } catch (Exception e) {
            Ln.d("XXXXXXXXXXXXXXXXX Error: Preference String could not be converted: '" +
                    newValue.toString() + "'");
            error = true;
        }

        return !error;
    }

}
