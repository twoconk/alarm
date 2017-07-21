package com.example.lbw.alarmclocktest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;;

/**
 * Created by lbw on 2017/6/24.
 */

public class SleepTime extends DialogFragment implements NumberPicker.OnValueChangeListener, NumberPicker.OnScrollListener, NumberPicker.Formatter, View.OnClickListener {
    private NumberPicker hourPicker;
    private OnLoginInforCompleted mOnLoginInforCompleted;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_sleep_time, null);
        hourPicker = (NumberPicker) view.findViewById(R.id.hour_picker);
        builder.setView(view);
        init();
        view.findViewById(R.id.cancal_setting_hour).setOnClickListener(this);
        view.findViewById(R.id.sure_setting_hour).setOnClickListener(this);
        return builder.create();
    }

    private void init() {
        hourPicker.setFormatter(this);
        hourPicker.setOnValueChangedListener(this);
        hourPicker.setOnScrollListener(this);
        hourPicker.setMaxValue(30);
        hourPicker.setMinValue(1);
        hourPicker.setValue(5);
    }

    @Override
    public String format(int value) {
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }

    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {
        switch (scrollState) {
            case NumberPicker.OnScrollListener.SCROLL_STATE_FLING:
                break;
            case NumberPicker.OnScrollListener.SCROLL_STATE_IDLE:
                break;
            case NumberPicker.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                break;
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancal_setting_hour:
                dismiss();
                break;
            case R.id.sure_setting_hour:
                mOnLoginInforCompleted.inputLoginInforCompleted(String.valueOf(hourPicker.getValue()));
                dismiss();
                break;
        }
    }

    public void setOnLoginInforCompleted(OnLoginInforCompleted onLoginInforCompleted) {
        mOnLoginInforCompleted = onLoginInforCompleted;
    }

    public interface OnLoginInforCompleted {
        void inputLoginInforCompleted(String hour);
    }
}
