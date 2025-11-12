package com.arkanoid.game.manager;

public interface PowerUp {
    void activate();
    void update();
    void deactivate();
    boolean isActive();
}