module org.zeki.virtualtechseller {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.zeki.virtualtechseller to javafx.fxml;
    exports org.zeki.virtualtechseller;
    exports org.zeki.virtualtechseller.controller;
    opens org.zeki.virtualtechseller.controller to javafx.fxml;
}