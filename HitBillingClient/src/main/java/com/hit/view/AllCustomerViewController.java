package com.hit.view;

import com.hit.model.Model;
import com.hit.model.ModelSingleton;
import com.hit.model.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AllCustomerViewController implements SceneSwitcher, Initializable {
    @FXML
    public Text customerMessage;
    Model model = ModelSingleton.getInstance();
    @FXML
    public TableView customerTable;
    @FXML
    public TableColumn <Map,String>customerIdColumn;
    @FXML
    public TableColumn<Map,String> fullNameColumn;
    @FXML
    public TableColumn<Map,String> balanceColumn;
    @FXML
    public TableColumn<Map,String> totalBillsColumn;
    @FXML
    public TableColumn<Map,String> unpaidBillsColumn;
    @FXML
    public TextField givenCustomerId;
    @FXML
    public TextField sumGiven;
    @FXML
    public ChoiceBox sumChoiceBox;
    @FXML
    public RadioButton sumRadio;
    @FXML
    public RadioButton idRadio;
    private Stage stage;
    private Scene scene;
    private Parent root;
    String[] choices = {"greater than","lower than","equals"};


    public AllCustomerViewController() {
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

    public void onFilterClick(ActionEvent event) {
        customerTable.getItems().clear();
        HashMap body = new HashMap<>();
        if(idRadio.isSelected()){
            String givenId = givenCustomerId.getText();
            try{
                long id = Long.parseLong(givenId);
            } catch (NumberFormatException e) {
                customerMessage.setText("ID Not Valid");
                return;
            }
            body.put("id",givenId);
            setTable("customer/customersById",body);
        }
        if(sumRadio.isSelected()){
            String givenSum = sumGiven.getText();
            String choice = sumChoiceBox.getValue().toString();
            try{
                double id = Double.parseDouble(givenSum);
            } catch (NumberFormatException e) {
                customerMessage.setText("Sum Not Valid");
                return;
            }
            body.put("sum",givenSum);
            body.put("threshold",choice);
            setTable("customer/customersBySum",body);
        }
    }

    public void onResetClick(ActionEvent event) {
        addAllCustomersBack();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerIdColumn.setCellValueFactory(new MapValueFactory<>("id"));
        fullNameColumn.setCellValueFactory(new MapValueFactory<>("fullName"));
        balanceColumn.setCellValueFactory(new MapValueFactory<>("balance"));
        totalBillsColumn.setCellValueFactory(new MapValueFactory<>("totalBills"));
        unpaidBillsColumn.setCellValueFactory(new MapValueFactory<>("unpaidBills"));
        HashMap body = new HashMap<>();
        body.put("Dummy","Dummy");
        model.sendRequest("customer/all",body);
        Request response = null;
        try {
            response = model.getResponseToRequest();
        }catch (Exception e){
            e.printStackTrace();
        }

        if(response.getHeader().contains("failure")){
            customerMessage.setText("No customers were found.");
        }

        else{
            Map billsMap = response.getBody();
            ObservableList<Map<String, Object>> items =
                    FXCollections.observableArrayList();


            for(Object value : billsMap.values()){
                if(value instanceof String){
                    String strValue = value.toString();
                    Map<String, Object> item = new HashMap<>();
                    item.put("id",strValue.split(",")[0]);
                    item.put("fullName",strValue.split(",")[1]);
                    item.put("balance",strValue.split(",")[2]);
                    item.put("totalBills",strValue.split(",")[3]);
                    item.put("unpaidBills",strValue.split(",")[4]);
                    items.add(item);
                }
            }
            customerTable.getItems().addAll(items);
        }
        sumChoiceBox.getItems().addAll(choices);
    }

    public void addAllCustomersBack(){
        customerTable.getItems().clear();
        HashMap body = new HashMap<>();
        body.put("Dummy","Dummy");
        model.sendRequest("customer/all",body);
        Request response = null;
        try {
            response = model.getResponseToRequest();
        }catch (Exception e){
            e.printStackTrace();
        }

        if(response.getHeader().contains("failure")){
            customerMessage.setText("No Customers were found.");
        }

        else{
            Map billsMap = response.getBody();
            ObservableList<Map<String, Object>> items =
                    FXCollections.observableArrayList();


            for(Object value : billsMap.values()){
                if(value instanceof String){
                    String strValue = value.toString();
                    Map<String, Object> item = new HashMap<>();
                    item.put("id",strValue.split(",")[0]);
                    item.put("fullName",strValue.split(",")[1]);
                    item.put("balance",strValue.split(",")[2]);
                    item.put("totalBills",strValue.split(",")[3]);
                    item.put("unpaidBills",strValue.split(",")[4]);
                    items.add(item);
                }
            }
            customerTable.getItems().addAll(items);
        }
    }
    public void setTable(String header,HashMap body){
        model.sendRequest(header,body);
        Request response = null;
        try {
            response = model.getResponseToRequest();
        }catch (Exception e){
            e.printStackTrace();
        }

        if(response.getHeader().contains("failure")){
            customerMessage.setText("No customers were found.");
        }

        else{
            Map billsMap = response.getBody();
            ObservableList<Map<String, Object>> items =
                    FXCollections.observableArrayList();


            for(Object value : billsMap.values()){
                if(value instanceof String){
                    String strValue = value.toString();
                    Map<String, Object> item = new HashMap<>();
                    item.put("id",strValue.split(",")[0]);
                    item.put("fullName",strValue.split(",")[1]);
                    item.put("balance",strValue.split(",")[2]);
                    item.put("totalBills",strValue.split(",")[3]);
                    item.put("unpaidBills",strValue.split(",")[4]);
                    items.add(item);
                }
            }
            customerTable.getItems().addAll(items);
        }
    }

}
