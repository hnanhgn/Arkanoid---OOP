package com.arkanoid.game.entities;

public abstract class Entities {
    protected double x, y;
    protected double width, height;
    protected double velocityX, velocityY;

    public abstract void update();

    public double getX() { return x; }
    public double getY() { return y; }
    public void setPosition(double x, double y) {
        this.x = x; this.y = y;
    }
}
