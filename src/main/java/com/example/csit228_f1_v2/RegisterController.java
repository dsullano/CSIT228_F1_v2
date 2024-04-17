package com.example.csit228_f1_v2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class RegisterController {
    public AnchorPane pnRegister;
    public AnchorPane pnMain;
    public TextField tfUsername;
    public TextField tfTmpPass;
    public PasswordField pfUserPass;
    public Label lblFeedback;

    @FXML
    protected void onRegisterClick() {

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "INSERT INTO tblusers (uname, password) VALUES (?, ?)"
             )) {
            String username = tfUsername.getText();
            String userpassword = pfUserPass.getText();
            boolean usernameAlreadyExists = false;

            String query = "SELECT * FROM tblusers";
            ResultSet res = statement.executeQuery(query);

            while(res.next()) {
                int id = res.getInt("id");
                String name = res.getString("uname");

                if(name.equals(username)) {
                    usernameAlreadyExists = true;
                    break;
                }
            }

            if(usernameAlreadyExists) {
                lblFeedback.setText("Username already exists!");
                lblFeedback.setTextFill(Color.RED);
            } else {
                statement.setString(1, username);
                statement.setString(2, userpassword);

                statement.executeUpdate();
                lblFeedback.setText("Registered successfully!");
                lblFeedback.setTextFill(Color.BLUE);
            }

            tfUsername.setText("");
            pfUserPass.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void onToLogInClick() throws IOException{
        Parent login = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("login-view.fxml")));
        AnchorPane p = (AnchorPane) pnRegister.getParent();
        p.getChildren().remove(pnRegister);
        p.getChildren().add(login);
    }

    public void onShowPassword(MouseEvent mouseEvent) {
        tfTmpPass.setText(pfUserPass.getText());
        tfTmpPass.setVisible(true);
        pfUserPass.setVisible(false);
    }

    public void onUnshowPassword(MouseEvent mouseEvent) {
        pfUserPass.setVisible(true);
        pfUserPass.setText(tfTmpPass.getText());
        tfTmpPass.setVisible(false);
    }
}