package com.arkanoid.game.entities;

public abstract class Entities {
    protected double x, y;
    protected double width, height;
    protected double velocityX, velocityY;
    protected double radius;
    public abstract void update();

    public double getRadius() {
        return radius;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getVelocityX() { return velocityX; }
    public double getVelocityY() { return velocityY; }
    public void setPosition(double x, double y) {
        this.x = x; this.y = y;
    }
    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }

}