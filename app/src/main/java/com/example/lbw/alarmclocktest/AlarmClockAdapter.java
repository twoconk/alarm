package com.example.lbw.alarmclocktest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lbw on 2017/6/26.
 */

public class AlarmClockAdapter extends ArrayAdapter<AlarmClock> {
    private int resourceId;
    private boolean isEdit;
    private boolean is_all_pick;
    private MyAlarmClockHelper helper;

    public AlarmClockAdapter(@NonNull Context context, int resource, @NonNull List<AlarmClock> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AlarmClock alarmClock = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView time = (TextView) view.findViewById(R.id.alarm_view_time);
        TextView title = (TextView) view.findViewById(R.id.alarm_view_title);
        TextView week = (TextView) view.findViewById(R.id.alarm_view_week);
        Switch isopen = (Switch) view.findViewById(R.id.alarm_view_switch);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.check_box);
        time.setText(alarmClock.getTime());
        title.setText(alarmClock.getTitle());
        week.setText(alarmClock.getWeektext());
        if (!isEdit) {
            isopen.setChecked(alarmClock.getIsOpen() == 1 ? true : false);
        }
        if (is_all_pick) {
            checkBox.setChecked(is_all_pick ? true : false);
        } else {
            checkBox.setChecked(alarmClock.getOpenCheckBox() == 1 ? true : false);
        }
        isopen.setVisibility(isEdit ? View.GONE : View.VISIBLE);
        checkBox.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        return view;
    }

    public void toggleEditStatus() {
        isEdit = !isEdit;
        notifyDataSetChanged();
    }

    public void toggleAllPickStatus() {
        is_all_pick = !is_all_pick;
        notifyDataSetChanged();
    }
}
