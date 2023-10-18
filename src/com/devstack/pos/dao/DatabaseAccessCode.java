package com.devstack.pos.dao;

import com.devstack.pos.db.DBConnection;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.dto.UserDto;
import com.devstack.pos.util.PasswordManager;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccessCode {
    //============User Management=====================

    //Create User - Called in the SignupFormController
    public static boolean createUser(String email,String password) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO user VALUES (?,?)";
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1,email);
        preparedStatement.setString(2, PasswordManager.encryptPassword(password));

        return preparedStatement.executeUpdate()>0;
    }

    //Find User - Called in the LoginFormController
    public static UserDto findUSer(String email) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM user WHERE email=?";
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1,email);
        ResultSet set = preparedStatement.executeQuery();

        if (set.next()){
            return new UserDto(
                    set.getString(1),
                    set.getString(2)
            );
        }
        return null;
    }

    //============Customer Management=================

    //Create Customer - Called in the CustomerFormController
    public static boolean createCustomer(String email, String name, String contact, double salary) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO customer VALUES (?,?,?,?,?)";
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1,email);
        preparedStatement.setString(2,name);
        preparedStatement.setString(3,"Active");
        preparedStatement.setString(4,contact);
        preparedStatement.setDouble(5, salary);

        return preparedStatement.executeUpdate()>0;
    }

    //Update Customer - Called in the CustomerFormController
    public static boolean updateCustomer(String email, String name, String contact, double salary) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE customer SET name=?,contact=?,salary=? WHERE email=?";
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1,name);
        preparedStatement.setString(2,contact);
        preparedStatement.setDouble(3, salary);
        preparedStatement.setString(4,email);
        return preparedStatement.executeUpdate()>0;
    }

    //Find Customer - Called in the CustomerFormController
    public static CustomerDto findCustomer(String email) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM customer WHERE email=?";
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1,email);
        ResultSet set = preparedStatement.executeQuery();
        if (set.next()){
            return new CustomerDto(
                    set.getString(1),
                    set.getString(2),
                    set.getString(3),
                    set.getString(4),
                    set.getDouble(5)
            );
        }
        return null;
    }

    //Delete Customer - Called in the CustomerFormController
    public static boolean deleteCustomer(String email) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM customer WHERE email=?";
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1,email);
        return preparedStatement.executeUpdate()>0;
    }

    //View Customers - Called in the CustomerFormController
    public static List<CustomerDto> findAllCustomers() throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM customer";
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet set = preparedStatement.executeQuery();
        List<CustomerDto> dtos = new ArrayList<>();
        while (set.next()){
            dtos.add(new CustomerDto(
                    set.getString(1),
                    set.getString(2),
                    set.getString(3),
                    set.getString(4),
                    set.getDouble(5)
            ));
        }
        return dtos;
    }

    //Search Customers - Called in the CustomerFormController
    public static List<CustomerDto> searchCustomers(String searchText) throws ClassNotFoundException, SQLException {

        searchText = "%"+searchText+"%";
        String sql = "SELECT * FROM customer WHERE email LIKE ? || name LIKE ?";
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1,searchText);
        preparedStatement.setString(2,searchText);
        ResultSet set = preparedStatement.executeQuery();
        List<CustomerDto> dtos = new ArrayList<>();
        while (set.next()){
            dtos.add(new CustomerDto(
                    set.getString(1),
                    set.getString(2),
                    set.getString(3),
                    set.getString(4),
                    set.getDouble(5)
            ));
        }
        return dtos;
    }
}
