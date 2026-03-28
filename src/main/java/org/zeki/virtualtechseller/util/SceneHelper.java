package org.zeki.virtualtechseller.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.zeki.virtualtechseller.Main;

import java.io.IOException;
import java.net.URL;

public final class SceneHelper {
    private SceneHelper() {
    }

    public static void changeScene(Node node, String viewPath) {
        // GLOBAL FUNCTION TO CHANGE AMONG SCENES
        try {
            URL resource = Main.class.getResource(viewPath);

            if (resource == null) {
                AlertHelper.showErrorAlert("Error de escena", "No se encontró la ruta de la escena");
                return;
            }

            Parent root = FXMLLoader.load(resource);
            Scene scene = new Scene(root);
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            AlertHelper.showErrorAlert("Error de escena", "Error en la carga de la escena");
            e.printStackTrace();
        }

    }
}
