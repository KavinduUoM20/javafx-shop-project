package com.devstack.pos.controller;

import com.devstack.pos.dao.DatabaseAccessCode;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.view.tm.CustomerTm;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class CustomerFormController {

    public TableColumn colID;
    public TableColumn colEmail;
    public TableColumn colName;
    public TableColumn colContact;
    public TableColumn colSalary;
    public TableColumn colOption;
    public TableView<CustomerTm> tbl;
    public TextField txtSearch;
    public JFXButton btnSaveCustomer;
    public JFXTextField txtSalary;
    public JFXTextField txtContact;
    public JFXTextField txtName;
    public JFXTextField txtEmail;
    public AnchorPane context;

    private String searchText = "";

    public void initialize() throws SQLException, ClassNotFoundException {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("actionButton"));
        loadAllCustomers(searchText);

        tbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                setData(newValue);
            }
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText=newValue;
            try {
                loadAllCustomers(searchText);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void setData(CustomerTm newValue) {
        txtEmail.setEditable(false);
        btnSaveCustomer.setText("Update Customer");
        txtEmail.setText(newValue.getEmail());
        txtName.setText(newValue.getName());
        txtContact.setText(newValue.getContact());
        txtSalary.setText(String.valueOf(newValue.getSalary()));
    }

    private void loadAllCustomers(String searchText) throws SQLException, ClassNotFoundException {
        ObservableList<CustomerTm> observableList = FXCollections.observableArrayList();
        int counter =1;
        for (CustomerDto dto:searchText.length()>0?DatabaseAccessCode.searchCustomers(searchText):DatabaseAccessCode.findAllCustomers()){
            Button btn = new Button("Delete");
            CustomerTm customerTm = new CustomerTm(
                    counter,dto.getEmail(),dto.getName(), dto.getContact(), dto.getSalary(), btn
            );
            observableList.add(customerTm);
            counter++;

            btn.setOnAction((e)->{
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure?",ButtonType.YES,ButtonType.NO);
                    Optional<ButtonType> selectedButtonType = alert.showAndWait();
                    if (selectedButtonType.get().equals(ButtonType.YES)){
                        if(DatabaseAccessCode.deleteCustomer(dto.getEmail())){
                            new Alert(Alert.AlertType.CONFIRMATION,"Customer Deleted!").show();
                            loadAllCustomers(searchText);
                        }else {
                            new Alert(Alert.AlertType.WARNING,"Try Again!").show();
                        }
                    }
                } catch (ClassNotFoundException | SQLException exception){
                    exception.printStackTrace();
                    new Alert(Alert.AlertType.WARNING,exception.getMessage()).show();
                }
            });
        }
        tbl.setItems(observableList);
    }

    public void btnSaveCustomerOnAction(ActionEvent actionEvent) {
        try {
            if (btnSaveCustomer.getText().equals("Save Customer")){
                if(DatabaseAccessCode.createCustomer(txtEmail.getText(),txtName.getText(),txtContact.getText(),Double.parseDouble(txtSalary.getText()))){
                    new Alert(Alert.AlertType.CONFIRMATION,"Customer Registered!").show();
                    loadAllCustomers(searchText);
                    clearFields();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Try Again!").show();
                }
            }else{
                if(DatabaseAccessCode.updateCustomer(txtEmail.getText(),txtName.getText(),txtContact.getText(),Double.parseDouble(txtSalary.getText()))){
                    new Alert(Alert.AlertType.CONFIRMATION,"Customer Updated!").show();
                    loadAllCustomers(searchText);
                    clearFields();

                }else {
                    new Alert(Alert.AlertType.WARNING,"Try Again!").show();
                }
            }

        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING,e.getMessage()).show();
        }
    }

    public void btnBackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    public void btnManageLoyaltyCardsOnAction(ActionEvent actionEvent) {
    }

    public void btnNewCustomerOnAction(ActionEvent actionEvent) {
        //txtEmail Set Editable
        txtEmail.setEditable(true);
        btnSaveCustomer.setText("Save Customer");
        clearFields();
    }


    private void clearFields() {
        txtEmail.clear();
        txtName.clear();
        txtContact.clear();
        txtSalary.clear();
    }

    private void setUI(String url) throws IOException {
        Stage stage = (Stage)context.getScene().getWindow();
        stage.centerOnScreen();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/"+url+".fxml")))
        );

    }

}
