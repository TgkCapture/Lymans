import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class task {

    public static BooleanProperty taskProperty = new SimpleBooleanProperty();
    public static String staticID = "";

    public String id, team_Id, name, content, ownerID, usernameString = "";
    Text titlesText, tasksText;

    Stage stage, editStage, uploadStage, deleteStage;
    TabPane tabPane, userTabPane;
    Tab todoTab, workTab, userTab, commenTab, onlyTab;
    VBox usersVBox;

    Map<String, String> fileMap = new TreeMap<String, String>();
    Map<Integer, String> fileReference = new TreeMap<Integer, String>();
    public int listReference = 0, listHieght = 30;

    Map<String, CheckBox> userMap = new TreeMap<String, CheckBox>();
    Set<String> userSet = new TreeSet<String>();
    List<CheckBox> userList = new ArrayList<CheckBox>();
    ToggleGroup userGroup = new ToggleGroup();

    Map<String, String> todoMap = new TreeMap<String, String>();
    Set<String> userIdSet = new TreeSet<String>();
    Set<String> assignedSet = new TreeSet<String>();
    Map<String, Tab> userTabs = new TreeMap<String, Tab>();

    public Connection conn;
    public Statement output;
    public ResultSet result;

    Double xSet = 0.0, ySet = 0.0;

    public task(String taskID, String team_id) {
        id = taskID;
        team_Id = team_id;
        int verified = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/team work hub", "root", "mydatabase");
            output = conn.createStatement();
            String query = "SELECT id_owner,name,content FROM tasks WHERE id = '" + id + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                ownerID = result.getString("id_owner");
                name = result.getString("name");
                content = result.getString("content");
                verified++;
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        if (verified != 0) {
            taskFunction();
        }
    }

    public void taskFunction() {
        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-border-width: 2 0 0 0; -fx-border-color: rgb(240, 240, 240);");

        BorderPane borderPane = new BorderPane();
        stackPane.getChildren().add(borderPane);

        // Task information

        StackPane infoPane = new StackPane();
        infoPane.setMaxWidth(350);
        infoPane.setMinWidth(350);
        infoPane.setStyle("-fx-background-color: white;");
        borderPane.setLeft(infoPane);

        BorderPane infoBorderPane = new BorderPane();
        infoBorderPane.setStyle("-fx-background-color: transparent;");
        infoPane.getChildren().add(infoBorderPane);

        // Task Information :: Title Bar

        StackPane aboutPane = new StackPane();
        aboutPane.setPadding(new Insets(5, 10, 5, 10));
        aboutPane.setAlignment(Pos.CENTER_LEFT);
        aboutPane.setEffect(new DropShadow(5, Color.GREY));
        aboutPane.setStyle("-fx-background-color: white; -fx-border-width: 0 0 1 0;");
        infoBorderPane.setTop(aboutPane);

        Text aboutText = new Text("About This Task");
        aboutText.setFont(Font.font("Comic Sans MS", 17));
        aboutText.setFill(Color.rgb(31, 31, 31));
        aboutPane.getChildren().add(aboutText);

        // Task Information :: Content

        StackPane aboutStackPane = new StackPane();
        aboutStackPane.setStyle("-fx-background-color: transparent;");
        infoBorderPane.setCenter(aboutStackPane);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.getStyleClass().add("task-scroll");
        aboutStackPane.getChildren().add(scrollPane);

        StackPane afterScrollPane = new StackPane();
        afterScrollPane.setPadding(new Insets(10));
        scrollPane.setContent(afterScrollPane);

        VBox aboutBox = new VBox();
        aboutBox.setSpacing(30);
        afterScrollPane.getChildren().add(aboutBox);

        VBox inforVBox = new VBox();
        inforVBox.setSpacing(20);
        aboutBox.getChildren().add(inforVBox);

        HBox inforHBox = new HBox();
        inforVBox.getChildren().add(inforHBox);

        StackPane titlePane = new StackPane();
        titlePane.setAlignment(Pos.CENTER_LEFT);
        titlePane.setPrefWidth(100);
        inforHBox.getChildren().add(titlePane);

        Text titleText = new Text("Task Title");
        titleText.setFont(Font.font("arial Rounded MT Bold", FontWeight.LIGHT, 16));
        titleText.setFill(Color.rgb(140, 140, 140));
        titlePane.getChildren().add(titleText);

        StackPane dotPane = new StackPane();
        dotPane.setPadding(new Insets(0, 10, 0, 10));
        inforHBox.getChildren().add(dotPane);

        Text dotText = new Text(":");
        dotText.setFont(Font.font("arial Rounded MT Bold", FontWeight.LIGHT, 16));
        dotText.setFill(Color.rgb(140, 140, 140));
        dotPane.getChildren().add(dotText);

        StackPane namePane = new StackPane();
        inforHBox.getChildren().add(namePane);

        Text nameText = new Text(name);
        nameText.setFont(Font.font("arial Rounded MT Bold", FontWeight.LIGHT, 16));
        nameText.setFill(Color.rgb(140, 140, 140));
        namePane.getChildren().add(nameText);

        VBox taskBox = new VBox();
        taskBox.setSpacing(5);
        inforVBox.getChildren().add(taskBox);

        FlowPane taskFlowPane = new FlowPane();
        taskFlowPane.setPadding(new Insets(0, 0, 5, 0));
        taskFlowPane.setStyle("-fx-border-width: 0 0 2 0; -fx-border-color: rgb(214, 214, 214);");
        taskBox.getChildren().add(taskFlowPane);

        Text taskText = new Text("Task");
        taskText.setFont(Font.font("arial Rounded MT Bold", FontWeight.LIGHT, 17));
        taskText.setFill(Color.rgb(31, 31, 31));
        taskFlowPane.getChildren().add(taskText);

        FlowPane taskPane = new FlowPane();
        taskPane.setAlignment(Pos.CENTER);
        taskPane.setPadding(new Insets(8, 0, 5, 0));
        taskBox.getChildren().add(taskPane);

        tasksText = new Text(content);
        tasksText.setFont(Font.font("arial Rounded MT Bold", FontWeight.LIGHT, 16));
        tasksText.setFill(Color.rgb(140, 140, 140));
        taskPane.getChildren().add(tasksText);

        FlowPane editPane = new FlowPane();
        editPane.setPadding(new Insets(5));
        editPane.setAlignment(Pos.CENTER);
        taskBox.getChildren().add(editPane);

        Button editButton = new Button("Edit");
        editButton.setPrefWidth(120);
        editButton.setPrefHeight(25);
        editButton.setCursor(Cursor.HAND);
        editButton.setStyle("-fx-background-color: Dodgerblue; -fx-border-radius: 4;");
        editButton.setTextFill(Color.WHITE);
        editButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                editFunction();
            }

        });
        editPane.getChildren().add(editButton);

        // files

        VBox fileBox = new VBox();
        aboutBox.getChildren().add(fileBox);

        FlowPane fileFlowPane = new FlowPane();
        fileFlowPane.setPadding(new Insets(0, 0, 5, 0));
        fileFlowPane.setStyle("-fx-border-width: 0 0 2 0; -fx-border-color: rgb(214, 214, 214);");
        fileBox.getChildren().add(fileFlowPane);

        Text fileText = new Text("Files");
        fileText.setFont(Font.font("arial Rounded MT Bold", FontWeight.LIGHT, 17));
        fileText.setFill(Color.rgb(31, 31, 31));
        fileFlowPane.getChildren().add(fileText);

        StackPane filePane = new StackPane();
        filePane.setPadding(new Insets(5, 0, 3, 0));
        fileBox.getChildren().add(filePane);

        int a = 0;
        try {
            String query = "SELECT id,file FROM task_Files WHERE task_id = '" + id + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                fileMap.put(result.getString("id"), result.getString("file"));
                a++;
            }
        } catch (Exception e) {
            System.out.print(e);
        }

        if (a != 0) {

            ListView<String> listView = new ListView<String>();
            listView.setStyle("-fx-background-color: transparent;");
            listView.getStyleClass().add("task_files");
            filePane.getChildren().add(listView);

            for (Map.Entry<String, String> entry : fileMap.entrySet()) {
                String file_Id = entry.getKey();
                String file_String = entry.getValue();
                listView.getItems().add(listReference, file_String);
                listView.setPrefHeight(listHieght);
                fileReference.put(listReference, file_Id);
                listHieght += 30;
                listReference++;
            }
        } else {
            FlowPane noFilPane = new FlowPane();
            noFilPane.setPadding(new Insets(10));
            filePane.getChildren().add(noFilPane);

            Text filText = new Text("No files");
            filText.setFont(Font.font("arial Rounded MT Bold", FontWeight.LIGHT, 16));
            filText.setFill(Color.rgb(180, 180, 180));
            noFilPane.getChildren().add(filText);
        }

        StackPane addFilePane = new StackPane();
        addFilePane.setAlignment(Pos.CENTER_RIGHT);
        fileBox.getChildren().add(addFilePane);

        Button fileButton = new Button("Add File");
        fileButton.setPrefWidth(120);
        fileButton.setPrefHeight(25);
        fileButton.setCursor(Cursor.HAND);
        fileButton.setStyle("-fx-background-color: Dodgerblue; -fx-border-radius: 4;");
        fileButton.setTextFill(Color.WHITE);
        fileButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                File fileSelected = fileChooser.showOpenDialog(new Stage());
                if (fileSelected != null) {
                    uploadProgress();
                    File createFolder = new File("src/files/Tasks/" + id);
                    createFolder.mkdir();
                    String fileName = fileSelected.getName();
                    File destination = new File("src/files/Tasks/" + id + "/" + fileName);
                    try {
                        Path path = Files.copy(fileSelected.toPath(), destination.toPath(),
                                StandardCopyOption.REPLACE_EXISTING);
                    } catch (Exception e) {
                        System.out.print(e);
                    }
                }
            }

        });
        addFilePane.getChildren().add(fileButton);

        // User

        VBox userBox = new VBox();
        aboutBox.getChildren().add(userBox);

        FlowPane userFlowPane = new FlowPane();
        userFlowPane.setPadding(new Insets(0, 0, 5, 0));
        userFlowPane.setStyle("-fx-border-width: 0 0 2 0; -fx-border-color: rgb(214, 214, 214);");
        userBox.getChildren().add(userFlowPane);

        Text userText = new Text("Unassigned users");
        userText.setFont(Font.font("arial Rounded MT Bold", FontWeight.LIGHT, 17));
        userText.setFill(Color.rgb(31, 31, 31));
        userFlowPane.getChildren().add(userText);

        VBox userVBox = new VBox();
        userVBox.setPadding(new Insets(10, 0, 10, 0));
        userVBox.setSpacing(5);
        userBox.getChildren().add(userVBox);

        try {
            String query = "SELECT user_id FROM team_people WHERE team_id = '" + team_Id + "' AND user_id != '"
                    + ownerID + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                userSet.add(result.getString("user_id"));
            }
        } catch (Exception e) {
            System.out.print(e);
        }

        if (userSet.isEmpty() != true) {
            for (String each : userSet) {
                String username = getTaskString("username", "users", each);
                Boolean assignedBoolean = false;
                try {
                    String query = "SELECT id FROM team_tasks WHERE assigned_id = '" + each + "' AND task_id = '" + id
                            + "'";
                    result = output.executeQuery(query);
                    while (result.next()) {
                        assignedBoolean = true;
                    }
                } catch (Exception e) {
                    System.out.print(e);
                }
                if (assignedBoolean == false) {
                    CheckBox checkBox = new CheckBox(username);
                    userList.add(checkBox);
                    checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

                        @Override
                        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue,
                                Boolean newBoolean) {
                            if (newBoolean == true) {
                                userMap.put(each, checkBox);
                            } else {
                                userMap.remove(each);
                            }
                        }

                    });
                    userVBox.getChildren().add(checkBox);
                }
            }
        } else {
            FlowPane noUserPane = new FlowPane();
            noUserPane.setAlignment(Pos.CENTER);
            noUserPane.setPadding(new Insets(5));
            userVBox.getChildren().add(noUserPane);

            Text noUserText = new Text("No Assigned User");
            noUserText.setFont(Font.font("arial Rounded MT Bold", FontWeight.LIGHT, 16));
            noUserText.setFill(Color.rgb(180, 180, 180));
            noUserPane.getChildren().add(noUserText);
        }

        BorderPane useBorderPane = new BorderPane();
        userBox.getChildren().add(useBorderPane);

        StackPane markAllPane = new StackPane();
        markAllPane.setAlignment(Pos.CENTER_LEFT);
        useBorderPane.setLeft(markAllPane);

        CheckBox markAllBox = new CheckBox("Mark all");
        markAllBox.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 17));
        markAllBox.setTextFill(Color.rgb(140, 140, 140));
        markAllBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean newBoolean) {
                if (newBoolean == true) {
                    for (CheckBox userAllBox : userList) {
                        userAllBox.setSelected(true);
                    }
                } else {
                    for (CheckBox userAllBox : userList) {
                        userAllBox.setSelected(false);
                    }
                }
            }

        });
        markAllPane.getChildren().add(markAllBox);

        StackPane assignPane = new StackPane();
        assignPane.setAlignment(Pos.CENTER_RIGHT);
        useBorderPane.setRight(assignPane);

        Button assignButton = new Button("Assign");
        assignButton.setPrefWidth(120);
        assignButton.setPrefHeight(25);
        assignButton.setCursor(Cursor.HAND);
        assignButton.setStyle("-fx-background-color: Dodgerblue; -fx-border-radius: 4;");
        assignButton.setTextFill(Color.WHITE);
        assignButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                for (Map.Entry<String, CheckBox> entry : userMap.entrySet()) {
                    String idString = getIdString("team_tasks");
                    try {
                        String query = "INSERT INTO `team_tasks` (`id`,`task_id`,`team_id`,`assigned_id`) VALUES('"
                                + idString + "','" + id + "','" + team_Id + "','" + entry.getKey() + "')";
                        output.executeUpdate(query);
                    } catch (Exception e) {
                        System.out.print(e);
                    }
                    userSet.add(entry.getKey());
                    userVBox.getChildren().remove(entry.getValue());
                    Tab tab = new Tab();
                    new userOnly(entry.getKey(), id, team_Id, tab);
                    userTabs.put(entry.getKey(), tab);

                    String username = getTaskString("username", "users", entry.getKey());
                    RadioButton usersRadioButton = new RadioButton(username);
                    usersRadioButton.setPadding(new Insets(2, 10, 2, 5));
                    usersRadioButton.setPrefWidth(390);
                    usersRadioButton.setCursor(Cursor.HAND);
                    usersRadioButton.setTextFill(Color.WHITE);
                    usersRadioButton.setToggleGroup(userGroup);
                    usersRadioButton.getStyleClass().add("usersRadio");
                    usersRadioButton.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent actionEvent) {
                            tabPane.getSelectionModel().select(userTab);
                            userTabPane.getSelectionModel().select(tab);
                            titlesText.setText("User Done Task");
                        }

                    });
                    usersVBox.getChildren().add(usersRadioButton);
                }
            }

        });
        assignPane.getChildren().add(assignButton);

        // Delete

        VBox deleteBox = new VBox();
        aboutBox.getChildren().add(deleteBox);

        FlowPane deletePane = new FlowPane();
        deletePane.setPadding(new Insets(0, 0, 5, 0));
        deletePane.setStyle("-fx-border-width: 0 0 2 0; -fx-border-color: rgb(214, 214, 214);");
        deleteBox.getChildren().add(deletePane);

        Text deleteText = new Text("Others");
        deleteText.setFont(Font.font("arial Rounded MT Bold", FontWeight.LIGHT, 17));
        deleteText.setFill(Color.rgb(31, 31, 31));
        deletePane.getChildren().add(deleteText);

        FlowPane buttonConfirm = new FlowPane();
        buttonConfirm.setAlignment(Pos.CENTER);
        buttonConfirm.setPadding(new Insets(13, 0, 10, 0));
        deleteBox.getChildren().add(buttonConfirm);

        Button deleteButton = new Button("Delete");
        deleteButton.setPrefWidth(120);
        deleteButton.setPrefHeight(25);
        deleteButton.setCursor(Cursor.HAND);
        deleteButton.setStyle("-fx-background-color: #3692ee; -fx-border-radius: 4;");
        deleteButton.setTextFill(Color.WHITE);
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                deleteDialog();
            }

        });
        buttonConfirm.getChildren().add(deleteButton);

        // Container

        StackPane containerPane = new StackPane();
        borderPane.setCenter(containerPane);

        BorderPane containPane = new BorderPane();
        containerPane.getChildren().add(containPane);

        subContent(containPane);

        // Content Pane

        StackPane contentPane = new StackPane();
        containPane.setCenter(contentPane);

        contentFunction(contentPane);

        //

        Scene scene = new Scene(stackPane, 1300, 700);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Task - " + name);
        stage.getIcons().add(new Image("files/icons/group.png"));
        stage.show();
    }

    public void deleteDialog() {
        deleteStage = new Stage();

        StackPane stackPane = new StackPane();
        stackPane.setPadding(new Insets(5));
        stackPane.setStyle("-fx-background-color: transparent;");

        StackPane pane = new StackPane();
        pane.setPadding(new Insets(10, 10, 20, 10));
        pane.setEffect(new DropShadow(5, Color.GRAY));
        pane.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        stackPane.getChildren().add(pane);

        VBox vBox = new VBox();
        vBox.setSpacing(15);
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
                deleteStage.setX(mouseEvent.getScreenX() - xSet);
                deleteStage.setY(mouseEvent.getScreenY() - ySet);
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
                deleteStage.close();
            }

        });
        cancelPane.getChildren().add(cancelButton);

        VBox textBox = new VBox();
        textBox.setSpacing(10);
        vBox.getChildren().add(textBox);

        StackPane titlePane = new StackPane();
        titlePane.setAlignment(Pos.CENTER);
        textBox.getChildren().add(titlePane);

        Text text = new Text("Delete This Task ??");
        text.setFont(Font.font("Arial Rounded MT Bold", 30));
        text.setFill(Color.rgb(51, 51, 51));
        titlePane.getChildren().add(text);

        StackPane discriptionPane = new StackPane();
        discriptionPane.setAlignment(Pos.CENTER);
        textBox.getChildren().add(discriptionPane);

        String discription = "If you delete this task all files, messages and relavent data associated with this task will be deleted";
        Text discriptionText = new Text(discription);
        discriptionText.setWrappingWidth(350);
        discriptionText.setTextAlignment(TextAlignment.CENTER);
        discriptionText.setFont(Font.font("Arial Rounded MT Bold", 16));
        discriptionText.setFill(Color.GRAY);
        discriptionPane.getChildren().add(discriptionText);

        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(hBox);

        Button proceedButton = new Button("Proceed");
        proceedButton.setPrefSize(100, 25);
        proceedButton.setStyle("-fx-background-color: dodgerblue;");
        proceedButton.setTextFill(Color.WHITE);
        proceedButton.setCursor(Cursor.HAND);
        proceedButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                deleteFunction();
            }

        });

        Button nopButton = new Button("Cancel");
        nopButton.setPrefSize(100, 25);
        nopButton.setStyle("-fx-background-color: dodgerblue;");
        nopButton.setTextFill(Color.WHITE);
        nopButton.setCursor(Cursor.HAND);
        nopButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                deleteStage.close();
            }

        });

        hBox.getChildren().addAll(proceedButton, nopButton);

        Scene deleteScene = new Scene(stackPane, 500, 230);
        deleteScene.setFill(Color.TRANSPARENT);

        deleteStage.setScene(deleteScene);
        deleteStage.initStyle(StageStyle.TRANSPARENT);
        deleteStage.setTitle("Team Work Hub - Add Task");
        deleteStage.getIcons().add(new Image("files/icons/group.png"));
        deleteStage.initModality(Modality.APPLICATION_MODAL);
        deleteStage.showAndWait();
    }

    public void deleteFunction() {
        try {
            String query = "DELETE FROM task_files WHERE task_id = '" + id + "'";
            output.executeUpdate(query);
            query = "DELETE FROM task_messages WHERE task_id = '" + id + "'";
            output.executeUpdate(query);
            query = "DELETE FROM task_user_files WHERE task_id = '" + id + "'";
            output.executeUpdate(query);
            query = "DELETE FROM tasks WHERE id = '" + id + "'";
            output.executeUpdate(query);
            query = "DELETE FROM team_tasks WHERE task_id = '" + id + "'";
            output.executeUpdate(query);
            query = "DELETE FROM to_do_task WHERE task_id = '" + id + "'";
            output.executeUpdate(query);
            query = "DELETE FROM update_task WHERE task_id = '" + id + "'";
            output.executeUpdate(query);
        } catch (Exception e) {
            System.out.print(e);
        }
        teams.getDeletedID = id;
        App.taskDeleteProperty.set(!App.taskDeleteProperty.get());
        deleteStage.close();
        stage.close();
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

    public void editFunction() {
        StackPane stackPane = new StackPane();
        stackPane.setPadding(new Insets(5));
        stackPane.setStyle("-fx-background-color: transparent;");

        StackPane pane = new StackPane();
        pane.setPadding(new Insets(10, 10, 20, 10));
        pane.setEffect(new DropShadow(5, Color.GRAY));
        pane.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        stackPane.getChildren().add(pane);

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
                editStage.setX(mouseEvent.getScreenX() - xSet);
                editStage.setY(mouseEvent.getScreenY() - ySet);
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
                editStage.close();
            }

        });
        cancelPane.getChildren().add(cancelButton);

        StackPane titlePane = new StackPane();
        titlePane.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().add(titlePane);

        Text text = new Text("Edit Task Content");
        text.setFont(Font.font("Arial Rounded MT Bold", 17));
        text.setFill(Color.rgb(31, 31, 31));
        titlePane.getChildren().add(text);

        StackPane contentPane = new StackPane();
        vBox.getChildren().add(contentPane);

        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setText(content);
        contentPane.getChildren().add(textArea);

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        vBox.getChildren().add(buttonBox);

        Button button = new Button("Edit");
        button.setPrefSize(100, 25);
        button.setStyle("-fx-background-color: dodgerblue;");
        button.setTextFill(Color.WHITE);
        button.setCursor(Cursor.HAND);
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                if (!content.equals(textArea.getText()) && !textArea.getText().equals("")) {
                    try {
                        String query = "UPDATE tasks SET content = '" + textArea.getText() + "' WHERE id = '" + id
                                + "'";
                        output.executeUpdate(query);
                        tasksText.setText(textArea.getText());
                        content = textArea.getText();
                    } catch (Exception e) {
                        System.out.print(e);
                    }
                    editStage.close();
                }
            }

        });

        Button theButton = new Button("Cancel");
        theButton.setPrefSize(100, 25);
        theButton.setStyle("-fx-background-color: dodgerblue;");
        theButton.setTextFill(Color.WHITE);
        theButton.setCursor(Cursor.HAND);
        theButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                editStage.close();
            }

        });

        buttonBox.getChildren().addAll(button, theButton);

        Scene editScene = new Scene(stackPane, 500, 210);
        editScene.setFill(Color.TRANSPARENT);

        editStage = new Stage();
        editStage.setScene(editScene);
        editStage.initStyle(StageStyle.TRANSPARENT);
        editStage.setTitle("Team Work Hub - Add Task");
        editStage.getIcons().add(new Image("files/icons/group.png"));
        editStage.initModality(Modality.APPLICATION_MODAL);
        editStage.showAndWait();
    }

    public void subContent(BorderPane containPane) {

        StackPane userPane = new StackPane();
        userPane.setMaxWidth(390);
        userPane.setMinWidth(390);
        userPane.setEffect(new DropShadow(3, Color.GRAY));
        userPane.setStyle("-fx-background-color: white; -fx-border-width: 0 0 1 0;");
        containPane.setLeft(userPane);

        BorderPane menuBorderPane = new BorderPane();
        userPane.getChildren().add(menuBorderPane);

        StackPane menuPane = new StackPane();
        menuPane.setEffect(new DropShadow(3, Color.GRAY));
        menuPane.setStyle("-fx-background-color: #222; -fx-border-width: 0 0 1 0;");
        menuBorderPane.setTop(menuPane);

        ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton todoButton = new RadioButton("To-do List");
        menuRadioButton(todoButton, toggleGroup);
        todoButton.setSelected(true);
        RadioButton workButton = new RadioButton("My Work");
        menuRadioButton(workButton, toggleGroup);

        workButton.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean argBoolean) {
                if (argBoolean == true) {
                    tabPane.getSelectionModel().select(workTab);
                    titlesText.setText("My Work");
                }
            }

        });
        todoButton.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean argBoolean) {
                if (argBoolean == true) {
                    tabPane.getSelectionModel().select(todoTab);
                    titlesText.setText("To do Task");
                }
            }

        });

        StackPane workPane = new StackPane();
        workPane.getChildren().add(workButton);

        StackPane todoPane = new StackPane();
        todoPane.getChildren().add(todoButton);

        VBox menuBox = new VBox();
        menuBox.setPadding(new Insets(7, 5, 7, 5));
        menuBox.getChildren().addAll(todoPane, workPane);
        menuPane.getChildren().add(menuBox);

        StackPane assignedPane = new StackPane();
        assignedPane.setPadding(new Insets(10));
        assignedPane.setStyle("-fx-background-color: #555;");
        menuBorderPane.setCenter(assignedPane);

        ScrollPane usersScrollPane = new ScrollPane();
        usersScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        usersScrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        usersScrollPane.setFitToWidth(true);
        usersScrollPane.setStyle("-fx-background-color: transparent;");
        usersScrollPane.getStyleClass().add("task-scroll");
        assignedPane.getChildren().add(usersScrollPane);

        usersVBox = new VBox();
        usersScrollPane.setContent(usersVBox);

        try {
            String query = "SELECT assigned_id FROM team_tasks WHERE task_id = '" + id + "' AND team_id = '" + team_Id
                    + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                userIdSet.add(result.getString("assigned_id"));
                assignedSet.add(result.getString("assigned_id"));
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        try {
            String query = "SELECT user_id FROM to_do_task WHERE task_id = '" + id + "' AND team_id = '" + team_Id
                    + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                int x = 0;
                for (String idInSet : userIdSet) {
                    if (idInSet.equals(result.getString("user_id"))) {
                        x++;
                    }
                }
                if (x == 0) {
                    userIdSet.add(result.getString("user_id"));
                }
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        if (userIdSet.isEmpty() != true) {
            for (String idInSet : userIdSet) {
                String username = "";
                try {
                    String query = "SELECT username FROM users WHERE id = '" + idInSet + "'";
                    result = output.executeQuery(query);
                    while (result.next()) {
                        username = result.getString("username");
                    }
                } catch (Exception e) {
                    System.out.print(e);
                }

                RadioButton usersRadioButton = new RadioButton(username);
                usersRadioButton.setPadding(new Insets(2, 10, 2, 5));
                usersRadioButton.setPrefWidth(390);
                usersRadioButton.setCursor(Cursor.HAND);
                usersRadioButton.setTextFill(Color.WHITE);
                usersRadioButton.getStyleClass().remove("radio-button");
                usersRadioButton.getStyleClass().add("usersRadio");
                usersRadioButton.setToggleGroup(userGroup);
                usersRadioButton.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        tabPane.getSelectionModel().select(userTab);
                        for (Map.Entry<String, Tab> entry : userTabs.entrySet()) {
                            if (idInSet.equals(entry.getKey())) {
                                userTabPane.getSelectionModel().select(entry.getValue());
                                titlesText.setText("User Done Task");
                            }
                        }
                    }

                });
                usersVBox.getChildren().add(usersRadioButton);
                Tab tab = new Tab();
                userTabs.put(idInSet, tab);
                todoMap.put(idInSet, username);
            }
        } else {
            Text nonText = new Text("No assigned member found or to-do task users");
            nonText.setWrappingWidth(200);
            nonText.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 18));
            nonText.setFill(Color.GRAY);
            assignedPane.getChildren().add(nonText);
        }

        StackPane commentPane = new StackPane();
        commentPane.setStyle("-fx-background-color: #333; -fx-border-width: 1 1 1 1;");
        commentPane.setCursor(Cursor.HAND);
        commentPane.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                tabPane.getSelectionModel().select(commenTab);
                titlesText.setText("Task Comment");
            }

        });
        menuBorderPane.setBottom(commentPane);

        HBox commentBox = new HBox();
        commentBox.setPadding(new Insets(10));
        commentBox.setSpacing(7);
        commentBox.setAlignment(Pos.CENTER);
        commentPane.getChildren().add(commentBox);

        ImageView commentView = new ImageView(new Image("files/icons/chat_2.png"));
        commentView.setFitWidth(30);
        commentView.setFitHeight(30);

        Text commentText = new Text("Comments");
        commentText.setFont(Font.font("Arial Rounded MT Bold", 20));
        commentText.setFill(Color.GRAY);

        commentBox.getChildren().addAll(commentView, commentText);
    }

    public void contentFunction(StackPane contentPane) {

        BorderPane contentBorderPane = new BorderPane();
        contentPane.getChildren().add(contentBorderPane);

        StackPane topPane = new StackPane();
        topPane.setStyle("-fx-background-color: rgb(254, 254, 254);");
        topPane.setPadding(new Insets(0, 0, 5, 0));
        contentBorderPane.setTop(topPane);

        BorderPane topBorderPane = new BorderPane();
        topBorderPane.setPadding(new Insets(10));
        topBorderPane.setEffect(new DropShadow(5, Color.GRAY));
        topBorderPane.setStyle("-fx-background-color: white;");
        topPane.getChildren().add(topBorderPane);

        StackPane titlePane = new StackPane();
        titlePane.setAlignment(Pos.CENTER_LEFT);
        topBorderPane.setLeft(titlePane);

        titlesText = new Text("To-Do List");
        titlesText.setFont(Font.font("Arial Rounded MT Bold", 20));
        titlesText.setFill(Color.GRAY);
        titlePane.getChildren().add(titlesText);

        StackPane imagePane = new StackPane();
        imagePane.setAlignment(Pos.CENTER_RIGHT);
        topBorderPane.setRight(imagePane);

        MenuItem refreshItem = new MenuItem("Refresh");
        MenuItem commentItem = new MenuItem("Comment");
        MenuItem removeItem = new MenuItem("Remove task");

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(refreshItem, commentItem, removeItem);

        ImageView imageView = new ImageView(new Image("files/icons/option_1.png"));
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        imageView.setCursor(Cursor.HAND);
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                contextMenu.show(imageView, mouseEvent.getScreenX(), mouseEvent.getScreenY());
            }

        });
        imagePane.getChildren().add(imageView);

        StackPane centerPane = new StackPane();
        centerPane.setStyle("-fx-background-color: rgb(254, 254, 254);");
        contentBorderPane.setCenter(centerPane);

        tabPane = new TabPane();
        tabPane.getStyleClass().add("main-tab");
        centerPane.getChildren().add(tabPane);

        todoTab = new Tab("todo");
        workTab = new Tab("myWork");
        userTab = new Tab("userTab");
        onlyTab = new Tab("userOnly");
        commenTab = new Tab("comment");
        tabPane.getTabs().addAll(todoTab, workTab, userTab, commenTab);

        // comment
        new comment_Task(commenTab, id, team_Id, App.id);
        new my_work(workTab, id, team_Id);
        new todoTask(id, team_Id, todoTab);

        usersOnly();

    }

    public void usersOnly() {
        StackPane stackPane = new StackPane();
        userTab.setContent(stackPane);

        userTabPane = new TabPane();
        stackPane.getChildren().add(userTabPane);

        for (Map.Entry<String, Tab> entry : userTabs.entrySet()) {
            userTabPane.getTabs().add(entry.getValue());
            new userOnly(entry.getKey(), id, team_Id, entry.getValue());
        }
    }

    public String getTimeString(String oldTime) {
        Timestamp timestamp = Timestamp.valueOf(oldTime);
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm a,  dd MMMM");
        String dateString = format.format(date);
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
        if (getString.equals("")) {
            getString = "Username";
        }
        return getString;
    }

    public void menuRadioButton(RadioButton radioButton, ToggleGroup toggleGroup) {
        radioButton.setPadding(new Insets(8, 10, 8, 10));
        radioButton.setTextFill(Color.rgb(200, 200, 200));
        radioButton.setPrefWidth(380);
        radioButton.setCursor(Cursor.HAND);
        radioButton.setToggleGroup(toggleGroup);
        radioButton.setFont(Font.font("Arial Rounded MT Bold", 18));
        radioButton.getStyleClass().remove("radio-button");
        radioButton.getStyleClass().add("task_menu_button");
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
}
