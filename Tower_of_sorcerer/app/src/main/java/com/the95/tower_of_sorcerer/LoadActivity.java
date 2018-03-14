package com.the95.tower_of_sorcerer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoadActivity extends AppCompatActivity {
    private String game_data = "0\n1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        findViewById(R.id.load_1).setOnClickListener(Load_Game);
        findViewById(R.id.load_2).setOnClickListener(Load_Game);
        findViewById(R.id.load_3).setOnClickListener(Load_Game);
        findViewById(R.id.load_4).setOnClickListener(Load_Game);

        load_data_from_save_files();
    }

    private void load_data_from_save_files() {

    }

    public View.OnClickListener Load_Game = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MediaPlayer SelectMusic = MediaPlayer.create(getApplicationContext(), R.raw.choose);
            switch (view.getId()) {
                case R.id.load_1:
                    SelectMusic.start();
                    Intent game_1 = new Intent(LoadActivity.this, Gamelogic.class);
                    game_1.putExtra("Game_File_1", game_data);
                    startActivity(game_1);
                    break;

                case R.id.load_2:
                    SelectMusic.start();
                    Intent game_2 = new Intent(LoadActivity.this, Gamelogic.class);
                    game_2.putExtra("Game_File_2", game_data);
                    startActivity(game_2);
                    break;

                case R.id.load_3:
                    SelectMusic.start();
                    Intent game_3 = new Intent(LoadActivity.this, Gamelogic.class);
                    game_3.putExtra("Game_File_3", game_data);
                    startActivity(game_3);
                    break;

                case R.id.load_4:
                    SelectMusic.start();
                    Intent game_4 = new Intent(LoadActivity.this, Gamelogic.class);
                    game_4.putExtra("Game_File_4", game_data);
                    startActivity(game_4);
                    break;
            }
        }
    };
}
