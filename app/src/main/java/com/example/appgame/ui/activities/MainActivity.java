package com.example.appgame.ui.activities;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import com.example.appgame.R;
import com.example.appgame.ui.fragments.GameFragment;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Инициализация MediaPlayer для фоновой музыки
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true); // Зацикливание музыки
            mediaPlayer.start(); // Запуск музыки
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(Build.VERSION.SDK_INT >= 19)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        // Получение параметров из Intent
        int speed = getIntent().getIntExtra("speed", 5);
        int damage = getIntent().getIntExtra("damage", 10);
        int range = getIntent().getIntExtra("range", 30);
        int potions = getIntent().getIntExtra("potions", 3);
        int fps = getIntent().getIntExtra("fps", 60);

        // Передача параметров в GameFragment
        Bundle bundle = new Bundle();
        bundle.putInt("speed", speed);
        bundle.putInt("damage", damage);
        bundle.putInt("range", range);
        bundle.putInt("potions", potions);
        bundle.putInt("fps", fps);


        GameFragment gameFragment = new GameFragment();
        gameFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, gameFragment)
                .commit();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Освобождение ресурсов MediaPlayer
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        // Освобождение ресурсов MediaPlayer
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        // Освобождение ресурсов MediaPlayer
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
