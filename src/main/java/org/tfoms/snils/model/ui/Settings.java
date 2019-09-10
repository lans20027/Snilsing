package org.tfoms.snils.model.ui;

import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class Settings {
    private final String defaultTimeWait = "600";
    private final String defaultResponseFolder = "\\\\srv-term03\\542202_3s\\in\\snils\\";
    private final String defaultRequestFolder = "\\\\srv-term03\\542202_3s\\out\\";
    private final String defaultErrorFolder = "\\\\srv-term03\\542202_3s\\in\\error\\";
    private final boolean defaultSaveResponse = false;



    private String timeWait;
    private String responseFolder;
    private String errorFolder;
    private String requestFolder;
    private boolean saveResponse;
    private String version;

    private final Properties properties = new Properties();


    public Settings(){
        loadSettings();
        System.out.println(this);
    }


    public void loadSettings(){
        try {
            properties.clear();
            properties.load(getClass().getClassLoader().getResourceAsStream("props/setting.properties"));
            version = properties.getProperty("version");
            timeWait = properties.getProperty("waitTime");
            responseFolder = properties.getProperty("responseFolder");
            errorFolder = properties.getProperty("errorFolder");
            requestFolder = properties.getProperty("requestFolder");
            saveResponse = new Boolean(properties.getProperty("saveResponse"));
        } catch (IOException e) {
            System.out.println("Не найдено");
        }
    }

    public void reset(){
        timeWait = defaultTimeWait;
        responseFolder = defaultResponseFolder;
        errorFolder = defaultErrorFolder;
        requestFolder = defaultRequestFolder;
        saveResponse = defaultSaveResponse;

        save();
    }


    public boolean save(){
        try {
            properties.setProperty("version",version);
            properties.setProperty("waitTime",timeWait);
            properties.setProperty("responseFolder",responseFolder);
            properties.setProperty("errorFolder",errorFolder);
            properties.setProperty("requestFolder",requestFolder);
            properties.setProperty("saveResponse",Boolean.toString(saveResponse));


            String path = getClass().getClassLoader().getResource("props/setting.properties").getPath();
            System.out.println("saving:" + this + "\n into " + path );

            this.properties.store(new FileOutputStream(path),"user saved");

            System.out.println("save ok");
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "timeWait='" + timeWait + '\'' +
                ", responseFolder='" + responseFolder + '\'' +
                ", errorFolder='" + errorFolder + '\'' +
                ", requestFolder='" + requestFolder + '\'' +
                ", saveResponse=" + saveResponse +
                ", version='" + version + '\'' +
                '}';
    }

    public String getTimeWait() {
        return timeWait;
    }

    public void setTimeWait(String timeWait) {
        this.timeWait = timeWait;
    }

    public String getResponseFolder() {
        return responseFolder;
    }

    public void setResponseFolder(String responseFolder) {
        this.responseFolder = responseFolder;
    }

    public String getErrorFolder() {
        return errorFolder;
    }

    public void setErrorFolder(String errorFolder) {
        this.errorFolder = errorFolder;
    }

    public String getRequestFolder() {
        return requestFolder;
    }

    public void setRequestFolder(String requestFolder) {
        this.requestFolder = requestFolder;
    }

    public boolean isSaveResponse() {
        return saveResponse;
    }

    public void setSaveResponse(boolean saveResponse) {
        this.saveResponse = saveResponse;
    }
}
