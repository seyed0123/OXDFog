module com.example.oxdfog {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.oxdfog.Client to javafx.fxml;
    exports com.example.oxdfog.Client;

    opens com.example.oxdfog.Server to javafx.fxml;
    exports com.example.oxdfog.Server;
}