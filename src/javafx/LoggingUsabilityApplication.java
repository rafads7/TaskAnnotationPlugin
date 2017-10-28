package javafx;

import models.Task;
import controllers.LoggingUsabilityController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class LoggingUsabilityApplication extends Application {

    private Task task;
    private AnchorPane rootLayout;
    private JFrame GUIWindow;

    public static void main(String[] args) {
        launch( args );
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Logging Usability");
        FXMLLoader loader = new FXMLLoader();
        loader.setClassLoader(getClass().getClassLoader());
        loader.setLocation(getClass().getResource( "/fxml/LoggingUsabilityFXML.fxml" ));

        try {
            rootLayout = loader.load();
            LoggingUsabilityController sc = loader.<LoggingUsabilityController>getController();
            sc.setTaskAndStages(task, stage, GUIWindow);
            sc.injectCodeManagement();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(rootLayout, 700, 630));
        stage.setResizable( false );
        stage.show();
    }
    public void setTaskAndStage(Task task, JFrame mainWindow) {
        this.task = task;
        this.GUIWindow = mainWindow;
    }
}
