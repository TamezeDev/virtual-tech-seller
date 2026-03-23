package org.zeki.virtualtechseller.controller.scene.global;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InitStage extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginViewController.class.getResource("/fxml/global/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200 , 1024);
        stage.setTitle("Virtual Tech Seller");
        stage.setScene(scene);
        stage.show();
    }
}
