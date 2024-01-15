module com.example.hitbillingclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.hit.view to javafx.fxml;
    exports com.hit.view;
}