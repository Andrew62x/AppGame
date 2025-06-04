package com.example.appgame.ui.views;


import static android.util.Log.INFO;

import static androidx.core.view.ViewKt.postDelayed;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.appgame.R;
import com.example.appgame.data.essence.EnemyModel;
import com.example.appgame.data.essence.PlayerModel;
import com.example.appgame.ui.fragments.GameFragment;

public class GameSurfaceView extends SurfaceView implements Runnable {

    private Thread gameThread;
    private SurfaceHolder surfaceHolder;
    private boolean isPlaying;
    private Joystick joystick;
    private GameMap gameMap;
    private PlayerModel playerModel;
    private Player player;
    private int BLOCK_SIZE; // Размер блока 100x100 пикселей
    private float mapOffsetX, mapOffsetY; // Смещение карты
    private boolean isAttacking; // Флаг атаки
    public boolean isVictory; // Флаг победы
    public boolean isDefeat;// Флаг поражения
    private Bitmap playerTexture;
    private Bitmap playerHitTexture;
    private Bitmap playerDiedTexture;
    private Bitmap enemySwordTexture;
    private Bitmap swordTexture;

    private Bitmap defeatImage;
    private HealthBar healthBar;
    private int fps = 60;
    private int potions = 4;
    //private Handler handler;


    int t = 0; // счетчик для отладки работы


    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder = getHolder();
        //handler = new Handler();
        BLOCK_SIZE = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_block).getHeight();
        // Инициализация джойстика
        joystick = new Joystick(0, 0, 100, 50);

        // Загрузка текстуры игрока
        playerTexture = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_texture);
        playerHitTexture = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_has_damage_texture);
        playerDiedTexture = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_died_texture);
        enemySwordTexture = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy_sword_texture);
        swordTexture = BitmapFactory.decodeResource(context.getResources(), R.drawable.sword_texture);
        //defeatImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.defeat_image);

        // Инициализация карты
        gameMap = new GameMap(context);
        gameMap.generateInitialMap(enemySwordTexture); // Генерация начальной карты

        // Инициализация модели игрока
        playerModel = new PlayerModel();

        // Инициализация игрока
        player = new Player(playerTexture, swordTexture, getWidth() / 2, getHeight() / 2);


        // Инициализация смещения карты
        mapOffsetX = 0;
        mapOffsetY = 0;
        isAttacking = false;
        isDefeat = false;
    }

    @Override
    public void run() {

        while (isPlaying) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        if (t == 30) {
            playerModel.setPosition(getWidth() / 2, getHeight() / 2);
            ++t;
        }
        if (isVictory || isDefeat) {
            return; // Если победа или поражение, не обновляем состояние игры
        }
        if (t < 30) ++t;
        joystick.update();

        float deltaX = (float) (joystick.getActuatorX() * player.getSpeed());
        float deltaY = (float) (joystick.getActuatorY() * player.getSpeed());


        // Проверка столкновений с твердыми блоками и Обновление координат игрока на карте
        move( deltaX,  deltaY);

        Log.i("pl", playerModel.getPositionX() + "");

        if (gameMap.isAllEnemiesDie()){
            if (gameMap.getPlayerCurrentRoomIndex(playerModel.getPositionX()+deltaX) < 4) {
                gameMap.openRoom(playerModel.getPositionX()+deltaX);
                gameMap.deleteEnemies();
                gameMap.generateEnemiesInCurrentRoom(gameMap.getPlayerCurrentRoomIndex(playerModel.getPositionX()+deltaX), enemySwordTexture);
            }
        }
        if (gameMap.getPlayerCurrentRoomIndex(playerModel.getPositionX()+deltaX) == 4){
            isVictory = true;
        }

        // Обновление позиций врагов
        for (EnemyModel enemy : gameMap.getEnemies()) {
            if (enemy.isAlive()) {
                enemy.moveTowards(playerModel.getPositionX(), playerModel.getPositionY());
                if (!enemy.isAttacking()) {
                    if(enemy.attack(playerModel)){
                        enemy.setAttacking(true);
                        // Выполнение атаки
                        postDelayed(() -> {
                            enemy.setAttacking(false);
                        }, enemy.getMeleeWeapon().getCallDawn()); // Сброс состояния атаки
                    }
                }
            }
        }

        //gameMap.updateEnemies(playerModel);
        // Проверка на получение урона и изменение текстуры
        if (playerModel.isHit()) {
            playerModel.setHit(false);
            player.setPlayerTexture(playerHitTexture);
            postDelayed(() -> player.setPlayerTexture(playerTexture), 3000); // Возвращаем исходную текстуру через 3 секунды
        }

        // Обновление текущего здоровья в полоске жизней
        healthBar.setCurrentHealth(playerModel.getHealth());

        // Проверка на поражение
        if (playerModel.getHealth() <= 0) {
            isDefeat = true;
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            Canvas canvas = surfaceHolder.lockCanvas();

            if (isVictory) {
                drawVictoryScreen(canvas); // Отрисовка экрана победы
            } else if (isDefeat) {
                // Отрисовка игрока в центре экрана
                player.setPlayerTexture(playerDiedTexture);
                drawDefeatScreen(canvas); // Отрисовка экрана поражения
                player.draw(canvas, getWidth() / 2, getHeight() / 2, playerModel.isFacingRight(), false);

            } else {
            canvas.drawColor(android.graphics.Color.BLACK); // Очистка экрана

            // Отрисовка карты с учетом смещения
            canvas.save();
            canvas.translate(-mapOffsetX, -mapOffsetY);
            gameMap.draw(canvas);
            canvas.restore();

            // Отрисовка игрока в центре экрана
            player.draw(canvas, getWidth() / 2, getHeight() / 2, playerModel.isFacingRight(), true);

            // Отрисовка джойстика
            joystick.draw(canvas);
            // Отрисовка полоски жизней
            healthBar.draw(canvas);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {

        try {
            Thread.sleep(1000/fps); // Контроль скорости обновления (примерно 60 FPS)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (x < getWidth() / 2) { // Если касание в левой части экрана
                    joystick.setPosition(x, y);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (joystick.isPressed(x, y)) {
                    joystick.setActuator(x, y);
                }
                break;
            case MotionEvent.ACTION_UP:
                joystick.resetActuator();
                joystick.deactivate();
                break;
        }
        return true;
    }

    public void performAttack() {
        // Выполнение атаки
        isAttacking = true;
        player.setAttacking(true);
        playerModel.attack(gameMap.getEnemies());
        postDelayed(() -> {
            isAttacking = false;
            player.setAttacking(false);
        }, playerModel.getMeleeWeapon().getCallDawn()); // Сброс состояния атаки
    }
    public void heal() {
        if(playerModel.getHealth()<=80){
            healthBar.heal(playerModel);
        }
    }
    public void setPlayerParams(int speed, int damage, int range, int potions, int fps) {
        // Установка параметров игрока
        player.setSpeed(speed);
        playerModel.getMeleeWeapon().setDamage(damage);
        playerModel.getMeleeWeapon().setRange(range);
        this.potions = potions;
        this.fps = fps;
        // Инициализация полоски жизней
        healthBar = new HealthBar(100, 1000, 80, 30, 30, potions);
    }


    private void move(float deltaX, float deltaY){
        if (!isCollision(playerModel.getPositionX() + deltaX, playerModel.getPositionY()) &&
                !isCollision(playerModel.getPositionX(), playerModel.getPositionY() + deltaY)) {
            mapOffsetX += deltaX;
            mapOffsetY += deltaY;
            playerModel.move(deltaX, deltaY);
        } else if (!isCollision(playerModel.getPositionX() + deltaX, playerModel.getPositionY())) {
            mapOffsetX += deltaX;
            playerModel.move(deltaX, 0);
        } else if (!isCollision(playerModel.getPositionX(), playerModel.getPositionY() + deltaY)) {
            mapOffsetY += deltaY;
            playerModel.move(0, deltaY);
        }
    }

    private boolean isCollision(float x, float y) {
        // Получаем координаты блока, на который пытается переместиться игрок
        int blockX = ((int) (x / BLOCK_SIZE)) % 50;
        int blockY = ((int) (y / BLOCK_SIZE));
        int type = gameMap.getRooms().get(gameMap.getPlayerCurrentRoomIndex(x)).getBlocks()[blockY][blockX].getType();
        // Log.i("bl", blockX + " " + blockY +" "+type +" "+gameMap.getPlayerCurrentRoomIndex(playerModel));
        // Проверяем, является ли блок твердым (тип 2 или 3)
        if (type == 2) {
            return true; // Столкновение с твердым блоком
        }


        return false; // Нет столкновения
    }
    private void drawVictoryScreen(Canvas canvas) {
        // Отрисовка синего фона
        canvas.drawColor(Color.BLUE);

        // Отрисовка текста "Победа"
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(100);
        paint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText("Победа", canvas.getWidth() / 2, canvas.getHeight() / 2, paint);
    }

    private void drawDefeatScreen(Canvas canvas) {
        // Отрисовка красного фона
        canvas.drawColor(Color.argb(255, 141, 78, 78));

        // Отрисовка текста "Поражение"
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(100);
        textPaint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText("Поражение", canvas.getWidth() / 2, canvas.getHeight() / 3, textPaint);

        // Отрисовка изображения поражения
        if (defeatImage != null) {
            int imageX = (canvas.getWidth() - defeatImage.getWidth()) / 2;
            int imageY = canvas.getHeight() / 2;
            canvas.drawBitmap(defeatImage, imageX, imageY, new Paint());
        }
    }


    public void pause() {
        isPlaying = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
