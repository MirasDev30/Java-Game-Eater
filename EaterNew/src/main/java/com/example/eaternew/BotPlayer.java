package com.example.eaternew;

import java.util.List;

public interface BotPlayer {
    List<Position> getPathToTheFood(Position var1);

    void setPosition(int var1, int var2);

    void setBallPosition();
}
