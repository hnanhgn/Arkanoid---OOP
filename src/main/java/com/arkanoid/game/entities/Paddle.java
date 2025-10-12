package com.arkanoid.game.entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Paddle extends Entities {
    private ImageView imageView;
    private double speed = 10;
    private double minX = 0;
    private double maxX = 600;
    private Image paddleImage;

    public Paddle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        try {
            paddleImage = new Image(getClass().getResourceAsStream("/images/normalPaddle.png"));
            paddleImage = new Image(getClass().getResourceAsStream("/images/normalPaddle.png"), width, height,
                    false, true);
        } catch (Exception e) {
            System.err.println("Không thể load ảnh paddle");

        }

        imageView = new ImageView(paddleImage);
        imageView.setX(x);
        imageView.setY(y);
    }

    public Paddle(double x, double y, double width, double height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        try {
            paddleImage = new Image(getClass().getResourceAsStream(imagePath), width, height, false, true);
        } catch (Exception e) {
            System.err.println("Không thể load ảnh từ path: " + imagePath);

        }

        imageView = new ImageView(paddleImage);
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
        double newX = x - speed;
        if (newX >= minX) {
            x = newX;
            update();
        }
    }

    public void moveRight() {
        double newX = x + speed;
        if (newX + width <= maxX) {
            x = newX;
            update();
        }
    }


}