package org.example.ooparkanoid;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle {
    private Rectangle rect;
    private double speed = 15;

    public Paddle(double x, double y, double width, double height) {
        rect = new Rectangle(x, y, width, height);
        rect.setFill(Color.DEEPSKYBLUE);
    }

    public Rectangle getNode() {
        return rect;
    }

    public void moveLeft() {
        rect.setX(rect.getX() - speed);
    }

    public void moveRight() {
        rect.setX(rect.getX() + speed);
    }
}