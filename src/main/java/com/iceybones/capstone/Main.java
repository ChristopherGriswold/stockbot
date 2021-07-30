package com.iceybones.capstone;

import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(
        Objects.requireNonNull(getClass().getClassLoader().getResource("main.fxml")));
    primaryStage.setTitle("iceybones Dashboard");
    primaryStage.setScene(new Scene(root, 1024, 768));
    primaryStage.show();
    primaryStage.setOnCloseRequest((a) -> System.exit(0));
  }


  public static void main(String[] args) {
    launch(args);
  }
}
