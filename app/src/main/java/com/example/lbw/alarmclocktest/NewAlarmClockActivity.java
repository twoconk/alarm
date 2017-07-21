package com.example.lbw.alarmclocktest;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Calendar;

//新建闹钟时的界面
public class NewAlarmClockActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener, NumberPicker.OnScrollListener, NumberPicker.Formatter, SleepTime.OnLoginInforCompleted, LabelFragment.getData {
    private MyAlarmClockHelper helper;
    private Cursor mCursor;
    private SleepTime sleepTime;
    private LabelFragment labels;
    private String weekNumber = "0000000";
    private String vibration;
    private int request0 = 0;
    private int request1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm_clock);
        ActivityCollector.AddActivity(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        helper = new MyAlarmClockHelper(this, "AlarmClock", null, 1);
        init();
        Button cancal = (Button) findViewById(R.id.cancal);
        cancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getWritableDatabase();
                int number = 0;
                @SuppressLint("Recycle") Cursor mCursor = db.rawQuery("select * from AlarmClock", null);
                number = mCursor.getCount();
                if (number == 0) {
                    finish();
                } else {
                    Intent intent = new Intent(NewAlarmClockActivity.this, Main2Activity.class);
                    startActivity(intent);
                }
            }
        });
        LinearLayout label = (LinearLayout)findViewById(R.id.label);
        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labels.show(getFragmentManager(), "LabelFragment");
            }
        });
        LinearLayout repetition = (LinearLayout)findViewById(R.id.repetition);
        repetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewAlarmClockActivity.this, RepetitionActivity.class);
                startActivityForResult(intent, request0);
            }
        });
        LinearLayout sound = (LinearLayout)findViewById(R.id.sound);
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewAlarmClockActivity.this, MusicActivity.class);
                startActivityForResult(intent, request1);
            }
        });
        LinearLayout time = (LinearLayout)findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sleepTime.show(getFragmentManager(), "sleepTime");
            }
        });
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                NumberPicker hours = (NumberPicker) findViewById(R.id.hour);
                NumberPicker minutes = (NumberPicker) findViewById(R.id.minute);
                int hour = hours.getValue();
                int minute = minutes.getValue();
                TextView title_text = (TextView) findViewById(R.id.label_text);
                String title = title_text.getText().toString();
                TextView week_text = (TextView) findViewById(R.id.week_text);
                String week = week_text.getText().toString();
                TextView music_text = (TextView) findViewById(R.id.music_text);
                String music = music_text.getText().toString();
                TextView continue_text = (TextView) findViewById(R.id.continue_text);
                String continues = continue_text.getText().toString();
                SQLiteDatabase db = helper.getWritableDatabase();
                values.put("hour", hour);
                values.put("minute", minute);
                values.put("week", weekNumber);
                values.put("continue", continues);
                values.put("music", music);
                values.put("state", 1);
                values.put("title", title);
                values.put("weektext", week);
                values.put("vibrator", vibration);
                values.put("openCheckBox", 0);
                db.insert("AlarmClock", null, values);
                Intent intent = new Intent(NewAlarmClockActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });
        sleepTime = new SleepTime();
        sleepTime.setOnLoginInforCompleted(this);
        labels = new LabelFragment();
        labels.setData(this);
    }

    @SuppressLint("WrongConstant")
    private void init() {
        long time = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int mintue = calendar.get(Calendar.MINUTE);

        NumberPicker hours = (NumberPicker) findViewById(R.id.hour);
        NumberPicker minutes = (NumberPicker) findViewById(R.id.minute);

        hours.setFormatter(this);
        hours.setOnValueChangedListener(this);
        hours.setOnScrollListener(this);
        hours.setMaxValue(23);
        hours.setMinValue(0);
        hours.setValue(hour);

        minutes.setFormatter(this);
        minutes.setOnValueChangedListener(this);
        minutes.setOnScrollListener(this);
        minutes.setMaxValue(59);
        minutes.setMinValue(0);
        minutes.setValue(mintue);
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
    public void inputLoginInforCompleted(String hour) {
        TextView continue_text = (TextView) findViewById(R.id.continue_text);
        continue_text.setText(hour);
    }

    @Override
    public void inputData(String label) {
        TextView data = (TextView) findViewById(R.id.label_text);
        data.setText(label);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == request0) {
                String week = data.getStringExtra("week_data");
                weekNumber = data.getStringExtra("WEEK");
                TextView week_text = (TextView) findViewById(R.id.week_text);
                week_text.setText(week);
            } else if (requestCode == request1) {
                String back = data.getStringExtra("back");
                String music = data.getStringExtra("Music");
                TextView music_text = (TextView) findViewById(R.id.music_text);
                if (resultCode == 1) {
                    music_text.setText(back);
                } else if (resultCode == 2) {
                    music_text.setText(music);
                }
                vibration = data.getStringExtra("vibration");
            }
        }
    }
}
