package com.the95.tower_of_sorcerer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

//attempt change
public class Gameboard extends View implements SensorEventListener {

    private SensorManager mSensorManager;

    private int width, height;

    private int mazeSizeX, mazeSizeY;

    float cellWidth, cellHeight;

    float totalCellWidth, totalCellHeight;

    int orientation;
    public final int UP = 0, DOWN = 1, RIGHT = 2, LEFT = 3, UPRIGHT=4, DOWNRIGHT=5, UPLEFT=6, DOWNLEFT=7;

    private int mazeFinishX, mazeFinishY, mazeStartX, mazeStartY;
    private Acceleromaze maze;
    private Activity context;
    private Paint side, ball, pit, background, starz, gravityFlip;


    Rect destinationBound, startBound, imageBound;
    Bitmap destination = BitmapFactory.decodeResource(getResources(), R.drawable.newearth);
    Bitmap starting = BitmapFactory.decodeResource(getResources(), R.drawable.brokeearth);
    Bitmap ssImageup = BitmapFactory.decodeResource(getResources(), R.drawable.ssimageup);
    Bitmap ssImagedown = BitmapFactory.decodeResource(getResources(), R.drawable.ssimagedown);
    Bitmap ssImageright = BitmapFactory.decodeResource(getResources(), R.drawable.ssimageright);
    Bitmap ssImageleft = BitmapFactory.decodeResource(getResources(), R.drawable.ssimageleft);
    Bitmap ssImagedownright = BitmapFactory.decodeResource(getResources(), R.drawable.ssimagedownright);
    Bitmap ssImageupright = BitmapFactory.decodeResource(getResources(), R.drawable.ssimageupright);
    Bitmap ssImagedownleft = BitmapFactory.decodeResource(getResources(), R.drawable.ssimagedownleft);
    Bitmap ssImageupleft = BitmapFactory.decodeResource(getResources(), R.drawable.ssimageupleft);


    double gravitySensitivityValue = 2;

    public Gameboard(Context context, Acceleromaze maze) {
        super(context);
        this.maze = maze;
        this.context = (Activity)context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mazeFinishX = maze.getFinalX();
        mazeFinishY = maze.getFinalY();
        mazeStartX = maze.getStartX();
        mazeStartY = maze.getStartY();
        mazeSizeX = maze.getMazeWidth();
        mazeSizeY = maze.getMazeHeight();
        side = new Paint();
        side.setColor(getResources().getColor(R.color.bord));
        ball = new Paint();
        ball.setColor(getResources().getColor(R.color.player));
        pit = new Paint();
        pit.setColor(getResources().getColor(R.color.ahh));
        Paint finish = new Paint();
        finish.setColor(getResources().getColor(R.color.end));
        background = new Paint();
        background.setColor(getResources().getColor(R.color.back));
        starz = new Paint();

        starz.setTextSize(60);
        gravityFlip = new Paint();
        gravityFlip.setColor(getResources().getColor(R.color.gravswitch));
        gravityFlip.setTextSize(60);
        setFocusable(true);
        this.setFocusableInTouchMode(true);
    }
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;         //for now square mazes
        int boxWidth = 20;          //for now 1 pixel wide walls
        cellWidth = (width - ((float)mazeSizeX*boxWidth)) / mazeSizeX;
        totalCellWidth = cellWidth+boxWidth;
        cellHeight = (height - ((float)mazeSizeY*boxWidth)) / mazeSizeY;
        totalCellHeight = cellHeight+boxWidth;
        ball.setTextSize(cellHeight * 0.75f);
        super.onSizeChanged(w, h, oldw, oldh);
    }
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, width, height, background);

        boolean[][] walls = maze.getBorders();
        //iterate over the boolean arrays to draw walls
        for (int i = 0; i < mazeSizeY; i++) {
            for (int j = 0; j < mazeSizeX; j++) {
                float x = j * totalCellWidth;
                float y = i * totalCellHeight;
                if (walls[i][j]) {
                    canvas.drawRect(x,   //start X
                            y,               //start Y
                            x + totalCellWidth,   //stop X
                            y + totalCellHeight,  //stop Y
                            side);
                }
            }
        }

        int[][] traps = maze.getObstacles();
        for (int i = 0; i < mazeSizeY; i++) {
            for (int j = 0; j < mazeSizeX; j++) {
                float x1 = j * totalCellWidth;
                float y1 = i * totalCellHeight;
                Log.d("log x,y values", " " + x1 + " " + y1);
                if (traps[i][j] == 3) {
                    canvas.drawText("X", x1 - 5 + (totalCellWidth / 3), y1 - 5 + (totalCellHeight), gravityFlip);
                }
                if (traps[i][j] == 2) {
                    starz.setColor(getResources().getColor(R.color.starcolor1));
                    canvas.drawText("*", x1 - 5 + (totalCellWidth / 3), y1 + (totalCellHeight), starz);
                }
                if (traps[i][j] == 4) {
                    starz.setColor(getResources().getColor(R.color.starcolor2));
                    canvas.drawText("*", x1 - 5 + (totalCellWidth / 3), y1 + (totalCellHeight), starz);
                }
                if (traps[i][j] == 5) {
                    starz.setColor(getResources().getColor(R.color.starcolor3));
                    canvas.drawText("*", x1 - 5 + (totalCellWidth / 3), y1 + (totalCellHeight), starz);
                }
                if (traps[i][j] == 6) {
                    starz.setColor(getResources().getColor(R.color.starcolor4));
                    canvas.drawText("*", x1 - 5 + (totalCellWidth / 3), y1 + (totalCellHeight), starz);
                }
                if (traps[i][j] == 1) {
                    canvas.drawCircle(x1 + (totalCellWidth / 2),
                            y1 + (totalCellWidth / 2),
                            (totalCellWidth / 2),
                            pit);
                }

            }
        }

        //draw the finishing point indicator


        //canvas.drawCircle((mazeFinishX * totalCellWidth) + (totalCellWidth / 2),
        //        (mazeFinishY * totalCellHeight) + (totalCellHeight / 2),
        //        (totalCellWidth / 2),
        //        finish);

        //draw the destination
        float left, top, right, bottom;
        left = ((mazeFinishX * totalCellWidth) + (totalCellWidth / 2)) - (totalCellWidth / 2);
        top = ((mazeFinishY * totalCellHeight) + (totalCellHeight / 2)) - (totalCellWidth / 2);
        right = ((mazeFinishX * totalCellWidth) + (totalCellWidth / 2)) + (totalCellWidth / 2);
        bottom = ((mazeFinishY * totalCellHeight) + (totalCellHeight / 2)) + (totalCellWidth / 2);
        destinationBound.set((int) left, (int) top, (int) right, (int) bottom);
        canvas.drawBitmap(destination, null, destinationBound, null);


        float sleft, stop, sright, sbottom;
        sleft = ((mazeStartX * totalCellWidth) + (totalCellWidth / 2)) - (totalCellWidth / 2);
        stop = ((mazeStartY * totalCellHeight) + (totalCellHeight / 2)) - (totalCellWidth / 2);
        sright = ((mazeStartX * totalCellWidth) + (totalCellWidth / 2)) + (totalCellWidth / 2);
        sbottom = ((mazeStartY * totalCellHeight) + (totalCellHeight / 2)) + (totalCellWidth / 2);
        startBound.set((int) sleft, (int) stop, (int) sright, (int) sbottom);
        canvas.drawBitmap(starting, null, startBound, null);

        int currentX = maze.getCurrentX(),currentY = maze.getCurrentY();
        //draw the ball
        //canvas.drawCircle(((currentX * totalCellWidth) + (totalCellWidth / 2)),   //x of center
        //        (currentY * totalCellHeight) + (totalCellHeight / 2),  //y of center
        //        (totalCellWidth / 2),                           //radius
        //        ball);

        //draw the spaceship itself.
        float dleft, dtop, dright, dbottom;
        dleft=((currentX * totalCellWidth) + (totalCellWidth / 2))-(totalCellWidth / 2);
        dtop=((currentY * totalCellHeight) + (totalCellHeight / 2))-(totalCellWidth / 2);
        dright=((currentX * totalCellWidth) + (totalCellWidth / 2))+(totalCellWidth / 2);
        dbottom=((currentY * totalCellHeight) + (totalCellHeight / 2))+(totalCellWidth / 2);
        imageBound.set( (int) dleft, (int) dtop, (int) dright, (int) dbottom);
        Bitmap ssImage;
        canvas.drawText("Score: "+String.valueOf(maze.getCoinPoints()), 50, 50, starz);
        if(orientation==UP) {
            ssImage = ssImageup;
        }
        else if(orientation==DOWN)
        {
            ssImage = ssImagedown;
        }
        else if(orientation==RIGHT)
        {
            ssImage = ssImageright;
        }
        else if(orientation==LEFT)
        {
            ssImage = ssImageleft;
        }
        else if(orientation==DOWNRIGHT)
        {
            ssImage = ssImagedownright;
        }
        else if(orientation==UPRIGHT)
        {
            ssImage = ssImageupright;
        }
        else if(orientation==DOWNLEFT)
        {
            ssImage = ssImagedownleft;
        }
        else
        {
            ssImage = ssImageupleft;
        }
        canvas.drawBitmap(ssImage, null, imageBound, null);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        /*try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
        if(event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;
        boolean moved = false;
        //maze.isFlipGravity()==false
        if(!maze.isFlipGravity()) {
            //UPRIGHT=4
            if ((event.values[0] < -gravitySensitivityValue) && (event.values[1] < -gravitySensitivityValue)) {
                moved = maze.move(Acceleromaze.UPRIGHT);
                orientation = UPRIGHT;
            }
            //DOWNRIGHT=5
            else if ((event.values[0] < -gravitySensitivityValue) && (event.values[1] > gravitySensitivityValue)) {
                moved = maze.move(Acceleromaze.DOWNRIGHT);
                orientation = DOWNRIGHT;
            }
            //UPLEFT=6
            else if ((event.values[0] > gravitySensitivityValue) && (event.values[1] < -gravitySensitivityValue)) {
                moved = maze.move(Acceleromaze.UPLEFT);
                orientation = UPLEFT;
            }
            //DOWNLEFT=7
            else if ((event.values[0] > gravitySensitivityValue) && (event.values[1] > gravitySensitivityValue)) {
                moved = maze.move(Acceleromaze.DOWNLEFT);
                orientation = DOWNLEFT;
            }
            else if (event.values[0] < -gravitySensitivityValue) {
                moved = maze.move(Acceleromaze.RIGHT);
                orientation = RIGHT;
            }
            else if (event.values[0] > gravitySensitivityValue) {
                moved = maze.move(Acceleromaze.LEFT);
                orientation = LEFT;
            }
            else if (event.values[1] < -gravitySensitivityValue) {
                moved = maze.move(Acceleromaze.UP);
                orientation = UP;
            }
            else if (event.values[1] > gravitySensitivityValue) {
                moved = maze.move(Acceleromaze.DOWN);
                orientation = DOWN;
            }
        }
        else if(maze.isFlipGravity())
        {
            //UPRIGHT=4
            if ((event.values[0] > gravitySensitivityValue) && (event.values[1] > gravitySensitivityValue)) {
                moved = maze.move(Acceleromaze.UPRIGHT);
                orientation = UPRIGHT;
            }
            //DOWNRIGHT=5
            else if ((event.values[0] > gravitySensitivityValue) && (event.values[1] < -gravitySensitivityValue)) {
                moved = maze.move(Acceleromaze.DOWNRIGHT);
                orientation = DOWNRIGHT;
            }
            //UPLEFT=6
            else if ((event.values[0] < -gravitySensitivityValue) && (event.values[1] > gravitySensitivityValue)) {
                moved = maze.move(Acceleromaze.UPLEFT);
                orientation = UPLEFT;
            }
            //DOWNLEFT=7
            else if ((event.values[0] < -gravitySensitivityValue) && (event.values[1] < -gravitySensitivityValue)) {
                moved = maze.move(Acceleromaze.DOWNLEFT);
                orientation = DOWNLEFT;
            }
            else if (event.values[0] > gravitySensitivityValue) {
                moved = maze.move(Acceleromaze.RIGHT);
                orientation = RIGHT;
            }
            else if (event.values[0] < -gravitySensitivityValue) {
                moved = maze.move(Acceleromaze.LEFT);
                orientation = LEFT;
            }
            else if (event.values[1] > gravitySensitivityValue) {
                moved = maze.move(Acceleromaze.UP);
                orientation = UP;
            }
            else if (event.values[1] < -gravitySensitivityValue) {
                moved = maze.move(Acceleromaze.DOWN);
                orientation = DOWN;
            }

        }
        if (moved) {
            invalidate();
            if (maze.isGameComplete()) {
                Log.d("This is score:", " "+maze.getCoinPoints());
                mSensorManager.unregisterListener(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getText(R.string.finished_title)+", Your score: "+maze.getCoinPoints());
                LayoutInflater inflater = context.getLayoutInflater();
                View view = inflater.inflate(R.layout.finish, null);
                builder.setView(view);
                View closeButton = view.findViewById(R.id.closeGame);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View clicked) {
                        if (clicked.getId() == R.id.closeGame) {
                            context.finish();
                        }
                    }
                });
                AlertDialog finishDialog = builder.create();
                finishDialog.show();
            }
            else if(maze.isALoser()) {
                mSensorManager.unregisterListener(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getText(R.string.loser_title));
                LayoutInflater inflater = context.getLayoutInflater();
                View view = inflater.inflate(R.layout.loser, null);
                builder.setView(view);
                View closeButton = view.findViewById(R.id.closeGame);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View clicked) {
                        if (clicked.getId() == R.id.closeGame) {
                            context.finish();
                        }
                    }
                });
                AlertDialog finishDialog = builder.create();
                finishDialog.show();
            }
        }

    }
    //attempt to save.
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

