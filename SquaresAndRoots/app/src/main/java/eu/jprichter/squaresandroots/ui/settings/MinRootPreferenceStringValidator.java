package eu.jprichter.squaresandroots.ui.settings;

import android.preference.Preference;

import eu.jprichter.squaresandroots.kernel.IKernel;
import roboguice.util.Ln;

/**
 * A Validator for the minRoot preference entered as a String
 *
 * Created by jrichter on 28.10.2015.
 */
public class MinRootPreferenceStringValidator implements
        Preference.OnPreferenceChangeListener {

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        Ln.d("XXXXXXXXXXXXXXXXX Preference changed: " + newValue);

        int newIntValue;
        boolean error = false;

        try{
            newIntValue = Integer.valueOf(newValue.toString());

            if(newIntValue < IKernel.MIN_MIN_ROOT || newIntValue > IKernel.MAX_MIN_ROOT)
                error = true;

        } catch (Exception e) {
            Ln.d("XXXXXXXXXXXXXXXXX Error: Preference String could not be converted: '" +
                    newValue.toString() + "'");
            error = true;
        }

        return !error;
    }

}
