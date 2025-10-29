    package com.arkanoid.game.entities;

    import com.arkanoid.game.Config;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;

    public class Paddle extends Entities {
        private ImageView imageView;
        private double minX = 10;
        private double maxX = Config.WIDTH_CANVAS - 10;
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

        public void resetToDefault() {
            this.x = x;
            this.y = y;
            // Reset any other state if needed
        }
        public double getWidth() { return width; }
        public double getHeight() { return height; }

    }