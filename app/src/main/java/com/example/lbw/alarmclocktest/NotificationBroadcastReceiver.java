package com.example.lbw.alarmclocktest;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lbw on 2017/7/11.
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        /*int type = intent.getIntExtra(TYPE, -1);
        if (type != -1) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(type);
        }*/

        if (action.equals("notification_clicked")) {
            stop();
            Intent intent1 = new Intent(context, Main2Activity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }

        if (action.equals("notification_cancelled")) {
            stop();
        }
    }

    private void stop() {
        if (OpenAlarmClockActivity.mMediaPlayer != null) {
            if (OpenAlarmClockActivity.mMediaPlayer.isPlaying()) {
                OpenAlarmClockActivity.mMediaPlayer.stop();
            }
        }

        if (OpenAlarmClockActivity.vibrator != null) {
            if (OpenAlarmClockActivity.vibrator.hasVibrator()) {
                OpenAlarmClockActivity.vibrator.cancel();
            }
        }
    }

}
