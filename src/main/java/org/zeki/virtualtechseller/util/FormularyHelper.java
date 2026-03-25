package org.zeki.virtualtechseller.util;

import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import java.util.List;


public class FormularyHelper {
    private FormularyHelper() {
    }

    public static void clearFields(List<TextField> fieldList) {
        fieldList.forEach(TextInputControl::clear);
    }

    public static boolean emptyFields(List<TextField> fieldList) {
        for (TextField textField : fieldList) {
            if (textField.getText().isBlank()) {
                return true;
            }
        }
        return false;
    }
}
