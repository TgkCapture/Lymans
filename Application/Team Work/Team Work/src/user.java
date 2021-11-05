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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
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

import java.nio.ByteBuffer;
import java.sql.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class user {

    String teamId;

    Stage stage, createStage, popupStage;
    Scene scene;
    TabPane contentTabPane;
    Text selectText;
    Tab homeTab, notificationTab, settingsTab;
    ToggleGroup toggleGroup, teamGroup, mainGroup;
    VBox recentBox, teamsBox;

    private double xSet = 0;
    private double ySet = 0;

    public Connection conn;
    public Statement output;
    public ResultSet result;

    ListView<String> listView;

    int teamTotal = 0;
    Map<String, String> teamTabMap = new TreeMap<String, String>();
    Map<String, RadioButton> radioMap = new TreeMap<String, RadioButton>();
    Map<String, RadioButton> homeMap = new TreeMap<String, RadioButton>();
    Map<String, String> namesMap = new TreeMap<String, String>();
    Map<String, Integer> indexMap = new TreeMap<String, Integer>();
    Map<String, Tab> teamTabs = new TreeMap<String, Tab>();

    public user() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/team work hub", "root", "mydatabase");
            output = conn.createStatement();
        } catch (Exception e) {
            System.out.print(e);
        }

        try {
            String insideQuery = "";
            String query = "SELECT team_id FROM team_people WHERE user_id = '" + App.id + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                insideQuery = insideQuery + " OR id = '" + result.getString("team_id") + "'";
            }
            query = "SELECT id,name FROM teams WHERE team_owner = '" + App.id + "' " + insideQuery;
            result = output.executeQuery(query);
            while (result.next()) {
                Tab tab = new Tab();
                RadioButton radioButton = new RadioButton(result.getString("name"));
                RadioButton homeRadioButton = new RadioButton(result.getString("name"));
                radioButton.selectedProperty().bindBidirectional(homeRadioButton.selectedProperty());
                teamTabs.put(result.getString("id"), tab);
                radioMap.put(result.getString("id"), radioButton);
                homeMap.put(result.getString("id"), homeRadioButton);
                teamTabMap.put(result.getString("id"), "TeamTab_" + teamTotal);
                namesMap.put(result.getString("id"), result.getString("name"));
                indexMap.put(result.getString("id"), teamTotal);
                teamTotal++;
            }
        } catch (Exception e) {
            System.out.print(e);
        }

        userMain();
        // Tab tab = new Tab();
        // teams teams = new teams(tab, "new Tab");
        // teams.addTask();
        new onlineConnection();
    }

    public void userMain() {

        StackPane stackPane = new StackPane();

        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setStyle("-fx-background-color:snow;");
        stackPane.getChildren().add(mainBorderPane);

        // Side Menu

        StackPane sideMenu = new StackPane();
        sideMenu.setMaxWidth(250);
        sideMenu.setMinWidth(250);
        sideMenu.setStyle("-fx-background-color:#333;");
        mainBorderPane.setLeft(sideMenu);

        BorderPane sideBorderPane = new BorderPane();
        sideMenu.getChildren().add(sideBorderPane);

        VBox sideBox = new VBox();
        sideBorderPane.setTop(sideBox);

        FlowPane titlePane = new FlowPane();
        titlePane.setAlignment(Pos.CENTER);
        titlePane.setPadding(new Insets(13));
        titlePane.setStyle("-fx-background-color:#222;");
        sideBox.getChildren().add(titlePane);

        Text title = new Text("Team Work Hub");
        title.setFont(Font.font("Arial Rounded MT Bold", FontWeight.BOLD, 21));
        title.setFill(Color.WHITE);
        titlePane.getChildren().add(title);

        VBox menu = new VBox();
        menu.setStyle("-fx-background-color:rgba(34, 34, 34, 0.600);");
        menu.setPadding(new Insets(12));
        menu.setSpacing(5);
        sideBox.getChildren().add(menu);

        // HOMEBOX

        mainGroup = new ToggleGroup();

        RadioButton homeRadioButton = new RadioButton("Home");
        homeRadioButton.setSelected(true);
        buttonFunction(homeRadioButton);
        homeRadioButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                contentTabPane.getSelectionModel().select(homeTab);
                selectText.setText("Home");
            }

        });
        menu.getChildren().add(homeRadioButton);

        RadioButton createRadioButton = new RadioButton("Create Teams");
        buttonFunction(createRadioButton);
        createRadioButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                createTeams();
            }

        });
        menu.getChildren().add(createRadioButton);

        RadioButton notisRadioButton = new RadioButton("Notification");
        buttonFunction(notisRadioButton);
        notisRadioButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                contentTabPane.getSelectionModel().select(notificationTab);
                selectText.setText("Notification");
            }

        });
        menu.getChildren().add(notisRadioButton);

        RadioButton aboutRadioButton = new RadioButton("Help");
        buttonFunction(aboutRadioButton);
        // menu.getChildren().add(aboutRadioButton);

        // RECENT

        VBox recent = new VBox();
        recent.setPadding(new Insets(10));
        sideBox.getChildren().add(recent);

        FlowPane recentPane = new FlowPane();
        recentPane.setAlignment(Pos.CENTER);
        recentPane.setStyle("-fx-border-width:0 0 2 0; -fx-border-color:rgba(128,128,128,0.700);");
        recentPane.setPadding(new Insets(4, 0, 10, 0));
        recent.getChildren().add(recentPane);

        Text recentText = new Text("Recent Teams");
        recentText.setFont(Font.font("Arial Rounded MT Bold", 17));
        recentText.setFill(Color.GRAY);
        recentPane.getChildren().add(recentText);

        // Menu Teams

        StackPane teamsPane = new StackPane();
        sideBorderPane.setCenter(teamsPane);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.getStyleClass().add("task-scroll");
        teamsPane.getChildren().add(scrollPane);

        StackPane panes = new StackPane();
        scrollPane.setContent(panes);

        teamsBox = new VBox();
        panes.getChildren().add(teamsBox);

        teamGroup = new ToggleGroup();

        for (Map.Entry<String, RadioButton> entry : radioMap.entrySet()) {
            entry.getValue().setPadding(new Insets(4, 10, 4, 10));
            entry.getValue().setTextFill(Color.WHITE);
            entry.getValue().setPrefWidth(2000);
            entry.getValue().setCursor(Cursor.HAND);
            entry.getValue().setToggleGroup(teamGroup);
            entry.getValue().getStyleClass().remove("radio-button");
            entry.getValue().getStyleClass().add("teamsRadio");
            entry.getValue().setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent actionEvent) {
                    for (Map.Entry<String, Tab> entries : teamTabs.entrySet()) {
                        if (entries.getKey().equals(entry.getKey())) {
                            contentTabPane.getSelectionModel().select(entries.getValue());
                            selectText.setText(entry.getValue().getText());
                            App.selectedTeam = entries.getKey();
                        }
                    }
                }

            });
            teamsBox.getChildren().add(entry.getValue());

        }

        App.deleteProperty = new SimpleBooleanProperty();
        App.deleteProperty.addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                for (Map.Entry<String, RadioButton> entry : radioMap.entrySet()) {
                    if (App.selectedTeam.equals(entry.getKey())) {
                        teamsBox.getChildren().remove(entry.getValue());
                    }
                }
                for (Map.Entry<String, RadioButton> entry : homeMap.entrySet()) {
                    if (App.selectedTeam.equals(entry.getKey())) {
                        recentBox.getChildren().remove(entry.getValue());
                    }
                }
                contentTabPane.getTabs().remove(contentTabPane.getSelectionModel().getSelectedItem());
                contentTabPane.getSelectionModel().select(homeTab);
                selectText.setText("Home");
            }

        });

        App.teamProperty = new SimpleBooleanProperty();
        App.teamProperty.addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                System.out.print("hello");
                Statement statement;
                ResultSet resultSet;
                Map<String, String> tmpTeamMap = new TreeMap<String, String>();
                try {
                    statement = conn.createStatement();
                    String query = "SELECT team_id FROM team_people WHERE user_id = '" + App.id + "'";
                    resultSet = statement.executeQuery(query);
                    while (resultSet.next()) {
                        String name = getString("name", "teams", resultSet.getString("team_id"));
                        tmpTeamMap.put(resultSet.getString("team_id"), name);
                    }
                } catch (Exception e) {
                    System.out.print(e);
                }
                for (Map.Entry<String, String> entry : tmpTeamMap.entrySet()) {
                    Boolean isExist = false;
                    for (Map.Entry<String, String> entryEntry : namesMap.entrySet()) {
                        if (entry.getKey().equals(entryEntry.getKey())) {
                            isExist = true;
                        }
                    }
                    if (isExist == false) {
                        teamTabMap.put(entry.getKey(), "TeamTab_" + teamTotal);
                        Tab tab = new Tab();
                        tab.setText(entry.getValue());
                        new teams(tab, entry.getKey());
                        contentTabPane.getTabs().add(tab);

                        namesMap.put(entry.getKey(), entry.getValue());
                        listView.getItems().add(entry.getValue());
                        selectText.setText(entry.getValue());

                        teamTabs.put(entry.getKey(), tab);
                        indexMap.put(entry.getKey(), teamTotal);
                        teamTotal++;
                    }
                }
            }

        });

        // Setting

        StackPane settingPane = new StackPane();
        settingPane.setPadding(new Insets(20));
        settingPane.setStyle("-fx-background-color: rgba(34, 34, 34, 0.462);");
        settingPane.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                contentTabPane.getSelectionModel().select(settingsTab);
                selectText.setText("Settings");
            }

        });
        sideBorderPane.setBottom(settingPane);

        HBox settingBox = new HBox();
        settingBox.setAlignment(Pos.CENTER_LEFT);
        settingBox.setSpacing(10);
        settingBox.setCursor(Cursor.HAND);
        settingPane.getChildren().add(settingBox);

        ImageView settinView = new ImageView(new Image("files/icons/settings.png"));
        settinView.setFitHeight(25);
        settinView.setFitWidth(25);

        Text settingText = new Text("Settings");
        settingText.setFont(Font.font("Arial Rounded MT Bold", 17));
        settingText.setFill(Color.DARKGREY);

        settingBox.getChildren().addAll(settinView, settingText);

        // Container

        StackPane container = new StackPane();
        container.setStyle("-fx-background-color:Snow;");
        mainBorderPane.setCenter(container);

        BorderPane containerPane = new BorderPane();
        container.getChildren().add(containerPane);

        // Header

        VBox header = new VBox();
        header.setStyle("-fx-background-color:snow;");
        containerPane.setTop(header);

        BorderPane headerBorder = new BorderPane();
        header.getChildren().add(headerBorder);

        StackPane headerMenu = new StackPane();
        headerBorder.setLeft(headerMenu);

        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color:transparent;");
        headerMenu.getChildren().add(menuBar);

        Menu hMenu = new Menu();
        Label homeLabel = new Label("Home");
        homeLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                contentTabPane.getSelectionModel().select(homeTab);
                selectText.setText("Home");
            }

        });
        hMenu.setGraphic(homeLabel);

        Menu createMenu = new Menu();
        Label createMenuLabel = new Label("Create Teams");
        createMenuLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                createTeams();
            }

        });
        createMenu.setGraphic(createMenuLabel);

        Menu notifyMenu = new Menu();
        Label notificationLabel = new Label("Notification");
        notificationLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                contentTabPane.getSelectionModel().select(notificationTab);
                selectText.setText("Notification");
            }

        });
        notifyMenu.setGraphic(notificationLabel);

        Menu settingMenu = new Menu();
        Label settingLabel = new Label("Settings");
        settingLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                contentTabPane.getSelectionModel().select(settingsTab);
                selectText.setText("Settings");
            }

        });
        settingMenu.setGraphic(settingLabel);

        Menu aboutMenu = new Menu("About");
        menuBar.getMenus().addAll(hMenu, createMenu, notifyMenu, settingMenu);

        StackPane headerAccount = new StackPane();
        headerAccount.setAlignment(Pos.CENTER_RIGHT);
        headerBorder.setRight(headerAccount);

        HBox accountBox = new HBox();
        headerAccount.getChildren().add(accountBox);

        StackPane signBox = new StackPane();
        signBox.setAlignment(Pos.CENTER_RIGHT);
        signBox.setPadding(new Insets(5));
        accountBox.getChildren().add(signBox);

        MenuBar usernameBar = new MenuBar();
        usernameBar.setStyle("-fx-background-color:transparent; -fx-padding:0");
        usernameBar.setCursor(Cursor.HAND);
        signBox.getChildren().add(usernameBar);

        Text usernameText = new Text(App.username);
        usernameText.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
        usernameText.setFill(Color.GRAY);

        Menu username = new Menu();
        username.setStyle("-fx-background-color:transparent;");
        username.setGraphic(usernameText);
        usernameBar.getMenus().add(username);

        MenuItem logout = new MenuItem("   Logout   ");
        logout.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                App.id = "";
                App.username = "";
                stage.close();
                new login();
            }

        });
        username.getItems().add(logout);

        StackPane profilePane = new StackPane();
        profilePane.setAlignment(Pos.CENTER);
        profilePane.setPadding(new Insets(5));
        accountBox.getChildren().add(profilePane);

        ImageView profileView = new ImageView(new Image("files/icons/user.png"));
        profileView.setFitWidth(40);
        profileView.setFitHeight(40);
        profilePane.getChildren().add(profileView);

        // header Search

        BorderPane headerHBox = new BorderPane();
        headerHBox.setStyle("-fx-background-color:rgb(186, 216, 233)");
        header.getChildren().add(headerHBox);

        FlowPane selected = new FlowPane();
        selected.setPadding(new Insets(10, 0, 10, 17));
        selected.requestFocus();
        headerHBox.setLeft(selected);

        selectText = new Text("Home");
        selectText.setFont(Font.font("sans-serif", 20));
        selected.getChildren().add(selectText);

        FlowPane searchPane = new FlowPane();
        searchPane.setAlignment(Pos.CENTER_RIGHT);
        searchPane.setPadding(new Insets(5, 10, 5, 0));
        headerHBox.setRight(searchPane);

        TextField searchField = new TextField();
        searchField.setPromptText("Search");
        searchField.setPrefWidth(300);
        searchField.setStyle(
                "-fx-border-width: 2; -fx-border-color: grey; -fx-border-radius: 5; -fx-background-radius: 5; -fx-focus-color:transparent");
        searchPane.getChildren().add(searchField);

        // Content

        StackPane content = new StackPane();
        content.setStyle("-fx-background-color:white");
        containerPane.setCenter(content);

        BorderPane contentPane = new BorderPane();
        content.getChildren().add(contentPane);

        // Body

        StackPane body = new StackPane();
        contentPane.setCenter(body);

        contentTabPane = new TabPane();
        containerPane.getStyleClass().add("main-tab");
        body.getChildren().add(contentTabPane);

        homeTab = new Tab("Home");
        notificationTab = new Tab("Notification");
        settingsTab = new Tab("Settings");
        contentTabPane.getTabs().addAll(homeTab, settingsTab, notificationTab);

        for (Map.Entry<String, Tab> entry : teamTabs.entrySet()) {
            new teams(entry.getValue(), entry.getKey());
            contentTabPane.getTabs().add(entry.getValue());
        }

        StackPane home = new StackPane();
        homeTab.setContent(home);

        homeFunction(home);
        new notification(notificationTab);
        new settings(settingsTab);

        // Preview

        StackPane previewer = new StackPane();
        previewer.setMinWidth(200);
        previewer.setMaxWidth(200);
        previewer.setStyle("-fx-background-color:rgb(233, 228, 228);");
        contentPane.setRight(previewer);
        previewFunction(previewer);

        // THEME

        App.themeProperty = new SimpleBooleanProperty();
        App.themeProperty.addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                String theme = App.theme;
                themes themes = new themes(theme);
                sideMenu.setStyle("-fx-background-color:" + themes.middleColouring());
                titlePane.setStyle("-fx-background-color:" + themes.mainColouring());
                menu.setStyle("-fx-background-color:" + themes.mainColouring());
                settingPane.setStyle("-fx-background-color:" + themes.mainColouring());
                header.setStyle("-fx-background-color:" + themes.topColouring());
            }

        });

        scene = new Scene(stackPane, 1500, 800);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Team Work Hub");
        stage.getIcons().add(new Image("files/icons/group.png"));
        stage.show();
    }

    public void homeFunction(StackPane home) {
        BorderPane borderPane = new BorderPane();
        home.getChildren().add(borderPane);

        StackPane stackPane = new StackPane();
        borderPane.setTop(stackPane);

        VBox homeVBox = new VBox();
        stackPane.getChildren().add(homeVBox);

        FlowPane homeFlowPane = new FlowPane();
        homeFlowPane.setAlignment(Pos.CENTER);
        homeFlowPane.setPadding(new Insets(25, 0, 20, 0));
        homeVBox.getChildren().add(homeFlowPane);

        Text homeText = new Text("Team Work Hub");
        homeText.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_BOLD, 45));
        homeText.setFill(Color.DODGERBLUE);
        homeFlowPane.getChildren().add(homeText);

        StackPane recentPane = new StackPane();
        recentPane.setPadding(new Insets(10));
        homeVBox.getChildren().add(recentPane);

        FlowPane recentFlowPane = new FlowPane();
        recentFlowPane.setPadding(new Insets(10, 0, 10, 0));
        recentFlowPane.setStyle(
                "-fx-border-width:0 0 2 0; -fx-border-style:solid; -fx-border-color:rgba(177, 174, 174, 0.733);");
        recentPane.getChildren().add(recentFlowPane);

        Text recentText = new Text("Recent Teams");
        recentText.setFont(Font.font("Arial Rounded MT Bold", 18));
        recentText.setFill(Color.GRAY);
        recentFlowPane.getChildren().add(recentText);

        // ALL RECENT

        StackPane allRecentPane = new StackPane();
        borderPane.setCenter(allRecentPane);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.getStyleClass().add("task-scroll");
        allRecentPane.getChildren().add(scrollPane);

        StackPane pane = new StackPane();
        scrollPane.setContent(pane);

        toggleGroup = new ToggleGroup();

        recentBox = new VBox();
        recentBox.setPadding(new Insets(0, 10, 50, 10));
        pane.getChildren().add(recentBox);

        FlowPane notFound = new FlowPane();
        notFound.setPadding(new Insets(5, 10, 0, 40));
        recentBox.getChildren().add(notFound);

        FlowPane createPane = new FlowPane();
        createPane.setPadding(new Insets(5, 0, 10, 40));
        recentBox.getChildren().add(createPane);

        if (namesMap.isEmpty() != true) {
            for (Map.Entry<String, RadioButton> entry : homeMap.entrySet()) {

                entry.getValue().setPadding(new Insets(2, 10, 2, 5));
                entry.getValue().setTextFill(Color.rgb(51, 51, 51));
                entry.getValue().setPrefWidth(2000);
                entry.getValue().setCursor(Cursor.HAND);
                entry.getValue().setToggleGroup(toggleGroup);
                entry.getValue().getStyleClass().remove("radio-button");
                entry.getValue().getStyleClass().add("teamRadio");
                entry.getValue().setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        for (Map.Entry<String, Tab> entry2 : teamTabs.entrySet()) {
                            if (entry.getKey().equals(entry2.getKey())) {
                                contentTabPane.getSelectionModel().select(entry2.getValue());
                                selectText.setText(entry.getValue().getText());
                                App.selectedTeam = entry.getKey();
                            }
                        }
                    }

                });
                recentBox.getChildren().add(entry.getValue());
            }
            recentBox.getChildren().remove(notFound);
        }

        Text notText = new Text("No Recent Teams Found");
        notText.setFont(Font.font("Arial Rounded MT Bold", 16));
        notText.setFill(Color.GRAY);
        notFound.getChildren().add(notText);

        Text createText = new Text("Create Team");
        createText.setFont(Font.font("Arial Rounded MT Bold", 15));
        createText.setFill(Color.DEEPSKYBLUE);
        createText.setCursor(Cursor.HAND);
        createText.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                createTeams();
            }

        });
        createPane.getChildren().add(createText);

    }

    public void previewFunction(StackPane previewer) {

        BorderPane previewPane = new BorderPane();
        previewer.getChildren().add(previewPane);

        // Task

        StackPane taskPane = new StackPane();
        taskPane.setPadding(new Insets(5));
        previewPane.setTop(taskPane);

        FlowPane taskFlowPane = new FlowPane();
        taskFlowPane.setStyle(
                "-fx-border-width: 0 0 2 0; -fx-border-color: rgba(177, 174, 174, 0.733); -fx-border-style:solid;");
        taskFlowPane.setPadding(new Insets(10, 0, 10, 0));
        taskPane.getChildren().add(taskFlowPane);

        Text taskText = new Text("Latest Tasks");
        taskText.setFont(Font.font("Arial Rounded MT Bold", 17));
        taskText.setFill(Color.GRAY);
        taskFlowPane.getChildren().add(taskText);

        // connection

        StackPane connPane = new StackPane();
        connPane.setPadding(new Insets(15, 10, 20, 10));
        connPane.setStyle("-fx-background-color:#555;");
        previewPane.setBottom(connPane);

        VBox connBox = new VBox();
        connPane.getChildren().add(connBox);

        FlowPane connectFlowPane = new FlowPane();
        connectFlowPane.setAlignment(Pos.CENTER);
        connBox.getChildren().add(connectFlowPane);

        Text connText = new Text("CONNECTION");
        connText.setFont(Font.font("Arial Rounded MT Bold", 17));
        connText.setFill(Color.DARKGRAY);
        connectFlowPane.getChildren().add(connText);

        FlowPane conditionPane = new FlowPane();
        conditionPane.setAlignment(Pos.CENTER);
        conditionPane.setPadding(new Insets(7, 0, 0, 0));
        connBox.getChildren().add(conditionPane);

        App.connectionText = new Text("Offline");
        App.connectionText.setFont(Font.font("Arial Rounded MT Bold", 15));
        App.connectionText.setFill(Color.GRAY);
        conditionPane.getChildren().add(App.connectionText);

    }

    public void createTeams() {

        StackPane mainPane = new StackPane();
        mainPane.setPadding(new Insets(10));
        mainPane.setStyle("-fx-background-color:transparent;");

        VBox mainBox = new VBox();
        mainBox.setEffect(new DropShadow(5, Color.BLACK));
        mainBox.setStyle("-fx-background-color:white; -fx-border-radius:5; -fx-background-radius:5;");
        mainBox.setPadding(new Insets(10, 20, 0, 20));
        mainPane.getChildren().add(mainBox);

        // Close

        FlowPane cancelCreate = new FlowPane();
        cancelCreate.setAlignment(Pos.CENTER_RIGHT);
        cancelCreate.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                xSet = mouseEvent.getSceneX();
                ySet = mouseEvent.getSceneY();
            }

        });
        cancelCreate.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                createStage.setX(mouseEvent.getScreenX() - xSet);
                createStage.setY(mouseEvent.getScreenY() - ySet);
            }

        });
        mainBox.getChildren().add(cancelCreate);

        ImageView cancelView = new ImageView(new Image("files/icons/close.png"));
        cancelView.setFitHeight(15);
        cancelView.setFitWidth(15);
        cancelView.setCursor(Cursor.HAND);
        cancelView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                createStage.close();
            }

        });
        cancelCreate.getChildren().add(cancelView);

        // Title

        StackPane createFlowPane = new StackPane();
        createFlowPane.setPadding(new Insets(0, 20, 0, 20));
        mainBox.getChildren().add(createFlowPane);

        FlowPane createPane = new FlowPane();
        createPane.setAlignment(Pos.CENTER);
        createPane.setPadding(new Insets(0, 0, 30, 0));
        createPane.setStyle(
                "-fx-border-width: 0 0 2 0; -fx-border-style:solid; -fx-border-color:rgba(177, 174, 174, 0.633);");
        createFlowPane.getChildren().add(createPane);

        Text creaText = new Text("Creating Team");
        creaText.setFont(Font.font("Arial Rounded MT Bold", 24));
        creaText.setFill(Color.DODGERBLUE);
        createPane.getChildren().add(creaText);

        // Title Inputs

        HBox titleBox = new HBox();
        titleBox.setPadding(new Insets(30, 20, 10, 20));
        titleBox.setAlignment(Pos.CENTER_LEFT);
        mainBox.getChildren().add(titleBox);

        FlowPane titlePane = new FlowPane();
        titlePane.setPadding(new Insets(10, 0, 0, 0));
        titleBox.getChildren().add(titlePane);

        Text titleText = new Text("Team Title");
        titleText.setFont(Font.font("Arial Rounded MT Bold", 15));
        titleText.setFill(Color.GRAY);
        titlePane.getChildren().add(titleText);

        FlowPane titleFiedPane = new FlowPane();
        titleBox.getChildren().add(titleFiedPane);

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        titleField.setPrefWidth(400);
        titleField.setPrefHeight(30);
        titleField.getStyleClass().add("create-field");
        titleField.setStyle(
                "-fx-border-width: 2;-fx-border-color: grey; -fx-border-radius: 5; -fx-focus-color:transparent; -fx-background-radius: 5");
        titleFiedPane.getChildren().add(titleField);

        // Purpose Input

        HBox purposeBox = new HBox();
        purposeBox.setPadding(new Insets(10, 20, 10, 20));
        purposeBox.setAlignment(Pos.CENTER_LEFT);
        mainBox.getChildren().add(purposeBox);

        FlowPane purposePane = new FlowPane();
        purposePane.setPadding(new Insets(10, 0, 0, 0));
        purposeBox.getChildren().add(purposePane);

        Text purposeText = new Text("Team Purpose");
        purposeText.setFont(Font.font("Arial Rounded MT Bold", 15));
        purposeText.setFill(Color.GRAY);
        purposePane.getChildren().add(purposeText);

        FlowPane purposePane2 = new FlowPane();
        purposeBox.getChildren().add(purposePane2);

        TextField purposField = new TextField();
        purposField.setPromptText("Purpose");
        purposField.setPrefWidth(400);
        purposField.setPrefHeight(30);
        purposField.getStyleClass().add("create-field");
        purposField.setStyle(
                "-fx-border-width: 2;-fx-border-color: grey; -fx-border-radius: 5; -fx-focus-color:transparent; -fx-background-radius: 5");
        purposePane2.getChildren().add(purposField);

        // Type Input

        HBox typeBox = new HBox();
        typeBox.setPadding(new Insets(10, 20, 10, 20));
        typeBox.setAlignment(Pos.CENTER_LEFT);
        mainBox.getChildren().add(typeBox);

        FlowPane typePane = new FlowPane();
        typePane.setPadding(new Insets(10, 0, 0, 0));
        typeBox.getChildren().add(typePane);

        Text typeText = new Text("Team Purpose");
        typeText.setFont(Font.font("Arial Rounded MT Bold", 15));
        typeText.setFill(Color.GRAY);
        typePane.getChildren().add(typeText);

        FlowPane typePane2 = new FlowPane();
        typeBox.getChildren().add(typePane2);

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.setPrefWidth(400);
        choiceBox.setStyle(
                "-fx-border-width: 2;-fx-border-color: grey; -fx-border-radius: 5; -fx-focus-color:transparent; -fx-background-radius: 5");
        choiceBox.getItems().addAll("Work", "Office", "School", "Business", "Family", "Friends", "Others");
        typePane2.getChildren().add(choiceBox);

        // Comment Input

        HBox commentBox = new HBox();
        commentBox.setPadding(new Insets(10, 20, 10, 20));
        commentBox.setAlignment(Pos.CENTER_LEFT);
        mainBox.getChildren().add(commentBox);

        FlowPane commentPane = new FlowPane();
        commentPane.setPadding(new Insets(10, 0, 0, 0));
        commentBox.getChildren().add(commentPane);

        Text commentText = new Text("Comment");
        commentText.setFont(Font.font("Arial Rounded MT Bold", 15));
        commentText.setFill(Color.GRAY);
        commentPane.getChildren().add(commentText);

        FlowPane commentPane2 = new FlowPane();
        commentBox.getChildren().add(commentPane2);

        TextArea commentArea = new TextArea();
        commentArea.setPromptText("Team Comment");
        commentArea.setPrefWidth(400);
        commentArea.setPrefHeight(80);
        commentPane2.getChildren().add(commentArea);

        // Submit Button

        FlowPane submitPane = new FlowPane();
        submitPane.setAlignment(Pos.CENTER);
        submitPane.setPadding(new Insets(30, 0, 0, 0));
        mainBox.getChildren().add(submitPane);

        Button createButton = new Button("Create");
        createButton.setPrefWidth(300);
        createButton.setStyle("-fx-background-color:dodgerblue;");
        createButton.getStyleClass().add("create-button");
        createButton.setCursor(Cursor.HAND);
        createButton.setTextFill(Color.WHITE);
        createButton.requestFocus();
        createButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String teamName = titleField.getText();
                String teamPurpose = purposField.getText();
                String teamType = choiceBox.getValue();
                String teamComment = commentArea.getText();
                String message = "Name Field must be Filled";

                if (!teamName.equals("")) {

                    boolean check = false;

                    while (check == false) {
                        teamId = randomUUID();
                        check = true;
                        try {
                            String query = "SELECT id FROM teams WHERE id = '" + teamId + "'";
                            result = output.executeQuery(query);
                            while (result.next()) {
                                check = false;
                            }
                        } catch (Exception e) {
                            System.out.print(e);
                        }
                    }

                    int a = 0;

                    try {
                        String query = "INSERT INTO `teams` (`id`,`name`,`team_owner`,`type`,`purpose`,`comment`) VALUES ('"
                                + teamId + "','" + teamName + "','user_1','" + teamType + "','" + teamPurpose + "','"
                                + teamComment + "')";
                        output.executeUpdate(query);
                        a++;
                    } catch (Exception e) {
                        System.out.print(e);
                        String errorString = "Failed to create Team, Try Again Later";
                        popupError(errorString);
                    }
                    if (a != 0) {
                        createStage.close();

                        teamTabMap.put(teamId, "TeamTab_" + teamTotal);
                        Tab tab = new Tab();
                        tab.setText(teamId);
                        new teams(tab, teamId);
                        contentTabPane.getTabs().add(tab);
                        contentTabPane.getSelectionModel().select(tab);

                        RadioButton radioButton = new RadioButton(teamName);
                        teamButtonFunction(radioButton);
                        radioButton.setOnAction(new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent actionEvent) {
                                for (Map.Entry<String, Tab> entries : teamTabs.entrySet()) {
                                    if (entries.getKey().equals(teamId)) {
                                        contentTabPane.getSelectionModel().select(tab);
                                        selectText.setText(teamName);
                                        App.selectedTeam = entries.getKey();
                                    }
                                }
                            }

                        });
                        teamsBox.getChildren().add(radioButton);

                        RadioButton homeRadioButton = new RadioButton(teamName);
                        homeRadioButton.setPadding(new Insets(2, 10, 2, 5));
                        homeRadioButton.setTextFill(Color.rgb(51, 51, 51));
                        homeRadioButton.setPrefWidth(2000);
                        homeRadioButton.setCursor(Cursor.HAND);
                        homeRadioButton.setToggleGroup(toggleGroup);
                        homeRadioButton.getStyleClass().remove("radio-button");
                        homeRadioButton.getStyleClass().add("teamRadio");
                        homeRadioButton.setOnAction(new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent actionEvent) {
                                for (Map.Entry<String, Tab> entries : teamTabs.entrySet()) {
                                    if (entries.getKey().equals(teamId)) {
                                        contentTabPane.getSelectionModel().select(tab);
                                        selectText.setText(teamName);
                                        App.selectedTeam = entries.getKey();
                                    }
                                }
                            }

                        });
                        recentBox.getChildren().add(homeRadioButton);

                        radioButton.selectedProperty().bindBidirectional(homeRadioButton.selectedProperty());
                        selectText.setText(teamName);

                        teamTabs.put(teamId, tab);
                        radioMap.put(teamId, radioButton);
                        homeMap.put(teamId, homeRadioButton);
                        teamTotal++;
                    }
                } else {
                    popupError(message);
                }
            }

        });
        submitPane.getChildren().add(createButton);

        Scene createScene = new Scene(mainPane, 750, 500);
        createScene.setFill(Color.TRANSPARENT);

        createStage = new Stage();
        createStage.setTitle("Team Work - Create Team");
        createStage.getIcons().add(new Image("files/icons/group.png"));
        createStage.setScene(createScene);
        createStage.initStyle(StageStyle.TRANSPARENT);
        createStage.initModality(Modality.APPLICATION_MODAL);
        createStage.showAndWait();

    }

    public void teamButtonFunction(RadioButton radioButton) {
        radioButton.setPadding(new Insets(4, 10, 4, 10));
        radioButton.setTextFill(Color.WHITE);
        radioButton.setPrefWidth(2000);
        radioButton.setCursor(Cursor.HAND);
        radioButton.setToggleGroup(teamGroup);
        radioButton.getStyleClass().remove("radio-button");
        radioButton.getStyleClass().add("teamsRadio");
    }

    public void popupError(String message) {

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

        Scene popupsScene = new Scene(mainPane, 600, 300);
        popupsScene.setFill(Color.TRANSPARENT);

        popupStage = new Stage();
        popupStage.setTitle("Team Work Hub - Attention");
        popupStage.getIcons().add(new Image("files/icons/group.png"));
        popupStage.setScene(popupsScene);
        popupStage.initStyle(StageStyle.TRANSPARENT);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.showAndWait();
    }

    public String randomUUID() {
        UUID uuid = UUID.randomUUID();
        long longId = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        String createdId = Long.toString(longId, Character.MAX_RADIX);
        return createdId;
    }

    public String getString(String indexString, String tableString, String todoId) {
        Statement statement;
        ResultSet resultSet;
        String getString = "";
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

    public void buttonFunction(RadioButton radioButton) {
        radioButton.setPadding(new Insets(3, 0, 3, 0));
        radioButton.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
        radioButton.setTextFill(Color.WHITE);
        radioButton.setPrefWidth(500);
        radioButton.setCursor(Cursor.HAND);
        radioButton.setToggleGroup(mainGroup);
        radioButton.getStyleClass().remove("radio-button");
        radioButton.getStyleClass().add("mainButtons");
    }
}
