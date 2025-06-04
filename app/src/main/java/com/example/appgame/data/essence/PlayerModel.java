package com.example.appgame.data.essence;

import android.util.Log;

import com.example.appgame.data.MeleeWeapon;

import java.util.List;

public class PlayerModel {

    private int health; // Здоровье игрока
    private double positionX; // Координата X игрока на карте
    private double positionY; // Координата Y игрока на карте
    private boolean facingRight; // Направление взгляда игрока (true - вправо, false - влево)
    private MeleeWeapon meleeWeapon; // Оружие ближнего боя
    private boolean isHit; // Флаг, указывающий, что игрок получил урон


    public PlayerModel() {
        this.health = 100; // Начальное здоровье
        this.positionX = 0; // Начальная позиция X на карте
        this.positionY = 0; // Начальная позиция Y на карте
        this.facingRight = true; // По умолчанию игрок смотрит вправо
        this.meleeWeapon = new MeleeWeapon(10, 100, 100); // Оружие ближнего боя с уроном 10 и дальностью 50
        this.isHit = false; // Изначально игрок не получил урон
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public float getPositionX() {
        return (float) positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return (float) positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public void setPosition(float positionX, float positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
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


    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }
    public void takeDamage(int damage) {
        setHealth(this.health - damage); // Уменьшает здоровье на величину урона
        setHit(true); // Устанавливаем флаг получения урона
    }
    public void heal(int amount) {
        setHealth(this.health + amount); // Увеличивает здоровье на величину лечения
    }

    public void move(float deltaX, float deltaY) {
        positionX += deltaX;
        positionY += deltaY;

        // Обновление направления взгляда игрока
        facingRight = deltaX >= 0;
    }
    public void attack(List<EnemyModel> enemies) {
        EnemyModel closestEnemy = findClosestEnemy(enemies);
        if (closestEnemy != null) {
            meleeWeapon.attack(closestEnemy, (float) positionX, (float) positionY);
        }
    }

    private EnemyModel findClosestEnemy(List<EnemyModel> enemies) {
        EnemyModel closestEnemy = null;
        float minDistance = Float.MAX_VALUE;

        for (EnemyModel enemy : enemies) {
            if (enemy.isAlive()) {
                float distance = (float) Math.sqrt(
                        Math.pow(enemy.getPositionX() - positionX, 2) +
                                Math.pow(enemy.getPositionY() - positionY, 2)
                );

                if (distance < minDistance) {
                    minDistance = distance;
                    closestEnemy = enemy;
                }
            }
        }

        return closestEnemy;
    }


}
