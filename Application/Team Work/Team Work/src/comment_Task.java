import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.nio.ByteBuffer;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class comment_Task {

    public String id, team_Id, id_owner;

    VBox messageBox;
    ScrollPane scrollPane;

    public Connection conn;
    public Statement output;
    public ResultSet result;

    Timeline timeline;

    public int messageReference = 0;
    Map<String, StackPane> referenceMap = new TreeMap<String, StackPane>();
    List<StackPane> panes = new ArrayList<StackPane>();
    List<String> idStrings = new ArrayList<String>();
    List<String> updateList = new ArrayList<String>();

    public comment_Task(Tab tab, String TaskId, String team_ID, String idString) {
        id = TaskId;
        team_Id = team_ID;
        id_owner = idString;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/team work hub", "root", "mydatabase");
            output = conn.createStatement();
        } catch (Exception e) {
            System.out.print(e);
        }
        commentFX(tab);
    }

    public void commentFX(Tab tab) {
        StackPane stackPane = new StackPane();
        tab.setContent(stackPane);

        BorderPane borderPane = new BorderPane();
        stackPane.getChildren().add(borderPane);

        StackPane contentPane = new StackPane();
        borderPane.setCenter(contentPane);

        scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.getStyleClass().add("task-scroll");
        scrollPane.setFitToWidth(true);
        scrollPane.setVvalue(1.0);
        contentPane.getChildren().add(scrollPane);

        StackPane comentPane = new StackPane();
        comentPane.setPadding(new Insets(5, 10, 5, 10));
        scrollPane.setContent(comentPane);

        messageBox = new VBox();
        comentPane.getChildren().add(messageBox);
        loadMessages();

        // Bottom

        StackPane bottomPane = new StackPane();
        bottomPane.setPadding(new Insets(5, 0, 0, 0));
        bottomPane.setStyle("-fx-background-color: transparent;");
        borderPane.setBottom(bottomPane);

        VBox bottomBox = new VBox();
        bottomBox.setPadding(new Insets(10));
        bottomBox.setSpacing(5);
        bottomBox.setEffect(new DropShadow(5, Color.GRAY));
        bottomBox.setStyle("-fx-background-color: white;");
        bottomPane.getChildren().add(bottomBox);

        TextArea textArea = new TextArea();
        textArea.setPromptText("Type");
        textArea.setWrapText(true);
        textArea.setStyle(
                "-fx-border-width: 2; -fx-border-radius: 5; -fx-border-color:rgb(200,200,200); -fx-focus-color: transparent; -fx-text-box-border:transparent; -fx-faint-focus-color: white;");
        textArea.setPrefHeight(145);
        bottomBox.getChildren().add(textArea);

        FlowPane flowPane = new FlowPane();
        flowPane.setPadding(new Insets(0, 1, 0, 1));
        bottomBox.getChildren().add(flowPane);

        Button button = new Button("Send");
        button.setPrefHeight(27);
        button.setPrefWidth(150);
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-border-radius: 4; -fx-background-color: Dodgerblue;");
        button.setCursor(Cursor.HAND);
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String getText = textArea.getText();
                if (!getText.equals("")) {
                    textArea.setText("");
                    insertMessage(getText);
                    scrollPane.setVvalue(1.0);
                }
            }

        });
        flowPane.getChildren().add(button);
    }

    public void insertMessage(String message) {
        String generateId = getIdString("task_messages");
        timeline.stop();
        try {
            String query = "INSERT INTO `task_messages` (`id`,`task_id`,`team_id`,`user_id`,`message`,`update`) VALUE ('"
                    + generateId + "','" + id + "','" + team_Id + "','" + App.id + "','" + message + "','1')";
            output.executeUpdate(query);
        } catch (Exception e) {
            System.out.print(e);
        }
        updateMessages();
        timeline.play();
        scrollPane.setVvalue(1.0);
    }

    public void loadMessages() {
        try {
            String query = "SELECT id FROM task_messages WHERE task_id = '" + id + "' AND team_id = '" + team_Id
                    + "' ORDER BY time";
            result = output.executeQuery(query);
            while (result.next()) {
                idStrings.add(result.getString("id"));
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        if (idStrings.isEmpty() != true) {
            for (String message_id : idStrings) {
                addMessage(message_id);
            }
        }
        timeline = new Timeline(new KeyFrame(Duration.millis(5000), load -> updateMessages()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    public void updateMessages() {
        int updateNum = 0;
        Statement statement;
        ResultSet resultSet;
        try {
            String query = "SELECT id FROM task_messages WHERE task_id = '" + id + "'  ORDER BY time";
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                updateList.add(resultSet.getString("id"));
                updateNum++;
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        if (messageReference != updateNum) {
            for (String idString : updateList) {
                boolean isExist = false;
                for (String existId : idStrings) {
                    if (existId.equals(idString)) {
                        isExist = true;
                    }
                }
                if (isExist == false) {
                    addMessage(idString);
                    idStrings.add(idString);
                }
            }
        }
        updateList.clear();
    }

    public void addMessage(String message_id) {
        String user_id = "", message = "", time = "";
        try {
            String query = "SELECT user_id,message,time FROM task_messages WHERE id = '" + message_id + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                user_id = result.getString("user_id");
                message = result.getString("message");
                time = getTimeString(result.getString("time"));
            }
        } catch (Exception e) {
            System.out.print(e);
        }

        String username = getTaskString("username", "users", user_id);
        if (user_id.equals(App.id)) {
            username = "Username ( You )";
        }

        StackPane messagePane = new StackPane();
        messageBox.getChildren().add(messagePane);

        VBox vBox = new VBox();
        messagePane.getChildren().add(vBox);

        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(Pos.CENTER_LEFT);

        vBox.getChildren().add(hBox);

        ImageView imageView = new ImageView(new Image("files/icons/user_1.png"));
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);

        Text text = new Text(username);
        text.setFont(Font.font("Comic Sans MS", 17));
        text.setFill(Color.rgb(31, 31, 31));

        hBox.getChildren().addAll(imageView, text);

        BorderPane contentPane = new BorderPane();
        vBox.getChildren().add(contentPane);

        StackPane linePane = new StackPane();
        linePane.setAlignment(Pos.CENTER);
        linePane.setMaxWidth(30);
        linePane.setMinWidth(30);

        contentPane.setLeft(linePane);

        StackPane line = new StackPane();
        line.setStyle("-border-width: 0 1 0 1; -fx-border-color: rgb(200,200,200);");
        line.setMaxWidth(2);
        line.setMinWidth(2);

        linePane.getChildren().add(line);

        StackPane stackPane = new StackPane();
        contentPane.setCenter(stackPane);

        VBox messageVBox = new VBox();
        messageVBox.setPadding(new Insets(5, 0, 0, 5));
        messageVBox.setSpacing(5);
        stackPane.getChildren().add(messageVBox);

        StackPane messageStackPane = new StackPane();
        messageStackPane.setPadding(new Insets(10));
        messageStackPane.setAlignment(Pos.CENTER_LEFT);
        messageStackPane.setEffect(new DropShadow(3, Color.rgb(170, 170, 170)));
        messageStackPane.setStyle("-fx-background-color: white; -fx-border-radius: 4; -fx-background-radius: 4;");
        messageVBox.getChildren().add(messageStackPane);

        Text messageText = new Text(message);
        messageText.setWrappingWidth(400);
        messageText.setFont(Font.font("Times New Roman", 16));
        messageStackPane.getChildren().add(messageText);

        HBox statusBox = new HBox();
        statusBox.setSpacing(7);
        statusBox.setAlignment(Pos.CENTER_RIGHT);
        messageVBox.getChildren().add(statusBox);

        Text timeText = new Text(time);
        timeText.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 15));
        timeText.setFill(Color.rgb(170, 170, 170));

        ImageView iconView = new ImageView(new Image("files/icons/delete_1.png"));
        iconView.setFitHeight(25);
        iconView.setFitWidth(25);

        statusBox.getChildren().addAll(timeText);

        if (id_owner.equals(user_id)) {

            Button button = new Button("Delete");
            button.setPadding(new Insets(2));
            button.setPrefWidth(100);
            button.setPrefHeight(23);
            button.setTextFill(Color.WHITE);
            button.setStyle("-fx-background-color: #3097fd;");
            button.setCursor(Cursor.HAND);
            button.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent arg0) {
                    try {
                        String query = "DELETE FROM task_messages WHERE id = '" + message_id + "'";
                        output.executeUpdate(query);
                    } catch (Exception e) {
                        System.out.print(e);
                    }
                    panes.remove(messagePane);
                    messageBox.getChildren().remove(messagePane);
                    messageReference--;
                }

            });
            statusBox.getChildren().add(button);
        }
        panes.add(messagePane);
        messageReference++;
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

    public String getTimeString(String oldTime) {
        Timestamp timestamp = Timestamp.valueOf(oldTime);
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm a,  dd MMMM");
        String dateString = format.format(date);
        return dateString;
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
