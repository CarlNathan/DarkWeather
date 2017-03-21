package com.example.carludren.darkweather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by carludren on 3/20/17.
 */

public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(getArguments().getString(getString(R.string.alert_title_key)))
                .setMessage(getArguments().getString(getString(R.string.alert_message_key)))
                .setPositiveButton(getArguments().getString(getString(R.string.alert_affirmative_key)), null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
