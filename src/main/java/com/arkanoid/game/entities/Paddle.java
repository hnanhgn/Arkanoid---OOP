package com.arkanoid.game.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle {
    private Rectangle rect;
    private double speed = 15;
    private double minX = 0;
    private double maxX = 600;

    public Paddle(double x, double y, double width, double height) {
        rect = new Rectangle(x, y, width, height);
        rect.setFill(Color.DEEPSKYBLUE);
    }

    public Rectangle getNode() {
        return rect;
    }

    public void setBoundary(double minX, double maxX) {
        this.minX = minX;
        this.maxX = maxX;
    }

    public void moveLeft() {
        double newX = rect.getX() - speed;
        if (newX >= minX) {
            rect.setX(newX);
        }
    }

    public void moveRight() {
        double newX = rect.getX() + speed;
        if (newX + rect.getWidth() <= maxX) {
            rect.setX(newX);
        }
    }

}