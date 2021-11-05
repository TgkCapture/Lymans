import java.sql.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class teamPeople {

    String id;
    Boolean selectedBoolean = false;
    RadioButton selectedButton;

    Set<String> idSet = new TreeSet<String>();
    Map<String, Tab> tabMap = new TreeMap<String, Tab>();

    Stage stage;
    ToggleGroup toggleGroup;
    TabPane tabPane;
    VBox vBox;

    public Connection conn;
    public Statement output;
    public ResultSet result;

    Double xSet = 0.0, ySet = 0.0;

    public teamPeople(String teamId, Tab tab) {
        id = teamId;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/team work hub", "root", "mydatabase");
            output = conn.createStatement();
        } catch (Exception e) {
            System.out.print(e);
        }
        teamGUI(tab);
    }

    public void teamGUI(Tab tab) {
        StackPane stackPane = new StackPane();
        tab.setContent(stackPane);

        BorderPane borderPane = new BorderPane();
        stackPane.getChildren().add(borderPane);

        StackPane leftPane = new StackPane();
        leftPane.setPrefWidth(350);
        leftPane.setMinWidth(300);
        leftPane.setStyle("-fx-border-width: 0 2 0 0; -fx-border-color: rgb(230,230,230);");
        borderPane.setLeft(leftPane);

        BorderPane pane = new BorderPane();
        leftPane.getChildren().add(pane);

        VBox topBox = new VBox();
        topBox.setSpacing(7);
        pane.setTop(topBox);

        StackPane titlePane = new StackPane();
        titlePane.setPadding(new Insets(7, 10, 0, 10));
        titlePane.setAlignment(Pos.CENTER_LEFT);
        topBox.getChildren().add(titlePane);

        Text text = new Text("Add People to team");
        text.setFont(Font.font("Comic Sans MS", 17));
        text.setFill(Color.rgb(31, 31, 31));
        titlePane.getChildren().add(text);

        StackPane buttonPane = new StackPane();
        // buttonPane.setAlignment(Pos.CENTER_LEFT);
        buttonPane.setPadding(new Insets(0, 10, 10, 10));
        buttonPane.setStyle("-fx-border-width: 0 0 2 0; -fx-border-color: rgb(230,230,230);");
        topBox.getChildren().add(buttonPane);

        Button addButton = new Button("Add People");
        addButton.setPrefSize(100, 24);
        addButton.setStyle("-fx-background-color: dodgerblue;");
        addButton.setCursor(Cursor.HAND);
        addButton.setTextFill(Color.WHITE);
        addButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                addUser();
            }

        });
        buttonPane.getChildren().add(addButton);

        StackPane recentPane = new StackPane();
        topBox.getChildren().add(recentPane);

        Text recentText = new Text("Recent");
        recentText.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 16));
        recentText.setFill(Color.GRAY);
        recentPane.getChildren().add(recentText);

        StackPane contentPane = new StackPane();
        pane.setCenter(contentPane);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.getStyleClass().add("task-scroll");
        contentPane.getChildren().add(scrollPane);

        StackPane allPane = new StackPane();
        scrollPane.setContent(allPane);

        vBox = new VBox();
        vBox.setPadding(new Insets(5));
        allPane.getChildren().add(vBox);

        toggleGroup = new ToggleGroup();

        tabPane = new TabPane();

        try {
            String query = "SELECT user_id FROM team_people WHERE team_id = '" + id + "' ORDER BY time";
            result = output.executeQuery(query);
            while (result.next()) {
                idSet.add(result.getString("user_id"));
            }
            query = "SELECT notifyId FROM notification WHERE team_id = '" + id
                    + "' AND choice = 'invitation' ORDER BY time";
            result = output.executeQuery(query);
            while (result.next()) {
                idSet.add(result.getString("notifyId"));
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        for (String string : idSet) {
            allPeople(string);
        }

        StackPane centerPane = new StackPane();
        borderPane.setCenter(centerPane);

        centerPane.getChildren().add(tabPane);

    }

    public void detailsPane(Tab tab, String string) {
        String usernameString = getString("username", "users", string);

        StackPane pane = new StackPane();
        pane.setPadding(new Insets(10));
        tab.setContent(pane);

        VBox paneBox = new VBox();
        paneBox.setSpacing(7);
        pane.getChildren().add(paneBox);

        StackPane titlePane = new StackPane();
        titlePane.setAlignment(Pos.CENTER_LEFT);
        paneBox.getChildren().add(titlePane);

        Text text = new Text("User Information");
        text.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 17));
        text.setFill(Color.rgb(31, 31, 31));
        titlePane.getChildren().add(text);

        StackPane logoPane = new StackPane();
        paneBox.getChildren().add(logoPane);

        ImageView imageView = new ImageView(new Image("files/icons/user_1.png"));
        imageView.setFitHeight(70);
        imageView.setFitWidth(70);
        logoPane.getChildren().add(imageView);

        VBox detailsBox = new VBox();
        detailsBox.setSpacing(6);
        detailsBox.setPadding(new Insets(0, 0, 10, 0));
        detailsBox.setStyle("-fx-border-width: 0 0 2 0; -fx-border-color: rgb(170,170,170);");
        paneBox.getChildren().add(detailsBox);

        Boolean checkBoolean = false;
        try {
            String query = "SELECT user_id FROM team_people WHERE user_id = '" + string + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                checkBoolean = true;
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        String status = "Added";
        if (checkBoolean != true) {
            status = "Requested (Not Confirmed)";
        }

        Text username = new Text(usernameString);
        username.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 18));
        username.setFill(Color.rgb(31, 31, 31));

        Text statusText = new Text("Condition : " + status);
        statusText.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 16));
        statusText.setFill(Color.GRAY);

        Button button = new Button();
        button.setStyle("-fx-background-color: dodgerblue;");
        button.setCursor(Cursor.HAND);
        button.setTextFill(Color.WHITE);

        if (checkBoolean != false) {
            button.setPrefSize(100, 24);
            button.setText("DELETE");
            button.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent actionEvent) {
                    deleteDialog(string);
                }

            });
        } else {
            button.setPrefSize(130, 24);
            button.setText("Delete Requested");
        }

        detailsBox.getChildren().addAll(username, statusText, button);
    }

    public void allPeople(String string) {
        String username = getString("username", "users", string);

        Tab tab = new Tab();
        detailsPane(tab, string);
        tabPane.getTabs().add(tab);
        tabMap.put(string, tab);

        RadioButton radioButton = new RadioButton(username);
        if (selectedBoolean != true) {
            radioButton.setSelected(true);
            selectedButton = radioButton;
            selectedBoolean = true;
        }
        radioButton.setPadding(new Insets(2, 10, 2, 5));
        radioButton.setTextFill(Color.rgb(51, 51, 51));
        radioButton.setPrefWidth(2000);
        radioButton.setCursor(Cursor.HAND);
        radioButton.setToggleGroup(toggleGroup);
        radioButton.getStyleClass().remove("radio-button");
        radioButton.getStyleClass().add("user-button");
        radioButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                tabPane.getSelectionModel().select(tab);
                selectedButton = radioButton;
                selectedBoolean = true;
            }

        });
        vBox.getChildren().add(radioButton);
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

    public void addUser() {

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

        Text text = new Text("Add User");
        text.setFont(Font.font("Arial Rounded MT Bold", 16));
        text.setFill(Color.rgb(31, 31, 31));
        titlePane.getChildren().add(text);

        // CONTENT

        StackPane contentPane = new StackPane();
        paneBox.getChildren().add(contentPane);

        TextField textField = new TextField();
        textField.setPrefHeight(30);
        textField.setPromptText("Enter Username");
        contentPane.getChildren().add(textField);

        StackPane buttonPane = new StackPane();
        buttonPane.setAlignment(Pos.CENTER_RIGHT);
        paneBox.getChildren().add(buttonPane);

        Button button = new Button("ADD");
        button.setPrefSize(100, 25);
        button.setStyle("-fx-background-color: dodgerblue;");
        button.setTextFill(Color.WHITE);
        button.setCursor(Cursor.HAND);
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String username = textField.getText();
                if (!username.equals("")) {
                    textField.setEditable(false);
                    Boolean checkBoolean = false, connBoolean = true, checkExist = false;
                    String userId = "", userName = "", email = "", logo = "";

                    Connection connection;
                    Statement statement;
                    ResultSet resultSet;
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        connection = DriverManager.getConnection("jdbc:mysql://freedb.tech:3306/freedbtech_Teamworkhub",
                                "freedbtech_Asapgang", "mydatabase1234");
                        statement = connection.createStatement();
                        String query = "SELECT * FROM users WHERE username = '" + username + "'";
                        resultSet = statement.executeQuery(query);
                        while (resultSet.next()) {
                            userId = resultSet.getString("id");
                            userName = resultSet.getString("username");
                            email = resultSet.getString("email");
                            logo = resultSet.getString("logo");
                            checkBoolean = true;
                        }
                        query = "SELECT id FROM team_people WHERE team_id = '" + id + "' AND user_id = '" + userId
                                + "'";
                        resultSet = statement.executeQuery(query);
                        while (resultSet.next()) {
                            checkExist = true;
                        }
                    } catch (Exception e) {
                        System.out.print(e);
                        connBoolean = false;
                        popupMessage("sad.png", "You are offline, Connect to the internet");
                        textField.setEditable(true);
                    }
                    if (checkBoolean == true && connBoolean == true) {
                        Boolean verify = false;
                        if (checkExist != true) {
                            try {
                                String query = "INSERT INTO `users` (`id`,`username`,`email`,`logo`) VALUES ('" + userId
                                        + "','" + userName + "','" + email + "','" + logo + "')";
                                output.executeUpdate(query);
                                verify = true;
                            } catch (Exception e) {
                                System.out.print(e);
                            }
                            idSet.add(userId);
                            if (verify == true) {
                                allPeople(userId);
                                stage.close();
                            }
                        } else {
                            popupMessage("sad.png", "Username already added to this team");
                        }
                    } else {
                        popupMessage("sad.png", "The username you entered does not exist");
                        textField.setEditable(true);
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

    public void popupMessage(String image, String message) {
        Stage messageStage = new Stage();

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

        Label text = new Label(message);
        text.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 22));
        text.setTextFill(Color.rgb(71, 71, 71));
        text.setWrapText(true);
        text.setMaxWidth(400);
        text.setTextAlignment(TextAlignment.CENTER);

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
                messageStage.close();
            }

        });
        buttonPane.getChildren().add(button);

        Scene scene = new Scene(stackPane, 450, 220);
        scene.setFill(Color.TRANSPARENT);

        messageStage.setScene(scene);
        messageStage.initStyle(StageStyle.TRANSPARENT);
        messageStage.setTitle("Task updated");
        messageStage.getIcons().add(new Image("files/icons/group.png"));
        messageStage.initModality(Modality.APPLICATION_MODAL);
        messageStage.showAndWait();
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
            String query = "DELETE FROM `users` WHERE id = '" + string + "'";
            output.executeUpdate(query);
            query = "DELETE FROM `team_people`WHERE id = '" + string + "'";
            output.executeUpdate(query);
        } catch (Exception e) {
            System.out.print(e);
        }
        tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedItem());
        vBox.getChildren().remove(selectedButton);
    }

}
