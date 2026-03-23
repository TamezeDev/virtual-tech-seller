module org.zeki.virtualtechseller {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.zeki.virtualtechseller to javafx.fxml;
    exports org.zeki.virtualtechseller;
    exports org.zeki.virtualtechseller.controller;
    opens org.zeki.virtualtechseller.controller to javafx.fxml;
    exports org.zeki.virtualtechseller.controller.scene.global;
    opens org.zeki.virtualtechseller.controller.scene.global to javafx.fxml;
    exports org.zeki.virtualtechseller.controller.scene.admin;
    opens org.zeki.virtualtechseller.controller.scene.admin to javafx.fxml;
    exports org.zeki.virtualtechseller.controller.scene.moderator;
    opens org.zeki.virtualtechseller.controller.scene.moderator to javafx.fxml;
}