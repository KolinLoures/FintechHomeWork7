package com.example.kolin.fintechhomework7.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.kolin.fintechhomework7.R;

/**
 * Dialog fragment to ask Delete or Add Reference between {@link com.example.kolin.fintechhomework7.model.Node}s
 */

public class AskDialog extends DialogFragment {

    public static final String TAG = AskDialog.class.getSimpleName();

    private static final String ARG_TYPE_DIALOG = "type_dialog";
    private static final String ARG_ID = "id";

    private int currentType;
    private long currentId;

    /**
     * Type of dialog
     *
     * @param type 0 - add dialog; 1 - delete dialog
     * @return
     */
    public static AskDialog newInstance(int type, long id) {

        Bundle args = new Bundle();
        args.putInt(ARG_TYPE_DIALOG, type);
        args.putLong(ARG_ID, id);
        AskDialog fragment = new AskDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public interface AskDialogCallback {
        /**
         * @param type 0 -  add; 1 - delete
         */
        void onClickPositiveBtn(int type, long id);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            currentType = getArguments().getInt(ARG_TYPE_DIALOG);
            currentId = getArguments().getLong(ARG_ID);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        String message = null;
        if (currentType == 0)
            message = getString(R.string.add_reference);
        else
            message = getString(R.string.remove_reference);

        builder.setMessage(message)
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dismiss())
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    if (getParentFragment() != null)
                        ((AskDialogCallback) getParentFragment()).onClickPositiveBtn(currentType, currentId);
                });


        return builder.create();
    }

}
