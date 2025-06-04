package com.example.appgame.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.appgame.R;
import com.example.appgame.data.essence.EnemyModel;

public class MeleeWeapon {
    private int damage; // Урон, наносимый оружием
    private float range; // Дальность действия оружия
    private int callDawn;


    public MeleeWeapon(int damage, float range, int callDawn) {
        this.damage = damage;
        this.range = range;
        this.callDawn = callDawn;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void attack(EnemyModel enemy, float playerX, float playerY) {
        float distanceToEnemy = (float) Math.sqrt(
                Math.pow(enemy.getPositionX() - playerX, 2) +
                        Math.pow(enemy.getPositionY() - playerY, 2)
        );

        if (distanceToEnemy <= range) {
            enemy.takeDamage(damage);
        }
    }

    public int getCallDawn() {
        return callDawn;
    }
}

