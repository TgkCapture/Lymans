import java.util.Map;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class App extends Application {

    public static String id = "", username = "", theme = "";
    public static Map<String, StackPane> taskMap = new TreeMap<String, StackPane>();
    public static BooleanProperty teamProperty, themeProperty, deleteProperty;
    public static BooleanProperty taskProperty = new SimpleBooleanProperty();
    public static BooleanProperty taskDeleteProperty = new SimpleBooleanProperty();
    public static String selectedTeam = "";
    public static Text connectionText;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        new login();
    }
}