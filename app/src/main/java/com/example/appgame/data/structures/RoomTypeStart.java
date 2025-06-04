package com.example.appgame.data.structures;

public class RoomTypeStart extends Room {
    @Override
    protected void initializeRoom() {
        for (int i = 1; i < SIZE-1; i++) {
            for (int j = 1; j < SIZE-1; j++) {
                    blocks[i][j] = new Block(1, 0);
            }

        }
        for (int i = 0; i < SIZE; i++) {
            blocks[i][0] = new Block(2, 0);
            blocks[i][SIZE-1] = new Block(2, 0);
        }
        for (int i = 1; i < SIZE-1; i++) {
            blocks[i][1] = new Block(4, 0);
            blocks[i][SIZE-2] = new Block(4, 0);
        }
        for (int j = 0; j < SIZE; j++) {
            blocks[0][j] = new Block(2, 0);
            blocks[SIZE-1][j] = new Block(2, 0);
        }

    }
}
