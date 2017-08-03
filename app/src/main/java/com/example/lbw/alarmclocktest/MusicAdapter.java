package com.example.lbw.alarmclocktest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lbw on 2017/6/27.
 */

public class MusicAdapter extends ArrayAdapter<Music> {
    private int resourceId;

    public MusicAdapter(@NonNull Context context, int resource, @NonNull List<Music> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Music m = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView music = (TextView) view.findViewById(R.id.music);
        RadioButton radioButton = (RadioButton) view.findViewById(R.id.music_button);
        music.setText(m.getName());
        if (m.getIsOpen() == 0) {
            radioButton.setChecked(false);
        } else if (m.getIsOpen() == 1) {
            radioButton.setChecked(true);
        }
        return view;
    }
}
