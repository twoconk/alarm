package com.example.lbw.alarmclocktest;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class RepetitionActivity extends AppCompatActivity {
    private int resultCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repetition);

        final CheckBox Monday = (CheckBox) findViewById(R.id.Monday);
        final CheckBox Tuesday = (CheckBox) findViewById(R.id.Tuesday);
        final CheckBox Wednesday = (CheckBox) findViewById(R.id.Wednesday);
        final CheckBox Thurday = (CheckBox) findViewById(R.id.Thurday);
        final CheckBox Friday = (CheckBox) findViewById(R.id.Friday);
        final CheckBox Saturday = (CheckBox) findViewById(R.id.Saturday);
        final CheckBox Sunday = (CheckBox) findViewById(R.id.Sunday);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String monday;
                String mon;
                String tuesday;
                String tues;
                String wednesday;
                String wednes;
                String thurday;
                String thur;
                String friday;
                String fri;
                String saturday;
                String satur;
                String sunday;
                String sun;
                String week;
                if (Monday.isChecked()) {
                    monday = getString(R.string.monday);
                    mon = "1";
                } else {
                    monday = "";
                    mon = "0";
                }
                if (Tuesday.isChecked()) {
                    tuesday = getString(R.string.tuesday);
                    tues = "1";
                } else {
                    tuesday = "";
                    tues = "0";
                }
                if (Wednesday.isChecked()) {
                    wednesday = getString(R.string.wednesday);
                    wednes = "1";
                } else {
                    wednesday = "";
                    wednes = "0";
                }
                if (Thurday.isChecked()) {
                    thurday = getString(R.string.thurday);
                    thur = "1";
                } else {
                    thurday = "";
                    thur = "0";
                }
                if (Friday.isChecked()) {
                    friday = getString(R.string.friday);
                    fri = "1";
                } else {
                    friday = "";
                    fri = "0";
                }
                if (Saturday.isChecked()) {
                    saturday = getString(R.string.saturday);
                    satur = "1";
                } else {
                    saturday = "";
                    satur = "0";
                }
                if (Sunday.isChecked()) {
                    sunday = getString(R.string.sunday);
                    sun = "1";
                } else {
                    sunday = "";
                    sun = "0";
                }

                if (Monday.isChecked() && Tuesday.isChecked() && Wednesday.isChecked() && Thurday.isChecked() && Friday.isChecked() && Saturday.isChecked() && Sunday.isChecked()) {
                    week = getString(R.string.everyday);
                } else if (Monday.isChecked() && Tuesday.isChecked() && Wednesday.isChecked() && Thurday.isChecked() && Friday.isChecked() && !Saturday.isChecked() && !Sunday.isChecked()) {
                    week = getString(R.string.workday);
                } else if (Saturday.isChecked() && Sunday.isChecked() && !Monday.isChecked() && !Tuesday.isChecked() && !Wednesday.isChecked() && !Thurday.isChecked() && !Friday.isChecked()) {
                    week = getString(R.string.weekend);
                } else if (!Monday.isChecked() && !Tuesday.isChecked() && !Wednesday.isChecked() && !Thurday.isChecked() && !Friday.isChecked() && !Saturday.isChecked() && !Sunday.isChecked()) {
                    week = getString(R.string.without_repetition);
                } else {
                    week = monday + " " + tuesday + " " + wednesday + " " + thurday + " " + friday + " " + saturday + " " + sunday;
                }
                String WEEK = sun + mon + tues + wednes + thur + fri + satur;
                Intent intent = new Intent(RepetitionActivity.this, NewAlarmClockActivity.class);
                intent.putExtra("WEEK", WEEK);
                intent.putExtra("week_data", week);
                RepetitionActivity.this.setResult(resultCode, intent);
                finish();
            }
        });
        final Button pick_all = (Button) findViewById(R.id.pick_all);
        pick_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pick_all.getText().toString().equals(getString(R.string.all_pick))) {
                    Monday.setChecked(true);
                    Tuesday.setChecked(true);
                    Wednesday.setChecked(true);
                    Thurday.setChecked(true);
                    Friday.setChecked(true);
                    Saturday.setChecked(true);
                    Sunday.setChecked(true);
                    pick_all.setText(R.string.cancel_all_pick);
                } else if (pick_all.getText().toString().equals(getString(R.string.cancel_all_pick))) {
                    Monday.setChecked(false);
                    Tuesday.setChecked(false);
                    Wednesday.setChecked(false);
                    Thurday.setChecked(false);
                    Friday.setChecked(false);
                    Saturday.setChecked(false);
                    Sunday.setChecked(false);
                    pick_all.setText(R.string.all_pick);
                }
            }
        });
    }

}
