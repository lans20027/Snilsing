package org.tfoms.snils;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

public class MainWindow extends Application {

    public static void main(String[] args){
        launch(args);
    }
    private void showProperties(){
        Properties properties = new Properties();
        try {

            String propPath = getClass().getClassLoader().getResource("props/setting.properties").getPath();
            File propsFile = new File(propPath);

            System.out.println("path:"  + propsFile.getPath());

            properties.load(getClass().getClassLoader().getResourceAsStream("props/setting.properties"));
            String version = properties.getProperty("version");
            System.out.println("version:" + version);
            properties.setProperty("version","115");
            version = properties.getProperty("version");
            System.out.println("version:" + version);
//            properties.store(new OutputStreamWriter(new Out"props/setting.properties"),"load new props");
        } catch (IOException e) {
            System.out.println("настройки не найдены");
        }
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        showProperties();
        Parent root = loader.load(getClass().getClassLoader().getResourceAsStream("fxml/index.fxml"));
//        InputStream is = new FileInputStream("fxml/index.fxml");
//        System.out.println(is);
//        Parent root = loader.load(new FileInputStream("fxml/index.fxml"));
        System.out.println("main Hello from " + Thread.currentThread().getName());
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
