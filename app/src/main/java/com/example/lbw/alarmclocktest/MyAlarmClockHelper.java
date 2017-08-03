package com.example.lbw.alarmclocktest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by lbw on 2017/6/25.
 */

public class MyAlarmClockHelper extends SQLiteOpenHelper {

    public static final String ALARM_CLOCK = "create table AlarmClock ("
            //id
            + "id integer primary key autoincrement,"
            //小时
            + "hour integer,"
            //分钟
            + "minute integer,"
            //重复的天数
            + "week text,"
            //小睡时长
            + "continue text,"
            //铃声
            + "music text,"
            //开关
            + "state integer,"
            //标签
            + "title text,"
            //重复的文字描述
            + "weektext text,"
            //是否有振动
            + "vibrator integer,"
            //是否删除
            + "openCheckBox integer)";
    private Context mContent;

    public MyAlarmClockHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContent = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ALARM_CLOCK);
        //Toast.makeText(mContent, "create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

