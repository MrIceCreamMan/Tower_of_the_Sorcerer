package com.the95.tower_of_sorcerer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;

import static android.graphics.Bitmap.createScaledBitmap;

public class Gamelogic extends Activity implements View.OnTouchListener {

    private GameView            gameview;
    private Floors              current_game;
    private byte[][]            current_floor;
    private int                 floor_num, sq_size;
    private boolean             load_ctr, refresh_ctr;
    private float               x, y;
    private ArrayList<Sprite>   all_sprites;
    private Bitmap              ball, kid, pic_debug;

    private Sprite              kid_sprite;

    private Bitmap              t__floor, t___wall, t___star, t_ustair, t_dstair;
    private Bitmap              t_door_y, t_door_b, t_door_r, t_door_m, t_prison, t___logo;
    private Bitmap              w___ironw, w_silverw, w_knightw, w_divinew, w_sacredw;
    private Bitmap              w___ironh, w_silverh, w_knighth, w_divineh, w_sacredh;
    private Bitmap              n___thief, n___saint, n_merchat, n___fairy;
    private Bitmap              n_shop__l, n_shop__m, n_shop__r;
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

    private static final String TAG = "debuuuuuuuuuuuuuuuuuug";
    //Log.v(TAG, "x = " + me.getX() + " y = " + me.getY());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameview = new GameView(this);
        gameview.setOnTouchListener(this);
        current_game = new Floors();
        current_floor = current_game.get_one_floor(1);
        floor_num = 1; sq_size = 32;
        load_ctr = refresh_ctr = true;
        x = y = 0;
        all_sprites = new ArrayList<>();
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.brokeearth);
        pic_debug = BitmapFactory.decodeResource(getResources(), R.drawable.z1_debug);
        kid = BitmapFactory.decodeResource(getResources(), R.drawable.poke);

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

    public class GameView extends SurfaceView implements Runnable {

        private Thread t = null;
        private SurfaceHolder holder;
        private boolean isItOK = false;

        public GameView(Context context) {
            super(context);
            holder = getHolder();
            //Log.v(TAG, "tell me the width = " + this.getWidth());
        }

        public void run() {

            //Log.v(TAG, "tell me something");
            while (true) {
                if (!holder.getSurface().isValid()) {
                    continue;
                }
                //Log.v(TAG, "hello there");
                Canvas canvas = holder.lockCanvas();
                sq_size = canvas.getWidth() / 12;
                holder.unlockCanvasAndPost(canvas);
                break;
            }

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
            kid_sprite = new Sprite(GameView.this, kid);

            while (isItOK) {
                if (!holder.getSurface().isValid()) {
                    continue;
                }
                if (refresh_ctr) {
                    if (load_ctr) {
                        current_floor = current_game.get_one_floor(floor_num);
                        load_draw_objects(current_floor);
                        load_ctr = false;
                    } else
                        load_draw_objects(current_floor);
                    refresh_ctr = false;
                }
                //Log.v(TAG, "who are you?");
                Canvas c = holder.lockCanvas();
                this.gameviewDraw(c);
                holder.unlockCanvasAndPost(c);
            }

        }

        protected void gameviewDraw(Canvas canvas) {
            canvas.drawARGB(255, 255, 128, 128);
            int origin = 0 - sq_size/2;
            for (int a = 1; a < 12; a++){  // -------------------- Draw Floor ----------------------
                for (int b = 1; b < 12; b++){
                    canvas.drawBitmap(t__floor, origin + sq_size * a, origin + sq_size * b, null);
                }
            }
            for (int i = 0; i < 13; i++) {  // ------------------- Draw Wall -----------------------
                canvas.drawBitmap(t___wall, origin + i * sq_size, origin, null);
                canvas.drawBitmap(t___wall, origin + i * sq_size, origin + 12 * sq_size, null);
                canvas.drawBitmap(t___wall, origin, origin + i * sq_size, null);
                canvas.drawBitmap(t___wall, origin + 12 * sq_size, origin + i * sq_size, null);
            }
            for (int idx = 0; idx < all_sprites.size(); idx++){
                all_sprites.get(idx).update(canvas);
            }
            Paint textpaint = new Paint();
            textpaint.setColor(Color.BLACK);
            textpaint.setTextSize(160);
            //canvas.drawPaint(textpaint);
            String my_text = String.valueOf(floor_num) + " F";
            canvas.drawText(my_text, 300, 1400, textpaint);
            canvas.drawBitmap(ball, x - ball.getWidth()/2, y - ball.getHeight()/2, null);
            kid_sprite.loopRun(canvas);
        }

        public void load_draw_objects(byte[][] curr_floor){
            all_sprites.clear();
            int origin = 0 - sq_size/2;
            for (int i = 1; i < 12; i++){
                for (int j = 1; j < 12; j++){
                    byte b;
                    switch (curr_floor[i][j]){
                        case 0:     // wall
                            b = 0;
                            Sprite sp_wall = new Sprite(GameView.this, t___wall, origin+j*sq_size, origin+i*sq_size, b);
                            all_sprites.add(sp_wall);
                            break;
                        case 1:     // floor
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
                        case 28:    // debug picture
                            b = 28;
                            Sprite sp_debug = new Sprite(GameView.this, pic_debug, origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_debug);
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
    }

    public boolean onTouch(View v, MotionEvent me) {

        //v.performClick();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        switch(me.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = me.getX();
                y = me.getY();
                if (x > 500 && y > 1200){
                    if (floor_num <50)
                        floor_num++;
                    refresh_ctr = true;
                    load_ctr = true;
                }
                else if (x < 500 && y > 1200){
                    if (floor_num > 0)
                        floor_num--;
                    refresh_ctr =true;
                    load_ctr = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                //x = me.getX();
                //y = me.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //x = me.getX();
                //y = me.getY();
                break;
            default:
                break;
        }
        return true;

    }

}



















