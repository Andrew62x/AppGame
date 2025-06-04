package com.example.appgame.data.essence;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.appgame.R;
import com.example.appgame.data.MeleeWeapon;
import com.example.appgame.data.structures.Block;

public class EnemyModel {

    private int type; // Тип врага (например, 0 - обычный, 1 - элитный, 2 - босс)
    private int health; // Здоровье врага
    private boolean isAlive; // Флаг, указывающий, жив ли враг
    private float positionX; // Координата X врага на карте
    private float positionY; // Координата Y врага на карте
    private float speed; // Скорость врага
    private boolean facingRight; // Направление взгляда врага (true - вправо, false - влево)
    private MeleeWeapon meleeWeapon; // Оружие ближнего боя
    private Bitmap swordTexture;
    private boolean isAttacking; // Флаг атаки
    private int BLOCK_SIZE;

    public EnemyModel(Bitmap swordTexture, int BLOCK_SIZE) {
        this.isAlive = true; // По умолчанию враг жив
        this.health = 100; // Начальное здоровье
        this.positionX = 0; // Начальная позиция X на карте
        this.positionY = 0; // Начальная позиция Y на карте
        this.speed = 1.0f; // Начальная скорость
        this.facingRight = true; // По умолчанию враг смотрит вправо
        this.meleeWeapon = new MeleeWeapon(10, 70, 1500); // Оружие ближнего боя с уроном 10 и дальностью 50
        this.swordTexture = swordTexture;
        this.isAttacking = false; // Изначально враг не атакует
        this.BLOCK_SIZE = BLOCK_SIZE;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        if (this.health <= 0) {
            this.isAlive = false; // Если здоровье <= 0, враг мёртв
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public void setPosition(float positionX, float positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public MeleeWeapon getMeleeWeapon() {
        return meleeWeapon;
    }

    public void setMeleeWeapon(MeleeWeapon meleeWeapon) {
        this.meleeWeapon = meleeWeapon;
    }


    public void takeDamage(int damage) {
        if (isAlive) {
            setHealth(this.health - damage); // Уменьшает здоровье на величину урона
        }
    }

    public void heal(int amount) {
        setHealth(this.health + amount); // Увеличивает здоровье на величину лечения
    }

    public void moveTowards(float targetX, float targetY) {
        float dx = targetX - positionX;
        float dy = targetY - positionY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance > 60 /*&& distance < 260*48*/) {
            if (distance > BLOCK_SIZE * 5) {
                positionX += (dx / distance) * 100;
                positionY += (dy / distance) * 100;
            } else {
                positionX += (dx / distance) * speed;
                positionY += (dy / distance) * speed;
            }

            // Обновление направления взгляда врага
            if (dx > 0) {
                facingRight = true;
            } else if (dx < 0) {
                facingRight = false;
            }
        }
    }

    public boolean attack(PlayerModel player) {
        float distanceToPlayer = (float) Math.sqrt(
                Math.pow(positionX - player.getPositionX(), 2) +
                        Math.pow(positionY - player.getPositionY(), 2)
        );

        if (distanceToPlayer <= meleeWeapon.getRange()) {
            player.takeDamage(meleeWeapon.getDamage());
            return true;
        }
        return false;

    }

    public Bitmap getSwordTexture() {
        return swordTexture;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }
}
