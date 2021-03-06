package main;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainForm.fxml")));
        stage.setTitle("Main Form");
        stage.setScene(new Scene(root, 1000, 500));
        stage.setResizable(false);
        stage.show();
    }


    public static void main(String[] args){
        launch(args);
    }
}
