package com.example.appgame.ui.fragments;

import static android.view.View.INVISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.appgame.R;
import com.example.appgame.ui.views.GameSurfaceView;

public class GameFragment extends Fragment {

    private GameSurfaceView gameSurfaceView;
    private Button attackButton;
    private Button healButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализация GameSurfaceView
        gameSurfaceView = view.findViewById(R.id.game_surface_view);

        // Получение параметров из аргументов
        Bundle args = getArguments();
        if (args != null) {
            int speed = args.getInt("speed", 5);
            int damage = args.getInt("damage", 10);
            int range = args.getInt("range", 30);
            int potions = args.getInt("potions", 3);
            int fps = args.getInt("fps", 60);

            // Передача параметров в GameSurfaceView
            gameSurfaceView.setPlayerParams(speed, damage, range, potions, fps);
        }

        // Инициализация GameSurfaceView
        gameSurfaceView = view.findViewById(R.id.game_surface_view);
        //gameSurfaceView.setPlayerParams(speed, damage, range, potions);

        // Инициализация кнопки атаки
        attackButton = view.findViewById(R.id.buttonAttack);

        // Настройка обработчика нажатия кнопки атаки
        attackButton.setOnClickListener(v -> {
            // Выполнение атаки
            gameSurfaceView.performAttack();
            if(gameSurfaceView.isDefeat || gameSurfaceView.isVictory){
                healButton.setVisibility(INVISIBLE);
                attackButton.setVisibility(INVISIBLE);
            }

        });


        // Инициализация кнопки лечения
        healButton = view.findViewById(R.id.buttonHeal);

        // Настройка обработчика нажатия кнопки лечения
        healButton.setOnClickListener(v -> {
            // Выполнение атаки
            gameSurfaceView.heal();
            if(gameSurfaceView.isDefeat || gameSurfaceView.isVictory){
                healButton.setVisibility(INVISIBLE);
                attackButton.setVisibility(INVISIBLE);
            }
        });

        // Настройка GameSurfaceView
        setupGameSurfaceView();
    }

    private void setupGameSurfaceView() {
        gameSurfaceView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        gameSurfaceView.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        gameSurfaceView.pause();
    }
}
