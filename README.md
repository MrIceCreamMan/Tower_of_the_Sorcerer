# Tower_of_the_Sorcerer
Android game development practice


----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------

Monster                 Health      Attack      Defense     Gold

m01t1_green_slime       35          18          1           1
m02t1_red_slime         45          20          2           2
m03t1_fierce_bat        35          38          3           3
m04t1_priest            60          32          8           5
m05t1_skeleton          50          42          6           6
m06t1_skeleton_warrior  55          52          12          8
m07t1_gatekeeper        50          48          22          12
m08t1_skeleton_captain  100         65          15          30

m09t2_black_slime       130         60          3           8
m10t2_giant_bat         60          100         8           12
m11t2_priest_master     100         95          30          18
m12t22_zombie           260         85          5           22
m13t2_stone_guardian    20          100         68          28
m14t2_zombie_warrior    320         120         15          30
m15t2_vampire           444         199         66          144

m16t3_slime_man         320         140         20          30
m17t3_skeleton_elite    220         180         30          35
m18t3_knight            210         200         65          45
m19t3_gatekeeper_elite  100         180         110         50
m20t3_swordsman         100         680         50          55
m21t3_knight_elite      160         230         105         65
m22t3_knight_captain    120         150         50          100

m23t4_slimelord         360         310         20          40
m24t4_vampire_bat       200         390         90          50
m25t4_mage              220         370         110         80
m26t4_mage_master       200         380         130         90
m27t4_demo_sergent      230         450         100         100
m28t4_dark_knight       180         430         210         120
m29t4_gate_guardian     180         460         360         200
m30t4_zeno_fake         800         500         100         500
m30t4_zeno_real         1000        625         125         1000

m31b1_squid             1200        180         20          100
m32b2_dragon            1500        600         250         800
m33b3_archmage          4500        560         310         1000

----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------

 0 - wall    1 - floor   2 - star    3 - upstairs    4 - downstairs
 5 - door_y  6 - door_b  7 - door_r  8 - door_m      9 - prison
10 - logo
----------------------------------------------------------------------------------
11 - iron_sword     12 - iron_shield    13 - silver_sword   14 - silver_shield
15 - knight_sword   16 - knight_shield  17 - divine_sword   17 - divine_shield
19 - sacred_sword   20 - sacred_shield
----------------------------------------------------------------------------------
21 - thief      22 - saint      23 - merchant   24 - fairy      25 - shop_left
26 - shop_mid   27 - shop_right 28 - princess   29 - lava       
----------------------------------------------------------------------------------
31 - slime_g    32 - slime_r    33 - bat_fier   34 - priest     35 - skeleton
36 - skelet_w   37 - gatekeep   38 - skelet_c   39 - slime_b    40 - bat_giant
41 - priest_m   42 - zombie     43 - stone_gd   44 - zombie_w   45 - vampire
46 - slime_man  47 - skelet_e   48 - knight     49 - gatekp_e   50 - swordsman
51 - knight_e   52 - knight_c   53 - slimelord  54 - bat_vamp   55 - mage
56 - mage_mast  57 - demo_sgt   58 - dk_knight  59 - gate_gudn  60 - fake_zeno
61 - squid_cent 62 - squid_side 63 - dragon     64 - archmage   65 - real_zeno
----------------------------------------------------------------------------------
71 - key_y      72 - key_b      73 - key_r      74 - potion_r   75 - potion_b
76 - crystal_r  77 - crystal_b  78 - stf_wsdm   79 - stf_echo   80 - stf_space
81 - cross      82 - elixir     83 - m_mattock  84 - wing_cent  85 - e_mattock
86 - bomb       87 - wing_up    88 - key_enhac  89 - wing_down  90 - lucky_gold
91- dragonsbane 92 - snow_cryst

----------------------------------------------------------------------------------
----------------------------------------------------------------------------------
----------------------------------------------------------------------------------

                        case 3:    // 
                            b = 3;
                            Sprite sp_ = new Sprite(GameView.this, , origin+j*sq_size,origin+i*sq_size, b);
                            all_sprites.add(sp_);
                            break;
