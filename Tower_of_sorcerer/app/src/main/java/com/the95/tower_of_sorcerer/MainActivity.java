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
    private int[]       game_settings;
    private boolean[]   music_settings;
    private boolean     bgm_on;
    private static final String TAG = "debuuuuuuuuuuuuuuuuuug";
    //Log.v(TAG, "x = " + me.getX() + " y = " + me.getY());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game_settings = new int[]{1,1,10,2};
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

    private void sfx_play(int which_sfx) {
        MediaPlayer sfx_music = MediaPlayer.create(getApplicationContext(), which_sfx);
        if (music_settings[1])
            sfx_music.start();
        sfx_music.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
    }

    public View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.new_game:
                    sfx_play(R.raw.sfx_choose);
                    String[] item_list = {getString(R.string.easy), getString(R.string.medium), getString(R.string.hard)};
                    AlertDialog.Builder new_game_builder = new AlertDialog.Builder(MainActivity.this);
                    new_game_builder.setTitle(R.string.difficulty_select);
                    new_game_builder.setItems(item_list, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            if (item == 0)
                                game_settings[3] = 1;
                            else if (item == 1)
                                game_settings[3] = 2;
                            else if (item == 2)
                                game_settings[3] = 3;
                            else
                                game_settings[3] = 1;
                            if (OpeningThemeMusic != null) {
                                OpeningThemeMusic.release();
                                bgm_on = false;
                            }
                            Intent game = new Intent(MainActivity.this, Gamelogic.class);
                            game.putExtra("Game_Settings", game_settings);
                            startActivity(game);
                        }
                    });
                    AlertDialog new_game_dialog = new_game_builder.create();
                    new_game_dialog.setCanceledOnTouchOutside(true);
                    new_game_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            sfx_play(R.raw.sfx_cancel);
                        }
                    });
                    new_game_dialog.show();
                    break;

                case R.id.load:
                    if (OpeningThemeMusic != null) {
                        OpeningThemeMusic.release();
                        bgm_on = false;
                    }
                    sfx_play(R.raw.sfx_choose);
                    Intent load_screen = new Intent(MainActivity.this, LoadActivity.class);
                    load_screen.putExtra("Game_Settings", game_settings);
                    startActivity(load_screen);
                    break;

                case R.id.settings:
                    sfx_play(R.raw.sfx_choose);
                    final String[] items = {"Enable background music", " Enable sound effects"};
                    AlertDialog.Builder settings_builder = new AlertDialog.Builder(MainActivity.this);
                    settings_builder.setTitle("Game_Settings");
                    settings_builder.setMultiChoiceItems(items, music_settings, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        }
                    });
                    settings_builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            sfx_play(R.raw.sfx_choose);
                            if (music_settings[0])
                                game_settings[0] = 1;
                            else
                                game_settings[0] = 0;
                            if (music_settings[1])
                                game_settings[1] = 1;
                            else
                                game_settings[1] = 0;
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
                    AlertDialog settings_dialog = settings_builder.create();
                    settings_dialog.show();
                    break;

                case R.id.exit:
                    if (OpeningThemeMusic != null)
                        OpeningThemeMusic.release();
                    sfx_play(R.raw.sfx_choose);
                    finish();
                    break;
            }
        }
    };

}
