import java.sql.*;
import java.text.SimpleDateFormat;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class teamSettings {

    public String name, userId, type, purpose, comment, time;
    public String id, user_Id;

    Label nameLabel, purposeLabel, typeLabel, commentLabel;

    Stage stage;

    public Connection conn;
    public Statement output;
    public ResultSet result;

    public Double xSet = 0.0, ySet = 0.0;

    public teamSettings(String teamId, Tab tab) {
        id = teamId;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/team work hub", "root", "mydatabase");
            output = conn.createStatement();
            String query = "SELECT * FROM teams WHERE id = '" + teamId + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                name = result.getString("name");
                userId = result.getString("team_owner");
                type = result.getString("type");
                purpose = result.getString("purpose");
                comment = result.getString("comment");
                time = result.getString("time");
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        teamGUI(tab);
    }

    public void teamGUI(Tab tab) {
        StackPane stackPane = new StackPane();
        tab.setContent(stackPane);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:transparent");
        scrollPane.getStyleClass().add("task-scroll");
        stackPane.getChildren().add(scrollPane);

        StackPane afterPane = new StackPane();
        scrollPane.setContent(afterPane);

        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(0, 20, 0, 20));
        afterPane.getChildren().add(vBox);

        titleContent(vBox, "Profile Settings");

        StackPane imagePane = new StackPane();
        vBox.getChildren().add(imagePane);

        ImageView imageView = new ImageView(new Image("files/icons/group.png"));
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        imagePane.getChildren().add(imageView);

        // DETAILS

        StackPane contentPane = new StackPane();
        vBox.getChildren().add(contentPane);

        VBox contentBox = new VBox();
        contentBox.setSpacing(10);
        contentPane.getChildren().add(contentBox);

        Button nameButton = new Button("Update");
        nameButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                updateChange("name");
            }

        });
        nameLabel = new Label(name);
        informationGUI("Team Name", nameLabel, nameButton, contentBox);

        Button purposeButton = new Button("Update");
        purposeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                updateChange("purpose");
            }

        });
        purposeLabel = new Label(purpose);
        informationGUI("Team Purpose", purposeLabel, purposeButton, contentBox);

        Button typeButton = new Button("Update");
        typeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                updateChange("type");
            }

        });
        typeLabel = new Label(type);
        informationGUI("Team Type", typeLabel, typeButton, contentBox);

        Button commentButton = new Button("Update");
        commentButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                updateChange("comment");
            }

        });
        commentLabel = new Label(comment);
        informationGUI("Team Comment", commentLabel, commentButton, contentBox);

        // DETAILS

        titleContent(vBox, "Team Details");

        StackPane pane = new StackPane();
        vBox.getChildren().add(pane);

        VBox paneBox = new VBox();
        paneBox.setSpacing(5);
        paneBox.setPadding(new Insets(20, 0, 10, 0));
        pane.getChildren().add(paneBox);

        int totalTask = 0, totalUser = 0;
        String username = "";
        try {
            String query = "SELECT username FROM users WHERE id = '" + userId + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                username = result.getString("username");
            }
            query = "SELECT id FROM tasks WHERE team_id = '" + id + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                totalTask++;
            }
            query = "SELECT id FROM team_tasks WHERE team_id = '" + id + "' AND assigned_id = '" + App.id + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                totalTask++;
            }
            query = "SELECT id FROM team_people WHERE team_id = '" + id + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                totalUser++;
            }
        } catch (Exception e) {
            System.out.print(e);
        }

        detailsGUI("Created By", username, paneBox);
        detailsGUI("Total Task", "" + totalTask, paneBox);
        detailsGUI("Total Users", "" + totalUser, paneBox);

        time = getTimeString(time);
        detailsGUI("Time", time, paneBox);

        titleContent(vBox, "About Team");

        // Deleting

        VBox deleteBox = new VBox();
        deleteBox.setPadding(new Insets(0, 0, 100, 0));
        deleteBox.setSpacing(10);
        vBox.getChildren().add(deleteBox);

        StackPane deleteTextPane = new StackPane();
        deleteBox.getChildren().add(deleteTextPane);

        Text text = new Text("Delete or remove This team From your Account");
        text.setFont(Font.font("Arial Rounded MT Bold", 19));
        text.setFill(Color.rgb(70, 70, 70));
        deleteTextPane.getChildren().add(text);

        StackPane deletePane = new StackPane();
        deleteBox.getChildren().add(deletePane);

        Button deleteButton = new Button();
        deleteButton.setPrefSize(100, 24);
        deleteButton.setPadding(new Insets(1));
        deleteButton.setStyle("-fx-background-color: dodgerblue");
        deleteButton.setTextFill(Color.WHITE);
        deleteButton.setCursor(Cursor.HAND);
        deletePane.getChildren().add(deleteButton);

        if (userId.equals(App.id)) {
            deleteButton.setText("Delete");
            deleteButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent actionEvent) {
                    deleteDialog();
                }

            });
        } else {
            deleteButton.setText("Remove");
        }
    }

    public void titleContent(VBox vBox, String string) {
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER_LEFT);
        stackPane.setPadding(new Insets(20, 0, 20, 0));
        stackPane.setStyle("-fx-border-width: 0 0 2 0; -fx-border-color: rgb(200,200,200);");
        vBox.getChildren().add(stackPane);

        Text text = new Text(string);
        text.setFont(Font.font("Arial Rounded MT Bold", 19));
        text.setFill(Color.rgb(31, 31, 31));
        stackPane.getChildren().add(text);
    }

    public void detailsGUI(String string, String content, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().add(hBox);

        Label nameLabel = new Label(string);
        nameLabel.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 16));
        nameLabel.setTextFill(Color.rgb(170, 170, 170));
        nameLabel.setPrefWidth(130);

        Text dotText = new Text(":");
        dotText.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 16));
        dotText.setFill(Color.rgb(170, 170, 170));

        Label contentLabel = new Label(content);
        contentLabel.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 16));
        contentLabel.setTextFill(Color.rgb(170, 170, 170));
        contentLabel.setPrefWidth(400);

        hBox.getChildren().addAll(nameLabel, dotText, contentLabel);
    }

    public void informationGUI(String name, Label label, Button button, VBox box) {
        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.TOP_CENTER);
        box.getChildren().add(hBox);

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 17));
        nameLabel.setTextFill(Color.GRAY);
        nameLabel.setMinWidth(150);

        Text text = new Text(":");
        text.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 17));
        text.setFill(Color.GRAY);

        label.setWrapText(true);
        label.setPrefWidth(300);
        label.setTextFill(Color.rgb(170, 170, 170));
        label.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 17));

        button.setPrefSize(100, 23);
        button.setPadding(new Insets(1));
        button.setStyle("-fx-background-color: dodgerblue");
        button.setTextFill(Color.WHITE);
        button.setCursor(Cursor.HAND);

        hBox.getChildren().addAll(nameLabel, text, label, button);
    }

    public String getTimeString(String oldTime) {
        String dateString = "";
        if (!oldTime.equals("") || oldTime != null) {
            Timestamp timestamp = Timestamp.valueOf(oldTime);
            Date date = new Date(timestamp.getTime());
            SimpleDateFormat format = new SimpleDateFormat("HH:mm a,  dd MMMM");
            dateString = format.format(date);
        }
        return dateString;
    }

    public void updateChange(String string) {

        StackPane stackpane = new StackPane();
        stackpane.setPadding(new Insets(5));
        stackpane.setStyle("-fx-background-color: transparent;");

        StackPane pane = new StackPane();
        pane.setPadding(new Insets(10));
        pane.setEffect(new DropShadow(5, Color.GRAY));
        pane.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        stackpane.getChildren().add(pane);

        VBox paneBox = new VBox();
        paneBox.setSpacing(7);
        pane.getChildren().add(paneBox);

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
                stage.setX(mouseEvent.getScreenX() - xSet);
                stage.setY(mouseEvent.getScreenY() - ySet);
            }

        });
        paneBox.getChildren().add(cancelPane);

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
                stage.close();
            }

        });
        cancelPane.getChildren().add(cancelButton);

        StackPane titlePane = new StackPane();
        titlePane.setAlignment(Pos.CENTER_LEFT);
        paneBox.getChildren().add(titlePane);

        Text text = new Text("Change Team " + string);
        text.setFont(Font.font("Arial Rounded MT Bold", 16));
        text.setFill(Color.rgb(31, 31, 31));
        titlePane.getChildren().add(text);

        // CONTENT

        StackPane contentPane = new StackPane();
        paneBox.getChildren().add(contentPane);

        TextField textField = new TextField();
        textField.setPrefHeight(30);
        textField.setPromptText("Enter new " + string);
        contentPane.getChildren().add(textField);

        StackPane buttonPane = new StackPane();
        buttonPane.setAlignment(Pos.CENTER_RIGHT);
        paneBox.getChildren().add(buttonPane);

        Button button = new Button("Change");
        button.setPrefSize(100, 25);
        button.setStyle("-fx-background-color: dodgerblue;");
        button.setTextFill(Color.WHITE);
        button.setCursor(Cursor.HAND);
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String target = textField.getText();
                if (!target.equals("") && !string.equals("")) {
                    updating(string, target);
                    stage.close();
                    if (string.equals("name")) {
                        nameLabel.setText(target);
                    }
                    if (string.equals("purpose")) {
                        purposeLabel.setText(target);
                    }
                    if (string.equals("type")) {
                        typeLabel.setText(target);
                    }
                    if (string.equals("comment")) {
                        commentLabel.setText(target);
                    }
                }
            }

        });
        buttonPane.getChildren().add(button);

        Scene scene = new Scene(stackpane, 500, 160);
        scene.setFill(Color.TRANSPARENT);

        stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Team Work Hub - Add Task");
        stage.getIcons().add(new Image("files/icons/group.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public void updating(String name, String value) {
        try {
            String query = "UPDATE teams SET " + name + " = '" + value + "' , `update` = '1'  WHERE id = '" + id + "' ";
            output.executeUpdate(query);
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void deleteFunction() {
        String taskQuery = "";
        try {
            int a = 0;
            String query = "SELECT id FROM tasks WHERE team_id = '" + id + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                if (a != 0) {
                    taskQuery = taskQuery + "OR task_id = '" + result.getString("id") + "'";
                } else {
                    taskQuery = "task_id = '" + result.getString("id") + "'";
                }
            }

            if (!taskQuery.equals("")) {
                query = "DELETE FROM task_files WHERE " + taskQuery;
                output.executeUpdate(query);

                query = "DELETE FROM task_user_files WHERE " + taskQuery;
                output.executeUpdate(query);
            }

            query = "DELETE FROM notification WHERE team_id = '" + id + "'";
            output.executeUpdate(query);

            query = "DELETE FROM task_messages WHERE team_id = '" + id + "'";
            output.executeUpdate(query);

            query = "DELETE FROM tasks WHERE team_id = '" + id + "'";
            output.executeUpdate(query);

            query = "DELETE FROM team_documents WHERE team_id = '" + id + "'";
            output.executeUpdate(query);

            query = "DELETE FROM team_people WHERE team_id = '" + id + "'";
            output.executeUpdate(query);

            query = "DELETE FROM to_do_task WHERE team_id = '" + id + "'";
            output.executeUpdate(query);

            query = "DELETE FROM team_tasks WHERE team_id = '" + id + "'";
            output.executeUpdate(query);

            query = "DELETE FROM teams WHERE id = '" + id + "'";
            output.executeUpdate(query);

        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void deleteDialog() {
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

        Text text = new Text("Delete This Task ??");
        text.setFont(Font.font("Arial Rounded MT Bold", 30));
        text.setFill(Color.rgb(51, 51, 51));
        titlePane.getChildren().add(text);

        StackPane discriptionPane = new StackPane();
        discriptionPane.setAlignment(Pos.CENTER);
        textBox.getChildren().add(discriptionPane);

        String discription = "If you delete this team all tasks, files, messages and relavent data associated with this team will be deleted";
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
                App.deleteProperty.set(!App.deleteProperty.get());
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
}
