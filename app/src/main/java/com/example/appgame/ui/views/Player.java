package com.example.appgame.ui.views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Matrix;
import android.util.Log;

public class Player {
    private Bitmap playerTexture;
    private Bitmap swordTexture;
    private Paint paint;
    private Matrix flipMatrix;
    private Matrix rotationMatrix;
    private boolean isAttacking;
    private int speed = 60;

    public Player(Bitmap playerTexture, Bitmap swordTexture, float positionX, float positionY) {
        this.playerTexture = playerTexture;
        this.swordTexture = swordTexture;
        this.paint = new Paint();
        this.flipMatrix = new Matrix();
        this.rotationMatrix = new Matrix();
        this.isAttacking = false;
    }

    public void draw(Canvas canvas, float centerX, float centerY, boolean facingRight, boolean isAlive) {
        // Отрисовка игрока в центре экрана
        if (!facingRight) {
            flipMatrix.setScale(-1, 1, playerTexture.getWidth() / 2, playerTexture.getHeight() / 2);
            canvas.drawBitmap(Bitmap.createBitmap(playerTexture, 0, 0, playerTexture.getWidth(), playerTexture.getHeight(), flipMatrix, true),
                    centerX - playerTexture.getWidth() / 2, centerY - playerTexture.getHeight() / 2, paint);
        } else {
            canvas.drawBitmap(playerTexture, centerX - playerTexture.getWidth() / 2, centerY - playerTexture.getHeight() / 2, paint);
        }
        int x = 95, y = 25;
        if (isAlive) {
            // Отрисовка меча
            if (isAttacking) {
                rotationMatrix.reset();
                rotationMatrix.postRotate(50, swordTexture.getWidth() / 2 - x, swordTexture.getHeight() / 2 - y);
                if (!facingRight) {
                    flipMatrix.setScale(-1, 1, swordTexture.getWidth() / 2 - x, swordTexture.getHeight() / 2 - y);
                    rotationMatrix.postConcat(flipMatrix);
                }
                Bitmap rotatedSword = Bitmap.createBitmap(swordTexture, 0, 0, swordTexture.getWidth(), swordTexture.getHeight(), rotationMatrix, true);
                if (!facingRight)
                    canvas.drawBitmap(rotatedSword, centerX - rotatedSword.getWidth() / 2 - x, centerY - rotatedSword.getHeight() / 2 - y, paint);
                else
                    canvas.drawBitmap(rotatedSword, centerX - rotatedSword.getWidth() / 2 + x, centerY - rotatedSword.getHeight() / 2 - y, paint);
            } else {
                if (!facingRight) {
                    flipMatrix.setScale(-1, 1, swordTexture.getWidth() / 2, swordTexture.getHeight() / 2);
                    canvas.drawBitmap(Bitmap.createBitmap(swordTexture, 0, 0, swordTexture.getWidth(), swordTexture.getHeight(), flipMatrix, true),
                            centerX - swordTexture.getWidth() / 2 - x, centerY - swordTexture.getHeight() / 2 - y, paint);
                } else {
                    canvas.drawBitmap(swordTexture, centerX - swordTexture.getWidth() / 2 + x, centerY - swordTexture.getHeight() / 2 - y, paint);
                }
            }
        }else{
            rotationMatrix.reset();
            rotationMatrix.postRotate(180, swordTexture.getWidth() / 2 - x, swordTexture.getHeight() / 2 - y);
            flipMatrix.setScale(-1, 1, swordTexture.getWidth() / 2 - x, swordTexture.getHeight() / 2 - y);
            rotationMatrix.postConcat(flipMatrix);
            Bitmap rotatedSword = Bitmap.createBitmap(swordTexture, 0, 0, swordTexture.getWidth(), swordTexture.getHeight(), rotationMatrix, true);
            canvas.drawBitmap(rotatedSword, centerX - rotatedSword.getWidth() / 2, centerY - rotatedSword.getHeight() / 2, paint);

        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    public void setPlayerTexture(Bitmap playerTexture1) {
        this.playerTexture = playerTexture1;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
