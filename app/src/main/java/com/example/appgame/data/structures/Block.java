package com.example.appgame.data.structures;


public class Block {
    private int type; // Тип блока: 0 - пустота, 1 - пол, 2 - стена, 3 - разрушаемый блок, 4 - указатель в полу
    private int texture; // Текстура блока

    public Block(int type, int texture) {
        this.type = type;
        this.texture = texture;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTexture() {
        return texture;
    }

    public void setTexture(int texture) {
        this.texture = texture;
    }
}
