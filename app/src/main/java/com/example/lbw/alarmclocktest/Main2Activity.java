package com.example.lbw.alarmclocktest;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//有闹钟是的界面
public class Main2Activity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public List<AlarmClock> alarmClockList = new ArrayList<>();
    private Cursor mCursor;
    private MyAlarmClockHelper helper;
    private AlarmClockAdapter adapter;
    private boolean isEdit;
    private boolean isAllPick;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private String week;
    private String weekText;
    private int[] hours;
    private int[] minutes;
    private int timeHour;
    private int timeMintue;
    private String[] weekTextArray;
    private String[] weekArray;
    private StringBuilder sb = new StringBuilder();
    private int[] alarmIdArray;
    private int alarmId;

    @SuppressLint("WrongConstant")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ActivityCollector.AddActivity(this);
        ActionBar actionBar = getSupportActionBar();
        helper = new MyAlarmClockHelper(this, "AlarmClock", null, 1);
        if (actionBar != null) {
            actionBar.hide();
        }
        initAlarmClock();
        adapter = new AlarmClockAdapter(Main2Activity.this, R.layout.alarm_list, alarmClockList);
        ListView listView = (ListView) findViewById(R.id.alarm_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        final Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isAllPick = !isAllPick;
                if (isEdit) {
                    adapter.toggleAllPickStatus();
                    if (isAllPick) {
                        add.setText(R.string.cancel_all_pick);
                        for (int i = 0; i < alarmClockList.size(); i++) {
                            AlarmClock alarmClock = alarmClockList.get(i);
                            alarmClock.setOpenCheckBox(1);
                        }
                    } else {
                        add.setText(R.string.all_pick);
                        for (int i = 0; i < alarmClockList.size(); i++) {
                            AlarmClock alarmClock = alarmClockList.get(i);
                            alarmClock.setOpenCheckBox(0);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Intent intent = new Intent(Main2Activity.this, NewAlarmClockActivity.class);
                    startActivity(intent);
                }
            }
        });
        final Button compile = (Button) findViewById(R.id.compile);
        compile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdit = !isEdit;
                if (isEdit) {
                    compile.setText(R.string.back_to);
                    TextView title_text = (TextView) findViewById(R.id.title_text);
                    title_text.setText(R.string.edit_alarm);
                    add.setText(R.string.all_pick);
                    Button delete = (Button) findViewById(R.id.delete);
                    delete.setVisibility(View.VISIBLE);
                } else {
                    compile.setText(R.string.edit);
                    TextView title_text = (TextView) findViewById(R.id.title_text);
                    title_text.setText(R.string.alarm);
                    add.setText(R.string.new_alarm);
                    Button delete = (Button) findViewById(R.id.delete);
                    delete.setVisibility(View.GONE);
                }
                adapter.toggleEditStatus();
            }
        });
        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String[] selectClock = findSelectedClock();
                if (selectClock == null) {
                    return;
                }
                SQLiteDatabase db = helper.getReadableDatabase();
                for (String s : selectClock) {
                    db.delete("AlarmClock", " id = ?", new String[]{s});
                }
                isEdit = !isEdit;
                if (isEdit) {
                    compile.setText(R.string.back_to);
                    TextView title_text = (TextView) findViewById(R.id.title_text);
                    title_text.setText(R.string.edit_alarm);
                    add.setText(R.string.all_pick);
                    Button delete = (Button) findViewById(R.id.delete);
                    delete.setVisibility(View.VISIBLE);
                } else {
                    compile.setText(R.string.edit);
                    TextView title_text = (TextView) findViewById(R.id.title_text);
                    title_text.setText(R.string.alarm);
                    add.setText(R.string.new_alarm);
                    Button delete = (Button) findViewById(R.id.delete);
                    delete.setVisibility(View.GONE);
                }
                if (selectClock.length != 0) {
                    Toast.makeText(Main2Activity.this, R.string.deleted_success, 2000).show();
                }
                adapter.toggleEditStatus();
                if (alarmClockList.isEmpty()) {
                    Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        getTime();
        if (hours != null && minutes != null) {
            for (int i = 0; i < sb.length(); i++) {
                timeHour = hours[i];
                timeMintue = minutes[i];
                week = weekArray[i];
                weekText = weekTextArray[i];
                alarmId = alarmIdArray[i];
                if (weekText != null) {
                    if (getString(R.string.without_repetition).equals(weekText)) {
                        calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY, timeHour);
                        calendar.set(Calendar.MINUTE, timeMintue);
                        calendar.set(Calendar.SECOND, 0);
                        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                        }
                        openAlarm(alarmId);
                    } else if (!(getString(R.string.without_repetition).equals(weekText))) {
                        if (week != null) {
                            int[] a = new int[week.length()];
                            for (int j = 0; j < week.length(); j++) {
                                a[j] = Integer.parseInt(String.valueOf(week.charAt(j)));
                            }
                            Calendar c = Calendar.getInstance();
                            c.setTimeInMillis(System.currentTimeMillis());
                            int index = c.get(Calendar.DAY_OF_WEEK);
                            if (a[index - 1] == 1) {
                                calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(System.currentTimeMillis());
                                calendar.set(Calendar.HOUR_OF_DAY, timeHour);
                                calendar.set(Calendar.MINUTE, timeMintue);
                                if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                                }
                                openAlarm(alarmId);
                            }
                        }
                    }
                }
            }
        }
    }

    private String[] findSelectedClock() {
        if (alarmClockList == null || alarmClockList.isEmpty()) {
            return null;
        }
        List<String> result = new ArrayList<>();
        for (int i = 0; i < alarmClockList.size(); i++) {
            AlarmClock clock = alarmClockList.get(i);
            if (clock.getOpenCheckBox() == 1) {
                result.add(String.valueOf(clock.getId()));
                alarmClockList.remove(i);
                i--;
            }
        }
        String[] strings = new String[result.size()];
        return result.toArray(strings);
    }

    private void initAlarmClock() {
        SQLiteDatabase db = helper.getWritableDatabase();
        mCursor = db.query("AlarmClock", null, null, null, null, null, null);
        if (mCursor.moveToFirst()) {
            do {
                int id = mCursor.getInt(mCursor.getColumnIndex("id"));
                int hour = mCursor.getInt(mCursor.getColumnIndex("hour"));
                int minute = mCursor.getInt(mCursor.getColumnIndex("minute"));
                String title = mCursor.getString(mCursor.getColumnIndex("title"));
                String weekText = mCursor.getString(mCursor.getColumnIndex("weektext"));
                int state = mCursor.getInt(mCursor.getColumnIndex("state"));
                int vibrator = mCursor.getInt(mCursor.getColumnIndex("vibrator"));
                String musicName = mCursor.getString(mCursor.getColumnIndex("music"));
                int openCheckBox = mCursor.getInt(mCursor.getColumnIndex("openCheckBox"));
                AlarmClock alarmClock1 = new AlarmClock();
                alarmClock1.setId(id);
                alarmClock1.setTime(getString(R.string.time, hour, minute));
                alarmClock1.setTitle(title);
                alarmClock1.setIsOpen(state);
                alarmClock1.setOpenCheckBox(openCheckBox);
                alarmClock1.setIsVibrator(vibrator);
                alarmClock1.setMusic(musicName);
                alarmClock1.setWeektext(weekText);
                alarmClockList.add(alarmClock1);
            } while (mCursor.moveToNext());
        }
        mCursor.close();
    }


    @SuppressLint("WrongConstant")
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isEdit) {
            AlarmClock alarmClock = alarmClockList.get(position);
            int openCheckBox = alarmClock.getOpenCheckBox();
            alarmClock.setOpenCheckBox(openCheckBox == 0 ? 1 : 0);
            adapter.notifyDataSetChanged();
        } else {
            AlarmClock alarmClock = alarmClockList.get(position);
            SQLiteDatabase db = helper.getReadableDatabase();
            ContentValues values = new ContentValues();
            int isOpen = alarmClock.getIsOpen();
            if (isOpen == 1) {
                alarmClock.setIsOpen(0);
                values.put("state", 0);
                db.update("AlarmClock", values, "id = ?", new String[]{String.valueOf(alarmClock.getId())});
                cancelTheAlarm(alarmClock.getId());
            } else if (isOpen == 0) {
                alarmClock.setIsOpen(1);
                values.put("state", 1);
                db.update("AlarmClock", values, "id = ?", new String[]{String.valueOf(alarmClock.getId())});
                String time = alarmClock.getTime().toString();
                String[] timeArray = time.split(":");
                int hour = Integer.parseInt(timeArray[0]);
                int minute = Integer.parseInt(timeArray[1]);
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                openAlarm(alarmClock.getId());
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void openAlarm(int id) {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, id, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
    }

    private void cancelTheAlarm(int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, id, intent, 0);
        alarmManager.cancel(pi);
    }

    public void getTime() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from AlarmClock where state = ?", new String[]{"1"});
        int number = cursor.getCount();
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int timeHour = cursor.getInt(cursor.getColumnIndex("hour"));
                int timeMintue = cursor.getInt(cursor.getColumnIndex("minute"));
                String db_week = cursor.getString(cursor.getColumnIndex("week"));
                String db_weektext = cursor.getString(cursor.getColumnIndex("weektext"));
                alarmIdArray = new int[number];
                hours = new int[number];
                minutes = new int[number];
                weekTextArray = new String[number];
                weekArray = new String[number];
                sb.append(1);
                alarmIdArray[i] = id;
                hours[i] = timeHour;
                minutes[i] = timeMintue;
                weekTextArray[i] = db_weektext;
                weekArray[i] = db_week;
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityCollector.finishAll();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        alarmClockList.clear();
        initAlarmClock();
        adapter.notifyDataSetChanged();
    }
}
