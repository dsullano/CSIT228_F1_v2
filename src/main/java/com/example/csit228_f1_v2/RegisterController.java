package com.example.csit228_f1_v2;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.*;
import java.util.Optional;
import javafx.scene.control.Alert;

import java.util.concurrent.atomic.AtomicReference;

public class RegisterController {
    public TextField textFieldName;
    public TextField textFieldEmail;
    public TextField textFieldAddress;
    public TextField textFieldUsername;

    public TextField textFieldId;
    public PasswordField textFieldPassword;

    @FXML
    protected void btnUpdateClick() {
        Optional<String> result = Helper.updateMessage("Update Record", "Enter ID to Update", "Please enter the ID:");

        AtomicReference<Optional<String>> newName = new AtomicReference<>(Optional.empty());
        AtomicReference<Optional<String>> newEmail = new AtomicReference<>(Optional.empty());
        AtomicReference<Optional<String>> newAddress = new AtomicReference<>(Optional.empty());

        result.ifPresent(id -> {
            if (!id.isEmpty()) {
                newName.set(Helper.inputMessage("Update Name", "Enter New Name", "Enter the new name:"));
                newEmail.set(Helper.inputMessage("Update Email", "Enter New Email", "Enter the new email:"));
                newAddress.set(Helper.inputMessage("Update Address", "Enter New Address", "Enter the new address:"));

                newName.get().ifPresent(name -> {
                    newEmail.get().ifPresent(email -> {
                        newAddress.get().ifPresent(address -> {
                            RUD.update(id, name, email, address);
                        });
                    });
                });
            } else {
                Helper.alert(Alert.AlertType.WARNING, "Updating Failed", "ID is Empty", "Please enter a valid ID.");
            }
        });
    }


    @FXML
    protected void btnReadClick() {
        Optional<String> result = Helper.inputMessage("Read Record", "Enter ID to Read", "Please enter the ID:");

        result.ifPresent(id -> {
            if (!id.isEmpty()) {
                RUD.read(id);
            } else {
                Helper.alert(Alert.AlertType.WARNING, "Reading Failed", "ID is Empty", "Please enter a valid ID.");
            }
        });
    }

    @FXML
    protected void btnDeleteClick() {
        Optional<String> result = Helper.inputMessage("Delete Record", "Enter ID to Delete", "Please enter the ID:");

        result.ifPresent(id -> {
            if (!id.isEmpty()) {
                if(RUD.delete(id))
                    Helper.alert(Alert.AlertType.INFORMATION, "Delete Successful", "Record Deleted", "Record with ID " + id + " has been deleted.");
            } else {
                Helper.alert(Alert.AlertType.WARNING, "Delete Failed", "ID is Empty", "Please enter a valid ID.");
            }
        });
    }

    @FXML
    protected void btnRegisterClick() {
        if(textFieldEmail.getLength() == 0){
            System.out.println("Please Enter Details");
            return;
        }
        boolean success = false;
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "INSERT INTO accounts (name, email, address) VALUES (?, ?, ?)"
             )) {
            String name = textFieldName.getText();
            String email = textFieldEmail.getText();
            String address = textFieldAddress.getText();
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, address);
            int rows = statement.executeUpdate();
            if (rows != 0) {
                success = true;
            }
            System.out.println("Rows Inserted: " + rows);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (success) {
            String username = textFieldUsername.getText();
            String password = textFieldPassword.getText();
            String hashPass = String.valueOf(password.hashCode());
            RUD.insert(username, hashPass);
        }
    }

}