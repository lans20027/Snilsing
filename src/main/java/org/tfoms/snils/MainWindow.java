package org.tfoms.snils;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindow extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getClassLoader().getResourceAsStream("fxml/index.fxml"));

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Снилс");
        primaryStage.show();
    }


    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }
}
