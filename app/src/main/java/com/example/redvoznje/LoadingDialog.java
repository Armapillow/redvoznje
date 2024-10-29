package com.example.redvoznje;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class LoadingDialog extends DialogFragment {

    private String mMessage;

    public LoadingDialog() {
        this.mMessage = "";
    }

    public LoadingDialog(String message) {
        this.mMessage = message;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(mMessage)
                .setPositiveButton("OK", (dialog, which) -> {

                });

        return builder.create();

    }
}
