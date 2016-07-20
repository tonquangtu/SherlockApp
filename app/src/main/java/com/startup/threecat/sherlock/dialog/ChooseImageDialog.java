package com.startup.threecat.sherlock.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.startup.threecat.sherlock.R;


/**
 * Created by Dell on 17-Jul-16.
 */
public class ChooseImageDialog extends DialogFragment {

    public static final int PICK_FROM_GALLERY = 0;
    public static final int TAKE_PHOTO = 1;
    public interface ChooseImageDialogListener {

        public void onDialogNegativeClick(DialogFragment dialog);
        public void onDialogItemClick(DialogFragment dialog, int position);

    }

    private ChooseImageDialogListener listener;

    public void setListener(ChooseImageDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_dialog_choose_image);
        builder.setItems(R.array.chooses_image, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogItemClick(ChooseImageDialog.this, which);
            }
        });



        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogNegativeClick(ChooseImageDialog.this);
            }
        });
        return builder.create();
    }
}
