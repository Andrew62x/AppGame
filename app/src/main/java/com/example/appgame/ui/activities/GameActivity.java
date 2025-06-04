package com.example.appgame.ui.activities;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;




import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.example.appgame.R;
import com.example.appgame.ui.fragments.GameFragment;


public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Загрузка GameFragment в контейнер
        if (savedInstanceState == null) {
            GameFragment gameFragment = new GameFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, gameFragment);
            transaction.commit();
        }
    }



}
