
package com.arkanoid.game.entities;

import javafx.scene.image.Image;

public class Item {
    private double x;
    private double y;
    private double width;
    private double height;
    private double speed;
    private ItemType type;
    private Image image;
    private boolean active;

    public Item(double x, double y, ItemType type, Image image) {
        this.x = x;
        this.y = y;
        this.width = 30;
        this.height = 30;
        this.speed = 2;
        this.type = type;
        this.image = image;
        this.active = true;
    }

    public void update() {
        y += speed;
    }

    public boolean checkPaddleCollision(Paddle paddle) {
        return x < paddle.getX() + paddle.getWidth() &&
                x + width > paddle.getX() &&
                y + height > paddle.getY() &&
                y < paddle.getY() + paddle.getHeight();
    }

    public boolean isOutOfScreen(double screenHeight) {
        return y > screenHeight;
    }

    // Getters and Setters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public ItemType getType() { return type; }
    public Image getImage() { return image; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
