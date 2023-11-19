package com.example.eaternew;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MyPlayer implements Player {
    private Circle ball;
    private Map map;
    private volatile Position position;

    public MyPlayer(Map map) {
        this.map = map;
        this.position = this.map.getStartPosition();
        int cellWH = map.getUnit();
        this.ball = new Circle((double)(this.position.getX() * cellWH + cellWH / 2), (double)(this.position.getY() * cellWH + cellWH / 2), (double)(cellWH / 2));
        this.ball.setFill(Color.RED);
        this.map.getChildren().add(this.ball);
    }

    public void moveRight() {
        if (this.position.getX() + 1 < this.map.getSize() && !this.map.getIsItWall()[this.position.getY()][this.position.getX() + 1]) {
            this.position.setX(this.position.getX() + 1);
            int cellWH = this.map.getUnit();
            this.ball.setCenterX((double)(this.position.getX() * cellWH + cellWH / 2));
        }
    }

    public void moveLeft() {
        if (this.position.getX() - 1 >= 0 && !this.map.getIsItWall()[this.position.getY()][this.position.getX() - 1]) {
            this.position.setX(this.position.getX() - 1);
            int cellWH = this.map.getUnit();
            this.ball.setCenterX((double)(this.position.getX() * cellWH + cellWH / 2));
        }
    }

    public void moveUp() {
        if (this.position.getY() - 1 >= 0 && !this.map.getIsItWall()[this.position.getY() - 1][this.position.getX()]) {
            this.position.setY(this.position.getY() - 1);
            int cellWH = this.map.getUnit();
            this.ball.setCenterY((double)(this.position.getY() * cellWH + cellWH / 2));
        }
    }

    public void moveDown() {
        if (this.position.getY() + 1 < this.map.getSize() && !this.map.getIsItWall()[this.position.getY() + 1][this.position.getX()]) {
            this.position.setY(this.position.getY() + 1);
            int cellWH = this.map.getUnit();
            this.ball.setCenterY((double)(this.position.getY() * cellWH + cellWH / 2));
        }
    }

    public Position getPosition() {
        return this.position;
    }
}


