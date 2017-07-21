package com.example.lbw.alarmclocktest;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private List<Music> musicList = new ArrayList<>();
    private MusicAdapter adapter;
    private int lastSelectIndex = 2;
    private String musicNane;
    private int resultCode = 1;
    private String vibration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ActivityCollector.AddActivity(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initMusic();
        adapter = new MusicAdapter(MusicActivity.this, R.layout.music_list, musicList);
        ListView listView = (ListView) findViewById(R.id.music_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        Button backTo = (Button) findViewById(R.id.back_to);
        backTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("back", getString(R.string.default_music));
                MusicActivity.this.setResult(resultCode, intent);
                finish();
            }
        });
        Button saveMusic = (Button) findViewById(R.id.save_sound);
        saveMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicList.get(0).getIsOpen() == 1) {
                    vibration = "1";
                } else {
                    vibration = "0";
                }
                List<Music> music_List = new ArrayList<>();
                musicList.remove(0);
                for (Music m : musicList) {
                    if (m.getIsOpen() == 1) {
                        musicNane = m.getName();
                    }
                }
                int resultCode = 2;
                Intent intent = new Intent();
                intent.putExtra("Music", musicNane);
                intent.putExtra("vibration", vibration);
                MusicActivity.this.setResult(resultCode, intent);
                finish();
            }
        });
    }

    private void initMusic() {
        Music vibration = new Music(getString(R.string.vibrate), "", 0);
        musicList.add(vibration);
        Music music0 = new Music(getString(R.string.null_music), "empty", 0);
        musicList.add(music0);
        Music music1 = new Music(getString(R.string.default_music), "Alarm01.ogg", 1);
        musicList.add(music1);
        Music music2 = new Music(getString(R.string.shadow), "Alarm02.ogg", 0);
        musicList.add(music2);
        Music music3 = new Music(getString(R.string.classics), "Alarm03.ogg", 0);
        musicList.add(music3);
        Music music4 = new Music(getString(R.string.metal), "Alarm04.ogg", 0);
        musicList.add(music4);
        Music music5 = new Music(getString(R.string.inspirit), "Alarm05.ogg", 0);
        musicList.add(music5);
        Music music6 = new Music(getString(R.string.lively), "Alarm06.ogg", 0);
        musicList.add(music6);
        Music music7 = new Music(getString(R.string.heart), "Alarm07.ogg", 0);
        musicList.add(music7);
        Music music8 = new Music(getString(R.string.happyness), "Alarm08.ogg", 0);
        musicList.add(music8);
        Music music9 = new Music(getString(R.string.blink), "Alarm09.ogg", 0);
        musicList.add(music9);
        Music music10 = new Music(getString(R.string.opening), "Alarm10.ogg", 0);
        musicList.add(music10);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            Music music = musicList.get(0);
            music.setIsOpen(music.getIsOpen() == 0 ? 1 : 0);
        } else if (position != lastSelectIndex) {
            musicList.get(position).setIsOpen(1);
            musicList.get(lastSelectIndex).setIsOpen(0);
            lastSelectIndex = position;
        }
        adapter.notifyDataSetChanged();
    }
}
