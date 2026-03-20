package org.zeki.virtualtechseller.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InitStage extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginViewController.class.getResource("/fxml/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200 , 800);
        stage.setTitle("Virtual Tech Seller");
        stage.setScene(scene);
        stage.show();
    }
}
