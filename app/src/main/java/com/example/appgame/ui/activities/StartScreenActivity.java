package com.example.appgame.ui.activities;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appgame.R;

public class StartScreenActivity extends AppCompatActivity {

    private SeekBar speedSeekBar;
    private SeekBar damageSeekBar;
    private SeekBar rangeSeekBar;
    private SeekBar potionsSeekBar;
    private TextView speedTextView;
    private TextView damageTextView;
    private TextView rangeTextView;
    private TextView potionsTextView;
    private TextView warningTextView;
    private Button startButton;
    private RadioButton r60;
    private RadioButton r90;
    private RadioButton r120;
    private int fps = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(Build.VERSION.SDK_INT >= 19)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        // Инициализация элементов интерфейса
        speedSeekBar = findViewById(R.id.speedSeekBar);
        damageSeekBar = findViewById(R.id.damageSeekBar);
        rangeSeekBar = findViewById(R.id.rangeSeekBar);
        potionsSeekBar = findViewById(R.id.potionsSeekBar);

        speedTextView = findViewById(R.id.speedTextView);
        damageTextView = findViewById(R.id.damageTextView);
        rangeTextView = findViewById(R.id.rangeTextView);
        potionsTextView = findViewById(R.id.potionsTextView);

        warningTextView = findViewById(R.id.textWarning);

        startButton = findViewById(R.id.startButton);
        startButton.setVisibility(View.INVISIBLE);


        findViewById(R.id.radioButton60).setOnClickListener(v -> on60Clicked());
        findViewById(R.id.radioButton90).setOnClickListener(v -> on90Clicked());
        findViewById(R.id.radioButton120).setOnClickListener(v -> on120Clicked());

        // Настройка слушателей для SeekBar
        speedSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener(speedTextView));
        damageSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener(damageTextView));
        rangeSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener(rangeTextView));
        potionsSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener(potionsTextView));

        // Настройка обработчика нажатия кнопки старта
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получение выбранных значений
                int speed = speedSeekBar.getProgress();
                int damage = damageSeekBar.getProgress();
                int range = rangeSeekBar.getProgress();
                int potions = potionsSeekBar.getProgress();

                // Переход на экран загрузки
                showLoadingScreen(speed, damage, range, potions);
            }
        });
    }

    private void showLoadingScreen(final int speed, final int damage, final int range, final int potions) {
        // Отображение экрана загрузки
        setContentView(R.layout.activity_loading_screen);

        // Имитация загрузки в течение 10 секунд
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Переход к основной игре
                startGame(speed, damage, range, potions);
            }
        }, 5000); // 5 секунд
    }

    private void startGame(int speed, int damage, int range, int potions) {
        // Создание Intent для перехода к основной игре
        Intent intent = new Intent(StartScreenActivity.this, MainActivity.class);

        // Передача выбранных параметров в основную игру
        intent.putExtra("speed", speed);
        intent.putExtra("damage", damage);
        intent.putExtra("range", range);
        intent.putExtra("potions", potions);
        intent.putExtra("fps", fps);

        startActivity(intent);
        finish();
    }
    private void on60Clicked(){
        fps = 60;
        startButton.setVisibility(VISIBLE);
        warningTextView.setVisibility(View.INVISIBLE);
    }
    private void on90Clicked(){
        fps = 90;
        startButton.setVisibility(VISIBLE);
        warningTextView.setVisibility(View.INVISIBLE);
    }
    private void on120Clicked(){
        fps = 120;
        startButton.setVisibility(VISIBLE);
        warningTextView.setVisibility(View.INVISIBLE);
    }


    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        private TextView textView;

        public SeekBarChangeListener(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            textView.setText(String.valueOf(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }
}
