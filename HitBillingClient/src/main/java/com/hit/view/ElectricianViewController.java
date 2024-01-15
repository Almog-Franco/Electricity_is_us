package com.hit.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ElectricianViewController implements SceneSwitcher {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void changeScene(ActionEvent event, String sceneName) throws IOException {
        root = FXMLLoader.load(getClass().getResource(sceneName));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void onBackButtonClick(ActionEvent event) throws IOException {
        changeScene(event,"hello-view.fxml");
    }

    public void onAddBillButton(ActionEvent event) throws IOException {
        changeScene(event,"bill-add-view.fxml");
    }

    public void onAddCustomerClick(ActionEvent event) throws IOException {
        changeScene(event,"customer-add-view.fxml");
    }


    public void onBillDeleteClick(ActionEvent event) throws IOException {
        changeScene(event,"bill-delete-view.fxml");
    }

    public void onViewCustomersButtonClick(ActionEvent event) throws IOException {
        changeScene(event,"all-customer-view.fxml");
    }

    public void onViewBillsButtonClick(ActionEvent event) throws IOException {
        changeScene(event,"all-bill-view.fxml");
    }
}
