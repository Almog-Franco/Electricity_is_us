package com.hit.view;

import com.hit.model.Model;
import com.hit.model.ModelSingleton;
import com.hit.model.Request;
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
import java.util.HashMap;

public class CustomerAddController implements SceneSwitcher {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Model model = ModelSingleton.getInstance();
    @FXML
    private TextField customerAddName;
    @FXML
    private TextField customerAddId;
    @FXML
    private Text customerMessage;

    public CustomerAddController() throws IOException {

    }


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
        changeScene(event,"electrician-view.fxml");
    }

    public void onAddCustomerClick(ActionEvent event) throws IOException, ClassNotFoundException {
        String id = customerAddId.getText();
        String fullName = customerAddName.getText();
        if(id.isBlank() || fullName.isBlank()){
            customerMessage.setText("ID or full name cannot be blank");
        }
        HashMap<String,String> body = new HashMap<String,String>();
        body.put("id",id);
        body.put("fullName",fullName);
        model.sendRequest("customer/add",body);
        String response = model.getResponse();

        if(response.contains("Customer added successfully")){
            customerMessage.setText("Customer added successfully");
        }
        else{
            customerMessage.setText("Customer addition failed");
        }
    }
}
