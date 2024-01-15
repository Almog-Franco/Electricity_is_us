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

public class BillDeleteController implements SceneSwitcher{
    private Model model = ModelSingleton.getInstance();
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField billIdGiven;
    @FXML
    private Text customerMessage;
    @FXML
    private Text billReturnedDate;
    @FXML
    private Text billReturnedOwner;
    @FXML
    private Text billReturnedSum;

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

    public void onFindButtonClick(ActionEvent event) throws IOException {
        String billId = billIdGiven.getText();
        Request response = null;
        try{
            long billIdLong = Long.parseLong(billId);
        } catch (NumberFormatException e){
            customerMessage.setText("Invalid ID");
            e.printStackTrace();
        }
        HashMap<String,String> billBody = new HashMap<>();
        billBody.put("id",billId);
        model.sendRequest("bill/get",billBody);
        try {
            response = model.getResponseToRequest();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!response.getHeader().contains("null")){
            billReturnedDate.setText("Bill Date found: " + response.getBody().get("billDate"));
            billReturnedOwner.setText("Bill Owner ID found: " + response.getBody().get("ownerId"));
            billReturnedSum.setText("Bill Sum found: " + response.getBody().get("billSum"));
        }
        else{
            billReturnedDate.setText("");
            billReturnedOwner.setText("");
            billReturnedSum.setText("");
            customerMessage.setText("Bill not found");
        }
    }

    public void onDeleteButtonClick(ActionEvent event) throws IOException {
        String billId = billIdGiven.getText();
        String response = null;
        try{
            long billIdLong = Long.parseLong(billId);
        } catch (NumberFormatException e){
            customerMessage.setText("Invalid ID");
            e.printStackTrace();
        }
        HashMap<String,String> billBody = new HashMap<>();
        billBody.put("id",billId);
        model.sendRequest("bill/delete",billBody);
        try {
            response = model.getResponse();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!response.contains("null")){
            billReturnedDate.setText("");
            billReturnedOwner.setText("");
            billReturnedSum.setText("");
            customerMessage.setText("Bill deleted successfully");
        }
        else{
            billReturnedDate.setText("");
            billReturnedOwner.setText("");
            billReturnedSum.setText("");
            customerMessage.setText("Bill deletion failed");
        }
    }


}
