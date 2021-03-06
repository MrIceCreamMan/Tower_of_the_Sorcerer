package com.the95.tower_of_the_sorcerer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LoadActivity extends AppCompatActivity {
    private byte[] game_data;
    private int[]  game_settings;
    private boolean[] can_load;
    private static final String TAG = "debuuuuuuuuuuuuuuuuuug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        Bundle extras = getIntent().getExtras();
        can_load = new boolean[]{false, false, false, false};
        if (extras != null) {
            game_settings = extras.getIntArray("Game_Settings");
        }

        findViewById(R.id.load_1).setOnClickListener(Load_Game);
        findViewById(R.id.load_2).setOnClickListener(Load_Game);
        findViewById(R.id.load_3).setOnClickListener(Load_Game);
        findViewById(R.id.load_4).setOnClickListener(Load_Game);
        findViewById(R.id.cancel).setOnClickListener(Load_Game);

        load_partial_data_from_save_file(1);
        load_partial_data_from_save_file(2);
        load_partial_data_from_save_file(3);
        load_partial_data_from_save_file(4);
        findViewById(R.id.load_1).setEnabled(can_load[0]);
        findViewById(R.id.load_2).setEnabled(can_load[1]);
        findViewById(R.id.load_3).setEnabled(can_load[2]);
        findViewById(R.id.load_4).setEnabled(can_load[3]);
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
            can_load[save_idx-1] = true;
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

    public View.OnClickListener Load_Game = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.load_1:
                    sfx_play(R.raw.sfx_choose);
                    Intent game_1 = new Intent(LoadActivity.this, Gamelogic.class);
                    load_all_data_from_save_file("save1.txt");
                    game_1.putExtra("Game_File_1", game_data);
                    game_1.putExtra("Game_Settings", game_settings);
                    startActivity(game_1);
                    finish();
                    break;

                case R.id.load_2:
                    sfx_play(R.raw.sfx_choose);
                    Intent game_2 = new Intent(LoadActivity.this, Gamelogic.class);
                    load_all_data_from_save_file("save2.txt");
                    game_2.putExtra("Game_File_2", game_data);
                    game_2.putExtra("Game_Settings", game_settings);
                    startActivity(game_2);
                    finish();
                    break;

                case R.id.load_3:
                    sfx_play(R.raw.sfx_choose);
                    Intent game_3 = new Intent(LoadActivity.this, Gamelogic.class);
                    load_all_data_from_save_file("save3.txt");
                    game_3.putExtra("Game_File_3", game_data);
                    game_3.putExtra("Game_Settings", game_settings);
                    startActivity(game_3);
                    finish();
                    break;

                case R.id.load_4:
                    sfx_play(R.raw.sfx_choose);
                    Intent game_4 = new Intent(LoadActivity.this, Gamelogic.class);
                    load_all_data_from_save_file("save4.txt");
                    game_4.putExtra("Game_File_4", game_data);
                    game_4.putExtra("Game_Settings", game_settings);
                    startActivity(game_4);
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
