package com.example.csit228_f1_v2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class HelloController {
    public GridPane pnLogin;
    public AnchorPane pnMain;
    public VBox pnHome;
    public Label lblFeedback;
    public TextField tfUsername, tfTmpPass;
    public PasswordField pfUserPass;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    protected void onLogInClick() throws IOException {
        String username = tfUsername.getText();
        String userpassword = pfUserPass.getText();
        boolean usernameAlreadyExists = false;
        boolean correctPassword = false;

        //read existing data
        try (Connection c = MySQLConnection.getConnection();
             Statement statement = c.createStatement()) {
            String query = "SELECT * FROM tblusers";
            ResultSet res = statement.executeQuery(query);

            while(res.next()) {
                int id = res.getInt("id");
                String uname = res.getString("uname");
                String upass = res.getString("password");

                //checks if username exists
                if(uname.equals(username)) {
                    usernameAlreadyExists = true;

                    //checks if password is correct
                    if(upass.equals(userpassword)) {
                        correctPassword = true;
                        break;
                    }
                }
            }

            if(usernameAlreadyExists) {
                //if successful login info
                if(correctPassword) {
                    Parent homeview = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("homepage.fxml")));
                    AnchorPane p = (AnchorPane) pnLogin.getParent();
                    p.getChildren().remove(pnLogin);
                    p.getChildren().add(homeview);
                } else {
                    lblFeedback.setText("Incorrect Password!");
                    lblFeedback.setTextFill(Color.RED);
                }
            } else {
                //if username does not exist
                lblFeedback.setText("Username not found!");
                lblFeedback.setTextFill(Color.RED);
            }

            //set both fields into no characters after button click
            tfUsername.setText("");
            pfUserPass.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void onToRegisterClick() throws IOException {
        Parent register = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("register-view.fxml")));
        AnchorPane p = (AnchorPane) pnLogin.getParent();
        p.getChildren().remove(pnLogin);
        p.getChildren().add(register);
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