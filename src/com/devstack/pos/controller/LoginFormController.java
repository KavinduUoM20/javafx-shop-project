package com.devstack.pos.controller;

import com.devstack.pos.dao.DatabaseAccessCode;
import com.devstack.pos.dto.UserDto;
import com.devstack.pos.util.PasswordManager;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginFormController {

    public JFXTextField txtEmail;
    public AnchorPane context;
    public JFXPasswordField txtPassword;

    public void btnLoginOnAction(ActionEvent actionEvent) {
        try {
            UserDto ud = DatabaseAccessCode.findUSer(txtEmail.getText());

            if (ud!=null){
                if (PasswordManager.checkPassword(txtPassword.getText(),ud.getPassword())){
                    setUI("DashboardForm");
                }else {
                    new Alert(Alert.AlertType.WARNING,"Check Your Password & Try Again!").show();
                }
            }else {
                new Alert(Alert.AlertType.WARNING,"User Email Not Found!").show();
            }

        }catch (ClassNotFoundException | SQLException | IOException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING,e.getMessage()).show();
        }
    }

    public void btnCreateAnAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUI("SignupForm");
    }

    private void setUI(String url) throws IOException {
        Stage stage = (Stage)context.getScene().getWindow();
        stage.centerOnScreen();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/"+url+".fxml")))
        );

    }
}
