package com.example.redvoznje;

import android.app.AlertDialog;
import android.content.Context;

public class DialogBuilder extends AlertDialog.Builder {

    public static DialogBuilder get(final Context context) {
        return new DialogBuilder(context, 0);
    }

    private DialogBuilder(final Context context, final int theme) {
        super(context, theme);
    }
}
