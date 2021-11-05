import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
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
import javafx.scene.input.MouseButton;
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
import javafx.stage.FileChooser.ExtensionFilter;

public class teams {

    public static String getDeletedID;

    TabPane teamTabPane;
    Tab documentTab, taskTab, userTab, settingTab;

    StackPane tmpStackPane;

    public String id, name;
    public double xSet = 0;
    public double ySet = 0;

    Set<String> idSet = new HashSet<String>();
    Set<String> taskSet = new TreeSet<String>();

    public Connection conn;
    public Statement output;
    public ResultSet result;

    Stage taskStage, popupStage, loadStage;
    VBox taskBox;
    public int selectedFile;
    public String fileSelected = "";

    public teams(Tab teamTab, String teamId) {

        id = teamId;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/team work hub", "root", "mydatabase");
            output = conn.createStatement();
            String query = "SELECT name From teams WHERE id = '" + id + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                name = result.getString("name");
            }
        } catch (Exception e) {
            System.out.print(e);
        }

        teamContent(teamTab);
        App.taskDeleteProperty.addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                for (Map.Entry<String, StackPane> entry : App.taskMap.entrySet()) {
                    if (getDeletedID.equals(entry.getKey())) {
                        taskBox.getChildren().remove(entry.getValue());
                    }
                }
            }

        });
    }

    public void teamContent(Tab teamTab) {

        StackPane contPane = new StackPane();
        teamTab.setContent(contPane);

        BorderPane contentBorderPane = new BorderPane();
        contPane.getChildren().add(contentBorderPane);

        StackPane menuPane = new StackPane();
        menuPane.setStyle(
                "-fx-border-width:0 0 2 0; -fx-border-style:solid; -fx-border-color:rgba(177, 174, 174, 0.433);");
        contentBorderPane.setTop(menuPane);

        HBox menuBox = new HBox();
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setSpacing(20);
        menuBox.setPadding(new Insets(10));
        menuPane.getChildren().add(menuBox);

        ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton taskButton = new RadioButton("Tasks");
        taskButton.setSelected(true);
        taskButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                teamTabPane.getSelectionModel().select(taskTab);
            }

        });
        radiosButton(taskButton, toggleGroup);

        RadioButton tableButton = new RadioButton("Spreadsheets");
        radiosButton(tableButton, toggleGroup);

        RadioButton filesButton = new RadioButton("Document");
        filesButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                teamTabPane.getSelectionModel().select(documentTab);
            }

        });
        radiosButton(filesButton, toggleGroup);

        RadioButton messageButton = new RadioButton("Notification");
        radiosButton(messageButton, toggleGroup);

        RadioButton userButton = new RadioButton("People");
        userButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                teamTabPane.getSelectionModel().select(userTab);
            }

        });
        radiosButton(userButton, toggleGroup);

        RadioButton optionButton = new RadioButton("Option");
        optionButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                teamTabPane.getSelectionModel().select(settingTab);
            }

        });
        radiosButton(optionButton, toggleGroup);

        menuBox.getChildren().addAll(taskButton, filesButton, userButton, optionButton);

        StackPane contentPane = new StackPane();
        contentBorderPane.setCenter(contentPane);

        teamTabPane = new TabPane();
        contentPane.getChildren().add(teamTabPane);

        taskTab = new Tab();
        taskFunction(taskTab);

        documentTab = new Tab();
        new documents(id, documentTab);

        userTab = new Tab();
        new teamPeople(id, userTab);

        settingTab = new Tab();
        new teamSettings(id, settingTab);

        teamTabPane.getTabs().addAll(taskTab, documentTab, userTab, settingTab);

    }

    public void taskFunction(Tab taskTab) {
        StackPane taskPane = new StackPane();
        taskTab.setContent(taskPane);

        BorderPane taskBorderPane = new BorderPane();
        taskPane.getChildren().add(taskBorderPane);

        StackPane taskContentPane = new StackPane();
        taskBorderPane.setCenter(taskContentPane);

        // Task Box

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:transparent");
        scrollPane.getStyleClass().add("task-scroll");
        taskContentPane.getChildren().add(scrollPane);

        taskBox = new VBox();
        taskBox.setPadding(new Insets(10, 10, 10, 10));
        taskBox.setSpacing(10);
        scrollPane.setContent(taskBox);

        FlowPane allTaskPane = new FlowPane();
        allTaskPane.setPadding(new Insets(0, 10, 0, 10));
        taskBox.getChildren().add(0, allTaskPane);

        Text allTaskText = new Text("All Task");
        allTaskText.setFill(Color.GRAY);
        allTaskText.setFont(Font.font("Arial Rounded MT Bold", 17));
        allTaskPane.getChildren().add(allTaskText);

        allTask(true);

        StackPane taskBottomPane = new StackPane();
        taskBottomPane.setAlignment(Pos.CENTER_RIGHT);
        taskBottomPane.setPadding(new Insets(10));
        taskBottomPane.setStyle(
                "-fx-border-width: 2 0 0 0; -fx-border-style:solid; -fx-border-color:rgba(177, 174, 174, 0.533);");
        taskBorderPane.setBottom(taskBottomPane);

        Button addButton = new Button("Add Task");
        addButton.setStyle("-fx-background-color:dodgerblue;");
        addButton.setTextFill(Color.WHITE);
        addButton.setPrefWidth(120);
        addButton.setCursor(Cursor.HAND);
        addButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                addTask();
            }

        });
        taskBottomPane.getChildren().add(addButton);
    }

    public void allTask(boolean cond) {
        String tmp_ID = "";
        try {
            String query = "SELECT id FROM tasks WHERE team_id = '" + id + "' ORDER BY time";
            if (cond == false) {
                query = "SELECT id FROM tasks WHERE team_id = '" + id + "' ORDER BY time DESC LIMIT 1";
            }
            result = output.executeQuery(query);
            while (result.next()) {
                taskSet.add(result.getString("id"));
                if (cond == false) {
                    tmp_ID = result.getString("id");
                }
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        if (cond != false) {
            for (String eachId : taskSet) {
                taskDetails(eachId);
            }
        } else {
            if (!tmp_ID.equals("")) {
                taskDetails(tmp_ID);
            }
        }
    }

    public void taskDetails(String eachId) {
        String taskID = eachId;
        String taskName = getString("name", "tasks", eachId);
        String taskContent = getString("content", "tasks", eachId);
        String taskTime = getString("time", "tasks", eachId);

        Label label = new Label("View more");
        label.setMinWidth(170);

        Label label1 = new Label("Delete task");
        label1.setMinWidth(170);

        MenuItem moreItem = new MenuItem("");
        moreItem.setGraphic(label);

        MenuItem deletItem = new MenuItem("");
        deletItem.setGraphic(label1);

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setMinWidth(300);
        contextMenu.setPrefWidth(300);
        contextMenu.setStyle("-fx-border-radius:3; -fx-background-radius:3;");
        contextMenu.getStyleClass().add("task-menus");
        contextMenu.getItems().addAll(moreItem, deletItem);

        StackPane tasksPane = new StackPane();
        tasksPane.setPadding(new Insets(7, 10, 7, 10));
        tasksPane.setEffect(new DropShadow(3, Color.rgb(180, 180, 180)));
        tasksPane.setStyle("-fx-background-color:white;-fx-border-radius:4; -fx-background-radius:4;");
        tasksPane.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                MouseButton mouseButton = mouseEvent.getButton();
                if (mouseButton == MouseButton.SECONDARY) {
                    contextMenu.show(tasksPane, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                }
                if (mouseButton == MouseButton.PRIMARY) {
                    contextMenu.hide();
                }
            }

        });
        taskBox.getChildren().add(1, tasksPane);
        App.taskMap.put(eachId, tasksPane);

        moreItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                new task(taskID, id);
            }

        });

        deletItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                tmpStackPane = tasksPane;
                deleteDialog(taskID);
            }

        });

        VBox tasksBox = new VBox();
        tasksBox.setSpacing(3);
        tasksPane.getChildren().add(tasksBox);

        // Title

        FlowPane taskNamePane = new FlowPane();
        tasksBox.getChildren().add(taskNamePane);

        Text taskText = new Text(taskName);
        taskText.setFill(Color.rgb(51, 51, 51));
        taskText.setFont(Font.font("Arial Rounded MT Bold", FontWeight.LIGHT, 16));
        taskNamePane.getChildren().add(taskText);

        // Content

        FlowPane taskContPane = new FlowPane();
        taskContPane.setPadding(new Insets(0, 0, 3, 0));
        tasksBox.getChildren().add(taskContPane);

        Text taskText2 = new Text(taskContent);
        taskText2.setFill(Color.GRAY);
        taskText2.setFont(Font.font("", 16));
        taskContPane.getChildren().add(taskText2);

        BorderPane statusPane = new BorderPane();
        tasksBox.getChildren().add(statusPane);

        FlowPane statusFlowPane = new FlowPane();
        statusFlowPane.setAlignment(Pos.CENTER_LEFT);
        statusPane.setLeft(statusFlowPane);

        // Button

        Button taskButton = new Button("More");
        taskButton.setMinWidth(100);
        taskButton.setMaxHeight(22);
        taskButton.setPadding(new Insets(2));
        taskButton.setStyle("-fx-background-color:dodgerblue;");
        taskButton.setTextFill(Color.WHITE);
        taskButton.setCursor(Cursor.HAND);
        taskButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                new task(taskID, id);
            }

        });
        statusFlowPane.getChildren().add(taskButton);

        FlowPane datePane = new FlowPane();
        datePane.setAlignment(Pos.CENTER_RIGHT);
        statusPane.setRight(datePane);

        Timestamp timestamp = Timestamp.valueOf(taskTime);
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm a,  dd MMMM");
        String dateString = format.format(date);

        Text dateText = new Text(dateString);
        dateText.setFill(Color.GRAY);
        dateText.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 16));
        datePane.getChildren().add(dateText);
    }

    public String isAssigned(String taskID, String userID) {
        int a = 0;

        Statement tmp_Statement;
        ResultSet tmp_ResultSet;

        try {
            tmp_Statement = conn.createStatement();
            String query = "SELECT id FROM team_tasks WHERE task_id = '" + taskID + "' AND assigned_id = '" + userID
                    + "'";
            tmp_ResultSet = tmp_Statement.executeQuery(query);
            while (tmp_ResultSet.next()) {
                a++;
            }
        } catch (Exception e) {
            System.out.print(e);
        }

        if (a != 0) {
            return "Assigned";
        } else {
            return "Not Assigned";
        }
    }

    public void addTask() {

        // Global Objects

        Map<String, File> fileMap = new TreeMap<String, File>();
        Map<String, String> userMap = new TreeMap<String, String>();

        List<CheckBox> checkBoxs = new ArrayList<CheckBox>();

        ListView<String> listView = new ListView<String>();
        listView.getStyleClass().add("task_file_view");
        listView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            selectedFile = listView.getSelectionModel().getSelectedIndex();
            fileSelected = newValue;
        });

        // Content

        StackPane mainPane = new StackPane();
        mainPane.setPadding(new Insets(10));
        mainPane.setStyle("-fx-background-color:transparent;");

        StackPane stackPane = new StackPane();
        stackPane.setEffect(new DropShadow(5, Color.BLACK));
        stackPane.setStyle("-fx-background-color:white; -fx-border-radius:5; -fx-background-radius:5;");
        stackPane.setPadding(new Insets(10, 20, 10, 20));
        mainPane.getChildren().add(stackPane);

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        stackPane.getChildren().add(vBox);

        FlowPane cancePane = new FlowPane();
        cancePane.setAlignment(Pos.CENTER_RIGHT);
        cancePane.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                xSet = event.getSceneX();
                ySet = event.getSceneY();
            }

        });
        cancePane.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                taskStage.setX(event.getScreenX() - xSet);
                taskStage.setY(event.getScreenY() - ySet);
            }

        });
        vBox.getChildren().add(cancePane);

        ImageView imageView = new ImageView(new Image("files/icons/close.png"));
        imageView.setFitHeight(15);
        imageView.setFitWidth(15);
        imageView.setCursor(Cursor.HAND);
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                taskStage.close();
            }

        });
        cancePane.getChildren().add(imageView);

        // title

        FlowPane titlePane = new FlowPane();
        titlePane.setAlignment(Pos.CENTER);
        titlePane.setPadding(new Insets(15));
        vBox.getChildren().add(titlePane);

        Text titleText = new Text("Add Task");
        titleText.setFill(Color.DODGERBLUE);
        titleText.setFont(Font.font("Arial Rounded MT Bold", 24));
        titlePane.getChildren().add(titleText);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);

        Text titleText2 = new Text("Title");
        titleText2.setFill(Color.GRAY);
        titleText2.setFont(Font.font("Arial Rounded MT Bold", 17));

        TextField textField = new TextField();
        textField.setMinWidth(300);
        textField.setPrefWidth(400);
        textField.setPromptText("Task title");

        hBox.setSpacing(30);
        hBox.getChildren().addAll(titleText2, textField);
        vBox.getChildren().add(hBox);

        VBox contentBox = new VBox();
        contentBox.setSpacing(4);
        vBox.getChildren().add(contentBox);

        Text text = new Text("Content");
        text.setFill(Color.GRAY);
        text.setFont(Font.font("Arial Rounded MT Bold", 17));

        TextArea textArea = new TextArea();
        textArea.setMaxHeight(100);
        textArea.setPromptText("Type content");

        contentBox.getChildren().addAll(text, textArea);

        VBox fileBox = new VBox();
        fileBox.setSpacing(7);
        vBox.getChildren().add(fileBox);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));

        Button fileButton = new Button("Add File");
        fileButton.setStyle("-fx-background-color:dodgerblue;");
        fileButton.setCursor(Cursor.HAND);
        fileButton.setPrefWidth(100);
        fileButton.setTextFill(Color.WHITE);
        fileButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                File tmp_File = fileChooser.showOpenDialog(new Stage());
                if (tmp_File != null) {
                    String tmp_Name = tmp_File.getName();
                    listView.getItems().add(tmp_Name);
                    fileMap.put(tmp_Name, tmp_File);
                }
            }

        });

        StackPane filePane = new StackPane();
        filePane.setMaxHeight(100);
        filePane.setMinHeight(100);
        filePane.setEffect(new DropShadow(4, Color.GRAY));
        filePane.setStyle(
                "-fx-border-radius:4; -fx-background-radius:4; -fx-border-style:solid; -fx-border-color:grey;");
        filePane.getChildren().add(listView);

        FlowPane fileFlowPane = new FlowPane();
        fileFlowPane.setAlignment(Pos.CENTER_RIGHT);

        Button removeButton = new Button("Remove");
        removeButton.setStyle("-fx-background-color:deepskyblue;");
        removeButton.setCursor(Cursor.HAND);
        removeButton.setPrefWidth(100);
        removeButton.setTextFill(Color.WHITE);
        removeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                boolean x = fileMap.isEmpty();
                if (x == false) {
                    if (!fileSelected.equals("")) {
                        listView.getItems().remove(selectedFile);
                        for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                            if (entry.getKey().equals(fileSelected)) {
                                fileMap.remove(entry.getKey());
                                fileSelected = "";
                            }
                        }
                    }
                }
            }

        });
        fileFlowPane.getChildren().add(removeButton);

        fileBox.getChildren().addAll(fileButton, filePane, fileFlowPane);

        VBox assignBox = new VBox();
        assignBox.setSpacing(7);
        vBox.getChildren().add(assignBox);

        Text assignText = new Text("Assigned People");
        assignText.setFont(Font.font("Arial Rounded MT Bold", 17));
        assignText.setFill(Color.GRAY);

        StackPane assignPane = new StackPane();
        assignPane.setMinHeight(100);
        assignPane.setMaxHeight(100);
        assignPane.setStyle(
                "-fx-border-width: 2 0 2 0; -fx-border-style:solid; -fx-border-color:rgba(177, 174, 174, 0.533);");

        int a = 0;

        try {
            String query = "SELECT user_id FROM team_people WHERE team_id = '" + id + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                String user_id = result.getString("user_id");
                idSet.add(user_id);
                a++;
            }
        } catch (Exception e) {
            System.out.print(e);
        }

        if (a != 0) {

            VBox userBox = new VBox();
            userBox.setPadding(new Insets(5, 0, 5, 0));
            userBox.setSpacing(5);
            assignPane.getChildren().add(userBox);

            for (String idString : idSet) {
                try {
                    String queryString = "SELECT username FROM users WHERE id = '" + idString + "'";
                    result = output.executeQuery(queryString);
                    while (result.next()) {
                        String username = result.getString("username");
                        CheckBox userCheckBox = new CheckBox(username);
                        checkBoxs.add(userCheckBox);
                        userCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

                            @Override
                            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue,
                                    Boolean newValue) {
                                if (newValue == true) {
                                    userMap.put(idString, username);
                                } else {
                                    userMap.remove(idString);
                                }
                            }

                        });
                        userBox.getChildren().add(userCheckBox);
                    }
                } catch (Exception e) {
                    System.out.print(e);
                }
            }
        } else {
            Text assignedText = new Text("No user Found");
            assignedText.setFill(Color.GRAY);
            assignedText.setFont(Font.font("Arial Rounded MT Bold", 17));
            assignPane.getChildren().add(assignedText);
        }

        FlowPane assignFlowPane = new FlowPane();
        assignFlowPane.setPadding(new Insets(0, 0, 10, 0));
        assignFlowPane.setAlignment(Pos.CENTER_LEFT);

        CheckBox allCheckBox = new CheckBox("Select All");
        allCheckBox.setFont(Font.font("Arial Rounded MT Bold", 16));
        allCheckBox.setTextFill(Color.GRAY);
        allCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
                if (newValue == true) {
                    for (CheckBox tmpBox : checkBoxs) {
                        tmpBox.setSelected(true);
                    }
                }
            }

        });
        assignFlowPane.getChildren().add(allCheckBox);

        assignBox.getChildren().addAll(assignText, assignPane, assignFlowPane);

        FlowPane submitPane = new FlowPane();
        submitPane.setAlignment(Pos.CENTER);
        vBox.getChildren().add(submitPane);

        Button submitButton = new Button("Add Task");
        submitButton.setPrefWidth(120);
        submitButton.setStyle("-fx-background-color:dodgerblue;");
        submitButton.setTextFill(Color.WHITE);
        submitButton.setCursor(Cursor.HAND);
        submitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {

                String titleString = textField.getText();
                String contentString = textArea.getText();
                if (!titleString.equals("") && !contentString.equals("")) {

                    taskStage.close();

                    int a = 0;
                    String idString = getIdString("tasks");
                    try {
                        String query = "INSERT INTO `tasks` (`id`,`team_id`,`id_owner`,`name`,`content`) VALUES ('"
                                + idString + "','" + id + "','" + App.id + "','" + titleString + "','" + contentString
                                + "')";
                        output.executeUpdate(query);
                        a++;
                    } catch (Exception e) {
                        System.out.print(e);
                        loadStage.close();
                        errorTask("Failed to add new Task");
                    }
                    if (a != 0) {
                        allTask(false);
                        if (userMap.isEmpty() == false) {
                            for (Map.Entry<String, String> entry : userMap.entrySet()) {
                                String tmpID = getIdString("team_tasks");
                                try {
                                    String query = "INSERT INTO `team_tasks` (`id`,`task_id`,`team_id`,`assigned_id`) VALUES ('"
                                            + tmpID + "','" + idString + "','" + id + "','" + entry.getKey() + "')";
                                    output.executeUpdate(query);
                                } catch (Exception e) {
                                    System.out.print(e);
                                }
                            }
                        }
                        if (fileMap.isEmpty() == false) {
                            // StackPane taskAdding = new StackPane();
                            // taskAdding.setStyle("-fx-background-color:transparent");
                            // taskAdding.setPadding(new Insets(10));

                            // StackPane contentPane = new StackPane();
                            // contentPane.setEffect(new DropShadow(4, Color.BLACK));
                            // contentPane.setStyle("-fx-background-color:white;");
                            // taskAdding.getChildren().add(contentPane);

                            // VBox loadBox = new VBox();
                            // loadBox.setSpacing(7);
                            // loadBox.setPadding(new Insets(10));
                            // loadBox.setStyle("-fx-background-color:white;");
                            // contentPane.getChildren().add(loadBox);

                            // FlowPane namePane = new FlowPane();
                            // namePane.setAlignment(Pos.CENTER_LEFT);
                            // loadBox.getChildren().add(namePane);

                            // Text nameText = new Text(name);
                            // nameText.setFont(Font.font("Arial Rounded MT Bold", 17));
                            // nameText.setFill(Color.GRAY);
                            // namePane.getChildren().add(nameText);

                            // BorderPane borderPane = new BorderPane();
                            // loadBox.getChildren().add(borderPane);

                            // FlowPane flowPane = new FlowPane();
                            // flowPane.setMaxWidth(200);
                            // flowPane.setAlignment(Pos.CENTER_LEFT);
                            // borderPane.setLeft(flowPane);

                            // Text title = new Text("Progress.....");
                            // title.setFont(Font.font("Arial Rounded MT Bold", 17));
                            // title.setFill(Color.rgb(32, 32, 32));
                            // flowPane.getChildren().add(title);

                            // FlowPane flowPane2 = new FlowPane();
                            // flowPane2.setMaxWidth(200);
                            // flowPane2.setAlignment(Pos.CENTER_RIGHT);
                            // borderPane.setRight(flowPane2);

                            // Text numText = new Text("Uploading file");
                            // numText.setFont(Font.font("Arial Rounded MT Bold", 17));
                            // numText.setFill(Color.GRAY);
                            // flowPane2.getChildren().add(numText);

                            // StackPane progressPane = new StackPane();
                            // loadBox.getChildren().add(progressPane);

                            // ProgressBar progressBar = new ProgressBar();
                            // progressBar.setPrefWidth(700);
                            // progressBar.setPrefHeight(27);
                            // progressPane.getChildren().add(progressBar);

                            // FlowPane waitPane = new FlowPane();
                            // waitPane.setAlignment(Pos.CENTER);
                            // loadBox.getChildren().add(waitPane);

                            // Text waitText = new Text("Please Wait");
                            // waitText.setFont(Font.font("Cooper Black", 20));
                            // waitText.setFill(Color.GRAY);
                            // waitPane.getChildren().add(waitText);

                            // Scene loadScene = new Scene(taskAdding, 600, 170);
                            // loadScene.setFill(Color.TRANSPARENT);

                            // loadStage = new Stage();
                            // loadStage.setScene(loadScene);
                            // loadStage.initStyle(StageStyle.TRANSPARENT);
                            // loadStage.setTitle("Adding Task...");
                            // loadStage.getIcons().add(new Image("files/icons/group.png"));
                            // loadStage.show();

                            // Task<Void> task = new Task<Void>() {

                            // @Override
                            // protected Void call() throws Exception {
                            // if (fileMap.isEmpty() == false) {
                            // File createFile = new File("src/files/Tasks/" + idString);
                            // createFile.mkdir();
                            // for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                            // String fileId = getIdString("task_files");
                            // File destination = new File(
                            // "src/files/Tasks/" + idString + "/" + entry.getKey());
                            // try {
                            // Path path = Files.copy(entry.getValue().toPath(), destination.toPath(),
                            // StandardCopyOption.REPLACE_EXISTING);
                            // String query = "INSERT INTO `task_files` (`id`,`task_id`,`folder`,`file`)
                            // VALUES ('"
                            // + fileId + "','" + idString + "','" + idString + "','"
                            // + entry.getKey() + "')";
                            // output.executeUpdate(query);
                            // } catch (Exception e) {
                            // System.out.print(e);
                            // }
                            // }
                            // loadStage.close();
                            // }
                            // return null;
                            // }
                            // };
                            // new Thread(task).start();
                            File createFile = new File("src/files/Tasks/" + idString);
                            createFile.mkdir();
                            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                                String fileId = getIdString("task_files");
                                File destination = new File("src/files/Tasks/" + idString + "/" + entry.getKey());
                                try {
                                    Path path = Files.copy(entry.getValue().toPath(), destination.toPath(),
                                            StandardCopyOption.REPLACE_EXISTING);
                                    String query = "INSERT INTO `task_files` (`id`,`task_id`,`folder`,`file`) VALUES ('"
                                            + fileId + "','" + idString + "','" + idString + "','" + entry.getKey()
                                            + "')";
                                    output.executeUpdate(query);
                                } catch (Exception e) {
                                    System.out.print(e);
                                }
                            }
                            // loadStage.close();
                        }
                    }

                } else {
                    String errorString = "Title & Content must be filled";
                    errorTask(errorString);
                }
            }

        });
        submitPane.getChildren().add(submitButton);

        Scene taskScene = new Scene(mainPane, 520, 700);
        taskScene.setFill(Color.TRANSPARENT);
        taskScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        taskStage = new Stage();
        taskStage.setScene(taskScene);
        taskStage.initStyle(StageStyle.TRANSPARENT);
        taskStage.setTitle("Team Work Hub - Add Task");
        taskStage.getIcons().add(new Image("files/icons/group.png"));
        taskStage.initModality(Modality.APPLICATION_MODAL);
        taskStage.showAndWait();
    }

    public void errorTask(String message) {
        StackPane mainPane = new StackPane();
        mainPane.setPadding(new Insets(10));
        mainPane.setStyle("-fx-background-color:transparent;");

        StackPane mainStackPane = new StackPane();
        mainStackPane.setEffect(new DropShadow(5, Color.BLACK));
        mainStackPane.setStyle("-fx-background-color:white; -fx-border-radius:5; -fx-background-radius:5;");
        mainPane.getChildren().add(mainStackPane);

        VBox contentBox = new VBox();
        contentBox.setAlignment(Pos.CENTER);
        mainPane.getChildren().add(contentBox);

        FlowPane imagePane = new FlowPane();
        imagePane.setAlignment(Pos.CENTER);
        contentBox.getChildren().add(imagePane);

        ImageView imageView = new ImageView(new Image("files/icons/sad.png"));
        imageView.setFitHeight(70);
        imageView.setFitWidth(70);
        imagePane.getChildren().add(imageView);

        FlowPane messagePane = new FlowPane();
        messagePane.setAlignment(Pos.CENTER);
        messagePane.setPadding(new Insets(20));
        contentBox.getChildren().add(messagePane);

        Text text = new Text(message);
        text.setFont(Font.font("Arial Rounded MT Bold", 20));
        text.setWrappingWidth(500);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFill(Color.GREY);
        messagePane.getChildren().add(text);

        FlowPane submitPane = new FlowPane();
        submitPane.setAlignment(Pos.CENTER);
        contentBox.getChildren().add(submitPane);

        Button button = new Button("OK");
        button.setPrefWidth(120);
        button.setStyle("-fx-background-color: dodgerblue");
        button.setTextFill(Color.WHITE);
        button.setCursor(Cursor.HAND);
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                popupStage.close();
            }

        });
        submitPane.getChildren().add(button);

        Scene popupsScene = new Scene(mainPane, 600, 270);
        popupsScene.setFill(Color.TRANSPARENT);

        popupStage = new Stage();
        popupStage.setTitle("Team Work Hub - Attention");
        popupStage.getIcons().add(new Image("files/icons/group.png"));
        popupStage.setScene(popupsScene);
        popupStage.initStyle(StageStyle.TRANSPARENT);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.showAndWait();
    }

    public void radiosButton(RadioButton radioButton, ToggleGroup toggleGroup) {
        radioButton.setAlignment(Pos.CENTER);
        radioButton.setPadding(new Insets(5));
        radioButton.setCursor(Cursor.HAND);
        radioButton.setPrefWidth(150);
        radioButton.setToggleGroup(toggleGroup);
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

    public void deleteDialog(String string) {
        Stage deleteStage = new Stage();

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

        Text text = new Text("Delete This User ??");
        text.setFont(Font.font("Arial Rounded MT Bold", 30));
        text.setFill(Color.rgb(51, 51, 51));
        titlePane.getChildren().add(text);

        StackPane discriptionPane = new StackPane();
        discriptionPane.setAlignment(Pos.CENTER);
        textBox.getChildren().add(discriptionPane);

        String discription = "If you delete this user, he or she will not access this team";
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
                deleteFunction(string);
                deleteStage.close();
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

    public void deleteFunction(String string) {
        try {
            String query = "DELETE FROM task_files WHERE task_id = '" + string + "'";
            output.executeUpdate(query);
            query = "DELETE FROM task_messages WHERE task_id = '" + string + "'";
            output.executeUpdate(query);
            query = "DELETE FROM task_user_files WHERE task_id = '" + string + "'";
            output.executeUpdate(query);
            query = "DELETE FROM tasks WHERE id = '" + string + "'";
            output.executeUpdate(query);
            query = "DELETE FROM team_tasks WHERE task_id = '" + string + "'";
            output.executeUpdate(query);
            query = "DELETE FROM to_do_task WHERE task_id = '" + string + "'";
            output.executeUpdate(query);
            query = "DELETE FROM update_task WHERE task_id = '" + string + "'";
            output.executeUpdate(query);
        } catch (Exception e) {
            System.out.print(e);
        }
        taskBox.getChildren().remove(tmpStackPane);
    }

    public String getString(String indexString, String tableString, String todoId) {
        String getString = "";
        Statement statement;
        ResultSet resultSet;
        try {
            statement = conn.createStatement();
            String query = "SELECT " + indexString + " FROM " + tableString + " WHERE id = '" + todoId + "'";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                getString = resultSet.getString(indexString);
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        return getString;
    }
}
