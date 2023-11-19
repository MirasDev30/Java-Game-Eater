package com.example.eaternew;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MyBotPlayer implements BotPlayer, Player {
    private Circle ball;
    private Map map;
    private volatile Position position;
    private PositionNode startingPositionNode = null;
    private boolean[][] validCells;

    public void setPosition(int x, int y) {
        this.position.setX(x);
        this.position.setY(y);
    }

    public void setBallPosition() {
        int cellWH = this.map.getUnit();
        this.ball.setCenterX((double)(this.position.getX() * cellWH + cellWH / 2));
        this.ball.setCenterY((double)(this.position.getY() * cellWH + cellWH / 2));
    }

    private List<Position> generatePathToFoodPosition(Position foodPosition) {
        if (this.startingPositionNode == null) {
            return null;
        } else {
            List<PositionNode> currentLevelNodes = this.startingPositionNode.currentLevelNodes;
            List<Position> pathToFood = new ArrayList();
            PositionNode foodNode = new PositionNode(new Position(-1, -1), (PositionNode)null);

            while(currentLevelNodes != null && currentLevelNodes.size() != 0) {
                foodNode = (PositionNode)currentLevelNodes.get(0);

                int indexWhichHasNext;
                for(indexWhichHasNext = 0; indexWhichHasNext < currentLevelNodes.size(); ++indexWhichHasNext) {
                    foodNode = (PositionNode)currentLevelNodes.get(indexWhichHasNext);
                    if (foodNode.position.equals(foodPosition)) {
                        break;
                    }
                }

                if (foodNode.position.equals(foodPosition)) {
                    break;
                }

                indexWhichHasNext = -1;

                for(int i = 0; i < currentLevelNodes.size(); ++i) {
                    if (((PositionNode)currentLevelNodes.get(i)).next.size() > 0) {
                        indexWhichHasNext = i;
                    }
                }

                if (((PositionNode)currentLevelNodes.get(indexWhichHasNext)).next.size() > 0 && ((PositionNode)((PositionNode)currentLevelNodes.get(indexWhichHasNext)).next.get(0)).currentLevelNodes.size() > 0) {
                    currentLevelNodes = ((PositionNode)((PositionNode)currentLevelNodes.get(indexWhichHasNext)).next.get(0)).currentLevelNodes;
                } else {
                    currentLevelNodes = null;
                }
            }

            while(foodNode.position.getX() != -1 && foodNode.prev != null) {
                pathToFood.add(new Position(foodNode.position.getX(), foodNode.position.getY()));
                foodNode = foodNode.prev;
            }

            return pathToFood;
        }
    }

    public List<Position> getPathToTheFood(Position foodPosition) {
        this.initializeValidCellAndStartingNode();
        this.buildShortestPathsMap(this.startingPositionNode);
        return this.generatePathToFoodPosition(foodPosition);
    }

    private void buildShortestPathsMap(PositionNode node) {
        List<PositionNode> currentLevelNodes = node.currentLevelNodes;
        PositionNode currentNode = null;

        while(!this.noValidCellsLeft()) {
            List<PositionNode> nextLevelNodes = new ArrayList();

            int i;
            for(i = 0; i < ((List)currentLevelNodes).size(); ++i) {
                currentNode = (PositionNode)((List)currentLevelNodes).get(i);
                if (currentNode.position.getY() - 1 >= 0 && !this.map.getIsItWall()[currentNode.position.getY() - 1][currentNode.position.getX()] && this.validCells[currentNode.position.getX()][currentNode.position.getY() - 1]) {
                    PositionNode nextUpNode = new PositionNode(new Position(currentNode.position.getX(), currentNode.position.getY() - 1), currentNode);
                    currentNode.next.add(nextUpNode);
                    nextLevelNodes.add(nextUpNode);
                    nextUpNode.currentPathLength = currentNode.currentPathLength + 1;
                    this.validCells[currentNode.position.getX()][currentNode.position.getY() - 1] = false;
                }

                if (currentNode.position.getY() + 1 < this.map.getSize() && !this.map.getIsItWall()[currentNode.position.getY() + 1][currentNode.position.getX()] && this.validCells[currentNode.position.getX()][currentNode.position.getY() + 1]) {
                    PositionNode nextDownNode = new PositionNode(new Position(currentNode.position.getX(), currentNode.position.getY() + 1), currentNode);
                    currentNode.next.add(nextDownNode);
                    nextLevelNodes.add(nextDownNode);
                    nextDownNode.currentPathLength = currentNode.currentPathLength + 1;
                    this.validCells[currentNode.position.getX()][currentNode.position.getY() + 1] = false;
                }

                if (currentNode.position.getX() + 1 < this.map.getSize() && !this.map.getIsItWall()[currentNode.position.getY()][currentNode.position.getX() + 1] && this.validCells[currentNode.position.getX() + 1][currentNode.position.getY()]) {
                    PositionNode nextRightNode = new PositionNode(new Position(currentNode.position.getX() + 1, currentNode.position.getY()), currentNode);
                    currentNode.next.add(nextRightNode);
                    nextLevelNodes.add(nextRightNode);
                    nextRightNode.currentPathLength = currentNode.currentPathLength + 1;
                    this.validCells[currentNode.position.getX() + 1][currentNode.position.getY()] = false;
                }

                if (currentNode.position.getX() - 1 >= 0 && !this.map.getIsItWall()[currentNode.position.getY()][currentNode.position.getX() - 1] && this.validCells[currentNode.position.getX() - 1][currentNode.position.getY()]) {
                    PositionNode nextLeftNode = new PositionNode(new Position(currentNode.position.getX() - 1, currentNode.position.getY()), currentNode);
                    currentNode.next.add(nextLeftNode);
                    nextLevelNodes.add(nextLeftNode);
                    nextLeftNode.currentPathLength = currentNode.currentPathLength + 1;
                    this.validCells[currentNode.position.getX() - 1][currentNode.position.getY()] = false;
                }
            }

            currentLevelNodes = nextLevelNodes;

            for(i = 0; i < ((List)currentLevelNodes).size(); ++i) {
                ((PositionNode)((List)currentLevelNodes).get(i)).currentLevelNodes = (List)currentLevelNodes;
            }
        }

    }

    private boolean noValidCellsLeft() {
        for(int i = 0; i < this.map.getSize(); ++i) {
            for(int j = 0; j < this.map.getSize(); ++j) {
                if (this.validCells[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    private void initializeValidCellAndStartingNode() {
        this.startingPositionNode = new PositionNode(new Position(this.position.getX(), this.position.getY()), (PositionNode)null);
        List<PositionNode> startingLevelNodes = new ArrayList();
        startingLevelNodes.add(this.startingPositionNode);
        this.startingPositionNode.currentLevelNodes = startingLevelNodes;

        for(int i = 0; i < this.map.getSize(); ++i) {
            for(int j = 0; j < this.map.getSize(); ++j) {
                if (this.map.getMap()[i][j] == 1) {
                    this.validCells[i][j] = false;
                } else {
                    this.validCells[i][j] = true;
                }
            }
        }

        this.validCells[this.position.getX()][this.position.getY()] = false;
    }

    public MyBotPlayer(Map map) {
        this.map = map;
        this.position = this.map.getStartPosition();
        int cellWH = map.getUnit();
        this.ball = new Circle((double)(this.position.getX() * cellWH + cellWH / 2), (double)(this.position.getY() * cellWH + cellWH / 2), (double)(cellWH / 2));
        this.ball.setFill(Color.RED);
        this.map.getChildren().add(this.ball);
        this.validCells = new boolean[map.getSize()][map.getSize()];
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

    private static class PositionNode {
        Position position;
        PositionNode prev;
        List<PositionNode> next;
        List<PositionNode> currentLevelNodes;
        int currentPathLength;

        PositionNode(Position e, PositionNode prev) {
            this.position = e;
            this.next = new ArrayList();
            this.currentLevelNodes = null;
            this.prev = prev;
            this.currentPathLength = 0;
        }
    }
}

