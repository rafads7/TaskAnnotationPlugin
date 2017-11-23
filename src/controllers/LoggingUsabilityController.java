package controllers;

import models.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class LoggingUsabilityController implements Initializable{

    private Task task;
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
    public void setTaskAndStages(Task task, Stage window, JFrame mainAppWindow) {
        this.task = task;
        this.window = window;
        this.mainAppWindow = mainAppWindow;
//        this.mainAppWindow.setFocusable(false);
//        this.mainAppWindow.setEnabled(false);

        if(task.getLoggingElement().getDescription() != null){
            usabilityTextArea.setText(task.getLoggingElement().getDescription());
        }
        if(task.getLoggingElement().getReferences().get("Tracker") != null){
            trackerReference.setText(task.getLoggingElement().getReferences().get("Tracker"));
        }
        if(task.getLoggingElement().getReferences().get("Dimensions") != null){
            dimensionsReference.setText(task.getLoggingElement().getReferences().get("Dimensions"));
        }
        if(task.getLoggingElement().getReferences().get("Events") != null){
            eventsReference.setText(task.getLoggingElement().getReferences().get("Events"));
        }
        if(task.getLoggingElement().getReferences().get("Screens") != null){
            screensReference.setText(task.getLoggingElement().getReferences().get("Screens"));
        }
        if(task.getLoggingElement().getReferences().get("UserID") != null){
            userIDReference.setText(task.getLoggingElement().getReferences().get("UserID"));
        }
        if(task.getLoggingElement().getReferences().get("UserTiming") != null){
            userTimingReference.setText(task.getLoggingElement().getReferences().get("UserTiming"));
        }
    }

    //GAMA call code injection management
    public void saveButtonManagement(ActionEvent actionEvent) {
        saveButton.setOnMouseClicked( new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (usabilityTextArea.getText() != null){
                    task.setLoggingElementDescription( usabilityTextArea.getText() );
                }
                if(trackerReference.getText() != null){
                    task.setLoggingReference("Tracker", trackerReference.getText());
                }
                if(dimensionsReference.getText() != null){
                    task.setLoggingReference("Dimensions", dimensionsReference.getText());
                }
                if(eventsReference.getText() != null){
                    task.setLoggingReference("Events", eventsReference.getText());
                }
                if(screensReference.getText() != null){
                    task.setLoggingReference("Screens", screensReference.getText());
                }
                if(userIDReference.getText() != null){
                    task.setLoggingReference("UserID", userIDReference.getText());
                }
                if(userTimingReference.getText() != null){
                    task.setLoggingReference("UserTiming", userTimingReference.getText());
                }
                window.close();
//                mainAppWindow.setFocusable(true);
//                mainAppWindow.setEnabled(true);
//                mainAppWindow.toFront();
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
