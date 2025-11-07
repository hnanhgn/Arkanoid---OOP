package com.arkanoid.game.entities;

import com.arkanoid.game.Config;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Paddle extends Entities {
    protected ImageView imageView;
    protected double minX = 10;
    protected double maxX = Config.WIDTH_CANVAS - 10;
    protected Image paddleImage;

    public Paddle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        try {
            paddleImage = new Image(getClass().getResourceAsStream("/images/normalPaddle.png"));
        } catch (Exception e) {
            System.err.println("Không thể load ảnh paddle");
        }
        imageView = new ImageView(paddleImage);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(false);
        imageView.setX(x);
        imageView.setY(y);
    }

    public Paddle(double x, double y, double width, double height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        try {
            paddleImage = new Image(getClass().getResourceAsStream(imagePath));
        } catch (Exception e) {
            System.err.println("Không thể load ảnh từ path: " + imagePath);
        }
        imageView = new ImageView(paddleImage);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(false);
        imageView.setX(x);
        imageView.setY(y);
    }

    @Override
    public void update() {
        imageView.setX(x);
        imageView.setY(y);
    }

    public ImageView getNode() {
        return imageView;
    }

    public void setBoundary(double minX, double maxX) {
        this.minX = minX;
        this.maxX = maxX;
    }

    public void moveLeft() {
        double newX = x - Config.PADDLE_SPEED;
        if (newX >= minX) {
            x = newX;
            update();
        }
    }

    public void moveRight() {
        double newX = x + Config.PADDLE_SPEED;
        if (newX + width <= maxX) {
            x = newX;
            update();
        }
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        update();
    }

    public double getWidth() { return width; }

    public double getHeight() { return height; }
}