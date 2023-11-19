package com.example.eaternew;

import java.util.Random;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Food {
    private Map map;
    private Pane foodPane;
    private Player player;
    private Circle circle;
    private Position foodPosition;
    private Label seconds;
    private final int timer = 5;
    private int numOfCircles = 10;
    private int time;
    private int points;
    private int size;
    private int[][] cells;

    public Food(Map var1, Player var2) {
        this.map = var1;
        this.foodPane = new Pane();
        this.map.getChildren().add(this.foodPane);
        this.player = var2;
        this.size = this.map.getSize();
        this.cells = this.map.getMap();
        Thread var3 = new Thread(() -> {
            while(this.numOfCircles > 0) {
                this.createFood();
                Platform.runLater(() -> {
                    this.foodPane.getChildren().addAll(new Node[]{this.circle, this.seconds});
                });

                for(this.time = 5; this.time > 0; --this.time) {
                    Platform.runLater(() -> {
                        this.seconds.setText("" + this.time);
                    });
                    if (this.player.getPosition().equals(this.foodPosition)) {
                        this.points += this.time;
                        break;
                    }

                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException va3) {
                    }
                }

                try {
                    Thread.sleep(10L);
                } catch (InterruptedException va2) {
                }

                Platform.runLater(() -> {
                    this.foodPane.getChildren().clear();
                });
                --this.numOfCircles;
            }

            this.foodPosition = null;
            System.out.println(this.getPoints());
        });
        var3.start();
    }

    public int getPoints() {
        return this.points;
    }

    private void createFood() {
        Random var1 = new Random();
        double var4 = (double)this.map.getUnit();

        int var2;
        int var3;
        do {
            do {
                var2 = var1.nextInt(this.size);
                var3 = var1.nextInt(this.size);
            } while(this.player.getPosition().equals(new Position(var2, var3)));
        } while(this.map.getMap()[var2][var3] == 1);

        this.circle = new Circle((double)var2 * var4 + var4 / 2.0, (double)var3 * var4 + var4 / 2.0, var4 / 4.0);
        this.circle.setFill(Color.GREEN);
        this.foodPosition = new Position(var2, var3);
        this.seconds = new Label("5");
        this.seconds.setTranslateX((double)var2 * var4);
        this.seconds.setTranslateY((double)var3 * var4);
    }

    public Position getPosition() {
        return this.foodPosition;
    }
}

