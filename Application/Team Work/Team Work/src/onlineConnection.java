import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.sql.*;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class onlineConnection {

    public Connection conn;
    public Statement output;
    public ResultSet result;

    public Connection connection;
    public Statement statement;
    public ResultSet resultSet;

    public onlineConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/team work hub", "root", "mydatabase");
            output = conn.createStatement();
        } catch (Exception e) {
            System.out.print(e);
        }
        Timeline statusline = new Timeline(new KeyFrame(Duration.millis(5000), load -> connectionStatus()));
        statusline.setCycleCount(Animation.INDEFINITE);
        statusline.play();
    }

    public void updateDatabases() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://freedb.tech:3306/freedbtech_Teamworkhub",
                    "freedbtech_Asapgang", "mydatabase1234");
            statement = connection.createStatement();
        } catch (Exception e) {
            System.out.print(e);
        }

        // NOTIFICATION
        try {
            String query = "SELECT * FROM notification WHERE update = '1'";
            result = output.executeQuery(query);
            while (result.next()) {
                Boolean checkBoolean = false;
                String idString = result.getString("id");
                String onlineQuery = "SELECT * FROM notification WHERE id = '" + idString + "' ";
                resultSet = statement.executeQuery(onlineQuery);
                while (resultSet.next()) {
                    checkBoolean = true;
                }
                if (checkBoolean != false) {
                    idString = getOnlineIdString("notification");
                    Statement updateStatement = conn.createStatement();
                    updateStatement.executeUpdate("UPDATE notification SET update = '2' WHERE id = '" + idString + "'");
                }
                onlineQuery = "INSERT INTO notification (`id`,`team_id`,`task_id`,`user_id`,`notifyId`,`title`,`content`,`choice`) VALUES ('"
                        + idString + "','" + result.getString("team_id") + "','" + result.getString("task_id") + "','"
                        + result.getString("user_id") + "','" + result.getString("notifyId") + "','"
                        + result.getString("title") + "','" + result.getString("content") + "','"
                        + result.getString("choice") + "')";
                statement.executeUpdate(onlineQuery);
            }

            Set<String> idSet = new TreeSet<String>();
            query = "SELECT id FROM notification";
            result = output.executeQuery(query);
            while (result.next()) {
                idSet.add(result.getString("id"));
            }

            query = "SELECT * FROM notification WHERE user_id = '" + App.id + "' OR notifyId = '" + App.id + "'";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Boolean checkBoolean = false;
                for (String eachString : idSet) {
                    if (eachString.equals(resultSet.getString("id"))) {
                        checkBoolean = true;
                    }
                }
                if (checkBoolean != true) {
                    String offlineQuery = "INSERT INTO notification (`id`,`team_id`,`task_id`,`user_id`,`notifyId`,`title`,`content`,`choice`,`update`) VALUES ('"
                            + resultSet.getString("id") + "','" + resultSet.getString("team_id") + "','"
                            + resultSet.getString("task_id") + "','" + resultSet.getString("user_id") + "','"
                            + resultSet.getString("notifyId") + "','" + resultSet.getString("title") + "','"
                            + resultSet.getString("content") + "','" + resultSet.getString("choice") + "','0')";
                    output.executeUpdate(offlineQuery);
                }
            }
        } catch (Exception e) {
            System.out.print(e);
        }

        // TASK MESSAGE

        try {
            String query = "SELECT * FROM task_messages WHERE update = '1'";
            result = output.executeQuery(query);
            while (result.next()) {
                Boolean checkBoolean = false;
                String idString = result.getString("id");
                String onlineQuery = "SELECT * FROM task_messages WHERE id = '" + idString + "' ";
                resultSet = statement.executeQuery(onlineQuery);
                while (resultSet.next()) {
                    checkBoolean = true;
                }
                if (checkBoolean != false) {
                    idString = getOnlineIdString("task_messages");
                    Statement updateStatement = conn.createStatement();
                    updateStatement
                            .executeUpdate("UPDATE task_messages SET update = '0' WHERE id = '" + idString + "'");
                }
                onlineQuery = "INSERT INTO task_messages (`id`,`task_id`,`team_id`,`user_id`,`message`) VALUES ('"
                        + idString + "','" + result.getString("task_id") + "','" + result.getString("team_id") + "','"
                        + result.getString("user_id") + "','" + result.getString("message") + "')";
                statement.executeUpdate(onlineQuery);
            }

            Set<String> idSet = new TreeSet<String>();
            query = "SELECT id FROM task_messages";
            result = output.executeQuery(query);
            while (result.next()) {
                idSet.add(result.getString("id"));
            }

            int a = 0;
            String queryCondition = "";
            query = "SELECT id FROM teams WHERE id_owner = '" + App.id + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                if (a != 0) {
                    queryCondition = queryCondition + "OR team_id = '" + result.getString("id") + "'";
                } else {
                    queryCondition = "team_id = '" + result.getString("id") + "'";
                }
            }
            query = "SELECT team_id FROM team_people WHERE user_id = '" + App.id + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                if (!queryCondition.equals("")) {
                    queryCondition = "team_id = '" + result.getString("id") + "'";
                } else {
                    queryCondition = queryCondition + "OR team_id = '" + result.getString("id") + "'";
                }
            }

            query = "SELECT * FROM task_messages WHERE " + queryCondition;
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Boolean checkBoolean = false;
                for (String eachString : idSet) {
                    if (eachString.equals(resultSet.getString("id"))) {
                        checkBoolean = true;
                    }
                }
                if (checkBoolean != true) {
                    String offlineQuery = "INSERT INTO task_messages (`id`,`task_id`,`team_id`,`user_id`,`message`,`update`) VALUES ('"
                            + resultSet.getString("id") + "','" + resultSet.getString("task_id") + "','"
                            + resultSet.getString("team_id") + "','" + resultSet.getString("user_id") + "','"
                            + resultSet.getString("message") + "','0')";
                    output.executeUpdate(offlineQuery);
                }
            }
        } catch (Exception e) {
            System.out.print(e);
        }

        // TASKS

        try {
            String query = "SELECT * FROM tasks WHERE update = '1'";
            result = output.executeQuery(query);
            while (result.next()) {
                Boolean checkBoolean = false;
                String idString = result.getString("id");
                String onlineQuery = "SELECT * FROM tasks WHERE id = '" + idString + "' ";
                resultSet = statement.executeQuery(onlineQuery);
                while (resultSet.next()) {
                    checkBoolean = true;
                }
                if (checkBoolean != false) {
                    idString = getOnlineIdString("tasks");
                    Statement updateStatement = conn.createStatement();
                    updateStatement.executeUpdate("UPDATE tasks SET update = '0' WHERE id = '" + idString + "'");
                }
                onlineQuery = "INSERT INTO tasks (`id`,`team_id`,`id_owner`,`name`,`content`,`type`) VALUES ('"
                        + idString + "','" + result.getString("team_id") + "','" + result.getString("id_owner") + "','"
                        + result.getString("name") + "','" + result.getString("content") + "', '"
                        + result.getString("type") + "')";
                ;
                statement.executeUpdate(onlineQuery);
            }

            Set<String> idSet = new TreeSet<String>();
            query = "SELECT task_id FROM team_tasks WHERE assigned_id = '" + App.id + "'";
            result = output.executeQuery(query);
            while (result.next()) {
                idSet.add(result.getString("task_id"));
            }

            query = "SELECT * FROM team_tasks WHERE assigned_id = '" + App.id + "'";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Boolean checkBoolean = false;
                for (String eachString : idSet) {
                    if (eachString.equals(resultSet.getString("task_id"))) {
                        checkBoolean = true;
                    }
                }
                if (checkBoolean != true) {
                    String offlineQuery = "INSERT INTO team_tasks (`id`,`task_id`,`team_id`,`assigned_id`) VALUES ('"
                            + resultSet.getString("id") + "','" + resultSet.getString("task_id") + "','"
                            + resultSet.getString("team_id") + "','" + resultSet.getString("assigned_id") + "'";
                    output.executeUpdate(offlineQuery);
                }
            }
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void connectionStatus() {
        try {
            URL url = new URL("https://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            connectionText(true);
        } catch (MalformedURLException e) {
            connectionText(false);
        } catch (IOException e) {
            connectionText(false);
        }
    }

    public void connectionText(Boolean condition) {
        if (condition != false) {
            App.connectionText.setText("Online");
            App.connectionText.setFill(Color.DARKCYAN);
        } else {
            App.connectionText.setText("Offline");
            App.connectionText.setFill(Color.GRAY);
        }
    }

    public String getOnlineIdString(String tableName) {
        boolean a = true;
        String tmp_id = "";
        Connection connections;
        Statement statements;
        ResultSet resultSets;
        while (a) {
            tmp_id = randomUUID();
            a = false;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connections = DriverManager.getConnection("jdbc:mysql://freedb.tech:3306/freedbtech_Teamworkhub",
                        "freedbtech_Asapgang", "mydatabase1234");
                statements = connections.createStatement();
                String query = "SELECT id FROM " + tableName + " WHERE id = '" + tmp_id + "'";
                resultSets = statements.executeQuery(query);
                while (resultSets.next()) {
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
