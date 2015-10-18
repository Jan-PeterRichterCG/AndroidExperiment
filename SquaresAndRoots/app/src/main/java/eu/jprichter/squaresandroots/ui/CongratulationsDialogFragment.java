package eu.jprichter.squaresandroots.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import eu.jprichter.squaresandroots.R;
import eu.jprichter.squaresandroots.kernel.IKernel;
import roboguice.activity.RoboActivity;
import roboguice.fragment.RoboDialogFragment;

/**
 * Created by jrichter on 18.10.2015.
 */
@Singleton
public class CongratulationsDialogFragment extends RoboDialogFragment {

    @Inject
    IKernel kernel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.congratulations).
                setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // don't do anything
                    }
                });
        return builder.create();
    }
}
