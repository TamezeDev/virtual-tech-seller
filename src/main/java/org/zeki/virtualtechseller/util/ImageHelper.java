package org.zeki.virtualtechseller.util;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public final class ImageHelper {

    public static File setImage(MouseEvent event, ImageView imageView) {
        // OPEN STAGE TO SELECT PATH PHOTO
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar foto");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.jpeg", "*.gif", "*.JPEG"));
        Node parent = (Node) event.getSource();
        Stage stage = (Stage) parent.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        // SHOW PHOTO AND GET URL
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
        }
        return file;
    }

    public static boolean getUrlCopyPhoto(File file) {
        // COPY FILE TO GET IMAGE FOR DB PRODUCT(ONLY IN THIS PROJECT, REALLY WE HAVE TO UPDATE PHOTO TO SERVER)
        if (file != null) {
            try {
                String cleanName = file.getName().replace(" ", "_");
                Path destinationFolder = Paths.get("src/main/resources/img/products/" + cleanName);
                Files.copy(file.toPath(), destinationFolder, StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }
}
