package com.the95.tower_of_sorcerer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LoadActivity extends AppCompatActivity {
    private byte[] game_data;
    private static final String TAG = "debuuuuuuuuuuuuuuuuuug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        findViewById(R.id.load_1).setOnClickListener(Load_Game);
        findViewById(R.id.load_2).setOnClickListener(Load_Game);
        findViewById(R.id.load_3).setOnClickListener(Load_Game);
        findViewById(R.id.load_4).setOnClickListener(Load_Game);
        findViewById(R.id.cancel).setOnClickListener(Load_Game);

        load_partial_data_from_save_file(1);
        load_partial_data_from_save_file(2);
        load_partial_data_from_save_file(3);
        load_partial_data_from_save_file(4);
    }

    private void load_partial_data_from_save_file(int save_idx) {
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
            byte[] meta_data = new byte[25];
            int field_count = 0;
            int total_bytes_read = fis.read(meta_data, 0, 25);
            Log.v(TAG, "meta data bytes = " + String.valueOf(total_bytes_read));
            int i = 0;
            StringBuilder sb = new StringBuilder();
            while(field_count < 5 && i < 25) {
                //Log.v(TAG, "i = " + String.valueOf(i) + " ,byte value = " + String.valueOf(meta_data[i]));
                if (meta_data[i] != 10)
                    sb.append((char) meta_data[i]);
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

    private void load_all_data_from_save_file(String file_name) {
        FileInputStream fis = null;
        try {
            fis = openFileInput(file_name);
            game_data = new byte[fis.available()];
            int total_bytes_read = fis.read(game_data);
            Log.v(TAG, "total bytes = " + String.valueOf(total_bytes_read));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public View.OnClickListener Load_Game = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MediaPlayer SelectMusic = MediaPlayer.create(getApplicationContext(), R.raw.choose);
            switch (view.getId()) {
                case R.id.load_1:
                    SelectMusic.start();
                    Intent game_1 = new Intent(LoadActivity.this, Gamelogic.class);
                    load_all_data_from_save_file("save1.txt");
                    game_1.putExtra("Game_File_1", game_data);
                    startActivity(game_1);
                    finish();
                    break;

                case R.id.load_2:
                    SelectMusic.start();
                    Intent game_2 = new Intent(LoadActivity.this, Gamelogic.class);
                    load_all_data_from_save_file("save2.txt");
                    game_2.putExtra("Game_File_2", game_data);
                    startActivity(game_2);
                    finish();
                    break;

                case R.id.load_3:
                    SelectMusic.start();
                    Intent game_3 = new Intent(LoadActivity.this, Gamelogic.class);
                    load_all_data_from_save_file("save3.txt");
                    game_3.putExtra("Game_File_3", game_data);
                    startActivity(game_3);
                    finish();
                    break;

                case R.id.load_4:
                    SelectMusic.start();
                    Intent game_4 = new Intent(LoadActivity.this, Gamelogic.class);
                    load_all_data_from_save_file("save4.txt");
                    game_4.putExtra("Game_File_4", game_data);
                    startActivity(game_4);
                    finish();
                    break;

                case R.id.cancel:
                    SelectMusic.start();
                    finish();
                    break;
            }
        }
    };
}
