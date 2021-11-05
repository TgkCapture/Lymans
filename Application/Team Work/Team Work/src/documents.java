import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class documents {

    public String teamId = "";

    VBox vBox;
    Stage uploadStage;

    Timeline timeline;

    public Connection conn;
    public Statement output;
    public ResultSet result;

    Set<String> idSet = new TreeSet<String>();

    Double xSet = 0.0, ySet = 0.0;

    public documents(String string, Tab tab) {
        teamId = string;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/team work hub", "root", "mydatabase");
            output = conn.createStatement();
        } catch (Exception e) {
            System.out.print(e);
        }
        documentFunction(tab);
    }

    public void documentFunction(Tab tab) {
        StackPane stackPane = new StackPane();
        tab.setContent(stackPane);

        BorderPane borderPane = new BorderPane();
        stackPane.getChildren().add(borderPane);

        StackPane contentPane = new StackPane();
        borderPane.setCenter(contentPane);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:transparent");
        scrollPane.getStyleClass().add("task-scroll");
        contentPane.getChildren().add(scrollPane);

        StackPane pane = new StackPane();
        scrollPane.setContent(pane);

        vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(20);
        pane.getChildren().add(vBox);

        try {
            String query = "SELECT id FROM team_documents WHERE team_id = '" + teamId + "' ORDER BY time";
            result = output.executeQuery(query);
            while (result.next()) {
                idSet.add(result.getString("id"));
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        for (String idString : idSet) {
            addFileFunction(idString);
        }

        timeline = new Timeline(new KeyFrame(Duration.millis(5000), load -> updateDocuments()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // BOTTOM

        StackPane bottomPane = new StackPane();
        bottomPane.setAlignment(Pos.CENTER_RIGHT);
        bottomPane.setPadding(new Insets(10));
        bottomPane.setStyle("-fx-border-width: 2 0 0 0; -fx-border-color: rgb(200,200,200)");
        borderPane.setBottom(bottomPane);

        Button button = new Button("Add File");
        button.setPrefSize(120, 24);
        button.setCursor(Cursor.HAND);
        button.setStyle("-fx-background-color: dodgerblue;");
        button.setTextFill(Color.WHITE);
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser chooser = new FileChooser();
                File selectedFile = chooser.showOpenDialog(new Stage());
                if (selectedFile != null) {
                    uploadProgress();
                    Task<Void> task = new Task<Void>() {

                        @Override
                        protected Void call() throws Exception {
                            try {
                                String filePath = "Team Work/src/files/teams/" + teamId;
                                File checkFile = new File(filePath);
                                if (!checkFile.exists()) {
                                    checkFile.mkdir();
                                }
                                filePath = filePath + selectedFile.getName();
                                File destination = new File(filePath);
                                Path path = Files.copy(selectedFile.toPath(), destination.toPath(),
                                        StandardCopyOption.REPLACE_EXISTING);
                                String idString = getIdString("team_documents");
                                String query = "INSERT INTO `team_documents` (`id`,`team_id`,`user_id`,`name`) VALUES ('"
                                        + idString + "','" + teamId + "','" + App.id + "','" + selectedFile.getName()
                                        + "')";
                                output.executeUpdate(query);
                            } catch (Exception e) {
                                System.out.print(e);
                            }
                            return null;
                        }

                    };
                    task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

                        @Override
                        public void handle(WorkerStateEvent event) {
                            updateDocuments();
                            uploadStage.close();
                        }

                    });
                    new Thread(task).start();
                }
            }

        });
        bottomPane.getChildren().add(button);
    }

    public void updateDocuments() {
        Statement statement;
        ResultSet resultSet;
        Set<String> idStrings = new TreeSet<String>();
        try {
            statement = conn.createStatement();
            String query = "SELECT id FROM team_documents WHERE team_id = '" + teamId + "' ORDER BY time";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                idStrings.add(resultSet.getString("id"));
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        for (String string : idStrings) {
            Boolean check = false;
            for (String eString : idSet) {
                if (string.equals(eString)) {
                    check = true;
                }
            }
            if (check != true) {
                addFileFunction(string);
                idSet.add(string);
            }
        }
    }

    public void addFileFunction(String string) {
        String user_id = getString("user_id", "team_documents", string);
        String content = getString("name", "team_documents", string);
        String time = getString("time", "team_documents", string);
        String username = getString("username", "users", user_id);

        StackPane stackPane = new StackPane();
        vBox.getChildren().add(0, stackPane);

        VBox fileVBox = new VBox();
        fileVBox.setSpacing(6);
        stackPane.getChildren().add(fileVBox);

        StackPane usernamePane = new StackPane();
        usernamePane.setAlignment(Pos.CENTER_LEFT);
        fileVBox.getChildren().add(usernamePane);

        Text text = new Text(username);
        text.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 17));
        text.setFill(Color.rgb(31, 31, 31));
        usernamePane.getChildren().add(text);

        StackPane pane = new StackPane();
        pane.setEffect(new DropShadow(4, Color.BLACK));
        pane.setStyle("-fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: white;");
        pane.setPadding(new Insets(7));
        fileVBox.getChildren().add(pane);

        VBox contentBox = new VBox();
        contentBox.setSpacing(5);
        pane.getChildren().add(contentBox);

        StackPane contentPane = new StackPane();
        contentPane.setAlignment(Pos.CENTER_LEFT);
        contentBox.getChildren().add(contentPane);

        Label label = new Label(content);
        label.setFont(Font.font("Times New Roman", 16));
        label.setTextFill(Color.rgb(31, 31, 31));
        label.setWrapText(true);
        contentPane.getChildren().add(label);

        StackPane allPane = new StackPane();
        contentBox.getChildren().add(allPane);

        Text removeText = new Text("Remove");
        removeText.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 16));
        removeText.setFill(Color.DEEPSKYBLUE);
        removeText.setCursor(Cursor.HAND);
        StackPane.setAlignment(removeText, Pos.CENTER_LEFT);
        removeText.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    String query = "DELETE FROM `team_documents` WHERE id = '" + string + "'";
                    output.executeUpdate(query);
                } catch (Exception e) {
                    System.out.print(e);
                }
                vBox.getChildren().remove(stackPane);
            }

        });
        allPane.getChildren().add(removeText);

        Button button = new Button("Download");
        button.setPadding(new Insets(1.5));
        button.setPrefSize(100, 25);
        button.setStyle("-fx-background-color: dodgerblue;");
        button.setTextFill(Color.WHITE);
        button.setCursor(Cursor.HAND);
        StackPane.setAlignment(button, Pos.CENTER_RIGHT);
        allPane.getChildren().add(button);

        time = getTimeString(time);

        Text timeText = new Text(time);
        timeText.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 15));
        timeText.setFill(Color.GREY);
        fileVBox.getChildren().add(timeText);
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

    public String getTimeString(String oldTime) {
        Timestamp timestamp = Timestamp.valueOf(oldTime);
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm a,  dd MMMM");
        String dateString = format.format(date);
        return dateString;
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
