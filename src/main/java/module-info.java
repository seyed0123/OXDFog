module com.example.oxdfog {
    requires javafx.controls;
    requires javafx.fxml;
    requires jbcrypt;
    requires java.sql;
    requires jackson.databind;
    requires org.json;
    requires com.google.gson;


    opens com.example.oxdfog to javafx.fxml;
    exports com.example.oxdfog ;

    opens com.example.oxdfog.Server ;
    exports com.example.oxdfog.Server;
}