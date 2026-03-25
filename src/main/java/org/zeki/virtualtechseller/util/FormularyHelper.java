package org.zeki.virtualtechseller.util;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import java.util.List;


public class FormularyHelper {
    private FormularyHelper() {
    }

    public static void clearFields(List<TextField> fieldList) {
        fieldList.forEach(TextInputControl::clear);
    }

    public static boolean emptyFields(List<TextField> fieldList, Label label) {
        for (TextField textField : fieldList) {
            if (textField.getText().isBlank()) {
                label.setText("Debe completar todos los campos");
                return true;
            }
        }
        return false;
    }
}
