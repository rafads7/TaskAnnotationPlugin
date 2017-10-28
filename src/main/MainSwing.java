package main;

import controllers.MainGUIController;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;

import javax.swing.*;
import java.io.IOException;

public class MainSwing {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initAndShowGUI();
            }
        });
    }

    //GUI Swing initialization
    private static void initAndShowGUI() {
        // This method is invoked on the EDT thread
        JFrame frame = new JFrame("Task Annotation for Usability Logging");
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setSize(1000, 700);
        frame.setVisible(true);

        Platform.runLater( new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel, frame);
            }
        });
    }

    //GUI JavaFX load into Swing frame
    private static void initFX(JFXPanel fxPanel, JFrame frame) {
        // This method is invoked on the JavaFX thread
        Scene scene = createScene(frame);
        fxPanel.setScene(scene);
    }

    //GUI JavaFX Scene creation
    private static Scene createScene(JFrame frame) {

        SplitPane rootLayout = null;

        FXMLLoader loader = new FXMLLoader();
        loader.setClassLoader(MainSwing.class.getClassLoader());
        loader.setLocation(MainSwing.class.getResource( "/fxml/MainGUIFXML.fxml" ));
        try {
            rootLayout = loader.load();
            MainGUIController sc = loader.<MainGUIController>getController();
            sc.sendMainAppFrame(frame);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Scene( rootLayout, 1000, 700 );
    }
}
