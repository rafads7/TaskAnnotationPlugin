package utils;

import models.Task;
import models.Graph;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.Optional;

public class Utils {

    public static void showWarningMessage(String mssg){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setContentText(mssg);
        alert.showAndWait();
    }
    public static boolean showWarningCheck(String title, String mssg){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title + " confirmation.");
        if(title.equals("Delete")){
            alert.setContentText("Are you sure you want to delete Task " + mssg + " and all its subtasks?");
        }else{
            alert.setContentText("You are trying to create a task in a position where there already exists a task. " +
                    "If you create it, the positions of the existing one and the later tasks of the map will be updated. Do you want to continue?");
        }

        ButtonType confirm = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(confirm, cancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == confirm){
            return true;
        }else{
            return false;
        }
    }

    public static void copyCode(String type, Stage window, JFrame mainAppWindow){

        String toCopy = "";

        if(type.equals( "tracker" )){
            toCopy = getTrackerCode();
        }else if(type.equals( "dimensions" )){
            toCopy = getDimensionsCode();
        }else if(type.equals( "events" )){
            toCopy = getEventsCode();
        }else if(type.equals( "screens" )){
            toCopy = getScreensCode();
        }else if(type.equals( "userID" )){
            toCopy = getUserIDCode();
        }else if(type.equals( "userTiming" )){
            toCopy = getUserTimingCode();
        }

        StringSelection stringSelection = new StringSelection(toCopy);
        java.awt.datatransfer.Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
        window.setIconified( true );
        mainAppWindow.setState(Frame.ICONIFIED);
    }

    private static String getTrackerCode(){
        return  "GoogleAnalytics analytics = GoogleAnalytics.getInstance(getContext());\n" +
                "Tracker mTracker =  analytics.newTracker(R.xml.analytics_global_tracker);\n" +
                "// You should type the context of your Activity or Fragment to create the analytics instance.";
    }
    private static String getDimensionsCode(){
        return "String label, category, action, dimensionValue1; // Label, category and action are not mandatory.\n\n" +
                "mTracker.send(new HitBuilders.EventBuilder()\n" +
                ".setCategory(category)\n" +
                ".setAction(action)\n" +
                ".setLabel(label)\n" +
                ".setCustomDimension(1, dimensionValue1)" +
                ".build());\n" +
                "//If you want to add more than one dimension, call the same method (setCustomDimension(int index, String dimensionValue)) concatenated with\n" +
                "// the last setCostumDimension(...) call i.e.: .setDimension(1, dimensionValue1).setDimension(2, dimensionVlaue2)....build();";
    }
    private static String getEventsCode() {
        return  "//Inject the following code inside the event to send.\n" +
                "String label, category, action;\n\n" +
                "mTracker.send(new HitBuilders.EventBuilder()\n" +
                ".setCategory(category)\n" +
                ".setAction(action)\n" +
                ".setLabel(label)\n" +
                ".build());\n";
    }
    private static String getScreensCode(){
        return  "mTracker.setScreenName(getScreenName()); //Set the name of the screen.\n" +
                "mTracker.send(new HitBuilders.ScreenViewBuilder()\n" +
                ".build());\n";
    }
    private static String getUserIDCode(){
        return  "mTracker.set(\"&uid\", user.getId());\n" +
                "mTracker.send(new HitBuilders.EventBuilder()\n" +
                ".setCategory(category)\n" +
                ".setAction(action)\n" +
                ".build());\n";
    }
    private static String getUserTimingCode(){
        return  "String label, category, action; //Not mandatory\n" +
                "long timing;\n\n" +
                "mTracker.send(new HitBuilders.EventBuilder()\n" +
                ".setCategory(category)\n" +
                ".setAction(action)\n" +
                ".setLabel(label)\n" +
                ".setValue(timing)\n" +
                ".build());\n";
    }

    private void printGraph(Graph graph){
        for(Task t: graph.getTasks().values()){
            System.out.println(t.toString());
        }
    }

}
