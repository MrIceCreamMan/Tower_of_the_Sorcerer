package com.the95.tower_of_sorcerer;

/**
 * Created by Jack Sparrow on 2/12/2018.
 */

import android.app.Activity;


public class Mazegen extends Activity {

    public static Acceleromaze getMaze(int mazeNo) {
        Acceleromaze maze = null;
        if(mazeNo == 1) {
            maze = new Acceleromaze();
            boolean[][] border = new boolean[][]{
                    {true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true },//0
                    {true ,false,true ,false,false,false,false,false,true ,false,false,false,false,false,false,false,false,true },//1
                    {true ,false,true ,false,false,false,false,false,true ,false,false,false,false,false,false,false,false,true },//2
                    {true ,false,true ,false,false,false,false,false,true ,false,false,false,false,false,false,false,false,true },//3
                    {true ,false,true ,false,false,false,false,false,true ,false,false,false,true ,false,false,false,false,true },//4
                    {true ,false,false,false,false,true ,false,false,true ,false,false,false,true ,false,false,false,false,true },//5
                    {true ,false,false,false,false,true ,false,false,true ,false,false,false,true ,false,false,false,false,true },//6
                    {true ,true ,true ,true ,true ,true ,false,false,true ,false,false,false,true ,false,false,false,false,true },//7
                    {true ,false,false,false,false,false,false,false,true ,false,false,false,true ,true ,true ,true ,true ,true },//8
                    {true ,false,false,false,false,false,false,false,true ,false,false,false,false,false,false,false,false,true },//9
                    {true ,false,false,false,false,false,false,false,true ,false,false,false,false,false,false,false,false,true },//10
                    {true ,false,false,false,false,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,false,false,true },//11
                    {true ,false,false,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,true },//12
                    {true ,false,false,false,false,true ,false,false,false,false,false,false,true ,false,false,false,false,true },//13
                    {true ,true ,true ,true ,false,true ,false,false,false,false,false,false,true ,false,false,false,false,true },//14
                    {true ,false,false,false,false,true ,false,false,false,false,false,false,true ,false,false,false,false,true },//15
                    {true ,false,false,false,false,true ,false,false,false,false,false,false,true ,false,false,false,false,true },//16
                    {true ,false,false,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,true },//17
                    {true ,false,true ,true ,true ,true ,false,false,true ,true ,true ,true ,true ,true ,true ,true ,true ,true },//18
                    {true ,false,false,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,true },//19
                    {true ,false,false,false,false,true ,false,false,false,false,false,false,true ,false,false,false,false,true },//20
                    {true ,false,false,false,false,true ,true ,true ,true ,true ,true ,true ,true ,false,false,false,false,true },//21
                    {true ,true ,true ,true ,false,true ,false,false,false,false,false,false,true ,false,false,false,false,true },//22
                    {true ,false,false,false,false,true ,false,false,false,false,false,false,true ,false,false,false,false,true },//23
                    {true ,false,false,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,true },//24
                    {true ,false,false,false,false,true ,false,false,true ,true ,true ,true ,true ,true ,true ,true ,true ,true },//25
                    {true ,false,true ,true ,true ,true ,false,false,false,false,false,false,false,false,false,false,false,true },//26
                    {true ,false,false,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,true },//27
                    {true ,false,false,false,false,true ,true ,true ,true ,true ,true ,true ,false ,false,false,false,false,true },//28
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//29
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//30
                    {true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true },//31
            };       //0   //1   //2   //3   //4   //5   //6   //7   //8   //9   //10  //11  //12  //13  //14  //15  //16  //17
            boolean[][] wholes = new boolean[][]{
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//0
                    {false,false,false,false,false,false,false,false,false,true ,false,false,false,false,false,true ,false,false},//1
                    {false,false,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,false,false},//2
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//3
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//4
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//5
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,true ,false,false,false,false},//6
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//7
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//8
                    {false,false,false,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,false},//9
                    {false,false,false,false,false,false,false,false,false,true ,false,false,false,false,false,false,false,false},//10
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//11
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//12
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,true ,false,false,false,false},//13
                    {false,false,false,false,false,false,false,false,false,false,true ,false,false,false,false,false,false,false},//14
                    {false,false,true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//15
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true ,false},//16
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//17
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//18
                    {false,false,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,false,false},//19
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//20
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//21
                    {false,false,false,false,false,false,false,true ,false,false,false,false,false,false,false,false,false,false},//22
                    {false,true ,false,false,false,false,false,false,false,false,false,false,false,false,false,true ,false,false},//23
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//24
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//25
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//26
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//27
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//28
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true ,false,false},//29
                    {false,true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//30
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false}//31
            };        //0   //1   //2   //3   //4   //5   //6   //7   //8   //9   //10  //11  //12  //13  //14  //15  //16  //17

            int[][] obstacles = new int[][]{
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//0
                    {0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0},//1
                    {0,0,0,0,1,0,3,0,0,0,0,0,3,0,0,0,0,0},//2
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,0},//3
                    {0,0,0,0,0,0,0,0,0,0,4,0,0,0,0,4,0,0},//4
                    {0,0,2,0,0,0,0,0,0,0,5,0,0,0,0,0,0,0},//5
                    {0,0,2,0,0,0,4,5,0,0,4,0,0,1,0,0,0,0},//6
                    {0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,0},//7
                    {0,0,0,0,0,0,0,0,0,0,4,0,0,0,0,0,0,0},//8
                    {0,0,4,0,0,1,0,0,0,0,0,0,0,0,5,0,0,0},//9
                    {0,0,4,0,0,0,0,0,0,1,0,0,0,0,4,0,0,0},//10
                    {0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//11
                    {0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//12
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0},//13
                    {0,0,0,0,0,0,0,0,4,0,1,0,0,0,0,5,0,0},//14
                    {0,0,1,0,0,0,0,0,4,0,0,0,0,0,4,0,0,0},//15
                    {0,0,0,0,0,0,0,0,4,0,0,0,0,0,0,0,1,0},//16
                    {0,0,0,0,0,0,3,0,4,0,0,0,0,0,0,0,0,0},//17
                    {0,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//18
                    {0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0},//19
                    {0,0,3,0,0,0,0,0,0,0,0,0,0,0,6,0,0,0},//20
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,0},//21
                    {0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},//22
                    {0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0},//23
                    {0,0,0,0,0,0,0,0,0,6,5,0,0,0,0,0,0,0},//24
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//25
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//26
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//27
                    {0,0,0,5,0,0,0,0,0,0,0,0,0,3,0,0,0,0},//28
                    {0,0,0,5,0,0,0,0,0,0,0,0,0,0,0,1,0,0},//29
                    {0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//30
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}//31
            };        //0   //1   //2   //3   //4   //5   //6   //7   //8   //9   //10  //11  //12  //13  //14  //15  //16  //17
            maze.setBorders(border);
            maze.setObstacles(obstacles);
            maze.setStartPosition(1, 2);
            //maze.setFinalPosition(3, 3);
            maze.setFinalPosition(15,6);
        }

        else if (mazeNo == 2) {
            maze = new Acceleromaze();
            boolean[][] border = new boolean[][]{
                    {true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true },//0
                    {true ,false,false,false,false,false,false,false,true ,false,false,false,false,false,false,false,false,true },//1
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//2
                    {true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,false,false,false,false,false,false,true },//3
                    {true ,false,false,false,false,false,false,true ,false,false,false,false,false,false,false,false,false,true },//4
                    {true ,false,false,false,false,false,false,true ,false,false,false,false,false,false,false,false,false,true },//5
                    {true ,false,false,false,true ,false,false,true ,false,false,false,true ,true ,true ,true ,true ,true ,true },//6
                    {true ,false,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,false,true },//7
                    {true ,false,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,false,true },//8
                    {true ,false,false,false,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,false,false,false,true },//9
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//10
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//11
                    {true ,true ,true ,true ,true ,true ,true ,false,false,false,false,true ,true ,true ,true ,true ,true ,true },//12
                    {true ,false,false,false,true ,false,false,false,false,false,false,false,false,true ,false,false,false,true },//13
                    {true ,false,false,false,true ,false,false,false,false,false,false,false,false,true ,false,false,false,true },//14
                    {true ,false,false,false,true ,false,false,true ,true ,true ,true ,false,false,true ,false,false,false,true },//15
                    {true ,false,false,false,false,false,false,true ,false,false,true ,false,false,false,false,false,false,true },//16
                    {true ,false,false,false,false,false,false,true ,false,false,true ,false,false,false,false,false,false,true },//17
                    {true ,false,false,false,true ,true ,true ,true ,false,false,true ,true ,true ,true ,false,false,false,true },//18
                    {true ,false,false,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,true },//19
                    {true ,false,false,false,false,true ,false,false,false,false,false,false,true ,false,false,false,false,true },//20
                    {true ,false,false,false,false,true ,true ,true ,true ,true ,true ,true ,true ,false,false,false,false,true },//21
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//22
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//23
                    {true ,false,false,false,true ,true ,true ,true ,false,false,true ,true ,true ,true ,false,false,false,true },//24
                    {true ,false,false,false,false,false,false,true ,false,false,true ,false,false,false,false,false,false,true },//25
                    {true ,false,false,false,false,false,false,true ,false,false,true ,false,false,false,false,false,false,true },//26
                    {true ,false,false,false,false,false,false,true ,false,false,true ,false,false,false,false,false,false,true },//27
                    {true ,false,false,false,false,false,false,true ,false,false,true ,false,false,false,false,false,false,true },//28
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//29
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//30
                    {true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true },//31
                    //0   //1   //2   //3   //4   //5   //6   //7   //8   //9   //10  //11  //12  //13  //14  //15  //16  //17
            };

            boolean[][] wholes = new boolean[][]{
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//0
                    {false,false,false,false,false,false,false,false,false,false,false,true ,false,false,false,false,false,false},//1
                    {false,false,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,false,false},//2
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//3
                    {false,false,false,false,false,false,true ,false,false,false,false,false,false,false,false,false,false,false},//4
                    {false,true ,false,false,false,false,false,false,false,false,false,true ,false,false,false,false,false,false},//5
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//6
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//7
                    {false,false,false,false,false,true ,false,false,false,false,false,false,false,false,false,false,true ,false},//8
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//9
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//10
                    {false,false,false,true ,false,false,false,false,false,false,false,false,false,false,true ,false,false,false},//11
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//12
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//13
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//14
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//15
                    {false,false,false,false,false,false,false,false,true ,false,false,false,false,false,false,false,false,false},//16
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//17
                    {false,true ,false,true ,false,false,false,false,false,false,false,false,false,false,true ,false,true ,false},//18
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//19
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//20
                    {false,false,false,false,false,false,false,false,false,true ,false,false,false,false,false,false,false,false},//21
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//22
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//23
                    {false,false,false,false,false,false,false,false,true ,false,false,false,false,false,false,false,false,false},//24
                    {false,false,false,true ,false,false,true ,false,false,false,false,false,false,true ,false,false,true ,false},//25
                    {false,false,false,false,false,false,false,false,false,true ,false,false,false,false,false,false,false,false},//26
                    {false,true ,false,false,true ,false,false,false,false,false,false,false,true ,false,false,true ,false,false},//27
                    {false,false,false,false,false,false,false,false,true ,false,false,false,false,false,false,false,false,false},//28
                    {false,false,true ,false,false,true ,false,false,false,false,false,true ,false,false,true ,false,false,false},//29
                    {false,true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//30
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//31
            };        //0   //1   //2   //3   //4   //5   //6   //7   //8   //9   //10  //11  //12  //13  //14  //15  //16  //17
            int[][] obstacles = new int[][]{
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//0
                    {0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0},//1
                    {0,0,0,0,1,0,0,0,2,0,0,0,0,4,5,4,0,0},//2
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,5,3,5,0,0},//3
                    {0,0,0,0,4,0,1,0,0,0,0,0,0,4,5,4,0,0},//4
                    {0,1,0,0,4,0,0,0,0,4,0,1,0,0,0,0,0,0},//5
                    {0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,0,0},//6
                    {0,0,0,0,0,0,0,0,0,4,0,0,0,5,0,0,0,0},//7
                    {0,0,3,0,0,1,0,0,0,0,0,0,0,5,0,0,1,0},//8
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0},//9
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//10
                    {0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0},//11
                    {0,0,0,0,0,0,0,2,4,4,2,0,0,0,0,0,0,0},//12
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//13
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//14
                    {0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0},//15
                    {0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0},//16
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//17
                    {0,1,0,4,0,0,0,0,6,6,0,0,0,0,4,0,1,0},//18
                    {0,0,0,0,0,0,0,0,5,5,0,0,3,0,0,0,0,0},//19
                    {0,0,5,0,0,0,1,0,6,5,0,0,0,0,0,5,0,0},//20
                    {0,0,0,5,0,0,0,0,0,0,0,0,0,0,5,0,0,0},//21
                    {0,0,5,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0},//22
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//23
                    {0,0,3,0,0,0,0,0,1,0,0,0,0,0,0,3,0,0},//24
                    {0,0,0,1,0,0,1,0,0,0,0,0,0,1,0,0,1,0},//25
                    {0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0},//26
                    {0,1,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,0},//27
                    {0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0},//28
                    {0,0,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0,0},//29
                    {0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//30
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            };
            maze.setBorders(border);
            maze.setObstacles(obstacles);
            maze.setStartPosition(1, 1);
            maze.setFinalPosition(8, 30);
        }
        else if (mazeNo == 3) {
            maze = new Acceleromaze();
            boolean[][] border = new boolean[][]{
                    {true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true },//0
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//1
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//2
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//3
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//4
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//5
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//6
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//7
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//8
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//9
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//10
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//11
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//12
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//13
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//14
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//15
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//16
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//17
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//18
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//19
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//20
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//21
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//22
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//23
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//24
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//25
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//26
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//27
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//28
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//29
                    {true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true },//30
                    {true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true },//31
                    //0   //1   //2   //3   //4   //5   //6   //7   //8   //9   //10  //11  //12  //13  //14  //15  //16  //17
            };

            boolean[][] wholes = new boolean[][]{
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//0
                    {false,false,false,false,false,false,false,false,false,false,false,false,true ,false,false,false,false,false},//1
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,true ,false,false,false,false},//2
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,true ,false,false,false},//3
                    {false,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,false,false,false,false,true ,false,false},//4
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true ,false},//5
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//6
                    {false,false,false,false,false,false,false,false,true ,true ,true ,true ,true ,true ,true ,true ,true ,false},//7
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//8
                    {false,true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//9
                    {false,false,true ,false,false,false,false,true ,true ,true ,true ,true ,true ,false,false,false,false,false},//10
                    {false,false,false,true ,false,false,false,true ,false,false,false,false,false,false,false,false,true ,false},//11
                    {false,false,false,true ,false,false,false,true ,false,false,false,false,false,false,false,true ,false,false},//12
                    {false,false,false,true ,true ,false,true ,true ,false,false,false,false,false,false,true ,false,false,false},//13
                    {false,false,false,false,false,false,false,false,false,false,false,true ,true ,true ,true ,false,false,false},//14
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//15
                    {false,false,false,true ,true ,true ,true ,true ,false,false,false,false,false,false,false,false,false,false},//16
                    {false,false,false,true ,false,false,false,false,true ,false,false,false,false,false,false,false,false,false},//17
                    {false,false,false,true ,false,false,false,false,false,true ,false,false,false,false,false,false,false,false},//18
                    {false,false,false,true ,false,false,false,false,false,false,true ,false,false,false,false,false,false,false},//19
                    {false,false,false,false,true ,false,false,false,true ,false,false,false,true ,false,false,false,true ,false},//20
                    {false,false,false,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,false},//21
                    {false,false,false,false,true ,false,true ,false,false,false,true ,false,false,false,true ,false,false,false},//22
                    {false,true ,false,true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//23
                    {false,true ,false,true ,true ,false,false,false,true ,false,false,false,true ,false,false,false,true ,false},//24
                    {false,true ,false,true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//25
                    {false,true ,false,true ,false,false,true ,false,false,false,true ,false,false,false,true ,false,false,false},//26
                    {false,true ,false,true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//27
                    {false,true ,false,true ,true ,false,false,false,true ,false,false,false,true ,false,false,false,true ,false},//28
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//29
                    {false,false,true ,false,false,false,false,false,false,false,true ,false,false,false,false,false,false,false},//30
                    {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},//31
            };        //0   //1   //2   //3   //4   //5   //6   //7   //8   //9   //10  //11  //12  //13  //14  //15  //16  //17
            int[][] obstacles = new int[][]{
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//0
                    {0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0},//1
                    {0,0,0,0,0,0,3,0,0,0,4,0,0,1,0,0,0,0},//2
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0},//3
                    {0,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,0,0},//4
                    {0,0,0,0,0,0,0,0,0,5,0,0,0,3,0,0,1,0},//5
                    {0,0,0,5,4,5,0,0,0,5,0,0,0,0,0,0,0,0},//6
                    {0,0,0,4,3,4,0,0,1,1,1,1,1,1,1,1,1,0},//7
                    {0,0,0,5,4,5,0,0,0,6,0,0,0,0,0,0,0,0},//8
                    {0,1,0,0,0,0,0,0,0,6,0,0,0,0,3,0,0,0},//9
                    {0,0,1,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0},//10
                    {0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,1,0},//11
                    {0,0,0,1,0,0,0,1,0,5,0,0,0,0,0,1,0,0},//12
                    {0,0,0,1,1,4,1,1,0,4,0,0,0,0,1,0,0,0},//13
                    {0,0,0,0,0,0,0,0,0,5,0,1,1,1,1,0,0,0},//14
                    {0,0,0,0,0,0,0,0,0,4,0,0,0,0,0,0,0,0},//15
                    {0,4,4,1,1,1,1,1,0,0,0,0,0,4,5,4,0,0},//16
                    {0,0,0,1,0,0,0,0,1,0,0,0,0,5,3,5,0,0},//17
                    {0,5,5,1,0,0,0,0,0,1,0,0,0,4,5,4,0,0},//18
                    {0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0},//19
                    {0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0},//20
                    {0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0},//21
                    {0,0,0,0,1,0,1,0,5,0,1,0,5,0,1,0,0,0},//22
                    {0,1,6,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//23
                    {0,1,0,1,1,0,0,0,1,0,5,0,1,0,5,0,1,0},//24
                    {0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//25
                    {0,1,0,1,0,0,1,0,5,0,1,0,6,0,1,0,0,0},//26
                    {0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//27
                    {0,1,0,1,1,0,5,0,1,0,3,0,1,0,5,0,1,0},//28
                    {0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//29
                    {0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},//30
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},

            };
            maze.setBorders(border);
            maze.setObstacles(obstacles);
            maze.setStartPosition(2, 2);
            maze.setFinalPosition(14, 29);

        }

        return maze;
    }
}
