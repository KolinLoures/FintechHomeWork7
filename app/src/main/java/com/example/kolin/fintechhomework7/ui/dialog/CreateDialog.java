package com.example.kolin.fintechhomework7.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.kolin.fintechhomework7.R;

/**
 * Dialog fragment to create for {@link com.example.kolin.fintechhomework7.model.Node#value}
 */
public class CreateDialog extends android.support.v4.app.DialogFragment {

    public static final String TAG = CreateDialog.class.getSimpleName();

    private TextInputLayout textInputLayout;
    private EditText editText;

    private TextWatcher textWatcher;

    private CreateDialogCallback callback;

    public interface CreateDialogCallback {
        void onClickPositiveBtn(int value);
    }

    public static CreateDialog newInstance() {
        return new CreateDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog, null);

        textInputLayout = view.findViewById(R.id.fragment_dialog_text_input_layout);
        editText = view.findViewById(R.id.fragment_dialog_edit_text);

        initTextWatcher();


        builder
                .setMessage(R.string.new_node)
                .setView(view)
                .setPositiveButton(R.string.add, (dialogInterface, i) -> {
                    if (callback != null)
                        callback.onClickPositiveBtn(Integer.parseInt(editText.getText().toString()));
                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dismiss());


        return builder.create();
    }

    private void initTextWatcher() {
        textWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = charSequence.toString();
                if (s.isEmpty()) {
                    enablePosButton(false);
                    textInputLayout.setError(getString(R.string.empty));
                }
                else {
                    enablePosButton(true);
                    textInputLayout.setError("");
                }
            }

            public void afterTextChanged(Editable editable) {
            }
        };

        editText.addTextChangedListener(textWatcher);
    }

    private void enablePosButton(boolean enable) {
        ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(enable);
    }

    @Override
    public void onDestroy() {
        editText.removeTextChangedListener(textWatcher);
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateDialogCallback)
            callback = (CreateDialogCallback) context;
        else
            throw new RuntimeException(context.toString() +
                    " must implement " + CreateDialogCallback.class.getCanonicalName());
    }

    @Override
    public void onDetach() {
        callback = null;
        super.onDetach();
    }


}
