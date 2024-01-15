package com.hit.view;

import com.hit.model.Model;
import com.hit.model.ModelSingleton;
import com.hit.model.Request;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class PayBillController implements Initializable,SceneSwitcher {
    Model model = ModelSingleton.getInstance();
    @FXML
    public Text billIdTextPayMenu;
    @FXML
    public Text billDate;
    @FXML
    public Text sumTextPayMenu;
    private Stage stage;
    private Scene scene;
    private Parent root;

    Text myText;

    @FXML
    private ChoiceBox<String> monthChoiceBox,yearChoiceBox;
    private String[] months = {"1","2","3","4","5","6","7","8","9","10","11","12"};
    private String[] years = {"23","24","25","26","27","28","29"};
    @FXML
    private TextField creditCardNumber;
    @FXML
    private Label customerMessage;


    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        yearChoiceBox.getItems().addAll(years);
        monthChoiceBox.getItems().addAll(months);


    }

    public void payNowButtonClick(ActionEvent event) throws IOException{

        String creditCardNumberString = creditCardNumber.getText();
        String chosenMonth = monthChoiceBox.getValue();
        String chosenYear = yearChoiceBox.getValue();


        if(creditCardNumberString.length() < 16){
            customerMessage.setText("Credit card details are invalid");
            return;
        }

        LocalDate date = LocalDate.of(Integer.parseInt("20"+chosenYear),Integer.parseInt(chosenMonth),1);
        if(date.isBefore(LocalDate.now())){
            customerMessage.setText("Credit card details are invalid");
            return;
        }
        HashMap body = new HashMap<>();
        body.put("id",billIdTextPayMenu.getText());
        model.sendRequest("bill/pay",body);
        Request response = null;
        try {
            response = model.getResponseToRequest();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(response.getHeader().contains("failure")){
            customerMessage.setText("No bills were found.");
        }
        else{
            customerMessage.setText("Bill Payed successfully!");
        }
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
        changeScene(event,"customer-view.fxml");
    }

    public void setDisplayBill(Map bill){
        billIdTextPayMenu.setText(bill.get("id").toString());
        billDate.setText(bill.get("date").toString());
        sumTextPayMenu.setText(bill.get("sum").toString());
    }
}
