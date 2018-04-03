package com.the95.tower_of_sorcerer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer OpeningThemeMusic;
    private int[]       game_settings;
    private boolean     bgm_on;
    private int         sb_val;
    private static final String TAG = "debuuuuuuuuuuuuuuuuuug";
    //Log.v(TAG, "x = " + me.getX() + " y = " + me.getY());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game_settings = new int[]{1,1,30,2};
        bgm_on = true;
        sb_val = 30;
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
        if (!bgm_on && game_settings[0] == 1) {
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
        if (game_settings[1] == 1)
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

                    final Dialog settings_dialog = new Dialog(MainActivity.this);
                    LayoutInflater settings_inflater = LayoutInflater.from(MainActivity.this);
                    View settings_view = settings_inflater.inflate(R.layout.settings_dialog, (ViewGroup)null);
                    settings_dialog.setCanceledOnTouchOutside(false);

                    final CheckBox cb_bgm = settings_view.findViewById(R.id.cb_bgm);
                    cb_bgm.setChecked(game_settings[0]==1);
                    final CheckBox cb_sfx = settings_view.findViewById(R.id.cb_sfx);
                    cb_sfx.setChecked(game_settings[1]==1);

                    Button btn_ok = settings_view.findViewById(R.id.btn_settings_ok);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sfx_play(R.raw.sfx_choose);
                            //settings_dialog.cancel();
                            if (cb_bgm.isChecked())
                                game_settings[0] = 1;
                            else
                                game_settings[0] = 0;
                            if (cb_sfx.isChecked())
                                game_settings[1] = 1;
                            else
                                game_settings[1] = 0;
                            game_settings[2] = sb_val;
                            if (game_settings[0] != 1 && bgm_on) {
                                bgm_on = false;
                                OpeningThemeMusic.release();
                            } else if (game_settings[0] == 1 && !bgm_on){
                                bgm_on = true;
                                OpeningThemeMusic = MediaPlayer.create(getApplicationContext(), R.raw.bgm_opening);
                                OpeningThemeMusic.setLooping(true);
                                OpeningThemeMusic.start();
                            }
                            settings_dialog.dismiss();
                        }
                    });

                    SeekBar sb_game_speed = settings_view.findViewById(R.id.sb_game_speed);
                    sb_game_speed.setProgress(game_settings[2]);
                    sb_game_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            sb_val = i;
                            //Log.v(TAG, "sb_val = " + String.valueOf(sb_val));
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                    settings_dialog.setContentView(settings_view);
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
