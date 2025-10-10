package com.arkanoid.game.entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Brick extends Entities {
    private ImageView node;
    private boolean destroyed = false;

    public Brick(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        Image image = new Image(getClass().getResource("/images/brick.png").toExternalForm());
        node = new ImageView(image);
        node.setFitWidth(width);
        node.setFitHeight(height);
        node.setX(x);
        node.setY(y);
    }

    public ImageView getNode() {
        return node;
    }

    public void destroy() {
        destroyed = true;
        node.setVisible(false);
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void update() {

    }
}