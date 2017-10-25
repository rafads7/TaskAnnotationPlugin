package controllers;

import beans.DiagramTask;
import com.sun.demo.jvmti.hprof.Tracker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.util.ResourceBundle;

public class LoggingUsabilityController implements Initializable{

    private DiagramTask task;
    private Stage window;
    private JFrame mainAppWindow;
    @FXML private Button saveButton;
    @FXML private TextArea usabilityTextArea;
    @FXML private TextField trackerReference, dimensionsReference, eventsReference, screensReference, userIDReference, userTimingReference;
    @FXML private Button tracker, dimensions, events, screens, userID, userTiming;

    //Initialization
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    public LoggingUsabilityController() {
    }

    //Task and Stage getter for proper task control
    public void setTaskAndStages(DiagramTask task, Stage window, JFrame mainAppWindow) {
        this.task = task;
        this.window = window;
        this.mainAppWindow = mainAppWindow;

        if(task.getUsabilityLoggingDescription() != null){
            usabilityTextArea.setText(task.getUsabilityLoggingDescription());
        }
        if(task.getReferences().get("Tracker") != null){
            trackerReference.setText(task.getReferences().get("Tracker"));
        }
        if(task.getReferences().get("Dimensions") != null){
            dimensionsReference.setText(task.getReferences().get("Dimensions"));
        }
        if(task.getReferences().get("Events") != null){
            eventsReference.setText(task.getReferences().get("Events"));
        }
        if(task.getReferences().get("Screens") != null){
            screensReference.setText(task.getReferences().get("Screens"));
        }
        if(task.getReferences().get("UserID") != null){
            userIDReference.setText(task.getReferences().get("UserID"));
        }
        if(task.getReferences().get("UserTiming") != null){
            userTimingReference.setText(task.getReferences().get("UserTiming"));
        }
    }

    //GAMA call code injection management
    public void saveButtonManagement(ActionEvent actionEvent) {
        saveButton.setOnMouseClicked( new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (usabilityTextArea.getText() != null){
                    task.setUsabilityLoggingDescription( usabilityTextArea.getText() );
                }
                if(trackerReference.getText() != null){
                    task.getReferences().put("Tracker", trackerReference.getText());
                }
                if(dimensionsReference.getText() != null){
                    task.getReferences().put("Dimensions", dimensionsReference.getText());
                }
                if(eventsReference.getText() != null){
                    task.getReferences().put("Events", eventsReference.getText());
                }
                if(screensReference.getText() != null){
                    task.getReferences().put("Screens", screensReference.getText());
                }
                if(userIDReference.getText() != null){
                    task.getReferences().put("UserID", userIDReference.getText());
                }
                if(userTimingReference.getText() != null){
                    task.getReferences().put("UserTiming", userTimingReference.getText());
                }

                window.close();
            }
        } );
    }
    public void injectCodeManagement() {

        tracker.setOnMouseClicked( event -> {
            utils.Utils.copyCode("tracker", window, mainAppWindow);
        } );
        dimensions.setOnMouseClicked( event -> {
            utils.Utils.copyCode("dimensions", window, mainAppWindow);
        } );
        screens.setOnMouseClicked( event -> {
            utils.Utils.copyCode("screens", window, mainAppWindow);
        } );
        events.setOnMouseClicked( event -> {
            utils.Utils.copyCode("events", window, mainAppWindow);
        } );
        userID.setOnMouseClicked( event -> {
            utils.Utils.copyCode("userID", window, mainAppWindow);
        } );
        userTiming.setOnMouseClicked( event -> {
            utils.Utils.copyCode("userTiming", window, mainAppWindow);
        } );
    }
}
