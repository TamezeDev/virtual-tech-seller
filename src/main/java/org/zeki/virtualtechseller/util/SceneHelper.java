package org.zeki.virtualtechseller.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.zeki.virtualtechseller.Main;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

public final class SceneHelper {
    private SceneHelper() {
    }

    public static void changeScene(Node node, String viewPath) {
        changeScene(node, viewPath, null);
    }

    public static <C> void changeScene(Node node, String viewPath, Consumer<C> controllerAction) {
        // GLOBAL FUNCTION TO CHANGE AMONG SCENES
        try {
            URL resource = Main.class.getResource(viewPath);
            // CHECK URL
            if (resource == null) {
                AlertHelper.showErrorAlert("Error de escena", "No se encontró la ruta de la escena");
                return;
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();
            // CHECK CONTROLLER TO GET DATA
            if (controllerAction != null) {
                C controller = loader.getController();
                controllerAction.accept(controller);
            }
            // SET SCENE
            Scene scene = new Scene(root);
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            AlertHelper.showErrorAlert("Error de escena", "Error en la carga de la escena");
        }
    }
}
