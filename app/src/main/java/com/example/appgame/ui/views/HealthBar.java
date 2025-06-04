package com.example.appgame.ui.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.appgame.data.essence.PlayerModel;

public class HealthBar {
    private int maxHealth;
    private int currentHealth;
    private int width;
    private int height;
    private int x;
    private int y;
    private int potions;
    private StringBuilder heals;

    public HealthBar(int maxHealth, int width, int height, int x, int y, int potions) {
        this.heals = new StringBuilder();
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.potions = potions;
        for (int i = 0; i < potions; ++i) {
            heals.append("+");
        }
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public void draw(Canvas canvas) {
        // Отрисовка фона полоски жизней
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.GRAY);

        canvas.drawRect(x, y, x + width, y + height, backgroundPaint);

        // Определение текущей ширины полоски жизней на основе здоровья
        float healthPercentage = (float) currentHealth / maxHealth;
        int currentHealthWidth = (int) (width * healthPercentage);

        // Отрисовка полоски жизней
        Paint healthPaint = new Paint();
        if (currentHealth >= 40) {
            healthPaint.setColor(Color.GREEN);
        } else {
            healthPaint.setColor(Color.RED);
        }
        canvas.drawRect(x, y, x + currentHealthWidth, y + height, healthPaint);

        Paint healsPaint = new Paint();
        healsPaint.setColor(Color.GREEN);
        healsPaint.setTextSize(height - 20);
        healsPaint.setFakeBoldText(true);
        canvas.drawText(String.valueOf(heals), x + width + 40, y + height / 2 + 20, healsPaint);

        // Отрисовка текста здоровья
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(height - 20);
        textPaint.setFakeBoldText(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Health: " + currentHealth, x + width / 2, y + height / 2 + 20, textPaint);

    }

    public void heal(PlayerModel playerModel) {
        if (heals.length() > 0) {
            playerModel.heal(20);
            StringBuilder newHeals = new StringBuilder();
            for (int i = 0; i < heals.length() - 1; ++i)
                newHeals.append("+");
            heals = newHeals;
        }
    }

}
