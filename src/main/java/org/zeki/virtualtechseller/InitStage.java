package org.zeki.virtualtechseller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.zeki.virtualtechseller.controller.scene.global.LoginController;

import java.io.IOException;

public class InitStage extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/fxml/global/start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200 , 800);
        stage.setResizable(false);
        stage.setTitle("Virtual Tech Seller");
        stage.setScene(scene);
        stage.show();
    }
}
