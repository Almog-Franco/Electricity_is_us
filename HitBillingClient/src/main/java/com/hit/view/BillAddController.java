package com.hit.view;

import com.hit.model.Model;
import com.hit.model.ModelSingleton;
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

public class BillAddController implements SceneSwitcher {
    private Stage stage;
    private Scene scene;
    private Parent root;
    Model model = ModelSingleton.getInstance();
    @FXML
    private TextField billDate;
    @FXML
    private TextField billOwnersId;
    @FXML
    private TextField billSum;
    @FXML
    private Text customerMessage;

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


    public void onAddBillClick(ActionEvent event) throws IOException {
        String billStringDate = billDate.getText();
        String billOwnerStringId = billOwnersId.getText();
        String billStringSum = billSum.getText();
        String response = "";
        try{
            double sum = Double.parseDouble(billStringSum);
            long Id = Long.parseLong(billOwnerStringId);
        }catch (Exception e){
            customerMessage.setText("Error, Please check the data entered");
            return;
        }
        HashMap<String,String> billBody = new HashMap<>();
        String id = String.valueOf(System.currentTimeMillis());
        billBody.put("id",id);
        billBody.put("billDate",billStringDate);
        billBody.put("ownerId",billOwnerStringId);
        billBody.put("billSum",billStringSum);
        model.sendRequest("bill/add",billBody);
        try {
            response = model.getResponse();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(response.contains("Bill added successfully")){
            customerMessage.setText("Bill added successfully! id: " + id);
        }
        else{
            customerMessage.setText("Bill addition failed");
        }
    }


}
