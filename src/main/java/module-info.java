module org.example.ooparkanoid {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.ooparkanoid to javafx.fxml;
    exports org.example.ooparkanoid;
}