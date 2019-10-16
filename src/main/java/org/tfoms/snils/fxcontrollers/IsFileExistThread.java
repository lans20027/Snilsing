package org.tfoms.snils.fxcontrollers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import org.tfoms.snils.dao.SnilsDAO;
import org.tfoms.snils.model.TablePerson;
import org.tfoms.snils.model.ui.Settings;
import org.tfoms.snils.model.ui.StatusBar;
import org.tfoms.snils.xmls.XmlParser;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class IsFileExistThread extends Thread{
    final StatusBar statusBar;
    final ArrayList<String> enps;
    final ArrayList<String> enpsGood;
    final TableView<TablePerson> tableView;
    private Settings settings;
    private String directorySnils;
    private String directoryError;
    private Long timeWait;
    private boolean saveResponse;


    public IsFileExistThread(StatusBar s, TableView<TablePerson> tableView){
        statusBar = s;
        enpsGood = new ArrayList<>();
        enps = new ArrayList<>();
        this.tableView = tableView;
        settings = new Settings();
        directoryError = settings.getErrorFolder();
        directorySnils = settings.getResponseFolder();
        timeWait = Long.valueOf(settings.getTimeWait());
        saveResponse = settings.isSaveResponse();
    }


    private boolean putDocuments(){
        XmlParser parser = new XmlParser();
        ObservableList<TablePerson> dataCust;
        synchronized (tableView) {
             dataCust = tableView.getItems();
        }

        for(TablePerson person : dataCust){
            try {
                if(parser.createDocument(person)){
                    enps.add(person.getEnp());
                }
            } catch (Exception e){
                Platform.runLater(() -> {
                    statusBar.update("Ошибка","Ошибка при создании документа для:" + person.getEnp() + "|-->" + e.getMessage(),0);
                });
                return false;
            }
        }
        return true;
    }


    private void checkGood(){
        File oiFile;
        Path pathSnils = Paths.get(directorySnils);
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(pathSnils)) {
            for (Path file : stream) {
                oiFile = file.toFile();
                if (oiFile.isFile() && oiFile.canRead()) {
                    String fileEnp = checkEnpsInsideFile(oiFile,enps,enpsGood);
                    if(!fileEnp.equals("")){
//                        System.out.println("DELETING:" + file.toString());
                        if(!saveResponse) Files.delete(file);
                    }
                }
            }
        }catch (Exception e){
            Platform.runLater(() -> {
                statusBar.update("Ошибка",e.getMessage(),0);
            });
        }

        File oiFile1;
        Path pathSnils1 = Paths.get(directoryError);
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(pathSnils1)) {
            for (Path file : stream) {
                oiFile1 = file.toFile();
                if (oiFile1.isFile() && oiFile1.canRead()) {
                    String fileEnp = checkEnpsInsideFile(oiFile1,enps,enpsGood);
                    if(!fileEnp.equals("")){
//                        System.out.println("DELETING:" + file.toString());
                        if(!saveResponse) Files.delete(file);
                    }
                }
            }
        }catch (Exception e){
            Platform.runLater(() -> {
                statusBar.update("Ошибка",e.getMessage(),0);
            });
        }
    }

    private String checkEnpsInsideFile(File file,ArrayList<String> enps,ArrayList<String> enpGood) throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);//в переменной документ лежит результат парсинга

        NodeList clientsId = document.getElementsByTagName("clientId");
        String clientId = clientsId.item(0).getTextContent();
        for(String enp : enps){
            if(clientId.startsWith(enp)){
                enpGood.add(enp);
                enps.remove(enp);
                String snils = "";
                NodeList snilsNode = document.getElementsByTagName("ns2:Snils");
                if(snilsNode.getLength() == 1){
                    snils = snilsNode.item(0).getTextContent();
                } else {
                    NodeList descriptionNode = document.getElementsByTagName("description");
                    if(descriptionNode.getLength() == 1) {
                        snils = descriptionNode.item(0).getTextContent();
                    }else{
                        NodeList descriptionNodeError = document.getElementsByTagName("ns2:description");
                        if(descriptionNodeError.getLength() == 1){
                            snils = "ошибка";
                        }
                    }
                }
                updateTableRow(enp,snils);
                System.out.println("found enp-" + enp);
                return enp;
            }
        }
        return "";
    }

    private void updateTableRow(String enp, String snils){
        synchronized (tableView){
            ObservableList<TablePerson> data = tableView.getItems();
            for(TablePerson p : data){
                if(p.getEnp().equals(enp)){
                    p.setSnils(snils);
                    System.out.println("enp:" + enp + " snils:" + snils);
                    SnilsDAO.insertPerson(p);
                    break;
                }
            }
            tableView.setItems(data);
            tableView.refresh();
        }
    }



    @Override
    public void run() {
        Thread thread = Thread.currentThread();
        Platform.runLater(()->{
            statusBar.update("Отправка запроса","",-1);
        });
        //'делаем запрос'
        //создаем документы в папке srv-term03/542202_3s/out
        putDocuments();
        Platform.runLater(()->{
            statusBar.update("Ожидание ответа","",-1);
        });

        int all = enps.size();
        try {
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            Runnable fileTask = () -> {
                    if (enps.isEmpty()){
                        executorService.shutdownNow();
                        Platform.runLater(()->{
                            statusBar.update("Получено ответов:"+ enpsGood.size() + "/" + all,"",0);
                        });
                        thread.interrupt();
                    }
                    checkGood();
            };

            long delay = 5000L;
            long period = 1000L;


            //запускаем задачу , которая ищет ответы со снилсами, либо ошибки
            executorService.scheduleWithFixedDelay(fileTask,delay,period,TimeUnit.MILLISECONDS);
//            System.out.println("sleeep");
            Thread.sleep(timeWait * 1000);
            System.out.println("interrupting");
            executorService.shutdownNow();
            executorService.awaitTermination(10,TimeUnit.SECONDS);
            throw new InterruptedException("timeout");
        } catch (InterruptedException e){
            Platform.runLater(() -> statusBar.update("Получено ответов:" + enpsGood.size()+ "/" + all,"ожидание остановлено",0));
        }
    }


}
