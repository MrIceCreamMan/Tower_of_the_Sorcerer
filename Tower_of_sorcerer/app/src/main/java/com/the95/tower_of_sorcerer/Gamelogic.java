package com.the95.tower_of_sorcerer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.Bitmap.createScaledBitmap;



public class Gamelogic extends Activity implements View.OnTouchListener {
    // constants for convenience
    public final int    DOWN = 0,          LEFT = 1,       RIGHT = 2,      UP = 3;
    public final int    STAFF1 = 4,        STAFF2 = 5,     FLY_UP = 6,     FLY_DOWN = 7;
    public final int    ITEM1 = 8,         ITEM2 = 9,      ITEM3 = 10,     ITEM4 = 11,         ITEM5 = 12;
    public final int    ITEM6 = 13,        ITEM7 = 14,     ITEM8 = 15,     ITEM9 = 16,         ITEM10 = 17;
    public final int    ITEM11 = 18,       ITEM12 = 19,    SAVE = 20;
    public final int[] bgm_list = new int[]{
            R.raw.level1_background,        R.raw.level2_background,    R.raw.level3_background,
            R.raw.level4_background,        R.raw.level5_background};
    public final int[] boss_music_list = new int[]{
            R.raw.level1_skeleton_fight,    R.raw.level2_vampire_fight,     R.raw.level3_archmage_fight,
            R.raw.level4_knight_fight,      R.raw.level5_demon_fight};
    public final int[][] m_table = new int[][]{
            {  35,   18,    1,    1},   // 0    green slime
            {  45,   20,    2,    2},   // 1    red slime
            {  35,   38,    3,    3},   // 2    bat
            {  60,   32,    8,    5},   // 3    priest
            {  50,   42,    6,    6},   // 4    skeleton
            {  55,   52,   12,    8},   // 5    skeleton warrior
            {  50,   48,   22,   12},   // 6    gate-keeper
            { 100,   65,   15,   30},   // 7    skeleton captain
            { 130,   60,    3,    8},   // 8    black slime
            {  60,  100,    8,   12},   // 9    giant bat
            { 100,   95,   30,   18},   //10    priest elite
            { 260,   85,    5,   22},   //11    zombie
            {  20,  100,   68,   28},   //12    rock monster
            { 320,  120,   15,   30},   //13    zombie warrior
            { 444,  199,   66,  144},   //14    vampire
            { 320,  140,   20,   30},   //15    slime-man
            { 220,  180,   30,   35},   //16    skeleton elite
            { 210,  200,   65,   45},   //17    knight
            { 100,  180,  110,   50},   //18    advanced gate-keeper
            { 100,  680,   50,   55},   //19    swordsman
            { 160,  230,  105,   65},   //20    knight elite
            { 120,  150,   50,  100},   //21    knight captain
            { 360,  310,   20,   40},   //22    slimelord
            { 200,  390,   90,   50},   //23    vampire bat
            { 220,  370,  110,   80},   //24    mage
            { 200,  380,  130,   90},   //25    mage master
            { 230,  450,  100,  100},   //26    demon sergeant
            { 180,  430,  210,  120},   //27    dark knight
            { 180,  460,  360,  200},   //28    elite gate-keeper
            {8000, 5000, 1000,  500},   //29    fake zeno with power
            { 800,  500,  100,  500},   //30    fake zeno without power
            {1200,  180,   20,  100},   //31    octopus
            {1500,  600,  250,  800},   //32    dragon
            {4500,  560,  310, 1000},   //33    archmage
            {1000,  625,  125, 1000}    //34    real zeno
    };
    //  general game controls
    private GameView            gameview;
    private ArrayList<Sprite>   all_sprites;
    private List<Byte>          monsters_to_draw;
    private ArrayList<Sprite>   monsters_picture;
    private ArrayList<String>   monsters_name1, monsters_name2;
    private static Gamelogic    parent;
    private byte[]              game_data_to_save;
    private MediaPlayer         background_music, sfx_music;
    private String              instruction;
    private boolean[]           music_settings;
    private int                 walk_result,    walk_count,     which_button,       sq_size;
    private int                 m_hp,           m_atk,          m_def,              m_gold;
    private float               x,              y,              page,               total_page;
    private int                 extra_height;
    private boolean             refresh_ctr,    load_ctr,       battle_coming,      button_click;
    private boolean             isWalk,         isBattle,       isEvent,            cantMove;
    private boolean             hero_attack,    show_hero,      not_show_hero,      show_fight;
    private boolean             blackout,       proceed,        no_dialog,          which_surface_view;
    //  current game data
    private Floors      current_game;
    private byte[][]    current_floor;
    private int         hp,                 atk,                def,                gold;
    private int         floor_num,          act,                thief_event_count,  highest_floor;
    private int         count_y,            count_b,            count_r,            count_wing;
    private int         hero_x,             hero_y,             temp_x,             temp_y;
    private int         price_idx;
    private boolean     stf_wsdm,           stf_echo,           stf_space,          cross;
    private boolean     elixir,             m_mattock,          wing_cent,          e_mattock;
    private boolean     bomb,               wing_up,            key_enhac,          wing_down;
    private boolean     lucky_gold,         dragonsbane,        snow_cryst,         sacred_shield;
    private int[]       merchant_history,   saint_history,      echo_history;
    // game pictures and stats table
    private Bitmap hero;
    private Sprite hero_sprite, red_star_sprite, octopus_sprite, dragon_sprite;
    private Bitmap menu_health, menu_gold, menu_background, menu_stf1, menu_stf2;
    private Bitmap menu_stf3, menu_up, menu_down, menu_left, menu_right;
    private Bitmap t__floor, t___wall, t___star, t_ustair, t_dstair, t_r_star;
    private Bitmap t_door_y, t_door_b, t_door_r, t_door_m, t_prison, t___logo;
    private Bitmap w___ironw, w_silverw, w_knightw, w_divinew, w_sacredw;
    private Bitmap w___ironh, w_silverh, w_knighth, w_divineh, w_sacredh;
    private Bitmap n___thief, n___saint, n_merchat, n___fairy, n_shop__l;
    private Bitmap n_shop__m, n_shop__r, n_princes, n____lava;
    private Bitmap m__slime_g, m__slime_r, m_bat_fier, m___priest, m_skeleton;
    private Bitmap m_skelet_w, m_gatekeep, m_skelet_c, m__slime_b, m_bat_gian;
    private Bitmap m_priest_m, m___zombie, m_stone_gd, m_zombie_w, m__vampire;
    private Bitmap m__slime_m, m_skelet_e, m___knight, m_gatekp_e, m_swordsmn;
    private Bitmap m_knight_e, m_knight_c, m_slimelod, m_bat_vamp, m_____mage;
    private Bitmap m_mage_mas, m_demo_sgt, m_d_knight, m_gate_gdn, m_demozeno;
    private Bitmap m__octopus, m___dragon, m_archmage;
    private Bitmap i____key_y, i____key_b, i____key_r, i_potion_r, i_potion_b;
    private Bitmap i_crystl_r, i_crystl_b, i_stf_wsdm, i_stf_echo, i_stf_spce;
    private Bitmap i____cross, i___elixir, i_m_mattok, i_wing_cen, i_e_mattok;
    private Bitmap i_____bomb, i__wing_up, i_key_ehac, i_wing_dow, i_lucky_gd;
    private Bitmap i_dra_bane, i_snow_crs;

    //  debug purpose
    private Paint pt1, pt2, pt3, pt4, pt5, pt6, pt7, pt8;
    private Bitmap ball, pic_debug, pic_d1, pic_d2, pic_d3, pic_dh;
    private static final String TAG = "debuuuuuuuuuuuuuuuuuug";
    //Log.v(TAG, "x = " + me.getX() + " y = " + me.getY());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize general game controls
        gameview = new GameView(Gamelogic.this);
        gameview.setOnTouchListener(this);
        all_sprites = new ArrayList<>();
        monsters_to_draw = new ArrayList<>();           monsters_picture = new ArrayList<>();
        monsters_name1 = new ArrayList<>();             monsters_name2 = new ArrayList<>();
        walk_result = 0;        walk_count = 0;         which_button = 6;       sq_size = 32;
        m_hp =      0;          m_atk = 0;              m_def = 0;              m_gold = 0;
        x = 0;                  y = 0;                  page = 0;               total_page = 1;
        extra_height = 0;
        refresh_ctr = true;     load_ctr = true;        battle_coming = false;  button_click = false;
        isWalk = false;         isBattle = false;       isEvent = false;        cantMove = false;
        hero_attack = true;     show_hero = true;       not_show_hero = false;  show_fight = false;
        blackout = false;       proceed = false;        no_dialog = true;       which_surface_view = true;

        // initialize current game data
        current_game = new Floors();
        current_floor = current_game.get_one_floor(1);
        hp = 1000;              atk = 200;              def = 100;              gold = 0;
        floor_num = 1;          act = 0;                thief_event_count = 0;  highest_floor = 1;
        count_y = 10;           count_b = 10;           count_r = 10;           count_wing = 0;
        hero_x = 6;             hero_y = 11;            temp_x = 6;             temp_y = 11;
        price_idx = 0;
        stf_wsdm = false;       stf_echo = false;       stf_space = false;      cross = false;
        elixir = false;         m_mattock = false;      wing_cent = false;      e_mattock = false;
        bomb = false;           wing_up = false;        key_enhac = false;      wing_down = false;
        lucky_gold = false;     dragonsbane = false;    snow_cryst = false;     sacred_shield = false;
        merchant_history = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        saint_history    = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        echo_history     = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // check if needs to load game
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            music_settings = extras.getBooleanArray("Music_Settings");
            byte[] game_data_to_load_1 = extras.getByteArray("Game_File_1");
            byte[] game_data_to_load_2 = extras.getByteArray("Game_File_2");
            byte[] game_data_to_load_3 = extras.getByteArray("Game_File_3");
            byte[] game_data_to_load_4 = extras.getByteArray("Game_File_4");
            if (game_data_to_load_1 != null)
                load_game(game_data_to_load_1);
            else if (game_data_to_load_2 != null)
                load_game(game_data_to_load_2);
            else if (game_data_to_load_3 != null)
                load_game(game_data_to_load_3);
            else if (game_data_to_load_4 != null)
                load_game(game_data_to_load_4);
        }

        // set background music
        int which_music;
        if (floor_num == 0)
            which_music = R.raw.level1_background;
        else if (floor_num == 50)
            which_music = R.raw.bgm_ending;
        else if (floor_num == 49) {
            if (act == 0)
                which_music = R.raw.level5_background;
            else
                which_music = R.raw.level5_demon_fight;
        } else if (floor_num%10 == 0) {
            if (act == 0)
                which_music = bgm_list[floor_num/10 - 1];
            else
                which_music = boss_music_list[floor_num/10 - 1];
        } else
            which_music = bgm_list[floor_num/10];
        background_music = MediaPlayer.create(getApplicationContext(), which_music);
        background_music.setLooping(true);
        if (music_settings[0])
            background_music.start();

        // debug/testing purpose
        set_all_true();

        // initialize pictures
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.newearth);
        pic_debug = BitmapFactory.decodeResource(getResources(), R.drawable.z1_debug);
        pic_d1 = BitmapFactory.decodeResource(getResources(), R.drawable.z3);
        pic_d2 = BitmapFactory.decodeResource(getResources(), R.drawable.z4);
        pic_d3 = BitmapFactory.decodeResource(getResources(), R.drawable.z5);
        pic_dh = BitmapFactory.decodeResource(getResources(), R.drawable.z2);
        hero = BitmapFactory.decodeResource(getResources(), R.drawable.hero);

        menu_health = BitmapFactory.decodeResource(getResources(), R.drawable.menu_health);
        menu_gold = BitmapFactory.decodeResource(getResources(), R.drawable.menu_gold);
        menu_background = BitmapFactory.decodeResource(getResources(), R.drawable.menu_backgournd);
        menu_up = BitmapFactory.decodeResource(getResources(), R.drawable.menu_up);
        menu_down = BitmapFactory.decodeResource(getResources(), R.drawable.menu_down);
        menu_left = BitmapFactory.decodeResource(getResources(), R.drawable.menu_left);
        menu_right = BitmapFactory.decodeResource(getResources(), R.drawable.menu_right);

        t__floor = BitmapFactory.decodeResource(getResources(), R.drawable.tile1_floor);
        t___wall = BitmapFactory.decodeResource(getResources(), R.drawable.tile2_wall);
        t___star = BitmapFactory.decodeResource(getResources(), R.drawable.tile3_star);
        t_ustair = BitmapFactory.decodeResource(getResources(), R.drawable.tile4_upstairs);
        t_dstair = BitmapFactory.decodeResource(getResources(), R.drawable.tile5_downstairs);
        t_door_y = BitmapFactory.decodeResource(getResources(), R.drawable.tile6_door_y);
        t_door_b = BitmapFactory.decodeResource(getResources(), R.drawable.tile7_door_b);
        t_door_r = BitmapFactory.decodeResource(getResources(), R.drawable.tile8_door_r);
        t_door_m = BitmapFactory.decodeResource(getResources(), R.drawable.tile9_door_m);
        t_prison = BitmapFactory.decodeResource(getResources(), R.drawable.tile10_prison);
        t___logo = BitmapFactory.decodeResource(getResources(), R.drawable.tile11_logo);
        t_r_star = BitmapFactory.decodeResource(getResources(), R.drawable.tile12_red_star);
        w___ironw = BitmapFactory.decodeResource(getResources(), R.drawable.w01_iron_sword);
        w___ironh = BitmapFactory.decodeResource(getResources(), R.drawable.w02_iron_shield);
        w_silverw = BitmapFactory.decodeResource(getResources(), R.drawable.w03_silver_sword);
        w_silverh = BitmapFactory.decodeResource(getResources(), R.drawable.w04_silver_shield);
        w_knightw = BitmapFactory.decodeResource(getResources(), R.drawable.w05_knight_sword);
        w_knighth = BitmapFactory.decodeResource(getResources(), R.drawable.w06_knight_shield);
        w_divinew = BitmapFactory.decodeResource(getResources(), R.drawable.w07_divine_sword);
        w_divineh = BitmapFactory.decodeResource(getResources(), R.drawable.w08_divine_shield);
        w_sacredw = BitmapFactory.decodeResource(getResources(), R.drawable.w09_sacred_sword);
        w_sacredh = BitmapFactory.decodeResource(getResources(), R.drawable.w10_sacred_shield);
        n___thief = BitmapFactory.decodeResource(getResources(), R.drawable.npc1_thief);
        n___saint = BitmapFactory.decodeResource(getResources(), R.drawable.npc2_saint);
        n_merchat = BitmapFactory.decodeResource(getResources(), R.drawable.npc3_merchant);
        n___fairy = BitmapFactory.decodeResource(getResources(), R.drawable.npc4_elf);
        n_shop__l = BitmapFactory.decodeResource(getResources(), R.drawable.npc5_shop_left);
        n_shop__m = BitmapFactory.decodeResource(getResources(), R.drawable.npc6_shop_middle);
        n_shop__r = BitmapFactory.decodeResource(getResources(), R.drawable.npc7_shop_right);
        n_princes = BitmapFactory.decodeResource(getResources(), R.drawable.npc8_princess);
        n____lava = BitmapFactory.decodeResource(getResources(), R.drawable.npc9_lava);
        m__slime_g = BitmapFactory.decodeResource(getResources(), R.drawable.m01t1_green_slime);
        m__slime_r = BitmapFactory.decodeResource(getResources(), R.drawable.m02t1_red_slime);
        m_bat_fier = BitmapFactory.decodeResource(getResources(), R.drawable.m03t1_fierce_bat);
        m___priest = BitmapFactory.decodeResource(getResources(), R.drawable.m04t1_priest);
        m_skeleton = BitmapFactory.decodeResource(getResources(), R.drawable.m05t1_skeleton);
        m_skelet_w = BitmapFactory.decodeResource(getResources(), R.drawable.m06t1_skeleton_warrior);
        m_gatekeep = BitmapFactory.decodeResource(getResources(), R.drawable.m07t1_gatekeeper);
        m_skelet_c = BitmapFactory.decodeResource(getResources(), R.drawable.m08t1_skeleton_captain);
        m__slime_b = BitmapFactory.decodeResource(getResources(), R.drawable.m09t2_black_slime);
        m_bat_gian = BitmapFactory.decodeResource(getResources(), R.drawable.m10t2_giant_bat);
        m_priest_m = BitmapFactory.decodeResource(getResources(), R.drawable.m11t2_priest_master);
        m___zombie = BitmapFactory.decodeResource(getResources(), R.drawable.m12t22_zombie);
        m_stone_gd = BitmapFactory.decodeResource(getResources(), R.drawable.m13t2_stone_guardian);
        m_zombie_w = BitmapFactory.decodeResource(getResources(), R.drawable.m14t2_zombie_warrior);
        m__vampire = BitmapFactory.decodeResource(getResources(), R.drawable.m15t2_vampire);
        m__slime_m = BitmapFactory.decodeResource(getResources(), R.drawable.m16t3_slime_man);
        m_skelet_e = BitmapFactory.decodeResource(getResources(), R.drawable.m17t3_skeleton_elite);
        m___knight = BitmapFactory.decodeResource(getResources(), R.drawable.m18t3_knight);
        m_gatekp_e = BitmapFactory.decodeResource(getResources(), R.drawable.m19t3_gatekeeper_elite);
        m_swordsmn = BitmapFactory.decodeResource(getResources(), R.drawable.m20t3_swordsman);
        m_knight_e = BitmapFactory.decodeResource(getResources(), R.drawable.m21t3_knight_elite);
        m_knight_c = BitmapFactory.decodeResource(getResources(), R.drawable.m22t3_knight_captain);
        m_slimelod = BitmapFactory.decodeResource(getResources(), R.drawable.m23t4_slimelord);
        m_bat_vamp = BitmapFactory.decodeResource(getResources(), R.drawable.m24t4_vampire_bat);
        m_____mage = BitmapFactory.decodeResource(getResources(), R.drawable.m25t4_mage);
        m_mage_mas = BitmapFactory.decodeResource(getResources(), R.drawable.m26t4_mage_master);
        m_demo_sgt = BitmapFactory.decodeResource(getResources(), R.drawable.m27t4_demo_sergent);
        m_d_knight = BitmapFactory.decodeResource(getResources(), R.drawable.m28t4_dark_knight);
        m_gate_gdn = BitmapFactory.decodeResource(getResources(), R.drawable.m29t4_gate_guardian);
        m_demozeno = BitmapFactory.decodeResource(getResources(), R.drawable.m30t4_zeno);
        m__octopus = BitmapFactory.decodeResource(getResources(), R.drawable.m31b1_octopus);
        m___dragon = BitmapFactory.decodeResource(getResources(), R.drawable.m32b2_dragon);
        m_archmage = BitmapFactory.decodeResource(getResources(), R.drawable.m33b3_archmage);
        i____key_y = BitmapFactory.decodeResource(getResources(), R.drawable.i1_yellow_key);
        i____key_b = BitmapFactory.decodeResource(getResources(), R.drawable.i2_blue_key);
        i____key_r = BitmapFactory.decodeResource(getResources(), R.drawable.i3_red_key);
        i_potion_r = BitmapFactory.decodeResource(getResources(), R.drawable.i4_red_potion);
        i_potion_b = BitmapFactory.decodeResource(getResources(), R.drawable.i5_blue_potion);
        i_crystl_r = BitmapFactory.decodeResource(getResources(), R.drawable.i6_red_crystal);
        i_crystl_b = BitmapFactory.decodeResource(getResources(), R.drawable.i7_blue_crystal);
        i_stf_wsdm = BitmapFactory.decodeResource(getResources(), R.drawable.i8_staff_of_wisdom);
        i_stf_echo = BitmapFactory.decodeResource(getResources(), R.drawable.i9_staff_of_echo);
        i_stf_spce = BitmapFactory.decodeResource(getResources(), R.drawable.i10_staff_of_space);
        i____cross = BitmapFactory.decodeResource(getResources(), R.drawable.i11_cross);
        i___elixir = BitmapFactory.decodeResource(getResources(), R.drawable.i12_elixir);
        i_m_mattok = BitmapFactory.decodeResource(getResources(), R.drawable.i13_magic_mattock);
        i_wing_cen = BitmapFactory.decodeResource(getResources(), R.drawable.i14_magic_wing_center);
        i_e_mattok = BitmapFactory.decodeResource(getResources(), R.drawable.i15_enhanced_mattock);
        i_____bomb = BitmapFactory.decodeResource(getResources(), R.drawable.i16_bomb);
        i__wing_up = BitmapFactory.decodeResource(getResources(), R.drawable.i17_magic_wing_up);
        i_key_ehac = BitmapFactory.decodeResource(getResources(), R.drawable.i18_enhanced_key);
        i_wing_dow = BitmapFactory.decodeResource(getResources(), R.drawable.i19_magic_wing_down);
        i_lucky_gd = BitmapFactory.decodeResource(getResources(), R.drawable.i20_lucky_gold);
        i_dra_bane = BitmapFactory.decodeResource(getResources(), R.drawable.i21_dragonsbane);
        i_snow_crs = BitmapFactory.decodeResource(getResources(), R.drawable.i22_snow_crystal);
        // button background color to show if buttons are clicked
        pt1 = new Paint();
        pt1.setColor(Color.rgb(220, 220, 220));
        pt1.setStrokeWidth(10);
        pt2 = new Paint();
        pt2.setColor(Color.rgb(220, 220, 220));
        pt2.setStrokeWidth(10);
        pt3 = new Paint();
        pt3.setColor(Color.rgb(220, 220, 220));
        pt3.setStrokeWidth(10);
        pt4 = new Paint();
        pt4.setColor(Color.rgb(220, 220, 220));
        pt4.setStrokeWidth(10);
        pt5 = new Paint();
        pt5.setColor(Color.rgb(220, 220, 220));
        pt5.setStrokeWidth(10);
        pt6 = new Paint();
        pt6.setColor(Color.rgb(180, 150, 180));
        pt6.setStrokeWidth(10);
        pt7 = new Paint();
        pt7.setColor(Color.rgb(150, 150, 150));
        pt7.setStrokeWidth(10);
        pt8 = new Paint();
        pt8.setColor(Color.rgb(220, 220, 220));
        pt8.setStrokeWidth(10);
        //Log.v(TAG, "width = " + sq_wall.getWidth() + " y = " + sq_wall.getHeight());
        setContentView(gameview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameview.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameview.resume();
    }

    @Override
    public boolean onTouch(View v, MotionEvent me) {
        sleep(25);

        switch (me.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!which_surface_view) {
                    v.performClick();
                    return true;
                }
                if (blackout) {
                    AlertDialog.Builder wakeup_builder = new AlertDialog.Builder(v.getContext());
                    wakeup_builder.setMessage(R.string.wake_up);
                    AlertDialog wakeup_dialog = wakeup_builder.create();
                    wakeup_dialog.setCanceledOnTouchOutside(true);
                    wakeup_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            isEvent = false;
                            blackout = false;
                        }
                    });
                    wakeup_dialog.show();
                    return true;
                }
                if (isEvent)
                    return true;
                x = me.getX();
                y = me.getY();
                final int origin = 0 - sq_size / 2;
                final int offset = extra_height / 5;
                final int margin = sq_size / 10;
                if (x > sq_size * 9 + origin && y > sq_size * 13 + origin + offset && x < sq_size * 10 && y < sq_size * 14 + offset) {
                    pt1.setColor(Color.rgb(255, 255, 255));
                    which_button = UP;
                    temp_x = hero_x;
                    temp_y = hero_y - 1;
                } else if (x > sq_size * 9 + origin && y > sq_size * 16 + origin + offset && x < sq_size * 10 && y < sq_size * 17 + offset) {
                    pt2.setColor(Color.rgb(255, 255, 255));
                    which_button = DOWN;
                    temp_x = hero_x;
                    temp_y = hero_y + 1;
                } else if (x > sq_size * 7 && y > sq_size * 14 + offset && x < sq_size * 9 + origin && y < sq_size * 16 + origin + offset) {
                    pt3.setColor(Color.rgb(255, 255, 255));
                    which_button = LEFT;
                    temp_x = hero_x - 1;
                    temp_y = hero_y;
                } else if (x > sq_size * 10 && y > sq_size * 14 + offset && x < sq_size * 12 + origin && y < sq_size * 16 + origin + offset) {
                    pt4.setColor(Color.rgb(255, 255, 255));
                    which_button = RIGHT;
                    temp_x = hero_x + 1;
                    temp_y = hero_y;
                } else if (x > sq_size * 7 && y > sq_size * 17 + offset*3 && x < sq_size * 9 + origin && y < sq_size * 19 + offset*3) {
                    pt5.setColor(Color.rgb(255, 255, 255));
                    which_button = STAFF1;
                    temp_y = hero_y; temp_x = hero_x;
                    if (stf_wsdm) {
                        load_ctr = true;
                        which_surface_view = false;
                    }
                } else if (x > sq_size * 9 + origin && y > sq_size * 17 + offset*3 && x < sq_size * 10 && y < sq_size * 19 + offset*3) {
                    pt6.setColor(Color.rgb(255, 255, 255));
                    temp_y = hero_y; temp_x = hero_x;
                    which_button = STAFF2;
                    if (stf_echo) {
                        LayoutInflater staff_echo_inflater = LayoutInflater.from(Gamelogic.this);
                        View staff_of_echo_view = staff_echo_inflater.inflate(R.layout.staff_of_echo_menu, null);
                        AlertDialog.Builder staff_echo_builder = new AlertDialog.Builder(Gamelogic.this);
                        final AlertDialog staff_echo_dialog = staff_echo_builder.create();
                        staff_echo_dialog.setCanceledOnTouchOutside(true);
                        final AlertDialog.Builder echo_builder = new AlertDialog.Builder(Gamelogic.this);

                        Button btn1 = staff_of_echo_view.findViewById(R.id.button1);
                        btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[0] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.saint_4f);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn2 = staff_of_echo_view.findViewById(R.id.button2);
                        btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[1] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.saint_6f);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn3 = staff_of_echo_view.findViewById(R.id.button3);
                        btn3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[2] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.merchant_6f1);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn4 = staff_of_echo_view.findViewById(R.id.button4);
                        btn4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[3] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.merchant_7f1);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn5 = staff_of_echo_view.findViewById(R.id.button5);
                        btn5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[4] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.merchant_12f1);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn6 = staff_of_echo_view.findViewById(R.id.button6);
                        btn6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[5] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.merchant_15f1);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn7 = staff_of_echo_view.findViewById(R.id.button7);
                        btn7.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[6] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.saint_16f);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn8 = staff_of_echo_view.findViewById(R.id.button8);
                        btn8.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[7] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.saint_18f);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn9 = staff_of_echo_view.findViewById(R.id.button9);
                        btn9.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[8] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.saint_21f);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn10 = staff_of_echo_view.findViewById(R.id.button10);
                        btn10.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[9] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.saint_27f);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn11 = staff_of_echo_view.findViewById(R.id.button11);
                        btn11.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[10] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.saint_31f);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn12 = staff_of_echo_view.findViewById(R.id.button12);
                        btn12.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[11] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.merchant_31f1);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn13 = staff_of_echo_view.findViewById(R.id.button13);
                        btn13.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[12] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.saint_33f);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn14 = staff_of_echo_view.findViewById(R.id.button14);
                        btn14.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[13] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.saint_36f);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn15 = staff_of_echo_view.findViewById(R.id.button15);
                        btn15.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[14] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.saint_37f);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn16 = staff_of_echo_view.findViewById(R.id.button16);
                        btn16.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[15] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.merchant_38f1);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn17 = staff_of_echo_view.findViewById(R.id.button17);
                        btn17.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[16] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.saint_39f);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn18 = staff_of_echo_view.findViewById(R.id.button18);
                        btn18.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[17] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.saint_45f);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn19 = staff_of_echo_view.findViewById(R.id.button19);
                        btn19.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[18] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.saint_42f);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn20 = staff_of_echo_view.findViewById(R.id.button20);
                        btn20.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[19] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.saint_45f);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn21 = staff_of_echo_view.findViewById(R.id.button21);
                        btn21.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[20] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.merchant_45f1);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn22 = staff_of_echo_view.findViewById(R.id.button22);
                        btn22.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[21] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.saint_46f);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn23 = staff_of_echo_view.findViewById(R.id.button23);
                        btn23.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[22] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.merchant_47f1);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn24 = staff_of_echo_view.findViewById(R.id.button24);
                        btn24.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (echo_history[23] == 0) {
                                    echo_builder.setMessage(R.string.no_echo);
                                    staff_echo_dialog.dismiss();
                                } else {
                                    echo_builder.setMessage(R.string.saint_48f);
                                    staff_echo_dialog.dismiss();
                                }
                                AlertDialog echo_dialog = echo_builder.create();
                                echo_dialog.setCanceledOnTouchOutside(true);
                                echo_dialog.show();
                            }
                        });
                        Button btn_cancel = staff_of_echo_view.findViewById(R.id.echo_cancel);
                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                staff_echo_dialog.cancel();
                            }
                        });
                        staff_echo_dialog.setView(staff_of_echo_view);
                        staff_echo_dialog.show();
                    }
                } else if (x > sq_size * 10 && y > sq_size * 17 + offset*3 && x < sq_size * 12 + origin && y < sq_size * 18 + offset*3) {
                    pt7.setColor(Color.rgb(255, 255, 255));
                    temp_y = hero_y; temp_x = hero_x;
                    which_button = FLY_UP;
                } else if (x > sq_size * 10 && y > sq_size * 18 + offset*3 && x < sq_size * 12 + origin && y < sq_size * 19 + offset*3) {
                    pt8.setColor(Color.rgb(255, 255, 255));
                    temp_y = hero_y; temp_x = hero_x;
                    which_button = FLY_DOWN;
                } else if (x > origin + sq_size && y > sq_size * 17 + offset*3 && x < sq_size*2 + origin && y < sq_size * 18 + offset*3) {
                    temp_y = hero_y; temp_x = hero_x;
                    which_button = ITEM1;
                } else if (x > origin + sq_size*2 && y > sq_size * 17 + offset*3 && x < sq_size*3 + origin && y < sq_size * 18 + offset*3) {
                    temp_y = hero_y; temp_x = hero_x;
                    which_button = ITEM2;
                } else if (x > origin + sq_size*3 && y > sq_size * 17 + offset*3 && x < sq_size*4 + origin && y < sq_size * 18 + offset*3) {
                    temp_y = hero_y; temp_x = hero_x;
                    which_button = ITEM3;
                } else if (x > origin + sq_size*4 && y > sq_size * 17 + offset*3 && x < sq_size*5 + origin && y < sq_size * 18 + offset*3) {
                    temp_y = hero_y; temp_x = hero_x;
                    which_button = ITEM4;
                } else if (x > origin + sq_size*5 && y > sq_size * 17 + offset*3 && x < sq_size*6 + origin && y < sq_size * 18 + offset*3) {
                    temp_y = hero_y; temp_x = hero_x;
                    which_button = ITEM5;
                } else if (x > origin + sq_size*6 && y > sq_size * 17 + offset*3 && x < sq_size*7 + origin && y < sq_size * 18 + offset*3) {
                    temp_y = hero_y; temp_x = hero_x;
                    which_button = ITEM6;
                } else if (x > origin + sq_size && y > sq_size * 18 + offset*3 && x < sq_size*2 + origin && y < sq_size * 19 + offset*3) {
                    temp_y = hero_y; temp_x = hero_x;
                    which_button = ITEM7;
                } else if (x > origin + sq_size*2 && y > sq_size * 18 + offset*3 && x < sq_size*3 + origin && y < sq_size * 19 + offset*3) {
                    temp_y = hero_y; temp_x = hero_x;
                    which_button = ITEM8;
                } else if (x > origin + sq_size*3 && y > sq_size * 18 + offset*3 && x < sq_size*4 + origin && y < sq_size * 19 + offset*3) {
                    temp_y = hero_y; temp_x = hero_x;
                    which_button = ITEM9;
                } else if (x > origin + sq_size*4 && y > sq_size * 18 + offset*3 && x < sq_size*5 + origin && y < sq_size * 19 + offset*3) {
                    temp_y = hero_y; temp_x = hero_x;
                    which_button = ITEM10;
                } else if (x > origin + sq_size*5 && y > sq_size * 18 + offset*3 && x < sq_size*6 + origin && y < sq_size * 19 + offset*3) {
                    temp_y = hero_y; temp_x = hero_x;
                    which_button = ITEM11;
                } else if (x > origin + sq_size*6 && y > sq_size * 18 + offset*3 && x < sq_size*7 + origin && y < sq_size * 19 + offset*3) {
                    temp_y = hero_y; temp_x = hero_x;
                    which_button = ITEM12;
                } else if (x > sq_size*5 - margin*4 && y > sq_size*13 + margin + offset && x < sq_size*6 + margin*7 && y < sq_size * 14 - margin + offset) {
                    temp_y = hero_y; temp_x = hero_x;
                    which_button = SAVE;
                } else {
                    temp_y = hero_y; temp_x = hero_x;
                    which_button = 21;
                }
                button_click = true;
                return true;
            case MotionEvent.ACTION_UP:
                if (!which_surface_view) {
                    return true;
                }
                button_click = false;
                no_dialog = true;
                //Log.v(TAG, game_data);
                pt1.setColor(Color.rgb(220, 220, 220));
                pt2.setColor(Color.rgb(220, 220, 220));
                pt3.setColor(Color.rgb(220, 220, 220));
                pt4.setColor(Color.rgb(220, 220, 220));
                pt5.setColor(Color.rgb(220, 220, 220));
                pt6.setColor(Color.rgb(180, 150, 180));
                pt7.setColor(Color.rgb(150, 150, 150));
                pt8.setColor(Color.rgb(220, 220, 220));
                checkNextPosition(v, temp_x, temp_y);
                return true;
            default:
                if (!which_surface_view) {
                    return true;
                }
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder back_builder = new AlertDialog.Builder(this);
        back_builder.setCancelable(false);
        back_builder.setMessage(R.string.back_btn_alert);
        back_builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                if (background_music != null)
                    background_music.release();
                finish();
            }
        });
        back_builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog back_alert = back_builder.create();
        back_alert.show();
    }

    // helper function to set all items to be true
    private void set_all_true() {
        stf_wsdm = stf_echo = stf_space = cross = elixir = true;
        m_mattock = wing_cent = e_mattock = bomb = wing_up = true;
        key_enhac = wing_down = lucky_gold = dragonsbane = snow_cryst = true;
        count_wing = 3;
    }

    // function allows the thread to sleep
    private void sleep(int sleep_time) {
        try {
            Thread.sleep(sleep_time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // boolean type to char type '0' and '1',
    // helper function for "prepare_to_save_game()"
    private char bool_to_0n1(boolean in_val) {
        if (in_val)
            return '1';
        else
            return '0';
    }

    // prepare to save to game
    private void prepare_to_save_game(){
        current_game.put_one_floor(floor_num, current_floor);

        String game_data = "";
        game_data += String.valueOf(hp) + "\n"          + String.valueOf(atk) + "\n";
        game_data += String.valueOf(def) + "\n"         + String.valueOf(gold) + "\n";
        game_data += String.valueOf(floor_num) + "\n"   + String.valueOf(count_y) + "\n";
        game_data += String.valueOf(count_b) + "\n"     + String.valueOf(count_r) + "\n";
        game_data += String.valueOf(hero_x) + "\n"      + String.valueOf(hero_y) + "\n";
        game_data += String.valueOf(temp_x) + "\n"      + String.valueOf(temp_y) + "\n";
        game_data += String.valueOf(price_idx) + "\n"   + String.valueOf(count_wing) + "\n";
        game_data += String.valueOf(act) + "\n"         + String.valueOf(highest_floor) + "\n";
        game_data += String.valueOf(thief_event_count) + "\n";
        game_data += bool_to_0n1(stf_wsdm) + "\n"    + bool_to_0n1(stf_echo) + "\n";
        game_data += bool_to_0n1(stf_space) + "\n"   + bool_to_0n1(cross) + "\n";
        game_data += bool_to_0n1(elixir) + "\n"      + bool_to_0n1(m_mattock) + "\n";
        game_data += bool_to_0n1(wing_cent) + "\n"   + bool_to_0n1(e_mattock) + "\n";
        game_data += bool_to_0n1(bomb) + "\n"        + bool_to_0n1(wing_up) + "\n";
        game_data += bool_to_0n1(key_enhac) + "\n"   + bool_to_0n1(wing_down) + "\n";
        game_data += bool_to_0n1(lucky_gold) + "\n"  + bool_to_0n1(dragonsbane) + "\n";
        game_data += bool_to_0n1(snow_cryst) + "\n"  + bool_to_0n1(sacred_shield) + "\n";
        StringBuilder sb = new StringBuilder(game_data);
        //
        for (int i = 0; i < 12; i++)
            sb.append(String.valueOf(merchant_history[i]));
        for (int j = 0; j < 19; j++)
            sb.append(String.valueOf(saint_history[j]));
        for (int k = 0; k < 24; k++)
            sb.append(String.valueOf(echo_history[k]));


        byte[] temp_array = sb.toString().getBytes();
        game_data_to_save = new byte[temp_array.length + 8619];
        int offset;
        for (offset = 0; offset < temp_array.length; offset++)
            game_data_to_save[offset] = temp_array[offset];

        byte[][][] floors = current_game.get_game();
        for (int a = 0; a < 51; a++) {
            for (int b = 0; b < 13; b++) {
                for (int c = 0; c < 13; c++) {
                    game_data_to_save[offset] = floors[a][b][c];
                    offset++;
                }
            }
        }
        //*/
    }

    // load game from save file
    private void load_game(byte[] game_data_to_load) {
        int offset = 0;
        StringBuilder temp_sb = new StringBuilder();
        while(game_data_to_load[offset] != 10 && offset < game_data_to_load.length){
            temp_sb.append((char)game_data_to_load[offset]);
            offset++;
        } offset++;
        //Log.v(TAG, "hp " + temp_sb.toString());
        hp = Integer.parseInt(temp_sb.toString());
        temp_sb.delete(0,temp_sb.length());
        while(game_data_to_load[offset] != 10 && offset < game_data_to_load.length){
            temp_sb.append((char)game_data_to_load[offset]);
            offset++;
        } offset++;
        atk = Integer.parseInt(temp_sb.toString());
        temp_sb.delete(0,temp_sb.length());
        while(game_data_to_load[offset] != 10 && offset < game_data_to_load.length){
            temp_sb.append((char)game_data_to_load[offset]);
            offset++;
        } offset++;
        def = Integer.parseInt(temp_sb.toString());
        temp_sb.delete(0,temp_sb.length());
        while(game_data_to_load[offset] != 10 && offset < game_data_to_load.length){
            temp_sb.append((char)game_data_to_load[offset]);
            offset++;
        } offset++;
        gold = Integer.parseInt(temp_sb.toString());
        temp_sb.delete(0,temp_sb.length());
        while(game_data_to_load[offset] != 10 && offset < game_data_to_load.length){
            temp_sb.append((char)game_data_to_load[offset]);
            offset++;
        } offset++;
        floor_num = Integer.parseInt(temp_sb.toString());
        temp_sb.delete(0,temp_sb.length());
        while(game_data_to_load[offset] != 10 && offset < game_data_to_load.length){
            temp_sb.append((char)game_data_to_load[offset]);
            offset++;
        } offset++;
        count_y = Integer.parseInt(temp_sb.toString());
        temp_sb.delete(0,temp_sb.length());
        while(game_data_to_load[offset] != 10 && offset < game_data_to_load.length){
            temp_sb.append((char)game_data_to_load[offset]);
            offset++;
        } offset++;
        count_b = Integer.parseInt(temp_sb.toString());
        temp_sb.delete(0,temp_sb.length());
        while(game_data_to_load[offset] != 10 && offset < game_data_to_load.length){
            temp_sb.append((char)game_data_to_load[offset]);
            offset++;
        } offset++;
        count_r = Integer.parseInt(temp_sb.toString());
        temp_sb.delete(0,temp_sb.length());
        while(game_data_to_load[offset] != 10 && offset < game_data_to_load.length){
            temp_sb.append((char)game_data_to_load[offset]);
            offset++;
        } offset++;
        hero_x = Integer.parseInt(temp_sb.toString());
        temp_sb.delete(0,temp_sb.length());
        while(game_data_to_load[offset] != 10 && offset < game_data_to_load.length){
            temp_sb.append((char)game_data_to_load[offset]);
            offset++;
        } offset++;
        hero_y = Integer.parseInt(temp_sb.toString());
        temp_sb.delete(0,temp_sb.length());
        while(game_data_to_load[offset] != 10 && offset < game_data_to_load.length){
            temp_sb.append((char)game_data_to_load[offset]);
            offset++;
        } offset++;
        temp_x = Integer.parseInt(temp_sb.toString());
        temp_sb.delete(0,temp_sb.length());
        while(game_data_to_load[offset] != 10 && offset < game_data_to_load.length){
            temp_sb.append((char)game_data_to_load[offset]);
            offset++;
        } offset++;
        temp_y = Integer.parseInt(temp_sb.toString());
        temp_sb.delete(0,temp_sb.length());
        while(game_data_to_load[offset] != 10 && offset < game_data_to_load.length){
            temp_sb.append((char)game_data_to_load[offset]);
            offset++;
        } offset++;
        price_idx = Integer.parseInt(temp_sb.toString());
        temp_sb.delete(0,temp_sb.length());
        while(game_data_to_load[offset] != 10 && offset < game_data_to_load.length){
            temp_sb.append((char)game_data_to_load[offset]);
            offset++;
        } offset++;
        count_wing = Integer.parseInt(temp_sb.toString());
        temp_sb.delete(0,temp_sb.length());
        while(game_data_to_load[offset] != 10 && offset < game_data_to_load.length){
            temp_sb.append((char)game_data_to_load[offset]);
            offset++;
        } offset++;
        act = Integer.parseInt(temp_sb.toString());
        temp_sb.delete(0,temp_sb.length());
        while(game_data_to_load[offset] != 10 && offset < game_data_to_load.length){
            temp_sb.append((char)game_data_to_load[offset]);
            offset++;
        } offset++;
        highest_floor = Integer.parseInt(temp_sb.toString());
        temp_sb.delete(0,temp_sb.length());
        while(game_data_to_load[offset] != 10 && offset < game_data_to_load.length){
            temp_sb.append((char)game_data_to_load[offset]);
            offset++;
        } offset++;
        thief_event_count = Integer.parseInt(temp_sb.toString());
        temp_sb.delete(0,temp_sb.length());
        stf_wsdm      = (game_data_to_load[offset] == '1');  offset += 2;
        stf_echo      = (game_data_to_load[offset] == '1');  offset += 2;
        stf_space     = (game_data_to_load[offset] == '1');  offset += 2;
        cross         = (game_data_to_load[offset] == '1');  offset += 2;
        elixir        = (game_data_to_load[offset] == '1');  offset += 2;
        m_mattock     = (game_data_to_load[offset] == '1');  offset += 2;
        wing_cent     = (game_data_to_load[offset] == '1');  offset += 2;
        e_mattock     = (game_data_to_load[offset] == '1');  offset += 2;
        bomb          = (game_data_to_load[offset] == '1');  offset += 2;
        wing_up       = (game_data_to_load[offset] == '1');  offset += 2;
        key_enhac     = (game_data_to_load[offset] == '1');  offset += 2;
        wing_down     = (game_data_to_load[offset] == '1');  offset += 2;
        lucky_gold    = (game_data_to_load[offset] == '1');  offset += 2;
        dragonsbane   = (game_data_to_load[offset] == '1');  offset += 2;
        snow_cryst    = (game_data_to_load[offset] == '1');  offset += 2;
        sacred_shield = (game_data_to_load[offset] == '1');  offset += 2;
        for (int i = 0; i < 12; i++) {
            merchant_history[i] = game_data_to_load[offset] - '0';
            offset++;
        }
        for (int j = 0; j < 19; j++) {
            saint_history[j] = game_data_to_load[offset] - '0';
            offset++;
        }
        for (int k = 0; k < 24; k++) {
            echo_history[k] = game_data_to_load[offset] - '0';
            offset++;
        }
        byte[][][] temp_floors = new byte[51][13][13];
        for (int a = 0; a < 51; a++) {
            for (int b = 0; b < 13; b++) {
                for (int c = 0; c < 13; c++) {
                    temp_floors[a][b][c] = game_data_to_load[offset];
                    offset++;
                }
            }
        }
        current_game = new Floors(temp_floors);
    }

    // interactions with npc: thief, saint, merchant, altar, and princess)
    public void checkNextPosition(View v, int j, int i) {
        //Log.v(TAG, "next called  " + String.valueOf(current_floor[i][j]));
        switch (current_floor[i][j]) {
            case 21:        // thief
                AlertDialog.Builder thief_builder = new AlertDialog.Builder(v.getContext());
                if (thief_event_count == 0) {
                    thief_event_count++;
                    thief_builder.setMessage(R.string.thief0);
                } else if (thief_event_count == 1) {
                    thief_event_count++;
                    thief_builder.setMessage(R.string.thief1);
                } else if (thief_event_count == 2) {
                    thief_event_count++;
                    thief_builder.setMessage(R.string.thief2);
                } else if (thief_event_count == 3) {
                    thief_event_count++;
                    thief_builder.setMessage(R.string.thief3);
                } else if (thief_event_count == 4) {
                    thief_event_count++;
                    thief_builder.setMessage(R.string.thief4);
                } else if (thief_event_count == 5) {
                    if (floor_num == 29)
                        thief_builder.setMessage(R.string.thief4);
                    else {
                        thief_event_count = 6;
                        thief_builder.setMessage(R.string.thief5);
                        current_game.change_35f();
                    }
                } else if (thief_event_count == 6) {
                    if (floor_num == 2)
                        thief_builder.setMessage(R.string.thief6_1);
                    else {
                        thief_event_count++;
                        thief_builder.setMessage(R.string.thief6_2);
                    }
                } else if (thief_event_count == 7) {
                    thief_event_count++;
                    thief_builder.setMessage(R.string.thief7);
                } else if (thief_event_count == 8) {
                    thief_event_count++;
                    thief_builder.setMessage(R.string.thief8);
                } else if (thief_event_count == 9) {
                    thief_event_count++;
                    thief_builder.setMessage(R.string.thief9);
                } else if (thief_event_count == 10) {
                    thief_event_count++;
                    thief_builder.setMessage(R.string.thief10);
                } else if (thief_event_count == 11) {
                    thief_event_count++;
                    thief_builder.setMessage(R.string.thief11);
                } else {
                    thief_event_count = 0;
                    thief_builder.setMessage(R.string.thief12);
                }
                AlertDialog thief_dialog = thief_builder.create();
                thief_dialog.setCanceledOnTouchOutside(true);
                thief_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        proceed = true;
                    }
                });
                isEvent = true;
                thief_dialog.show();
                break;
            case 22:        // saint
                AlertDialog.Builder saint_builder = new AlertDialog.Builder(v.getContext());
                switch (floor_num) {
                    case 2:
                        if (saint_history[0] == 0) {
                            saint_builder.setMessage(R.string.saint_2f);
                            saint_builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    atk = atk * 103 / 100;
                                    def = def * 103 / 100;
                                    saint_history[0]++;
                                }
                            });
                            saint_builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                        } else
                            saint_builder.setMessage(R.string.saint_default);
                        break;
                    case 3:
                        if (saint_history[1] == 0) {
                            saint_history[1]++;
                            saint_builder.setMessage(R.string.saint_3f);
                            stf_wsdm = true;
                        } else
                            saint_builder.setMessage(R.string.saint_default);
                        break;
                    case 4:
                        if (saint_history[2] == 0) {
                            saint_history[2]++;
                            echo_history[0]++;
                            saint_builder.setMessage(R.string.saint_4f);
                        } else
                            saint_builder.setMessage(R.string.saint_default);
                        break;
                    case 6:
                        if (saint_history[3] == 0) {
                            saint_history[3]++;
                            echo_history[1]++;
                            saint_builder.setMessage(R.string.saint_6f);
                        } else
                            saint_builder.setMessage(R.string.saint_default);
                        break;
                    case 16:
                        if (j == 1)
                            if (saint_history[4] == 0) {
                                saint_history[4]++;
                                echo_history[6]++;
                                saint_builder.setMessage(R.string.saint_16f);
                            } else
                                saint_builder.setMessage(R.string.saint_default);
                        else {
                            if (saint_history[5] == 0) {
                                saint_history[5]++;
                                saint_builder.setMessage(R.string.saint_16fh);
                                elixir = true;
                            } else
                                saint_builder.setMessage(R.string.saint_default);
                        }
                        break;
                    case 18:
                        if (saint_history[6] == 0) {
                            saint_history[6]++;
                            echo_history[7]++;
                            saint_builder.setMessage(R.string.saint_18f);
                        } else
                            saint_builder.setMessage(R.string.saint_default);
                        break;
                    case 21:
                        if (saint_history[7] == 0) {
                            saint_history[7]++;
                            echo_history[8]++;
                            saint_builder.setMessage(R.string.saint_21f);
                        } else
                            saint_builder.setMessage(R.string.saint_default);
                        break;
                    case 23:
                        if (saint_history[8] == 0) {
                            saint_builder.setMessage(R.string.saint_23f0);
                        } else
                            saint_builder.setMessage(R.string.saint_23f1);
                        break;
                    case 27:
                        if (saint_history[9] == 0) {
                            saint_history[9]++;
                            echo_history[9]++;
                            saint_builder.setMessage(R.string.saint_27f);
                        } else
                            saint_builder.setMessage(R.string.saint_default);
                        break;
                    case 31:
                        if (saint_history[10] == 0) {
                            saint_history[10]++;
                            echo_history[10]++;
                            saint_builder.setMessage(R.string.saint_31f);
                        } else
                            saint_builder.setMessage(R.string.saint_default);
                        break;
                    case 33:
                        if (saint_history[11] == 0) {
                            saint_history[11]++;
                            echo_history[12]++;
                            saint_builder.setMessage(R.string.saint_33f);
                        } else
                            saint_builder.setMessage(R.string.saint_default);
                        break;
                    case 36:
                        if (saint_history[12] == 0) {
                            saint_history[12]++;
                            echo_history[13]++;
                            saint_builder.setMessage(R.string.saint_36f);
                        } else
                            saint_builder.setMessage(R.string.saint_default);
                        break;
                    case 37:
                        if (saint_history[13] == 0) {
                            saint_history[13]++;
                            echo_history[14]++;
                            saint_builder.setMessage(R.string.saint_37f);
                        } else
                            saint_builder.setMessage(R.string.saint_default);
                        break;
                    case 39:
                        if (saint_history[14] == 0) {
                            saint_history[14]++;
                            echo_history[16]++;
                            saint_builder.setMessage(R.string.saint_39f);
                        } else
                            saint_builder.setMessage(R.string.saint_default);
                        break;
                    case 42:
                        if (saint_history[15] == 0) {
                            saint_history[15]++;
                            echo_history[18]++;
                            saint_builder.setMessage(R.string.saint_42f);
                        } else
                            saint_builder.setMessage(R.string.saint_default);
                        break;
                    case 45:
                        if (saint_history[16] == 0) {
                            saint_history[16]++;
                            echo_history[19]++;
                            saint_builder.setMessage(R.string.saint_45f);
                        } else
                            saint_builder.setMessage(R.string.saint_default);
                        break;
                    case 46:
                        if (saint_history[17] == 0) {
                            saint_history[17]++;
                            echo_history[21]++;
                            saint_builder.setMessage(R.string.saint_46f);
                        } else
                            saint_builder.setMessage(R.string.saint_default);
                        break;
                    case 48:
                        if (saint_history[18] == 0) {
                            saint_history[18]++;
                            echo_history[23]++;
                            saint_builder.setMessage(R.string.saint_48f);
                        } else
                            saint_builder.setMessage(R.string.saint_default);
                        break;
                    default:
                        saint_builder.setMessage("Saint dialog bug in checkNextPostion()");
                        break;
                }
                AlertDialog saint_dialog = saint_builder.create();
                saint_dialog.setCanceledOnTouchOutside(true);
                saint_dialog.show();
                break;
            case 23:        // merchant
                final AlertDialog.Builder merchant_builder = new AlertDialog.Builder(v.getContext());
                final AlertDialog.Builder no_gold_builder = new AlertDialog.Builder(v.getContext());
                switch (floor_num) {
                    case 2:
                        if (merchant_history[0] == 0) {
                            merchant_history[0]++;
                            merchant_builder.setMessage(R.string.merchant_2f0);
                            gold += 1000;
                        } else {
                            merchant_builder.setMessage(R.string.merchant_2f1);
                        }
                        break;
                    case 6:
                        if (merchant_history[1] == 0) {
                            merchant_builder.setMessage(R.string.merchant_6f0);
                            merchant_builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (gold >= 50) {
                                        merchant_history[1]++;
                                        gold -= 50;
                                        count_b++;
                                    } else {
                                        no_gold_builder.setMessage(R.string.purchase_fail);
                                        AlertDialog no_gold_dialog = no_gold_builder.create();
                                        no_gold_dialog.show();
                                    }
                                }
                            });
                            merchant_builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                        } else if (merchant_history[1] == 1) {
                            merchant_history[1]++;
                            echo_history[2]++;
                            merchant_builder.setMessage(R.string.merchant_6f1);
                        } else {
                            merchant_builder.setMessage(R.string.merchant_default);
                        }
                        break;
                    case 7:
                        if (merchant_history[2] == 0) {
                            merchant_builder.setMessage(R.string.merchant_7f0);
                            merchant_builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (gold >= 50) {
                                        merchant_history[2]++;
                                        gold -= 50;
                                        count_y += 5;
                                    } else {
                                        no_gold_builder.setMessage(R.string.purchase_fail);
                                        AlertDialog no_gold_dialog = no_gold_builder.create();
                                        no_gold_dialog.show();
                                    }
                                }
                            });
                            merchant_builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                        } else if (merchant_history[2] == 1) {
                            merchant_history[2]++;
                            echo_history[3]++;
                            merchant_builder.setMessage(R.string.merchant_7f1);
                        } else {
                            merchant_builder.setMessage(R.string.merchant_default);
                        }
                        break;
                    case 12:
                        if (hero_x < 6) {
                            if (merchant_history[3] == 0) {
                                merchant_builder.setMessage(R.string.merchant_12f0);
                                merchant_builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (gold >= 800) {
                                            merchant_history[3]++;
                                            gold -= 800;
                                            count_r++;
                                        } else {
                                            no_gold_builder.setMessage(R.string.purchase_fail);
                                            AlertDialog no_gold_dialog = no_gold_builder.create();
                                            no_gold_dialog.show();
                                        }
                                    }
                                });
                                merchant_builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                            } else if (merchant_history[3] == 1) {
                                merchant_history[3]++;
                                echo_history[4]++;
                                merchant_builder.setMessage(R.string.merchant_12f1);
                            } else {
                                merchant_builder.setMessage(R.string.merchant_default);
                            }
                        } else {
                            merchant_builder.setMessage(R.string.merchant_12fh);
                            merchant_builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (gold >= 1000) {
                                        merchant_history[4]++;
                                        gold -= 1000;
                                        count_y++;
                                    } else {
                                        no_gold_builder.setMessage(R.string.purchase_fail);
                                        AlertDialog no_gold_dialog = no_gold_builder.create();
                                        no_gold_dialog.show();
                                    }
                                }
                            });
                            merchant_builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                        }
                        break;
                    case 15:
                        if (merchant_history[5] == 0) {
                            merchant_builder.setMessage(R.string.merchant_15f0);
                            merchant_builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (gold >= 200) {
                                        merchant_history[5]++;
                                        gold -= 200;
                                        count_b++;
                                    } else {
                                        no_gold_builder.setMessage(R.string.purchase_fail);
                                        AlertDialog no_gold_dialog = no_gold_builder.create();
                                        no_gold_dialog.show();
                                    }
                                }
                            });
                            merchant_builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                        } else if (merchant_history[5] == 1) {
                            merchant_history[5]++;
                            echo_history[5]++;
                            merchant_builder.setMessage(R.string.merchant_15f1);
                        } else {
                            merchant_builder.setMessage(R.string.merchant_default);
                        }
                        break;
                    case 28:
                        merchant_builder.setMessage(R.string.merchant_28f);
                        merchant_builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (count_y > 0) {
                                    gold += 100;
                                    count_y--;
                                } else {
                                    no_gold_builder.setMessage(R.string.purchase_fail_28f);
                                    AlertDialog no_gold_dialog = no_gold_builder.create();
                                    no_gold_dialog.show();
                                }
                            }
                        });
                        merchant_builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        break;
                    case 31:
                        if (merchant_history[7] == 0) {
                            merchant_builder.setMessage(R.string.merchant_31f0);
                            merchant_builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (gold >= 1000) {
                                        merchant_history[7]++;
                                        gold -= 1000;
                                        count_y += 4;
                                        count_b++;
                                    } else {
                                        no_gold_builder.setMessage(R.string.purchase_fail);
                                        AlertDialog no_gold_dialog = no_gold_builder.create();
                                        no_gold_dialog.show();
                                    }
                                }
                            });
                            merchant_builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                        } else if (merchant_history[7] == 1) {
                            merchant_history[7]++;
                            echo_history[11]++;
                            merchant_builder.setMessage(R.string.merchant_31f1);
                        } else {
                            merchant_builder.setMessage(R.string.merchant_default);
                        }
                        break;
                    case 38:
                        if (merchant_history[8] == 0) {
                            merchant_builder.setMessage(R.string.merchant_38f0);
                            merchant_builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (gold >= 200) {
                                        merchant_history[8]++;
                                        gold -= 200;
                                        count_y += 3;
                                    } else {
                                        no_gold_builder.setMessage(R.string.purchase_fail);
                                        AlertDialog no_gold_dialog = no_gold_builder.create();
                                        no_gold_dialog.show();
                                    }
                                }
                            });
                            merchant_builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                        } else if (merchant_history[8] == 1) {
                            merchant_history[8]++;
                            echo_history[15]++;
                            merchant_builder.setMessage(R.string.merchant_38f1);
                        } else {
                            merchant_builder.setMessage(R.string.merchant_default);
                        }
                        break;
                    case 39:
                        if (merchant_history[9] == 0) {
                            merchant_builder.setMessage(R.string.merchant_39f0);
                            merchant_builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (gold >= 2000) {
                                        merchant_history[9]++;
                                        gold -= 2000;
                                        count_b += 3;
                                    } else {
                                        no_gold_builder.setMessage(R.string.purchase_fail);
                                        AlertDialog no_gold_dialog = no_gold_builder.create();
                                        no_gold_dialog.show();
                                    }
                                }
                            });
                            merchant_builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                        } else if (merchant_history[9] == 1) {
                            merchant_history[9]++;
                            echo_history[17]++;
                            merchant_builder.setMessage(R.string.merchant_39f1);
                        } else {
                            merchant_builder.setMessage(R.string.merchant_default);
                        }
                        break;
                    case 45:
                        if (merchant_history[10] == 0) {
                            merchant_builder.setMessage(R.string.merchant_45f0);
                            merchant_builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (gold >= 1000) {
                                        merchant_history[10]++;
                                        gold -= 1000;
                                        hp += 2000;
                                    } else {
                                        no_gold_builder.setMessage(R.string.purchase_fail);
                                        AlertDialog no_gold_dialog = no_gold_builder.create();
                                        no_gold_dialog.show();
                                    }
                                }
                            });
                            merchant_builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                        } else if (merchant_history[10] == 1) {
                            merchant_history[10]++;
                            echo_history[20]++;
                            merchant_builder.setMessage(R.string.merchant_45f1);
                        } else {
                            merchant_builder.setMessage(R.string.merchant_default);
                        }
                        break;
                    case 47:
                        if (merchant_history[11] == 0) {
                            merchant_builder.setMessage(R.string.merchant_47f0);
                            merchant_builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (gold >= 4000) {
                                        merchant_history[11]++;
                                        gold -= 4000;
                                        e_mattock = true;
                                    } else {
                                        no_gold_builder.setMessage(R.string.purchase_fail);
                                        AlertDialog no_gold_dialog = no_gold_builder.create();
                                        no_gold_dialog.show();
                                    }
                                }
                            });
                            merchant_builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                        } else if (merchant_history[11] == 1) {
                            merchant_history[11]++;
                            echo_history[22]++;
                            merchant_builder.setMessage(R.string.merchant_47f1);
                        } else {
                            merchant_builder.setMessage(R.string.merchant_default);
                        }
                        break;
                    default:
                        merchant_builder.setMessage("Merchant dialog bug in checkNextPostion()");
                        break;
                }
                AlertDialog merchant_dialog = merchant_builder.create();
                merchant_dialog.show();
                break;
            case 26:        // altar
                final int price = 10 * price_idx * price_idx + 10 * price_idx + 20;
                String item_atk = "Attack + " + String.valueOf(2 + 2 * (floor_num / 10));
                String item_def = "Defence + " + String.valueOf(4 + 4 * (floor_num / 10));
                String offer = "Would you like to offer " + String.valueOf(price) + " gold for one of the following items";
                String[] item_list = {"Health + 1000", item_atk, item_def, "Not this time"};
                final AlertDialog.Builder altar_builder = new AlertDialog.Builder(v.getContext());
                final AlertDialog.Builder fail_offer_builder = new AlertDialog.Builder(v.getContext());
                altar_builder.setTitle(offer);
                altar_builder.setItems(item_list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            if (gold > price) {
                                gold -= price;
                                price_idx++;
                                hp += 1000;
                                sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_choose);
                                if (music_settings[1])
                                    sfx_music.start();
                            } else {
                                sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_cancel);
                                if (music_settings[1])
                                    sfx_music.start();
                                fail_offer_builder.setMessage(R.string.purchase_fail);
                                AlertDialog no_gold_dialog = fail_offer_builder.create();
                                no_gold_dialog.show();
                            }
                        } else if (item == 1) {
                            if (gold > price) {
                                sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_choose);
                                if (music_settings[1])
                                    sfx_music.start();
                                gold -= price;
                                price_idx++;
                                atk += (2 + 2 * (floor_num / 10));
                            } else {
                                sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_cancel);
                                if (music_settings[1])
                                    sfx_music.start();
                                fail_offer_builder.setMessage(R.string.purchase_fail);
                                AlertDialog no_gold_dialog = fail_offer_builder.create();
                                no_gold_dialog.show();
                            }
                        } else if (item == 2) {
                            if (gold > price) {
                                sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_choose);
                                if (music_settings[1])
                                    sfx_music.start();
                                gold -= price;
                                price_idx++;
                                def += (4 + 4 * (floor_num / 10));
                            } else {
                                sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_cancel);
                                if (music_settings[1])
                                    sfx_music.start();
                                fail_offer_builder.setMessage(R.string.purchase_fail);
                                AlertDialog no_gold_dialog = fail_offer_builder.create();
                                no_gold_dialog.show();
                            }
                        } else {
                            sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_cancel);
                            if (music_settings[1])
                                sfx_music.start();
                        }
                    }
                });
                AlertDialog altar_list = altar_builder.create();
                altar_list.show();
                break;
            case 28:        // princess
                AlertDialog.Builder princess_builder = new AlertDialog.Builder(v.getContext());
                final AlertDialog.Builder zeno_builder = new AlertDialog.Builder(v.getContext());
                princess_builder.setMessage(R.string.princess_dialog1);
                AlertDialog princess_dialog = princess_builder.create();
                princess_dialog.setCanceledOnTouchOutside(true);
                princess_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        current_game.change_24f();
                        zeno_builder.setMessage(R.string.princess_dialog2);
                        AlertDialog zeno_dialog = zeno_builder.create();
                        zeno_dialog.setCanceledOnTouchOutside(true);
                        zeno_dialog.show();
                    }
                });
                princess_dialog.show();
                break;
            default:
                break;
        }
    }

    // calculate the damage inflict to hero during battle
    public int damage_calculation(int idx){
        int initial_hp = hp;
        int a_damage_b;
        if (cross && (idx == 11 || idx == 13 || idx == 14))
             a_damage_b = 2*atk - m_table[idx][2];
        else if (dragonsbane && idx == 32)
            a_damage_b = 2*atk - m_table[idx][2];
        else
            a_damage_b = atk - m_table[idx][2];
        int b_damage_a = m_table[idx][1] - def;
        int ahp = hp, bhp = m_table[idx][0];
        boolean a_attack = true;
        if (a_damage_b <= 0) {
            // don't have enough attack to damage the monster
            return 987654321;
        } else if (b_damage_a <= 0) {
            // monster can't damage hero
            return 0;
        } else {
            while (ahp > 0 && bhp > 0) {
                if (a_attack)
                    bhp -= a_damage_b;
                else {
                    ahp -= b_damage_a;
                }
                a_attack = !a_attack;
            }
        }
        return (initial_hp-ahp);
    }

    public class GameView extends SurfaceView implements Runnable {

        private Thread t = null;
        private SurfaceHolder holder;
        private boolean isItOK = false;

        public GameView(Gamelogic in_parent) {
            super(in_parent);
            parent = in_parent;
            holder = getHolder();
        }

        @Override
        public boolean performClick() {
            super.performClick();
            if (total_page == 1 || page == 1) {
                total_page = 1;
                page = 0;
                which_surface_view = true;
                return true;
            } else {
                page++;
                return true;
            }
        }

        public void pause() {
            isItOK = false;
            while (true) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
            t = null;
        }

        public void resume() {
            isItOK = true;
            t = new Thread(this);
            t.start();
        }

        public void run() {

            //Log.v(TAG, "tell me something");
            while (true) {
                if (!holder.getSurface().isValid()) {
                    continue;
                }
                Canvas canvas = holder.lockCanvas();
                Log.v(TAG, "width = " + String.valueOf(canvas.getWidth()) + "  \nHeight =" +  String.valueOf(canvas.getHeight()));
                sq_size = canvas.getWidth() / 12;
                extra_height = canvas.getHeight() - sq_size * 19;
                holder.unlockCanvasAndPost(canvas);
                break;
            }
            resize_bitmaps();

            int xx = hero_x * sq_size - sq_size / 2;
            int yy = hero_y * sq_size - sq_size / 2;
            hero_sprite = new Sprite(GameView.this, hero, xx, yy, (byte) 30);
            red_star_sprite = new Sprite(GameView.this, t_r_star, xx, yy, (byte) 10);
            octopus_sprite = new Sprite(GameView.this, m__octopus, 5 * sq_size - sq_size/2, 4 * sq_size - sq_size/2, (byte) 62);
            dragon_sprite = new Sprite(GameView.this, m___dragon, 5 * sq_size - sq_size/2, 5 * sq_size - sq_size/2, (byte) 64);

            while (isItOK) {
                if (!holder.getSurface().isValid()) {
                    continue;
                }
                if (which_surface_view) {
                    if (!isWalk) {
                        if (button_click && !isBattle && !isEvent && !cantMove)
                            button_logic(which_button);
                        if (isBattle)
                            battle_animation(hero_attack, hero_y, hero_x);
                        else {
                            cantMove = false;
                            show_hero = !not_show_hero;
                            show_fight = false;
                        }
                        if (isEvent && !isBattle && !cantMove)
                            event();
                    }
                    if (refresh_ctr) {
                        current_floor = current_game.get_one_floor(floor_num);
                        load_objects(current_floor);
                        refresh_ctr = false;
                    }
                    Canvas c = holder.lockCanvas();
                    this.draw_game(c);
                    holder.unlockCanvasAndPost(c);
                } else {
                    if (load_ctr) {
                        find_monsters_on_current_floor();
                        load_ctr = false;
                    }
                    Canvas c = holder.lockCanvas();
                    this.draw_staff_of_wisdom_results(c);
                    holder.unlockCanvasAndPost(c);
                }
            }
        }

        private void resize_bitmaps() {
            menu_health = createScaledBitmap(menu_health, sq_size, sq_size, false);
            menu_gold = createScaledBitmap(menu_gold, sq_size, sq_size, false);
            menu_background = createScaledBitmap(menu_background, sq_size, sq_size, false);
            menu_up = createScaledBitmap(menu_up, sq_size * 2 - sq_size / 2, sq_size * 2 - sq_size / 2, false);
            menu_down = createScaledBitmap(menu_down, sq_size * 2 - sq_size / 2, sq_size * 2 - sq_size / 2, false);
            menu_left = createScaledBitmap(menu_left, sq_size * 2 - sq_size / 2, sq_size * 2 - sq_size / 2, false);
            menu_right = createScaledBitmap(menu_right, sq_size * 2 - sq_size / 2, sq_size * 2 - sq_size / 2, false);
            menu_stf1 = createScaledBitmap(i_stf_wsdm, sq_size * 2 - sq_size / 2, sq_size * 2 - sq_size / 2, false);
            menu_stf2 = createScaledBitmap(i_stf_echo, sq_size * 2 - sq_size / 2, sq_size * 2 - sq_size / 2, false);
            menu_stf3 = createScaledBitmap(i_stf_spce, sq_size * 2 - sq_size / 2, sq_size * 2 - sq_size / 2, false);
            t__floor = createScaledBitmap(t__floor, sq_size, sq_size, false);
            t___wall = createScaledBitmap(t___wall, sq_size, sq_size, false);
            t___star = createScaledBitmap(t___star, sq_size, sq_size, false);
            t_ustair = createScaledBitmap(t_ustair, sq_size, sq_size, false);
            t_dstair = createScaledBitmap(t_dstair, sq_size, sq_size, false);
            t_door_y = createScaledBitmap(t_door_y, sq_size, sq_size, false);
            t_door_b = createScaledBitmap(t_door_b, sq_size, sq_size, false);
            t_door_r = createScaledBitmap(t_door_r, sq_size, sq_size, false);
            t_door_m = createScaledBitmap(t_door_m, sq_size, sq_size, false);
            t_prison = createScaledBitmap(t_prison, sq_size, sq_size, false);
            t___logo = createScaledBitmap(t___logo, sq_size, sq_size, false);
            t_r_star = createScaledBitmap(t_r_star, sq_size, sq_size, false);
            w___ironw = createScaledBitmap(w___ironw, sq_size, sq_size, false);
            w___ironh = createScaledBitmap(w___ironh, sq_size, sq_size, false);
            w_silverw = createScaledBitmap(w_silverw, sq_size, sq_size, false);
            w_silverh = createScaledBitmap(w_silverh, sq_size, sq_size, false);
            w_knightw = createScaledBitmap(w_knightw, sq_size, sq_size, false);
            w_knighth = createScaledBitmap(w_knighth, sq_size, sq_size, false);
            w_divinew = createScaledBitmap(w_divinew, sq_size, sq_size, false);
            w_divineh = createScaledBitmap(w_divineh, sq_size, sq_size, false);
            w_sacredw = createScaledBitmap(w_sacredw, sq_size, sq_size, false);
            w_sacredh = createScaledBitmap(w_sacredh, sq_size, sq_size, false);
            n___thief = createScaledBitmap(n___thief, sq_size * 3, sq_size, false);
            n___saint = createScaledBitmap(n___saint, sq_size * 3, sq_size, false);
            n_merchat = createScaledBitmap(n_merchat, sq_size * 3, sq_size, false);
            n___fairy = createScaledBitmap(n___fairy, sq_size * 3, sq_size, false);
            n_shop__l = createScaledBitmap(n_shop__l, sq_size, sq_size, false);
            n_shop__m = createScaledBitmap(n_shop__m, sq_size * 3, sq_size, false);
            n_shop__r = createScaledBitmap(n_shop__r, sq_size, sq_size, false);
            n_princes = createScaledBitmap(n_princes, sq_size * 3, sq_size, false);
            n____lava = createScaledBitmap(n____lava, sq_size * 4, sq_size, false);
            m__slime_g = createScaledBitmap(m__slime_g, sq_size * 3, sq_size, false);
            m__slime_r = createScaledBitmap(m__slime_r, sq_size * 3, sq_size, false);
            m_bat_fier = createScaledBitmap(m_bat_fier, sq_size * 3, sq_size, false);
            m___priest = createScaledBitmap(m___priest, sq_size * 3, sq_size, false);
            m_skeleton = createScaledBitmap(m_skeleton, sq_size * 3, sq_size, false);
            m_skelet_w = createScaledBitmap(m_skelet_w, sq_size * 3, sq_size, false);
            m_gatekeep = createScaledBitmap(m_gatekeep, sq_size * 3, sq_size, false);
            m_skelet_c = createScaledBitmap(m_skelet_c, sq_size * 3, sq_size, false);
            m__slime_b = createScaledBitmap(m__slime_b, sq_size * 3, sq_size, false);
            m_bat_gian = createScaledBitmap(m_bat_gian, sq_size * 3, sq_size, false);
            m_priest_m = createScaledBitmap(m_priest_m, sq_size * 3, sq_size, false);
            m___zombie = createScaledBitmap(m___zombie, sq_size * 3, sq_size, false);
            m_stone_gd = createScaledBitmap(m_stone_gd, sq_size * 3, sq_size, false);
            m_zombie_w = createScaledBitmap(m_zombie_w, sq_size * 3, sq_size, false);
            m__vampire = createScaledBitmap(m__vampire, sq_size * 3, sq_size, false);
            m__slime_m = createScaledBitmap(m__slime_m, sq_size * 3, sq_size, false);
            m_skelet_e = createScaledBitmap(m_skelet_e, sq_size * 3, sq_size, false);
            m___knight = createScaledBitmap(m___knight, sq_size * 3, sq_size, false);
            m_gatekp_e = createScaledBitmap(m_gatekp_e, sq_size * 3, sq_size, false);
            m_swordsmn = createScaledBitmap(m_swordsmn, sq_size * 3, sq_size, false);
            m_knight_e = createScaledBitmap(m_knight_e, sq_size * 3, sq_size, false);
            m_knight_c = createScaledBitmap(m_knight_c, sq_size * 3, sq_size, false);
            m_slimelod = createScaledBitmap(m_slimelod, sq_size * 3, sq_size, false);
            m_bat_vamp = createScaledBitmap(m_bat_vamp, sq_size * 3, sq_size, false);
            m_____mage = createScaledBitmap(m_____mage, sq_size * 3, sq_size, false);
            m_mage_mas = createScaledBitmap(m_mage_mas, sq_size * 3, sq_size, false);
            m_demo_sgt = createScaledBitmap(m_demo_sgt, sq_size * 3, sq_size, false);
            m_d_knight = createScaledBitmap(m_d_knight, sq_size * 3, sq_size, false);
            m_gate_gdn = createScaledBitmap(m_gate_gdn, sq_size * 3, sq_size, false);
            m_demozeno = createScaledBitmap(m_demozeno, sq_size * 3, sq_size, false);
            m__octopus = createScaledBitmap(m__octopus, sq_size * 9, sq_size * 3, false);
            m___dragon = createScaledBitmap(m___dragon, sq_size * 9, sq_size * 3, false);
            m_archmage = createScaledBitmap(m_archmage, sq_size * 3, sq_size, false);
            i____key_y = createScaledBitmap(i____key_y, sq_size, sq_size, false);
            i____key_b = createScaledBitmap(i____key_b, sq_size, sq_size, false);
            i____key_r = createScaledBitmap(i____key_r, sq_size, sq_size, false);
            i_potion_r = createScaledBitmap(i_potion_r, sq_size, sq_size, false);
            i_potion_b = createScaledBitmap(i_potion_b, sq_size, sq_size, false);
            i_crystl_r = createScaledBitmap(i_crystl_r, sq_size, sq_size, false);
            i_crystl_b = createScaledBitmap(i_crystl_b, sq_size, sq_size, false);
            i_stf_wsdm = createScaledBitmap(i_stf_wsdm, sq_size, sq_size, false);
            i_stf_echo = createScaledBitmap(i_stf_echo, sq_size, sq_size, false);
            i_stf_spce = createScaledBitmap(i_stf_spce, sq_size, sq_size, false);
            i____cross = createScaledBitmap(i____cross, sq_size, sq_size, false);
            i___elixir = createScaledBitmap(i___elixir, sq_size, sq_size, false);
            i_m_mattok = createScaledBitmap(i_m_mattok, sq_size, sq_size, false);
            i_wing_cen = createScaledBitmap(i_wing_cen, sq_size, sq_size, false);
            i_e_mattok = createScaledBitmap(i_e_mattok, sq_size, sq_size, false);
            i_____bomb = createScaledBitmap(i_____bomb, sq_size, sq_size, false);
            i__wing_up = createScaledBitmap(i__wing_up, sq_size, sq_size, false);
            i_key_ehac = createScaledBitmap(i_key_ehac, sq_size, sq_size, false);
            i_wing_dow = createScaledBitmap(i_wing_dow, sq_size, sq_size, false);
            i_lucky_gd = createScaledBitmap(i_lucky_gd, sq_size, sq_size, false);
            i_dra_bane = createScaledBitmap(i_dra_bane, sq_size, sq_size, false);
            i_snow_crs = createScaledBitmap(i_snow_crs, sq_size, sq_size, false);

            pic_debug = createScaledBitmap(pic_debug, sq_size * 3, sq_size, false);
            pic_d1 = createScaledBitmap(pic_d1, sq_size, sq_size, false);
            pic_d2 = createScaledBitmap(pic_d2, sq_size, sq_size, false);
            pic_d3 = createScaledBitmap(pic_d3, sq_size, sq_size, false);
            pic_dh = createScaledBitmap(pic_dh, sq_size, sq_size, false);
            hero = createScaledBitmap(hero, sq_size * 3, sq_size * 4, false);
        }

        private void battle_animation(boolean isHeroAttack, int i, int j) {
            cantMove = true;
            int a_damage_b;
            int b_damage_a = m_atk - def;
            if (cross && (m_atk == 85 || m_atk == 120 || m_atk == 199))
                a_damage_b = 2*atk - m_def;
            else if (dragonsbane && m_atk == 600)
                a_damage_b = 2*atk - m_def;
            else
                a_damage_b = atk - m_def;
            if (b_damage_a < 0)
                b_damage_a = 0;
            if (isHeroAttack) {
                show_hero = false;
                show_fight = true;
                m_hp -= a_damage_b;
                sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_fight);
                if (music_settings[1])
                    sfx_music.start();
            } else {
                show_hero = true;
                show_fight = false;
                hp -= b_damage_a;
            }
            hero_attack = !hero_attack;
            if (m_hp <= 0) {
                isBattle = false;
                if (current_floor[i][j] > 0)
                    current_floor[i][j] = 1;
                refresh_ctr = true;
                if (lucky_gold)
                    gold += m_gold * 2;
                else
                    gold += m_gold;
                check_complete();
            } else {
                isBattle = true;
            }
        }

        private void event() {
            switch (floor_num) {
                case 2:
                    if (thief_event_count == 1) {
                        if (act == 0) {
                            if (!proceed)
                                return;
                            proceed = false;
                            act++;
                            current_floor[7][2] = 1;
                            refresh_ctr = true;
                        } else if (act == 1) {
                            sleep(500);
                            act++;
                            current_floor[7][3] = 1;
                            current_floor[7][2] = 21;
                            refresh_ctr = true;
                        } else if (act == 2) {
                            sleep(500);
                            act++;
                            current_floor[7][2] = 1;
                            current_floor[7][1] = 21;
                            refresh_ctr = true;
                        } else {
                            sleep(500);
                            act = 0;
                            current_floor[7][1] = 1;
                            current_floor[8][1] = 21;
                            refresh_ctr = true;
                            isEvent = false;
                        }
                    } else if (thief_event_count == 2) {
                        if (act == 0) {
                            if (!proceed)
                                return;
                            proceed = false;
                            act++;
                            current_floor[8][1] = 1;
                            current_floor[9][1] = 21;
                            refresh_ctr = true;
                        } else if (act == 1) {
                            sleep(500);
                            act++;
                            current_floor[9][1] = 1;
                            current_floor[10][1] = 21;
                            refresh_ctr = true;
                        } else {
                            sleep(500);
                            act = 0;
                            current_floor[10][1] = 1;
                            refresh_ctr = true;
                            isEvent = false;
                        }
                    } else {
                        if (!proceed)
                            return;
                        proceed = false;
                        isEvent = false;
                    }
                    break;
                case 3:
                    if (act == 0) {
                        act++;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder f3_builder1 = new AlertDialog.Builder(parent);
                                f3_builder1.setMessage(R.string.zeno_3f);
                                AlertDialog f3_dialog1 = f3_builder1.create();
                                f3_dialog1.setCanceledOnTouchOutside(true);
                                f3_dialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        proceed = true;
                                        current_floor[9][5] = 1;
                                    }
                                });
                                f3_dialog1.show();
                            }
                        });
                    } else if (act == 1) {
                        if (!proceed)
                            return;
                        proceed = false;
                        act++;
                        current_floor[7][5] = 60;
                        refresh_ctr = true;
                    } else if (act == 2) {
                        sleep(500);
                        act++;
                        current_floor[8][5] = 57;
                        current_floor[10][5] = 57;
                        current_floor[9][4] = 57;
                        current_floor[9][6] = 57;
                        refresh_ctr = true;
                    } else if (act == 3) {
                        sleep(500);
                        act++;
                        show_fight = true;
                        hp = 0;
                    } else if (act == 4) {
                        sleep(500);
                        act++;
                        show_fight = false;
                    } else {
                        sleep(500);
                        act = 0;
                        floor_num = 2;
                        hero_x = 4;
                        hero_y = 8;
                        hp = 400;
                        atk = 10;
                        def = 10;
                        current_floor[7][5] = 1;
                        current_floor[8][5] = 1;
                        current_floor[10][5] = 1;
                        current_floor[9][4] = 1;
                        current_floor[9][6] = 1;
                        refresh_ctr = true;
                        isEvent = false;
                        blackout = true;
                    }
                    break;
                case 10:
                    if (act == 0) {
                        act++;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder f10_builder1 = new AlertDialog.Builder(parent);
                                f10_builder1.setMessage(R.string.skeleton_captain_10f_1);
                                AlertDialog f10_dialog1 = f10_builder1.create();
                                f10_dialog1.setCanceledOnTouchOutside(true);
                                f10_dialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        proceed = true;
                                        current_floor[5][6] = 1;
                                    }
                                });
                                f10_dialog1.show();
                            }
                        });
                    } else if (act == 1) {
                        if (!proceed)
                            return;
                        proceed = false;
                        if (background_music != null)           // kill the old music first
                            background_music.release();
                        background_music = MediaPlayer.create(getApplicationContext(), boss_music_list[0]);
                        background_music.setLooping(true);
                        if (music_settings[0])                  // if bgm option is checked
                            background_music.start();
                        act++;
                        current_floor[4][6] = 1;
                        current_floor[3][6] = 38;
                        current_floor[6][5] = 1;
                        current_floor[6][7] = 1;
                        refresh_ctr = true;
                    } else if (act == 2) {
                        sleep(200);
                        act++;
                        current_floor[3][6] = 8;
                        current_floor[7][6] = 8;
                        current_floor[2][6] = 38;
                        refresh_ctr = true;
                    } else if (act == 3) {
                        sleep(200);
                        act++;
                        current_floor[4][4] = 1;
                        current_floor[4][8] = 1;
                        current_floor[2][6] = 1;
                        current_floor[1][6] = 38;
                        current_floor[4][2] = 1;
                        current_floor[4][3] = 36;
                        current_floor[4][10] = 1;
                        current_floor[4][9] = 36;
                        refresh_ctr = true;
                    } else if (act == 4) {
                        sleep(200);
                        act++;
                        current_floor[4][4] = 36;
                        current_floor[4][8] = 36;
                        current_floor[4][3] = 1;
                        current_floor[4][9] = 1;
                        refresh_ctr = true;
                    } else if (act == 5) {
                        sleep(200);
                        act++;
                        current_floor[4][5] = 36;
                        current_floor[4][7] = 36;
                        current_floor[4][4] = 1;
                        current_floor[4][8] = 1;
                        refresh_ctr = true;
                    } else if (act == 6) {
                        sleep(200);
                        act++;
                        current_floor[4][6] = 36;
                        current_floor[5][7] = 36;
                        current_floor[4][5] = 1;
                        current_floor[4][7] = 1;
                        refresh_ctr = true;
                    } else if (act == 7) {
                        sleep(200);
                        act++;
                        current_floor[6][7] = 36;
                        current_floor[5][7] = 1;
                        refresh_ctr = true;
                    } else if (act == 8) {
                        sleep(200);
                        act++;
                        current_floor[6][6] = 36;
                        current_floor[6][7] = 1;
                        refresh_ctr = true;
                    } else if (act == 9) {
                        sleep(200);
                        act++;
                        current_floor[3][1] = 1;
                        current_floor[3][2] = 1;
                        current_floor[3][3] = 1;
                        current_floor[4][1] = 35;
                        current_floor[4][2] = 35;
                        current_floor[4][3] = 35;
                        current_floor[3][9] = 1;
                        current_floor[3][10] = 1;
                        current_floor[3][11] = 1;
                        current_floor[4][9] = 35;
                        current_floor[4][10] = 35;
                        current_floor[4][11] = 35;
                        refresh_ctr = true;
                    } else if (act == 10) {
                        sleep(200);
                        act++;
                        current_floor[4][4] = 35;
                        current_floor[4][8] = 35;
                        current_floor[4][3] = 1;
                        current_floor[4][9] = 1;
                        refresh_ctr = true;
                    } else if (act == 11) {
                        sleep(200);
                        act++;
                        current_floor[4][5] = 35;
                        current_floor[4][7] = 35;
                        current_floor[4][3] = 35;
                        current_floor[4][9] = 35;
                        current_floor[4][4] = 1;
                        current_floor[4][8] = 1;
                        current_floor[4][2] = 1;
                        current_floor[4][10] = 1;
                        refresh_ctr = true;
                    } else if (act == 12) {
                        sleep(200);
                        act++;
                        current_floor[5][5] = 35;
                        current_floor[5][7] = 35;
                        current_floor[4][4] = 35;
                        current_floor[4][8] = 35;
                        current_floor[4][2] = 35;
                        current_floor[4][10] = 35;
                        current_floor[4][5] = 1;
                        current_floor[4][7] = 1;
                        current_floor[4][3] = 1;
                        current_floor[4][9] = 1;
                        current_floor[4][1] = 1;
                        current_floor[4][11] = 1;
                        refresh_ctr = true;
                    } else if (act == 13) {
                        sleep(200);
                        act++;
                        current_floor[6][5] = 35;
                        current_floor[6][7] = 35;
                        current_floor[4][5] = 35;
                        current_floor[4][7] = 35;
                        current_floor[4][3] = 35;
                        current_floor[4][9] = 35;
                        current_floor[5][5] = 1;
                        current_floor[5][7] = 1;
                        current_floor[4][4] = 1;
                        current_floor[4][8] = 1;
                        current_floor[4][2] = 1;
                        current_floor[4][10] = 1;
                        refresh_ctr = true;
                    } else if (act == 14) {
                        sleep(200);
                        act++;
                        current_floor[5][5] = 35;
                        current_floor[5][7] = 35;
                        current_floor[4][4] = 35;
                        current_floor[4][8] = 35;
                        current_floor[4][5] = 1;
                        current_floor[4][7] = 1;
                        current_floor[4][3] = 1;
                        current_floor[4][9] = 1;
                        refresh_ctr = true;
                    } else if (act == 15) {
                        sleep(200);
                        act++;
                        current_floor[4][5] = 35;
                        current_floor[4][7] = 35;
                        current_floor[4][4] = 1;
                        current_floor[4][8] = 1;
                        refresh_ctr = true;
                    } else if (act == 16) {
                        sleep(200);
                        act++;
                        current_floor[4][4] = 8;
                        current_floor[4][8] = 8;
                        refresh_ctr = true;
                        isEvent = false;
                    } else if (act == 17) {
                        act++;
                        current_floor[3][6] = 1;
                        refresh_ctr = true;
                        isEvent = false;
                    } else if (act == 18) {
                        act++;
                        if (background_music != null)           // kill the old music first
                            background_music.release();
                        background_music = MediaPlayer.create(getApplicationContext(), bgm_list[0]);
                        background_music.setLooping(true);
                        if (music_settings[0])                  // if bgm option is checked
                            background_music.start();
                    } else if (act == 19) {
                        sleep(300);
                        act++;
                        current_floor[3][1] = 76;
                        current_floor[3][2] = 76;
                        current_floor[3][3] = 76;
                        refresh_ctr = true;
                    } else if (act == 20) {
                        sleep(200);
                        act++;
                        current_floor[3][9] = 77;
                        current_floor[3][10] = 77;
                        current_floor[3][11] = 77;
                        refresh_ctr = true;
                    } else if (act == 21) {
                        sleep(200);
                        act++;
                        current_floor[4][1] = 75;
                        current_floor[4][2] = 75;
                        current_floor[4][3] = 75;
                        refresh_ctr = true;
                    } else if (act == 22) {
                        sleep(200);
                        act++;
                        current_floor[4][9] = 71;
                        current_floor[4][10] = 71;
                        current_floor[4][11] = 71;
                        refresh_ctr = true;
                    } else if (act == 23) {
                        sleep(200);
                        act++;
                        current_floor[11][6] = 3;
                        current_floor[9][6] = -4;
                        refresh_ctr = true;
                    } else if (act == 24) {
                        sleep(200);
                        act++;
                        current_floor[4][4] = 1;
                        current_floor[4][8] = 1;
                        current_floor[7][6] = 1;
                        refresh_ctr = true;
                        isEvent = false;
                    } else if (act == 25) {
                        act++;
                        current_floor[10][1] = 21;
                        refresh_ctr = true;
                    } else if (act == 26) {
                        sleep(200);
                        act++;
                        current_floor[9][1] = 21;
                        current_floor[10][1] = 1;
                        refresh_ctr = true;
                    } else if (act == 27) {
                        sleep(200);
                        act++;
                        current_floor[8][1] = 21;
                        current_floor[9][1] = 1;
                        refresh_ctr = true;
                    } else if (act == 28) {
                        sleep(200);
                        act++;
                        current_floor[8][2] = 21;
                        current_floor[8][1] = 1;
                        refresh_ctr = true;
                    } else if (act == 29) {
                        sleep(200);
                        act++;
                        current_floor[8][3] = 21;
                        current_floor[8][2] = 1;
                        refresh_ctr = true;
                    } else if (act == 30) {
                        sleep(200);
                        act++;
                        current_floor[9][3] = 21;
                        current_floor[8][3] = 1;
                        refresh_ctr = true;
                    } else if (act == 31) {
                        sleep(200);
                        act++;
                        current_floor[10][3] = 21;
                        current_floor[9][3] = 1;
                        refresh_ctr = true;
                    } else if (act == 32) {
                        sleep(200);
                        act++;
                        current_floor[11][3] = 21;
                        current_floor[10][3] = 1;
                        refresh_ctr = true;
                    } else if (act == 33) {
                        sleep(200);
                        act++;
                        current_floor[11][4] = 21;
                        current_floor[11][3] = 1;
                        refresh_ctr = true;
                    } else if (act == 34) {
                        sleep(200);
                        act++;
                        current_floor[11][5] = 21;
                        current_floor[11][4] = 1;
                        refresh_ctr = true;
                    } else if (act == 35) {
                        sleep(200);
                        act++;
                        current_floor[10][5] = 21;
                        current_floor[11][5] = 1;
                        refresh_ctr = true;
                    } else if (act == 36) {
                        sleep(200);
                        act++;
                        current_floor[10][6] = 21;
                        current_floor[10][5] = 1;
                        refresh_ctr = true;
                        isEvent = false;
                    } else {
                        if (!proceed)
                            return;
                        act = 0;
                        current_floor[10][6] = 1;
                        refresh_ctr = true;
                        isEvent = false;
                    }
                    break;
                case 15:
                    if (hero_y == 1) {
                        if (act == 0) {
                            if (!proceed)
                                return;
                            proceed = false;
                            act++;
                            current_floor[1][8] = 1;
                            refresh_ctr = true;
                        } else if (act == 1) {
                            sleep(300);
                            act++;
                            current_floor[1][9] = 1;
                            current_floor[1][8] = 21;
                            refresh_ctr = true;
                        } else if (act == 2) {
                            sleep(300);
                            act++;
                            current_floor[1][8] = 1;
                            current_floor[1][7] = 21;
                            refresh_ctr = true;
                        } else {
                            sleep(500);
                            act = 0;
                            current_floor[1][7] = 1;
                            refresh_ctr = true;
                            isEvent = false;
                        }
                    } else {
                        if (act == 0) {
                            act++;
                            current_floor[8][6] = 1;
                        } else if (act == 1) {
                            sleep(300);
                            act++;
                            current_floor[9][6] = 8;
                            refresh_ctr = true;
                            isEvent = false;
                        } else if (act == 2) {
                            act++;
                        } else if (act == 3) {
                            act++;
                            current_floor[5][6] = 83;
                            current_floor[4][5] = 1;
                            current_floor[4][6] = 1;
                            current_floor[4][7] = 1;
                            current_floor[5][5] = 1;
                            current_floor[5][7] = 1;
                            current_floor[6][5] = 1;
                            current_floor[6][6] = 1;
                            current_floor[6][7] = 1;
                            refresh_ctr = true;
                        } else {
                            sleep(500);
                            act = 0;
                            current_floor[3][6] = 1;
                            current_floor[9][6] = 1;
                            refresh_ctr = true;
                            isEvent = false;
                        }
                    }
                    break;
                case 20:
                    if (act == 0) {
                        act++;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder f20_builder1 = new AlertDialog.Builder(parent);
                                f20_builder1.setMessage(R.string.vampire_20f_1);
                                AlertDialog f20_dialog1 = f20_builder1.create();
                                f20_dialog1.setCanceledOnTouchOutside(true);
                                f20_dialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        proceed = true;
                                        current_floor[8][6] = 1;
                                    }
                                });
                                f20_dialog1.show();
                            }
                        });
                    } else if (act == 1) {
                        if (!proceed)
                            return;
                        proceed = false;
                        if (background_music != null)           // kill the old music first
                            background_music.release();
                        background_music = MediaPlayer.create(getApplicationContext(), boss_music_list[1]);
                        background_music.setLooping(true);
                        if (music_settings[0])                  // if bgm option is checked
                            background_music.start();
                        act++;
                        current_floor[9][6] = 8;
                        refresh_ctr = true;
                    } else if (act == 2) {
                        sleep(700);
                        act++;
                        current_floor[6][6] = 40;
                        current_floor[6][5] = 1;
                        current_floor[6][7] = 1;
                        current_floor[5][5] = 1;
                        current_floor[5][6] = 1;
                        current_floor[5][7] = 1;
                        current_floor[7][5] = 1;
                        current_floor[7][6] = 1;
                        current_floor[7][7] = 1;
                        refresh_ctr = true;
                    } else if (act == 3) {
                        sleep(700);
                        act++;
                        current_floor[6][6] = 1;
                        current_floor[5][5] = 40;
                        current_floor[5][7] = 40;
                        current_floor[7][5] = 40;
                        current_floor[7][7] = 40;
                        refresh_ctr = true;
                    } else if (act == 4) {
                        sleep(700);
                        act++;
                        current_floor[6][6] = 40;
                        current_floor[5][5] = 1;
                        current_floor[5][7] = 1;
                        current_floor[7][5] = 1;
                        current_floor[7][7] = 1;
                        refresh_ctr = true;
                    } else if (act == 5) {
                        sleep(700);
                        act++;
                        current_floor[6][6] = 1;
                        current_floor[5][6] = 40;
                        current_floor[6][5] = 40;
                        current_floor[6][7] = 40;
                        current_floor[7][6] = 40;
                        refresh_ctr = true;
                    } else if (act == 6) {
                        sleep(700);
                        act++;
                        current_floor[6][6] = 40;
                        current_floor[5][6] = 1;
                        current_floor[6][5] = 1;
                        current_floor[6][7] = 1;
                        current_floor[7][6] = 1;
                        refresh_ctr = true;
                    } else if (act == 7) {
                        sleep(700);
                        act++;
                        current_floor[5][5] = 40;
                        current_floor[5][7] = 40;
                        current_floor[7][5] = 40;
                        current_floor[7][7] = 40;
                        refresh_ctr = true;
                    } else if (act == 8) {
                        sleep(700);
                        act++;
                        current_floor[5][6] = 40;
                        current_floor[6][5] = 40;
                        current_floor[6][7] = 40;
                        current_floor[7][6] = 40;
                        refresh_ctr = true;
                    } else if (act == 9) {
                        sleep(700);
                        act++;
                        current_floor[6][5] = 1;
                        current_floor[6][7] = 1;
                        current_floor[5][5] = 1;
                        current_floor[5][6] = 1;
                        current_floor[5][7] = 1;
                        current_floor[7][5] = 1;
                        current_floor[7][6] = 1;
                        current_floor[7][7] = 1;
                        current_floor[6][6] = 45;
                        refresh_ctr = true;
                        isEvent = false;
                    } else if (act == 10) {
                        act++;
                        if (background_music != null)           // kill the old music first
                            background_music.release();
                        background_music = MediaPlayer.create(getApplicationContext(), bgm_list[1]);
                        background_music.setLooping(true);
                        if (music_settings[0])                  // if bgm option is checked
                            background_music.start();
                    } else if (act == 11) {
                        act++;
                        current_floor[5][4] = 76;
                        current_floor[6][4] = 76;
                        current_floor[7][4] = 76;
                        current_floor[5][8] = 77;
                        current_floor[6][8] = 77;
                        current_floor[7][8] = 77;
                        refresh_ctr = true;
                    } else if (act == 12) {
                        sleep(500);
                        act++;
                        current_floor[4][5] = 71;
                        current_floor[4][6] = 71;
                        current_floor[4][7] = 71;
                        current_floor[8][5] = 75;
                        current_floor[8][6] = 75;
                        current_floor[8][7] = 75;
                        refresh_ctr = true;
                    } else {
                        sleep(700);
                        act = 0;
                        current_floor[3][6] = 1;
                        current_floor[9][6] = 1;
                        current_floor[1][6] = 3;
                        refresh_ctr = true;
                        isEvent = false;
                    }
                    break;
                case 24:
                    if (act == 0) {
                        act++;
                    } else if (act == 1) {
                        sleep(500);
                        act++;
                        not_show_hero = true;
                        hero_x = 6; hero_y = 7;
                        refresh_ctr = true;
                    } else if (act == 2) {
                        sleep(1000);
                        act++;
                        for (int e = 1; e < 12; e++) {
                            for (int r = 1; r < 12; r++) {
                                if (current_floor[e][r] != 1)
                                    current_floor[e][r] = 1;
                            }
                        }
                        refresh_ctr = true;
                    } else if (act == 3) {
                        sleep(1000);
                        act++;
                        for (int e = 1; e < 12; e++) {
                            for (int r = 1; r < 12; r++) {
                                current_floor[e][r] = 2;
                            }
                        }
                        refresh_ctr = true;
                    } else if (act == 4) {
                        sleep(1000);
                        act++;
                        for (int e = 5; e < 8; e++) {
                            for (int r = 5; r < 8; r++) {
                                current_floor[e][r] = 1;
                            }
                        }
                        refresh_ctr = true;
                    } else if (act == 5) {
                        sleep(1000);
                        act++;
                        not_show_hero = false;
                        refresh_ctr = true;
                    } else if (act == 6) {
                        sleep(1000);
                        act++;
                        for (int e = 4; e < 9; e++) {
                            current_floor[e][4] = 0;
                            current_floor[e][8] = 0;
                            current_floor[4][e] = 0;
                            current_floor[8][e] = 0;
                        }
                        refresh_ctr = true;
                    } else if (act == 7) {
                        sleep(1000);
                        act++;
                        current_floor[5][6] = 21;
                        refresh_ctr = true;
                    } else {
                        sleep(1000);
                        act = 0;
                        isEvent = false;
                        floor_num = 50;
                    }
                    break;
                case 25:
                    if (act == 0) {
                        act++;
                    } else if (act == 1) {
                        sleep(500);
                        act++;
                        current_floor[10][6] = 8;
                        refresh_ctr = true;
                    } else if (act == 2) {
                        sleep(500);
                        act++;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder f25_builder1 = new AlertDialog.Builder(parent);
                                f25_builder1.setMessage(R.string.archmage_25f);
                                AlertDialog f25_dialog1 = f25_builder1.create();
                                f25_dialog1.setCanceledOnTouchOutside(true);
                                f25_dialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        proceed = true;
                                    }
                                });
                                f25_dialog1.show();
                            }
                        });
                    } else if (act == 3) {
                        if (!proceed)
                            return;
                        proceed = false;
                        if (background_music != null)           // kill the old music first
                            background_music.release();
                        background_music = MediaPlayer.create(getApplicationContext(), boss_music_list[3]);
                        background_music.setLooping(true);
                        if (music_settings[0])                  // if bgm option is checked
                            background_music.start();
                        act++;
                        isEvent = false;
                    } else if (act == 4) {
                        sleep(300);
                        if (background_music != null)           // kill the old music first
                            background_music.release();
                        background_music = MediaPlayer.create(getApplicationContext(), bgm_list[3]);
                        background_music.setLooping(true);
                        if (music_settings[0])                  // if bgm option is checked
                            background_music.start();
                        act++;
                        current_floor[8][4] = 73;
                        refresh_ctr = true;
                    } else if (act == 5) {
                        sleep(300);
                        act++;
                        current_floor[8][5] = 73;
                        refresh_ctr = true;
                    } else if (act == 6) {
                        sleep(300);
                        act++;
                        current_floor[8][6] = 73;
                        refresh_ctr = true;
                    } else if (act == 7) {
                        sleep(300);
                        act++;
                        current_floor[8][7] = 73;
                        refresh_ctr = true;
                    } else if (act == 8) {
                        sleep(300);
                        act++;
                        current_floor[8][8] = 73;
                        refresh_ctr = true;
                    } else {
                        sleep(300);
                        act++;
                        current_floor[10][6] = 1;
                        refresh_ctr = true;
                        isEvent = false;
                    }
                    break;
                case 29:
                    if (!proceed)
                        return;
                    proceed = false;
                    isEvent = false;
                    break;
                case 30:
                    if (act == 0) {
                        act++;
                    } else if (act == 1) {
                        sleep(300);
                        act++;
                        current_floor[4][6] = 8;
                        current_floor[7][6] = 8;
                        refresh_ctr = true;
                        isEvent = false;
                    } else if (act == 2) {
                        act++;
                    } else if (act == 3) {
                        act++;
                        current_floor[4][6] = 1;
                        current_floor[7][6] = 1;
                        refresh_ctr = true;
                    } else {
                        sleep(300);
                        act = 0;
                        current_floor[1][6] = 3;
                        refresh_ctr = true;
                        isEvent = false;
                    }
                    break;
                case 32:
                    if (act == 0) {
                        act++;
                    } else if (act == 1) {
                        sleep(200);
                        act++;
                        current_floor[1][10] = 52;
                        refresh_ctr = true;
                    } else if (act == 2) {
                        sleep(200);
                        act++;
                        current_floor[1][10] = 1;
                        current_floor[1][9] = 52;
                        refresh_ctr = true;
                    } else if (act == 3) {
                        sleep(200);
                        act++;
                        current_floor[1][9] = 1;
                        current_floor[1][8] = 52;
                        refresh_ctr = true;
                    } else if (act == 4) {
                        sleep(200);
                        act++;
                        current_floor[1][8] = 1;
                        current_floor[1][7] = 52;
                        refresh_ctr = true;
                    } else if (act == 5) {
                        sleep(200);
                        act++;
                        current_floor[1][7] = 1;
                        current_floor[1][6] = 52;
                        refresh_ctr = true;
                    } else if (act == 6) {
                        sleep(200);
                        act++;
                        current_floor[1][6] = 1;
                        current_floor[2][6] = 52;
                        refresh_ctr = true;
                    } else if (act == 7) {
                        sleep(200);
                        act++;
                        current_floor[2][6] = 1;
                        current_floor[3][6] = 52;
                        refresh_ctr = true;
                    } else if (act == 8) {
                        sleep(200);
                        act++;
                        current_floor[3][6] = 1;
                        current_floor[4][6] = 52;
                        refresh_ctr = true;
                    } else if (act == 9) {
                        sleep(200);
                        act++;
                        current_floor[4][6] = 1;
                        current_floor[5][6] = 52;
                        refresh_ctr = true;
                    } else if (act == 10) {
                        sleep(200);
                        act++;
                        current_floor[5][6] = 1;
                        current_floor[6][6] = 52;
                        refresh_ctr = true;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder f32_builder1 = new AlertDialog.Builder(parent);
                                f32_builder1.setMessage(R.string.knight_captain_32f_1);
                                AlertDialog f32_dialog1 = f32_builder1.create();
                                f32_dialog1.setCanceledOnTouchOutside(true);
                                f32_dialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        proceed = true;
                                    }
                                });
                                f32_dialog1.show();
                            }
                        });
                    } else if (act == 11) {
                        if (!proceed)
                            return;
                        proceed = false;
                        act++;
                        current_floor[6][6] = 1;
                        current_floor[7][6] = 52;
                        refresh_ctr = true;
                    } else if (act == 12) {
                        sleep(200);
                        act++;
                        current_floor[7][6] = 1;
                        current_floor[8][6] = 52;
                        refresh_ctr = true;
                    } else if (act == 13) {
                        sleep(200);
                        act++;
                        current_floor[8][6] = 1;
                        current_floor[9][6] = 52;
                        refresh_ctr = true;
                    } else if (act == 14) {
                        sleep(200);
                        act++;
                        current_floor[9][6] = 1;
                        current_floor[10][6] = 52;
                        refresh_ctr = true;
                        hero_attack = false;
                        m_hp = m_table[21][0];
                        m_atk = m_table[21][1];
                        m_def = m_table[21][2];
                        m_gold = m_table[21][3];
                        isBattle = true;
                        isEvent = false;
                    } else if (act == 15) {
                        act++;
                        current_floor[9][6] = 52;
                        refresh_ctr = true;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder f32_builder2 = new AlertDialog.Builder(parent);
                                f32_builder2.setMessage(R.string.knight_captain_32f_2);
                                AlertDialog f32_dialog2 = f32_builder2.create();
                                f32_dialog2.setCanceledOnTouchOutside(true);
                                f32_dialog2.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        proceed = true;
                                    }
                                });
                                f32_dialog2.show();
                            }
                        });
                    } else if (act == 16) {
                        sleep(200);
                        if (!proceed)
                            return;
                        proceed = false;
                        act++;
                        current_floor[9][6] = 52;
                        refresh_ctr = true;
                    } else if (act == 17) {
                        sleep(50);
                        act++;
                        current_floor[8][6] = 52;
                        current_floor[9][6] = 1;
                        refresh_ctr = true;
                    } else if (act == 18) {
                        sleep(50);
                        act++;
                        current_floor[7][6] = 52;
                        current_floor[8][6] = 1;
                        refresh_ctr = true;
                    } else if (act == 19) {
                        sleep(50);
                        act++;
                        current_floor[6][6] = 52;
                        current_floor[7][6] = 1;
                        refresh_ctr = true;
                    } else if (act == 20) {
                        sleep(50);
                        act++;
                        current_floor[5][6] = 52;
                        current_floor[6][6] = 1;
                        refresh_ctr = true;
                    } else if (act == 21) {
                        sleep(50);
                        act++;
                        current_floor[4][6] = 52;
                        current_floor[5][6] = 1;
                        refresh_ctr = true;
                    } else if (act == 22) {
                        sleep(50);
                        act++;
                        current_floor[3][6] = 52;
                        current_floor[4][6] = 1;
                        refresh_ctr = true;
                    } else if (act == 23) {
                        sleep(50);
                        act++;
                        current_floor[2][6] = 52;
                        current_floor[3][6] = 1;
                        refresh_ctr = true;
                    } else if (act == 24) {
                        sleep(50);
                        act++;
                        current_floor[1][6] = 52;
                        current_floor[2][6] = 1;
                        refresh_ctr = true;
                    } else if (act == 25) {
                        sleep(50);
                        act++;
                        current_floor[1][7] = 52;
                        current_floor[1][6] = 1;
                        refresh_ctr = true;
                    } else if (act == 26) {
                        sleep(50);
                        act++;
                        current_floor[1][8] = 52;
                        current_floor[1][7] = 1;
                        refresh_ctr = true;
                    } else if (act == 27) {
                        sleep(50);
                        act++;
                        current_floor[1][9] = 52;
                        current_floor[1][8] = 1;
                        refresh_ctr = true;
                    } else if (act == 28) {
                        sleep(50);
                        act++;
                        current_floor[1][10] = 52;
                        current_floor[1][9] = 1;
                        refresh_ctr = true;
                    } else if (act == 29) {
                        sleep(50);
                        act = 0;
                        current_floor[1][10] = 1;
                        refresh_ctr = true;
                        isEvent = false;
                    }
                    break;
                case 33:
                    if (act == 0) {
                        current_floor[5][10] = 1;
                        act++;
                    } else if (act == 1) {
                        sleep(300);
                        act = 0;
                        current_floor[4][10] = 8;
                        current_floor[8][10] = 8;
                        refresh_ctr = true;
                        isEvent = false;
                    }
                    break;
                case 35:
                    if (thief_event_count == 5) {
                        current_game.change_2f();
                        isEvent = false;
                    } else if (act == 0) {
                        if (highest_floor == 35) {
                            if (!proceed)
                                return;
                            proceed = false;
                            act++;
                        } else {
                            act++;
                        }
                    } else if (act == 1) {
                        if (highest_floor == 35) {
                            sleep(300);
                            act++;
                            current_floor[10][5] = 1;
                            current_floor[11][5] = 21;
                            refresh_ctr = true;
                        } else {
                            sleep(500);
                            act++;
                            current_floor[6][5] = 1;
                            current_floor[6][6] = 92;
                            current_floor[6][7] = 1;
                            current_floor[7][5] = 1;
                            current_floor[7][7] = 1;
                            refresh_ctr = true;
                        }
                    } else if (act == 2) {
                        if (highest_floor == 35) {
                            sleep(300);
                            act = 0;
                            current_floor[11][5] = 1;
                            refresh_ctr = true;
                            isEvent = false;
                        } else {
                            sleep(500);
                            act++;
                            current_floor[5][5] = 75;
                            current_floor[5][6] = 75;
                            current_floor[5][7] = 75;
                            refresh_ctr = true;
                        }
                    } else {
                        sleep(500);
                        act = 0;
                        current_floor[3][6] = 1;
                        refresh_ctr = true;
                        isEvent = false;
                    }
                    break;
                case 38:
                    if (act == 0) {
                        act++;
                    } else if (act == 1) {
                        sleep(600);
                        act++;
                        current_floor[9][2] = 8;
                        refresh_ctr = true;
                    } else {
                        sleep(600);
                        act = 0;
                        current_floor[9][2] = 1;
                        refresh_ctr = true;
                        isEvent = false;
                    }
                    break;
                case 40:
                    if (act == 0) {
                        boolean no_teleport = current_floor[2][2] != 1 && current_floor[2][3] != 1 && current_floor[2][4] != 1;
                        no_teleport &= current_floor[2][8] != 1 && current_floor[2][9] != 1 && current_floor[2][10] != 1;
                        no_teleport &= current_floor[4][3] != 1 && current_floor[4][4] != 1 && current_floor[4][5] != 1;
                        no_teleport &= current_floor[4][7] != 1 && current_floor[4][8] != 1 && current_floor[4][9] != 1;
                        no_teleport &= current_floor[2][6] != 1;
                        current_floor[7][6] = 1;
                        if (no_teleport)
                            act = 5;
                        else
                            act++;
                    } else if (act == 1) {
                        sleep(300);
                        act++;
                        hero_x = 6;
                        hero_y = 7;
                        current_floor[2][6] = 52;
                        current_floor[8][6] = 8;
                        refresh_ctr = true;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder f32_builder2 = new AlertDialog.Builder(parent);
                                f32_builder2.setMessage(R.string.knight_captain_40f_0);
                                AlertDialog f32_dialog2 = f32_builder2.create();
                                f32_dialog2.setCanceledOnTouchOutside(true);
                                f32_dialog2.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        proceed = true;
                                    }
                                });
                                f32_dialog2.show();
                            }
                        });
                    } else if (act == 2) {
                        if (!proceed)
                            return;
                        proceed = false;
                        act++;
                    } else if (act == 3) {
                        sleep(300);
                        act++;
                        current_floor[2][2] = 50;
                        current_floor[2][3] = 50;
                        current_floor[2][4] = 50;
                        current_floor[2][8] = 51;
                        current_floor[2][9] = 51;
                        current_floor[2][10] = 51;
                        refresh_ctr = true;
                    } else if (act == 4) {
                        sleep(300);
                        act++;
                        current_floor[4][3] = 47;
                        current_floor[4][4] = 47;
                        current_floor[4][5] = 47;
                        current_floor[4][7] = 48;
                        current_floor[4][8] = 48;
                        current_floor[4][9] = 48;
                        refresh_ctr = true;
                    } else if (act == 5) {
                        sleep(300);
                        act++;
                        current_floor[8][6] = 8;
                        refresh_ctr = true;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder f40_builder1 = new AlertDialog.Builder(parent);
                                f40_builder1.setMessage(R.string.knight_captain_40f_1);
                                AlertDialog f40_dialog1 = f40_builder1.create();
                                f40_dialog1.setCanceledOnTouchOutside(true);
                                f40_dialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        proceed = true;
                                    }
                                });
                                f40_dialog1.show();
                            }
                        });
                    } else if (act == 6) {
                        if (!proceed)
                            return;
                        proceed = false;
                        if (background_music != null)           // kill the old music first
                            background_music.release();
                        background_music = MediaPlayer.create(getApplicationContext(), boss_music_list[3]);
                        background_music.setLooping(true);
                        if (music_settings[0])                  // if bgm option is checked
                            background_music.start();
                        act++;
                    } else if (act == 7) {
                        sleep(300);
                        act++;
                        current_floor[4][5] = 1;
                        current_floor[5][5] = 47;
                        refresh_ctr = true;
                    } else if (act == 8) {
                        sleep(50);
                        act++;
                        current_floor[4][4] = 1;
                        current_floor[4][5] = 47;
                        current_floor[5][5] = 1;
                        current_floor[6][5] = 47;
                        refresh_ctr = true;
                    } else if (act == 9) {
                        sleep(50);
                        act++;
                        current_floor[4][3] = 1;
                        current_floor[4][4] = 47;
                        current_floor[4][5] = 1;
                        current_floor[5][5] = 47;
                        current_floor[6][5] = 1;
                        current_floor[7][5] = 47;
                        refresh_ctr = true;
                    } else if (act == 10) {
                        sleep(50);
                        act++;
                        current_floor[4][4] = 1;
                        current_floor[4][5] = 47;
                        current_floor[5][5] = 1;
                        current_floor[6][5] = 47;
                        current_floor[7][5] = 1;
                        current_floor[7][6] = 47;
                        hero_attack = false;
                        m_hp = m_table[16][0];
                        m_atk = m_table[16][1];
                        m_def = m_table[16][2];
                        m_gold = m_table[16][3];
                        isBattle = true;
                        refresh_ctr = true;
                        isEvent = false;
                    } else if (act == 11) {
                        sleep(50);
                        act++;
                        current_floor[4][5] = 1;
                        current_floor[5][5] = 47;
                        current_floor[6][5] = 1;
                        current_floor[7][5] = 47;
                        refresh_ctr = true;
                    } else if (act == 12) {
                        sleep(50);
                        act++;
                        current_floor[5][5] = 1;
                        current_floor[6][5] = 47;
                        current_floor[7][5] = 1;
                        current_floor[7][6] = 47;
                        hero_attack = false;
                        m_hp = m_table[16][0];
                        m_atk = m_table[16][1];
                        m_def = m_table[16][2];
                        m_gold = m_table[16][3];
                        isBattle = true;
                        refresh_ctr = true;
                        isEvent = false;
                    } else if (act == 13) {
                        sleep(50);
                        act++;
                        current_floor[6][5] = 1;
                        current_floor[7][5] = 47;
                        refresh_ctr = true;
                    } else if (act == 14) {
                        sleep(50);
                        act++;
                        current_floor[7][5] = 1;
                        current_floor[7][6] = 47;
                        hero_attack = false;
                        m_hp = m_table[16][0];
                        m_atk = m_table[16][1];
                        m_def = m_table[16][2];
                        m_gold = m_table[16][3];
                        isBattle = true;
                        refresh_ctr = true;
                        isEvent = false;
                    } else if (act == 15) {
                        sleep(300);
                        act++;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder f40_builder2 = new AlertDialog.Builder(parent);
                                f40_builder2.setMessage(R.string.knight_captain_40f_2);
                                AlertDialog f40_dialog2 = f40_builder2.create();
                                f40_dialog2.setCanceledOnTouchOutside(true);
                                f40_dialog2.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        proceed = true;
                                    }
                                });
                                f40_dialog2.show();
                            }
                        });
                    } else if (act == 16) {
                        if (!proceed)
                            return;
                        proceed = false;
                        act++;
                    } else if (act == 17) {
                        sleep(300);
                        act++;
                        current_floor[4][7] = 1;
                        current_floor[5][7] = 48;
                        refresh_ctr = true;
                    } else if (act == 18) {
                        sleep(50);
                        act++;
                        current_floor[4][8] = 1;
                        current_floor[4][7] = 48;
                        current_floor[5][7] = 1;
                        current_floor[6][7] = 48;
                        refresh_ctr = true;
                    } else if (act == 19) {
                        sleep(50);
                        act++;
                        current_floor[4][9] = 1;
                        current_floor[4][8] = 48;
                        current_floor[4][7] = 1;
                        current_floor[5][7] = 48;
                        current_floor[6][7] = 1;
                        current_floor[7][7] = 48;
                        refresh_ctr = true;
                    } else if (act == 20) {
                        sleep(50);
                        act++;
                        current_floor[4][8] = 1;
                        current_floor[4][7] = 48;
                        current_floor[5][7] = 1;
                        current_floor[6][7] = 48;
                        current_floor[7][7] = 1;
                        current_floor[7][6] = 48;
                        hero_attack = false;
                        m_hp = m_table[17][0];
                        m_atk = m_table[17][1];
                        m_def = m_table[17][2];
                        m_gold = m_table[17][3];
                        isBattle = true;
                        refresh_ctr = true;
                        isEvent = false;
                    } else if (act == 21) {
                        sleep(50);
                        act++;
                        current_floor[4][7] = 1;
                        current_floor[5][7] = 48;
                        current_floor[6][7] = 1;
                        current_floor[7][7] = 48;
                        refresh_ctr = true;
                    } else if (act == 22) {
                        sleep(50);
                        act++;
                        current_floor[5][7] = 1;
                        current_floor[6][7] = 48;
                        current_floor[7][7] = 1;
                        current_floor[7][6] = 48;
                        hero_attack = false;
                        m_hp = m_table[17][0];
                        m_atk = m_table[17][1];
                        m_def = m_table[17][2];
                        m_gold = m_table[17][3];
                        isBattle = true;
                        refresh_ctr = true;
                        isEvent = false;
                    } else if (act == 23) {
                        sleep(50);
                        act++;
                        current_floor[6][7] = 1;
                        current_floor[7][7] = 48;
                        refresh_ctr = true;
                    } else if (act == 24) {
                        sleep(50);
                        act++;
                        current_floor[7][7] = 1;
                        current_floor[7][6] = 48;
                        hero_attack = false;
                        m_hp = m_table[17][0];
                        m_atk = m_table[17][1];
                        m_def = m_table[17][2];
                        m_gold = m_table[17][3];
                        isBattle = true;
                        refresh_ctr = true;
                        isEvent = false;
                    } else if (act == 25) {
                        sleep(300);
                        act++;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder f40_builder3 = new AlertDialog.Builder(parent);
                                f40_builder3.setMessage(R.string.knight_captain_40f_3);
                                AlertDialog f40_dialog3 = f40_builder3.create();
                                f40_dialog3.setCanceledOnTouchOutside(true);
                                f40_dialog3.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        proceed = true;
                                    }
                                });
                                f40_dialog3.show();
                            }
                        });
                    } else if (act == 26) {
                        if (!proceed)
                            return;
                        proceed = false;
                        act++;
                    } else if (act == 27) {
                        sleep(300);
                        act++;
                        current_floor[2][4] = 1;
                        current_floor[3][4] = 50;
                        refresh_ctr = true;
                    } else if (act == 28) {
                        sleep(50);
                        act++;
                        current_floor[2][3] = 1;
                        current_floor[2][4] = 50;
                        current_floor[3][4] = 1;
                        current_floor[3][5] = 50;
                        refresh_ctr = true;
                    } else if (act == 29) {
                        sleep(50);
                        act++;
                        current_floor[2][2] = 1;
                        current_floor[2][3] = 50;
                        current_floor[2][4] = 1;
                        current_floor[3][4] = 50;
                        current_floor[3][5] = 1;
                        current_floor[4][5] = 50;
                        refresh_ctr = true;
                    } else if (act == 30) {
                        sleep(50);
                        act++;
                        current_floor[2][3] = 1;
                        current_floor[2][4] = 50;
                        current_floor[3][4] = 1;
                        current_floor[3][5] = 50;
                        current_floor[4][5] = 1;
                        current_floor[5][5] = 50;
                        refresh_ctr = true;
                    } else if (act == 31) {
                        sleep(50);
                        act++;
                        current_floor[2][4] = 1;
                        current_floor[3][4] = 50;
                        current_floor[3][5] = 1;
                        current_floor[4][5] = 50;
                        current_floor[5][5] = 1;
                        current_floor[6][5] = 50;
                        refresh_ctr = true;
                    } else if (act == 32) {
                        sleep(50);
                        act++;
                        current_floor[3][4] = 1;
                        current_floor[3][5] = 50;
                        current_floor[4][5] = 1;
                        current_floor[5][5] = 50;
                        current_floor[6][5] = 1;
                        current_floor[7][5] = 50;
                        refresh_ctr = true;
                    } else if (act == 33) {
                        sleep(50);
                        act++;
                        current_floor[3][5] = 1;
                        current_floor[4][5] = 50;
                        current_floor[5][5] = 1;
                        current_floor[6][5] = 50;
                        current_floor[7][5] = 1;
                        current_floor[7][6] = 50;
                        refresh_ctr = true;
                        hero_attack = false;
                        m_hp = m_table[19][0];
                        m_atk = m_table[19][1];
                        m_def = m_table[19][2];
                        m_gold = m_table[19][3];
                        isBattle = true;
                        isEvent = false;
                    } else if (act == 34) {
                        sleep(50);
                        act++;
                        current_floor[4][5] = 1;
                        current_floor[5][5] = 50;
                        current_floor[6][5] = 1;
                        current_floor[7][5] = 50;
                        refresh_ctr = true;
                    } else if (act == 35) {
                        sleep(50);
                        act++;
                        current_floor[5][5] = 1;
                        current_floor[6][5] = 50;
                        current_floor[7][5] = 1;
                        current_floor[7][6] = 50;
                        refresh_ctr = true;
                        hero_attack = false;
                        m_hp = m_table[19][0];
                        m_atk = m_table[19][1];
                        m_def = m_table[19][2];
                        m_gold = m_table[19][3];
                        isBattle = true;
                        isEvent = false;
                    } else if (act == 36) {
                        sleep(50);
                        act++;
                        current_floor[6][5] = 1;
                        current_floor[7][5] = 50;
                        refresh_ctr = true;
                    } else if (act == 37) {
                        sleep(50);
                        act++;
                        current_floor[7][5] = 1;
                        current_floor[7][6] = 50;
                        refresh_ctr = true;
                        hero_attack = false;
                        m_hp = m_table[19][0];
                        m_atk = m_table[19][1];
                        m_def = m_table[19][2];
                        m_gold = m_table[19][3];
                        isBattle = true;
                        isEvent = false;
                    } else if (act == 38) {
                        sleep(300);
                        act++;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder f40_builder4 = new AlertDialog.Builder(parent);
                                f40_builder4.setMessage(R.string.knight_captain_40f_4);
                                AlertDialog f40_dialog4 = f40_builder4.create();
                                f40_dialog4.setCanceledOnTouchOutside(true);
                                f40_dialog4.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        proceed = true;
                                    }
                                });
                                f40_dialog4.show();
                            }
                        });
                    } else if (act == 39) {
                        if (!proceed)
                            return;
                        proceed = false;
                        act++;
                    } else if (act == 40) {
                        sleep(300);
                        act++;
                        current_floor[2][8] = 1;
                        current_floor[3][8] = 51;
                        refresh_ctr = true;
                    } else if (act == 41) {
                        sleep(50);
                        act++;
                        current_floor[2][9] = 1;
                        current_floor[2][8] = 51;
                        current_floor[3][8] = 1;
                        current_floor[3][7] = 51;
                        refresh_ctr = true;
                    } else if (act == 42) {
                        sleep(50);
                        act++;
                        current_floor[2][10] = 1;
                        current_floor[2][9] = 51;
                        current_floor[2][8] = 1;
                        current_floor[3][8] = 51;
                        current_floor[3][7] = 1;
                        current_floor[4][7] = 51;
                        refresh_ctr = true;
                    } else if (act == 43) {
                        sleep(50);
                        act++;
                        current_floor[2][9] = 1;
                        current_floor[2][8] = 51;
                        current_floor[3][8] = 1;
                        current_floor[3][7] = 51;
                        current_floor[4][7] = 1;
                        current_floor[5][7] = 51;
                        refresh_ctr = true;
                    } else if (act == 44) {
                        sleep(50);
                        act++;
                        current_floor[2][8] = 1;
                        current_floor[3][8] = 51;
                        current_floor[3][7] = 1;
                        current_floor[4][7] = 51;
                        current_floor[5][7] = 1;
                        current_floor[6][7] = 51;
                        refresh_ctr = true;
                    } else if (act == 45) {
                        sleep(50);
                        act++;
                        current_floor[3][8] = 1;
                        current_floor[3][7] = 51;
                        current_floor[4][7] = 1;
                        current_floor[5][7] = 51;
                        current_floor[6][7] = 1;
                        current_floor[7][7] = 51;
                        refresh_ctr = true;
                    } else if (act == 46) {
                        sleep(50);
                        act++;
                        current_floor[3][7] = 1;
                        current_floor[4][7] = 51;
                        current_floor[5][7] = 1;
                        current_floor[6][7] = 51;
                        current_floor[7][7] = 1;
                        current_floor[7][6] = 51;
                        refresh_ctr = true;
                        hero_attack = false;
                        m_hp = m_table[20][0];
                        m_atk = m_table[20][1];
                        m_def = m_table[20][2];
                        m_gold = m_table[20][3];
                        isBattle = true;
                        isEvent = false;
                    } else if (act == 47) {
                        sleep(50);
                        act++;
                        current_floor[4][7] = 1;
                        current_floor[5][7] = 51;
                        current_floor[6][7] = 1;
                        current_floor[7][7] = 51;
                        refresh_ctr = true;
                    } else if (act == 48) {
                        sleep(50);
                        act++;
                        current_floor[5][7] = 1;
                        current_floor[6][7] = 51;
                        current_floor[7][7] = 1;
                        current_floor[7][6] = 51;
                        refresh_ctr = true;
                        hero_attack = false;
                        m_hp = m_table[20][0];
                        m_atk = m_table[20][1];
                        m_def = m_table[20][2];
                        m_gold = m_table[20][3];
                        isBattle = true;
                        isEvent = false;
                    } else if (act == 49) {
                        sleep(50);
                        act++;
                        current_floor[6][7] = 1;
                        current_floor[7][7] = 51;
                        refresh_ctr = true;
                    } else if (act == 50) {
                        sleep(50);
                        act++;
                        current_floor[7][7] = 1;
                        current_floor[7][6] = 51;
                        refresh_ctr = true;
                        hero_attack = false;
                        m_hp = m_table[20][0];
                        m_atk = m_table[20][1];
                        m_def = m_table[20][2];
                        m_gold = m_table[20][3];
                        isBattle = true;
                        isEvent = false;
                    } else if (act == 51) {
                        sleep(300);
                        act++;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder f40_builder5 = new AlertDialog.Builder(parent);
                                f40_builder5.setMessage(R.string.knight_captain_40f_5);
                                AlertDialog f40_dialog5 = f40_builder5.create();
                                f40_dialog5.setCanceledOnTouchOutside(true);
                                f40_dialog5.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        proceed = true;
                                    }
                                });
                                f40_dialog5.show();
                            }
                        });
                    } else if (act == 52) {
                        if (!proceed)
                            return;
                        proceed = false;
                        act++;
                    } else if (act == 53) {
                        sleep(300);
                        act++;
                        current_floor[2][6] = 1;
                        current_floor[3][6] = 52;
                        refresh_ctr = true;
                    } else if (act == 54) {
                        sleep(50);
                        act++;
                        current_floor[3][6] = 1;
                        current_floor[4][6] = 52;
                        refresh_ctr = true;
                    } else if (act == 55) {
                        sleep(100);
                        act++;
                        current_floor[4][6] = 1;
                        current_floor[5][6] = 52;
                        refresh_ctr = true;
                    } else if (act == 56) {
                        sleep(200);
                        act++;
                        current_floor[5][6] = 1;
                        current_floor[6][6] = 52;
                        refresh_ctr = true;
                    } else if (act == 57) {
                        sleep(300);
                        act++;
                        current_floor[6][6] = 1;
                        current_floor[7][6] = 52;
                        refresh_ctr = true;
                        hero_attack = false;
                        m_hp = m_table[21][0];
                        m_atk = m_table[21][1];
                        m_def = m_table[21][2];
                        m_gold = m_table[21][3];
                        isBattle = true;
                        isEvent = false;
                    } else if (act == 58) {
                        sleep(300);
                        act++;
                        current_floor[6][6] = 52;
                        refresh_ctr = true;
                    } else if (act == 59) {
                        sleep(300);
                        act++;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder f40_builder6 = new AlertDialog.Builder(parent);
                                f40_builder6.setMessage(R.string.knight_captain_40f_6);
                                AlertDialog f40_dialog6 = f40_builder6.create();
                                f40_dialog6.setCanceledOnTouchOutside(true);
                                f40_dialog6.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        proceed = true;
                                    }
                                });
                                f40_dialog6.show();
                            }
                        });
                    } else if (act == 60) {
                        if (!proceed)
                            return;
                        proceed = false;
                        if (background_music != null)           // kill the old music first
                            background_music.release();
                        background_music = MediaPlayer.create(getApplicationContext(), bgm_list[3]);
                        background_music.setLooping(true);
                        if (music_settings[0])                  // if bgm option is checked
                            background_music.start();
                        act++;
                    } else if (act == 61) {
                        sleep(25);
                        act++;
                        current_floor[6][6] = 1;
                        current_floor[5][6] = 52;
                        refresh_ctr = true;
                    } else if (act == 62) {
                        sleep(25);
                        act++;
                        current_floor[5][6] = 1;
                        current_floor[4][6] = 52;
                        refresh_ctr = true;
                    } else if (act == 63) {
                        sleep(25);
                        act++;
                        current_floor[4][6] = 1;
                        current_floor[3][6] = 52;
                        refresh_ctr = true;
                    } else if (act == 64) {
                        sleep(25);
                        act++;
                        current_floor[3][6] = 1;
                        current_floor[2][6] = 52;
                        refresh_ctr = true;
                    } else if (act == 65) {
                        sleep(25);
                        act++;
                        current_floor[1][6] = 3;
                        refresh_ctr = true;
                    } else if (act == 66) {
                        sleep(25);
                        act++;
                        current_floor[2][6] = 1;
                        refresh_ctr = true;
                    } else if (act == 67) {
                        sleep(300);
                        act++;
                        current_floor[2][2] = 76;
                        current_floor[2][3] = 76;
                        current_floor[2][4] = 76;
                        refresh_ctr = true;
                    } else if (act == 68) {
                        sleep(300);
                        act++;
                        current_floor[2][8] = 77;
                        current_floor[2][9] = 77;
                        current_floor[2][10] = 77;
                        refresh_ctr = true;
                    } else if (act == 69) {
                        sleep(300);
                        act++;
                        current_floor[4][3] = 75;
                        current_floor[4][4] = 75;
                        current_floor[4][5] = 75;
                        refresh_ctr = true;
                    } else if (act == 70) {
                        sleep(300);
                        act = 0;
                        current_floor[4][7] = 71;
                        current_floor[4][8] = 71;
                        current_floor[4][9] = 71;
                        refresh_ctr = true;
                        isEvent = false;
                    }
                    break;
                case 41:
                    if (act == 0) {
                        act++;
                    } else if (act == 1) {
                        sleep(300);
                        act++;
                        current_floor[7][5] = 1;
                        current_floor[7][7] = 1;
                        refresh_ctr = true;
                    } else if (act == 2) {
                        sleep(300);
                        act++;
                        current_floor[6][5] = 0;
                        current_floor[6][6] = 0;
                        current_floor[6][7] = 0;
                        refresh_ctr = true;
                    } else {
                        sleep(300);
                        act = 0;
                        current_floor[5][6] = 89;
                        refresh_ctr = true;
                        isEvent = false;
                    }
                    break;
                case 42:
                    if (act == 0) {
                        act++;
                    } else if (act == 1) {
                        sleep(500);
                        act++;
                        current_floor[6][6] = 60;
                        refresh_ctr = true;
                    } else if (act == 2) {
                        sleep(500);
                        act++;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder f42_builder1 = new AlertDialog.Builder(parent);
                                f42_builder1.setMessage(R.string.knight_captain_42f_1);
                                AlertDialog f42_dialog1 = f42_builder1.create();
                                f42_dialog1.setCanceledOnTouchOutside(true);
                                f42_dialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        proceed = true;
                                    }
                                });
                                f42_dialog1.show();
                            }
                        });
                    } else if (act == 3) {
                        if (!proceed)
                            return;
                        proceed = false;
                        sleep(500);
                        act++;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder f42_builder2 = new AlertDialog.Builder(parent);
                                f42_builder2.setMessage(R.string.zeno_42f_1);
                                AlertDialog f42_dialog2 = f42_builder2.create();
                                f42_dialog2.setCanceledOnTouchOutside(true);
                                f42_dialog2.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        proceed = true;
                                    }
                                });
                                f42_dialog2.show();
                            }
                        });
                    } else if (act == 4) {
                        if (!proceed)
                            return;
                        proceed = false;
                        sleep(500);
                        act++;
                        current_floor[7][6] = 57;
                        current_floor[9][6] = 57;
                        current_floor[8][5] = 57;
                        current_floor[8][7] = 57;
                        refresh_ctr = true;
                    } else if (act == 5) {
                        sleep(500);
                        act++;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder f42_builder3 = new AlertDialog.Builder(parent);
                                f42_builder3.setMessage(R.string.knight_captain_42f_2);
                                AlertDialog f42_dialog3 = f42_builder3.create();
                                f42_dialog3.setCanceledOnTouchOutside(true);
                                f42_dialog3.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        proceed = true;
                                    }
                                });
                                f42_dialog3.show();
                            }
                        });
                    } else if (act == 6) {
                        if (!proceed)
                            return;
                        proceed = false;
                        sleep(500);
                        act++;
                        current_floor[8][6] = 10;
                        refresh_ctr = true;
                    } else if (act == 7) {
                        sleep(500);
                        act++;
                        current_floor[8][6] = 1;
                        refresh_ctr = true;
                    } else if (act == 8) {
                        sleep(500);
                        act++;
                        current_floor[7][6] = 1;
                        current_floor[9][6] = 1;
                        current_floor[8][5] = 1;
                        current_floor[8][7] = 1;
                        refresh_ctr = true;
                    } else if (act == 9) {
                        sleep(500);
                        act++;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder f42_builder4 = new AlertDialog.Builder(parent);
                                f42_builder4.setMessage(R.string.zeno_42f_2);
                                AlertDialog f42_dialog4 = f42_builder4.create();
                                f42_dialog4.setCanceledOnTouchOutside(true);
                                f42_dialog4.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        proceed = true;
                                    }
                                });
                                f42_dialog4.show();
                            }
                        });
                    } else {
                        if (!proceed)
                            return;
                        proceed = false;
                        sleep(500);
                        act = 0;
                        current_floor[6][6] = 1;
                        refresh_ctr = true;
                        isEvent = false;
                    }
                    break;
                case 43:
                    if (act == 0) {
                        act++;
                    } else if (act == 1) {
                        sleep(300);
                        act++;
                        current_floor[1][7] = 0;
                        refresh_ctr = true;
                    } else if (act == 2) {
                        sleep(300);
                        act++;
                        current_floor[1][9] = 1;
                        current_floor[1][10] = 57;
                        refresh_ctr = true;
                    } else if (act == 3) {
                        sleep(300);
                        act++;
                        current_floor[1][10] = 1;
                        current_floor[1][11] = 57;
                        refresh_ctr = true;
                    } else if (act == 4) {
                        sleep(300);
                        act = 0;
                        current_floor[1][10] = 0;
                        refresh_ctr = true;
                        isEvent = false;
                    }
                    break;
                case 49:
                    if (act == 0) {
                        act++;
                        current_floor[6][6] = 1;
                    } else if (act == 1) {
                        sleep(500);
                        act++;
                        current_floor[7][6] = 8;
                        refresh_ctr = true;
                    } else if (act == 2) {
                        sleep(500);
                        act++;
                        current_floor[3][6] = 60;
                        refresh_ctr = true;
                    } else if (act == 3) {
                        sleep(500);
                        act++;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder f49_builder1 = new AlertDialog.Builder(parent);
                                f49_builder1.setMessage(R.string.zeno_49f_1);
                                AlertDialog f49_dialog1 = f49_builder1.create();
                                f49_dialog1.setCanceledOnTouchOutside(true);
                                f49_dialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        proceed = true;
                                    }
                                });
                                f49_dialog1.show();
                            }
                        });
                    } else if (act == 4) {
                        if (!proceed)
                            return;
                        proceed = false;
                        if (background_music != null)           // kill the old music first
                            background_music.release();
                        background_music = MediaPlayer.create(getApplicationContext(), boss_music_list[4]);
                        background_music.setLooping(true);
                        if (music_settings[0])                  // if bgm option is checked
                            background_music.start();
                        sleep(500);
                        act++;
                        current_floor[2][5] = 57;
                        refresh_ctr = true;
                    } else if (act == 5) {
                        sleep(500);
                        act++;
                        current_floor[2][6] = 57;
                        refresh_ctr = true;
                    } else if (act == 6) {
                        sleep(500);
                        act++;
                        current_floor[2][7] = 57;
                        refresh_ctr = true;
                    } else if (act == 7) {
                        sleep(500);
                        act++;
                        current_floor[3][7] = 57;
                        refresh_ctr = true;
                    } else if (act == 8) {
                        sleep(500);
                        act++;
                        current_floor[4][7] = 57;
                        refresh_ctr = true;
                    } else if (act == 9) {
                        sleep(500);
                        act++;
                        current_floor[4][6] = 57;
                        refresh_ctr = true;
                    } else if (act == 10) {
                        sleep(500);
                        act++;
                        current_floor[4][5] = 57;
                        refresh_ctr = true;
                    } else if (act == 11) {
                        sleep(500);
                        act++;
                        current_floor[3][5] = 57;
                        refresh_ctr = true;
                        isEvent = false;
                    } else if (act == 12) {
                        act++;
                    } else if (act == 13) {
                        sleep(500);
                        act++;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder f49_builder2 = new AlertDialog.Builder(parent);
                                f49_builder2.setMessage(R.string.zeno_49f_2);
                                AlertDialog f49_dialog2 = f49_builder2.create();
                                f49_dialog2.setCanceledOnTouchOutside(true);
                                f49_dialog2.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        proceed = true;
                                    }
                                });
                                f49_dialog2.show();
                            }
                        });
                    } else if (act == 14) {
                        if (!proceed)
                            return;
                        proceed = false;
                        sleep(500);
                        act++;
                        current_floor[3][6] = 61;
                        refresh_ctr = true;
                        isEvent = false;
                    } else if (act == 15) {
                        sleep(500);
                        if (background_music != null)           // kill the old music first
                            background_music.release();
                        background_music = MediaPlayer.create(getApplicationContext(), bgm_list[4]);
                        background_music.setLooping(true);
                        if (music_settings[0])                  // if bgm option is checked
                            background_music.start();
                        act++;
                        current_floor[2][5] = 1;
                        current_floor[2][7] = 1;
                        current_floor[4][5] = 1;
                        current_floor[4][7] = 1;
                        current_floor[7][6] = 1;
                        refresh_ctr = true;
                    } else if (act == 16) {
                        sleep(500);
                        act++;
                        current_floor[5][5] = 75;
                        current_floor[5][6] = 75;
                        current_floor[5][7] = 75;
                        refresh_ctr = true;
                    } else if (act == 17) {
                        sleep(500);
                        act++;
                        current_floor[4][2] = 76;
                        current_floor[4][3] = 76;
                        current_floor[4][4] = 76;
                        current_floor[4][8] = 77;
                        current_floor[4][9] = 77;
                        current_floor[4][10] = 77;
                        refresh_ctr = true;
                    } else {
                        sleep(500);
                        act = 0;
                        current_floor[2][5] = 73;
                        current_floor[2][7] = 91;
                        refresh_ctr = true;
                        isEvent = false;
                    }
                    break;
                case 50:
                    if (thief_event_count == 0) {
                        current_floor[5][6] = 66;
                        refresh_ctr = true;
                    }
                    isEvent = false;
                    break;
                default:
                    isEvent = true;
                    break;
            }
        }

        private void button_logic(int inButton) {
            final int xu, yu, xd, yd, xl, yl, xr, yr;
            if (hero_x > 1)
                xl = hero_x - 1;
            else
                xl = hero_x;
            if (hero_x < 11)
                xr = hero_x + 1;
            else
                xr = hero_x;
            xu = hero_x;
            xd = hero_x;
            if (hero_y > 1)
                yu = hero_y - 1;
            else
                yu = hero_y;
            if (hero_y < 11)
                yd = hero_y + 1;
            else
                yd = hero_y;
            yl = hero_y;
            yr = hero_y;
            switch (inButton) {
                case UP:
                    hero_sprite.set_direction(UP);
                    walk_result = move(UP);
                    if (walk_result != 0)
                        hero_y--;
                    break;
                case DOWN:
                    hero_sprite.set_direction(DOWN);
                    walk_result = move(DOWN);
                    if (walk_result != 0)
                        hero_y++;
                    break;
                case LEFT:
                    hero_sprite.set_direction(LEFT);
                    walk_result = move(LEFT);
                    if (walk_result != 0)
                        hero_x--;
                    break;
                case RIGHT:
                    hero_sprite.set_direction(RIGHT);
                    walk_result = move(RIGHT);
                    if (walk_result != 0)
                        hero_x++;
                    break;
                case STAFF1:
                    break;
                case STAFF2:
                    break;
                case FLY_UP:
                    sleep(150);
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_teleport);
                    if (music_settings[1])
                        sfx_music.start();
                    if (floor_num%10 == 0 && floor_num != 0) {  // if needs to change music
                        if (background_music != null)           // kill the old music first
                            background_music.release();
                        background_music = MediaPlayer.create(getApplicationContext(), bgm_list[floor_num/10]);
                        background_music.setLooping(true);
                        if (music_settings[0])                  // if bgm option is checked
                            background_music.start();
                    }
                    if (floor_num < 50) {
                        current_game.put_one_floor(floor_num, current_floor);
                        floor_num++;
                        int pairup[] = find_hero_next_floor(true, floor_num);
                        refresh_ctr = true;
                        hero_y = pairup[0];
                        hero_x = pairup[1];
                    }
                    break;
                case FLY_DOWN:
                    sleep(150);
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_teleport);
                    if (music_settings[1])
                        sfx_music.start();
                    if (floor_num%10 == 1 && floor_num != 1) {  // if needs to change music
                        if (background_music != null)           // kill the old music first
                            background_music.release();
                        background_music = MediaPlayer.create(getApplicationContext(), bgm_list[floor_num/10 - 1]);
                        background_music.setLooping(true);
                        if (music_settings[0])                  // if bgm option is checked
                            background_music.start();
                    }
                    if (floor_num > 0) {
                        current_game.put_one_floor(floor_num, current_floor);
                        floor_num--;
                        int pairup[] = find_hero_next_floor(false, floor_num);
                        refresh_ctr = true;
                        hero_y = pairup[0];
                        hero_x = pairup[1];
                    }
                    break;
                case ITEM1:     // cross
                    if (cross && no_dialog) {
                        no_dialog = false;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder cross_builder = new AlertDialog.Builder(parent);
                                cross_builder.setTitle(R.string.item_title_cross);
                                cross_builder.setMessage(R.string.item_description_cross);
                                cross_builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                AlertDialog cross_dialog = cross_builder.create();
                                cross_dialog.setCanceledOnTouchOutside(true);
                                cross_dialog.show();
                            }
                        });
                    }
                    break;
                case ITEM2:     // lucky gold
                    if (lucky_gold && no_dialog) {
                        no_dialog = false;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder lucky_gold_builder = new AlertDialog.Builder(parent);
                                lucky_gold_builder.setTitle(R.string.item_title_lucky_gold);
                                lucky_gold_builder.setMessage(R.string.item_description_lucky_gold);
                                lucky_gold_builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                AlertDialog lucky_gold_dialog = lucky_gold_builder.create();
                                lucky_gold_dialog.setCanceledOnTouchOutside(true);
                                lucky_gold_dialog.show();
                            }
                        });
                    }
                    break;
                case ITEM3:     // mattock
                    if (m_mattock && no_dialog) {
                        no_dialog = false;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder m_mattock_builder = new AlertDialog.Builder(parent);
                                m_mattock_builder.setTitle(R.string.item_title_mattock);
                                m_mattock_builder.setMessage(R.string.item_description_mattock);
                                m_mattock_builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        boolean wall_nearby = false;
                                        if (current_floor[yu][xu] == 0) {
                                            current_floor[yu][xu] = 1;
                                            wall_nearby = true;
                                        }
                                        if (current_floor[yd][xd] == 0) {
                                            current_floor[yd][xd] = 1;
                                            wall_nearby = true;
                                        }
                                        if (current_floor[yl][xl] == 0) {
                                            current_floor[yl][xl] = 1;
                                            wall_nearby = true;
                                        }
                                        if (current_floor[yr][xr] == 0) {
                                            current_floor[yr][xr] = 1;
                                            wall_nearby = true;
                                        }
                                        if (wall_nearby) {
                                            m_mattock = false;
                                            refresh_ctr = true;
                                        }
                                    }
                                });
                                m_mattock_builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                AlertDialog m_mattock_dialog = m_mattock_builder.create();
                                m_mattock_dialog.setCanceledOnTouchOutside(true);
                                m_mattock_dialog.show();
                            }
                        });
                    }
                    break;
                case ITEM4:     // wing up
                    if (wing_up && no_dialog) {
                        no_dialog = false;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder wing_up_builder = new AlertDialog.Builder(parent);
                                wing_up_builder.setTitle(R.string.item_title_wing_up);
                                wing_up_builder.setMessage(R.string.item_description_wing_up);
                                wing_up_builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        boolean can_teleport = false;
                                        if (current_floor[yu][xu] == 3 || current_floor[yu][xu] == 4) {
                                            can_teleport = true;
                                        } else if (current_floor[yd][xd] == 3 || current_floor[yd][xd] == 4) {
                                            can_teleport = true;
                                        } else if (current_floor[yl][xl] == 3 || current_floor[yl][xl] == 4) {
                                            can_teleport = true;
                                        } else if (current_floor[yr][xr] == 3 || current_floor[yr][xr] == 4) {
                                            can_teleport = true;
                                        }
                                        if (can_teleport) {
                                            sleep(150);
                                            if (floor_num < 49) {
                                                current_game.put_one_floor(floor_num, current_floor);
                                                floor_num++;
                                                int pairup[] = find_hero_next_floor(true, floor_num);
                                                refresh_ctr = true;
                                                hero_y = pairup[0];
                                                hero_x = pairup[1];
                                            }
                                            sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                                            if (music_settings[1])
                                                sfx_music.start();
                                            wing_up = false;
                                        }
                                    }
                                });
                                wing_up_builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                AlertDialog wing_up_dialog = wing_up_builder.create();
                                wing_up_dialog.setCanceledOnTouchOutside(true);
                                wing_up_dialog.show();
                            }
                        });
                    }
                    break;
                case ITEM5:     // wing center
                    if (wing_cent && no_dialog) {
                        no_dialog = false;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder wing_cent_builder = new AlertDialog.Builder(parent);
                                wing_cent_builder.setTitle(R.string.item_title_wing_center);
                                String full_message = getString(R.string.item_description_wing_center) + " (teleport left: " + String.valueOf(count_wing) + ")";
                                wing_cent_builder.setMessage(full_message);
                                wing_cent_builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        boolean can_teleport = current_floor[12-hero_y][12-hero_x] == -9 || current_floor[12-hero_y][12-hero_x] == -8;
                                        can_teleport |= current_floor[12-hero_y][12-hero_x] == -7 || current_floor[12-hero_y][12-hero_x] == -6;
                                        can_teleport |= current_floor[12-hero_y][12-hero_x] == 1;
                                        if (can_teleport) {
                                            sleep(150);
                                            hero_y = 12 - hero_y;
                                            hero_x = 12 - hero_x;
                                            count_wing--;
                                            sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                                            if (music_settings[1])
                                                sfx_music.start();
                                            if (count_wing == 0)
                                                wing_cent = false;
                                        }
                                    }
                                });
                                wing_cent_builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                AlertDialog wing_cent_dialog = wing_cent_builder.create();
                                wing_cent_dialog.setCanceledOnTouchOutside(true);
                                wing_cent_dialog.show();
                            }
                        });
                    }
                    break;
                case ITEM6:     // wing down
                    if (wing_down && no_dialog) {
                        no_dialog = false;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder wing_down_builder = new AlertDialog.Builder(parent);
                                wing_down_builder.setTitle(R.string.item_title_wing_down);
                                wing_down_builder.setMessage(R.string.item_description_wing_down);
                                wing_down_builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        boolean can_teleport = false;
                                        if (current_floor[yu][xu] == 3 || current_floor[yu][xu] == 4) {
                                            can_teleport = true;
                                        } else if (current_floor[yd][xd] == 3 || current_floor[yd][xd] == 4) {
                                            can_teleport = true;
                                        } else if (current_floor[yl][xl] == 3 || current_floor[yl][xl] == 4) {
                                            can_teleport = true;
                                        } else if (current_floor[yr][xr] == 3 || current_floor[yr][xr] == 4) {
                                            can_teleport = true;
                                        }
                                        if (can_teleport) {
                                            sleep(150);
                                            if (floor_num > 0) {
                                                current_game.put_one_floor(floor_num, current_floor);
                                                floor_num--;
                                                int pairup[] = find_hero_next_floor(false, floor_num);
                                                refresh_ctr = true;
                                                hero_y = pairup[0];
                                                hero_x = pairup[1];
                                            }
                                            sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                                            if (music_settings[1])
                                                sfx_music.start();
                                            wing_down = false;
                                        }
                                    }
                                });
                                wing_down_builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                AlertDialog wing_down_dialog = wing_down_builder.create();
                                wing_down_dialog.setCanceledOnTouchOutside(true);
                                wing_down_dialog.show();
                            }
                        });
                    }
                    break;
                case ITEM7:     // dragonsbane
                    if (dragonsbane && no_dialog) {
                        no_dialog = false;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder dragonsbane_builder = new AlertDialog.Builder(parent);
                                dragonsbane_builder.setTitle(R.string.item_title_dragonsbane);
                                dragonsbane_builder.setMessage(R.string.item_description_dragonsbane);
                                dragonsbane_builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                AlertDialog dragonsbane_dialog = dragonsbane_builder.create();
                                dragonsbane_dialog.setCanceledOnTouchOutside(true);
                                dragonsbane_dialog.show();
                            }
                        });
                    }
                    break;
                case ITEM8:     // snow crystal
                    if (snow_cryst && no_dialog) {
                        no_dialog = false;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder snow_cryst_builder = new AlertDialog.Builder(parent);
                                snow_cryst_builder.setTitle(R.string.item_title_snow_crystal);
                                snow_cryst_builder.setMessage(R.string.item_description_snow_crystal);
                                snow_cryst_builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                AlertDialog snow_cryst_dialog = snow_cryst_builder.create();
                                snow_cryst_dialog.setCanceledOnTouchOutside(true);
                                snow_cryst_dialog.show();
                            }
                        });
                    }
                    break;
                case ITEM9:     // enhanced mattock
                    if (e_mattock && no_dialog) {
                        no_dialog = false;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder e_mattock_builder = new AlertDialog.Builder(parent);
                                e_mattock_builder.setTitle(R.string.item_title_enhanced_mattock);
                                e_mattock_builder.setMessage(R.string.item_description_enhanced_mattock);
                                e_mattock_builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int in_int) {
                                        for (int i = 1; i < 12; i++) {
                                            for (int j = 1; j < 12; j++) {
                                                if (current_floor[i][j] == 0) {
                                                    current_floor[i][j] = 1;
                                                }
                                            }
                                        }
                                        refresh_ctr = true;
                                        e_mattock = false;
                                    }
                                });
                                e_mattock_builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                AlertDialog e_mattock_dialog = e_mattock_builder.create();
                                e_mattock_dialog.setCanceledOnTouchOutside(true);
                                e_mattock_dialog.show();
                            }
                        });
                    }
                    break;
                case ITEM10:     // elixir
                    if (elixir && no_dialog) {
                        no_dialog = false;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder elixir_builder = new AlertDialog.Builder(parent);
                                elixir_builder.setTitle(R.string.item_title_elixir);
                                elixir_builder.setMessage(R.string.item_description_elixir);
                                elixir_builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int in_int) {
                                        hp += 10*atk + 5*def;
                                        sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_potion);
                                        if (music_settings[1])
                                            sfx_music.start();
                                        elixir = false;
                                    }
                                });
                                elixir_builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                AlertDialog elixir_dialog = elixir_builder.create();
                                elixir_dialog.setCanceledOnTouchOutside(true);
                                elixir_dialog.show();
                            }
                        });
                    }
                    break;
                case ITEM11:     // enhanced yellow key
                    if (key_enhac && no_dialog) {
                        no_dialog = false;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder key_enhac_builder = new AlertDialog.Builder(parent);
                                key_enhac_builder.setTitle(R.string.item_title_enhanced_key);
                                key_enhac_builder.setMessage(R.string.item_description_enhanced_key);
                                key_enhac_builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int in_int) {
                                        for (int i = 1; i < 12; i++) {
                                            for (int j = 1; j < 12; j++) {
                                                if (current_floor[i][j] == 5) {
                                                    current_floor[i][j] = 1;
                                                }
                                            }
                                        }
                                        refresh_ctr = true;
                                        sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_open_doors);
                                        if (music_settings[1])
                                            sfx_music.start();
                                        key_enhac = false;
                                    }
                                });
                                key_enhac_builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                AlertDialog key_enhac_dialog = key_enhac_builder.create();
                                key_enhac_dialog.setCanceledOnTouchOutside(true);
                                key_enhac_dialog.show();
                            }
                        });
                    }
                    break;
                case ITEM12:     // bomb
                    if (bomb && no_dialog) {
                        no_dialog = false;
                        parent.runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog.Builder bomb_builder = new AlertDialog.Builder(parent);
                                bomb_builder.setTitle(R.string.item_title_bomb);
                                bomb_builder.setMessage(R.string.item_description_bomb);
                                bomb_builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int in_int) {
                                        if (current_floor[yu][xu] > 30 && current_floor[yu][xu] < 60 && current_floor[yu][xu] != 52) {
                                            if (lucky_gold)
                                                gold += 2*m_table[current_floor[yu][xu]-31][3];
                                            else
                                                gold += m_table[current_floor[yu][xu]-31][3];
                                            current_floor[yu][xu] = 1;
                                            bomb = false;
                                        }
                                        if (current_floor[yd][xd] > 30 && current_floor[yd][xd] < 60 && current_floor[yd][xd] != 52) {
                                            if (lucky_gold)
                                                gold += 2*m_table[current_floor[yd][xd]-31][3];
                                            else
                                                gold += m_table[current_floor[yd][xd]-31][3];
                                            current_floor[yd][xd] = 1;
                                            bomb = false;
                                        }
                                        if (current_floor[yl][xl] > 30 && current_floor[yl][xl] < 60 && current_floor[yl][xl] != 52) {
                                            if (lucky_gold)
                                                gold += 2*m_table[current_floor[yl][xl]-31][3];
                                            else
                                                gold += m_table[current_floor[yl][xl]-31][3];
                                            current_floor[yl][xl] = 1;
                                            bomb = false;
                                        }
                                        if (current_floor[yr][xr] > 30 && current_floor[yr][xr] < 60 && current_floor[yr][xr] != 52) {
                                            if (lucky_gold)
                                                gold += 2*m_table[current_floor[yr][xr]-31][3];
                                            else
                                                gold += m_table[current_floor[yr][xr]-31][3];
                                            current_floor[yr][xr] = 1;
                                            bomb = false;
                                        }
                                        check_complete();
                                        refresh_ctr = true;
                                    }
                                });
                                bomb_builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                AlertDialog bomb_dialog = bomb_builder.create();
                                bomb_dialog.setCanceledOnTouchOutside(true);
                                bomb_dialog.show();
                            }
                        });
                    }
                    break;
                case SAVE:
                    if (no_dialog) {
                        no_dialog = false;
                        Intent save_game = new Intent(Gamelogic.this, SaveActivity.class);
                        prepare_to_save_game();
                        save_game.putExtra("Game_Data", game_data_to_save);
                        startActivity(save_game);
                    }
                    break;
                default:
                    break;
            }
        }

        private void load_objects(byte[][] curr_floor) {
            all_sprites.clear();
            int origin = 0 - sq_size / 2;
            for (int i = 1; i < 12; i++) {
                for (int j = 1; j < 12; j++) {
                    byte b;
                    switch (curr_floor[i][j]) {
                        case -9:
                            b = -9;
                            Sprite magic_damageh = new Sprite(GameView.this, pic_dh, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(magic_damageh);
                            break;
                        case -8:
                            b = -8;
                            Sprite magic_damage3 = new Sprite(GameView.this, pic_d3, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(magic_damage3);
                            break;
                        case -7:
                            b = -7;
                            Sprite magic_damage2 = new Sprite(GameView.this, pic_d2, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(magic_damage2);
                            break;
                        case -6:
                            b = -6;
                            Sprite magic_damage1 = new Sprite(GameView.this, pic_d1, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(magic_damage1);
                            break;
                        case -5:    // invisible wall
                            break;
                        case -4:    // event floor
                            b = -4;
                            Sprite sp_event_floor = new Sprite(GameView.this, t__floor, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_event_floor);
                            break;
                        case -3:    // event wall
                            b = -3;
                            Sprite sp_event_wall = new Sprite(GameView.this, t___wall, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_event_wall);
                            break;
                        case -2:    // fake floor
                            b = -2;
                            Sprite sp_fake_floor = new Sprite(GameView.this, t__floor, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_fake_floor);
                            break;
                        case -1:    // fake wall
                            b = -1;
                            Sprite sp_fake_wall = new Sprite(GameView.this, t___wall, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_fake_wall);
                            break;
                        case 0:     // wall
                            b = 0;
                            Sprite sp_wall = new Sprite(GameView.this, t___wall, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_wall);
                            break;
                        case 1:     // floor
                            b = 1;
                            Sprite sp_floor = new Sprite(GameView.this, t__floor, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_floor);
                            break;
                        case 2:     // star
                            b = 2;
                            Sprite sp_star = new Sprite(GameView.this, t___star, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_star);
                            break;
                        case 3:     // upstairs
                            b = 3;
                            Sprite sp_upstairs = new Sprite(GameView.this, t_ustair, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_upstairs);
                            break;
                        case 4:     // downstairs
                            b = 4;
                            Sprite sp_downstairs = new Sprite(GameView.this, t_dstair, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_downstairs);
                            break;
                        case 5:     // door_yellow
                            b = 5;
                            Sprite sp_door_y = new Sprite(GameView.this, t_door_y, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_door_y);
                            break;
                        case 6:     // door_blue
                            b = 6;
                            Sprite sp_door_b = new Sprite(GameView.this, t_door_b, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_door_b);
                            break;
                        case 7:     // door_red
                            b = 7;
                            Sprite sp_door_r = new Sprite(GameView.this, t_door_r, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_door_r);
                            break;
                        case 8:     // door_magic
                            b = 8;
                            Sprite sp_door_m = new Sprite(GameView.this, t_door_m, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_door_m);
                            break;
                        case 9:     // prison
                            b = 9;
                            Sprite sp_prison = new Sprite(GameView.this, t_prison, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_prison);
                            break;
                        case 10:     // logo
                            b = 10;
                            Sprite sp_logo = new Sprite(GameView.this, t___logo, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_logo);
                            break;
                        case 11:    // iron_sword
                            b = 11;
                            Sprite sp_iron_sword = new Sprite(GameView.this, w___ironw, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_iron_sword);
                            break;
                        case 12:    // iron_shield
                            b = 12;
                            Sprite sp_iron_shield = new Sprite(GameView.this, w___ironh, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_iron_shield);
                            break;
                        case 13:    // silver_sword
                            b = 13;
                            Sprite sp_silver_sword = new Sprite(GameView.this, w_silverw, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_silver_sword);
                            break;
                        case 14:    // silver_shield
                            b = 14;
                            Sprite sp_silver_shield = new Sprite(GameView.this, w_silverh, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_silver_shield);
                            break;
                        case 15:    // knight_sword
                            b = 15;
                            Sprite sp_knight_sword = new Sprite(GameView.this, w_knightw, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_knight_sword);
                            break;
                        case 16:    // knight_shield
                            b = 16;
                            Sprite sp_knight_shield = new Sprite(GameView.this, w_knighth, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_knight_shield);
                            break;
                        case 17:    // divine_sword
                            b = 17;
                            Sprite sp_divine_sword = new Sprite(GameView.this, w_divinew, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_divine_sword);
                            break;
                        case 18:    // divine_shield
                            b = 18;
                            Sprite sp_divine_shield = new Sprite(GameView.this, w_divineh, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_divine_shield);
                            break;
                        case 19:    // sacred_sword
                            b = 19;
                            Sprite sp_sacred_sword = new Sprite(GameView.this, w_sacredw, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_sacred_sword);
                            break;
                        case 20:    // sacred_shield
                            b = 20;
                            Sprite sp_sacred_shield = new Sprite(GameView.this, w_sacredh, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_sacred_shield);
                            break;
                        case 21:    // thief
                            b = 21;
                            Sprite sp_thief = new Sprite(GameView.this, n___thief, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_thief);
                            break;
                        case 22:    // saint
                            b = 22;
                            Sprite sp_saint = new Sprite(GameView.this, n___saint, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_saint);
                            break;
                        case 23:    // merchant
                            b = 23;
                            Sprite sp_merchant = new Sprite(GameView.this, n_merchat, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_merchant);
                            break;
                        case 24:    // fairy
                            b = 24;
                            Sprite sp_fairy = new Sprite(GameView.this, n___fairy, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_fairy);
                            break;
                        case 25:    // shop_left
                            b = 25;
                            Sprite sp_shop_l = new Sprite(GameView.this, n_shop__l, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_shop_l);
                            break;
                        case 26:    // shop_middle
                            b = 26;
                            Sprite sp_shop_m = new Sprite(GameView.this, n_shop__m, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_shop_m);
                            break;
                        case 27:    // shop_right
                            b = 27;
                            Sprite sp_shop_r = new Sprite(GameView.this, n_shop__r, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_shop_r);
                            break;
                        case 28:    // princess
                            b = 28;
                            Sprite sp_princess = new Sprite(GameView.this, n_princes, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_princess);
                            break;
                        case 29:    // lava
                            b = 29;
                            Sprite sp_lava = new Sprite(GameView.this, n____lava, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_lava);
                            break;
                        case 31:    // green slime
                            b = 31;
                            Sprite sp_slime_g = new Sprite(GameView.this, m__slime_g, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_slime_g);
                            break;
                        case 32:    // red slime
                            b = 32;
                            Sprite sp_slime_r = new Sprite(GameView.this, m__slime_r, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_slime_r);
                            break;
                        case 33:    // fierce bat
                            b = 33;
                            Sprite sp_fierce_bat = new Sprite(GameView.this, m_bat_fier, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_fierce_bat);
                            break;
                        case 34:    // priest
                            b = 34;
                            Sprite sp_priest = new Sprite(GameView.this, m___priest, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_priest);
                            break;
                        case 35:    // skeleton
                            b = 35;
                            Sprite sp_skeleton = new Sprite(GameView.this, m_skeleton, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_skeleton);
                            break;
                        case 36:    // skeleton_warrior
                            b = 36;
                            Sprite sp_skeleton_warrior = new Sprite(GameView.this, m_skelet_w, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_skeleton_warrior);
                            break;
                        case 37:    // gate_keeper
                            b = 37;
                            Sprite sp_gate_keeper = new Sprite(GameView.this, m_gatekeep, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_gate_keeper);
                            break;
                        case 38:    // skeleton_captain
                            b = 38;
                            Sprite sp_skeleton_captain = new Sprite(GameView.this, m_skelet_c, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_skeleton_captain);
                            break;
                        case 39:    // black_slime
                            b = 39;
                            Sprite sp_slime_b = new Sprite(GameView.this, m__slime_b, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_slime_b);
                            break;
                        case 40:    // giant bat
                            b = 40;
                            Sprite sp_giant_bat = new Sprite(GameView.this, m_bat_gian, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_giant_bat);
                            break;
                        case 41:    // priest_master
                            b = 41;
                            Sprite sp_priest_master = new Sprite(GameView.this, m_priest_m, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_priest_master);
                            break;
                        case 42:    // zombie
                            b = 42;
                            Sprite sp_zombie = new Sprite(GameView.this, m___zombie, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_zombie);
                            break;
                        case 43:    // stone_guardian
                            b = 43;
                            Sprite sp_stone_guardian = new Sprite(GameView.this, m_stone_gd, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_stone_guardian);
                            break;
                        case 44:    // zombie_warrior
                            b = 44;
                            Sprite sp_zombie_warrior = new Sprite(GameView.this, m_zombie_w, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_zombie_warrior);
                            break;
                        case 45:    // vampire
                            b = 45;
                            Sprite sp_vampire = new Sprite(GameView.this, m__vampire, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_vampire);
                            break;
                        case 46:    // slime_man
                            b = 46;
                            Sprite sp_slime_man = new Sprite(GameView.this, m__slime_m, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_slime_man);
                            break;
                        case 47:    // skeleton_elite
                            b = 47;
                            Sprite sp_skeleton_elite = new Sprite(GameView.this, m_skelet_e, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_skeleton_elite);
                            break;
                        case 48:    // knight
                            b = 48;
                            Sprite sp_knight = new Sprite(GameView.this, m___knight, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_knight);
                            break;
                        case 49:    // gatekeeper_elite
                            b = 49;
                            Sprite sp_gatekeeper_elite = new Sprite(GameView.this, m_gatekp_e, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_gatekeeper_elite);
                            break;
                        case 50:    // swordsman
                            b = 50;
                            Sprite sp_swordsman = new Sprite(GameView.this, m_swordsmn, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_swordsman);
                            break;
                        case 51:    // knight_elite
                            b = 51;
                            Sprite sp_knight_elite = new Sprite(GameView.this, m_knight_e, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_knight_elite);
                            break;
                        case 52:    // knight_captain
                            b = 52;
                            Sprite sp_knight_captain = new Sprite(GameView.this, m_knight_c, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_knight_captain);
                            break;
                        case 53:    // slimelord
                            b = 53;
                            Sprite sp_slimelord = new Sprite(GameView.this, m_slimelod, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_slimelord);
                            break;
                        case 54:    // vampire_bat
                            b = 54;
                            Sprite sp_vampire_bat = new Sprite(GameView.this, m_bat_vamp, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_vampire_bat);
                            break;
                        case 55:    // mage
                            b = 55;
                            Sprite sp_mage = new Sprite(GameView.this, m_____mage, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_mage);
                            break;
                        case 56:    // mage master
                            b = 56;
                            Sprite sp_mage_master = new Sprite(GameView.this, m_mage_mas, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_mage_master);
                            break;
                        case 57:    // demo_sergent
                            b = 57;
                            Sprite sp_demo_sergent = new Sprite(GameView.this, m_demo_sgt, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_demo_sergent);
                            break;
                        case 58:    // dark_knight
                            b = 58;
                            Sprite sp_dark_knight = new Sprite(GameView.this, m_d_knight, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_dark_knight);
                            break;
                        case 59:    // gate_guardian
                            b = 59;
                            Sprite sp_gate_guardian = new Sprite(GameView.this, m_gate_gdn, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_gate_guardian);
                            break;
                        case 60:    // fake_zeno1
                            b = 60;
                            Sprite sp_fake_zeno1 = new Sprite(GameView.this, m_demozeno, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_fake_zeno1);
                            break;
                        case 61:    // fake_zeno2
                            b = 61;
                            Sprite sp_real_zeno2 = new Sprite(GameView.this, m_demozeno, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_real_zeno2);
                            break;
                        case 62:    // octopus center
                            break;
                        case 63:    // octopus sides
                            break;
                        case 64:    // dragon
                            break;
                        case 65:    // archmage
                            b = 65;
                            Sprite sp_archmage = new Sprite(GameView.this, m_archmage, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_archmage);
                            break;
                        case 66:    // real zeno
                            b = 66;
                            Sprite sp_real_zeno = new Sprite(GameView.this, m_demozeno, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_real_zeno);
                            break;
                        case 71:    // yellow_key
                            b = 71;
                            Sprite sp_key_y = new Sprite(GameView.this, i____key_y, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_key_y);
                            break;
                        case 72:    // blue_key
                            b = 72;
                            Sprite sp_key_b = new Sprite(GameView.this, i____key_b, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_key_b);
                            break;
                        case 73:    // red_key
                            b = 73;
                            Sprite sp_key_r = new Sprite(GameView.this, i____key_r, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_key_r);
                            break;
                        case 74:    // red_potion
                            b = 74;
                            Sprite sp_potion_r = new Sprite(GameView.this, i_potion_r, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_potion_r);
                            break;
                        case 75:    // blue_potion
                            b = 75;
                            Sprite sp_potion_b = new Sprite(GameView.this, i_potion_b, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_potion_b);
                            break;
                        case 76:    // red_crystal
                            b = 76;
                            Sprite sp_crystal_r = new Sprite(GameView.this, i_crystl_r, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_crystal_r);
                            break;
                        case 77:    // blue_crystal
                            b = 77;
                            Sprite sp_crystal_b = new Sprite(GameView.this, i_crystl_b, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_crystal_b);
                            break;
                        case 78:    // staff_of_wisdom
                            b = 78;
                            Sprite sp_staff_wisdom = new Sprite(GameView.this, i_stf_wsdm, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_staff_wisdom);
                            break;
                        case 79:    // staff_of_echo
                            b = 79;
                            Sprite sp_staff_echo = new Sprite(GameView.this, i_stf_echo, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_staff_echo);
                            break;
                        case 80:    // staff_of_space
                            b = 80;
                            Sprite sp_staff_space = new Sprite(GameView.this, i_stf_spce, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_staff_space);
                            break;
                        case 81:    // cross
                            b = 81;
                            Sprite sp_cross = new Sprite(GameView.this, i____cross, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_cross);
                            break;
                        case 82:    // elixir
                            b = 82;
                            Sprite sp_elixir = new Sprite(GameView.this, i___elixir, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_elixir);
                            break;
                        case 83:    // magic_mattock
                            b = 83;
                            Sprite sp_magic_mattock = new Sprite(GameView.this, i_m_mattok, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_magic_mattock);
                            break;
                        case 84:    // magic_wing_center
                            b = 84;
                            Sprite sp_magic_wing_center = new Sprite(GameView.this, i_wing_cen, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_magic_wing_center);
                            break;
                        case 85:    // enhanced_mattock
                            b = 85;
                            Sprite sp_enhanced_mattock = new Sprite(GameView.this, i_e_mattok, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_enhanced_mattock);
                            break;
                        case 86:    // bomb
                            b = 86;
                            Sprite sp_bomb = new Sprite(GameView.this, i_____bomb, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_bomb);
                            break;
                        case 87:    // magic_wing_up
                            b = 87;
                            Sprite sp_magic_wing_up = new Sprite(GameView.this, i__wing_up, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_magic_wing_up);
                            break;
                        case 88:    // enhanced_key
                            b = 88;
                            Sprite sp_enhanced_key = new Sprite(GameView.this, i_key_ehac, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_enhanced_key);
                            break;
                        case 89:    // magic_wing_down
                            b = 89;
                            Sprite sp_magic_wing_down = new Sprite(GameView.this, i_wing_dow, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_magic_wing_down);
                            break;
                        case 90:    // lucky_gold
                            b = 90;
                            Sprite sp_lucky_gold = new Sprite(GameView.this, i_lucky_gd, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_lucky_gold);
                            break;
                        case 91:    // dragonsbane
                            b = 91;
                            Sprite sp_dragonsbane = new Sprite(GameView.this, i_dra_bane, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_dragonsbane);
                            break;
                        case 92:    // snow_crystal
                            b = 92;
                            Sprite sp_snow_crystal = new Sprite(GameView.this, i_snow_crs, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(sp_snow_crystal);
                            break;
                        default:    // debug
                            b = 10;
                            Sprite mydebug = new Sprite(GameView.this, t___logo, origin + j * sq_size, origin + i * sq_size, b);
                            all_sprites.add(mydebug);
                            break;
                    }
                }
            }
        }

        private void draw_game(Canvas canvas) {
            if (blackout) {
                canvas.drawARGB(255, 0, 0, 0);
                return;
            }
            final int origin = 0 - sq_size / 2;
            final int margin = sq_size / 10;
            final int offset = extra_height / 5;
            // -------------------- Draw Floor ----------------------
            for (int a = 1; a < 12; a++) {
                for (int b = 1; b < 12; b++) {
                    canvas.drawBitmap(t__floor, origin + sq_size * a, origin + sq_size * b, null);
                }
            }
            // ------------------- Draw Wall -----------------------
            for (int i = 0; i < 13; i++) {
                canvas.drawBitmap(t___wall, origin + i * sq_size, origin, null);
                canvas.drawBitmap(t___wall, origin + i * sq_size, origin + 12 * sq_size, null);
                canvas.drawBitmap(t___wall, origin, origin + i * sq_size, null);
                canvas.drawBitmap(t___wall, origin + 12 * sq_size, origin + i * sq_size, null);
            }
            // ------------------- Draw Menu -----------------------
            for (int a = 0; a < 12; a++) {
                for (int b = 12; b < 22; b++) {
                    canvas.drawBitmap(menu_background, sq_size * a, sq_size * b, null);
                }
            }
            Paint num_box = new Paint();
            num_box.setStrokeWidth(10);
            num_box.setColor(Color.rgb(130, 130, 130));
            canvas.drawRect(sq_size * 2 - margin * 4, sq_size * 13 + margin + offset, sq_size * 4 + margin, sq_size * 14 - margin + offset, num_box);
            canvas.drawRect(sq_size * 2 - margin * 4, sq_size * 14 + margin + offset, sq_size * 4 - margin, sq_size * 15 - margin + offset, num_box);
            canvas.drawRect(sq_size * 2 - margin * 4, sq_size * 15 + margin + offset, sq_size * 4 - margin, sq_size * 16 - margin + offset, num_box);
            canvas.drawRect(sq_size * 2 - margin * 4, sq_size * 16 + margin + offset, sq_size * 4 - margin, sq_size * 17 - margin + offset, num_box);
            canvas.drawRect(sq_size * 5 + margin, sq_size * 14 + margin + offset, sq_size * 7 - margin * 3, sq_size * 15 - margin + offset, num_box);
            canvas.drawRect(sq_size * 5 + margin, sq_size * 15 + margin + offset, sq_size * 7 - margin * 3, sq_size * 16 - margin + offset, num_box);
            canvas.drawRect(sq_size * 5 + margin, sq_size * 16 + margin + offset, sq_size * 7 - margin * 3, sq_size * 17 - margin + offset, num_box);
            canvas.drawBitmap(menu_health, sq_size + origin, 13 * sq_size + offset, null);
            canvas.drawBitmap(w___ironw, sq_size + origin, 14 * sq_size + offset, null);
            canvas.drawBitmap(w___ironh, sq_size + origin, 15 * sq_size + offset, null);
            canvas.drawBitmap(menu_gold, sq_size + origin, 16 * sq_size + offset, null);
            canvas.drawBitmap(i____key_y, sq_size * 4, 14 * sq_size + offset, null);
            canvas.drawBitmap(i____key_b, sq_size * 4, 15 * sq_size + offset, null);
            canvas.drawBitmap(i____key_r, sq_size * 4, 16 * sq_size + offset, null);
            // ------------------- Draw Arrow Buttons -----------------------
            canvas.drawRect(sq_size * 9 + origin, sq_size * 13 + origin + offset, sq_size * 10, sq_size * 14 + offset, pt1);
            canvas.drawRect(sq_size * 9 + origin, sq_size * 16 + origin + offset, sq_size * 10, sq_size * 17 + offset, pt2);
            canvas.drawRect(sq_size * 7, sq_size * 14 + offset, sq_size * 9 + origin, sq_size * 16 + origin + offset, pt3);
            canvas.drawRect(sq_size * 10, sq_size * 14 + offset, sq_size * 12 + origin, sq_size * 16 + origin + offset, pt4);
            canvas.drawBitmap(menu_up, sq_size * 9 + origin, sq_size * 13 + origin + offset, null);
            canvas.drawBitmap(menu_down, sq_size * 9 + origin, sq_size * 16 + origin + offset, null);
            canvas.drawBitmap(menu_left, sq_size * 7, sq_size * 14 + offset, null);
            canvas.drawBitmap(menu_right, sq_size * 10, sq_size * 14 + offset, null);
            // ------------------- Draw Staff Buttons ------------------------
            canvas.drawRect(sq_size * 7, sq_size * 17 + offset*3, sq_size * 9 + origin, sq_size * 19 + offset*3, pt5);
            canvas.drawRect(sq_size * 9 + origin, sq_size * 17 + offset*3, sq_size * 10, sq_size * 19 + offset*3, pt6);
            canvas.drawRect(sq_size * 10, sq_size * 17 + offset*3, sq_size * 12 + origin, sq_size * 18 + offset*3, pt7);
            canvas.drawRect(sq_size * 10, sq_size * 18 + offset*3, sq_size * 12 + origin, sq_size * 19 + offset*3, pt8);
            // ------------------- Draw Items Buttons ------------------------
            Paint stash_paint = new Paint();
            stash_paint.setStrokeWidth(10);
            stash_paint.setColor(Color.rgb(180, 150, 180));
            for (int a = 1; a < 7; a++) {
                canvas.drawRect(sq_size * a + origin, sq_size * 17 + offset*3, sq_size * (a + 1) + origin, sq_size * 18 + offset*3, stash_paint);
                if (a % 2 == 1)
                    stash_paint.setColor(Color.rgb(220, 220, 220));
                else
                    stash_paint.setColor(Color.rgb(180, 150, 180));
                canvas.drawRect(sq_size * a + origin, sq_size * 18 + offset*3, sq_size * (a + 1) + origin, sq_size * 19 + offset*3, stash_paint);
            }
            if (stf_wsdm)
                canvas.drawBitmap(menu_stf1, sq_size * 7, sq_size * 17 - origin / 2 + offset*3, null);
            if (stf_echo)
                canvas.drawBitmap(menu_stf2, sq_size * 9 + origin, sq_size * 17 - origin / 2 + offset*3, null);
            if (stf_space)
                canvas.drawBitmap(menu_stf3, sq_size * 10, sq_size * 17 - origin / 2 + offset*3, null);
            if (cross)
                canvas.drawBitmap(i____cross, sq_size + origin, sq_size * 17 + offset*3, null);
            if (lucky_gold)
                canvas.drawBitmap(i_lucky_gd, sq_size * 2 + origin, sq_size * 17 + offset*3, null);
            if (dragonsbane)
                canvas.drawBitmap(i_dra_bane, sq_size + origin, sq_size * 18 + offset*3, null);
            if (snow_cryst)
                canvas.drawBitmap(i_snow_crs, sq_size * 2 + origin, sq_size * 18 + offset*3, null);
            if (m_mattock)
                canvas.drawBitmap(i_m_mattok, sq_size * 3 + origin, sq_size * 17 + offset*3, null);
            if (e_mattock)
                canvas.drawBitmap(i_e_mattok, sq_size * 3 + origin, sq_size * 18 + offset*3, null);
            if (wing_up)
                canvas.drawBitmap(i__wing_up, sq_size * 4 + origin, sq_size * 17 + offset*3, null);
            if (wing_cent)
                canvas.drawBitmap(i_wing_cen, sq_size * 5 + origin, sq_size * 17 + offset*3, null);
            if (wing_down)
                canvas.drawBitmap(i_wing_dow, sq_size * 6 + origin, sq_size * 17 + offset*3, null);
            if (elixir)
                canvas.drawBitmap(i___elixir, sq_size * 4 + origin, sq_size * 18 + offset*3, null);
            if (key_enhac)
                canvas.drawBitmap(i_key_ehac, sq_size * 5 + origin, sq_size * 18 + offset*3, null);
            if (bomb)
                canvas.drawBitmap(i_____bomb, sq_size * 6 + origin, sq_size * 18 + offset*3, null);
            // ------------------- Draw Octopus and  Dragon-----------------------
            if (floor_num == 15 && current_floor[5][6] != 83)
                octopus_sprite.update(canvas);
            if (floor_num == 35 && current_floor[7][6] == 64)
                dragon_sprite.update(canvas);
            // ------------------------ Update sprites ---------------------------
            for (int idx = 0; idx < all_sprites.size(); idx++) {
                all_sprites.get(idx).update(canvas);
            }
            // ------------------- Draw Hero -----------------------
            if (show_hero) {
                if (walk_result == 1 || walk_result == 2) {
                    if (walk_count < 2) {
                        isWalk = true;
                        hero_sprite.walk(canvas);
                        walk_count++;
                    } else {
                        walk_count = 0;
                        hero_sprite.walk(canvas);
                        walk_result = 0;
                        isWalk = false;
                        if (battle_coming){
                            battle_coming = false;
                            isBattle = true;
                        }
                    }
                } else {
                    hero_sprite.set_location(hero_x * sq_size - sq_size / 2, hero_y * sq_size - sq_size / 2);
                    hero_sprite.stand(canvas);
                }
            }
            // ------------------- Draw star to indicate fighting -----------------------
            if (show_fight) {
                red_star_sprite.set_location(hero_x * sq_size - sq_size / 2, hero_y * sq_size - sq_size / 2);
                red_star_sprite.update(canvas);
                sleep(300);
            }
            // ------------------- Draw Text -----------------------
            Paint textpaint = new Paint();
            textpaint.setColor(Color.BLACK);
            textpaint.setTextSize(sq_size * 3 / 4);
            String my_text = String.valueOf(count_y);
            canvas.drawText(my_text, sq_size * 5 + margin * 3, sq_size * 14 + margin * 8 + offset, textpaint);
            my_text = String.valueOf(count_b);
            canvas.drawText(my_text, sq_size * 5 + margin * 3, sq_size * 15 + margin * 8 + offset, textpaint);
            my_text = String.valueOf(count_r);
            canvas.drawText(my_text, sq_size * 5 + margin * 3, sq_size * 16 + margin * 8 + offset, textpaint);
            my_text = String.valueOf(hp);
            canvas.drawText(my_text, sq_size * 2 - margin * 2, sq_size * 13 + margin * 8 + offset, textpaint);
            my_text = String.valueOf(atk);
            canvas.drawText(my_text, sq_size * 2 - margin * 2, sq_size * 14 + margin * 8 + offset, textpaint);
            my_text = String.valueOf(def);
            canvas.drawText(my_text, sq_size * 2 - margin * 2, sq_size * 15 + margin * 8 + offset, textpaint);
            my_text = String.valueOf(gold);
            canvas.drawText(my_text, sq_size * 2 - margin * 2, sq_size * 16 + margin * 8 + offset, textpaint);
            textpaint.setTextSize(sq_size);
            my_text = String.valueOf(floor_num) + " F";
            canvas.drawText(my_text, sq_size * 3 + margin*3, sq_size * 13 - margin + offset, textpaint);
            stash_paint.setColor(Color.rgb(210, 210, 210));
            canvas.drawRect(sq_size*5 - margin*4, sq_size * 13 + margin + offset, sq_size*6 + margin*7, sq_size * 14 - margin + offset, stash_paint);
            textpaint.setTextSize(sq_size*9/12);
            textpaint.setColor(Color.rgb(40, 50, 50));
            my_text = "Save";
            canvas.drawText(my_text, sq_size*5 - margin*2, sq_size * 13 + margin * 8 + offset, textpaint);
            // ------------------- Debug purpose -----------------------

            sleep(50);
            //*/
            canvas.drawBitmap(ball, x - ball.getWidth() / 2, y - ball.getHeight() / 2, null);
        }

        private void check_complete() {
            if (hp <= 0){
                parent.runOnUiThread(new Runnable() {
                    public void run() {
                        AlertDialog.Builder game_over_builder = new AlertDialog.Builder(parent);
                        game_over_builder.setTitle(R.string.game_over_title);
                        game_over_builder.setMessage(R.string.game_over_message);
                        game_over_builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        AlertDialog game_over_dialog = game_over_builder.create();
                        game_over_dialog.setCanceledOnTouchOutside(false);
                        game_over_dialog.show();
                    }
                });
                return;
            }
            switch (floor_num) {
                case 2:
                    if (current_floor[2][6] == 1 && current_floor[2][8] == 1) {
                        current_floor[5][5] = 1;
                        current_floor[5][9] = 1;
                        current_floor[8][5] = 1;
                        current_floor[8][9] = 1;
                        current_floor[11][5] = 1;
                        current_floor[11][9] = 1;
                    }
                    break;
                case 8:
                    if (hero_y != 5)
                        break;
                    if (hero_x != 9 && hero_x != 11)
                        break;
                    if (current_floor[5][9] == 1 && current_floor[5][11] == 1)
                        current_floor[4][10] = 1;
                    break;
                case 10:
                    if (hero_x != 6 && current_floor[1][6] != 38)
                        break;
                    boolean condition = current_floor[4][5] == 1 && current_floor[4][6] == 1;
                    condition &= current_floor[4][7] == 1 && current_floor[5][5] == 1;
                    condition &= current_floor[5][7] == 1 && current_floor[6][5] == 1;
                    condition &= current_floor[6][6] == 1 && current_floor[6][7] == 1;
                    if (condition)
                        isEvent = true;
                    break;
                case 11:
                    if (hero_x == 1 || hero_x == 3) {
                        if (current_floor[5][1] == 1 && current_floor[5][3] == 1)
                            current_floor[4][2] = 1;
                    }
                    break;
                case 14:
                    if (hero_y == 1 ||(hero_x == 2 && hero_y == 2)) {
                        if (current_floor[1][1] == 1 && current_floor[2][2] == 1 && current_floor[1][3] == 1)
                            current_floor[3][1] = 73;
                    }
                    break;
                case 15:
                    if (hero_x != 6)
                        break;
                    if (current_floor[6][6] == 1)
                        isEvent = true;
                    break;
                case 17:
                    if ((hero_x == 1 || hero_x == 3)&& hero_y == 8)
                        if (current_floor[8][1] == 1 && current_floor[8][3] == 1)
                            current_floor[7][2] = 1;
                    if ((hero_x == 1 || hero_x == 3)&& hero_y == 5)
                        if (current_floor[5][1] == 1 && current_floor[5][3] == 1)
                            current_floor[4][2] = 1;
                    if ((hero_x == 9 || hero_x == 11)&& hero_y == 8)
                        if (current_floor[8][9] == 1 && current_floor[8][11] == 1)
                            current_floor[7][10] = 1;
                    if ((hero_x == 9 || hero_x == 11)&& hero_y == 5)
                        if (current_floor[5][9] == 1 && current_floor[5][11] == 1)
                            current_floor[4][10] = 1;
                    break;
                case 20:
                    if (hero_x != 6)
                        break;
                    if (current_floor[6][6] == 1)
                        isEvent = true;
                    break;
                case 25:
                    isEvent = true;
                    break;
                case 30:
                    boolean open30f = current_floor[5][3] == 1 && current_floor[5][4] == 1;
                    open30f &= current_floor[5][5] == 1 && current_floor[5][7] == 1;
                    open30f &= current_floor[5][8] == 1 && current_floor[5][9] == 1;
                    if (open30f)
                        isEvent = true;
                    break;
                case 32:
                    if (hero_x == 6)
                        isEvent = true;
                    break;
                case 33:
                    if (hero_x < 8)
                        break;
                    boolean open33f = current_floor[5][9] == 1 && current_floor[5][11] == 1;
                    open33f &= current_floor[7][9] == 1 && current_floor[7][11] == 1;
                    if (open33f) {
                        current_floor[4][10] = 1;
                        current_floor[8][10] = 1;
                    }
                    break;
                case 34:
                    if (hero_y < 4 || hero_y > 8)
                        break;
                    boolean open34f = current_floor[4][5] == 1 && current_floor[4][7] == 1;
                    open34f &= current_floor[4][9] == 1 && current_floor[4][11] == 1;
                    open34f &= current_floor[8][5] == 1 && current_floor[8][7] == 1;
                    open34f &= current_floor[8][9] == 1 && current_floor[8][11] == 1;
                    if (open34f) {
                        current_floor[5][1] = 71;
                        current_floor[5][3] = 71;
                        current_floor[6][2] = 73;
                        current_floor[7][1] = 71;
                        current_floor[7][3] = 71;
                    }
                    break;
                case 35:
                    isEvent = true;
                    break;
                case 38:
                    if (hero_x > 3 || hero_y != 10)
                        break;
                    if (current_floor[10][1] == 1 && current_floor[10][3] == 1)
                        isEvent = true;
                    break;
                case 40:
                    if (hero_y == 7)
                        isEvent = true;
                    break;
                case 41:
                    if (hero_x == 5 || hero_x == 7) {
                        current_floor[4][6] = 1;
                        break;
                    }
                    if (hero_x == 2 && hero_y == 2) {
                        current_floor[2][3] = 1;
                        if (current_floor[2][1] == -7)
                            current_floor[2][1] = 1;
                        current_floor[2][10] = -3;
                        break;
                    }
                    if (hero_x == 10 && hero_y == 2) {
                        current_floor[2][9] = 1;
                        if (current_floor[2][11] == -7)
                            current_floor[2][11] = 1;
                        isEvent = true;
                        break;
                    }
                    if (hero_x == 3 && hero_y == 5) {
                        current_floor[5][2] = 1;
                        current_floor[6][3] = 1;
                        if (current_floor[4][3] == -6)
                            current_floor[4][3] = 1;
                        break;
                    }
                    if (hero_x == 9 && hero_y == 5) {
                        current_floor[5][10] = 1;
                        current_floor[6][9] = 1;
                        if (current_floor[4][9] == -6)
                            current_floor[4][9] = 1;
                        break;
                    }
                    break;
                case 42:
                    if (hero_x == 9) {
                        if (hero_y == 4) {
                            current_floor[5][9] = 1;
                            if (current_floor[4][10] == -8)
                                current_floor[4][10] = -7;
                            else
                                current_floor[4][10] = 1;
                        } else if (hero_y == 8)
                            current_floor[8][10] = 1;
                        break;
                    }
                    if (hero_x == 11) {
                        if (hero_y == 4) {
                            if (current_floor[4][10] == -8)
                                current_floor[4][10] = -6;
                            else
                                current_floor[4][10] = 1;
                            if (current_floor[5][11] == -8)
                                current_floor[5][11] = -6;
                            else
                                current_floor[5][11] = 1;
                        } else if (hero_y == 6) {
                            if (current_floor[5][11] == -8)
                                current_floor[5][11] = -7;
                            else
                                current_floor[4][10] = 1;
                            current_floor[6][10] = 1;
                        } else if (hero_y == 8)
                            current_floor[8][10] = 1;
                        break;
                    }
                    break;
                case 43:
                    if (hero_x == 4 && hero_y == 4) {
                        current_floor[3][4] = 1;
                        current_floor[5][4] = 1;
                        break;
                    }
                    if (hero_x == 9 && hero_y == 7) {
                        current_floor[6][9] = 1;
                        current_floor[7][10] = 1;
                        current_floor[8][9] = 1;
                        break;
                    }
                    break;

                case 44:
                    if (current_floor[9][5] == 1 && current_floor[9][7] == 1)
                        current_floor[8][6] = 1;
                    break;
                case 45:
                    if (hero_x == 5) {
                        if (hero_y == 3) {
                            if (current_floor[3][6] == -8)
                                current_floor[3][6] = -7;
                            else
                                current_floor[3][6] = 1;
                            if (current_floor[4][5] == -8)
                                current_floor[4][5] = -7;
                            else
                                current_floor[4][5] = 1;
                        } else if (hero_y == 5) {
                            if (current_floor[5][6] == -8)
                                current_floor[5][6] = -6;
                            else
                                current_floor[5][6] = 1;
                            if (current_floor[4][5] == -8)
                                current_floor[4][5] = -6;
                            else
                                current_floor[4][5] = 1;
                        } else if (hero_y == 9 || hero_y == 11) {
                            if (current_floor[9][5] == 1 && current_floor[11][5] == 1)
                                current_floor[10][4] = 1;
                        }
                        break;
                    }
                    if (hero_x == 7) {
                        if (hero_y == 3) {
                            if (current_floor[3][6] == -8)
                                current_floor[3][6] = -6;
                            else
                                current_floor[3][6] = 1;
                            if (current_floor[4][7] == -8)
                                current_floor[4][7] = -6;
                            else
                                current_floor[4][7] = 1;
                        } else if (hero_y == 5) {
                            if (current_floor[5][6] == -8)
                                current_floor[5][6] = -7;
                            else
                                current_floor[5][6] = 1;
                            if (current_floor[4][7] == -8)
                                current_floor[4][7] = -7;
                            else
                                current_floor[4][7] = 1;
                        }
                        break;
                    }
                    if (hero_x == 8) {
                        if (hero_y == 9) {
                            current_floor[10][8] = 1;
                            if (current_floor[11][8] == 1)
                                current_floor[10][7] = 1;
                        } else if (hero_y == 11) {
                            current_floor[10][8] = 1;
                            if (current_floor[9][8] == 1)
                                current_floor[10][7] = 1;
                        }
                        break;
                    }
                    if (hero_y == 8 && hero_x == 10) {
                        current_floor[7][10] = 1;
                        current_floor[8][11] = 1;
                        break;
                    }
                    break;
                case 46:
                    if (hero_x == 2) {
                        if (hero_y == 1) {
                            if (current_floor[1][3] == -7)
                                current_floor[1][3] = 1;
                            current_floor[1][1] = 1;
                            current_floor[2][2] = 1;
                        } else if (hero_y == 4) {
                            if (current_floor[4][3] == -6)
                                current_floor[4][3] = 1;
                            current_floor[4][1] = 1;
                            current_floor[3][2] = 1;
                        }
                    } else if (hero_x == 4 && hero_y == 9) {
                        current_floor[9][3] = 1;
                        current_floor[9][5] = 1;
                    } else if (hero_x == 8 && hero_y == 7) {
                        current_floor[6][8] = 1;
                        current_floor[7][7] = 1;
                    } else if (hero_x == 10 && hero_y == 9) {
                        current_floor[9][11] = 1;
                        current_floor[10][10] = 1;
                    }
                    break;
                case 47:
                    if (hero_x == 1) {
                        if (current_floor[hero_y + 1][1] == 56 && hero_y < 10 && hero_y > 2) {
                            current_floor[hero_y][1] = 1;
                            current_floor[hero_y + 1][1] = -7;
                            if (current_floor[hero_y + 1][2] == -7)
                                current_floor[hero_y + 1][2] = 1;
                            current_floor[hero_y + 2][1] = 56;
                            if (current_floor[hero_y + 2][2] == 1)
                                current_floor[hero_y + 2][2] = -7;
                            if (current_floor[hero_y + 3][1] == 1)
                                current_floor[hero_y + 3][1] = -7;
                        } else if (current_floor[hero_y - 1][1] == 56 && hero_y > 3) {
                            current_floor[hero_y][1] = 1;
                            current_floor[hero_y - 1][1] = -7;
                            if (current_floor[hero_y - 1][2] == -7)
                                current_floor[hero_y - 1][2] = 1;
                            current_floor[hero_y - 2][1] = 56;
                            if (current_floor[hero_y - 2][2] == 1)
                                current_floor[hero_y - 2][2] = -7;
                            if (current_floor[hero_y - 3][1] == 1)
                                current_floor[hero_y - 3][1] = -7;
                        } else if (current_floor[hero_y + 1][1] != 56 && current_floor[hero_y - 1][1] != 56){
                            if (hero_y != 11)
                                current_floor[hero_y + 1][1] = 1;
                            if (hero_y != 2)
                                current_floor[hero_y - 1][1] = 1;
                            if (current_floor[hero_y][2] == -7)
                                current_floor[hero_y][2] = 1;
                        }
                    } else if (hero_x == 4 && hero_y == 4) {
                        current_floor[4][4] = 1;
                        current_floor[4][5] = -7;
                        current_floor[4][6] = 56;
                        if (current_floor[5][5] == -8)
                            current_floor[5][5] = -6;
                        else
                            current_floor[5][5] = 1;
                    } else if (hero_x == 5) {
                        if (hero_y == 4 && current_floor[4][6] != 56) {
                            if (current_floor[4][4] == -7)
                                current_floor[4][4] = 1;
                            if (current_floor[5][6] == 55) {
                                current_floor[5][5] = -6;
                                current_floor[4][6] = -6;
                            } else {
                                current_floor[5][5] = 1;
                                current_floor[4][6] = 1;
                            }
                        } else if (hero_y == 11) {
                            current_floor[10][5] = 1;
                            current_floor[11][4] = 1;
                            current_floor[11][6] = 1;
                        }
                    } else if (hero_x == 6) {
                        if (hero_y == 4 && current_floor[4][5] == -7) {
                            current_floor[4][5] = 1;
                            if (current_floor[5][6] == -7)
                                current_floor[5][6] = 1;
                            else if (current_floor[5][6] == 55)
                                current_floor[4][6] = -6;
                        } else if (hero_y == 5 && current_floor[6][6] == -6) {
                            current_floor[6][6] = 1;
                            if (current_floor[5][5] == -8)
                                current_floor[5][5] = -7;
                            else
                                current_floor[5][5] = 1;
                            if (current_floor[4][6] == -8)
                                current_floor[4][6] = -7;
                            else if (current_floor[4][6] == -6)
                                current_floor[4][6] = 1;
                        }
                    } else if (hero_x == 8) {
                        if (hero_y == 3) {
                            current_floor[3][8] = 1;
                            current_floor[2][8] = -7;
                            current_floor[1][9] = -7;
                            current_floor[1][8] = 56;
                        } else if (hero_y == 2 && current_floor[1][8] == -7) {
                            current_floor[1][8] = 1;
                            current_floor[3][8] = 1;
                        } else if (hero_y == 1 && current_floor[1][9] == -7) {
                            current_floor[2][8] = 1;
                            current_floor[1][9] = 1;
                            if (current_floor[1][7] == -7)
                                current_floor[1][7] = 1;
                        }

                    } else if (hero_x == 9) {
                        if (hero_y == 5) {
                            current_floor[5][8] = 1;
                            current_floor[6][9] = 1;
                            current_floor[5][10] = 1;
                        }
                    }
                    break;
                case 48:
                    if (hero_x == 1) {
                        if (hero_y == 9) {
                            current_floor[8][1] = 1;
                            current_floor[10][1] = 1;
                        } else if (hero_y == 1)
                            current_floor[8][8] = 1;
                    } else if (hero_x == 2) {
                        if (hero_y == 4){
                            current_floor[4][2] = 1;
                            current_floor[3][2] = -7;
                            current_floor[3][1] = 1;
                            current_floor[3][3] = 1;
                            current_floor[2][2] = 56;
                            current_floor[2][1] = -7;
                            if (current_floor[1][2] == 1)
                                current_floor[1][2] = -7;
                        } else if (hero_y == 2) {
                            if (current_floor[1][2] == -7)
                                current_floor[1][2] = 1;
                            current_floor[2][1] = 1;
                            current_floor[3][2] = 1;
                        }
                    } else if (hero_x == 4 && hero_y == 7) {
                        current_floor[7][3] = 1;
                        current_floor[7][5] = 1;
                        current_floor[8][4] = 1;
                        if (current_floor[6][4] == -6)
                            current_floor[6][4] = 1;
                    } else if (hero_x == 7 && (hero_y == 3 || hero_y == 5)) {
                        current_floor[4][7] = 1;
                    } else if (hero_x == 9 && (hero_y == 3 || hero_y == 5)) {
                        current_floor[4][9] = 1;
                    } else if (hero_x == 10 && hero_y == 7) {
                        current_floor[7][9] = 1;
                        current_floor[7][11] = 1;
                    }
                    break;
                case 49:
                    if (hero_y == 10) {
                        if (hero_x == 5) {
                            current_floor[11][5] = 1;
                            if (current_floor[10][7] == 56)
                                current_floor[10][6] = -7;
                            else {
                                current_floor[10][6] = 1;
                                current_floor[9][6] = 1;
                            }
                        } else if (hero_x == 7){
                            current_floor[11][7] = 1;
                            if (current_floor[10][5] == 56)
                                current_floor[10][6] = -7;
                            else {
                                current_floor[10][6] = 1;
                                current_floor[9][6] = 1;
                            }
                        }
                    }
                    else if (hero_y == 8) {
                        if (current_floor[8][5] == 1 && current_floor[8][7] == 1)
                            current_floor[7][6] = 1;
                    } else if (hero_y < 6) {
                        if (hero_x == 6 && hero_y == 3)
                            isEvent = true;
                        else {
                            boolean condition_49f = current_floor[2][5] == 57 && current_floor[2][7] == 57;
                            condition_49f &= current_floor[4][5] == 57 && current_floor[4][7] == 57;
                            condition_49f &= current_floor[2][6] == 1 && current_floor[4][6] == 1;
                            condition_49f &= current_floor[3][5] == 1 && current_floor[3][7] == 1;
                            if (condition_49f)
                                isEvent = true;
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        private int move(int direction) {
            int i, j;
            switch (direction) {
                case UP:
                    i = hero_y - 1; j = hero_x;
                    break;
                case DOWN:
                    i = hero_y + 1; j = hero_x;
                    break;
                case LEFT:
                    i = hero_y; j = hero_x - 1;
                    break;
                case RIGHT:
                    i = hero_y; j = hero_x + 1;
                    break;
                default:
                    return 0;
            }
            switch (current_floor[i][j]) {
                case -9:        // damage half life
                    if (sacred_shield)
                        return 1;
                    if (hp == 1)
                        return 0;
                    else {
                        hero_attack = false;
                        m_hp = atk;
                        m_atk = hp/2 + def;
                        m_def = 0;
                        m_gold = 0;
                        battle_coming = true;
                        return 1;
                    }
                case -8:        // damage 300
                    if (sacred_shield)
                        return 1;
                    if (hp <= 300)
                        return 0;
                    else {
                        hero_attack = false;
                        m_hp = atk;
                        m_atk = 300 + def;
                        m_def = 0;
                        m_gold = 0;
                        battle_coming = true;
                        return 1;
                    }
                case -7:        // damage 200
                    if (sacred_shield)
                        return 1;
                    if (hp <= 200)
                        return 0;
                    else {
                        hero_attack = false;
                        m_hp = atk;
                        m_atk = 200 + def;
                        m_def = 0;
                        m_gold = 0;
                        battle_coming = true;
                        return 1;
                    }
                case -6:        // damage 100
                    if (sacred_shield)
                        return 1;
                    if (hp <= 100)
                        return 0;
                    else {
                        hero_attack = false;
                        m_hp = atk;
                        m_atk = 100 + def;
                        m_def = 0;
                        m_gold = 0;
                        battle_coming = true;
                        return 1;
                    }
                case -5:        // invisible wall
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_blocked);
                    if (music_settings[1])
                        sfx_music.start();
                    return 0;
                case -4:        // event floor
                    isEvent = true;
                    return 1;
                case -3:        // event wall
                    switch (floor_num) {
                        case 12:
                            current_floor[1][11] = 23;
                            break;
                        case 16:
                            current_floor[11][11] = 22;
                            break;
                        case 19:
                            current_floor[3][6] = 81;
                            break;
                        case 41:
                            current_floor[2][10] = 56;
                            current_floor[2][9] = -7;
                            if (current_floor[2][11] == 1)
                                current_floor[2][11] = -7;
                            break;
                        default:
                            current_floor[6][6] = 10;
                            break;
                    }
                    refresh_ctr = true;
                    return 0;
                case -2:        // fake floor
                    if (floor_num == 23) {
                        sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_blocked);
                        if (music_settings[1])
                            sfx_music.start();
                        current_floor[i][j] = 0;
                        refresh_ctr = true;
                        for (int q = 1; q < 12; q++) {
                            for (int w = 1; w < 12; w++) {
                                if (current_floor[q][w] == -2) {
                                    return 0;
                                }
                            }
                        }
                        saint_history[8]++;
                        current_game.change_29f();
                    } else if (floor_num == 48) {
                        current_floor[1][1] = 57;
                        refresh_ctr = true;
                    }
                    return 0;
                case -1:        // fake wall
                    current_floor[i][j] = 1;
                    refresh_ctr = true;
                    return 1;
                case 0:         // wall
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_blocked);
                    if (music_settings[1])
                        sfx_music.start();
                    return 0;
                case 1:         // floor
                    return 1;
                case 3:         // upstairs
                    sleep(150);
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_stairs);
                    if (music_settings[1])
                        sfx_music.start();
                    if (floor_num%10 == 0 && floor_num != 0) {  // if needs to change music
                        if (background_music != null)           // kill the old music first
                            background_music.release();
                        background_music = MediaPlayer.create(getApplicationContext(), bgm_list[floor_num/10]);
                        background_music.setLooping(true);
                        if (music_settings[0])                  // if bgm option is checked
                            background_music.start();
                    }
                    current_game.put_one_floor(floor_num, current_floor);
                    if (floor_num == 43)
                        floor_num = 45;
                    else
                        floor_num++;
                    int pairup[] = find_hero_next_floor(true,floor_num);
                    hero_y = pairup[0];
                    hero_x = pairup[1];
                    refresh_ctr = true;
                    if (floor_num > highest_floor) {
                        if (floor_num == 32 || floor_num == 35 || floor_num == 42)
                            isEvent = true;
                        highest_floor = floor_num;
                    }
                    return 0;
                case 4:         // downstairs
                    sleep(150);
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_stairs);
                    if (music_settings[1])
                        sfx_music.start();
                    if (floor_num%10 == 1 && floor_num != 1) {  // if needs to change music
                        if (background_music != null)           // kill the old music first
                            background_music.release();
                        background_music = MediaPlayer.create(getApplicationContext(), bgm_list[floor_num/10 - 1]);
                        background_music.setLooping(true);
                        if (music_settings[0])                  // if bgm option is checked
                            background_music.start();
                    }
                    current_game.put_one_floor(floor_num, current_floor);
                    if (floor_num == 45)
                        floor_num = 43;
                    else
                        floor_num--;
                    int pairdown[] = find_hero_next_floor(false,floor_num);
                    hero_y = pairdown[0];
                    hero_x = pairdown[1];
                    refresh_ctr = true;
                    return 0;
                case 5:         // yellow door
                    if (count_y > 0) {
                        sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_open_doors);
                        if (music_settings[1])
                            sfx_music.start();
                        count_y--;
                        current_floor[i][j] = 1;
                        refresh_ctr = true;
                        if (floor_num == 39) {
                            boolean condition = current_floor[2][2] == 5 && current_floor[2][4] == 1;
                            condition &= current_floor[2][6] == 5 && current_floor[4][2] == 5;
                            condition &= current_floor[4][6] == 1 && current_floor[6][2] == 5;
                            condition &= current_floor[6][4] == 5 && current_floor[6][6] == 5;
                            if (condition)
                                current_floor[4][4] = 84;
                        } else if (floor_num == 41) {
                            if (i == 2 && j == 1 && current_floor[2][2] == 56) {
                                current_floor[2][1] = -7;
                                return 0;
                            } else if (i == 4 && j == 3 && current_floor[5][3] == 55) {
                                current_floor[4][3] = -6;
                                return 0;
                            } else if (i == 4 && j == 9 && current_floor[5][9] == 55) {
                                current_floor[4][9] = -6;
                                return 0;
                            } else if (i == 2 && j == 11 && current_floor[2][10] == 56) {
                                current_floor[2][11] = -7;
                                return 0;
                            }
                        } else if (floor_num == 43 && i == 1 && j == 8) {
                            current_floor[1][8] = -4;
                            return 0;
                        } else if (floor_num == 46) {
                            if (i == 1 && j == 3 && current_floor[1][2] == 56) {
                                current_floor[1][3] = -7;
                                return 0;
                            } else if (i == 4 && j == 3 && current_floor[4][2] == 55) {
                                current_floor[4][3] = -6;
                                return 0;
                            }
                        } else if (floor_num == 47) {
                            if (i == 1 && j == 7 && current_floor[1][8] == 56) {
                                current_floor[1][7] = -7;
                                return 0;
                            } else if (i == 4 && j == 4 && current_floor[4][5] == 56) {
                                current_floor[4][4] = -7;
                                return 0;
                            }
                        } else if (floor_num == 48) {
                            if (i == 6 && j == 4 & current_floor[7][4] == 55) {
                                current_floor[6][4] = -6;
                                return 0;
                            }
                        }
                        return 1;
                    } else {
                        sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_blocked);
                        if (music_settings[1])
                            sfx_music.start();
                        return 0;
                    }
                case 6:         // blue door
                    if (count_b > 0) {
                        sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_open_doors);
                        if (music_settings[1])
                            sfx_music.start();
                        count_b--;
                        current_floor[i][j] = 1;
                        refresh_ctr = true;
                        if (floor_num == 48) {
                            if (i == 4 && j == 2 && current_floor[3][2] == 56) {
                                current_floor[4][2] = -7;
                                return 0;
                            }
                        }
                        return 1;
                    } else {
                        sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_blocked);
                        if (music_settings[1])
                            sfx_music.start();
                        return 0;
                    }
                case 7:         // red door
                    if (count_r > 0) {
                        sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_open_doors);
                        if (music_settings[1])
                            sfx_music.start();
                        count_r--;
                        current_floor[i][j] = 1;
                        refresh_ctr = true;
                        return 1;
                    } else {
                        sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_blocked);
                        if (music_settings[1])
                            sfx_music.start();
                        return 0;
                    }
                case 8:         // magic door
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_blocked);
                    if (music_settings[1])
                        sfx_music.start();
                    return 0;
                case 9:         // prison
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_blocked);
                    if (music_settings[1])
                        sfx_music.start();
                    return 0;
                case 11:        // iron sword
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_iron_sword);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    atk += 10;
                    refresh_ctr = true;
                    return 1;
                case 12:        // iron shield
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_iron_shield);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    def += 10;
                    refresh_ctr = true;
                    return 1;
                case 13:        // silver sword
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_silver_sword);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    atk += 20;
                    refresh_ctr = true;
                    return 1;
                case 14:        // silver shield
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_silver_shield);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    def += 20;
                    refresh_ctr = true;
                    return 1;
                case 15:        // knight sword
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_knight_sword);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    current_floor[10][8] = 0;
                    atk += 40;
                    refresh_ctr = true;
                    return 1;
                case 16:        // knight shield
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_knight_shield);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    current_floor[5][2] = 0;
                    def += 40;
                    refresh_ctr = true;
                    return 1;
                case 17:        // divine sword
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_divine_sword);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    atk += 50;
                    refresh_ctr = true;
                    return 1;
                case 18:        // divine shield
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_divine_shield);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    def += 50;
                    refresh_ctr = true;
                    return 1;
                case 19:        // sacred sword
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_sacred_sword);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    atk += 100;
                    refresh_ctr = true;
                    return 1;
                case 20:        // sacred shield
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_sacred_shield);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    sacred_shield = true;
                    def += 100;
                    refresh_ctr = true;
                    return 1;
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                    return 0;
                case 29:
                    if (snow_cryst) {
                        sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_lava_freezing);
                        if (music_settings[1])
                            sfx_music.start();
                        current_floor[i][j] = 1;
                        refresh_ctr = true;
                        return 1;
                    } else {
                        sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_lava_cant_pass);
                        if (music_settings[1])
                            sfx_music.start();
                        return 0;
                    }
                case 31:
                    return battle_preparation(0);
                case 32:
                    return battle_preparation(1);
                case 33:
                    return battle_preparation(2);
                case 34:
                    return battle_preparation(3);
                case 35:
                    return battle_preparation(4);
                case 36:
                    return battle_preparation(5);
                case 37:
                    return battle_preparation(6);
                case 38:
                    return battle_preparation(7);
                case 39:
                    return battle_preparation(8);
                case 40:
                    return battle_preparation(9);
                case 41:
                    return battle_preparation(10);
                case 42:
                    return battle_preparation(11);
                case 43:
                    return battle_preparation(12);
                case 44:
                    return battle_preparation(13);
                case 45:
                    return battle_preparation(14);
                case 46:
                    return battle_preparation(15);
                case 47:
                    return battle_preparation(16);
                case 48:
                    return battle_preparation(17);
                case 49:
                    return battle_preparation(18);
                case 50:
                    return battle_preparation(19);
                case 51:
                    return battle_preparation(20);
                case 52:
                    return battle_preparation(21);
                case 53:
                    return battle_preparation(22);
                case 54:
                    return battle_preparation(23);
                case 55:
                    return battle_preparation(24);
                case 56:
                    return battle_preparation(25);
                case 57:
                    return battle_preparation(26);
                case 58:
                    return battle_preparation(27);
                case 59:
                    return battle_preparation(28);
                case 60:
                    return battle_preparation(29);
                case 61:
                    return battle_preparation(30);
                case 62:
                    return battle_preparation(31);
                case 63:
                    int walk_result = battle_preparation(31);
                    m_gold = m_gold / 2;
                    return walk_result;
                case 64:
                    return battle_preparation(32);
                case 65:
                    return battle_preparation(33);
                case 66:
                    return battle_preparation(34);
                case 71:        // yellow key
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    count_y++;
                    refresh_ctr = true;
                    return 1;
                case 72:        // blue key
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    count_b++;
                    refresh_ctr = true;
                    return 1;
                case 73:        // red key
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    count_r++;
                    refresh_ctr = true;
                    return 1;
                case 74:        // red potion
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            String full_message = getString(R.string.pick_up_red_potion);
                            full_message += String.valueOf(50*(1+floor_num/10)) + ".";
                            pick_up_builder.setMessage(full_message);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_potion);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    hp += 50 * (floor_num / 10 + 1);
                    refresh_ctr = true;
                    return 1;
                case 75:        // blue potion
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            String full_message = getString(R.string.pick_up_blue_potion);
                            full_message += String.valueOf(200*(1+floor_num/10)) + ".";
                            pick_up_builder.setMessage(full_message);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_potion);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    hp += 200 * (floor_num / 10 + 1);
                    refresh_ctr = true;
                    if (floor_num == 48 && i == 1 && j == 2 && current_floor[2][2] == 56)
                        current_floor[1][2] = -7;
                    return 1;
                case 76:        // red crystal
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            String full_message = getString(R.string.pick_up_red_crystal);
                            full_message += String.valueOf(1+floor_num/10) + ".";
                            pick_up_builder.setMessage(full_message);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_crystal);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    atk += (floor_num / 10 + 1);
                    refresh_ctr = true;
                    return 1;
                case 77:        // blue crystal
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            String full_message = getString(R.string.pick_up_blue_crystal);
                            full_message += String.valueOf(1+floor_num/10) + ".";
                            pick_up_builder.setMessage(full_message);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_crystal);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    def += (floor_num / 10 + 1);
                    refresh_ctr = true;
                    return 1;
                case 78:        // staff of wisdom
                    current_floor[i][j] = 1;
                    stf_wsdm = true;
                    refresh_ctr = true;
                    return 1;
                case 79:        // staff of echo
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_stf_echo);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    stf_echo = true;
                    refresh_ctr = true;
                    return 1;
                case 80:        // staff of space
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_stf_space);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    stf_space = true;
                    refresh_ctr = true;
                    return 1;
                case 81:        // cross
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_cross);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    cross = true;
                    refresh_ctr = true;
                    return 1;
                case 82:        // elixir
                    current_floor[i][j] = 1;
                    elixir = true;
                    refresh_ctr = true;
                    return 1;
                case 83:        // magic mattock
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_mattock);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    m_mattock = true;
                    refresh_ctr = true;
                    return 1;
                case 84:        // magic wing cent
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_wing_cent);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    wing_cent = true;
                    refresh_ctr = true;
                    return 1;
                case 85:        // enhanced mattock
                    current_floor[i][j] = 1;
                    e_mattock = true;
                    refresh_ctr = true;
                    return 1;
                case 86:        // bomb
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_bomb);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    bomb = true;
                    refresh_ctr = true;
                    return 1;
                case 87:        // magic wing up
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_wing_up);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    wing_up = true;
                    refresh_ctr = true;
                    return 1;
                case 88:        // enhanced key
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_enhanced_key);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    key_enhac = true;
                    refresh_ctr = true;
                    return 1;
                case 89:        // magic wing down
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_wing_down);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    wing_down = true;
                    refresh_ctr = true;
                    return 1;
                case 90:        // lucky gold
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_lucky_gold);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    lucky_gold = true;
                    refresh_ctr = true;
                    return 1;
                case 91:        // dragonsbane
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_dragonsbane);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    dragonsbane = true;
                    refresh_ctr = true;
                    return 1;
                case 92:        // snow crystal
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder pick_up_builder = new AlertDialog.Builder(parent);
                            pick_up_builder.setMessage(R.string.pick_up_snow_crystal);
                            AlertDialog pick_up_dialog = pick_up_builder.create();
                            pick_up_dialog.setCanceledOnTouchOutside(true);
                            pick_up_dialog.show();
                        }
                    });
                    sfx_music = MediaPlayer.create(getApplicationContext(), R.raw.sfx_items);
                    if (music_settings[1])
                        sfx_music.start();
                    current_floor[i][j] = 1;
                    snow_cryst = true;
                    refresh_ctr = true;
                    return 1;

                default:
                    return 0;
            }
        }

        private int[] find_hero_next_floor(boolean dir, int in_floor_num) {
            byte[][] floor = current_game.get_one_floor(in_floor_num);
            int i = 6, j = 6;
            for (int a = 1; a < 12; a++) {
                for (int b = 1; b < 12; b++) {
                    if (dir) {  // going up
                        if (floor[a][b] == 4) {
                            i = a;
                            j = b;
                        }
                    } else {    // going down
                        if (floor[a][b] == 3) {
                            i = a;
                            j = b;
                        }
                    }
                }
            }
            if (floor[i + 1][j] == 1) {
                hero_sprite.set_direction(DOWN);
                return new int[]{i + 1, j};
            }
            if (floor[i - 1][j] == 1 || floor[i - 1][j] == -6) {
                hero_sprite.set_direction(UP);
                return new int[]{i - 1, j};
            }
            if (floor[i][j - 1] == 1) {
                hero_sprite.set_direction(LEFT);
                return new int[]{i, j - 1};
            }
            if (floor[i][j + 1] == 1) {
                hero_sprite.set_direction(RIGHT);
                return new int[]{i, j + 1};
            }
            return new int[]{12, 1};
        }

        private int battle_preparation(int m_idx) {
            hero_attack = true;
            m_hp = m_table[m_idx][0];
            m_atk = m_table[m_idx][1];
            m_def = m_table[m_idx][2];
            m_gold = m_table[m_idx][3];

            int dagamge = damage_calculation(m_idx);
            if (dagamge > hp) {
                // don't have enough health to attack the monster
                return 0;
            } else {
                // able to defeat the monster
                battle_coming = true;
                return 1;
            }
        }

        private void draw_staff_of_wisdom_results(Canvas canvas) {
            final int origin = 0 - sq_size / 2;
            final int offset = extra_height / 9;
            canvas.drawARGB(255, 200, 200, 200);
            int extra_wall = extra_height / sq_size;
            // ------------------- Draw Wall -----------------------
            for (int i = 0; i < 13; i++) {
                canvas.drawBitmap(t___wall, origin + i * sq_size, origin, null);
                canvas.drawBitmap(t___wall, origin + i * sq_size, origin + (19+extra_wall) * sq_size, null);
            }
            for (int j = 0; j < 19 + extra_wall; j++) {
                canvas.drawBitmap(t___wall, origin, origin + j * sq_size, null);
                canvas.drawBitmap(t___wall, origin + 12 * sq_size, origin + j * sq_size, null);
            }
            Paint textpaint = new Paint();
            textpaint.setColor(Color.BLACK);
            textpaint.setTextSize(sq_size*7/8);
            String my_text = "Monster Statistics";
            canvas.drawText(my_text, sq_size * 5/2, sq_size * 3/2 + offset, textpaint);
            if (monsters_to_draw.size() < 7){
                textpaint.setColor(Color.DKGRAY);
                textpaint.setTextSize(sq_size*7/8);
                instruction = "Click to exit";
                canvas.drawText(instruction, sq_size * 7/2, sq_size * 18 + offset*8, textpaint);
                draw_monster_stats(canvas, 0, monsters_to_draw.size());
            } else {
                if (page == 0){
                    textpaint.setColor(Color.DKGRAY);
                    textpaint.setTextSize(sq_size*7/8);
                    instruction = "Click to continue";
                    canvas.drawText(instruction, sq_size * 6/2, sq_size * 18 + offset*8, textpaint);
                    draw_monster_stats(canvas, 0, 6);

                } else {
                    textpaint.setColor(Color.DKGRAY);
                    textpaint.setTextSize(sq_size*7/8);
                    instruction = "Click to exit";
                    canvas.drawText(instruction, sq_size * 7/2, sq_size * 18 + offset*8, textpaint);
                    draw_monster_stats(canvas, 6, monsters_to_draw.size()-6);
                }
            }
        }

        private void draw_monster_stats(Canvas canvas, int i, int size){
            final int margin = sq_size / 10;
            final int offset = extra_height / 8;
            Paint textpaint = new Paint();
            String my_text;
            for (int a = 0; a < size; a++){
                monsters_picture.get(i+a).set_location(sq_size, sq_size * a*2 + sq_size * 9/4 + margin*5*a + offset*(a+2));
                monsters_picture.get(i+a).update(canvas);

                textpaint.setARGB(255,0, 66, 173);
                textpaint.setTextSize(sq_size*14/32);
                my_text = monsters_name1.get(a+i);
                canvas.drawText(my_text, sq_size-margin, sq_size * a*2 + margin * 37 + margin*5*a + offset*(a+2), textpaint);
                my_text = monsters_name2.get(a+i);
                canvas.drawText(my_text, sq_size+margin, sq_size * a*2 + margin * 42 + margin*5*a + offset*(a+2), textpaint);

                int stats_table_idx;
                if (monsters_to_draw.get(a+i) < 63)
                    stats_table_idx = monsters_to_draw.get(a+i) - 31;
                else if (monsters_to_draw.get(a+i) > 63 && monsters_to_draw.get(a+i) < 67)
                    stats_table_idx = monsters_to_draw.get(a+i) - 32;
                else
                    stats_table_idx = 0;
                textpaint.setARGB(255,0, 0, 0);
                textpaint.setTextSize(sq_size*22/32);

                canvas.drawBitmap(menu_health, sq_size * 3 - margin*3, sq_size * a*2 + sq_size * 9/4 + margin*5*a + offset*(a+2), null);
                my_text = String.valueOf(m_table[stats_table_idx][0]);
                canvas.drawText(my_text, sq_size * 4 - margin, sq_size * a*2 + margin * 30 + margin*5*a + offset*(a+2), textpaint);

                canvas.drawBitmap(w___ironw, sq_size * 6, sq_size * a*2 + sq_size * 9/4 + margin*5*a + offset*(a+2), null);
                my_text = String.valueOf(m_table[stats_table_idx][1]);
                canvas.drawText(my_text, sq_size * 7 + margin, sq_size * a*2 + margin * 30 + margin*5*a + offset*(a+2), textpaint);

                canvas.drawBitmap(w___ironh, sq_size * 9 - margin*2, sq_size * a*2 + sq_size * 9/4 + margin*5*a + offset*(a+2), null);
                my_text = String.valueOf(m_table[stats_table_idx][2]);
                canvas.drawText(my_text, sq_size * 10 - margin, sq_size * a*2 + margin * 30 + margin*5*a + offset*(a+2), textpaint);

                canvas.drawBitmap(menu_gold, sq_size * 3 + margin*5, sq_size * a*2 + sq_size * 13/4 + margin*5*a + offset*(a+2), null);
                my_text = String.valueOf(m_table[stats_table_idx][3]);
                textpaint.setARGB(255,153, 77, 0);
                canvas.drawText(my_text, sq_size * 4 + margin*5, sq_size * a*2 + margin * 40 + margin*5*a + offset*(a+2), textpaint);

                canvas.drawBitmap(t___logo, sq_size * 6 + margin*2, sq_size * a*2 + sq_size * 13/4 + margin*5*a + offset*(a+2), null);
                if (damage_calculation(stats_table_idx) == 987654321)
                    my_text = "Can't Attack";
                else
                    my_text = String.valueOf(damage_calculation(stats_table_idx));
                textpaint.setARGB(255,204, 41, 0);
                canvas.drawText(my_text, sq_size * 7 + margin*3, sq_size * a*2 + margin * 40 + margin*5*a + offset*(a+2), textpaint);
            }
            sleep(100);
        }

        private void find_monsters_on_current_floor() {
            monsters_to_draw.clear();
            monsters_picture.clear();
            monsters_name1.clear();
            monsters_name2.clear();
            boolean dont_add;
            for (int i = 1; i < 12; i++) {
                for (int j = 1; j < 12; j++) {
                    if (current_floor[i][j] > 30 && current_floor[i][j] < 70) {
                        if (!monsters_to_draw.contains(current_floor[i][j])) {
                            dont_add = false;
                            byte b;
                            switch (current_floor[i][j]) {
                                case 31:    // green slime
                                    b = 31;
                                    monsters_name1.add("Green");
                                    monsters_name2.add("Slime");
                                    Sprite sp_slime_g = new Sprite(GameView.this, m__slime_g, 0,0, b);
                                    monsters_picture.add(sp_slime_g);
                                    break;
                                case 32:    // red slime
                                    b = 32;
                                    monsters_name1.add("Red");
                                    monsters_name2.add("Slime");
                                    Sprite sp_slime_r = new Sprite(GameView.this, m__slime_r, 0,0, b);
                                    monsters_picture.add(sp_slime_r);
                                    break;
                                case 33:    // fierce bat
                                    b = 33;
                                    monsters_name1.add("Fierce");
                                    monsters_name2.add("Bat");
                                    Sprite sp_fierce_bat = new Sprite(GameView.this, m_bat_fier, 0,0, b);
                                    monsters_picture.add(sp_fierce_bat);
                                    break;
                                case 34:    // priest
                                    b = 34;
                                    monsters_name1.add("Priest");
                                    monsters_name2.add("");
                                    Sprite sp_priest = new Sprite(GameView.this, m___priest, 0,0, b);
                                    monsters_picture.add(sp_priest);
                                    break;
                                case 35:    // skeleton
                                    b = 35;
                                    monsters_name1.add("Skeleton");
                                    monsters_name2.add("");
                                    Sprite sp_skeleton = new Sprite(GameView.this, m_skeleton, 0,0, b);
                                    monsters_picture.add(sp_skeleton);
                                    break;
                                case 36:    // skeleton_warrior
                                    b = 36;
                                    monsters_name1.add("Skeleton");
                                    monsters_name2.add("Warrior");
                                    Sprite sp_skeleton_warrior = new Sprite(GameView.this, m_skelet_w, 0,0, b);
                                    monsters_picture.add(sp_skeleton_warrior);
                                    break;
                                case 37:    // gate_keeper
                                    b = 37;
                                    monsters_name1.add("Gate-Keeper");
                                    monsters_name2.add("");
                                    Sprite sp_gate_keeper = new Sprite(GameView.this, m_gatekeep, 0,0, b);
                                    monsters_picture.add(sp_gate_keeper);
                                    break;
                                case 38:    // skeleton_captain
                                    b = 38;
                                    monsters_name1.add("Skeleton");
                                    monsters_name2.add("Captain");
                                    Sprite sp_skeleton_captain = new Sprite(GameView.this, m_skelet_c, 0,0, b);
                                    monsters_picture.add(sp_skeleton_captain);
                                    break;
                                case 39:    // black_slime
                                    b = 39;
                                    monsters_name1.add("Black");
                                    monsters_name2.add("Slime");
                                    Sprite sp_slime_b = new Sprite(GameView.this, m__slime_b, 0,0, b);
                                    monsters_picture.add(sp_slime_b);
                                    break;
                                case 40:    // giant bat
                                    b = 40;
                                    monsters_name1.add("Giant");
                                    monsters_name2.add("Bat");
                                    Sprite sp_giant_bat = new Sprite(GameView.this, m_bat_gian, 0,0, b);
                                    monsters_picture.add(sp_giant_bat);
                                    break;
                                case 41:    // elite_priest
                                    b = 41;
                                    monsters_name1.add("Elite");
                                    monsters_name2.add("Priest");
                                    Sprite sp_priest_master = new Sprite(GameView.this, m_priest_m, 0,0, b);
                                    monsters_picture.add(sp_priest_master);
                                    break;
                                case 42:    // zombie
                                    b = 42;
                                    monsters_name1.add("Zombie");
                                    monsters_name2.add("");
                                    Sprite sp_zombie = new Sprite(GameView.this, m___zombie, 0,0, b);
                                    monsters_picture.add(sp_zombie);
                                    break;
                                case 43:    // Rock_Monster
                                    b = 43;
                                    monsters_name1.add("Rock");
                                    monsters_name2.add("Monster");
                                    Sprite sp_stone_guardian = new Sprite(GameView.this, m_stone_gd, 0,0, b);
                                    monsters_picture.add(sp_stone_guardian);
                                    break;
                                case 44:    // zombie_warrior
                                    b = 44;
                                    monsters_name1.add("Zombie");
                                    monsters_name2.add("Warrior");
                                    Sprite sp_zombie_warrior = new Sprite(GameView.this, m_zombie_w, 0,0, b);
                                    monsters_picture.add(sp_zombie_warrior);
                                    break;
                                case 45:    // vampire
                                    b = 45;
                                    monsters_name1.add("Vampire");
                                    monsters_name2.add("");
                                    Sprite sp_vampire = new Sprite(GameView.this, m__vampire, 0,0, b);
                                    monsters_picture.add(sp_vampire);
                                    break;
                                case 46:    // slime_man
                                    b = 46;
                                    monsters_name1.add("Slime");
                                    monsters_name2.add("Man");
                                    Sprite sp_slime_man = new Sprite(GameView.this, m__slime_m, 0,0, b);
                                    monsters_picture.add(sp_slime_man);
                                    break;
                                case 47:    // skeleton_elite
                                    b = 47;
                                    monsters_name1.add("Skeleton");
                                    monsters_name2.add("Elite");
                                    Sprite sp_skeleton_elite = new Sprite(GameView.this, m_skelet_e, 0,0, b);
                                    monsters_picture.add(sp_skeleton_elite);
                                    break;
                                case 48:    // knight
                                    b = 48;
                                    monsters_name1.add("Knight");
                                    monsters_name2.add("");
                                    Sprite sp_knight = new Sprite(GameView.this, m___knight, 0,0, b);
                                    monsters_picture.add(sp_knight);
                                    break;
                                case 49:    // advanced_Gate-Keeper
                                    b = 49;
                                    monsters_name1.add("Advanced");
                                    monsters_name2.add("Gate-Keeper");
                                    Sprite sp_gatekeeper_elite = new Sprite(GameView.this, m_gatekp_e, 0,0, b);
                                    monsters_picture.add(sp_gatekeeper_elite);
                                    break;
                                case 50:    // swordsman
                                    b = 50;
                                    monsters_name1.add("Swordsman");
                                    monsters_name2.add("");
                                    Sprite sp_swordsman = new Sprite(GameView.this, m_swordsmn, 0,0, b);
                                    monsters_picture.add(sp_swordsman);
                                    break;
                                case 51:    // knight_elite
                                    b = 51;
                                    monsters_name1.add("Knight");
                                    monsters_name2.add("Elite");
                                    Sprite sp_knight_elite = new Sprite(GameView.this, m_knight_e, 0,0, b);
                                    monsters_picture.add(sp_knight_elite);
                                    break;
                                case 52:    // knight_captain
                                    b = 52;
                                    monsters_name1.add("Knight");
                                    monsters_name2.add("Captain");
                                    Sprite sp_knight_captain = new Sprite(GameView.this, m_knight_c, 0,0, b);
                                    monsters_picture.add(sp_knight_captain);
                                    break;
                                case 53:    // slimelord
                                    b = 53;
                                    monsters_name1.add("Slimelord");
                                    monsters_name2.add("");
                                    Sprite sp_slimelord = new Sprite(GameView.this, m_slimelod, 0,0, b);
                                    monsters_picture.add(sp_slimelord);
                                    break;
                                case 54:    // vampire_bat
                                    b = 54;
                                    monsters_name1.add("Vampire");
                                    monsters_name2.add("Bat");
                                    Sprite sp_vampire_bat = new Sprite(GameView.this, m_bat_vamp, 0,0, b);
                                    monsters_picture.add(sp_vampire_bat);
                                    break;
                                case 55:    // mage
                                    b = 55;
                                    monsters_name1.add("Mage");
                                    monsters_name2.add("");
                                    Sprite sp_mage = new Sprite(GameView.this, m_____mage, 0,0, b);
                                    monsters_picture.add(sp_mage);
                                    break;
                                case 56:    // mage elite
                                    b = 56;
                                    monsters_name1.add("Mage");
                                    monsters_name2.add("Elite");
                                    Sprite sp_mage_master = new Sprite(GameView.this, m_mage_mas, 0,0, b);
                                    monsters_picture.add(sp_mage_master);
                                    break;
                                case 57:    // demon_sergent
                                    b = 57;
                                    monsters_name1.add("Demon");
                                    monsters_name2.add("Sergent");
                                    Sprite sp_demo_sergent = new Sprite(GameView.this, m_demo_sgt, 0,0, b);
                                    monsters_picture.add(sp_demo_sergent);
                                    break;
                                case 58:    // dark_knight
                                    b = 58;
                                    monsters_name1.add("Dark");
                                    monsters_name2.add("Knight");
                                    Sprite sp_dark_knight = new Sprite(GameView.this, m_d_knight, 0,0, b);
                                    monsters_picture.add(sp_dark_knight);
                                    break;
                                case 59:    // elite Gate-Keeper
                                    b = 59;
                                    monsters_name1.add("Elite");
                                    monsters_name2.add("Gate-Keeper");
                                    Sprite sp_gate_guardian = new Sprite(GameView.this, m_gate_gdn, 0,0, b);
                                    monsters_picture.add(sp_gate_guardian);
                                    break;
                                case 60:    // fake_zeno1
                                    b = 60;
                                    monsters_name1.add("Zeno");
                                    monsters_name2.add("");
                                    Sprite sp_fake_zeno1 = new Sprite(GameView.this, m_demozeno, 0,0, b);
                                    monsters_picture.add(sp_fake_zeno1);
                                    break;
                                case 61:    // fake_zeno2
                                    b = 61;
                                    monsters_name1.add("Zeno");
                                    monsters_name2.add("");
                                    Sprite sp_fake_zeno2 = new Sprite(GameView.this, m_demozeno, 0,0, b);
                                    monsters_picture.add(sp_fake_zeno2);
                                    break;
                                case 62:    // octopus center
                                    b = 62;
                                    monsters_name1.add("");
                                    monsters_name2.add("Octopus");
                                    Bitmap small_octopus = createScaledBitmap(m__octopus, sq_size * 9/2, sq_size * 3/2, false);
                                    Sprite sp_octopus = new Sprite(GameView.this, small_octopus, 0,0, b);
                                    monsters_picture.add(sp_octopus);
                                    break;
                                case 63:    // octopus sides
                                    dont_add = true;
                                    break;
                                case 64:    // dragon
                                    b = 62;
                                    monsters_name1.add("");
                                    monsters_name2.add("Dragon");
                                    Bitmap small_dragon = createScaledBitmap(m___dragon, sq_size * 9/2, sq_size * 3/2, false);
                                    Sprite sp_dragon = new Sprite(GameView.this, small_dragon, 0,0, b);
                                    monsters_picture.add(sp_dragon);
                                    break;
                                case 65:    // archmage
                                    b = 65;
                                    monsters_name1.add("Archmage");
                                    monsters_name2.add("");
                                    Sprite sp_archmage = new Sprite(GameView.this, m_archmage, 0,0, b);
                                    monsters_picture.add(sp_archmage);
                                    break;
                                case 66:    // real_zeno
                                    b = 61;
                                    monsters_name1.add("Zeno");
                                    monsters_name2.add("");
                                    Sprite sp_real_zeno = new Sprite(GameView.this, m_demozeno, 0,0, b);
                                    monsters_picture.add(sp_real_zeno);
                                    break;
                                default:
                                    dont_add = true;
                                    break;
                            }
                            if (!dont_add)
                                monsters_to_draw.add(current_floor[i][j]);
                        }
                    }
                }
            }
            if (monsters_to_draw.size() > 6) {
                instruction = "Click to continue";
                total_page = 2;
            }
        }

    }

}



















