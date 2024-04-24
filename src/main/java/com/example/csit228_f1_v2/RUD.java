package com.example.csit228_f1_v2;

import javafx.scene.control.Alert;

import java.sql.*;

public class RUD {

    public static void update(String id, String newName, String newEmail, String newAddress) {
        String query = "UPDATE accounts SET";
        boolean hasUpdate = false;

        if (newName != null && !newName.isEmpty()) {
            query += " name = '" + newName + "'";
            hasUpdate = true;
        }

        if (newEmail != null && !newEmail.isEmpty()) {
            query += (hasUpdate ? "," : "") + " email = '" + newEmail + "'";
            hasUpdate = true;
        }

        if (newAddress != null && !newAddress.isEmpty()) {
            query += (hasUpdate ? "," : "") + " address = '" + newAddress + "'";
        }

        query += " WHERE id = ?";

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(query)) {
            statement.setInt(1, Integer.parseInt(id));
            int rows = statement.executeUpdate();

            if (rows > 0) {
                Helper.showAlert(Alert.AlertType.INFORMATION, "Update Successful", "Record Updated", "Record with ID " + id + " has been updated.");
            } else {
                Helper.showAlert(Alert.AlertType.WARNING, "Update Failed", "No Record Updated", "No record found with ID " + id);
            }
        } catch (SQLException e) {
            Helper.showAlert(Alert.AlertType.ERROR, "Error", "Update Error", "An error occurred while updating record: " + e.getMessage());
        }
    }

    public static void insert(String username,String password){
        System.out.println(username);
        System.out.println(password);
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "INSERT INTO users (username, password) VALUES (? , ?)"
             )){
            statement.setString(1, username);
            statement.setString(2, password);
            int rows = statement.executeUpdate();
            System.out.println("Rows Inserted: " + rows);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public static boolean delete(String text){

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "DELETE FROM users WHERE user_id = ?"
             )) {
            statement.setInt(1, Integer.parseInt(text));
            int rows = statement.executeUpdate();
            if (rows == 0) {
                Helper.showAlert(Alert.AlertType.WARNING, "Delete Failed", "No Record Deleted", "No record found with ID " + text);
                return false;
            } else {
                Helper.showAlert(Alert.AlertType.INFORMATION, "Delete Successful", "Record Deleted", "Record with ID " + text + " has been deleted.");
                return true;
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

    }

    public static void read(String id) {
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("SELECT name, email, address FROM accounts WHERE id = ?")) {
            statement.setInt(1, Integer.parseInt(id));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");

                // Show the details in a pop-up dialog
                showDetailsDialog(name, email, address);
            } else {
                Helper.showAlert(Alert.AlertType.WARNING, "Search Failed", "No Record Found", "No record found with ID " + id);
            }
        } catch (SQLException e) {
            Helper.showAlert(Alert.AlertType.ERROR, "Error", "Search Error", "An error occurred while searching record: " + e.getMessage());
        }
    }

    private static void showDetailsDialog(String name, String email, String address) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Details");
        alert.setHeaderText(null);
        alert.setContentText("Name: " + name + "\nEmail: " + email + "\nAddress: " + address);
        alert.showAndWait();
    }

}