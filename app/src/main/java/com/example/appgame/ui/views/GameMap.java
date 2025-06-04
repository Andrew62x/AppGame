package com.example.appgame.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;

import com.example.appgame.R;
import com.example.appgame.data.structures.*;
import com.example.appgame.data.essence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameMap {
    private List<Room> rooms;
    private List<EnemyModel> enemies;
    private Paint paint;
    private Bitmap[] blockTextures;
    private Bitmap enemyTexture, diedenemyTexture;
    private Matrix flipMatrix;
    private Matrix rotationMatrix;
    private Random random;
    private int BLOCK_SIZE; // Размер блока 100x100 пикселей

    protected int SIZE = 50; // Размер комнаты


    public GameMap(Context context) {
        rooms = new ArrayList<>();
        enemies = new ArrayList<>();
        paint = new Paint();
        random = new Random();
        flipMatrix = new Matrix();
        rotationMatrix = new Matrix();


        // Загрузка текстур блоков
        blockTextures = new Bitmap[6];
        blockTextures[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_block);
        blockTextures[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.floor_block);
        blockTextures[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wall_block);
        blockTextures[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.breakable_block);
        blockTextures[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.floor_block_pointer);
        blockTextures[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wall_block_breaken);
        BLOCK_SIZE = blockTextures[0].getHeight();
        // Загрузка текстуры врага
        enemyTexture = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_texture);
        diedenemyTexture = BitmapFactory.decodeResource(context.getResources(), R.drawable.died_enemy_texture);

    }

    public void generateInitialMap(Bitmap swordTexture) {
        // Генерация линейной карты из комнат
        //Первая комната
        Room roomStart = new RoomTypeStart();
        rooms.add(roomStart);

        for (int i = 1; i < 4; i++) { // Генерация 3 комнат по середине
            Room room = new RoomTypeMedium();
            rooms.add(room);
        }
        // последняя комната
        Room roomEnd = new RoomTypeEnd();
        rooms.add(roomEnd);
        // Генерация врагов в текущей комнате
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        generateEnemiesInCurrentRoom(0, swordTexture);


    }

    public void generateEnemiesInCurrentRoom(int number, Bitmap swordTexture) {
        number++;
        // Генерация врагов в текущей комнате
        int numberOfEnemies = random.nextInt(5) + 1; // Случайное количество врагов от 1 до 5
        for (int i = 0; i < numberOfEnemies; i++) {
            EnemyModel enemy = new EnemyModel(swordTexture, BLOCK_SIZE);
            enemy.setType(random.nextInt(3)); // Случайный тип врага
            enemy.setHealth(random.nextInt(100) + 50); // Случайное здоровье от 50 до 149
            enemy.setPosition(((random.nextInt(47)+1) * BLOCK_SIZE) * number + BLOCK_SIZE / 2, random.nextInt(48) * BLOCK_SIZE + BLOCK_SIZE / 2); // Случайная позиция в комнате
            enemy.setSpeed(random.nextInt(25) + 30); // Скорость от 30 до 54
            enemies.add(enemy);
        }
    }



    public int getPlayerCurrentRoomIndex(float playerX) {
        // Определение индекса комнаты, в которой находится игрок
        int roomIndex = (int) (playerX / (rooms.get(0).getSIZE() * BLOCK_SIZE));
        // Проверка границ, чтобы убедиться, что индекс комнаты находится в допустимом диапазоне
        if (roomIndex < 0) {
            roomIndex = 0;
        } else if (roomIndex >= rooms.size()) {
            roomIndex = rooms.size() - 1;
        }
        return roomIndex;
    }

    public void draw(Canvas canvas) {
        int offsetX = 0;
        for (Room room : rooms) {
            drawRoom(canvas, room, offsetX);
            offsetX += 50 * BLOCK_SIZE; // Смещение по вертикали для следующей комнаты
        }

        // Отрисовка врагов
        for (EnemyModel enemy : enemies) {
            drawEnemy(canvas, enemy);
        }
    }

    private void drawRoom(Canvas canvas, Room room, int offsetX) {
        Block[][] blocks = room.getBlocks();
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                Block block = blocks[i][j];
                canvas.drawBitmap(blockTextures[block.getType()], j * BLOCK_SIZE + offsetX, i * BLOCK_SIZE, paint);
            }
        }
    }

    private void drawEnemy(Canvas canvas, EnemyModel enemy) {
        if(enemy.isAlive()){
            int x = -75, y = 0; // Смещение для меча
            // Отрисовка врага
            if (!enemy.isFacingRight()) {
                flipMatrix.setScale(-1, 1, enemyTexture.getWidth() / 2, enemyTexture.getHeight() / 2);
                canvas.drawBitmap(Bitmap.createBitmap(enemyTexture, 0, 0, enemyTexture.getWidth(), enemyTexture.getHeight(), flipMatrix, true),
                        enemy.getPositionX() - enemyTexture.getWidth() / 2, enemy.getPositionY() - enemyTexture.getHeight() / 2, paint);
            } else {
                canvas.drawBitmap(enemyTexture, enemy.getPositionX() - enemyTexture.getWidth() / 2, enemy.getPositionY() - enemyTexture.getHeight() / 2, paint);
            }

            Bitmap swordTexture = enemy.getSwordTexture();
            // Отрисовка меча
            if (enemy.isAttacking()) {
                rotationMatrix.reset();
                rotationMatrix.postRotate(50, swordTexture.getWidth() / 2 - x, swordTexture.getHeight() / 2 - y);
                if (!enemy.isFacingRight()) {
                    flipMatrix.setScale(-1, 1, swordTexture.getWidth() / 2 - x, swordTexture.getHeight() / 2 - y);
                    rotationMatrix.postConcat(flipMatrix);
                }
                Bitmap rotatedSword = Bitmap.createBitmap(swordTexture, 0, 0, swordTexture.getWidth(), swordTexture.getHeight(), rotationMatrix, true);
                if (!enemy.isFacingRight())
                    canvas.drawBitmap(rotatedSword, enemy.getPositionX() - rotatedSword.getWidth() / 2 - x, enemy.getPositionY() - rotatedSword.getHeight() / 2 - y, paint);
                else
                    canvas.drawBitmap(rotatedSword, enemy.getPositionX() - rotatedSword.getWidth() / 2 + x, enemy.getPositionY() - rotatedSword.getHeight() / 2 - y, paint);
            } else {
                if (!enemy.isFacingRight()) {
                    flipMatrix.setScale(-1, 1, swordTexture.getWidth() / 2, swordTexture.getHeight() / 2);
                    canvas.drawBitmap(Bitmap.createBitmap(swordTexture, 0, 0, swordTexture.getWidth(), swordTexture.getHeight(), flipMatrix, true),
                            enemy.getPositionX() - swordTexture.getWidth() / 2 - x, enemy.getPositionY() - swordTexture.getHeight() / 2 - y, paint);
                } else {
                    canvas.drawBitmap(swordTexture, enemy.getPositionX() - swordTexture.getWidth() / 2 + x, enemy.getPositionY() - swordTexture.getHeight() / 2 - y, paint);
                }
            }
        }else{
            canvas.drawBitmap(diedenemyTexture, enemy.getPositionX() - enemyTexture.getWidth() / 2, enemy.getPositionY() - enemyTexture.getHeight() / 2, paint);
        }
    }



    public List<EnemyModel> getEnemies() {
        return enemies;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public boolean isAllEnemiesDie() {
        for (EnemyModel enemy : enemies)
            if (enemy.isAlive())
                return false;
        return true;
    }

    private Room openedRoom(Room room) {
        Block[][] block = room.getBlocks();
        // Открытие комнаты, изменение типов блоков на границах на проходимые
        for (int i = 1; i < SIZE - 1; i++) {
            block[i][SIZE - 1].setType(5); // Правая граница
        }
        for (int i = 1; i < SIZE-1; i++) {
            block[i][SIZE-2] = new Block(4, 0);
        }
        return room;
    }

    public void openRoom(float posX) {
        rooms.set(getPlayerCurrentRoomIndex(posX), openedRoom(rooms.get(getPlayerCurrentRoomIndex(posX))));
    }

    public void deleteEnemies() {
        enemies.clear();
    }


}
