package com.arkanoid.game.entities;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Brick extends Entities {
    private Group node;
    private ImageView baseBrick;
    private ImageView overlay;
    private boolean destroyed = false;

    private int color;
    private int type;
    private int hitCount = 0;

    public Brick(double x, double y, double width, double height, int color, int type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.type = type;

        if (type == 1) {
            node = null;
            return;
        }

        node = new Group();

        if (type != 3) {
            String basePath = "/images/brick" + color + ".png";
            Image baseImage = new Image(getClass().getResource(basePath).toExternalForm());
            baseBrick = new ImageView(baseImage);
            baseBrick.setFitHeight(height);
            baseBrick.setFitWidth(width);
            baseBrick.setX(x);
            baseBrick.setY(y);
            node.getChildren().add(baseBrick);

            if (type == 2) {
                Image chainImage = new Image(getClass().getResource("/images/chain.png").toExternalForm());
                overlay = new ImageView(chainImage);
                overlay.setFitHeight(height);
                overlay.setFitWidth(width);
                overlay.setY(y);
                overlay.setX(x);
                node.getChildren().add(overlay);
            }
        }

        if (type == 3) {
            Image baseImage = new Image(getClass().getResource("/images/bricklock.png").toExternalForm());
            baseBrick = new ImageView(baseImage);
            baseBrick.setFitHeight(height);
            baseBrick.setFitWidth(width);
            baseBrick.setX(x);
            baseBrick.setY(y);
            node.getChildren().add(baseBrick);
        }
    }

    public Group getNode() {
        return node;
    }

    public int getColor() {
        return color;
    }

    public ImageView getBaseBrick() {
        return baseBrick;
    }

    public ImageView getOverlay() {
        return overlay;
    }

    public int getType() {
        return type;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    public void setNode(Group node) {
        this.node = node;
    }

    public void setBaseBrick(ImageView baseBrick) {
        this.baseBrick = baseBrick;
    }

    public void setOverlay(ImageView overlay) {
        this.overlay = overlay;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void destroy() {
        destroyed = true;
        if (node != null) {
            node.setVisible(false);
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void onHit() {
        if (destroyed) return;
        switch (type) {
            case 1:
                return;
            case 2:
                hitCount++;
                if (hitCount >= 2) {
                    destroy();
                } else {
                    if (overlay != null) overlay.setVisible(false);
                }
                break;
            case 3:
                return;
            default:
                destroy();
                break;
        }
    }

    @Override
    public void update() {

    }
}