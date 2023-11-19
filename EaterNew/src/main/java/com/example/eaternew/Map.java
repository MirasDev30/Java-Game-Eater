package com.example.eaternew;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Map extends Pane {
    private int unit;
    private int size = 0;
    private int[][] map;
    private Position start;
    private int cellWidthHeight = 40;
    private boolean[][] isItWall;
    private int[][] updatedMap;
    private Label scoreLabel;

    public Map(String nameOfFile) {
        this.unit = this.cellWidthHeight;

        try {
            try {
                System.out.println("Working Directory = " + System.getProperty("user.dir"));
                BufferedReader buffReader = new BufferedReader(new FileReader(nameOfFile));
                String sizeStr = buffReader.readLine();
                if (sizeStr == null) {
                    throw new Exception();
                }

                this.size = Integer.parseInt(sizeStr);
                this.map = new int[this.size][this.size];
                this.isItWall = new boolean[this.size][this.size];
                this.updatedMap = new int[this.size][this.size];

                for(int i = 0; i < this.size; ++i) {
                    String currentLine = buffReader.readLine();
                    String[] currentLineNumbers = currentLine.split(" ");

                    for(int j = 0; j < currentLineNumbers.length; ++j) {
                        this.updatedMap[j][i] = this.map[i][j] = Integer.parseInt(currentLineNumbers[j]);
                    }
                }

                buffReader.close();
                this.drawMapPane();
            } catch (FileNotFoundException var13) {
                var13.printStackTrace();
            } catch (IOException var14) {
                var14.printStackTrace();
            } catch (Exception var15) {
                var15.printStackTrace();
            }

        } finally {
            ;
        }
    }

    private void drawMapPane() {
        int i;
        for(i = 0; i <= this.size; ++i) {
            Line lineH = new Line((double)(i * this.cellWidthHeight), 0.0, (double)(i * this.cellWidthHeight), (double)(this.cellWidthHeight * this.size));
            Line lineV = new Line(0.0, (double)(i * this.cellWidthHeight), (double)(this.cellWidthHeight * this.size), (double)(i * this.cellWidthHeight));
            this.getChildren().add(lineH);
            this.getChildren().add(lineV);
        }

        for(i = 0; i < this.size; ++i) {
            for(int j = 0; j < this.size; ++j) {
                this.isItWall[i][j] = false;
                if (this.map[i][j] == 1) {
                    Rectangle rectangle = new Rectangle((double)(j * this.cellWidthHeight), (double)(i * this.cellWidthHeight), (double)this.cellWidthHeight, (double)this.cellWidthHeight);
                    rectangle.setFill(Color.BLACK);
                    this.getChildren().add(rectangle);
                    this.isItWall[i][j] = true;
                }

                if (this.map[i][j] == 2) {
                    this.start = new Position(j, i);
                }
            }
        }

        this.scoreLabel = new Label("Score: 0");
        this.scoreLabel.setFont(Font.font("Monospaced", 20.0));
        this.scoreLabel.setMinWidth((double)(this.unit * 4));
        this.scoreLabel.setMaxWidth((double)(this.unit * 4));
        this.scoreLabel.setMinHeight((double)this.unit);
        this.scoreLabel.setMaxHeight((double)this.unit);
        this.scoreLabel.setLayoutX((double)(this.size * this.unit / 2 - 2 * this.unit));
        this.scoreLabel.setLayoutY((double)((this.size + 1) * this.unit));
        this.getChildren().add(this.scoreLabel);
    }

    public int getUnit() {
        return this.unit;
    }

    public int getSize() {
        return this.size;
    }

    public int[][] getMap() {
        return this.updatedMap;
    }

    public Position getStartPosition() {
        return this.start;
    }

    public boolean[][] getIsItWall() {
        return this.isItWall;
    }

    public void setScore(int value) {
        this.scoreLabel.setText("Score: " + value);
    }
}

