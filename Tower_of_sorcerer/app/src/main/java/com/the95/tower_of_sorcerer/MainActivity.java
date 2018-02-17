package com.the95.tower_of_sorcerer;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    MediaPlayer OpeningThemeMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OpeningThemeMusic = MediaPlayer.create(getApplicationContext(), R.raw.opening);
        OpeningThemeMusic.start();

        findViewById(R.id.new_game).setOnClickListener(myListener);
        findViewById(R.id.load).setOnClickListener(myListener);
        findViewById(R.id.settings).setOnClickListener(myListener);
        findViewById(R.id.exit).setOnClickListener(myListener);
    }

    public View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MediaPlayer SelectMusic = MediaPlayer.create(getApplicationContext(), R.raw.choose);
            switch (view.getId()) {
                case R.id.new_game:
                    if (OpeningThemeMusic != null)
                        OpeningThemeMusic.release();
                    SelectMusic.start();
                    String[] level = {"easy", "medium", "hard"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle(getString(R.string.levelSelect));
                    builder.setItems(level, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            Intent game = new Intent(MainActivity.this, Gamelogic.class);
                            startActivity(game);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    break;

                case R.id.load:
                    if (OpeningThemeMusic != null)
                        OpeningThemeMusic.release();
                    SelectMusic.start();
                    Intent load_screen = new Intent(MainActivity.this, LoadActivity.class);
                    startActivity(load_screen);
                    break;

                case R.id.settings:
                    if (OpeningThemeMusic != null)
                        OpeningThemeMusic.release();
                    SelectMusic.start();
                    Intent settings_screen = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(settings_screen);
                    break;

                case R.id.exit:
                    if (OpeningThemeMusic != null)
                        OpeningThemeMusic.release();
                    SelectMusic.start();
                    finish();
                    break;
            }
        }
    };

}
