package com.the95.tower_of_sorcerer;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer OpeningThemeMusic;
    private boolean[]   music_settings;
    private boolean     bgm_on;
    private static final String TAG = "debuuuuuuuuuuuuuuuuuug";
    //Log.v(TAG, "x = " + me.getX() + " y = " + me.getY());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        music_settings = new boolean[]{true, true};
        bgm_on = true;
        OpeningThemeMusic = MediaPlayer.create(getApplicationContext(), R.raw.bgm_opening);
        OpeningThemeMusic.setLooping(true);
        OpeningThemeMusic.start();

        findViewById(R.id.new_game).setOnClickListener(myListener);
        findViewById(R.id.load).setOnClickListener(myListener);
        findViewById(R.id.settings).setOnClickListener(myListener);
        findViewById(R.id.exit).setOnClickListener(myListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!bgm_on && music_settings[0]) {
            bgm_on = true;
            OpeningThemeMusic = MediaPlayer.create(getApplicationContext(), R.raw.bgm_opening);
            OpeningThemeMusic.setLooping(true);
            OpeningThemeMusic.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bgm_on) {
            bgm_on = false;
            OpeningThemeMusic.release();
        }
    }

    public View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final MediaPlayer SelectMusic = MediaPlayer.create(getApplicationContext(), R.raw.sfx_choose);
            switch (view.getId()) {
                case R.id.new_game:
                    if (OpeningThemeMusic != null) {
                        OpeningThemeMusic.release();
                        bgm_on = false;
                    }
                    if (music_settings[1])
                        SelectMusic.start();
                    Intent game = new Intent(MainActivity.this, Gamelogic.class);
                    game.putExtra("Music_Settings", music_settings);
                    startActivity(game);
                    break;

                case R.id.load:
                    if (OpeningThemeMusic != null) {
                        OpeningThemeMusic.release();
                        bgm_on = false;
                    }
                    if (music_settings[1])
                        SelectMusic.start();
                    Intent load_screen = new Intent(MainActivity.this, LoadActivity.class);
                    load_screen.putExtra("Music_Settings", music_settings);
                    startActivity(load_screen);
                    break;

                case R.id.settings:
                    if (music_settings[1])
                        SelectMusic.start();
                    Log.v(TAG, "x = " + String.valueOf(music_settings[0]) + " y = " + String.valueOf(music_settings[1]));
                    final String[] items = {"Enable background music", " Enable sound effects"};
                    final boolean[] temp_settings = music_settings;
                    AlertDialog.Builder music_builder = new AlertDialog.Builder(MainActivity.this);
                    music_builder.setTitle("Music Settings");
                    music_builder.setMultiChoiceItems(items, music_settings, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                            temp_settings[indexSelected] = isChecked;
                        }
                    });
                    music_builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            if (music_settings[1])
                                SelectMusic.start();
                            if (!music_settings[0] && bgm_on) {
                                bgm_on = false;
                                OpeningThemeMusic.release();
                            } else if (music_settings[0] && !bgm_on){
                                bgm_on = true;
                                OpeningThemeMusic = MediaPlayer.create(getApplicationContext(), R.raw.bgm_opening);
                                OpeningThemeMusic.setLooping(true);
                                OpeningThemeMusic.start();
                            }
                        }
                    });
                    AlertDialog music_dialog = music_builder.create();
                    music_dialog.show();
                    break;

                case R.id.exit:
                    if (OpeningThemeMusic != null)
                        OpeningThemeMusic.release();
                    if (music_settings[1])
                        SelectMusic.start();
                    finish();
                    break;
            }
        }
    };

}
