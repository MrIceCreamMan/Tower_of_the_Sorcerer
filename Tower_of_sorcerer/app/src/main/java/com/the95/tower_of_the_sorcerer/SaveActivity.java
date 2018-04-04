package com.the95.tower_of_the_sorcerer;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveActivity extends AppCompatActivity {
    final String    FILENAME1 = "save1.txt";
    final String    FILENAME2 = "save2.txt";
    final String    FILENAME3 = "save3.txt";
    final String    FILENAME4 = "save4.txt";
    private byte[]  game_data;
    private int[]   game_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            game_data = extras.getByteArray("Game_Data");
            game_settings = extras.getIntArray("Game_Settings");
        }

        findViewById(R.id.save_1).setOnClickListener(Save_Game);
        findViewById(R.id.save_2).setOnClickListener(Save_Game);
        findViewById(R.id.save_3).setOnClickListener(Save_Game);
        findViewById(R.id.save_4).setOnClickListener(Save_Game);
        findViewById(R.id.cancel).setOnClickListener(Save_Game);

        load_data_from_save_files(1);
        load_data_from_save_files(2);
        load_data_from_save_files(3);
        load_data_from_save_files(4);
    }

    private void load_data_from_save_files(int save_idx) {
        TextView[] tw_list = new TextView[5];
        switch (save_idx) {
            case 1:
                tw_list[0] = findViewById(R.id.health_val_1);
                tw_list[1] = findViewById(R.id.attack_val_1);
                tw_list[2] = findViewById(R.id.defence_val_1);
                tw_list[3] = findViewById(R.id.gold_val_1);
                tw_list[4] = findViewById(R.id.floor_num_1);
                break;
            case 2:
                tw_list[0] = findViewById(R.id.health_val_2);
                tw_list[1] = findViewById(R.id.attack_val_2);
                tw_list[2] = findViewById(R.id.defence_val_2);
                tw_list[3] = findViewById(R.id.gold_val_2);
                tw_list[4] = findViewById(R.id.floor_num_2);
                break;
            case 3:
                tw_list[0] = findViewById(R.id.health_val_3);
                tw_list[1] = findViewById(R.id.attack_val_3);
                tw_list[2] = findViewById(R.id.defence_val_3);
                tw_list[3] = findViewById(R.id.gold_val_3);
                tw_list[4] = findViewById(R.id.floor_num_3);
                break;
            case 4:
                tw_list[0] = findViewById(R.id.health_val_4);
                tw_list[1] = findViewById(R.id.attack_val_4);
                tw_list[2] = findViewById(R.id.defence_val_4);
                tw_list[3] = findViewById(R.id.gold_val_4);
                tw_list[4] = findViewById(R.id.floor_num_4);
                break;
            default:
                break;
        }
        FileInputStream fis = null;
        try {
            fis = openFileInput("save"+String.valueOf(save_idx)+".txt");
            //byte[] saved_data = new byte[fis.available()];
            byte[] saved_data = new byte[25];
            int field_count = 0;
            int total_bytes_read = fis.read(saved_data, 0, 25);
            int i = 0;
            StringBuilder sb = new StringBuilder();
            while(field_count < 5 && i < 25) {
                //Log.v(TAG, "i = " + String.valueOf(i) + " ,byte value = " + String.valueOf(saved_data[i]));
                if (saved_data[i] != 10)
                    sb.append((char) saved_data[i]);
                else {
                    if (field_count == 4)
                        sb.append(" F");
                    tw_list[field_count].setText(sb.toString());
                    sb.delete(0,sb.length());
                    field_count++;
                }
                i++;
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

    public View.OnClickListener Save_Game = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FileOutputStream fos;
            switch (view.getId()) {
                case R.id.save_1:
                    sfx_play(R.raw.sfx_choose);
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
                        fos.write(game_data);
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                    break;

                case R.id.save_2:
                    sfx_play(R.raw.sfx_choose);
                    try {
                        fos = openFileOutput(FILENAME2, Context.MODE_PRIVATE);
                        fos.write(game_data);
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                    break;

                case R.id.save_3:
                    sfx_play(R.raw.sfx_choose);
                    try {
                        fos = openFileOutput(FILENAME3, Context.MODE_PRIVATE);
                        fos.write(game_data);
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                    break;

                case R.id.save_4:
                    sfx_play(R.raw.sfx_choose);
                    try {
                        fos = openFileOutput(FILENAME4, Context.MODE_PRIVATE);
                        fos.write(game_data);
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                    break;

                case R.id.cancel:
                    sfx_play(R.raw.sfx_cancel);
                    finish();
                    break;
            }
        }
    };
}
