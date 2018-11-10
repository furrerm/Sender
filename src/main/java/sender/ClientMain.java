package sender;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("starting");
        Parent root = null;
        System.out.println(getClass().getResource("../client.fxml").getPath());

        try {
            root = FXMLLoader.load(getClass().getResource("../client.fxml"));
            //root = new FXMLLoader(getClass().getResource("Client.fxml")).load();
        } catch (Exception e){
            e.printStackTrace();
        }
        primaryStage.setTitle("FXML Welcome");
        primaryStage.setScene(new Scene(root, 800, 800));
        primaryStage.show();


    }
}
