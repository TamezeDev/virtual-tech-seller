package org.zeki.virtualtechseller.util;

import javafx.scene.control.Alert;

public class AlertHelper {
    private AlertHelper() {
    }

    public static void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    public static void showDBConnectAlert() {
        showErrorAlert(
                "Conexión con servidor",
                "No se ha podido establecer la conexión con el servidor"
        );
    }

    public static void showSQLAlert() {
        showErrorAlert(
                "Error SQL",
                "Se produjo un error en la consulta de datos"
        );
    }
}
