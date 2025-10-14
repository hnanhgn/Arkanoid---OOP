module com.arkanoid {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.arkanoid.game.ui to javafx.fxml;
    exports com.arkanoid;
    exports com.arkanoid.game.entities;
    exports com.arkanoid.game.ui;
}