package com.greyka.imgr.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.greyka.imgr.R;
import com.greyka.imgr.utilities.myUtils;

import java.util.Objects;

public class LocationInfoPickerDialog extends DialogFragment {

    // Use this instance of the interface to deliver action events
    Callback mcallback;
    String nickName;
    private Button sumbit;
    private EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("oncreateview", "vrea");
        View view = inflater.inflate(R.layout.location_nickname_picker, container, false);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        myUtils.myDensityHelper myDensityHelper = new myUtils.myDensityHelper(getContext());
        getDialog().getWindow().setLayout(myDensityHelper.dp2px(300), myDensityHelper.dp2px(120));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.location_nickname_picker, null);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        builder.setView(view);
        bindViews(view);
        initViews();
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        setCancelable(true);
        // Create the AlertDialog object and return it
        return dialog;
    }

    @SuppressLint("DefaultLocale")
    void bindViews(View view) {
        editText = view.findViewById(R.id.input_location_nickname);
        sumbit = view.findViewById(R.id.submit);
    }

    void initViews() {
        sumbit.setOnClickListener(v -> {
            if (Objects.equals(nickName, "") || nickName == null) {
                nickName = "未命名";
            }
            mcallback.callback(nickName);
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nickName = s.toString();
            }
        });
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public void setCallback(Callback callback) {
        this.mcallback = callback;
    }

    public interface Callback {
        void callback(String nickname);
    }
}
