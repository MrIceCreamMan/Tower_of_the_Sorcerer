package com.the95.tower_of_sorcerer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Settings_Back_Home_func();
    }

    private void Settings_Back_Home_func(){
        Button back_home = (Button)findViewById(R.id.back);
        back_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view) {
                MediaPlayer SelectMusic = MediaPlayer.create(getApplicationContext(), R.raw.choose);
                SelectMusic.start();
                finish();
            }
        });
    }

}
