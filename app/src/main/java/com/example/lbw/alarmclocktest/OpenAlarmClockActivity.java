package com.example.lbw.alarmclocktest;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.Arrays;
import java.util.Calendar;

public class OpenAlarmClockActivity extends AppCompatActivity implements View.OnTouchListener {
    public static MediaPlayer mMediaPlayer;
    public static Vibrator vibrator;
    private MyAlarmClockHelper helper;
    private String musicName;
    private String continueTime;
    private int[] hours;
    private int[] minutes;
    private String[] musics;
    private int[] vibrators;
    private String[] continues;
    private int vib;
    private StringBuilder number = new StringBuilder();
    int num = 0;
    private String[] weekText;
    private String week;
    private int[] alarmId;
    private int id;
    private StringBuilder hour;
    private StringBuilder minute;
    private int year;
    private int mouth;
    private int day;
    //手指向右滑动时的最小速度
    private static final int XSPEED_MIN = 300;
    //手指向右滑动时的最小距离
    private static final int XDISTANCE_MIN = 200;
    //记录手指按下时的横坐标。
    private float xDown;
    //记录手指移动时的横坐标。
    private float xMove;
    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_open_alarm_clock);
        ActivityCollector.AddActivity(this);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        helper = new MyAlarmClockHelper(this, "AlarmClock", null, 1);
        getTime();
        boolean hasTime = getEarestTime();
        if (hasTime) {
            if (!(getString(R.string.null_music).equals(musicName))) {
                startMusic();
            }
        } else {
            startContinuesMusic();
        }
        if (vib == 1) {
            startVibrator();
        }
        closeWithoutRepetitionAlarm();
        if (num != 0) {
            showNotification();
        }
        Button sleep = (Button) findViewById(R.id.sleep);
        sleep.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                int continue_mintue = Integer.parseInt(continueTime);
                Intent intent = new Intent(OpenAlarmClockActivity.this, AlarmReceiver.class);
                PendingIntent pi = PendingIntent.getBroadcast(OpenAlarmClockActivity.this, 0, intent, 0);
                Calendar calendar = Calendar.getInstance();
                long time = System.currentTimeMillis();
                calendar.setTimeInMillis(time);
                calendar.add(Calendar.MINUTE, continue_mintue);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
                finish();
            }
        });
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.open_alarm_clock);
        relativeLayout.setOnTouchListener(this);
        TextView alarmTime = (TextView)findViewById(R.id.alarm_time);
        Calendar calendar = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();
        if (calendar.get(Calendar.HOUR_OF_DAY) < 10){
            sb.append("0");
        }
        sb.append(calendar.get(Calendar.HOUR_OF_DAY));
        sb.append(":");
        if (calendar.get(Calendar.MINUTE) < 10){
            sb.append("0");
        }
        sb.append(calendar.get(Calendar.MINUTE));
        if (sb != null) {
            alarmTime.setText(sb);
        }
    }

    private void startContinuesMusic() {
        mMediaPlayer = MediaPlayer.create(this, R.raw.alarm01);
        mMediaPlayer.setLooping(true);
        try {
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showNotification() {
        Intent intentClick = new Intent(this, NotificationBroadcastReceiver.class);
        intentClick.setAction("notification_clicked");
        PendingIntent pendingIntentClick = PendingIntent.getBroadcast(this, 0, intentClick, PendingIntent.FLAG_ONE_SHOT);

        Intent intentCancel = new Intent(this, NotificationBroadcastReceiver.class);
        intentCancel.setAction("notification_cancelled");
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this, 0, intentCancel, PendingIntent.FLAG_ONE_SHOT);

        if (getNextTime()) {
            if (getNumberOfAlarm() != 0) {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(getString(R.string.you_set) + num + getString(R.string.number_of_alarm))
                        .setContentText("下一个闹钟" + year + "-" + mouth + "-" + day + " " + hour + ":" + minute)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntentClick)
                        .setDeleteIntent(pendingIntentCancel);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, notificationBuilder.build());
            }else {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(getString(R.string.you_set) + num + getString(R.string.number_of_alarm))
                        .setContentText("没有下一个闹钟了")
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntentClick)
                        .setDeleteIntent(pendingIntentCancel);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, notificationBuilder.build());
            }
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle(getString(R.string.you_set) + num + getString(R.string.number_of_alarm))
                    .setContentText("没有下一个闹钟了")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntentClick)
                    .setDeleteIntent(pendingIntentCancel);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
        }
    }

    private int getNumberOfAlarm() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from AlarmClock where state= ? ", new String[]{"1"});
        return cursor.getCount();
    }

    public void startMusic() {
        if (musicName != null) {
            if (musicName.equals(getString(R.string.default_music))) {
                mMediaPlayer = MediaPlayer.create(this, R.raw.alarm01);
            } else if (musicName.equals(getString(R.string.shadow))) {
                mMediaPlayer = MediaPlayer.create(this, R.raw.alarm02);
            } else if (musicName.equals(getString(R.string.classics))) {
                mMediaPlayer = MediaPlayer.create(this, R.raw.alarm03);
            } else if (musicName.equals(getString(R.string.metal))) {
                mMediaPlayer = MediaPlayer.create(this, R.raw.alarm04);
            } else if (musicName.equals(getString(R.string.inspirit))) {
                mMediaPlayer = MediaPlayer.create(this, R.raw.alarm05);
            } else if (musicName.equals(getString(R.string.lively))) {
                mMediaPlayer = MediaPlayer.create(this, R.raw.alarm06);
            } else if (musicName.equals(getString(R.string.heart))) {
                mMediaPlayer = MediaPlayer.create(this, R.raw.alarm07);
            } else if (musicName.equals(getString(R.string.happyness))) {
                mMediaPlayer = MediaPlayer.create(this, R.raw.alarm08);
            } else if (musicName.equals(getString(R.string.blink))) {
                mMediaPlayer = MediaPlayer.create(this, R.raw.alarm09);
            } else if (musicName.equals(getString(R.string.opening))) {
                mMediaPlayer = MediaPlayer.create(this, R.raw.alarm10);
            }
            mMediaPlayer.setLooping(true);
            try {
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mMediaPlayer.start();
                    }
                });
                mMediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void cancelVibrator() {
        vibrator.cancel();
    }

    public void cancelMusic() {
        mMediaPlayer.stop();
    }

    public void startVibrator() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {500, 1000, 500, 1000};
        vibrator.vibrate(pattern, 0);
    }

    public void getTime() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from AlarmClock where state= ? ", new String[]{"1"});
        num = cursor.getCount();
        int i = 0;
        alarmId = new int[num];
        hours = new int[num];
        minutes = new int[num];
        musics = new String[num];
        vibrators = new int[num];
        continues = new String[num];
        weekText = new String[num];
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int time_hour = cursor.getInt(cursor.getColumnIndex("hour"));
                int time_mintue = cursor.getInt(cursor.getColumnIndex("minute"));
                String musicName = cursor.getString(cursor.getColumnIndex("music"));
                String continuetime = cursor.getString(cursor.getColumnIndex("continue"));
                int vibrator = cursor.getInt(cursor.getColumnIndex("vibrator"));
                String dbWeektext = cursor.getString(cursor.getColumnIndex("weektext"));
                alarmId[i] = id;
                hours[i] = time_hour;
                minutes[i] = time_mintue;
                musics[i] = musicName;
                vibrators[i] = vibrator;
                continues[i] = continuetime;
                weekText[i] = dbWeektext;
                i++;
                number.append(1);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public boolean getEarestTime() {
        long time = System.currentTimeMillis();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(time);
        @SuppressLint("WrongConstant") int hour = calendar1.get(Calendar.HOUR_OF_DAY);
        @SuppressLint("WrongConstant") int mintue = calendar1.get(Calendar.MINUTE);
        if (hours != null && minutes != null) {
            for (int i = 0; i < hours.length; i++) {
                if (hours[i] == hour && minutes[i] > mintue - 1) {
                    continueTime = continues[i];
                    musicName = musics[i];
                    vib = vibrators[i];
                    week = weekText[i];
                    id = alarmId[i];
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                int distanceX = (int) (xMove - xDown);
                int xSpeed = getScrollVelocity();
                if (distanceX > XDISTANCE_MIN && xSpeed > XSPEED_MIN) {
                    Intent intent = new Intent(OpenAlarmClockActivity.this,Main2Activity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.move_left_in, R.anim.move_right_out);
                    finish();
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return true;
    }

    private void closeWithoutRepetitionAlarm() {
        if (week != null) {
            if (getString(R.string.without_repetition).equals(week)) {
                SQLiteDatabase db = helper.getReadableDatabase();
                ContentValues values = new ContentValues();
                values.put("state", 0);
                db.update("AlarmClock", values, "id = ?", new String[]{String.valueOf(id)});
            }
        }
    }

    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    public int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1, (float) 0.01);
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    @SuppressLint("WrongConstant")
    public boolean getNextTime() {
        long nowTime = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        long[] times = new long[hours.length];
        for (int i = 0; i < hours.length; i++) {
            c.set(Calendar.HOUR_OF_DAY, hours[i]);
            c.set(Calendar.MINUTE, minutes[i]);
            times[i] = c.getTimeInMillis();
        }
        Arrays.sort(times);
        if (times[times.length - 1] > nowTime) {
            for (int j = 0; j < times.length; j++) {
                if (times[j] > nowTime) {
                    calendar.setTimeInMillis(times[j]);
                    calendarTo(calendar);
                    return true;
                }
            }
        }
        for (int j = 0; j < times.length; j++) {
            if (times[j] > (nowTime - 1000 * 60 * 60 * 24)) {
                calendar.setTimeInMillis(times[j]);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                calendarTo(calendar);
                return true;
            }
        }
        return false;
    }

    @SuppressLint("WrongConstant")
    private boolean calendarTo(Calendar calendar) {
        year = calendar.get(Calendar.YEAR);
        mouth = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        int nextHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nextMinute = calendar.get(Calendar.MINUTE);
        if (nextMinute < 10){
            minute = new StringBuilder("0");
            minute.append(nextMinute);
        }else {
            minute = new StringBuilder(String.valueOf(nextMinute));
        }
        if (nextHour < 10){
            hour = new StringBuilder("0");
            hour.append(nextHour);
        }else {
            hour = new StringBuilder(String.valueOf(nextHour));
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelMusic();
        if (vib == 1) {
            cancelVibrator();
        }
    }


}
