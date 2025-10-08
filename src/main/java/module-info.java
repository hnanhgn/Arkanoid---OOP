module com.arkanoid {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.arkanoid.game.ui to javafx.fxml;

    exports com.arkanoid;
    opens com.arkanoid.game.manager to javafx.fxml;
}
