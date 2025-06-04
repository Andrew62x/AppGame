package com.example.appgame.data.structures;

public class RoomTypeEnd extends Room {
    @Override
    protected void initializeRoom() {
        for (int i = 1; i < SIZE-1; i++) {
            for (int j = 0; j < SIZE-1; j++) {
                blocks[i][j] = new Block(1, 0); // Пол
            }

        }
        for (int i = 0; i < SIZE; i++) {
            blocks[i][SIZE-1] = new Block(2, 0);
        }
        for (int j = 0; j < SIZE; j++) {
            blocks[0][j] = new Block(2, 0);
            blocks[SIZE-1][j] = new Block(2, 0);
        }

    }
}
