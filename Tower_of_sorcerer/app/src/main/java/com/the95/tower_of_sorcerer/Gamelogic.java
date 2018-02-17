package com.the95.tower_of_sorcerer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import static android.graphics.Bitmap.createScaledBitmap;

public class Gamelogic extends Activity implements View.OnTouchListener {

    Floors gamefloor;
    Bitmap ball, kid;
    Bitmap sq_wall, sq_floor;
    float x, y;
    Sprite kid_sprite;
    private static final String TAG = "debuuuuuuuuuuuuuuuuuug";
    //Log.v(TAG, "x = " + me.getX() + " y = " + me.getY());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gamefloor = new Floors(this);
        gamefloor.setOnTouchListener(this);
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.brokeearth);
        kid = BitmapFactory.decodeResource(getResources(), R.drawable.poke);
        sq_floor = BitmapFactory.decodeResource(getResources(), R.drawable.square_floor);
        sq_wall = BitmapFactory.decodeResource(getResources(), R.drawable.square_wall1);
        //Log.v(TAG, "width = " + sq_wall.getWidth() + " y = " + sq_wall.getHeight());
        x = y = 0;
        setContentView(gamefloor);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gamefloor.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gamefloor.resume();
    }

    public class Floors extends SurfaceView implements Runnable {

        Thread t = null;
        SurfaceHolder holder;
        boolean isItOK = false;

        public Floors(Context context) {
            super(context);
            holder = getHolder();
            //Log.v(TAG, "tell me the width = " + this.getWidth());
        }

        public void run() {

            kid_sprite = new Sprite(Floors.this, kid);
            int scale_w = 0;
            //Log.v(TAG, "tell me something");

            while (isItOK) {
                if (!holder.getSurface().isValid()) {
                    continue;
                }
                //Log.v(TAG, "hello there");
                Canvas c = holder.lockCanvas();
                if (scale_w != c.getWidth() / 12) {
                    scale_w = c.getWidth() / 12;
                    sq_floor = createScaledBitmap(sq_floor, scale_w, scale_w, false);
                    sq_wall = createScaledBitmap(sq_wall, scale_w, scale_w, false);
                }
                this.floorsDraw(c);
                holder.unlockCanvasAndPost(c);
            }

        }

        protected void floorsDraw(Canvas canvas) {
            //canvas.drawARGB(255, 255, 128, 128);
            int origin = 0 - sq_wall.getWidth()/2;
            for (int a = 0; a < 11; a++){  // -------------------- Draw Floor ----------------------
                for (int b = 0; b < 11; b++){
                    canvas.drawBitmap(sq_floor, origin + sq_floor.getWidth() * (a+1), origin + sq_floor.getWidth() * (b+1), null);
                }
            }
            for (int i = 0; i < 13; i++) {  // ------------------- Draw Wall -----------------------
                canvas.drawBitmap(sq_wall, origin + i * sq_wall.getWidth(), origin, null);
                canvas.drawBitmap(sq_wall, origin + i * sq_wall.getWidth(), origin + 12 * sq_wall.getWidth(), null);
                canvas.drawBitmap(sq_wall, origin, origin + i * sq_wall.getWidth(), null);
                canvas.drawBitmap(sq_wall, origin + 12 * sq_wall.getWidth(),origin + i * sq_wall.getWidth(), null);
            }
            canvas.drawBitmap(ball, x - ball.getWidth()/2, y - ball.getHeight()/2, null);
            kid_sprite.onDraw(canvas);
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



















