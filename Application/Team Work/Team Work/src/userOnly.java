import javafx.event.EventHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.TreeSet;

import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.File;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.documents.Paragraph;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class userOnly {

    public String userId, taskID, teamID, username = "";

    File fileFile;
    Task<Void> task;

    Stage stage, uploadStage;

    public Connection conn;
    public Statement output;
    public ResultSet result;

    Double xSet = 0.0, ySet = 0.0;

    public userOnly(String userID, String taskId, String teamId, Tab tab) {
        userId = userID;
        taskID = taskId;
        teamID = teamId;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/team work hub", "root", "mydatabase");
            output = conn.createStatement();
        } catch (Exception e) {
            System.out.print(e);
        }
        todoFunction(tab);
    }

    public void todoFunction(Tab tab) {
        StackPane contentPane = new StackPane();
        tab.setContent(contentPane);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.getStyleClass().add("task-scroll");
        scrollPane.setFitToWidth(true);
        contentPane.getChildren().add(scrollPane);

        StackPane todoPane = new StackPane();
        todoPane.setPadding(new Insets(10));
        scrollPane.setContent(todoPane);

        VBox todoBox = new VBox();
        todoBox.setSpacing(30);
        todoPane.getChildren().add(todoBox);

        String todoString = getTaskString("done_task", "to_do_task", userId);
        String timeString = getTaskString("time", "to_do_task", userId);
        username = getTaskString("username", "users", userId);
        if (username.equals("")) {
            username = "Username";
        }

        StackPane pane = new StackPane();
        todoBox.getChildren().add(pane);

        VBox vBox = new VBox();
        vBox.setSpacing(14);
        pane.getChildren().add(vBox);

        StackPane todoTopPane = new StackPane();
        vBox.getChildren().add(todoTopPane);

        BorderPane infoBorderPane = new BorderPane();
        todoTopPane.getChildren().add(infoBorderPane);

        HBox todoTopHBox = new HBox();
        todoTopHBox.setSpacing(7);
        todoTopHBox.setAlignment(Pos.CENTER_LEFT);
        infoBorderPane.setLeft(todoTopHBox);

        ImageView logoView = new ImageView(new Image("files/icons/user_1.png"));
        logoView.setFitHeight(35);
        logoView.setFitWidth(35);

        Text userText = new Text(username);
        userText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));

        todoTopHBox.getChildren().addAll(logoView, userText);

        StackPane assignedPane = new StackPane();
        assignedPane.setAlignment(Pos.CENTER_RIGHT);
        infoBorderPane.setRight(assignedPane);

        Text assignedText = new Text();
        assignedText.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 17));
        assignedText.setFill(Color.GRAY);
        assignedPane.getChildren().add(assignedText);

        try {
            String query = "SELECT id FROM team_tasks WHERE assigned_id = '" + userId + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                assignedText.setText("Assigned");
                break;
            }
        } catch (Exception e) {
            System.out.print(e);
        }

        StackPane documencingPane = new StackPane();
        documencingPane.setPadding(new Insets(10));
        documencingPane.setEffect(new DropShadow(4, Color.GRAY));
        documencingPane.setStyle("-fx-background-color: white; -fx-border-radius: 4; -fx-background-radius: 4;");
        vBox.getChildren().add(documencingPane);

        VBox documencingBox = new VBox();
        documencingBox.setSpacing(10);
        documencingPane.getChildren().add(documencingBox);

        StackPane donePane = new StackPane();
        documencingBox.getChildren().add(donePane);

        VBox doneVBox = new VBox();
        doneVBox.setSpacing(5);
        donePane.getChildren().add(doneVBox);

        StackPane doneContent = new StackPane();
        doneContent.setPadding(new Insets(0, 0, 20, 0));
        doneContent.setAlignment(Pos.CENTER_LEFT);
        doneVBox.getChildren().add(doneContent);

        Text contentText = new Text(todoString);
        contentText.setFont(Font.font("Times New Roman", 17));
        doneContent.getChildren().add(contentText);

        HBox doneHBox = new HBox();
        doneHBox.setAlignment(Pos.CENTER_RIGHT);
        doneHBox.setSpacing(10);
        doneVBox.getChildren().add(doneHBox);

        Button saveButton = new Button("Save");
        saveButton.setPrefWidth(100);
        saveButton.setPrefHeight(24);
        saveButton.setCursor(Cursor.HAND);
        saveButton.setStyle("-fx-background-color: Dodgerblue; -fx-border-radius: 4;");
        saveButton.setTextFill(Color.WHITE);
        saveButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                DirectoryChooser chooser = new DirectoryChooser();
                File file = chooser.showDialog(new Stage());
                if (file != null) {
                    String fileName = "Document";
                    if (!username.equals("")) {
                        fileName = username;
                    }
                    String tempFilePath = file.toString() + "/" + fileName + ".doc";
                    int fileIncrement = 0;
                    boolean fileChecker = false;
                    while (fileChecker != true) {
                        File tmpFile = new File(tempFilePath);
                        if (tmpFile.exists()) {
                            tempFilePath = file.toString() + "/" + fileName + "( " + fileIncrement + " )" + ".doc";
                            fileIncrement++;
                        } else {
                            fileChecker = true;
                        }
                    }
                    Document document = new Document();
                    Section section = document.addSection();
                    Paragraph paragraph = section.addParagraph();
                    paragraph.setText(todoString);
                    document.saveToFile(tempFilePath, FileFormat.Doc);
                    document.close();
                    updatedMessage("history.png", "File Saved");
                }
            }

        });

        Button copyButton = new Button("Copy");
        copyButton.setPrefWidth(100);
        copyButton.setPrefHeight(24);
        copyButton.setCursor(Cursor.HAND);
        copyButton.setStyle("-fx-background-color: Dodgerblue; -fx-border-radius: 4;");
        copyButton.setTextFill(Color.WHITE);
        copyButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                StringSelection stringSelection = new StringSelection(todoString);
                java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
                updatedMessage("history.png", "Copied to clipboard");
            }

        });

        doneHBox.getChildren().addAll(saveButton, copyButton);

        StackPane filePane = new StackPane();
        documencingBox.getChildren().add(filePane);

        VBox fileVBox = new VBox();
        fileVBox.setSpacing(7);
        filePane.getChildren().add(fileVBox);

        StackPane fileTitlePane = new StackPane();
        fileTitlePane.setPadding(new Insets(0, 0, 5, 0));
        fileTitlePane.setAlignment(Pos.CENTER_LEFT);
        fileTitlePane.setStyle("-fx-border-width: 0 0 2 0; -fx-border-color: rgb(214,214,214);");
        fileVBox.getChildren().add(fileTitlePane);

        Text fileText = new Text("Files");
        fileText.setFont(Font.font("Arial Rounded MT Bold", 17));
        fileText.setFill(Color.rgb(31, 31, 31));
        fileTitlePane.getChildren().add(fileText);

        VBox allFilesBox = new VBox();
        allFilesBox.setSpacing(7);
        fileVBox.getChildren().add(allFilesBox);

        Set<String> userFileSet = new TreeSet<String>();

        try {
            String query = "SELECT file FROM task_user_files WHERE task_id = '" + taskID + "' AND user_id = '" + userId
                    + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                userFileSet.add(result.getString("file"));
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        if (userFileSet.isEmpty() == false) {
            for (String fileName : userFileSet) {
                StackPane filPane = new StackPane();
                allFilesBox.getChildren().add(filPane);

                BorderPane filBorderPane = new BorderPane();
                filPane.getChildren().add(filBorderPane);

                StackPane fileNamePane = new StackPane();
                fileNamePane.setAlignment(Pos.CENTER_LEFT);
                filBorderPane.setLeft(fileNamePane);

                Text fileNameText = new Text(fileName);
                fileNameText.setFont(Font.font("Times New Roman", 16));
                fileNameText.setFill(Color.BLACK);
                fileNamePane.getChildren().add(fileNameText);

                StackPane fileNamePane2 = new StackPane();
                fileNamePane2.setAlignment(Pos.CENTER_RIGHT);
                filBorderPane.setRight(fileNamePane2);

                Button downloadButton = new Button("Download");
                downloadButton.setPrefWidth(120);
                downloadButton.setPrefHeight(25);
                downloadButton.setCursor(Cursor.HAND);
                downloadButton.setStyle("-fx-background-color: Dodgerblue; -fx-border-radius: 4;");
                downloadButton.setTextFill(Color.WHITE);
                downloadButton.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        DirectoryChooser chooser = new DirectoryChooser();
                        File distination = chooser.showDialog(new Stage());
                        fileFile = new File(distination.toString() + "/" + fileName);
                        Boolean checkBoolean = true;
                        int i = 0;
                        while (checkBoolean) {
                            if (fileFile.exists()) {
                                fileFile = new File(distination.toString() + "(" + i + ")/" + fileName);
                                i++;
                            }
                            if (!fileFile.exists()) {
                                checkBoolean = false;
                            }
                        }
                        File fileSource = new File(
                                "src/files/task_User_files/" + taskID + "/" + userId + "/" + fileName);
                        if (fileSource.exists()) {
                            String textString = "Downloading : " + fileName + " ....";
                            uploadProgress(textString);
                            task = new Task<Void>() {

                                @Override
                                protected Void call() throws Exception {
                                    try {
                                        Path path = Files.copy(fileSource.toPath(), fileFile.toPath(),
                                                StandardCopyOption.REPLACE_EXISTING);
                                    } catch (Exception e) {
                                        System.out.print(e);
                                    }
                                    return null;
                                }

                            };
                            task.setOnSucceeded((WorkerStateEvent event) -> {
                                uploadStage.close();
                            });
                            new Thread(task).start();
                        } else {
                            System.out.print("Niga");
                        }
                    }

                });
                fileNamePane2.getChildren().add(downloadButton);
            }
        } else {
            StackPane nofilePane = new StackPane();
            nofilePane.setAlignment(Pos.CENTER);
            allFilesBox.getChildren().add(nofilePane);

            Text nofileText = new Text("No files");
            nofileText.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 17));
            nofileText.setFill(Color.rgb(150, 150, 150));
            nofilePane.getChildren().add(nofileText);
        }
        userFileSet.clear();

        StackPane timePane = new StackPane();
        timePane.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().add(timePane);

        // timeString = getTimeString(timeString);
        Text timeText = new Text(timeString);
        timeText.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 17));
        timeText.setFill(Color.DARKGREY);
        timePane.getChildren().add(timeText);
    }

    public String getTimeString(String oldTime) {
        String dateString = "";
        if (!oldTime.equals("")) {
            Timestamp timestamp = Timestamp.valueOf(oldTime);
            Date date = new Date(timestamp.getTime());
            SimpleDateFormat format = new SimpleDateFormat("HH:mm a,  dd MMMM");
            dateString = format.format(date);
        }
        return dateString;
    }

    public String getTaskString(String indexString, String tableString, String todoId) {
        String getString = "";
        try {
            String query = "SELECT " + indexString + " FROM " + tableString + " WHERE id = '" + todoId + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                getString = result.getString(indexString);
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        return getString;
    }

    public void uploadProgress(String downloadName) {

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
                task.cancel();
                uploadStage.close();
            }

        });
        cancelPane.getChildren().add(cancelButton);

        StackPane titlePane = new StackPane();
        titlePane.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().add(titlePane);

        Text text = new Text(downloadName);
        text.setFont(Font.font("Arial Rounded MT Bold", 16));
        text.setFill(Color.GRAY);
        titlePane.getChildren().add(text);

        StackPane progressPane = new StackPane();
        vBox.getChildren().add(progressPane);

        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefSize(500, 35);
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
                task.cancel();
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
}
