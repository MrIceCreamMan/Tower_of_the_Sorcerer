package com.the95.tower_of_sorcerer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.FileOutputStream;

public class SaveActivity extends AppCompatActivity {
    private String           game_data;
    private byte[][][]       floors;
    private FileOutputStream fos;
    private String           FILENAME = "Internal_String";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            game_data = extras.getString("Game_Data");
        }

        findViewById(R.id.save_1).setOnClickListener(Save_Game);
        findViewById(R.id.save_2).setOnClickListener(Save_Game);
        findViewById(R.id.save_3).setOnClickListener(Save_Game);
        findViewById(R.id.save_4).setOnClickListener(Save_Game);

        load_data_from_save_files();
    }

    private void load_data_from_save_files() {

    }

    public View.OnClickListener Save_Game = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MediaPlayer SelectMusic = MediaPlayer.create(getApplicationContext(), R.raw.choose);
            switch (view.getId()) {
                case R.id.save_1:
                    SelectMusic.start();
                    finish();
                    break;

                case R.id.save_2:
                    SelectMusic.start();
                    finish();
                    break;

                case R.id.save_3:
                    SelectMusic.start();
                    finish();
                    break;

                case R.id.save_4:
                    SelectMusic.start();
                    finish();
                    break;
            }
        }
    };
}
