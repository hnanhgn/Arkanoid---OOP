module com.arkanoid {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;

    opens com.arkanoid.game.ui to javafx.fxml;

    opens images;
    exports com.arkanoid;
    exports com.arkanoid.game.entities;
    exports com.arkanoid.game.ui;
}