package com.example.lbw.alarmclocktest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

//最开始没有闹钟的界面
public class MainActivity extends AppCompatActivity {
    private MyAlarmClockHelper helper;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.AddActivity(this);
        ActionBar actionBar = getSupportActionBar();
        helper = new MyAlarmClockHelper(this, "AlarmClock", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        int number = 0;
        @SuppressLint("Recycle") Cursor mCursor = db.rawQuery("select * from AlarmClock", null);
        number = mCursor.getCount();
        if (number != 0) {
            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
            startActivity(intent);
        }
        if (actionBar != null) {
            actionBar.hide();
        }
        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewAlarmClockActivity.class);//新建到新建窗口
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityCollector.finishAll();
        }
        return super.onKeyDown(keyCode, event);
    }
}
