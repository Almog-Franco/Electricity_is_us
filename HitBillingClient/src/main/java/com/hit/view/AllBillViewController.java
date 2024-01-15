package com.hit.view;

import com.hit.model.Model;
import com.hit.model.ModelSingleton;
import com.hit.model.Request;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AllBillViewController implements SceneSwitcher, Initializable {

    @FXML
    public RadioButton idRadio;
    @FXML
    public RadioButton sumRadio;
    @FXML
    public ChoiceBox sumChoiceBox;
    private Stage stage;
    private Scene scene;
    private Parent root;
    Model model = ModelSingleton.getInstance();
    @FXML
    private Text customerMessage;
    @FXML
    private TableView billTable;
    @FXML
    private TableColumn<Map,String> billIdColumn;
    @FXML
    private TableColumn<Map,String> billDateColumn;
    @FXML
    private TableColumn<Map,String> billOwnerIdColumn;
    @FXML
    private TableColumn<Map,String> billSumColumn;
    @FXML
    private TextField idFilter;
    @FXML
    private TextField sumFilter;
    String[] choices = {"greater than","lower than","equals"};



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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        billIdColumn.setCellValueFactory(new MapValueFactory<>("id"));
        billDateColumn.setCellValueFactory(new MapValueFactory<>("date"));
        billOwnerIdColumn.setCellValueFactory(new MapValueFactory<>("ownerId"));
        billSumColumn.setCellValueFactory(new MapValueFactory<>("sum"));
        HashMap body = new HashMap<>();
        body.put("Dummy","Dummy");
        model.sendRequest("bill/all",body);
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
            Map billsMap = response.getBody();
            ObservableList<Map<String, Object>> items =
                    FXCollections.observableArrayList();


            for(Object value : billsMap.values()){
                if(value instanceof String){
                    String strValue = value.toString();
                    Map<String, Object> item = new HashMap<>();
                    item.put("id",strValue.split(",")[0]);
                    item.put("date",strValue.split(",")[1]);
                    item.put("sum",strValue.split(",")[2]);
                    item.put("ownerId",strValue.split(",")[3]);
                    items.add(item);
                }
            }
            billTable.getItems().addAll(items);
        }
        sumChoiceBox.getItems().addAll(choices);


    }

    public void onFilterButtonClick(ActionEvent event) {
        billTable.getItems().clear();
        HashMap body = new HashMap<>();
        if(idRadio.isSelected()){
            String givenId = idFilter.getText();
            try{
                long id = Long.parseLong(givenId);
            } catch (NumberFormatException e) {
                customerMessage.setText("ID Not Valid");
                return;
            }
            body.put("id",givenId);
            setTable("bill/billsById",body);
        }
        if(sumRadio.isSelected()){
            String givenSum = sumFilter.getText();
            String choice = sumChoiceBox.getValue().toString();
            try{
                double id = Double.parseDouble(givenSum);
            } catch (NumberFormatException e) {
                customerMessage.setText("Sum Not Valid");
                return;
            }
            body.put("sum",givenSum);
            body.put("threshold",choice);
            setTable("bill/billsBySum",body);
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
            customerMessage.setText("No bills were found.");
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
                    item.put("date",strValue.split(",")[1]);
                    item.put("sum",strValue.split(",")[2]);
                    item.put("ownerId",strValue.split(",")[3]);
                    items.add(item);
                }
            }
            billTable.getItems().addAll(items);
    }
    }

    public void addAllBillsBack(){
        billTable.getItems().clear();
        HashMap body = new HashMap<>();
        body.put("Dummy","Dummy");
        model.sendRequest("bill/all",body);
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
            Map billsMap = response.getBody();
            ObservableList<Map<String, Object>> items =
                    FXCollections.observableArrayList();


            for(Object value : billsMap.values()){
                if(value instanceof String){
                    String strValue = value.toString();
                    Map<String, Object> item = new HashMap<>();
                    item.put("id",strValue.split(",")[0]);
                    item.put("date",strValue.split(",")[1]);
                    item.put("sum",strValue.split(",")[2]);
                    item.put("ownerId",strValue.split(",")[3]);
                    items.add(item);
                }
            }
            billTable.getItems().addAll(items);
        }
        sumChoiceBox.getItems().addAll(choices);
    }

    public void onResetClick(ActionEvent event) {
        addAllBillsBack();
    }
}
