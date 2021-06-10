package com.iceybones.capstone.app;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationManager extends Application {
  public static Stage primaryStage;

  @Override
  public void start(Stage primaryStage) throws Exception {
    ApplicationManager.primaryStage = primaryStage;
    Parent root = FXMLLoader.load(
          Objects.requireNonNull(getClass().getClassLoader().getResource("views/login.fxml")));
    primaryStage.setTitle("login");
    primaryStage.setScene(new Scene(root, 360, 200));
    primaryStage.show();
    primaryStage.centerOnScreen();
    primaryStage.setResizable(false);
    primaryStage.setOnCloseRequest((a) -> System.exit(0));
  }

  public static void login() {
    ExecutorService service = Executors.newSingleThreadExecutor();
    service.submit(() -> {
      Parent root = null;
      try {
        root = FXMLLoader.load(
            Objects.requireNonNull(ApplicationManager.class.getClassLoader().getResource("views/main.fxml")));
      } catch (IOException e) {
        e.printStackTrace();
      }
      Parent finalRoot = root;
      Platform.runLater(() -> {
        primaryStage.setTitle("iceybones Dashboard");
        primaryStage.setScene(new Scene(finalRoot, 1024, 768));
        primaryStage.show();
        primaryStage.centerOnScreen();
        primaryStage.setResizable(true);
        primaryStage.setOnCloseRequest((a) -> System.exit(0));
      });
    });
  }

  public static void main(String[] args) {
    launch(args);
  }
}
