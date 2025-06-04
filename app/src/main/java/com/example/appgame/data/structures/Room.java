package com.example.appgame.data.structures;

public class Room {
    protected Block[][] blocks; // Матрица блоков
    protected int SIZE = 50; // Размер комнаты

    public Room() {
        blocks = new Block[SIZE][SIZE]; // Размер комнаты 50x50 блоков
        initializeRoom();
    }

    protected void initializeRoom() {
        // Инициализация комнаты блоками
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (i == 0 || i == SIZE - 1) {
                    blocks[i][j] = new Block(2, 0); // Внешние стены сверху и снизу
                } else if (j == 0 || j == SIZE - 1) {
                    blocks[i][j] = new Block(0, 0); // Открытые проходы слева и справа
                } else {
                    blocks[i][j] = new Block(1, 0); // Пол
                }
            }
        }
    }

    public Block[][] getBlocks() {
        return blocks;
    }


    public boolean isPlayerInside(int playerI, int playerJ) {
        // Проверка, находится ли игрок внутри комнаты
        return playerI > 0 && playerI < SIZE - 1 && playerJ > 0 && playerJ < SIZE - 1;
    }


    public int getSIZE() {
        return SIZE;
    }
}
