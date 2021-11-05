import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
import javafx.scene.layout.Priority;
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

public class settings {

    Stage updateStage;
    Label usernameLabel, nameLabel, surnameLabel;
    PasswordField passwordField;
    ToggleGroup themeGroup, themesGroup;

    TabPane tabPane;
    Tab profileTab, themeTab, connectTab, aboutTab;

    public Connection conn;
    public Statement output;
    public ResultSet result;

    Double ySet = 0.0, xSet = 0.0;

    Boolean oldPassBoolean = false, newPassBoolean = false, confirmBoolean = false;

    public settings(Tab tab) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/team work hub", "root", "mydatabase");
            output = conn.createStatement();
        } catch (Exception e) {
            System.out.print(e);
        }
        settingsFunction(tab);
    }

    public void settingsFunction(Tab tab) {
        StackPane stackPane = new StackPane();
        tab.setContent(stackPane);

        BorderPane borderPane = new BorderPane();
        stackPane.getChildren().add(borderPane);

        StackPane menuPane = new StackPane();
        menuPane.setMaxWidth(200);
        menuPane.setMinWidth(200);
        menuPane.setStyle(
                "-fx-background-color: snow; -fx-border-width: 0 2 0 0; -fx-border-color: rgb(230, 230, 230);");
        borderPane.setLeft(menuPane);

        VBox menuBox = new VBox();
        menuPane.getChildren().add(menuBox);

        ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton profileButton = new RadioButton("Profile");
        profileButton.setSelected(true);
        profileButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                tabPane.getSelectionModel().select(profileTab);
            }

        });
        menuRadioButton(profileButton, toggleGroup);

        RadioButton themeRadioButton = new RadioButton("Themes");
        themeRadioButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                tabPane.getSelectionModel().select(themeTab);
            }

        });
        menuRadioButton(themeRadioButton, toggleGroup);
        RadioButton connRadioButton = new RadioButton("Connection");
        menuRadioButton(connRadioButton, toggleGroup);
        RadioButton aboutRadioButton = new RadioButton("About");
        menuRadioButton(aboutRadioButton, toggleGroup);

        menuBox.getChildren().addAll(profileButton, themeRadioButton, connRadioButton, aboutRadioButton);

        StackPane contentPane = new StackPane();
        borderPane.setCenter(contentPane);

        BorderPane pane = new BorderPane();
        contentPane.getChildren().add(pane);

        StackPane centerPane = new StackPane();
        pane.setCenter(centerPane);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.getStyleClass().add("task-scroll");
        centerPane.getChildren().add(scrollPane);

        StackPane containerPane = new StackPane();
        scrollPane.setContent(containerPane);

        tabPane = new TabPane();
        containerPane.getChildren().add(tabPane);

        profileTab = new Tab();
        themeTab = new Tab();
        connectTab = new Tab();
        aboutTab = new Tab();

        tabPane.getTabs().addAll(profileTab, themeTab, connectTab, aboutTab);

        profileFunction(profileTab);
        themeFunction(themeTab);
    }

    public void themeFunction(Tab tab) {
        themeGroup = new ToggleGroup();
        themesGroup = new ToggleGroup();

        StackPane stackPane = new StackPane();
        stackPane.setPadding(new Insets(15));
        tab.setContent(stackPane);

        VBox vBox = new VBox();
        vBox.setSpacing(20);
        stackPane.getChildren().add(vBox);

        // FiRST ROW

        HBox firstHBox = new HBox();
        vBox.getChildren().add(firstHBox);

        StackPane leftPane = new StackPane();
        HBox.setHgrow(leftPane, Priority.ALWAYS);

        RadioButton defaultButton = new RadioButton();
        RadioButton defaultRadioButton = new RadioButton("Default");
        themeMethod(leftPane, defaultButton, defaultRadioButton, "#333", "rgb(243, 237, 237)");
        firstHBox.getChildren().add(leftPane);

        StackPane rightPane = new StackPane();
        HBox.setHgrow(rightPane, Priority.ALWAYS);

        RadioButton blueButton = new RadioButton();
        RadioButton blueRadioButton = new RadioButton("Blue");
        themeMethod(rightPane, blueButton, blueRadioButton, "Dodgerblue", "deepskyBlue");
        firstHBox.getChildren().add(rightPane);

        // SECOND ROW

        HBox secondHBox = new HBox();
        vBox.getChildren().add(secondHBox);

        StackPane secondLeft = new StackPane();
        HBox.setHgrow(secondLeft, Priority.ALWAYS);

        RadioButton redButton = new RadioButton();
        RadioButton redRadioButton = new RadioButton("Red");
        themeMethod(secondLeft, redButton, redRadioButton, "Crimson", "rgb(250, 131, 155)");
        secondHBox.getChildren().add(secondLeft);

        StackPane secondRight = new StackPane();
        HBox.setHgrow(secondRight, Priority.ALWAYS);

        RadioButton indigoButton = new RadioButton();
        RadioButton indigoRadioButton = new RadioButton("Indigo");
        themeMethod(secondRight, indigoButton, indigoRadioButton, "Indigo", "rgb(142, 62, 199)");
        secondHBox.getChildren().add(secondRight);

        // THIRD ROW

        HBox thirdHBox = new HBox();
        vBox.getChildren().add(thirdHBox);

        StackPane thirdLeft = new StackPane();
        HBox.setHgrow(thirdLeft, Priority.ALWAYS);

        RadioButton greenButton = new RadioButton();
        RadioButton greenRadioButton = new RadioButton("Green");
        themeMethod(thirdLeft, greenButton, greenRadioButton, "rgb(2, 158, 158)", "cyan");
        thirdHBox.getChildren().add(thirdLeft);

        StackPane thirdRight = new StackPane();
        HBox.setHgrow(thirdRight, Priority.ALWAYS);

        RadioButton pinkButton = new RadioButton();
        RadioButton pinkRadioButton = new RadioButton("Pink");
        themeMethod(thirdRight, pinkButton, pinkRadioButton, "deeppink", "pink");
        thirdHBox.getChildren().add(thirdRight);

        // FORTH ROW

        HBox forthHBox = new HBox();
        vBox.getChildren().add(forthHBox);

        StackPane forthLeft = new StackPane();
        HBox.setHgrow(forthLeft, Priority.ALWAYS);

        RadioButton whiteButton = new RadioButton();
        RadioButton whiteRadioButton = new RadioButton("White");
        themeMethod(forthLeft, whiteButton, whiteRadioButton, "rgb(243, 237, 237)", "snow");
        forthHBox.getChildren().add(forthLeft);

        StackPane forthRight = new StackPane();
        HBox.setHgrow(forthRight, Priority.ALWAYS);

        RadioButton orangeButton = new RadioButton();
        RadioButton orangeRadioButton = new RadioButton("Orange");
        themeMethod(forthRight, orangeButton, orangeRadioButton, "darkorange", "orange");
        forthHBox.getChildren().add(forthRight);

    }

    public void themeMethod(StackPane stackPane, RadioButton radioButton, RadioButton textButton, String color,
            String colors) {
        StackPane pane = new StackPane();
        pane.setMaxWidth(200);
        pane.setMinWidth(200);
        stackPane.getChildren().add(pane);

        VBox vBox = new VBox();
        vBox.setSpacing(5);
        pane.getChildren().add(vBox);

        StackPane contentPane = new StackPane();
        contentPane.setEffect(new DropShadow(5, Color.BLACK));
        contentPane.setMaxHeight(100);
        contentPane.setMinHeight(100);
        contentPane.setPrefWidth(200);

        BorderPane borderPane = new BorderPane();
        contentPane.getChildren().add(borderPane);

        StackPane leftPane = new StackPane();
        leftPane.setPrefWidth(50);
        leftPane.setStyle("-fx-background-color: " + color);
        borderPane.setLeft(leftPane);

        StackPane centerPane = new StackPane();
        centerPane.setAlignment(Pos.TOP_CENTER);
        borderPane.setCenter(centerPane);

        BorderPane tmpPane = new BorderPane();
        centerPane.getChildren().add(tmpPane);

        StackPane topPane = new StackPane();
        topPane.setPrefHeight(20);
        topPane.setStyle("-fx-background-color: " + colors);
        tmpPane.setTop(topPane);

        StackPane midPane = new StackPane();
        midPane.setStyle("-fx-background-color: white;");
        tmpPane.setCenter(midPane);

        radioButton.setGraphic(contentPane);
        radioButton.setToggleGroup(themeGroup);
        radioButton.setPadding(new Insets(5));
        radioButton.setCursor(Cursor.HAND);
        radioButton.getStyleClass().remove("radio-button");
        radioButton.getStyleClass().add("settingsTheme");
        vBox.getChildren().add(radioButton);

        StackPane titlePane = new StackPane();
        titlePane.setAlignment(Pos.CENTER);
        vBox.getChildren().add(titlePane);

        textButton.setTextFill(Color.GRAY);
        textButton.setToggleGroup(themesGroup);
        textButton.setCursor(Cursor.HAND);
        textButton.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 16));
        textButton.getStyleClass().remove("radio-button");
        textButton.getStyleClass().add("settingstext");
        titlePane.getChildren().add(textButton);

        radioButton.selectedProperty().bindBidirectional(textButton.selectedProperty());
        radioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                App.theme = textButton.getText();
                App.themeProperty.set(!App.themeProperty.get());
            }

        });
    }

    public void profileFunction(Tab tab) {
        StackPane stackPane = new StackPane();
        tab.setContent(stackPane);

        VBox vBox = new VBox();
        vBox.setSpacing(30);
        stackPane.getChildren().add(vBox);

        StackPane logoPane = new StackPane();
        logoPane.setPadding(new Insets(0, 10, 0, 10));
        vBox.getChildren().add(logoPane);

        VBox logoBox = new VBox();
        logoBox.setPadding(new Insets(30, 0, 30, 0));
        logoBox.setSpacing(8);
        logoBox.setStyle("-fx-border-width: 0 0 2 0; -fx-border-color: rgb(240,240,240);");
        logoPane.getChildren().add(logoBox);

        StackPane imagePane = new StackPane();
        logoBox.getChildren().add(imagePane);

        String imageLink = "files/icons/user_1.png";
        File imageFile = new File("Team Work/src/files/logo/logo.png");
        if (imageFile.exists()) {
            imageLink = "files/logo/logo.png";
        }

        ImageView imageView = new ImageView();
        imageView.setImage(new Image(imageLink));
        imageView.setFitWidth(130);
        imageView.setFitHeight(130);
        imageView.setStyle("-fx-border-radius: 150; -fx-background-radius: 150;");
        imagePane.getChildren().add(imageView);

        StackPane namePane = new StackPane();
        logoBox.getChildren().add(namePane);

        Text username = new Text(App.username);
        username.setFont(Font.font("Arial Rounded MT Bold", 17));
        username.setFill(Color.rgb(140, 140, 140));
        namePane.getChildren().add(username);

        StackPane changeLogo = new StackPane();
        logoBox.getChildren().add(changeLogo);

        Button changeButton = new Button("Change Logo");
        changeButton.setPrefSize(120, 24);
        changeButton.setTextFill(Color.WHITE);
        changeButton.setStyle("-fx-background-color: dodgerblue;");
        changeButton.setCursor(Cursor.HAND);
        changeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(new Stage());
                if (file != null) {
                    File destination = new File("Team Work/src/files/logo/logo.png");
                    try {
                        Path path = Files.copy(file.toPath(), destination.toPath(),
                                StandardCopyOption.REPLACE_EXISTING);
                        imageView.setImage(new Image("files/logo/logo.png"));
                    } catch (Exception e) {
                        System.out.print(e);
                    }
                }
            }

        });
        changeLogo.getChildren().add(changeButton);

        VBox inforBox = new VBox();
        inforBox.setSpacing(10);
        vBox.getChildren().add(inforBox);

        // USERNAME

        usernameLabel = new Label(App.username);
        Button usernameButton = new Button("Update");
        usernameButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                updatePopup("username", 200);
            }

        });
        updateGUI(inforBox, "Username", usernameLabel, usernameButton);

        Button passwordButton = new Button("Update");
        passwordButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                updatePopup("password", 330);
            }

        });
        updateGUI(inforBox, "Password", new Label(), passwordButton);

        Label emailLabel = new Label("EmailAddress@gmail.com");
        Button emailButton = new Button("Update");
        updateGUI(inforBox, "Email", emailLabel, emailButton);

        nameLabel = new Label("Lyman");
        Button nameButton = new Button("Update");
        nameButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                updatePopup("name", 200);
            }

        });
        updateGUI(inforBox, "Name", nameLabel, nameButton);

        surnameLabel = new Label("Ken");
        Button surnameButton = new Button("Update");
        surnameButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                updatePopup("surname", 200);
            }

        });
        updateGUI(inforBox, "Surname", surnameLabel, surnameButton);

    }

    public void updatePopup(String index, int hieght) {
        updateStage = new Stage();

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
                updateStage.setX(mouseEvent.getScreenX() - xSet);
                updateStage.setY(mouseEvent.getScreenY() - ySet);
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
                updateStage.close();
            }

        });
        cancelPane.getChildren().add(cancelButton);

        StackPane contentPane = new StackPane();
        vBox.getChildren().add(contentPane);

        if (index.equals("username")) {
            usernameUpdate(contentPane);
        }
        if (index.equals("password")) {
            passwordUpdate(contentPane);
        }
        if (index.equals("name")) {
            detailsUpdate(contentPane, "name");
        }
        if (index.equals("surname")) {
            detailsUpdate(contentPane, "surname");
        }

        Scene updateScene = new Scene(stackPane, 500, hieght);
        updateScene.setFill(Color.TRANSPARENT);

        updateStage.setScene(updateScene);
        updateStage.initStyle(StageStyle.TRANSPARENT);
        updateStage.setTitle("Team Work Hub - Update Profile");
        updateStage.getIcons().add(new Image("files/icons/group.png"));
        updateStage.initModality(Modality.APPLICATION_MODAL);
        updateStage.showAndWait();
    }

    public void detailsUpdate(StackPane stackPane, String string) {
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        stackPane.getChildren().add(vBox);

        StackPane titlePane = new StackPane();
        vBox.getChildren().add(titlePane);

        Text titleText = new Text("Update " + string);
        titleText.setFont(Font.font("Arial Rounded MT Bold", 17));
        titleText.setFill(Color.rgb(31, 31, 31));
        titlePane.getChildren().add(titleText);

        StackPane pane = new StackPane();
        vBox.getChildren().add(pane);

        TextField textField = new TextField();
        textField.setPrefHeight(27);
        textField.setPromptText("Enter new " + string);
        pane.getChildren().add(textField);

        StackPane buttonPane = new StackPane();
        buttonPane.setAlignment(Pos.CENTER_RIGHT);
        vBox.getChildren().add(buttonPane);

        Button button = new Button("update");
        button.setPrefSize(100, 23);
        button.setPadding(new Insets(1));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: dodgerblue;");
        button.setCursor(Cursor.HAND);
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String valueString = textField.getText();
                if (!valueString.equals("")) {
                    try {
                        String query = "UPDATE users SET " + string + " = '" + valueString + "' WHERE id = '" + App.id
                                + "' ";
                        output.executeUpdate(query);
                        if (string.equals("name")) {
                            nameLabel.setText(valueString);
                        } else {
                            surnameLabel.setText(valueString);
                        }
                    } catch (Exception e) {
                        System.out.print(e);
                    }
                    updateStage.close();
                }
            }

        });
        buttonPane.getChildren().add(button);

    }

    public void passwordUpdate(StackPane stackPane) {
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        stackPane.getChildren().add(vBox);

        StackPane titlePane = new StackPane();
        vBox.getChildren().add(titlePane);

        Text titleText = new Text("Password Update");
        titleText.setFont(Font.font("Arial Rounded MT Bold", 17));
        titleText.setFill(Color.rgb(31, 31, 31));
        titlePane.getChildren().add(titleText);

        // OLD PASSWORD

        VBox oldBox = new VBox();
        oldBox.setSpacing(4);
        vBox.getChildren().add(oldBox);

        StackPane oldPane = new StackPane();
        oldBox.getChildren().add(oldPane);

        Text oldText = new Text("Old Password");
        oldText.setFont(Font.font("Arial Rounded MT Bold", 16));
        oldText.setFill(Color.GRAY);
        StackPane.setAlignment(oldText, Pos.CENTER_LEFT);
        oldPane.getChildren().add(oldText);

        ImageView oldView = new ImageView(new Image("files/icons/hide.png"));
        oldView.setFitHeight(20);
        oldView.setFitWidth(20);

        PasswordField oldField = new PasswordField();
        oldField.setPrefHeight(27);
        oldField.setPromptText("Your old Password");
        TextField oldTextField = new TextField();
        oldTextField.setPromptText("Your old Password");
        oldTextField.setPrefHeight(27);
        oldField.textProperty().bindBidirectional(oldTextField.textProperty());

        Button oldButton = new Button();
        oldButton.setPadding(new Insets(1));
        oldButton.setGraphic(oldView);
        oldButton.setStyle("-fx-background-color: transparent;");
        oldButton.setCursor(Cursor.HAND);
        StackPane.setAlignment(oldButton, Pos.CENTER_RIGHT);
        oldButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (oldPassBoolean != true) {
                    oldView.setImage(new Image("files/icons/show.png"));
                    oldTextField.toFront();
                    oldPassBoolean = !oldPassBoolean;
                } else {
                    oldView.setImage(new Image("files/icons/hide.png"));
                    oldField.toFront();
                    oldPassBoolean = !oldPassBoolean;
                }
            }

        });
        oldPane.getChildren().add(oldButton);

        StackPane oldStackPane = new StackPane(oldTextField, oldField);
        oldBox.getChildren().add(oldStackPane);

        // NEW PASSWORD

        VBox newBox = new VBox();
        newBox.setSpacing(4);
        vBox.getChildren().add(newBox);

        StackPane newPane = new StackPane();
        newBox.getChildren().add(newPane);

        Text newText = new Text("New Password");
        newText.setFont(Font.font("Arial Rounded MT Bold", 16));
        newText.setFill(Color.GRAY);
        StackPane.setAlignment(newText, Pos.CENTER_LEFT);
        newPane.getChildren().add(newText);

        ImageView newView = new ImageView(new Image("files/icons/hide.png"));
        newView.setFitHeight(20);
        newView.setFitWidth(20);

        PasswordField newField = new PasswordField();
        newField.setPrefHeight(27);
        newField.setPromptText("Enter new password");
        TextField newTextField = new TextField();
        newTextField.setPrefHeight(27);
        newTextField.setPromptText("Enter new password");
        newField.textProperty().bindBidirectional(newTextField.textProperty());

        Button newButton = new Button();
        newButton.setPadding(new Insets(1));
        newButton.setGraphic(newView);
        newButton.setStyle("-fx-background-color: transparent;");
        newButton.setCursor(Cursor.HAND);
        StackPane.setAlignment(newButton, Pos.CENTER_RIGHT);
        newButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (newPassBoolean != true) {
                    newView.setImage(new Image("files/icons/show.png"));
                    newTextField.toFront();
                    newPassBoolean = !newPassBoolean;
                } else {
                    newView.setImage(new Image("files/icons/hide.png"));
                    newField.toFront();
                    newPassBoolean = !newPassBoolean;
                }
            }

        });
        newPane.getChildren().add(newButton);

        StackPane newStackPane = new StackPane(newTextField, newField);
        newBox.getChildren().add(newStackPane);

        // CONFIRM PASSWORD

        VBox confirmBox = new VBox();
        confirmBox.setSpacing(4);
        vBox.getChildren().add(confirmBox);

        StackPane confirmPane = new StackPane();
        confirmBox.getChildren().add(confirmPane);

        Text confirmText = new Text("Old Password");
        confirmText.setFont(Font.font("Arial Rounded MT Bold", 16));
        confirmText.setFill(Color.GRAY);
        StackPane.setAlignment(confirmText, Pos.CENTER_LEFT);
        confirmPane.getChildren().add(confirmText);

        ImageView confirmView = new ImageView(new Image("files/icons/hide.png"));
        confirmView.setFitHeight(20);
        confirmView.setFitWidth(20);

        PasswordField confirmField = new PasswordField();
        confirmField.setPrefHeight(27);
        confirmField.setPromptText("Re-Enter new Password");
        TextField confirmTextField = new TextField();
        confirmTextField.setPrefHeight(27);
        confirmTextField.setPromptText("Re-Enter new Password");
        confirmField.textProperty().bindBidirectional(confirmTextField.textProperty());

        Button confirmButton = new Button();
        confirmButton.setPadding(new Insets(1));
        confirmButton.setGraphic(confirmView);
        confirmButton.setStyle("-fx-background-color: transparent;");
        confirmButton.setCursor(Cursor.HAND);
        StackPane.setAlignment(confirmButton, Pos.CENTER_RIGHT);
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (confirmBoolean != true) {
                    confirmView.setImage(new Image("files/icons/show.png"));
                    confirmTextField.toFront();
                    confirmBoolean = !confirmBoolean;
                } else {
                    confirmView.setImage(new Image("files/icons/hide.png"));
                    confirmField.toFront();
                    confirmBoolean = !confirmBoolean;
                }
            }

        });
        confirmPane.getChildren().add(confirmButton);

        StackPane confirmStackPane = new StackPane(confirmTextField, confirmField);
        confirmBox.getChildren().add(confirmStackPane);

        StackPane updatePane = new StackPane();
        updatePane.setAlignment(Pos.CENTER_RIGHT);
        vBox.getChildren().add(updatePane);

        Button button = new Button("update");
        button.setPrefSize(100, 23);
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: dodgerblue;");
        button.setCursor(Cursor.HAND);
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String oldString = oldField.getText();
                String newString = newField.getText();
                String confirmString = confirmField.getText();
                if (!oldString.equals("") && !newString.equals("") && !confirmString.equals("")) {
                    if (newString.equals(confirmString)) {
                        if (newString.length() > 7) {
                            String checkString = "";
                            try {
                                String query = "SELECT password FROM users WHERE id = '" + App.id + "'";
                                result = output.executeQuery(query);
                                while (result.next()) {
                                    checkString = result.getString("password");
                                }
                            } catch (Exception e) {
                                System.out.print(e);
                            }
                            if (checkString.equals(oldString)) {
                                try {
                                    String query = "UPDATE users SET password = '" + newString + "' WHERE id = '"
                                            + App.id + "'";
                                    output.executeUpdate(query);
                                } catch (Exception e) {
                                    System.out.print(e);
                                }
                                passwordField.setText(newString);
                                updateStage.close();
                            } else {
                                popupMessage("sad.png", "The old password you entered is incorrect");
                            }
                        } else {
                            popupMessage("sad.png", "Password require at least 8 characters");
                        }
                    } else {
                        popupMessage("sad.png", "Make sure Confirm password match new password");
                    }
                } else {
                    popupMessage("sad.png", "Fill all fields to proceed updating your password");
                }
            }

        });
        updatePane.getChildren().add(button);

    }

    public void usernameUpdate(StackPane stackPane) {

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        stackPane.getChildren().add(vBox);

        StackPane textPane = new StackPane();
        vBox.getChildren().add(textPane);

        Text text = new Text("Change Username");
        text.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 17));
        text.setFill(Color.rgb(31, 31, 31));
        textPane.getChildren().add(text);

        StackPane pane = new StackPane();
        vBox.getChildren().add(pane);

        TextField textField = new TextField();
        textField.setPromptText("New Username");
        textField.setPrefHeight(27);
        pane.getChildren().add(textField);

        StackPane buttonPane = new StackPane();
        buttonPane.setAlignment(Pos.CENTER_RIGHT);
        vBox.getChildren().add(buttonPane);

        Button button = new Button("update");
        button.setPrefSize(100, 23);
        button.setPadding(new Insets(1));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: dodgerblue;");
        button.setCursor(Cursor.HAND);
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                String newUsername = textField.getText();
                if (!newUsername.equals("")) {
                    int usernameLength = newUsername.length();
                    if (usernameLength > 7 && usernameLength < 31) {
                        Boolean checkBoolean = false;
                        try {
                            String query = "SELECT username FROM users WHERE username = '" + newUsername + "'";
                            result = output.executeQuery(query);
                            while (result.next()) {
                                checkBoolean = true;
                            }
                        } catch (Exception e) {
                            System.out.print(e);
                        }
                        if (checkBoolean != true) {
                            try {
                                String query = "UPDATE users SET username = '" + newUsername + "' WHERE id = '" + App.id
                                        + "'";
                                output.executeUpdate(query);
                                App.username = newUsername;
                            } catch (Exception e) {
                                System.out.print(e);
                            }
                            usernameLabel.setText(newUsername);
                            updateStage.close();
                        } else {
                            popupMessage("sad.png", "Username already exist");
                        }
                    } else {
                        popupMessage("sad.png", "Username require at least 8 to 30 Characters");
                    }
                }
            }

        });
        buttonPane.getChildren().add(button);

    }

    public void updateGUI(VBox vBox, String indexString, Label label, Button button) {
        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(hBox);

        Label indexLabel = new Label(indexString);
        indexLabel.setTextFill(Color.GRAY);
        indexLabel.setFont(Font.font("Arial Rounded MT Bold", 17));
        indexLabel.setPrefWidth(120);

        Label dotLabel = new Label(":");
        dotLabel.setTextFill(Color.GRAY);
        dotLabel.setFont(Font.font("Arial Rounded MT Bold", 17));

        button.setPrefSize(100, 23);
        button.setPadding(new Insets(1));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: dodgerblue;");
        button.setCursor(Cursor.HAND);

        if (indexString.equals("Password")) {
            passwordField = new PasswordField();
            passwordField.setFont(Font.font("Arial Rounded MT Bold"));
            passwordField.setEditable(false);
            passwordField.setText("mypassword");
            passwordField.setPrefWidth(340);

            hBox.getChildren().addAll(indexLabel, dotLabel, passwordField, button);
        } else {
            label.setTextFill(Color.rgb(150, 150, 150));
            label.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 17));
            label.setPrefWidth(340);
            label.setWrapText(true);

            hBox.getChildren().addAll(indexLabel, dotLabel, label, button);
        }
    }

    public void menuRadioButton(RadioButton radioButton, ToggleGroup toggleGroup) {
        radioButton.setPadding(new Insets(7, 10, 7, 10));
        radioButton.setTextFill(Color.rgb(51, 51, 51));
        radioButton.setPrefWidth(200);
        radioButton.setCursor(Cursor.HAND);
        radioButton.setToggleGroup(toggleGroup);
        radioButton.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 16));
        radioButton.getStyleClass().remove("radio-button");
        radioButton.getStyleClass().add("settings-menu");
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
}
