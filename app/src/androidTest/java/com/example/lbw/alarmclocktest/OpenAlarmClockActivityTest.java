package com.example.lbw.alarmclocktest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by lbw on 2017/7/5.
 */
@RunWith(AndroidJUnit4.class)
public class OpenAlarmClockActivityTest {

    @Rule
    public ActivityTestRule<OpenAlarmClockActivity> activityRule = new ActivityTestRule<>(OpenAlarmClockActivity.class);

    @Test
    public void showNotification() throws Exception {
        activityRule.getActivity().showNotification();
    }

}