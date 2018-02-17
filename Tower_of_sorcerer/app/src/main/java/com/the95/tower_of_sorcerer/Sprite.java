package com.the95.tower_of_sorcerer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {

    private int x, y;
    private int xSpeed, ySpeed;
    private int height, width;
    private Bitmap b;
    private Gamelogic.Floors ov;
    private int currentFrame = 0;
    private int direction = 2;
    private Rect src, dst;

    public Sprite(Gamelogic.Floors ourView, Bitmap blob) {
        b = blob;
        ov = ourView;
        height = b.getHeight() / 4;
        width = b.getWidth() / 4;
        x = y = 0;
        xSpeed = 20;
        ySpeed = 0;
        src = new Rect(0,0,0,0);
        dst = new Rect(0,0,0,0);
    }

    private void update() {

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
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        currentFrame = ++currentFrame % 4;
        x += xSpeed;
        y += ySpeed;
    }

    public void onDraw(Canvas canvas) {
        update();
        int srcX = currentFrame * width;
        int srcY = direction * height;
        src.set(srcX, srcY, srcX + width, srcY + height);
        dst.set(x, y, x + width, y + height);
        canvas.drawBitmap(b, src, dst, null);
    }
}
