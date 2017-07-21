package com.example.lbw.alarmclocktest;

/**
 * Created by lbw on 2017/6/26.
 */

public class AlarmClock {
    private int id;
    private String time;
    private String title;
    private String week;
    private String weekText;
    private String music;
    private int isOpen;
    private int openCheckBox;
    private int isVibrator;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public int getOpenCheckBox() {
        return openCheckBox;
    }

    public void setOpenCheckBox(int openCheckBox) {
        this.openCheckBox = openCheckBox;
    }

    public String getWeektext() {
        return weekText;
    }

    public void setWeektext(String weektext) {
        this.weekText = weektext;
    }

    public int getIsVibrator() {
        return isVibrator;
    }

    public void setIsVibrator(int isVibrator) {
        this.isVibrator = isVibrator;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }
}

