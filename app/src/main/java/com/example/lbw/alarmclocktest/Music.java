package com.example.lbw.alarmclocktest;

/**
 * Created by lbw on 2017/6/27.
 */

public class Music {
    private String name;
    private String music;
    private int isOpen = 0;

    public Music(String name, String music, int isOpen) {
        this.name = name;
        this.music = music;
        this.isOpen = isOpen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
