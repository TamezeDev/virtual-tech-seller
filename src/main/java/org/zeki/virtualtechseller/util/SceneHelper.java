package org.zeki.virtualtechseller.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.zeki.virtualtechseller.Main;
import org.zeki.virtualtechseller.controller.client.DetailProductController;
import org.zeki.virtualtechseller.controller.client.DetailPurchaseController;
import org.zeki.virtualtechseller.model.product.Product;
import org.zeki.virtualtechseller.model.product.Sale;

import java.io.IOException;
import java.net.URL;

public class SceneHelper {
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
        }

    }

    public static void goToSaleDetails(Node node, String viewPath, Sale sale) {
        try {
            URL resource = Main.class.getResource(viewPath);

            if (resource == null) {
                AlertHelper.showErrorAlert("Error de escena", "No se encontró la ruta de la escena");
                return;
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            DetailPurchaseController detailPurchaseController = loader.getController();
            detailPurchaseController.setCurrentSale(sale);

            Scene scene = new Scene(root);
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            AlertHelper.showErrorAlert("Error de escena", "Error en la carga de la escena");
            e.printStackTrace();
        }
    }

    public static void goToProductDetails(Node node, String viewPath, Product product) {
        try {
            URL resource = Main.class.getResource(viewPath);

            if (resource == null) {
                AlertHelper.showErrorAlert("Error de escena", "No se encontró la ruta de la escena");
                return;
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            DetailProductController detailProductController = loader.getController();
            detailProductController.setCurrentProduct(product);

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
