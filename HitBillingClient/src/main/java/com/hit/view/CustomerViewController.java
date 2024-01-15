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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CustomerViewController implements SceneSwitcher, Initializable {
    Model model = ModelSingleton.getInstance();
    @FXML
    public TextField billIDInput;
    @FXML
    public TextField customerIdInput;
    @FXML
    public TableView searchResultTable;
    @FXML
    public TableColumn<Map,String> billIDColumn;
    @FXML
    public TableColumn<Map,String> billDateColumn;
    @FXML
    public TableColumn<Map,String> billSumColumn;
    @FXML
    public Text customerMessage;
    private Stage stage;
    private Scene scene;
    private Parent root;

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

    public void payBillButtonClick(ActionEvent event) throws IOException{
        Map selectedRow = (Map) searchResultTable.getSelectionModel().getSelectedItem();
        if(selectedRow == null){
            customerMessage.setText("No bill was picked to pay");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pay-bill-view.fxml"));
        root = loader.load();
        PayBillController payBillController = loader.getController();
        payBillController.setDisplayBill(selectedRow);
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void onFindByCustomerIdClick(ActionEvent event) {
        searchResultTable.getItems().clear();
        String givenId = customerIdInput.getText();
        HashMap body = new HashMap<>();
        try{
            long id = Long.parseLong(givenId);
        } catch (NumberFormatException e) {
            customerMessage.setText("ID Not Valid");
            return;
        }
        body.put("id",givenId);
        setTable("bill/billsById",body);
    }

    public void onFindBillClick(ActionEvent event) {
        searchResultTable.getItems().clear();
        String givenId = billIDInput.getText();
        HashMap body = new HashMap<>();
        try{
            long id = Long.parseLong(givenId);
        } catch (NumberFormatException e) {
            customerMessage.setText("ID Not Valid");
            return;
        }
        body.put("id",givenId);
        setTable("bill/Single",body);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        billIDColumn.setCellValueFactory(new MapValueFactory<>("id"));
        billDateColumn.setCellValueFactory(new MapValueFactory<>("date"));
        billSumColumn.setCellValueFactory(new MapValueFactory<>("sum"));

    }

    public void setTable(String header,HashMap body){
        searchResultTable.getItems().clear();
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
                    items.add(item);
                }
            }
            searchResultTable.getItems().addAll(items);
        }
    }
}
