module org.zeki.virtualtechseller {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires static lombok;
    requires java.sql;
    requires java.desktop;
    requires javafx.base;
    requires org.zeki.virtualtechseller;

    opens org.zeki.virtualtechseller to javafx.fxml;
    exports org.zeki.virtualtechseller;
    opens org.zeki.virtualtechseller.controller.client to javafx.fxml;
    exports org.zeki.virtualtechseller.controller.client;
    exports org.zeki.virtualtechseller.controller.global;
    opens org.zeki.virtualtechseller.controller.global to javafx.fxml;
    exports org.zeki.virtualtechseller.controller.admin;
    opens org.zeki.virtualtechseller.controller.admin to javafx.fxml;
    exports org.zeki.virtualtechseller.controller.moderator;
    opens org.zeki.virtualtechseller.controller.moderator to javafx.fxml;
    exports org.zeki.virtualtechseller.app;
    opens org.zeki.virtualtechseller.app to javafx.fxml;
}