import java.nio.ByteBuffer;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class notification {

    VBox vBox;

    Timeline timeline;

    public Connection conn;
    public Statement output;
    public ResultSet result;

    Set<String> teamSet = new TreeSet<String>();
    Set<String> idsSet = new TreeSet<String>();
    String mainQuery = "";

    public notification(Tab tab) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/team work hub", "root", "mydatabase");
            output = conn.createStatement();
        } catch (Exception e) {
            System.out.print(e);
        }
        mainFunction(tab);
        getNotification();
    }

    public void mainFunction(Tab tab) {
        StackPane stackPane = new StackPane();
        tab.setContent(stackPane);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:transparent");
        scrollPane.getStyleClass().add("task-scroll");
        stackPane.getChildren().add(scrollPane);

        StackPane pane = new StackPane();
        scrollPane.setContent(pane);

        vBox = new VBox();
        vBox.setSpacing(35);
        vBox.setPadding(new Insets(10, 15, 10, 15));
        pane.getChildren().add(vBox);
        calculations();

    }

    public void getNotification() {
        timeline = new Timeline(new KeyFrame(Duration.millis(5000), load -> updateNotification()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void updateNotification() {
        Statement statement;
        ResultSet resultSet;
        try {
            statement = conn.createStatement();
            String query = "SELECT id FROM notification WHERE notifyId = '" + App.id + "' ORDER BY time";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Boolean isExist = false;
                for (String each : idsSet) {
                    if (each.equals(resultSet.getString("id"))) {
                        isExist = true;
                    }
                }
                if (isExist == false) {
                    notificationFunction(resultSet.getString("id"));
                    idsSet.add(resultSet.getString("id"));
                }
            }
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void calculations() {
        try {
            String query = "SELECT id FROM notification WHERE notifyId = '" + App.id + "' ORDER BY time";
            result = output.executeQuery(query);
            while (result.next()) {
                idsSet.add(result.getString("id"));
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        for (String notificationId : idsSet) {
            notificationFunction(notificationId);
        }
    }

    public void notificationFunction(String idString) {

        String title = "", content = "";

        String choice = getString("choice", "notification", idString);
        String team_id = getString("team_id", "notification", idString);
        String task_id = getString("task_id", "notification", idString);
        String user_id = getString("user_id", "notification", idString);

        String teamName = getString("name", "teams", team_id);
        String taskTitle = getString("name", "tasks", task_id);
        String timeString = getString("time", "notification", idString);

        if (choice.equals("invitation")) {
            title = "New Team Invitation ( Request )";
            content = "You have been added to new team named " + teamName
                    + " , Click Proceed Button to be in this team or Ignore Button to ignore this team.";
        } else {
            if (choice.equals("task")) {
                title = teamName;
                content = "New task have been added to " + teamName + " . Task title is " + taskTitle
                        + " . Click view task to view this task. ";
            } else {
                title = getString("title", "notification", idString);
                content = getString("content", "notification", idString);
            }
        }

        StackPane stackPane = new StackPane();
        stackPane.setPadding(new Insets(0, 0, 7, 0));
        stackPane.setStyle("-fx-border-width: 0 0 2 0; -fx-border-color: rgb(190,190,190);");
        vBox.getChildren().add(0, stackPane);

        VBox paneBox = new VBox();
        paneBox.setSpacing(5);
        stackPane.getChildren().add(paneBox);

        StackPane titlePane = new StackPane();
        titlePane.setAlignment(Pos.CENTER_LEFT);
        paneBox.getChildren().add(titlePane);

        Text text = new Text(title);
        text.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 19));
        text.setFill(Color.rgb(31, 31, 31));
        titlePane.getChildren().add(text);

        StackPane contentPane = new StackPane();
        contentPane.setAlignment(Pos.CENTER_LEFT);
        paneBox.getChildren().add(contentPane);

        Label label = new Label(content);
        label.setFont(Font.font("Times New Roman", 16));
        label.setTextFill(Color.rgb(51, 51, 51));
        label.setWrapText(true);
        contentPane.getChildren().add(label);

        if (choice.equals("invitation")) {
            StackPane extraPane = new StackPane();
            extraPane.setAlignment(Pos.CENTER);
            paneBox.getChildren().add(extraPane);

            VBox paneVBox = new VBox();
            paneVBox.setSpacing(4);
            paneVBox.setPadding(new Insets(5, 0, 0, 0));
            extraPane.getChildren().add(paneVBox);

            String purpose = getString("purpose", "teams", team_id);
            String userName = getString("username", "users", user_id);

            holizontalBox(paneVBox, "Team Name", teamName);
            holizontalBox(paneVBox, "Purpose", purpose);
            holizontalBox(paneVBox, "Added By", userName);

            HBox buttonBox = new HBox();
            buttonBox.setSpacing(10);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);
            paneVBox.getChildren().add(buttonBox);

            Button ignoreButton = new Button("Ignore");
            ignoreButton.setPrefSize(105, 24);
            ignoreButton.setTextFill(Color.WHITE);
            ignoreButton.setStyle("-fx-background-color: dodgerblue;");
            ignoreButton.setCursor(Cursor.HAND);
            ignoreButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        String query = "DELETE FROM notification WHERE id = '" + idString + "'";
                        output.executeUpdate(query);
                        vBox.getChildren().remove(stackPane);
                    } catch (Exception e) {
                        System.out.print(e);
                    }
                }

            });

            Button acceptButton = new Button("Proceed");
            acceptButton.setPrefSize(105, 24);
            acceptButton.setTextFill(Color.WHITE);
            acceptButton.setStyle("-fx-background-color: dodgerblue;");
            acceptButton.setCursor(Cursor.HAND);
            acceptButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent actionEvent) {
                    String getId = getIdString("team_people");
                    try {
                        String query = "INSERT INTO `team_people` (`id`,`team_id`,`user_id`,`position`) VALUES ('"
                                + getId + "','" + team_id + "','" + App.id + "','0')";
                        output.executeUpdate(query);
                        query = "DELETE FROM notification WHERE id = '" + idString + "'";
                        output.executeUpdate(query);
                        vBox.getChildren().remove(stackPane);
                        App.teamProperty.set(!App.teamProperty.get());
                    } catch (Exception e) {
                        System.out.print(e);
                    }
                }

            });

            buttonBox.getChildren().addAll(ignoreButton, acceptButton);
        } else {
            if (choice.equals("task")) {
                StackPane linkPane = new StackPane();
                paneBox.getChildren().add(linkPane);

                Text linkText = new Text("View Task");
                linkText.setFont(Font.font("Arial Rounded MT Bold", 16));
                linkText.setFill(Color.DEEPSKYBLUE);
                linkText.setCursor(Cursor.HAND);
                linkText.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        new task(task_id, team_id);
                    }

                });
                StackPane.setAlignment(linkText, Pos.CENTER_LEFT);
                linkPane.getChildren().add(linkText);

                Button button = new Button("remove");
                button.setPrefSize(100, 24);
                button.setStyle("-fx-background-color: dodgerblue");
                button.setTextFill(Color.WHITE);
                button.setCursor(Cursor.HAND);
                button.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        try {
                            String query = "DELETE FROM notification WHERE id = '" + idString + "'";
                            output.executeUpdate(query);
                            vBox.getChildren().remove(stackPane);
                        } catch (Exception e) {
                            System.out.print(e);
                        }
                    }

                });
                StackPane.setAlignment(button, Pos.CENTER_RIGHT);
                linkPane.getChildren().add(button);
            } else {
                StackPane removePane = new StackPane();
                paneBox.getChildren().add(removePane);

                Button button = new Button("remove");
                button.setPrefSize(100, 24);
                button.setStyle("-fx-background-color: dodgerblue");
                button.setTextFill(Color.WHITE);
                button.setCursor(Cursor.HAND);
                button.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        try {
                            String query = "DELETE FROM notification WHERE id = '" + idString + "'";
                            output.executeUpdate(query);
                            vBox.getChildren().remove(stackPane);
                        } catch (Exception e) {
                            System.out.print(e);
                        }
                    }

                });
                removePane.getChildren().add(button);
            }
        }

        timeString = getTimeString(timeString);

        StackPane timePane = new StackPane();
        timePane.setAlignment(Pos.CENTER_LEFT);
        paneBox.getChildren().add(timePane);

        Text timeText = new Text(timeString);
        timeText.setFont(Font.font("Comic Sans MS", 16));
        timeText.setFill(Color.GRAY);
        timePane.getChildren().add(timeText);

    }

    public void holizontalBox(VBox paneVBox, String name, String dash) {
        HBox nameBox = new HBox();
        nameBox.setSpacing(40);
        nameBox.setPadding(new Insets(0, 120, 0, 120));
        paneVBox.getChildren().add(nameBox);

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Times new Roman", 16));
        nameLabel.setTextFill(Color.GRAY);
        nameLabel.setPrefWidth(120);

        Text dotText = new Text(":");
        dotText.setFont(Font.font("Times new Roman", 16));
        dotText.setFill(Color.GRAY);

        Text dashText = new Text(dash);
        dashText.setFont(Font.font("Times new Roman", 16));
        dashText.setFill(Color.GRAY);

        nameBox.getChildren().addAll(nameLabel, dotText, dashText);
    }

    public String getString(String indexString, String table, String idString) {
        String getString = "";
        try {
            String query = "SELECT " + indexString + " FROM " + table + " WHERE id = '" + idString + "'";
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
        String dateString = "";
        if (!oldTime.equals("")) {
            Timestamp timestamp = Timestamp.valueOf(oldTime);
            Date date = new Date(timestamp.getTime());
            SimpleDateFormat format = new SimpleDateFormat("HH:mm a,  dd MMMM");
            dateString = format.format(date);
        }
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
