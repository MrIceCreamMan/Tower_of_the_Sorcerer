package com.the95.tower_of_sorcerer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Sprite {

    private int x, y;
    private byte id;
    private int xSpeed, ySpeed;
    private int height, width;
    private Bitmap sprite_sheet;
    private Gamelogic.GameView ov;
    private int currentFrame = 0;
    private int direction = 2;
    private Rect src, dst;
    private int timer = 0;

    private static final String TAG = "debuuuuuuuuuuuuuuuuuug";

    public Sprite(Gamelogic.GameView ourView, Bitmap in_sheet) {
        sprite_sheet = in_sheet;
        ov = ourView;
        height = sprite_sheet.getHeight() / 4;
        width = sprite_sheet.getWidth() / 4;
        x = y = 0;
        id = 127;
        xSpeed = 20;
        ySpeed = 0;
        src = new Rect(0,0,0,0);
        dst = new Rect(0,0,0,0);
    }
    public Sprite(Gamelogic.GameView ourView, Bitmap in_sheet, int in_xloc, int in_yloc, byte in_id) {
        sprite_sheet = in_sheet;
        x = in_xloc;
        y = in_yloc;
        id = in_id;

        ov = ourView;
        height = sprite_sheet.getHeight();
        if (id == 25 || id == 27)
            width = sprite_sheet.getWidth();
        else if (id == 29)
            width = sprite_sheet.getWidth() / 4;
        else if (id > 20 && id < 66)
            width = sprite_sheet.getWidth() / 3;
        else {
            width = sprite_sheet.getWidth();
        }
        currentFrame = 1;
        xSpeed = 0;
        ySpeed = 0;
        src = new Rect(0,0,0,0);
        dst = new Rect(0,0,0,0);
        if (id == 30){
            height = sprite_sheet.getHeight() / 4;
            width = sprite_sheet.getWidth() / 3;
            direction = 3;
        }
    }

    public void display(Canvas canvas){

        src.set(0, 0, width, height);
        dst.set(x, y, x + width, y + height);
        canvas.drawBitmap(sprite_sheet, src, dst, null);
    }

    public void blink_x3(Canvas canvas) {

        timer = ++timer % 4;
        if (timer == 1) {
            currentFrame = ++currentFrame % 3;
        }

        int srcX = currentFrame * width;
        src.set(srcX, 0, srcX + width, height);
        dst.set(x, y, x + width, y + height);
        canvas.drawBitmap(sprite_sheet, src, dst, null);
    }
    public void blink_x4(Canvas canvas) {

        timer = ++timer % 4;
        if (timer == 1) {
            currentFrame = ++currentFrame % 4;
        }

        int srcX = currentFrame * width;
        src.set(srcX, 0, srcX + width, height);
        dst.set(x, y, x + width, y + height);
        canvas.drawBitmap(sprite_sheet, src, dst, null);
    }

    public void loopRun(Canvas canvas) {
        // 0 = up
        // 1 = down
        // 2 = left
        // 3 = right

        // facing down
        if (x > ov.getWidth() - width - xSpeed) {
            xSpeed = 0;
            ySpeed = 20;
            direction = 0;
        }
        //going left
        if (y > ov.getHeight() - height - ySpeed) {
            xSpeed = -20;
            ySpeed = 0;
            direction = 1;
        }
        // facing up
        if (x + xSpeed < 0) {
            x = 0;
            xSpeed = 0;
            ySpeed = -20;
            direction = 3;
        }
        //facing right
        if (y + ySpeed < 0) {
            y = 0;
            xSpeed = 20;
            ySpeed = 0;
            direction = 2;
        }

        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        currentFrame = ++currentFrame % 4;
        x += xSpeed;
        y += ySpeed;

        int srcX = currentFrame * width;
        int srcY = direction * height;
        src.set(srcX, srcY, srcX + width, srcY + height);
        dst.set(x, y, x + width, y + height);
        canvas.drawBitmap(sprite_sheet, src, dst, null);
    }


    public void set_location(int in_x, int in_y) {
        x = in_x;
        y = in_y;
    }
    public void set_direction(int in_d) {
        direction = in_d;
    }
    public void walk(Canvas canvas) {

        if (direction == 0) {
            // going down
            xSpeed = 0;
            ySpeed = height / 3;
        } else if (direction == 1) {
            // going left
            xSpeed = -width / 3;
            ySpeed = 0;
        } else if (direction == 2) {
            // going right
            xSpeed = width / 3;
            ySpeed = 0;
        } else {
            // going up
            xSpeed = 0;
            ySpeed = -height / 3;
        }

        currentFrame = ++currentFrame % 3;
        x += xSpeed;
        y += ySpeed;

        int srcX = currentFrame * width;
        int srcY = direction * height;
        src.set(srcX, srcY, srcX + width, srcY + height);
        dst.set(x, y, x + width, y + height);
        canvas.drawBitmap(sprite_sheet, src, dst, null);
    }
    public void stand(Canvas canvas) {
        int srcY = direction * height;
        src.set(width, srcY, 2 * width, srcY + height);
        dst.set(x, y, x + width, y + height);
        canvas.drawBitmap(sprite_sheet, src, dst, null);
    }

    public void update(Canvas canvas){
/*
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/
        if (id == 25 || id == 27)
            display(canvas);
        else if (id == 29)
            blink_x4(canvas);
        else if (id > 20 && id < 66)
            blink_x3(canvas);
        else
            display(canvas);
    }

}
