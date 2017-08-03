package com.example.lbw.alarmclocktest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lbw on 2017/6/28.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intenter = new Intent(context, OpenAlarmClockActivity.class);
        intenter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intenter);
    }
}
