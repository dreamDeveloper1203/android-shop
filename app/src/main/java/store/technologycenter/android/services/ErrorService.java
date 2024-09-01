package store.technologycenter.android.services;

import android.app.AlertDialog;
import android.content.Context;

import androidx.core.content.ContextCompat;

import store.technologycenter.android.R;

public class ErrorService {


    public static void showErrorDialog(Context context, String message ){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.error));
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(android.R.string.yes, (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.dark));
    }

}
