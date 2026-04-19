package com.dhruvijain.deliveryplanner.app;

import com.dhruvijain.deliveryplanner.constants.AppConstants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class DeliveryPlannerApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        com.dhruvijain.deliveryplanner.dao.DatabaseHelper.initializeDatabase();
        
        URL fxmlUrl = getClass().getResource(AppConstants.VIEW_LOGIN);
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(loader.load(), AppConstants.WINDOW_WIDTH, AppConstants.WINDOW_HEIGHT);

        URL cssUrl = getClass().getResource(AppConstants.CSS_STYLES);
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        stage.setTitle(AppConstants.APP_TITLE);
        stage.setMinWidth(AppConstants.WINDOW_MIN_WIDTH);
        stage.setMinHeight(AppConstants.WINDOW_MIN_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }
}
