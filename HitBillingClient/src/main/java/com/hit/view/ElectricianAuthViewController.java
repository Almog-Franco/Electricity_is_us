package com.hit.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class ElectricianAuthViewController implements SceneSwitcher{
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField passwordInput;
    @FXML
    private Text errorMessage;

    @Override
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

    public void onConfirmButtonClick(ActionEvent event) throws IOException{
        LocalDate localDate = LocalDate.now();
        int addition = localDate.getMonth().getValue() + localDate.getDayOfMonth();
        String correctPassword = "" + localDate.getYear() + localDate.getMonth().getValue() + localDate.getDayOfMonth() + addition;
        String enteredPassword = passwordInput.getText();
        if(enteredPassword.equals(correctPassword)){
            changeScene(event,"electrician-view.fxml");
        }
        else if (passwordInput.getText().length() == 0){
            errorMessage.setText("Password is empty");
        }
        else {
            errorMessage.setText("Incorrect password");
        }

    }
}
