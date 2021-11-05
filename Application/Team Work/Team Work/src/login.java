import java.nio.ByteBuffer;
import java.sql.*;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

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
import javafx.scene.layout.FlowPane;
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

public class login {

    Text usernameLog = new Text();
    TabPane tabPane;
    Tab loginTab, existTab;

    String selectedId = "";
    Boolean logBoolean = false;

    int popup = 0;
    private double xSet = 0;
    private double ySet = 0;

    private double xOffset = 0;
    private double YOffset = 0;

    Stage stage, signStage;
    Button signButton, cancelButton;

    ToggleGroup toggleGroup = new ToggleGroup();

    Set<String> idSet = new TreeSet<String>();

    public Connection conn;
    public Statement output;
    public ResultSet result;

    public login() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/team work hub", "root", "mydatabase");
            output = conn.createStatement();
        } catch (Exception e) {
            System.out.print(e);
        }

        loginFunction();
    }

    public void loginFunction() {
        StackPane introPane = new StackPane();
        introPane.setPrefWidth(300);
        introPane.setMaxWidth(300);
        introPane.setStyle("-fx-background-color:rgb(255,255,255,0.85);");

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(20));
        vBox.setSpacing(10);
        introPane.getChildren().add(vBox);

        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        vBox.getChildren().add(box);

        Text titleText = new Text("Team Work");
        titleText.setFont(Font.font("Arial Rounded MT Bold", FontWeight.BOLD, 35));
        box.getChildren().add(titleText);

        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        vBox.getChildren().add(flowPane);

        ImageView iconView = new ImageView(new Image("files/icons/image_2.png"));
        iconView.setFitHeight(150);
        iconView.setFitWidth(150);
        flowPane.getChildren().add(iconView);

        FlowPane flowPane2 = new FlowPane();
        flowPane2.setAlignment(Pos.CENTER);
        vBox.getChildren().add(flowPane2);

        Text text = new Text("Hello");
        text.setFont(Font.font("gabriola", FontWeight.EXTRA_BOLD, 40));
        flowPane2.getChildren().add(text);

        FlowPane flowPane3 = new FlowPane();
        flowPane3.setAlignment(Pos.CENTER);
        vBox.getChildren().add(flowPane3);

        Text text2 = new Text();
        text2.setText(
                "Team Work System is a standard application used to connect team or group members together. It allow users to share data, work and send messages within the group. User can create or join the group.");
        text2.setFont(Font.font("Calibri", 20));
        text2.setWrappingWidth(300);
        text2.setTextAlignment(TextAlignment.CENTER);
        flowPane3.getChildren().add(text2);

        // Wellcome

        StackPane contPane = new StackPane();
        contPane.setPrefWidth(500);
        contPane.setStyle("-fx-background-color:rgb(231, 90, 118)");

        VBox vBox2 = new VBox();
        contPane.getChildren().add(vBox2);

        VBox vBox3 = new VBox();
        vBox3.setStyle("-fx-background-color:#333;");
        vBox2.getChildren().add(vBox3);

        HBox wellBox = new HBox();
        wellBox.setPadding(new Insets(20));
        wellBox.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                xOffset = mouseEvent.getSceneX();
                YOffset = mouseEvent.getSceneY();
            }

        });
        wellBox.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.setX(mouseEvent.getScreenX() - xOffset);
                stage.setY(mouseEvent.getScreenY() - YOffset);
            }

        });
        vBox3.getChildren().add(wellBox);

        Text wellText = new Text("Welcome");
        wellText.setFont(Font.font("Arial Rounded MT Bold", 30));
        wellText.setFill(Color.WHITE);
        wellBox.getChildren().add(wellText);

        FlowPane iFlowPane = new FlowPane();
        iFlowPane.setAlignment(Pos.CENTER);
        vBox3.getChildren().add(iFlowPane);

        Text iText = new Text("Easy Secure your work and save data");
        iText.setFill(Color.WHITE);
        iText.setFont(Font.font("Arial Rounded MT Bold", 18));
        iFlowPane.getChildren().add(iText);

        FlowPane iFlowPane1 = new FlowPane();
        iFlowPane1.setAlignment(Pos.CENTER);
        vBox3.getChildren().add(iFlowPane1);

        Text iText1 = new Text("Login online");
        iText1.setFill(Color.WHITE);
        iText1.setFont(Font.font("Arial Rounded MT Bold", 18));
        iFlowPane1.getChildren().add(iText1);

        FlowPane buttoFlowPane = new FlowPane();
        buttoFlowPane.setAlignment(Pos.CENTER);
        buttoFlowPane.setPadding(new Insets(20));
        vBox3.getChildren().add(buttoFlowPane);

        Button getButton = new Button("Get Started");
        getButton.setPrefWidth(120);
        getButton.setTextFill(Color.WHITE);
        getButton.setStyle("-fx-background-color:Dodgerblue;");
        buttoFlowPane.getChildren().add(getButton);

        VBox localBox = new VBox();
        vBox2.getChildren().add(localBox);

        FlowPane locaFlowPane = new FlowPane();
        locaFlowPane.setAlignment(Pos.CENTER);
        locaFlowPane.setPadding(new Insets(20));
        localBox.getChildren().add(locaFlowPane);

        ImageView imageView2 = new ImageView(new Image("files/icons/user_1.png"));
        imageView2.setFitWidth(100);
        imageView2.setFitHeight(100);
        locaFlowPane.getChildren().add(imageView2);

        systemAccounts(localBox);

        // Sign up | Sign in

        StackPane accouPane = new StackPane();
        accouPane.setPrefWidth(350);
        accouPane.setMaxWidth(350);
        accouPane.setStyle("-fx-background-color:snow;");

        loginGUI(accouPane);

        HBox contBox = new HBox();
        contBox.getChildren().addAll(introPane, contPane, accouPane);

        Scene scene = new Scene(contBox, 1150, 500);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add("application.css");
        stage = new Stage();
        stage.setTitle("Team Work - Login");
        stage.getIcons().add(new Image("files/icons/group.png"));
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    public void systemAccounts(VBox vBox) {
        try {
            String query = "SELECT user_id FROM system_account";
            result = output.executeQuery(query);
            while (result.next()) {
                idSet.add(result.getString("user_id"));
            }
        } catch (Exception e) {
            System.out.print(e);
        }

        if (!idSet.isEmpty()) {
            logBoolean = true;

            StackPane stackPane = new StackPane();
            vBox.getChildren().add(stackPane);

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background-color: transparent;");
            scrollPane.getStyleClass().add("task-scroll");
            stackPane.getChildren().add(scrollPane);

            StackPane pane = new StackPane();
            pane.setPadding(new Insets(0, 5, 10, 5));
            scrollPane.setContent(pane);

            VBox accountBox = new VBox();
            pane.getChildren().add(accountBox);

            int a = 0;

            for (String eachId : idSet) {
                String username = getString("username", "users", eachId);
                RadioButton radioButton = new RadioButton(username);
                buttonFunction(radioButton);
                radioButton.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        tabPane.getSelectionModel().select(existTab);
                        radioButton.setSelected(true);
                        usernameLog.setText(username);
                        selectedId = eachId;
                    }

                });
                accountBox.getChildren().add(radioButton);
                if (a == 0) {
                    radioButton.setSelected(true);
                    usernameLog.setText(username);
                    selectedId = eachId;
                }
                a++;
            }
        } else {
            FlowPane locaPane = new FlowPane();
            locaPane.setAlignment(Pos.CENTER);
            locaPane.setPadding(new Insets(10));
            vBox.getChildren().add(locaPane);

            Text localText = new Text("Logged in Account will be viewd here");
            localText.setFont(Font.font("Arial Rounded MT Bold", 17));
            localText.setFill(Color.WHITE);
            locaPane.getChildren().add(localText);
        }
    }

    public void signFunction() {

        FlowPane cancePane = new FlowPane();
        cancePane.setPadding(new Insets(5));
        cancePane.setAlignment(Pos.CENTER_RIGHT);
        cancePane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                xSet = mouseEvent.getSceneX();
                ySet = mouseEvent.getSceneY();
            }
        });
        cancePane.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                signStage.setX(mouseEvent.getScreenX() - xSet);
                signStage.setY(mouseEvent.getScreenY() - ySet);
            }

        });

        ImageView imageView = new ImageView(new Image("cancel.png"));
        imageView.setFitHeight(11);
        imageView.setFitWidth(11);

        cancelButton = new Button();
        cancelButton.setGraphic(imageView);
        cancelButton.setStyle("-fx-background-color:transparent;");
        cancelButton.setCursor(Cursor.HAND);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                signStage.close();
            }

        });
        cancePane.getChildren().add(cancelButton);

        FlowPane titlePane = new FlowPane();
        titlePane.setAlignment(Pos.CENTER);
        titlePane.setPadding(new Insets(0, 30, 0, 30));

        Text titleText = new Text("Create Account");
        titleText.setFont(Font.font("Arial Rounded MT Bold", FontWeight.BOLD, 25));
        titlePane.getChildren().add(titleText);

        // USERNAME

        VBox usernameBox = new VBox();
        usernameBox.setPadding(new Insets(15, 20, 0, 20));
        usernameBox.setSpacing(7);

        Text usernameText = new Text("Username");
        usernameText.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        usernameText.setFill(Color.GRAY);
        usernameBox.getChildren().add(usernameText);

        TextField usernameField = new TextField();
        usernameField.setPrefWidth(300);
        usernameField.setPrefHeight(30);
        usernameField.setPromptText("Username");
        usernameBox.getChildren().add(usernameField);

        // PASSWORD

        VBox passwordBox = new VBox();
        passwordBox.setPadding(new Insets(15, 20, 0, 20));
        passwordBox.setSpacing(7);

        Text passwordText = new Text("Password");
        passwordText.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        passwordText.setFill(Color.GRAY);
        passwordBox.getChildren().add(passwordText);

        TextField passwordField = new TextField();
        passwordField.setPrefWidth(300);
        passwordField.setPrefHeight(30);
        passwordField.setPromptText("Password");
        passwordBox.getChildren().add(passwordField);

        // CONFIRM PASSWORD

        VBox confirmBox = new VBox();
        confirmBox.setPadding(new Insets(15, 20, 0, 20));
        confirmBox.setSpacing(7);

        Text confirmText = new Text("Confirm Password");
        confirmText.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        confirmText.setFill(Color.GRAY);
        confirmBox.getChildren().add(confirmText);

        TextField confirmField = new TextField();
        confirmField.setPrefWidth(300);
        confirmField.setPrefHeight(30);
        confirmField.setPromptText("Password");
        confirmBox.getChildren().add(confirmField);

        // NAME

        VBox nameBox = new VBox();
        nameBox.setPadding(new Insets(15, 20, 0, 20));
        nameBox.setSpacing(5);

        Text nameText = new Text("Name");
        nameText.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        nameText.setFill(Color.GRAY);
        nameBox.getChildren().add(nameText);

        TextField nameField = new TextField();
        nameField.setPrefWidth(300);
        nameField.setPrefHeight(30);
        nameField.setPromptText("First name");
        nameBox.getChildren().add(nameField);

        // SURNAME

        VBox surnameBox = new VBox();
        surnameBox.setPadding(new Insets(15, 20, 0, 20));
        surnameBox.setSpacing(5);

        Text surnameText = new Text("Surname");
        surnameText.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        surnameText.setFill(Color.GRAY);
        surnameBox.getChildren().add(surnameText);

        TextField surnameField = new TextField();
        surnameField.setPrefWidth(300);
        surnameField.setPrefHeight(30);
        surnameField.setPromptText("Last name");
        surnameBox.getChildren().add(surnameField);

        // EMAIL

        VBox emailBox = new VBox();
        emailBox.setPadding(new Insets(15, 20, 0, 20));
        emailBox.setSpacing(5);

        Text emailText = new Text("Email");
        emailText.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        emailText.setFill(Color.GRAY);
        emailBox.getChildren().add(emailText);

        TextField emailField = new TextField();
        emailField.setPrefWidth(300);
        emailField.setPrefHeight(30);
        emailField.setPromptText("Email Address");
        emailBox.getChildren().add(emailField);

        // SUBMIT

        VBox submitBox = new VBox();
        submitBox.setPadding(new Insets(12, 20, 0, 20));
        submitBox.setSpacing(30);

        Button signupButton = new Button("Sign Up");
        signupButton.setTextFill(Color.WHITE);
        signupButton.setStyle("-fx-background-color:dodgerblue;");
        signupButton.setCursor(Cursor.HAND);
        signupButton.setMaxWidth(460);
        signupButton.setPrefHeight(30);
        signupButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {

                String getUsername = usernameField.getText();
                String getPassword = passwordField.getText();
                String getConfirm = confirmField.getText();
                String getName = nameField.getText();
                String getSurname = surnameField.getText();
                String getEmail = emailField.getText();

                if (!getUsername.equals("") && !getPassword.equals("") && !getConfirm.equals("") && !getName.equals("")
                        && !getSurname.equals("") && !getEmail.equals("")) {
                    if (getUsername.length() > 7) {
                        if (getPassword.length() > 7) {
                            if (getPassword.equals(getConfirm)) {

                                Connection connection;
                                try {
                                    Class.forName("com.mysql.cj.jdbc.Driver");
                                    connection = DriverManager.getConnection(
                                            "jdbc:mysql://freedb.tech:3306/freedbtech_Teamworkhub",
                                            "freedbtech_Asapgang", "mydatabase1234");
                                    Statement statement = connection.createStatement();
                                    Boolean checkUsername = false;
                                    String query = "SELECT username FROM users WHERE username = '" + getUsername + "'";
                                    ResultSet resultSet = statement.executeQuery(query);
                                    while (resultSet.next()) {
                                        checkUsername = true;
                                    }
                                    if (checkUsername != true) {
                                        String getId = "";
                                        Boolean getBoolean = true;
                                        while (getBoolean != false) {
                                            getId = randomUUID();
                                            getBoolean = false;
                                            query = "SELECT id FROM users WHERE id = '" + getId + "'";
                                            resultSet = statement.executeQuery(query);
                                            while (resultSet.next()) {
                                                getBoolean = true;
                                            }
                                        }
                                        query = "INSERT INTO `users` (`id`,`username`,`password`,`name`,`surname`,`email`) VALUES ('"
                                                + getId + "','" + getUsername + "','" + getPassword + "','" + getName
                                                + "','" + getSurname + "','" + getEmail + "')";
                                        statement.executeUpdate(query);
                                        output.executeUpdate(query);
                                        query = "INSERT INTO `system_account` (`user_id`) VALUES ('" + getId + "')";
                                        output.executeUpdate(query);
                                        App.id = getId;
                                        App.username = getUsername;
                                        signStage.close();
                                        stage.close();
                                        new user();
                                    } else {
                                        popupMessage("sad.png", "Username Already exist in the system");
                                    }
                                } catch (Exception e) {
                                    System.out.print(e);
                                    popupMessage("sad.png", "You are offline, Connect to the internet");
                                }
                            } else {
                                popupMessage("sad.png", "Make sure Passwords you entered are matching");
                            }
                        } else {
                            popupMessage("sad.png", "Too short Password, It must have atleast 8 characters");
                        }
                    } else {
                        popupMessage("sad.png", "Too short Username, It must have atleast 8 characters");
                    }
                } else {
                    popupMessage("sad.png", "Make sure you fill all requied fields in this section");
                }
            }

        });
        submitBox.getChildren().add(signupButton);

        FlowPane footerPane = new FlowPane();
        footerPane.setAlignment(Pos.CENTER);
        footerPane.setPadding(new Insets(5, 0, 27, 0));
        submitBox.getChildren().add(footerPane);

        Text footerText = new Text("Team Work @ teamwork.com");
        footerText.setFont(Font.font("gabriola", FontWeight.EXTRA_BOLD, 25));
        footerText.setStyle("-fx-color:#222;");
        footerText.setTextAlignment(TextAlignment.CENTER);
        footerPane.getChildren().add(footerText);

        VBox signBox = new VBox();
        signBox.setEffect(new DropShadow(5, Color.BLACK));
        signBox.setStyle("-fx-background-color:white; -fx-border-radius:5; -fx-background-radius:5;");
        signBox.getChildren().addAll(cancePane, titlePane, usernameBox, passwordBox, confirmBox, nameBox, surnameBox,
                emailBox, submitBox);

        VBox signVBox = new VBox();
        signVBox.setStyle("-fx-background-color:transparent;");
        signVBox.setPadding(new Insets(10));
        signVBox.getChildren().add(signBox);

        Scene signScene = new Scene(signVBox, 500, 700);
        signScene.setFill(Color.TRANSPARENT);
        signStage = new Stage();
        signStage.getIcons().add(new Image("files/icons/group.png"));
        signStage.setTitle("Team Work - Sign Up");
        signStage.setScene(signScene);
        signStage.initStyle(StageStyle.TRANSPARENT);
        signStage.initModality(Modality.APPLICATION_MODAL);
        signStage.showAndWait();

    }

    public void loginGUI(StackPane stackPane) {
        BorderPane borderPane = new BorderPane();
        stackPane.getChildren().add(borderPane);

        ImageView imageView = new ImageView(new Image("cancel.png"));
        imageView.setFitHeight(11);
        imageView.setFitWidth(11);

        Button cancelButton = new Button();
        cancelButton.setStyle("-fx-background-color:transparent;");
        cancelButton.setGraphic(imageView);
        cancelButton.setCursor(Cursor.HAND);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                stage.close();
            }

        });

        FlowPane cancelPane = new FlowPane();
        cancelPane.setAlignment(Pos.CENTER_RIGHT);
        cancelPane.getChildren().add(cancelButton);
        cancelPane.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                xOffset = mouseEvent.getSceneX();
                YOffset = mouseEvent.getSceneY();
            }

        });
        cancelPane.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.setX(mouseEvent.getScreenX() - xOffset);
                stage.setY(mouseEvent.getScreenY() - YOffset);
            }

        });
        borderPane.setTop(cancelPane);

        // CENTER

        StackPane pane = new StackPane();
        borderPane.setCenter(pane);

        tabPane = new TabPane();
        tabPane.getStyleClass().add("main-tab");
        pane.getChildren().add(tabPane);

        loginTab = new Tab();
        existTab = new Tab();
        tabPane.getTabs().addAll(loginTab, existTab);

        if (logBoolean != false) {
            tabPane.getSelectionModel().select(existTab);
        }

        StackPane loginTabPane = new StackPane();
        loginTab.setContent(loginTabPane);

        StackPane existStackPane = new StackPane();
        existTab.setContent(existStackPane);

        existFunction(existStackPane);

        // ACCOUNT TAB

        VBox accountBox = new VBox();
        loginTabPane.getChildren().add(accountBox);

        FlowPane accountPane = new FlowPane();
        stackPane.setPadding(new Insets(10));
        accountBox.getChildren().add(accountPane);

        Text accountText = new Text("Sign In | Sign Up");
        accountText.setFont(Font.font("Arial Rounded MT Bold", 18));
        accountPane.getChildren().add(accountText);

        FlowPane loginPane = new FlowPane();
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setPadding(new Insets(40, 10, 20, 10));
        accountBox.getChildren().add(loginPane);

        Text loginText = new Text("Login");
        loginText.setFont(Font.font("gabriola", FontWeight.EXTRA_BOLD, 35));
        loginText.setFill(Color.DODGERBLUE);
        loginPane.getChildren().add(loginText);

        FlowPane usertextPane = new FlowPane();
        usertextPane.setPadding(new Insets(0, 0, 5, 5));
        accountBox.getChildren().add(usertextPane);

        // USERNAME INPUT

        Text usernameText = new Text("Username");
        usernameText.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        usertextPane.getChildren().add(usernameText);

        FlowPane usernamePane = new FlowPane();
        usernamePane.setAlignment(Pos.CENTER);
        accountBox.getChildren().add(usernamePane);

        TextField username = new TextField();
        username.setPromptText("Username");
        username.setPrefWidth(300);
        username.setPrefHeight(30);
        usernamePane.getChildren().add(username);

        FlowPane passtextPane = new FlowPane();
        passtextPane.setPadding(new Insets(10, 0, 5, 5));
        accountBox.getChildren().add(passtextPane);

        // PASSWORD INPUT

        Text passwordText = new Text("password");
        passwordText.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        passtextPane.getChildren().add(passwordText);

        FlowPane passwordPane = new FlowPane();
        passwordPane.setAlignment(Pos.CENTER);
        passwordPane.setPadding(new Insets(0, 0, 5, 0));
        accountBox.getChildren().add(passwordPane);

        TextField password = new TextField();
        password.setPromptText("Password");
        password.setPrefWidth(300);
        password.setPrefHeight(30);
        passwordPane.getChildren().add(password);

        FlowPane submitPane = new FlowPane();
        submitPane.setAlignment(Pos.CENTER);
        submitPane.setPadding(new Insets(10, 0, 10, 0));
        accountBox.getChildren().add(submitPane);

        Button submit = new Button("login");
        submit.setPrefWidth(300);
        submit.setPrefHeight(30);
        submit.setTextFill(Color.WHITE);
        submit.setCursor(Cursor.HAND);
        submit.setStyle("-fx-background-color:Dodgerblue;");
        submit.getStyleClass().add("loginButton");
        submit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {

                String passString = "", id = "", name = "", surname = "", email = "";
                Boolean connectBoolean = true, loginBoolean = false;

                String usernameInput = username.getText();
                String passwordInput = password.getText();

                Connection connection;
                try {
                    String query = "SELECT id,password,name,surname,email FROM users WHERE username = '" + usernameInput
                            + "'";

                    Class.forName("com.mysql.cj.jdbc.Driver");
                    connection = DriverManager.getConnection("jdbc:mysql://freedb.tech:3306/freedbtech_Teamworkhub",
                            "freedbtech_Asapgang", "mydatabase1234");
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    while (resultSet.next()) {
                        passString = resultSet.getString("password");
                        id = resultSet.getString("id");
                        name = resultSet.getString("name");
                        surname = resultSet.getString("surname");
                        email = resultSet.getString("email");
                        loginBoolean = true;
                    }
                } catch (Exception e) {
                    connectBoolean = false;
                    System.out.print(e);
                }

                if (connectBoolean != false) {
                    if (loginBoolean != false) {
                        if (passString.equals(passwordInput)) {
                            try {
                                String query = "INSERT INTO `users` (`id`,`username`,`password`,`name`,`surname`,`email`) VALUES ('"
                                        + id + "','" + usernameInput + "','" + passString + "','" + name + "','"
                                        + surname + "','" + email + "')";
                                output.executeUpdate(query);
                                query = "INSERT INTO `system_account` (`user_id`) VALUES ('" + id + "')";
                                output.executeUpdate(query);
                            } catch (Exception e) {
                                System.out.print(e);
                            }
                            App.id = id;
                            App.username = usernameInput;
                            stage.close();
                            new user();
                        } else {
                            popupMessage("sad.png", "Username or password you entered are incorrect");
                        }
                    } else {
                        popupMessage("sad.png", "Username or password you entered are incorrect");
                    }
                } else {
                    popupMessage("sad.png", "You are offline, Connect to the internet");
                }
            }

        });
        submitPane.getChildren().add(submit);

        FlowPane orPane = new FlowPane();
        orPane.setAlignment(Pos.CENTER);
        orPane.setPrefHeight(100);
        accountBox.getChildren().add(orPane);

        Text orText = new Text("OR");
        orText.setFont(Font.font("Arial Rounded MT Bold", 15));
        orText.setFill(Color.GRAY);
        orPane.getChildren().add(orText);

        FlowPane signPane = new FlowPane();
        signPane.setAlignment(Pos.CENTER);
        signPane.setPrefWidth(300);
        accountBox.getChildren().add(signPane);

        signButton = new Button("Sign up");
        signButton.setTextFill(Color.WHITE);
        signButton.setCursor(Cursor.HAND);
        signButton.setStyle("-fx-background-color:rgb(23, 179, 49);");
        signButton.setPrefWidth(200);
        signButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                signFunction();
            }

        });
        signPane.getChildren().add(signButton);
    }

    public void existFunction(StackPane stackPane) {
        VBox vBox = new VBox();
        vBox.setSpacing(35);
        stackPane.getChildren().add(vBox);

        StackPane localPane = new StackPane();
        localPane.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().add(localPane);

        Text localText = new Text("Local Account");
        localText.setFont(Font.font("Arial Rounded Mt Bold", 18));
        localText.setFill(Color.rgb(31, 31, 31));
        localPane.getChildren().add(localText);

        StackPane titlePane = new StackPane();
        vBox.getChildren().add(titlePane);

        Text loginText = new Text("Login");
        loginText.setFont(Font.font("Gabriola", FontWeight.EXTRA_BOLD, 34));
        loginText.setFill(Color.DODGERBLUE);
        titlePane.getChildren().add(loginText);

        VBox logBox = new VBox();
        logBox.setSpacing(7);
        vBox.getChildren().add(logBox);

        StackPane logoPane = new StackPane();
        logBox.getChildren().add(logoPane);

        ImageView imageView = new ImageView(new Image("files/icons/user_1.png"));
        imageView.setFitHeight(70);
        imageView.setFitWidth(70);
        logoPane.getChildren().add(imageView);

        StackPane usernamePane = new StackPane();
        logBox.getChildren().add(usernamePane);

        // usernameLog.setText("Username");
        usernameLog.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 18));
        usernameLog.setFill(Color.rgb(31, 31, 31));
        usernamePane.getChildren().add(usernameLog);

        StackPane passwordPane = new StackPane();
        logBox.getChildren().add(passwordPane);

        PasswordField passwordField = new PasswordField();
        passwordField.setPrefSize(300, 30);
        passwordField.setPromptText("Enter Password");
        passwordPane.getChildren().add(passwordField);

        StackPane submitPane = new StackPane();
        logBox.getChildren().add(submitPane);

        Button button = new Button("login");
        button.setTextFill(Color.WHITE);
        button.setPrefSize(100, 24);
        button.setPadding(new Insets(1));
        button.setCursor(Cursor.HAND);
        button.setStyle("-fx-background-color: dodgerblue");
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                String passwordData = passwordField.getText();
                if (!selectedId.equals("") && !passwordData.equals("")) {
                    String getPassword = "", getUsername = "";
                    try {
                        String query = "SELECT password,username FROM users WHERE id = '" + selectedId + "'";
                        result = output.executeQuery(query);
                        while (result.next()) {
                            getPassword = result.getString("password");
                            getUsername = result.getString("username");
                        }
                    } catch (Exception e) {
                        System.out.print(e);
                    }
                    if (passwordData.equals(getPassword)) {
                        App.id = selectedId;
                        App.username = getUsername;
                        stage.close();
                        new user();
                    } else {
                        popupMessage("sad.png", "Incorrect password. You can login with new account");
                    }
                }
            }

        });
        submitPane.getChildren().add(button);

        VBox switchBox = new VBox();
        switchBox.setPadding(new Insets(20, 0, 0, 0));
        switchBox.setSpacing(5);
        switchBox.setAlignment(Pos.CENTER);

        vBox.getChildren().add(switchBox);

        Text switchText = new Text("Sign in with new account ??");
        switchText.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_LIGHT, 18));
        switchText.setFill(Color.rgb(31, 31, 31));

        Button switchButton = new Button("Get Started");
        switchButton.setTextFill(Color.WHITE);
        switchButton.setPrefSize(110, 24);
        switchButton.setPadding(new Insets(1));
        switchButton.setCursor(Cursor.HAND);
        switchButton.setStyle("-fx-background-color: deepskyblue");
        switchButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                tabPane.getSelectionModel().select(loginTab);
            }

        });

        switchBox.getChildren().addAll(switchText, switchButton);
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

    public String getString(String indexString, String tableString, String idString) {
        String getString = "";
        try {
            String query = "SELECT " + indexString + " FROM " + tableString + " WHERE id = '" + idString + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                getString = result.getString(indexString);
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        return getString;
    }

    public void buttonFunction(RadioButton radioButton) {
        radioButton.setPadding(new Insets(5));
        radioButton.setTextFill(Color.WHITE);
        radioButton.setPrefWidth(500);
        radioButton.setCursor(Cursor.HAND);
        radioButton.setToggleGroup(toggleGroup);
        radioButton.getStyleClass().remove("radio-button");
        radioButton.getStyleClass().add("loginButtons");
    }

    public String randomUUID() {
        UUID uuid = UUID.randomUUID();
        long longId = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        String createdId = Long.toString(longId, Character.MAX_RADIX);
        return createdId;
    }
}
