package com.the95.tower_of_sorcerer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
//import android.util.Log;

import java.util.ArrayList;

import static android.graphics.Bitmap.createScaledBitmap;

public class Gamelogic extends Activity implements View.OnTouchListener {
    // constants for convenience
    public final int            UP = 3, DOWN = 0, RIGHT = 2, LEFT = 1, FLY_UP = 4, FLY_DOWN = 5;
    public final int            THIEF = 1, SAINT = 2, MERCHANGT = 3, FAIRTY = 4, SHOP = 5, PRINCESS = 6;
    //  general game controls
    private GameView            gameview;
    private float               x, y;
    private ArrayList<Sprite>   all_sprites;
    private int                 sq_size;
    private int                 walk_result, walk_count;
    private int                 which_button;
    private int                 m_hp, m_atk, m_def, m_gold;
    private int                 price_idx, act;
    private boolean             refresh_ctr, isWalk, isBattle, isEvent;
    private boolean             button_click;
    private boolean             show_hero, show_fight, hero_attack;
    private boolean             blackout = false, proceed = false;
    //  current game data
    private Floors              current_game;
    private byte[][]            current_floor;
    private int                 floor_num;
    private int                 hero_x, hero_y;
    private int                 count_y, count_b, count_r;
    private int                 atk, def, hp, gold;
    private boolean             stf_wsdm, stf_echo, stf_space, cross, elixir;
    private boolean             m_mattock, wing_cent, e_mattock, bomb, wing_up;
    private boolean             key_enhac, wing_down, lucky_gold, dragonsbane, snow_cryst;
    // game pictures and stats table
    private Bitmap              ball, kid, pic_debug, hero;
    private Sprite              kid_sprite, hero_sprite, red_star_sprite;

    private Bitmap              menu_health, menu_gold, menu_background, menu_stf1, menu_stf2;
    private Bitmap              menu_stf3, menu_up, menu_down, menu_left, menu_right;
    private Bitmap              t__floor, t___wall, t___star, t_ustair, t_dstair;
    private Bitmap              t_door_y, t_door_b, t_door_r, t_door_m, t_prison, t___logo;
    private Bitmap              w___ironw, w_silverw, w_knightw, w_divinew, w_sacredw;
    private Bitmap              w___ironh, w_silverh, w_knighth, w_divineh, w_sacredh;
    private Bitmap              n___thief, n___saint, n_merchat, n___fairy, n_shop__l;
    private Bitmap              n_shop__m, n_shop__r, n_princes, n____lava;
    private Bitmap              m__slime_g, m__slime_r, m_bat_fier, m___priest, m_skeleton;
    private Bitmap              m_skelet_w, m_gatekeep, m_skelet_c, m__slime_b, m_bat_gian;
    private Bitmap              m_priest_m, m___zombie, m_stone_gd, m_zombie_w, m__vampire;
    private Bitmap              m__slime_m, m_skelet_e, m___knight, m_gatekp_e, m_swordsmn;
    private Bitmap              m_knight_e, m_knight_c, m_slimelod, m_bat_vamp, m_____mage;
    private Bitmap              m_mage_mas, m_demo_sgt, m_d_knight, m_gate_gdn, m_demozeno;
    private Bitmap              m____squid, m___dragon, m_archmage;
    private Bitmap              i____key_y, i____key_b, i____key_r, i_potion_r, i_potion_b;
    private Bitmap              i_crystl_r, i_crystl_b, i_stf_wsdm, i_stf_echo, i_stf_spce;
    private Bitmap              i____cross, i___elixir, i_m_mattok, i_wing_cen, i_e_mattok;
    private Bitmap              i_____bomb, i__wing_up, i_key_ehac, i_wing_dow, i_lucky_gd;
    private Bitmap              i_dra_bane, i_snow_crs;
    public final int[][]        m_table = new int[][]{
            {35,18,1,1},
            {45,20,2,2},
            {35,38,3,3},
            {60,32,8,5},
            {50,42,6,6},
            {55,52,12,8},
            {50,48,22,12},
            {100,65,15,30},
            {130,60,3,8},
            {60,100,8,12},
            {100,95,30,18},
            {260,85,5,22},
            {20,100,68,28},
            {320,120,15,30},
            {444,199,66,144},
            {320,140,20,30},
            {220,180,30,35},
            {210,200,65,45},
            {100,180,110,50},
            {100,680,50,55},
            {160,230,105,65},
            {120,150,50,100},
            {360,310,20,40},
            {200,390,90,50},
            {220,370,110,80},
            {200,380,130,90},
            {230,450,100,100},
            {180,430,210,120},
            {180,460,360,200},
            {800,500,100,500},
            {1000,625,125,1000},
            {1200,180,20,100},
            {1500,600,250,800},
            {4500,560,310,1000}
    };
    //  debug purpose
    private Paint               pt1, pt2, pt3, pt4, pt5, pt6, pt7, pt8;
    //private static final String TAG = "debuuuuuuuuuuuuuuuuuug";
    //Log.v(TAG, "x = " + me.getX() + " y = " + me.getY());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize general game controls
        gameview = new GameView(this);
        gameview.setOnTouchListener(this);
        x = y = 0;
        all_sprites = new ArrayList<>();
        sq_size = 32;
        walk_result = 0; walk_count = 0;
        which_button = 6;
        m_hp = 0; m_atk = 0; m_def = 0; m_gold = 0;
        price_idx = 0; act = 0;
        refresh_ctr = true; isWalk = false; isBattle = false; isEvent = false;
        button_click = false;
        show_hero = true; show_fight = false; hero_attack = true;

        //set_all_true();
        // initialize current game data
        current_game = new Floors();
        current_floor = current_game.get_one_floor(1);
        floor_num = 3;
        hero_x = 6; hero_y = 11;
        count_y = 10; count_b = 10; count_r = 10;
        atk = 100; def = 100; hp = 1000; gold = 0;
        stf_wsdm = stf_echo = stf_space = cross = elixir = false;
        m_mattock = wing_cent = e_mattock = bomb = wing_up = false;
        key_enhac = wing_down = lucky_gold = dragonsbane = snow_cryst = false;
        // initialize pictures
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.brokeearth);
        pic_debug = BitmapFactory.decodeResource(getResources(), R.drawable.z1_debug);
        kid = BitmapFactory.decodeResource(getResources(), R.drawable.poke);
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
        m____squid = BitmapFactory.decodeResource(getResources(), R.drawable.m31b1_squid);
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
        // debug purpose
        pt1 = new Paint(); pt1.setColor(Color.rgb(220, 220, 220)); pt1.setStrokeWidth(10);
        pt2 = new Paint(); pt2.setColor(Color.rgb(220, 220, 220)); pt1.setStrokeWidth(10);
        pt3 = new Paint(); pt3.setColor(Color.rgb(220, 220, 220)); pt1.setStrokeWidth(10);
        pt4 = new Paint(); pt4.setColor(Color.rgb(220, 220, 220)); pt1.setStrokeWidth(10);
        pt5 = new Paint(); pt5.setColor(Color.rgb(220, 220, 220)); pt1.setStrokeWidth(10);
        pt6 = new Paint(); pt6.setColor(Color.rgb(180, 150, 180)); pt1.setStrokeWidth(10);
        pt7 = new Paint(); pt7.setColor(Color.rgb(150, 150, 150)); pt1.setStrokeWidth(10);
        pt8 = new Paint(); pt8.setColor(Color.rgb(220, 220, 220)); pt1.setStrokeWidth(10);
        //Log.v(TAG, "width = " + sq_wall.getWidth() + " y = " + sq_wall.getHeight());
        setContentView(gameview);
    }
    private void set_all_true(){
        stf_wsdm = stf_echo = stf_space = cross = elixir = true;
        m_mattock = wing_cent = e_mattock = bomb = wing_up = true;
        key_enhac = wing_down = lucky_gold = dragonsbane = snow_cryst = true;
    };

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

        //v.performClick();

        try {
            Thread.sleep(25);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //*/
        switch (me.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (blackout){
                    AlertDialog.Builder wakeup_builder = new AlertDialog.Builder(v.getContext());
                    wakeup_builder.setMessage("... ... wake up!");
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
                button_click = true;
                x = me.getX();
                y = me.getY();
                int temp_x = 0, temp_y = 0;
                final int origin = 0 - sq_size / 2;
                if (x > sq_size*9 + origin        && y > sq_size*13          && x < sq_size*10          && y < sq_size*14) {
                    pt1.setColor(Color.rgb(255, 255, 255));
                    which_button = UP;
                    temp_x = hero_x; temp_y = hero_y - 1;
                } else if (x > sq_size*9 + origin && y > sq_size*16 + origin && x < sq_size*10          && y < sq_size*17) {
                    pt2.setColor(Color.rgb(255, 255, 255));
                    which_button = DOWN;
                    temp_x = hero_x; temp_y = hero_y + 1;
                } else if (x > sq_size*7          && y > sq_size*14          && x < sq_size*9  + origin && y < sq_size*16 + origin) {
                    pt3.setColor(Color.rgb(255, 255, 255));
                    which_button = LEFT;
                    temp_x = hero_x - 1; temp_y = hero_y;
                } else if (x > sq_size*10         && y > sq_size*14          && x < sq_size*12 + origin && y < sq_size*16 + origin) {
                    pt4.setColor(Color.rgb(255, 255, 255));
                    which_button = RIGHT;
                    temp_x = hero_x + 1; temp_y = hero_y;
                } else if (x > sq_size*7          && y > sq_size*17          && x < sq_size*9  + origin && y < sq_size*19) {
                    pt5.setColor(Color.rgb(255, 255, 255));
                    which_button = 6;
                } else if (x > sq_size*9 + origin && y > sq_size*17          && x < sq_size*10          && y < sq_size*19) {
                    pt6.setColor(Color.rgb(255, 255, 255));
                    which_button = 7;
                } else if (x > sq_size*10         && y > sq_size*17          && x < sq_size*12 + origin && y < sq_size*18) {
                    pt7.setColor(Color.rgb(255, 255, 255));
                    which_button = FLY_UP;
                } else if (x > sq_size*10         && y > sq_size*18          && x < sq_size*12 + origin && y < sq_size*19) {
                    pt8.setColor(Color.rgb(255, 255, 255));
                    which_button = FLY_DOWN;
                } else {
                    which_button = 8;
                }
                checkPos(v, temp_x, temp_y);
                return true;
            case MotionEvent.ACTION_UP:
                button_click = false;
                pt1.setColor(Color.rgb(220, 220, 220));
                pt2.setColor(Color.rgb(220, 220, 220));
                pt3.setColor(Color.rgb(220, 220, 220));
                pt4.setColor(Color.rgb(220, 220, 220));
                pt5.setColor(Color.rgb(220, 220, 220));
                pt6.setColor(Color.rgb(180, 150, 180));
                pt7.setColor(Color.rgb(150, 150, 150));
                pt8.setColor(Color.rgb(220, 220, 220));
                return true;
            default:
                return true;
        }
    }
    public void checkPos(View v, int j, int i){
        switch (current_floor[i][j]) {
            case -4:        // event floor
                switch (floor_num) {
                    case 3:
                        AlertDialog.Builder f3_builder = new AlertDialog.Builder(v.getContext());
                        f3_builder.setMessage("You are trapped, kys haha");
                        AlertDialog f3_dialog = f3_builder.create();
                        f3_dialog.setCanceledOnTouchOutside(true);
                        f3_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                proceed = true;
                            }
                        });
                        f3_dialog.show();
                        break;
                    default:
                        break;
                }
                break;

            case 21:        // thief
                AlertDialog.Builder thief_builder = new AlertDialog.Builder(v.getContext());
                thief_builder.setMessage("thief!");
                AlertDialog thief_dialog = thief_builder.create();
                thief_dialog.show();
                break;
            case 22:        // saint
                AlertDialog.Builder saint_builder = new AlertDialog.Builder(v.getContext());
                saint_builder.setMessage("saint");
                AlertDialog saint_dialog = saint_builder.create();
                saint_dialog.show();
                break;
            case 23:        // merchant
                AlertDialog.Builder merchant_builder = new AlertDialog.Builder(v.getContext());
                merchant_builder.setMessage("buy!");
                merchant_builder.setPositiveButton("cool", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gold = 1000;
                    }
                });
                merchant_builder.setNegativeButton("nah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gold = 10;
                    }
                });
                AlertDialog merchant_dialog = merchant_builder.create();
                merchant_dialog.show();
                break;
            case 26:        // shop
                String item_atk = "Attack \t+ " + String.valueOf(2 + 2 * (floor_num/10));
                String item_def = "Defence \t+ " + String.valueOf(4 + 4 * (floor_num/10));
                String price = "Would you like to spend " + String.valueOf(10*price_idx*price_idx+10*price_idx+20)+ " gold for one of the following items";
                String[] item_list = {"Health \t+ 1000", item_atk, item_def, "Not this time"};
                AlertDialog.Builder shop_builder = new AlertDialog.Builder(v.getContext());
                shop_builder.setTitle(price);
                shop_builder.setItems(item_list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            count_y += 10;
                        } else if (item == 1) {
                            count_b += 10;
                        } else if (item == 2){
                            count_r += 10;
                        } else {
                            count_y = 100;
                        }
                    }
                });
                AlertDialog shop_list = shop_builder.create();
                shop_list.show();
                break;
            case 28:
                AlertDialog.Builder princess_builder = new AlertDialog.Builder(v.getContext());
                princess_builder.setMessage("I am fake, fuck you!");
                AlertDialog princess_dialog = princess_builder.create();
                princess_dialog.show();
                break;
            default:
                break;
        }
    }

    public class GameView extends SurfaceView implements Runnable {

        private Thread t = null;
        private SurfaceHolder holder;
        private boolean isItOK = false;

        public GameView(Context context) {
            super(context);
            holder = getHolder();
            //Log.v(TAG, "tell me the width = " + this.getWidth());
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

        /*
        @Override
        public boolean performClick() {
            super.performClick();
            return true;
        }
        */
        }

        public void run() {

            //Log.v(TAG, "tell me something");
            while (true) {
                if (!holder.getSurface().isValid()) {
                    continue;
                }
                Canvas canvas = holder.lockCanvas();
                sq_size = canvas.getWidth() / 12;
                holder.unlockCanvasAndPost(canvas);
                break;
            }
            resize_bitmaps();

            int xx = hero_x * sq_size - sq_size / 2;
            int yy = hero_y * sq_size - sq_size / 2;
            hero_sprite = new Sprite(GameView.this, hero, xx, yy, (byte)30);
            red_star_sprite = new Sprite(GameView.this, t___logo, xx, yy, (byte)10);
            kid_sprite = new Sprite(GameView.this, kid);


            while (isItOK) {
                if (!holder.getSurface().isValid()) {
                    continue;
                }
                if (isBattle && !isWalk)
                    battle_animation(hero_attack, hero_y, hero_x);
                else if (isEvent && !isWalk)
                    event(floor_num);
                if (button_click && !isWalk && !isBattle && !isEvent)
                    button_logic(which_button);
                if (refresh_ctr) {
                    current_floor = current_game.get_one_floor(floor_num);
                    load_objects(current_floor);
                    refresh_ctr = false;
                }
                Canvas c = holder.lockCanvas();
                this.draw_game(c);
                holder.unlockCanvasAndPost(c);
            }
        }

        private void resize_bitmaps(){
            menu_health = createScaledBitmap(menu_health, sq_size, sq_size, false);
            menu_gold = createScaledBitmap(menu_gold, sq_size, sq_size, false);
            menu_background = createScaledBitmap(menu_background, sq_size, sq_size, false);
            menu_up = createScaledBitmap(menu_up, sq_size*2 - sq_size/2, sq_size*2 - sq_size/2, false);
            menu_down = createScaledBitmap(menu_down, sq_size*2 - sq_size/2, sq_size*2 - sq_size/2, false);
            menu_left = createScaledBitmap(menu_left, sq_size*2 - sq_size/2, sq_size*2 - sq_size/2, false);
            menu_right = createScaledBitmap(menu_right, sq_size*2 - sq_size/2, sq_size*2 - sq_size/2, false);
            menu_stf1 = createScaledBitmap(i_stf_wsdm, sq_size*2 - sq_size/2, sq_size*2 - sq_size/2, false);
            menu_stf2 = createScaledBitmap(i_stf_echo, sq_size*2 - sq_size/2, sq_size*2 - sq_size/2, false);
            menu_stf3 = createScaledBitmap(i_stf_spce, sq_size*2 - sq_size/2, sq_size*2 - sq_size/2, false);
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
            m____squid = createScaledBitmap(m____squid, sq_size * 3, sq_size, false);
            m___dragon = createScaledBitmap(m___dragon, sq_size * 3, sq_size, false);
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
            hero = createScaledBitmap(hero, sq_size * 3, sq_size * 4, false);
        }

        private void load_objects(byte[][] curr_floor){
            all_sprites.clear();
            int origin = 0 - sq_size/2;
            for (int i = 1; i < 12; i++){
                for (int j = 1; j < 12; j++){
                    byte b;
                    switch (curr_floor[i][j]){
                        case -4:    // event floor
                            b = -4;
                            Sprite sp_event_floor = new Sprite(GameView.this, t__floor, origin+j*sq_size, origin+i*sq_size, b);
                            all_sprites.add(sp_event_floor);
                            break;
                        case -3:    // event wall
                            b = -3;
                            Sprite sp_event_wall = new Sprite(GameView.this, t___wall, origin+j*sq_size, origin+i*sq_size, b);
                            all_sprites.add(sp_event_wall);
                            break;
                        case -2:    // fake floor
                            b = -2;
                            Sprite sp_fake_floor = new Sprite(GameView.this, t__floor, origin+j*sq_size, origin+i*sq_size, b);
                            all_sprites.add(sp_fake_floor);
                            break;
                        case -1:    // fake wall
                            b = -1;
                            Sprite sp_fake_wall = new Sprite(GameView.this, t___wall, origin+j*sq_size, origin+i*sq_size, b);
                            all_sprites.add(sp_fake_wall);
                            break;
                        case 0:     // wall
                            b = 0;
                            Sprite sp_wall = new Sprite(GameView.this, t___wall, origin+j*sq_size, origin+i*sq_size, b);
                            all_sprites.add(sp_wall);
                            break;
                        case 1:     // floor
                            b = 1;
                            Sprite sp_floor = new Sprite(GameView.this, t__floor, origin+j*sq_size, origin+i*sq_size, b);
                            all_sprites.add(sp_floor);
                            break;
                        case 2:     // star
                            b = 2;
                            Sprite sp_star = new Sprite(GameView.this, t___star, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_star);
                            break;
                        case 3:     // upstairs
                            b = 3;
                            Sprite sp_upstairs = new Sprite(GameView.this, t_ustair, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_upstairs);
                            break;
                        case 4:     // downstairs
                            b = 4;
                            Sprite sp_downstairs = new Sprite(GameView.this, t_dstair, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_downstairs);
                            break;
                        case 5:     // door_yellow
                            b = 5;
                            Sprite sp_door_y = new Sprite(GameView.this, t_door_y, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_door_y);
                            break;
                        case 6:     // door_blue
                            b = 6;
                            Sprite sp_door_b = new Sprite(GameView.this, t_door_b, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_door_b);
                            break;
                        case 7:     // door_red
                            b = 7;
                            Sprite sp_door_r = new Sprite(GameView.this, t_door_r, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_door_r);
                            break;
                        case 8:     // door_magic
                            b = 8;
                            Sprite sp_door_m = new Sprite(GameView.this, t_door_m, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_door_m);
                            break;
                        case 9:     // prison
                            b = 9;
                            Sprite sp_prison = new Sprite(GameView.this, t_prison, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_prison);
                            break;
                        case 10:     // logo
                            b = 10;
                            Sprite sp_logo = new Sprite(GameView.this, t___logo, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_logo);
                            break;
                        case 11:    // iron_sword
                            b = 11;
                            Sprite sp_iron_sword = new Sprite(GameView.this, w___ironw, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_iron_sword);
                            break;
                        case 12:    // iron_shield
                            b = 12;
                            Sprite sp_iron_shield = new Sprite(GameView.this, w___ironh, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_iron_shield);
                            break;
                        case 13:    // silver_sword
                            b = 13;
                            Sprite sp_silver_sword = new Sprite(GameView.this, w_silverw, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_silver_sword);
                            break;
                        case 14:    // silver_shield
                            b = 14;
                            Sprite sp_silver_shield = new Sprite(GameView.this, w_silverh, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_silver_shield);
                            break;
                        case 15:    // knight_sword
                            b = 15;
                            Sprite sp_knight_sword = new Sprite(GameView.this, w_knightw, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_knight_sword);
                            break;
                        case 16:    // knight_shield
                            b = 16;
                            Sprite sp_knight_shield = new Sprite(GameView.this, w_knighth, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_knight_shield);
                            break;
                        case 17:    // divine_sword
                            b = 17;
                            Sprite sp_divine_sword = new Sprite(GameView.this, w_divinew, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_divine_sword);
                            break;
                        case 18:    // divine_shield
                            b = 18;
                            Sprite sp_divine_shield = new Sprite(GameView.this, w_divineh, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_divine_shield);
                            break;
                        case 19:    // sacred_sword
                            b = 19;
                            Sprite sp_sacred_sword = new Sprite(GameView.this, w_sacredw, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_sacred_sword);
                            break;
                        case 20:    // sacred_shield
                            b = 20;
                            Sprite sp_sacred_shield = new Sprite(GameView.this, w_sacredh, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_sacred_shield);
                            break;
                        case 21:    // thief
                            b = 21;
                            Sprite sp_thief = new Sprite(GameView.this, n___thief, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_thief);
                            break;
                        case 22:    // saint
                            b = 22;
                            Sprite sp_saint = new Sprite(GameView.this, n___saint, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_saint);
                            break;
                        case 23:    // merchant
                            b = 23;
                            Sprite sp_merchant = new Sprite(GameView.this, n_merchat, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_merchant);
                            break;
                        case 24:    // fairy
                            b = 24;
                            Sprite sp_fairy = new Sprite(GameView.this, n___fairy, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_fairy);
                            break;
                        case 25:    // shop_left
                            b = 25;
                            Sprite sp_shop_l = new Sprite(GameView.this, n_shop__l, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_shop_l);
                            break;
                        case 26:    // shop_middle
                            b = 26;
                            Sprite sp_shop_m = new Sprite(GameView.this, n_shop__m, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_shop_m);
                            break;
                        case 27:    // shop_right
                            b = 27;
                            Sprite sp_shop_r = new Sprite(GameView.this, n_shop__r, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_shop_r);
                            break;
                        case 28:    // princess
                            b = 28;
                            Sprite sp_princess = new Sprite(GameView.this, n_princes, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_princess);
                            break;
                        case 29:    // lava
                            b = 29;
                            Sprite sp_lava = new Sprite(GameView.this, n____lava, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_lava);
                            break;
                        case 31:    // green slime
                            b = 31;
                            Sprite sp_slime_g = new Sprite(GameView.this, m__slime_g, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_slime_g);
                            break;
                        case 32:    // red slime
                            b = 32;
                            Sprite sp_slime_r = new Sprite(GameView.this, m__slime_r, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_slime_r);
                            break;
                        case 33:    // fierce bat
                            b = 33;
                            Sprite sp_fierce_bat = new Sprite(GameView.this, m_bat_fier, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_fierce_bat);
                            break;
                        case 34:    // priest
                            b = 34;
                            Sprite sp_priest = new Sprite(GameView.this, m___priest, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_priest);
                            break;
                        case 35:    // skeleton
                            b = 35;
                            Sprite sp_skeleton = new Sprite(GameView.this, m_skeleton, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_skeleton);
                            break;
                        case 36:    // skeleton_warrior
                            b = 36;
                            Sprite sp_skeleton_warrior = new Sprite(GameView.this, m_skelet_w, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_skeleton_warrior);
                            break;
                        case 37:    // gate_keeper
                            b = 37;
                            Sprite sp_gate_keeper = new Sprite(GameView.this, m_gatekeep, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_gate_keeper);
                            break;
                        case 38:    // skeleton_captain
                            b = 38;
                            Sprite sp_skeleton_captain = new Sprite(GameView.this, m_skelet_c, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_skeleton_captain);
                            break;
                        case 39:    // black_slime
                            b = 39;
                            Sprite sp_slime_b = new Sprite(GameView.this, m__slime_b, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_slime_b);
                            break;
                        case 40:    // giant bat
                            b = 40;
                            Sprite sp_giant_bat = new Sprite(GameView.this, m_bat_gian, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_giant_bat);
                            break;
                        case 41:    // priest_master
                            b = 41;
                            Sprite sp_priest_master = new Sprite(GameView.this, m_priest_m, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_priest_master);
                            break;
                        case 42:    // zombie
                            b = 42;
                            Sprite sp_zombie = new Sprite(GameView.this, m___zombie, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_zombie);
                            break;
                        case 43:    // stone_guardian
                            b = 43;
                            Sprite sp_stone_guardian = new Sprite(GameView.this, m_stone_gd, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_stone_guardian);
                            break;
                        case 44:    // zombie_warrior
                            b = 44;
                            Sprite sp_zombie_warrior = new Sprite(GameView.this, m_zombie_w, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_zombie_warrior);
                            break;
                        case 45:    // vampire
                            b = 45;
                            Sprite sp_vampire = new Sprite(GameView.this, m__vampire, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_vampire);
                            break;
                        case 46:    // slime_man
                            b = 46;
                            Sprite sp_slime_man = new Sprite(GameView.this, m__slime_m, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_slime_man);
                            break;
                        case 47:    // skeleton_elite
                            b = 47;
                            Sprite sp_skeleton_elite = new Sprite(GameView.this, m_skelet_e, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_skeleton_elite);
                            break;
                        case 48:    // knight
                            b = 48;
                            Sprite sp_knight = new Sprite(GameView.this, m___knight, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_knight);
                            break;
                        case 49:    // gatekeeper_elite
                            b = 49;
                            Sprite sp_gatekeeper_elite = new Sprite(GameView.this, m_gatekp_e, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_gatekeeper_elite);
                            break;
                        case 50:    // swordsman
                            b = 50;
                            Sprite sp_swordsman = new Sprite(GameView.this, m_swordsmn, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_swordsman);
                            break;
                        case 51:    // knight_elite
                            b = 51;
                            Sprite sp_knight_elite = new Sprite(GameView.this, m_knight_e, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_knight_elite);
                            break;
                        case 52:    // knight_captain
                            b = 52;
                            Sprite sp_knight_captain = new Sprite(GameView.this, m_knight_c, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_knight_captain);
                            break;
                        case 53:    // slimelord
                            b = 53;
                            Sprite sp_slimelord = new Sprite(GameView.this, m_slimelod, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_slimelord);
                            break;
                        case 54:    // vampire_bat
                            b = 54;
                            Sprite sp_vampire_bat = new Sprite(GameView.this, m_bat_vamp, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_vampire_bat);
                            break;
                        case 55:    // mage
                            b = 55;
                            Sprite sp_mage = new Sprite(GameView.this, m_____mage, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_mage);
                            break;
                        case 56:    // mage master
                            b = 56;
                            Sprite sp_mage_master = new Sprite(GameView.this, m_mage_mas, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_mage_master);
                            break;
                        case 57:    // demo_sergent
                            b = 57;
                            Sprite sp_demo_sergent = new Sprite(GameView.this, m_demo_sgt, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_demo_sergent);
                            break;
                        case 58:    // dark_knight
                            b = 58;
                            Sprite sp_dark_knight = new Sprite(GameView.this, m_d_knight, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_dark_knight);
                            break;
                        case 59:    // gate_guardian
                            b = 59;
                            Sprite sp_gate_guardian = new Sprite(GameView.this, m_gate_gdn, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_gate_guardian);
                            break;
                        case 60:    // fake_zeno
                            b = 60;
                            Sprite sp_fake_zeno = new Sprite(GameView.this, m_demozeno, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_fake_zeno);
                            break;
                        case 64:    // archmage
                            b = 64;
                            Sprite sp_archmage = new Sprite(GameView.this, m_archmage, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_archmage);
                            break;
                        case 65:    // real_zeno
                            b = 65;
                            Sprite sp_real_zeno = new Sprite(GameView.this, m_demozeno, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_real_zeno);
                            break;
                        case 71:    // yellow_key
                            b = 71;
                            Sprite sp_key_y = new Sprite(GameView.this, i____key_y, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_key_y);
                            break;
                        case 72:    // blue_key
                            b = 72;
                            Sprite sp_key_b = new Sprite(GameView.this, i____key_b, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_key_b);
                            break;
                        case 73:    // red_key
                            b = 73;
                            Sprite sp_key_r = new Sprite(GameView.this, i____key_r, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_key_r);
                            break;
                        case 74:    // red_potion
                            b = 74;
                            Sprite sp_potion_r = new Sprite(GameView.this, i_potion_r, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_potion_r);
                            break;
                        case 75:    // blue_potion
                            b = 75;
                            Sprite sp_potion_b = new Sprite(GameView.this, i_potion_b, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_potion_b);
                            break;
                        case 76:    // red_crystal
                            b = 76;
                            Sprite sp_crystal_r = new Sprite(GameView.this, i_crystl_r, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_crystal_r);
                            break;
                        case 77:    // blue_crystal
                            b = 77;
                            Sprite sp_crystal_b = new Sprite(GameView.this, i_crystl_b, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_crystal_b);
                            break;
                        case 78:    // staff_of_wisdom
                            b = 78;
                            Sprite sp_staff_wisdom = new Sprite(GameView.this, i_stf_wsdm, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_staff_wisdom);
                            break;
                        case 79:    // staff_of_echo
                            b = 79;
                            Sprite sp_staff_echo = new Sprite(GameView.this, i_stf_echo, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_staff_echo);
                            break;
                        case 80:    // staff_of_space
                            b = 80;
                            Sprite sp_staff_space = new Sprite(GameView.this, i_stf_spce, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_staff_space);
                            break;
                        case 81:    // cross
                            b = 81;
                            Sprite sp_cross = new Sprite(GameView.this, i____cross, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_cross);
                            break;
                        case 82:    // elixir
                            b = 82;
                            Sprite sp_elixir = new Sprite(GameView.this, i___elixir, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_elixir);
                            break;
                        case 83:    // magic_mattock
                            b = 83;
                            Sprite sp_magic_mattock = new Sprite(GameView.this, i_m_mattok, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_magic_mattock);
                            break;
                        case 84:    // magic_wing_center
                            b = 84;
                            Sprite sp_magic_wing_center = new Sprite(GameView.this, i_wing_cen, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_magic_wing_center);
                            break;
                        case 85:    // enhanced_mattock
                            b = 85;
                            Sprite sp_enhanced_mattock = new Sprite(GameView.this, i_e_mattok, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_enhanced_mattock);
                            break;
                        case 86:    // bomb
                            b = 86;
                            Sprite sp_bomb = new Sprite(GameView.this, i_____bomb, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_bomb);
                            break;
                        case 87:    // magic_wing_up
                            b = 87;
                            Sprite sp_magic_wing_up = new Sprite(GameView.this,i__wing_up , origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_magic_wing_up);
                            break;
                        case 88:    // enhanced_key
                            b = 88;
                            Sprite sp_enhanced_key = new Sprite(GameView.this, i_key_ehac, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_enhanced_key);
                            break;
                        case 89:    // magic_wing_down
                            b = 89;
                            Sprite sp_magic_wing_down = new Sprite(GameView.this, i_wing_dow, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_magic_wing_down);
                            break;
                        case 90:    // lucky_gold
                            b = 90;
                            Sprite sp_lucky_gold = new Sprite(GameView.this, i_lucky_gd, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_lucky_gold);
                            break;
                        case 91:    // dragonsbane
                            b = 91;
                            Sprite sp_dragonsbane = new Sprite(GameView.this, i_dra_bane, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_dragonsbane);
                            break;
                        case 92:    // snow_crystal
                            b = 92;
                            Sprite sp_snow_crystal = new Sprite(GameView.this, i_snow_crs, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_snow_crystal);
                            break;
                        default:    // debug
                            b = 10;
                            Sprite mydebug = new Sprite(GameView.this, t___logo, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(mydebug);
                            break;
                    }
                }
            }
        }

        private void button_logic(int inButton) {
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
                case FLY_UP:
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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
                default:
                    break;

            }
        }

        private void draw_game(Canvas canvas) {
            if (blackout) {
                canvas.drawARGB(255, 0,0,0);
                return;
            }
            final int origin = 0 - sq_size/2;
            final int margin = sq_size / 10;
            // -------------------- Draw Floor ----------------------
            for (int a = 1; a < 12; a++){
                for (int b = 1; b < 12; b++){
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
            for (int a = 0; a < 12; a++){
                for (int b = 12; b < 20; b++){
                    canvas.drawBitmap(menu_background,sq_size * a, sq_size * b, null);
                }
            }
            Paint num_box = new Paint();
            num_box.setStrokeWidth(10);
            num_box.setColor(Color.rgb(130, 130, 130));
            canvas.drawRect(sq_size*2-margin*4, sq_size*13+origin+margin, sq_size*4-margin, sq_size*14+origin-margin, num_box);
            canvas.drawRect(sq_size*2-margin*4, sq_size*14+origin+margin, sq_size*4-margin, sq_size*15+origin-margin, num_box);
            canvas.drawRect(sq_size*2-margin*4, sq_size*15+origin+margin, sq_size*4-margin, sq_size*16+origin-margin, num_box);
            canvas.drawRect(sq_size*2-margin*4, sq_size*16+origin+margin, sq_size*4-margin, sq_size*17+origin-margin, num_box);
            canvas.drawRect(sq_size*5+margin, sq_size*14+origin+margin, sq_size*7-margin*3, sq_size*15+origin-margin, num_box);
            canvas.drawRect(sq_size*5+margin, sq_size*15+origin+margin, sq_size*7-margin*3, sq_size*16+origin-margin, num_box);
            canvas.drawRect(sq_size*5+margin, sq_size*16+origin+margin, sq_size*7-margin*3, sq_size*17+origin-margin, num_box);
            canvas.drawBitmap(menu_health, sq_size + origin, 13 * sq_size + origin, null);
            canvas.drawBitmap(w___ironw, sq_size + origin, 14 * sq_size + origin, null);
            canvas.drawBitmap(w___ironh, sq_size + origin, 15 * sq_size + origin, null);
            canvas.drawBitmap(menu_gold, sq_size + origin, 16 * sq_size + origin, null);
            canvas.drawBitmap(i____key_y, sq_size * 4, 14 * sq_size + origin, null);
            canvas.drawBitmap(i____key_b, sq_size * 4, 15 * sq_size + origin, null);
            canvas.drawBitmap(i____key_r, sq_size * 4, 16 * sq_size + origin, null);
            // ------------------- Draw Buttons -----------------------
            canvas.drawRect(sq_size*9 + origin, sq_size*13 + origin, sq_size*10,          sq_size*14,          pt1);
            canvas.drawRect(sq_size*9 + origin, sq_size*16 + origin, sq_size*10,          sq_size*17,          pt2);
            canvas.drawRect(sq_size*7,          sq_size*14,          sq_size*9  + origin, sq_size*16 + origin, pt3);
            canvas.drawRect(sq_size*10,         sq_size*14,          sq_size*12 + origin, sq_size*16 + origin, pt4);
            canvas.drawRect(sq_size*7,          sq_size*17,          sq_size*9  + origin, sq_size*19,          pt5);
            canvas.drawRect(sq_size*9 + origin, sq_size*17,          sq_size*10,          sq_size*19,          pt6);
            canvas.drawRect(sq_size*10,         sq_size*17,          sq_size*12 + origin, sq_size*18,          pt7);
            canvas.drawRect(sq_size*10,         sq_size*18,          sq_size*12 + origin, sq_size*19,          pt8);
            canvas.drawBitmap(menu_up,    sq_size*9 + origin, sq_size*13 + origin, null);
            canvas.drawBitmap(menu_down,  sq_size*9 + origin, sq_size*16 + origin, null);
            canvas.drawBitmap(menu_left,  sq_size*7,          sq_size*14,          null);
            canvas.drawBitmap(menu_right, sq_size*10,         sq_size*14,          null);
            Paint stash_paint = new Paint();
            stash_paint.setStrokeWidth(10);
            stash_paint.setColor(Color.rgb(180, 150, 180));
            for (int a = 1; a < 7; a++){
                canvas.drawRect(sq_size*a + origin, sq_size*17, sq_size*(a+1) + origin, sq_size*18, stash_paint);
                if (a%2 == 1)
                    stash_paint.setColor(Color.rgb(220, 220, 220));
                else
                    stash_paint.setColor(Color.rgb(180, 150, 180));
                canvas.drawRect(sq_size*a + origin, sq_size*18, sq_size*(a+1) + origin, sq_size*19, stash_paint);
            }
            if (stf_wsdm)
                canvas.drawBitmap(menu_stf1, sq_size*7,           sq_size*17-origin/2, null);
            if (stf_echo)
                canvas.drawBitmap(menu_stf2, sq_size*9  + origin, sq_size*17-origin/2, null);
            if (stf_space)
                canvas.drawBitmap(menu_stf3, sq_size*10,          sq_size*17-origin/2, null);
            if (cross)
                canvas.drawBitmap(i____cross, sq_size   + origin, sq_size*17,          null);
            if (lucky_gold)
                canvas.drawBitmap(i_lucky_gd, sq_size*2 + origin, sq_size*17,          null);
            if (dragonsbane)
                canvas.drawBitmap(i_dra_bane, sq_size   + origin, sq_size*18,          null);
            if (snow_cryst)
                canvas.drawBitmap(i_snow_crs, sq_size*2 + origin, sq_size*18,          null);
            if (m_mattock)
                canvas.drawBitmap(i_m_mattok, sq_size*3 + origin, sq_size*17,          null);
            if (e_mattock)
                canvas.drawBitmap(i_e_mattok, sq_size*3 + origin, sq_size*18,          null);
            if (wing_up)
                canvas.drawBitmap(i__wing_up, sq_size*4 + origin, sq_size*17,          null);
            if (wing_cent)
                canvas.drawBitmap(i_wing_cen, sq_size*5 + origin, sq_size*17,          null);
            if (wing_down)
                canvas.drawBitmap(i_wing_dow, sq_size*6 + origin, sq_size*17,          null);
            if (elixir)
                canvas.drawBitmap(i___elixir, sq_size*4 + origin, sq_size*18,          null);
            if (key_enhac)
                canvas.drawBitmap(i_key_ehac, sq_size*5 + origin, sq_size*18,          null);
            if (bomb)
                canvas.drawBitmap(i_____bomb, sq_size*6 + origin, sq_size*18,          null);
            // ------------------- Update sprites -----------------------
            for (int idx = 0; idx < all_sprites.size(); idx++){
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
                    }
                } else {
                    hero_sprite.set_location(hero_x * sq_size - sq_size / 2, hero_y * sq_size - sq_size / 2);
                    hero_sprite.stand(canvas);
                }
            }
            // ------------------- Draw star to indicate fighting -----------------------
            if (show_fight){
                red_star_sprite.set_location(hero_x * sq_size - sq_size / 2, hero_y * sq_size - sq_size / 2);
                red_star_sprite.display(canvas);
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // ------------------- Draw Text -----------------------
            Paint textpaint = new Paint();
            textpaint.setColor(Color.BLACK);
            textpaint.setTextSize(sq_size*3/4);
            String my_text = String.valueOf(count_y);
            canvas.drawText(my_text, sq_size*5+margin*3, sq_size*14+margin*3, textpaint);
            my_text = String.valueOf(count_b);
            canvas.drawText(my_text, sq_size*5+margin*3, sq_size*15+margin*3, textpaint);
            my_text = String.valueOf(count_r);
            canvas.drawText(my_text, sq_size*5+margin*3, sq_size*16+margin*3, textpaint);
            my_text = String.valueOf(hp);
            canvas.drawText(my_text, sq_size*2-margin*2, sq_size*13+margin*3, textpaint);
            my_text = String.valueOf(atk);
            canvas.drawText(my_text, sq_size*2-margin*2, sq_size*14+margin*3, textpaint);
            my_text = String.valueOf(def);
            canvas.drawText(my_text, sq_size*2-margin*2, sq_size*15+margin*3, textpaint);
            my_text = String.valueOf(gold);
            canvas.drawText(my_text, sq_size*2-margin*2, sq_size*16+margin*3, textpaint);
            textpaint.setTextSize(sq_size);
            my_text = String.valueOf(floor_num) + " F";
            canvas.drawText(my_text, 5*sq_size, 13*sq_size+margin*2, textpaint);
            // ------------------- Debug purpose -----------------------
            //kid_sprite.loopRun(canvas);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //*/
            canvas.drawBitmap(ball, x - ball.getWidth()/2, y - ball.getHeight()/2, null);
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
                case -4:        // event floor
                    current_floor[i][j] = 1;
                    isEvent = true;
                    return 1;
                case -2:        // fake floor
                    current_floor[i][j] = 0;
                    refresh_ctr = true;
                    return 0;
                case -1:        // fake wall
                    current_floor[i][j] = 1;
                    refresh_ctr = true;
                    return 1;
                case 0:         // wall
                    return 0;
                case 1:         // floor
                    return 1;
                case 3:         // upstairs
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    current_game.put_one_floor(floor_num, current_floor);
                    floor_num++;
                    int pairup[] = find_hero_next_floor(true,floor_num);
                    hero_y = pairup[0];
                    hero_x = pairup[1];
                    refresh_ctr = true;
                    return 0;
                case 4:         // downstairs
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    current_game.put_one_floor(floor_num, current_floor);
                    floor_num--;
                    int pairdown[] = find_hero_next_floor(false,floor_num);
                    hero_y = pairdown[0];
                    hero_x = pairdown[1];
                    refresh_ctr = true;
                    return 0;
                case 5:         // yellow door
                    if (count_y > 0) {
                        count_y--;
                        current_floor[i][j] = 1;
                        refresh_ctr = true;
                        return 1;
                    } else
                        return 0;
                case 6:         // blue door
                    if (count_b > 0) {
                        count_b--;
                        current_floor[i][j] = 1;
                        refresh_ctr = true;
                        return 1;
                    } else
                        return 0;
                case 7:         // red door
                    if (count_r > 0) {
                        count_r--;
                        current_floor[i][j] = 1;
                        refresh_ctr = true;
                        return 1;
                    } else
                        return 0;
                case 8:         // magic door
                    int ret_num = 0;
                    switch (floor_num){
                        case 8:
                            if (current_floor[5][9] == 1 && current_floor[5][11] ==1)
                                ret_num = 1;
                        default:
                            ret_num = 0;
                    }
                    return ret_num;
                case 9:         // prison
                    return 0;
                case 11:        // iron sword
                    current_floor[i][j] = 1;
                    atk += 10;
                    refresh_ctr = true;
                    return 1;
                case 12:        // iron shield
                    current_floor[i][j] = 1;
                    def += 10;
                    refresh_ctr = true;
                    return 1;
                case 13:        // silver sword
                    current_floor[i][j] = 1;
                    atk += 20;
                    refresh_ctr = true;
                    return 1;
                case 14:        // silver shield
                    current_floor[i][j] = 1;
                    def += 20;
                    refresh_ctr = true;
                    return 1;
                case 15:        // knight sword
                    current_floor[i][j] = 1;
                    atk += 40;
                    refresh_ctr = true;
                    return 1;
                case 16:        // knight shield
                    current_floor[i][j] = 1;
                    def += 40;
                    refresh_ctr = true;
                    return 1;
                case 17:        // divine sword
                    current_floor[i][j] = 1;
                    atk += 50;
                    refresh_ctr = true;
                    return 1;
                case 18:        // divine shield
                    current_floor[i][j] = 1;
                    def += 50;
                    refresh_ctr = true;
                    return 1;
                case 19:        // sacred sword
                    current_floor[i][j] = 1;
                    atk += 100;
                    refresh_ctr = true;
                    return 1;
                case 20:        // sacred shield
                    current_floor[i][j] = 1;
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
                        current_floor[i][j] = 1;
                        refresh_ctr = true;
                        return 1;
                    } else
                        return 0;
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

                case 71:        // yellow key
                    current_floor[i][j] = 1;
                    count_y++;
                    refresh_ctr = true;
                    return 1;
                case 72:        // blue key
                    current_floor[i][j] = 1;
                    count_b++;
                    refresh_ctr = true;
                    return 1;
                case 73:        // red key
                    current_floor[i][j] = 1;
                    count_r++;
                    refresh_ctr = true;
                    return 1;
                case 74:        // red potion
                    current_floor[i][j] = 1;
                    hp += 50 * (floor_num/10 + 1);
                    refresh_ctr = true;
                    return 1;
                case 75:        // blue potion
                    current_floor[i][j] = 1;
                    hp += 200 * (floor_num/10 + 1);
                    refresh_ctr = true;
                    return 1;
                case 76:        // red crystal
                    current_floor[i][j] = 1;
                    atk += (floor_num/10 + 1);
                    refresh_ctr = true;
                    return 1;
                case 77:        // blue crystal
                    current_floor[i][j] = 1;
                    def += (floor_num/10 + 1);
                    refresh_ctr = true;
                    return 1;
                case 78:        // staff of wisdom
                    current_floor[i][j] = 1;
                    stf_wsdm = true;
                    refresh_ctr = true;
                    return 1;
                case 79:        // staff of echo
                    current_floor[i][j] = 1;
                    stf_echo = true;
                    refresh_ctr = true;
                    return 1;
                case 80:        // staff of space
                    current_floor[i][j] = 1;
                    stf_space = true;
                    refresh_ctr = true;
                    return 1;
                case 81:        // cross
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
                    current_floor[i][j] = 1;
                    m_mattock = true;
                    refresh_ctr = true;
                    return 1;
                case 84:        // magic wing cent
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
                    current_floor[i][j] = 1;
                    bomb = true;
                    refresh_ctr = true;
                    return 1;
                case 87:        // magic wing up
                    current_floor[i][j] = 1;
                    wing_up = true;
                    refresh_ctr = true;
                    return 1;
                case 88:        // enhanced key
                    current_floor[i][j] = 1;
                    key_enhac = true;
                    refresh_ctr = true;
                    return 1;
                case 89:        // magic wing down
                    current_floor[i][j] = 1;
                    wing_down = true;
                    refresh_ctr = true;
                    return 1;
                case 90:        // lucky gold
                    current_floor[i][j] = 1;
                    lucky_gold = true;
                    refresh_ctr = true;
                    return 1;
                case 91:        // dragonsbane
                    current_floor[i][j] = 1;
                    dragonsbane = true;
                    refresh_ctr = true;
                    return 1;
                case 92:        // snow crystal
                    current_floor[i][j] = 1;
                    snow_cryst = true;
                    refresh_ctr = true;
                    return 1;

                default:
                    return 1;
            }
        }

        private void event(int in_floor_num){
            switch (in_floor_num) {
                case 3:
                    if (act == 0) {
                        if (!proceed)
                            return;
                        act++;
                        current_floor[7][5] = 60;
                        refresh_ctr = true;
                    } else if (act == 1) {
                        try {
                            Thread.sleep(900);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        act++;
                        current_floor[8][5] = 57;
                        current_floor[10][5] = 57;
                        current_floor[9][4] = 57;
                        current_floor[9][6] = 57;
                        refresh_ctr = true;
                    } else if (act == 2) {
                        try {
                            Thread.sleep(900);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        act++;
                        show_fight = true;
                        hp = 0;
                    } else if (act == 3) {
                        try {
                            Thread.sleep(900);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        act++;
                        show_fight = false;
                    } else {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        act = 0;
                        floor_num = 2;
                        hero_x = 4; hero_y = 8;
                        hp = 200; atk = 10; def = 10;
                        refresh_ctr = true;
                        isEvent = false;
                        blackout = true;
                    }
                    break;
                default:
                    isEvent = true;
                    break;
            }
        }

        private int[] find_hero_next_floor(boolean dir, int in_floor_num) {
            byte[][] floor = current_game.get_one_floor(in_floor_num);
            int i = 6,j = 6;
            for (int a = 1; a < 12; a++){
                for (int b = 1; b < 12; b++){
                    if (dir) {  // going up
                        if (floor[a][b] == 4){
                            i = a; j = b;
                        }
                    } else {    // going down
                        if (floor[a][b] == 3){
                            i = a; j = b;
                        }
                    }
                }
            }
            if (floor[i+1][j] == 1) {
                int[] res = {i+1,j};
                return res;
            }
            if (floor[i-1][j] == 1) {
                int[] res = {i-1,j};
                return res;
            }
            if (floor[i][j-1] == 1) {
                int[] res = {i,j-1};
                return res;
            }
            if (floor[i][j+1] == 1) {
                int[] res = {i,j+1};
                return res;
            }
            int[] res = {12,1};
            return res;
        }

        private int battle_preparation(int m_idx){
            int rst = 0;
            hero_attack = true;
            m_hp = m_table[m_idx][0];
            m_atk = m_table[m_idx][1];
            m_def = m_table[m_idx][2];
            m_gold = m_table[m_idx][3];
            int a_damage_b = atk - m_def;
            int b_damage_a = m_atk - def;
            int ahp = hp, bhp = m_hp;
            boolean a_attack = true;
            if (a_damage_b <= 0) {
                // don't have enough attack to damage the monster
                return rst;
            } else if (b_damage_a <= 0) {
                // monster can't damage hero
                isBattle = true;
                rst = 1;
                return rst;
            } else{
                while(ahp > 0 && bhp > 0){
                    if (a_attack)
                        bhp -= a_damage_b;
                    else
                        ahp -= b_damage_a;
                    a_attack = !a_attack;
                }
                if (ahp > 0) {
                    isBattle = true;
                    rst = 1;
                }
                else
                    rst = 0;
                return rst;
            }

        }

        private void battle_animation(boolean isHero, int i, int j){
            int a_damage_b = atk - m_def;
            int b_damage_a = m_atk - def;
            if (b_damage_a < 0)
                b_damage_a = 0;
            if (m_hp <= 0){
                isBattle = false;
                show_hero = true;
                show_fight = false;
                current_floor[i][j] = 1;
                refresh_ctr = true;
                if (lucky_gold)
                    gold += m_gold * 2;
                else
                    gold += m_gold;
            } else {
                isBattle = true;
            }
            if (isHero) {
                show_hero = false;
                show_fight = true;
                m_hp -= a_damage_b;
            }
            else {
                show_hero = true;
                show_fight = false;
                hp -= b_damage_a;
            }
            hero_attack = !hero_attack;
        }


    }


}



















