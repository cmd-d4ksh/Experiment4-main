module com.dhruvijain.deliveryplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.dhruvijain.deliveryplanner to javafx.fxml;
    opens com.dhruvijain.deliveryplanner.app to javafx.fxml;
    opens com.dhruvijain.deliveryplanner.controller to javafx.fxml;
    opens com.dhruvijain.deliveryplanner.model to javafx.base;

    exports com.dhruvijain.deliveryplanner;
    exports com.dhruvijain.deliveryplanner.app;
    exports com.dhruvijain.deliveryplanner.constants;
    exports com.dhruvijain.deliveryplanner.controller;
    exports com.dhruvijain.deliveryplanner.model;
    exports com.dhruvijain.deliveryplanner.dao;
    exports com.dhruvijain.deliveryplanner.bll;
}
