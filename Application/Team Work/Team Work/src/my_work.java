import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.documents.Paragraph;

// import org.apache.poi.xwpf.usermodel.XWPFDocument;
// import org.apache.poi.xwpf.usermodel.XWPFParagraph;
// import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.FileChooser.ExtensionFilter;

public class my_work {

    public String id, team_Id, doneId = "", existContent = "", font = "";
    public String savedContent = "", savedFont = "";
    Text timeText, savedText;
    TabPane tabPane;
    Tab workTab, fileTab, saveTab;
    RadioButton fileButton;

    public Connection conn;
    public Statement output;
    public ResultSet result;

    int referenceNum = 0;
    Boolean uploadBoolean = true;
    Map<String, String> fileMap = new TreeMap<String, String>();
    Map<Integer, String> fileReference = new TreeMap<Integer, String>();
    File selectedfile;

    File selectedForder;
    TextArea textArea;

    Thread thread;
    Task<Void> taskEvent;
    int changeValue = 0;

    Stage stage, saveStage, fontStage, uploadStage;
    Tab tmpTab;

    Double ySet = 0.0;
    Double xSet = 0.0;

    public my_work(Tab tab, String task_id, String team_id) {
        id = task_id;
        team_Id = team_id;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/team work hub", "root", "mydatabase");
            output = conn.createStatement();
        } catch (Exception e) {
            System.out.print(e);
        }
        workFX(tab);
    }

    public void workFX(Tab tab) {
        StackPane stackPane = new StackPane();
        tab.setContent(stackPane);

        BorderPane borderPane = new BorderPane();
        stackPane.getChildren().add(borderPane);

        StackPane topPane = new StackPane();
        borderPane.setTop(topPane);

        HBox menuBox = new HBox();
        menuBox.setPadding(new Insets(5));
        menuBox.setSpacing(20);
        menuBox.setAlignment(Pos.CENTER);
        topPane.getChildren().add(menuBox);

        ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton workButton = new RadioButton("Work");
        fileButton = new RadioButton("Files");
        RadioButton saveButton = new RadioButton("Saved");

        workButton.setSelected(true);
        radioButtons(toggleGroup, workButton);
        radioButtons(toggleGroup, fileButton);
        radioButtons(toggleGroup, saveButton);

        workButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                tabPane.getSelectionModel().select(workTab);
            }

        });
        fileButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                tabPane.getSelectionModel().select(fileTab);
            }

        });
        saveButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                tabPane.getSelectionModel().select(saveTab);
            }

        });

        menuBox.getChildren().addAll(workButton, fileButton, saveButton);

        StackPane centerPane = new StackPane();
        borderPane.setCenter(centerPane);

        tabPane = new TabPane();
        centerPane.getChildren().add(tabPane);

        workTab = new Tab("work");
        fileTab = new Tab("files");
        saveTab = new Tab("saved");
        tmpTab = new Tab("tmp_Tab");
        tabPane.getTabs().addAll(workTab, fileTab, saveTab, tmpTab);
        workFunction(workTab);
        fileFunction(fileTab);
        savedFunction(saveTab);
    }

    public void savedFunction(Tab tab) {
        String timeString = "";
        try {
            String query = "SELECT done_task,font,time FROM to_do_task WHERE task_id = '" + id + "' AND user_id = '"
                    + App.id + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                savedContent = result.getString("done_task");
                savedFont = result.getString("font");
                timeString = result.getString("time");
            }
        } catch (Exception e) {
            System.out.print(e);
        }

        StackPane stackPane = new StackPane();
        stackPane
                .setStyle("-fx-background-color: white; -fx-border-width: 2 0 0 0; -fx-border-color: rgb(240,240,240)");
        tab.setContent(stackPane);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.getStyleClass().add("text-editor");
        scrollPane.getStyleClass().add("");
        stackPane.getChildren().add(scrollPane);

        StackPane pane = new StackPane();
        pane.setPadding(new Insets(20, 20, 10, 20));
        scrollPane.setContent(pane);

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        pane.getChildren().add(vBox);

        StackPane timePane = new StackPane();
        timePane.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().add(timePane);

        timeString = timeConvert(timeString);
        String lastUpdate = "Last Update :   " + timeString;

        timeText = new Text(lastUpdate);
        timeText.setFont(Font.font("Arial Rounded MT Bold", 16));
        timeText.setFill(Color.GRAY);
        timePane.getChildren().add(timeText);

        StackPane contentPane = new StackPane();
        contentPane.setAlignment(Pos.BASELINE_LEFT);
        contentPane.setPadding(new Insets(10, 10, 30, 10));
        contentPane.setPrefHeight(100);
        contentPane.setEffect(new DropShadow(4, Color.GRAY));
        contentPane.setStyle("-fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: white;");
        vBox.getChildren().add(contentPane);

        savedText = new Text(savedContent);
        savedText.setFont(Font.font(savedFont, 16));
        savedText.setFill(Color.rgb(31, 31, 31));
        contentPane.getChildren().add(savedText);
    }

    public void fileFunction(Tab fileTab) {
        StackPane stackPane = new StackPane();
        fileTab.setContent(stackPane);

        BorderPane borderPane = new BorderPane();
        stackPane.getChildren().add(borderPane);

        StackPane addedPane = new StackPane();
        addedPane.setPadding(new Insets(10));
        addedPane.setStyle("-fx-border-width: 2 0 0 0; -fx-border-color: rgb(240,240,240);");
        borderPane.setCenter(addedPane);

        ListView<String> fileView = new ListView<String>();
        fileView.getStyleClass().add("userFileView");
        addedPane.getChildren().add(fileView);
        try {
            String query = "SELECT id,file FROM task_user_files WHERE task_id = '" + id + "' AND user_id = '" + App.id
                    + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                fileMap.put(result.getString("id"), result.getString("file"));
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        if (fileMap.isEmpty() != true) {
            for (Map.Entry<String, String> entry : fileMap.entrySet()) {
                fileReference.put(referenceNum, entry.getKey());
                fileView.getItems().add(referenceNum, entry.getValue());
                referenceNum++;
            }
        }
        fileView.setCellFactory(cellfactory -> {
            ListCell<String> cell = new ListCell<String>();
            cell.textProperty().bind(cell.itemProperty());
            ContextMenu contextMenu = new ContextMenu();
            MenuItem menuItem = new MenuItem("View & Download");
            menuItem.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    String fileName = fileView.getSelectionModel().getSelectedItem();
                    saveFile(fileName, false);
                }

            });
            MenuItem removeItem = new MenuItem("Remove");
            removeItem.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    int x = fileView.getSelectionModel().getSelectedIndex();
                    for (Map.Entry<Integer, String> entry : fileReference.entrySet()) {
                        if (x == entry.getKey()) {
                            String selectedId = entry.getValue();
                            String fileName = fileView.getSelectionModel().getSelectedItem();
                            try {
                                File file = new File("src/files/task_User_files/" + id + "/" + App.id + "/" + fileName);
                                if (file.exists()) {
                                    file.delete();
                                }
                                String query = "DELETE FROM task_User_files WHERE id = '" + selectedId
                                        + "' AND task_id = '" + id + "'";
                                output.executeUpdate(query);
                            } catch (Exception e) {
                                System.out.print(e);
                            }
                            fileView.getItems().remove(x);
                        }
                    }
                }

            });
            contextMenu.getItems().addAll(menuItem, removeItem);
            cell.setContextMenu(contextMenu);
            return cell;
        });

        StackPane addPane = new StackPane();
        addPane.setPadding(new Insets(4, 0, 0, 0));
        borderPane.setBottom(addPane);

        StackPane addStackPane = new StackPane();
        addStackPane.setPadding(new Insets(15, 15, 40, 15));
        addStackPane.setEffect(new DropShadow(4, Color.GRAY));
        addStackPane.setStyle("-fx-background-color: white;");
        addPane.getChildren().add(addStackPane);

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        addStackPane.getChildren().add(vBox);

        StackPane titlePane = new StackPane();
        titlePane.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().add(titlePane);

        Text text = new Text("Upload file");
        text.setFont(Font.font("Arial Rounded MT Bold", 17));
        text.setFill(Color.rgb(31, 31, 31));
        titlePane.getChildren().add(text);

        StackPane fieldPane = new StackPane();
        vBox.getChildren().add(fieldPane);

        TextField textField = new TextField();
        textField.setPrefHeight(35);
        textField.setText("Selected File");
        textField.setEditable(false);
        fieldPane.getChildren().add(textField);

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        vBox.getChildren().add(buttonBox);

        Button selectButton = new Button("Select");
        selectButton.setPrefHeight(25);
        selectButton.setPrefWidth(110);
        selectButton.setStyle("-fx-background-color: dodgerblue;");
        selectButton.setTextFill(Color.WHITE);
        selectButton.setCursor(Cursor.HAND);
        selectButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select File");
                fileChooser.getExtensionFilters().add(new ExtensionFilter("All files", "*.*"));

                selectedfile = fileChooser.showOpenDialog(new Stage());
                if (selectedfile != null) {
                    textField.setText(selectedfile.toString());
                }
            }

        });

        Button addButton = new Button("Add");
        addButton.setPrefHeight(25);
        addButton.setPrefWidth(110);
        addButton.setStyle("-fx-background-color: dodgerblue;");
        addButton.setTextFill(Color.WHITE);
        addButton.setCursor(Cursor.HAND);
        addButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (selectedfile != null) {
                    BooleanProperty booleanProperty = new SimpleBooleanProperty(true);
                    booleanProperty.addListener(new ChangeListener<Boolean>() {

                        @Override
                        public void changed(ObservableValue<? extends Boolean> stableValue, Boolean oldBoolean,
                                Boolean newBoolean) {
                            // uploadStage.close();
                            if (uploadBoolean != true) {
                                updatedMessage("sad.png", "File Not Uploaded : Try Again");
                            }
                            System.out.print("Updating");
                        }

                    });
                    taskEvent = new Task<Void>() {

                        @Override
                        protected Void call() throws Exception {
                            String directoryString = "src/files/task_User_files/" + id + "/" + App.id;
                            File directoryFolder = new File(directoryString);
                            if (!directoryFolder.exists()) {
                                directoryFolder.mkdir();
                                System.out.print(directoryFolder);
                            }
                            File destination = new File(directoryString + "/" + selectedfile.getName());
                            System.out.print(selectedfile);
                            Path path = Files.copy(selectedfile.toPath(), destination.toPath(),
                                    StandardCopyOption.REPLACE_EXISTING);
                            try {
                                String generatedID = getIdString("task_user_files");
                                String query = "INSERT INTO `task_user_files` (`id`,`task_id`,`user_id`,`file`) VALUES ('"
                                        + generatedID + "','" + id + "','" + App.id + "','" + selectedfile.getName()
                                        + "')";
                                output.executeUpdate(query);
                                uploadBoolean = true;
                            } catch (Exception e) {
                                uploadBoolean = false;
                                System.out.print(e);
                            }
                            booleanProperty.set(!booleanProperty.get());
                            return null;
                        }

                    };
                    Boolean checkFile = false;
                    try {
                        String query = "SELECT id FROM task_user_files WHERE task_id = '" + id + "' AND user_id = '"
                                + App.id + "' AND file = '" + selectedfile.getName() + "'";
                        result = output.executeQuery(query);
                        while (result.next()) {
                            checkFile = true;
                        }
                    } catch (Exception e) {
                        System.out.print(e);
                    }
                    if (checkFile != true) {
                        uploadProgress();
                        thread = new Thread(taskEvent);
                        thread.setDaemon(true);
                        thread.start();
                    } else {
                        updatedMessage("history.png", "File Already Exist");
                    }
                }
            }

        });

        buttonBox.getChildren().addAll(selectButton, addButton);
    }

    public void uploadProgress() {

        StackPane stackpane = new StackPane();
        stackpane.setPadding(new Insets(5));
        stackpane.setStyle("-fx-background-color: transparent;");

        StackPane pane = new StackPane();
        pane.setPadding(new Insets(10));
        pane.setEffect(new DropShadow(5, Color.GRAY));
        pane.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        stackpane.getChildren().add(pane);

        VBox vBox = new VBox();
        vBox.setSpacing(7);
        pane.getChildren().add(vBox);

        StackPane cancelPane = new StackPane();
        cancelPane.setAlignment(Pos.CENTER_RIGHT);
        cancelPane.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                xSet = mouseEvent.getSceneX();
                ySet = mouseEvent.getSceneY();
            }

        });
        cancelPane.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                uploadStage.setX(mouseEvent.getScreenX() - xSet);
                uploadStage.setY(mouseEvent.getScreenY() - ySet);
            }

        });
        vBox.getChildren().add(cancelPane);

        ImageView imageView = new ImageView(new Image("files/icons/close_1.png"));
        imageView.setFitHeight(16);
        imageView.setFitWidth(16);

        Button cancelButton = new Button();
        cancelButton.setPadding(new Insets(1));
        cancelButton.setGraphic(imageView);
        cancelButton.setStyle("-fx-background-color: transparent;");
        cancelButton.setCursor(Cursor.HAND);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                thread.stop();
                uploadStage.close();
            }

        });
        cancelPane.getChildren().add(cancelButton);

        StackPane titlePane = new StackPane();
        titlePane.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().add(titlePane);

        Text text = new Text("Uploading File...");
        text.setFont(Font.font("Arial Rounded MT Bold", 16));
        text.setFill(Color.GRAY);
        titlePane.getChildren().add(text);

        StackPane progressPane = new StackPane();
        vBox.getChildren().add(progressPane);

        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefSize(500, 35);
        progressBar.progressProperty().bind(taskEvent.progressProperty());
        progressPane.getChildren().add(progressBar);

        StackPane buttonPane = new StackPane();
        buttonPane.setAlignment(Pos.CENTER_RIGHT);
        vBox.getChildren().add(buttonPane);

        Button button = new Button("Cancel");
        button.setPrefSize(100, 25);
        button.setStyle("-fx-background-color: dodgerblue;");
        button.setTextFill(Color.WHITE);
        button.setCursor(Cursor.HAND);
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                thread.stop();
                uploadStage.close();
            }

        });
        buttonPane.getChildren().add(button);

        Scene uploadScene = new Scene(stackpane, 500, 160);
        uploadScene.setFill(Color.TRANSPARENT);

        uploadStage = new Stage();
        uploadStage.setScene(uploadScene);
        uploadStage.initStyle(StageStyle.TRANSPARENT);
        uploadStage.setTitle("File Upload");
        uploadStage.getIcons().add(new Image("files/icons/group.png"));
        uploadStage.show();
    }

    public void workFunction(Tab workTab) {
        StackPane stackPane = new StackPane();
        workTab.setContent(stackPane);

        BorderPane borderPane = new BorderPane();
        stackPane.getChildren().add(borderPane);

        StackPane centerPane = new StackPane();
        borderPane.setCenter(centerPane);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPadding(new Insets(20, 40, 20, 40));
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: rgb(230, 230, 230)");
        scrollPane.getStyleClass().add("text-editor");
        centerPane.getChildren().add(scrollPane);

        StackPane editorPane = new StackPane();
        scrollPane.setContent(editorPane);

        // String existContent = "";
        try {
            String query = "SELECT id,done_task,font FROM to_do_task WHERE task_id = '" + id + "' AND user_id = '"
                    + App.id + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                doneId = result.getString("id");
                existContent = result.getString("done_task");
                font = result.getString("font");
            }
        } catch (Exception e) {
            System.out.print(e);
        }

        textArea = new TextArea();
        textArea.setFont(Font.font(font));
        textArea.setWrapText(true);
        textArea.setText(existContent);
        textArea.setStyle(
                "-fx-border-width: 0; -fx-border-color:transparent; -fx-focus-color: transparent; -fx-text-box-border:transparent; -fx-faint-focus-color: transparent;");
        editorPane.getChildren().add(textArea);

        int textHeight = 0;
        Text text = new Text();
        text.setVisible(false);
        text.setWrappingWidth(textArea.getWidth());
        text.textProperty().bind(textArea.textProperty());
        text.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {

            @Override
            public void changed(ObservableValue<? extends Bounds> args, Bounds oldBounds, Bounds newBounds) {
                if (textHeight != newBounds.getHeight()) {
                    textArea.setPrefHeight(text.getLayoutBounds().getHeight() + 100);
                    scrollPane.setVvalue(1.0);
                }
            }

        });

        StackPane tmpPane = new StackPane();
        tmpPane.getChildren().add(text);
        tmpTab.setContent(tmpPane);

        StackPane bottomPane = new StackPane();
        borderPane.setBottom(bottomPane);

        HBox hBox = new HBox();
        hBox.setSpacing(33);
        hBox.setPadding(new Insets(10));
        hBox.setAlignment(Pos.CENTER);
        bottomPane.getChildren().add(hBox);

        Label updateLabel = new Label();
        labels(updateLabel, "Update", "files/icons/update.png");
        updateLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (!textArea.getText().equals(existContent)) {
                    Boolean taskBoolean = true;
                    Boolean saveBoolean = false;
                    String doneTaskId = "";
                    try {
                        String query = "SELECT id FROM to_do_task WHERE task_id = '" + id + "' AND user_id = '" + App.id
                                + "'";
                        result = output.executeQuery(query);
                        while (result.next()) {
                            doneTaskId = result.getString("id");
                            taskBoolean = false;
                        }
                    } catch (Exception e) {
                        System.out.print(e);
                    }
                    if (taskBoolean == true) {
                        String idGet = getIdString("to_do_task");
                        try {
                            String query = "INSERT INTO `to_do_task` (`id`,`task_id`,`team_id`,`user_id`,`font`,`done_task`) VALUES('"
                                    + idGet + "','" + id + "','" + team_Id + "','" + App.id + "','" + font + "','"
                                    + textArea.getText() + "')";
                            output.executeUpdate(query);
                            saveBoolean = true;
                            updatedMessage("3039437.png", "Updated");
                        } catch (Exception e) {
                            updatedMessage("sad.png", "Fail to Update");
                            System.out.print(e);
                        }
                        existContent = textArea.getText();
                    } else {
                        if (!doneTaskId.equals("")) {
                            try {
                                String query = "UPDATE to_do_task SET done_task = '" + textArea.getText()
                                        + "' WHERE id = '" + doneTaskId + "' AND task_id = '" + id + "'";
                                output.executeUpdate(query);
                                saveBoolean = true;
                                updatedMessage("3039437.png", "Updated");
                            } catch (Exception e) {
                                updatedMessage("sad.png", "Fail to Update");
                                System.out.print(e);
                            }
                            existContent = textArea.getText();
                        }
                    }
                    if (saveBoolean == true) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        String newTime = formatter.format(now);
                        newTime = timeConvert(newTime);
                        String timeString = "Last Updated :   " + newTime;
                        timeText.setText(timeString);
                        savedText.setText(textArea.getText());
                        task.staticID = id;
                        task.taskProperty.set(!task.taskProperty.get());
                        try {
                            String query = "UPDATE update_task SET onlineupdate = '1' WHERE task_id = '" + id + "' ";
                            output.executeUpdate(query);
                        } catch (Exception e) {
                            System.out.print(e);
                        }
                    }
                }
            }

        });

        Label saveLabel = new Label();
        labels(saveLabel, "Save", "files/icons/save.png");
        saveLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!textArea.getText().equals("")) {
                    saveFile(textArea.getText(), true);
                }
            }

        });

        Label copyLabel = new Label();
        labels(copyLabel, "Copy", "files/icons/copy.png");
        copyLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!textArea.getText().equals("")) {
                    StringSelection stringSelection = new StringSelection(textArea.getText());
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                    updatedMessage("history.png", "Copied to clipboard");
                }
            }

        });

        Label pasteLabel = new Label();
        labels(pasteLabel, "Paste", "files/icons/paste.png");
        pasteLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                textArea.requestFocus();
                int caret = textArea.getCaretPosition();
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                String clipboardString = "";
                try {
                    clipboardString = (String) clipboard.getData(DataFlavor.stringFlavor);
                } catch (UnsupportedFlavorException | IOException e) {
                    e.printStackTrace();
                }
                textArea.insertText(caret, clipboardString);
            }

        });

        Label fontLabel = new Label();
        labels(fontLabel, "Fonts", "files/icons/font.png");
        fontLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                fontOptions();
            }

        });

        Label fileLabel = new Label();
        labels(fileLabel, "Files", "files/icons/file.png");
        fileLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                tabPane.getSelectionModel().select(fileTab);
                fileButton.setSelected(true);
            }

        });

        hBox.getChildren().addAll(updateLabel, saveLabel, copyLabel, pasteLabel, fontLabel, fileLabel);
    }

    public void fontOptions() {
        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: transparent;");
        stackPane.setPadding(new Insets(5));

        StackPane pane = new StackPane();
        pane.setPadding(new Insets(10, 15, 10, 15));
        pane.setEffect(new DropShadow(5, Color.BLACK));
        pane.setStyle("-fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: white;");
        stackPane.getChildren().add(pane);

        VBox vBox = new VBox();
        vBox.setSpacing(8);
        pane.getChildren().add(vBox);

        StackPane cancelPane = new StackPane();
        cancelPane.setAlignment(Pos.CENTER_RIGHT);
        vBox.getChildren().add(cancelPane);

        ImageView cancelView = new ImageView(new Image("files/icons/close_1.png"));
        cancelView.setFitHeight(15);
        cancelView.setFitWidth(15);

        Button cancelButton = new Button();
        cancelButton.setGraphic(cancelView);
        cancelButton.setPadding(new Insets(2));
        cancelButton.setStyle("-fx-background-color: transparent;");
        cancelButton.setCursor(Cursor.HAND);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                fontStage.close();
            }

        });
        cancelPane.getChildren().add(cancelButton);

        StackPane titlePane = new StackPane();
        titlePane.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().add(titlePane);

        Text text = new Text("Choose Font");
        text.setFont(Font.font("Arial Rounded MT Bold", 17));
        text.setFill(Color.rgb(31, 31, 31));
        titlePane.getChildren().add(text);

        StackPane fontPane = new StackPane();
        vBox.getChildren().add(fontPane);

        ChoiceBox<String> choiceBox = new ChoiceBox<String>();
        choiceBox.requestFocus();
        choiceBox.setPrefSize(498, 40);
        choiceBox.getItems().addAll("Times New Roman", "Calibri", "Comic Sans MS", "Arial Rounded MT Bold");
        choiceBox.setValue(font);
        fontPane.getChildren().add(choiceBox);

        ComboBox<String> comboBox = new ComboBox<String>();
        comboBox.setPrefSize(498, 40);
        comboBox.getItems().addAll("Times New Roman", "Calibri", "Comic Sans MS", "Arial Rounded MT Bold");
        comboBox.setValue("Times New Roman");
        // fontPane.getChildren().add(comboBox);

        StackPane changePane = new StackPane();
        changePane.setAlignment(Pos.CENTER_RIGHT);
        vBox.getChildren().add(changePane);

        Button button = new Button("Proceed");
        button.setPrefHeight(25);
        button.setPrefWidth(110);
        button.setStyle("-fx-background-color: dodgerblue;");
        button.setTextFill(Color.WHITE);
        button.setCursor(Cursor.HAND);
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String selectedFont = choiceBox.getValue();
                font = selectedFont;
                if (!selectedFont.equals(font) && !doneId.equals("")) {
                    try {
                        String query = "UPDATE to_do_task SET font = '" + selectedFont + "' WHERE id = '" + doneId
                                + "'";
                        output.executeUpdate(query);
                        savedText.setFont(Font.font(selectedFont, 16));
                    } catch (Exception e) {
                        System.out.print(e);
                    }
                }
                textArea.setFont(Font.font(selectedFont));
                fontStage.close();
            }

        });
        changePane.getChildren().add(button);

        Scene fontScene = new Scene(stackPane, 500, 185);
        fontScene.setFill(Color.TRANSPARENT);

        fontStage = new Stage();
        fontStage.setScene(fontScene);
        fontStage.initStyle(StageStyle.TRANSPARENT);
        fontStage.setTitle("Task updated");
        fontStage.getIcons().add(new Image("files/icons/group.png"));
        fontStage.initModality(Modality.APPLICATION_MODAL);
        fontStage.showAndWait();
    }

    public void saveFile(String dataString, Boolean theBoolen) {
        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: transparent;");
        stackPane.setPadding(new Insets(5));

        StackPane pane = new StackPane();
        pane.setPadding(new Insets(10, 15, 10, 15));
        pane.setEffect(new DropShadow(5, Color.BLACK));
        pane.setStyle("-fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: white;");
        stackPane.getChildren().add(pane);

        VBox vBox = new VBox();
        vBox.setSpacing(15);
        pane.getChildren().add(vBox);

        StackPane titlePane = new StackPane();
        titlePane.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().add(titlePane);

        String titleString = "Save File";
        if (theBoolen != true) {
            titleString = "Download File";
        }

        Text text = new Text(titleString);
        text.setFont(Font.font("Arial Rounded MT Bold", 17));
        text.setFill(Color.rgb(31, 31, 31));
        titlePane.getChildren().add(text);

        HBox nameBox = new HBox();
        nameBox.setSpacing(10);
        nameBox.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().add(nameBox);

        String fileString = "File";
        if (theBoolen != true) {
            fileString = dataString;
        }

        TextField field = new TextField();
        field.setPromptText("Filename");
        field.setText(fileString);
        field.setPrefHeight(30);
        field.setPrefWidth(350);
        field.setStyle("-fx-focus-color: rgb(170,170,170); -fx-faint-focus-color: transparent;");

        Text nameText = new Text("File name");
        nameText.setFont(Font.font("Arial Rounded MT Bold", 16));
        nameText.setFill(Color.rgb(31, 31, 31));

        nameBox.getChildren().addAll(field, nameText);

        HBox fileBox = new HBox();
        fileBox.setSpacing(10);
        fileBox.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().add(fileBox);

        TextField textField = new TextField();
        textField.setEditable(false);
        textField.setText("Select Folder");
        textField.setFont(Font.font("Times New Roman", 17));
        textField.setPrefHeight(34);
        textField.setPrefWidth(350);

        Button button = new Button("Choose");
        button.setPrefSize(100, 25);
        button.setCursor(Cursor.HAND);
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: dodgerblue;");
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Select Forder");
                selectedForder = chooser.showDialog(new Stage());
                textField.setText(selectedForder.toString());
            }

        });

        fileBox.getChildren().addAll(textField, button);

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(buttonBox);

        Button proceedButton = new Button("Proceed");
        proceedButton.setPrefSize(100, 25);
        proceedButton.setCursor(Cursor.HAND);
        proceedButton.setTextFill(Color.WHITE);
        proceedButton.setStyle("-fx-background-color: rgb(58, 156, 236);");
        proceedButton.getStyleClass().add("buttonHover");
        proceedButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (theBoolen == true) {
                    if (!field.getText().equals("") && selectedForder != null) {
                        String tempFilePath = selectedForder.toString() + "/" + field.getText() + ".doc";
                        int fileIncrement = 0;
                        boolean fileChecker = false;
                        while (fileChecker != true) {
                            File tmpFile = new File(tempFilePath);
                            if (tmpFile.exists()) {
                                tempFilePath = selectedForder.toString() + "/" + field.getText() + "( " + fileIncrement
                                        + " )" + ".doc";
                                fileIncrement++;
                            } else {
                                fileChecker = true;
                            }
                        }
                        saveStage.close();
                        Document document = new Document();
                        Section section = document.addSection();
                        Paragraph paragraph = section.addParagraph();
                        paragraph.setText(dataString);
                        document.saveToFile(tempFilePath, FileFormat.Doc);
                        document.close();
                    }
                } else {
                    File file = new File("src/files/task_User_files/" + id + "/" + App.id + "/" + dataString);
                    if (file.exists()) {
                        System.out.print("File Founded");
                    }
                    File destination = new File(selectedForder + "/" + dataString);
                    try {
                        Path path = Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
                    } catch (Exception e) {
                        System.out.print(e);
                    }
                    saveStage.close();
                }
            }

        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefSize(100, 25);
        cancelButton.setCursor(Cursor.HAND);
        cancelButton.setTextFill(Color.WHITE);
        cancelButton.setStyle("-fx-background-color: rgb(58, 156, 236);");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                saveStage.close();
            }

        });

        buttonBox.getChildren().addAll(proceedButton, cancelButton);

        Scene saveScene = new Scene(stackPane, 500, 195);
        saveScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        saveScene.setFill(Color.TRANSPARENT);

        saveStage = new Stage();
        saveStage.setScene(saveScene);
        saveStage.initStyle(StageStyle.TRANSPARENT);
        saveStage.setTitle("Task updated");
        saveStage.getIcons().add(new Image("files/icons/group.png"));
        saveStage.initModality(Modality.APPLICATION_MODAL);
        saveStage.showAndWait();
    }

    public void updatedMessage(String image, String message) {

        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: transparent;");
        stackPane.setPadding(new Insets(5));

        StackPane pane = new StackPane();
        pane.setEffect(new DropShadow(5, Color.BLACK));
        pane.setStyle("-fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: white;");
        stackPane.getChildren().add(pane);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        pane.getChildren().add(vBox);

        String fileSource = "files/icons/" + image;
        ImageView imageView = new ImageView(new Image(fileSource));
        imageView.setFitHeight(55);
        imageView.setFitWidth(55);

        StackPane imagePane = new StackPane();
        imagePane.setAlignment(Pos.CENTER);
        imagePane.getChildren().add(imageView);
        vBox.getChildren().add(imagePane);

        Text text = new Text(message);
        text.setFont(Font.font("Arial Rounded MT Bold", 22));
        text.setFill(Color.rgb(31, 31, 31));

        StackPane textPane = new StackPane();
        textPane.setAlignment(Pos.CENTER);
        textPane.getChildren().add(text);
        vBox.getChildren().add(textPane);

        StackPane buttonPane = new StackPane();
        buttonPane.setAlignment(Pos.CENTER);
        vBox.getChildren().add(buttonPane);

        Button button = new Button("Oky");
        button.setPrefHeight(25);
        button.setPrefWidth(100);
        button.setTextFill(Color.WHITE);
        button.setCursor(Cursor.HAND);
        button.setStyle("-fx-background-color: dodgerblue;");
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }

        });
        buttonPane.getChildren().add(button);

        Scene scene = new Scene(stackPane, 400, 200);
        scene.setFill(Color.TRANSPARENT);

        stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Task updated");
        stage.getIcons().add(new Image("files/icons/group.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public String getIdString(String tableName) {
        boolean a = true;
        String tmp_id = "";
        while (a) {
            tmp_id = randomUUID();
            a = false;
            try {
                String query = "SELECT id FROM " + tableName + " WHERE id = '" + tmp_id + "'";
                result = output.executeQuery(query);
                while (result.next()) {
                    a = true;
                }
            } catch (Exception e) {
                System.out.print(e);
            }
        }
        return tmp_id;
    }

    public String randomUUID() {
        UUID uuid = UUID.randomUUID();
        long longId = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        String createdId = Long.toString(longId, Character.MAX_RADIX);
        return createdId;
    }

    public void labels(Label label, String name, String imageSource) {

        ImageView updateView = new ImageView(new Image(imageSource));
        updateView.setFitHeight(35);
        updateView.setFitWidth(35);

        label.setPadding(new Insets(2));
        label.setText(name);
        label.setGraphic(updateView);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setCursor(Cursor.HAND);
        label.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 17));
        label.setTextFill(Color.GRAY);
        label.setContentDisplay(ContentDisplay.TOP);
    }

    public void radioButtons(ToggleGroup toggleGroup, RadioButton radioButton) {
        radioButton.setPrefWidth(120);
        radioButton.setAlignment(Pos.CENTER);
        radioButton.setFont(Font.font("Comic Sans MS", 15));
        radioButton.setTextFill(Color.rgb(31, 31, 31));
        radioButton.setToggleGroup(toggleGroup);
        radioButton.setCursor(Cursor.HAND);
    }

    public String timeConvert(String timeString) {
        String getTime = "";
        if (!timeString.equals("")) {
            Timestamp timestamp = Timestamp.valueOf(timeString);
            Date date = new Date(timestamp.getTime());
            SimpleDateFormat format = new SimpleDateFormat("dd MMMM, HH:mm a");
            getTime = format.format(date);
        }
        return getTime;
    }
}
