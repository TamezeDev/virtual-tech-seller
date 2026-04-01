package org.zeki.virtualtechseller.util;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import org.zeki.virtualtechseller.model.user.UserRole;

import java.util.List;
import java.util.regex.Pattern;


public final class FormularyHelper {
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

    public static boolean matchPassword(String passA, String passB) {
        return passA.equals(passB);
    }

    public static boolean phoneFormatValid(String phone) {
        return Pattern.matches("^\\+?[1-9][0-9]{7,14}$", phone);
    }

    public static UserRole getSelectedRole(String selectedRole) {
        if (selectedRole == null) return UserRole.CLIENT;
        switch (selectedRole) {
            case "Administrador" -> {
                return UserRole.ADMIN;
            }
            case "Moderador" -> {
                return UserRole.MODERATOR;
            }
            default -> {
                return UserRole.CLIENT;
            }
        }
    }

    public static boolean emailFormatValid(String email) {
        // CHECK @
        String[] parts = email.split("@", 2);
        if (parts.length != 2) {
            return false;
        }
        String local = parts[0];
        String domain = parts[1];
        // LOCAL PART CONTROL
        if (local.isEmpty() || domain.isEmpty() || local.startsWith(".") || local.endsWith(".")
                || local.contains("..") || !domain.contains(".")) {
            return false;
        }
        String[] domainParts = domain.split("\\.", 2);
        // DOMAIN PART CONTROL
        if (domainParts.length != 2) {
            return false;
        }
        String domainName = domainParts[0];
        String extension = domainParts[1];

        if (domainName.isEmpty() || extension.length() < 2 || domain.startsWith(".")
                || domain.endsWith(".") || domain.contains("..")) {
            return false;
        }

        return true;
    }

    public static boolean passFormatValid(String pass) {

        boolean number = false;
        boolean lower = false;
        boolean upper = false;
        boolean symbol = false;

        // CHECK BOOLEANS
        if (pass.length() >= 8) {
            char[] parsePass = pass.toCharArray();
            for (char ascii : parsePass) {
                if (ascii > 64 && ascii < 91) {
                    upper = true;
                } else if (ascii > 96 && ascii < 123) {
                    lower = true;
                } else if (ascii > 47 && ascii < 58) {
                    number = true;
                } else if ((ascii > 32 && ascii < 48) || (ascii > 57 && ascii < 65)
                        || (ascii > 90 && ascii < 97) || (ascii > 122 && ascii < 127)) {
                    symbol = true;
                }
            }
        }
        return (number && lower && upper && symbol);
    }
}
