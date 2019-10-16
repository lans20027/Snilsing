package org.tfoms.snils;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tfoms.snils.model.ui.Settings;

import java.io.*;
import java.net.URL;
import java.util.Properties;

public class MainWindow extends Application {

    private Settings settings;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getClassLoader().getResourceAsStream("fxml/index.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        settings = new Settings();
        primaryStage.setTitle("Снилсование v" + settings.getVersion());
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }
}
