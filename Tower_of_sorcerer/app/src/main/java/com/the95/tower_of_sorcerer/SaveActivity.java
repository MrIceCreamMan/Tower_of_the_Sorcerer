package com.the95.tower_of_sorcerer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveActivity extends AppCompatActivity {
    final String    FILENAME1 = "save1.txt";
    final String    FILENAME2 = "save2.txt";
    final String    FILENAME3 = "save3.txt";
    final String    FILENAME4 = "save4.txt";
    private String  game_data;

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
        String collected = null;
        FileInputStream fis = null;
        try {
            fis = openFileInput(FILENAME1);
            byte[] saved_data = new byte[fis.available()];
            while (fis.read(saved_data) != -1) {
                collected = new String(saved_data);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public View.OnClickListener Save_Game = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            FileOutputStream fos;

            MediaPlayer SelectMusic = MediaPlayer.create(getApplicationContext(), R.raw.choose);
            switch (view.getId()) {
                case R.id.save_1:
                    SelectMusic.start();
                    /*
                    File f = new File(FILENAME);
                    try {
                        fos = new FileOutputStream(f);
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }//*/
                    try {
                        fos = openFileOutput(FILENAME1, Context.MODE_PRIVATE);
                        fos.write(game_data.getBytes());
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                    break;

                case R.id.save_2:
                    SelectMusic.start();
                    try {
                        fos = openFileOutput(FILENAME2, Context.MODE_PRIVATE);
                        fos.write(game_data.getBytes());
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                    break;

                case R.id.save_3:
                    SelectMusic.start();
                    try {
                        fos = openFileOutput(FILENAME3, Context.MODE_PRIVATE);
                        fos.write(game_data.getBytes());
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                    break;

                case R.id.save_4:
                    SelectMusic.start();
                    try {
                        fos = openFileOutput(FILENAME4, Context.MODE_PRIVATE);
                        fos.write(game_data.getBytes());
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                    break;
            }
        }
    };
}
