package com.hit.view;

import com.hit.model.Client;
import com.hit.model.Model;
import com.hit.model.ModelSingleton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Driver extends Application {


    public Driver() throws IOException {
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Electricity Is Us");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
        Model model = ModelSingleton.getInstance();
        model.disconnect();
    }
}