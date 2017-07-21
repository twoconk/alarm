package com.example.lbw.alarmclocktest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class LabelFragment extends DialogFragment implements View.OnClickListener {
    private getData data;
    private EditText label;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_label, null);
        builder.setView(view);
        view.findViewById(R.id.cancal_setting).setOnClickListener(this);
        view.findViewById(R.id.sure_setting).setOnClickListener(this);
        label = (EditText) view.findViewById(R.id.label_Edit);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancal_setting:
                dismiss();
                break;
            case R.id.sure_setting:
                if (!label.getText().toString().equals("")) {
                    data.inputData(label.getText().toString());
                }
                dismiss();
                break;
        }
    }

    public void setData(getData d) {
        data = d;
    }

    public interface getData {
        void inputData(String label);
    }
}
