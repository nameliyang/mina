package com.ly.mina.quickstart;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;

public class GUIServer extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = getClass().getResource
                ("/my.fxml");
        Pane myPane = (Pane) FXMLLoader.load(url);
        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
