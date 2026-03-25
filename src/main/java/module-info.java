module org.zeki.virtualtechseller {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires static lombok;
    requires java.sql;


    opens org.zeki.virtualtechseller to javafx.fxml;
    exports org.zeki.virtualtechseller;
    exports org.zeki.virtualtechseller.controller.scene.global;
    opens org.zeki.virtualtechseller.controller.scene.global to javafx.fxml;
    exports org.zeki.virtualtechseller.controller.scene.admin;
    opens org.zeki.virtualtechseller.controller.scene.admin to javafx.fxml;
    exports org.zeki.virtualtechseller.controller.scene.moderator;
    opens org.zeki.virtualtechseller.controller.scene.moderator to javafx.fxml;
    exports org.zeki.virtualtechseller.app;
    opens org.zeki.virtualtechseller.app to javafx.fxml;
}