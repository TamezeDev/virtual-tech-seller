package org.zeki.virtualtechseller.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.zeki.virtualtechseller.Main;
import org.zeki.virtualtechseller.controller.global.LoginController;
import org.zeki.virtualtechseller.util.ViewPath;

import java.io.IOException;
import java.util.Objects;

public class MainStageApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource(ViewPath.START_VIEW));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 768);
        String iconUrl = "/img/system/main_icon.png";

        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResource(iconUrl)).toExternalForm()));
        stage.setTitle("Virtual Tech Seller");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
