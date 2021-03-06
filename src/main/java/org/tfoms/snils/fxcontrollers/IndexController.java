package org.tfoms.snils.fxcontrollers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.tfoms.snils.dao.FindSnilsDAO;
import org.tfoms.snils.dao.SnilsDAO;
import org.tfoms.snils.model.TablePerson;
import org.tfoms.snils.model.ui.StatusBar;

import java.io.*;
import java.util.*;

public class IndexController {
    private final ObservableList<TablePerson> personData = FXCollections.observableArrayList();

    @FXML
    BorderPane parent;

    @FXML
    ProgressBar progressBar;

    @FXML
    Label statusLabel;

    @FXML
    MenuItem menuExport;

    @FXML
    MenuItem menuImport;

    @FXML
    private TableView<TablePerson> personTableview;

    @FXML
    private TableColumn<TablePerson, String> snilsCol;
    @FXML
    private TableColumn<TablePerson,String> enpCol;
    @FXML
    private TableColumn<TablePerson,String> famCol;
    @FXML
    private TableColumn<TablePerson,String> imCol;
    @FXML
    private TableColumn<TablePerson,String> otCol;
    @FXML
    private TableColumn<TablePerson,Date> drCol;
    @FXML
    private TableColumn<TablePerson,String> serdocCol;
    @FXML
    private TableColumn<TablePerson,String> numdocCol;

    private StatusBar statusBar;

    private final String statusText = "Здесь будут показываться возможные ошибки";
    private final Tooltip statusTooltip = new Tooltip(statusText);

    private IsFileExistThread checkFilesExistsThread;

    private File lastImported = null;
    private File lastExported = null;


    @FXML
    public void initialize(){
        enpCol.setCellValueFactory(new PropertyValueFactory<>("enp"));
        snilsCol.setCellValueFactory(new PropertyValueFactory<>("snils"));
        famCol.setCellValueFactory(new PropertyValueFactory<>("personSurname"));
        imCol.setCellValueFactory(new PropertyValueFactory<>("personFirstname"));
        otCol.setCellValueFactory(new PropertyValueFactory<>("personLastname"));
        drCol.setCellValueFactory(new PropertyValueFactory<>("personBirthday"));
        enpCol.setPrefWidth(140);
        serdocCol.setCellValueFactory(new PropertyValueFactory<>("personSerdoc"));
        numdocCol.setCellValueFactory(new PropertyValueFactory<>("personNumdoc"));
        System.out.println("init column good");

        personTableview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ContextMenu contextMenu = getContextMenu();
        contextMenu.setOnShowing(event -> {
                    contextMenu.getItems().get(0).setDisable(!isRowsSelected());
                    contextMenu.getItems().get(1).setDisable(personTableview.getItems().size() == 0);
                    });
        personTableview.setContextMenu(contextMenu);

        ContextMenu contextMenu1 = getContextMenuForStatusLabel();
        contextMenu1.setOnShowing(event -> {
            contextMenu1.getItems().get(0).setDisable(checkFilesExistsThread == null || !checkFilesExistsThread.isAlive());
        });
        statusLabel.setContextMenu(contextMenu1);

        statusBar = new StatusBar(progressBar,statusLabel);
    }

    @FXML
    public void removeSelection(Event event){
        personTableview.getSelectionModel().clearSelection();
    }


    @FXML
    public void sendQueryNew(){
        checkFilesExistsThread = new IsFileExistThread(statusBar,personTableview);
        checkFilesExistsThread.start();
    }


    @FXML
    public void openSettings(){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/settings.fxml"));
        try {
            Parent parent = loader.load();
            SettingsDialogController controller = loader.<SettingsDialogController>getController();

            Scene scene = new Scene(parent, 400, 300);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Настройки");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);

            stage.showAndWait();
        } catch (IOException e) {
            statusBar.update("Ошибка при открытии настроек",e.getMessage(),0);
        }

    }


    @FXML
    public void findSnilsGood(){
        Thread findSnilsGoodThread = new Thread(() -> {
            try {
                List<TablePerson> data = SnilsDAO.findSnilsGood();
                synchronized (personData) {
                    personData.clear();
                    personData.addAll(data);
                }

                synchronized (personTableview) {
                    personTableview.setItems(personData);
                    personTableview.refresh();
                }

                Platform.runLater(() -> {
                    statusBar.update("Статус",0);
                });
            }catch (Exception ex){
                Platform.runLater(() -> {
                    statusBar.update("Ошибка при поиске снилсов", ex.getMessage(),0);
                    showErrorDialog(ex);
                    menuExport.setDisable(false);
                });
            }
        });


        statusBar.update("Поиск людей в базе",-1);
        findSnilsGoodThread.start();
    }

    @FXML
    public void importExcel(){
        statusBar.reset();//обновляем статус бар

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Импорт данных");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel files","*.xlsx"));


        if(lastImported != null){//последний открываемый файл
            fileChooser.setInitialDirectory(lastImported.getParentFile());
            fileChooser.setInitialFileName(lastImported.getName());
        }
        File file = fileChooser.showOpenDialog(parent.getScene().getWindow());

        if(file == null) return;
        lastImported = file;


        statusBar.update("Считывание из экселя",-1);
        menuExport.setDisable(true);

        Thread thread = new Thread(() -> {
            try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file))) {
                XSSFSheet sheet = workbook.getSheetAt(0);
                int rows = sheet.getPhysicalNumberOfRows();


                ArrayList<String> enps = new ArrayList<>(rows);
                List<TablePerson> data = new ArrayList<>();


                String mode = sheet.getRow(0).getCell(0).getStringCellValue();

                if(mode != null && (mode.equalsIgnoreCase("prizyv") || mode.contains("prizyv"))){
                    for (int i = 1; i < rows; i++) {
                        Row row = sheet.getRow(i);
                        if(row.getCell(0).getStringCellValue() == null || row.getCell(0).getStringCellValue().length() == 0) break;
                        try {
                            String surname = row.getCell(0).getStringCellValue();
                            String firstname = row.getCell(1).getStringCellValue();
                            String lastname = row.getCell(2).getStringCellValue();
                            Date birthday = row.getCell(3).getDateCellValue();
                            Cell serCell = row.getCell(4);
                            serCell.setCellType(CellType.STRING);
                            String ser = serCell.getStringCellValue();
                            Cell numCell = row.getCell(5);
                            numCell.setCellType(CellType.STRING);
                            String num  = numCell.getStringCellValue();
                            data.add(SnilsDAO.findPerson(surname.trim().toUpperCase(),firstname.trim().toUpperCase(),lastname.trim().toUpperCase(),birthday,ser.trim(),num.trim()));
                        } catch (Exception ex) {
                            throw new Exception("Строка " + i + "Ошибка:" + ex.toString());
                        }
                    }
                }else {
                    for (int i = 0; i < rows; i++) {
                        Row row = sheet.getRow(i);
                        try {
                            enps.add(row.getCell(0).getStringCellValue().trim());
                        } catch (Exception ex) {
                            break;
                        }
                    }
                    data = FindSnilsDAO.findPersonByEnp1(enps);
                }


                synchronized (personData) {
                    personData.clear();
                    personData.addAll(data);
                }

                synchronized (personTableview) {
                    personTableview.setItems(personData);
                    personTableview.refresh();
                }


                Platform.runLater(() -> {
                    statusBar.update("Импорт готово",0);
                    menuExport.setDisable(false);
                });

            }catch (Exception ex){
                Platform.runLater(() -> {
                    statusBar.update("Ошибка при импорте",ex.getLocalizedMessage(),0);
                    showErrorDialog(ex);
                    menuExport.setDisable(false);
                });




            }
        });
        thread.start();
    }

    @FXML
    public void exportExcel(){
        statusBar.reset();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Экспорт в эксель");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel files","*.xlsx"));
        if(lastExported != null){
            fileChooser.setInitialDirectory(lastExported.getParentFile());
            fileChooser.setInitialFileName(lastExported.getName());
        }
        File file = fileChooser.showSaveDialog(parent.getScene().getWindow());

        if(file == null) return;
        lastExported = file;



        menuImport.setDisable(true);
        statusBar.update("Запись в эксель",-1);

        Thread thread = new Thread(() -> {
            try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                XSSFSheet sheet = workbook.createSheet("Данные");

                int rownum = 0;
                String[] headerRow={"snils","enp","fam","im","ot","dr","serdoc","numdoc","sex"};

                Row row = sheet.createRow(rownum++);


                for(int i =0; i < headerRow.length; i++) {
                    sheet.setColumnWidth(i, 3500);
                    row.createCell(i).setCellValue(headerRow[i]);
                }

                for(TablePerson x : personData){
                    row = sheet.createRow(rownum++);
                    row.createCell(0).setCellValue(x.getSnils());
                    row.createCell(1).setCellValue(x.getEnp());
                    row.createCell(2).setCellValue(x.getPersonSurname());
                    row.createCell(3).setCellValue(x.getPersonFirstname());
                    row.createCell(4).setCellValue(x.getPersonLastname());
                    row.createCell(5).setCellValue(x.getPersonBirthday().toString());
                    row.createCell(6).setCellValue(x.getPersonSerdoc());
                    row.createCell(7).setCellValue(x.getPersonNumdoc());
                    row.createCell(8).setCellValue(x.getSex());
                }
                workbook.write(new FileOutputStream(file));
                Platform.runLater(() -> {
                    statusBar.update("Экспорт готово",0);
                    menuImport.setDisable(false);
                });
            }catch (Exception ex){
                Platform.runLater(() -> {
                    statusBar.update("Ошибка при экспорте",ex.getMessage(),0);
                    showErrorDialog(ex);
                    menuImport.setDisable(false);
                });
            }
        });
        thread.start();
    }

    @FXML
    public void deleteRows(Event event){
        ObservableList<TablePerson> selectedRows = personTableview.getSelectionModel().getSelectedItems();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Удалить выделенные записи из таблицы?\nВыбрано записей:" + selectedRows.size());

        alert.setHeaderText("Удаление записей");
        alert.setTitle("Подтвердите действие");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ArrayList<TablePerson> rows = new ArrayList<>(selectedRows);
            rows.forEach(row ->{
                personTableview.getItems().remove(row);
                personData.remove(row);
            });
        }
    }


    private ContextMenu getContextMenu(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Удалить выделенное");
        MenuItem item2 = new MenuItem("Очистить таблицу");
        item1.setOnAction(this::deleteRows);
        item2.setOnAction(this::clearTable);
        contextMenu.getItems().addAll(item1,item2);

        return contextMenu;
    }

    private void clearTable(Event event){
        synchronized (personTableview){
            personTableview.getItems().clear();
        }
    }

    private ContextMenu getContextMenuForStatusLabel(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Перестать ожидать");
        item1.setOnAction(event -> {checkFilesExistsThread.interrupt(); statusLabel.setText("Готово");});
        contextMenu.getItems().add(item1);
        return contextMenu;
    }

    private boolean isRowsSelected(){
        return personTableview.getSelectionModel().getSelectedItems().size() > 0;
    }


    private void showErrorDialog(String errorMsg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(errorMsg);
        alert.showAndWait();
    }

    private void showErrorDialog(Exception errorMsg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        StringBuilder description = new StringBuilder("Описание ошибки:\n");

        description.append(errorMsg.toString());

        String filePath = "";
        try (PrintWriter printWriter = new PrintWriter("ExceptionDescription.txt")){
            errorMsg.printStackTrace(printWriter);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        description.append("\n\nДетальное описание ошибки лежит в файле Snilsing/app/ExceptionDescription.txt\n" );
        alert.setContentText(description.toString());
        alert.showAndWait();
    }

}
