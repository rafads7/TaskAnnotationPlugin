package javafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class Help extends Application {

    public static void main(String[] args) {
        launch( args );
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Task Drawing");

        Label creationTitle = new Label("Task Creation");
        creationTitle.setFont( Font.font("Verdana", FontWeight.BOLD, 12));
        Label creation = new Label();
        creation.setWrapText( true );
        creation.setText( "- In order to create a task, you must indicate a task name and position. Optionally, you can also indicate a plan.\n" +
                "- The first level (root) of the tree must be always indicated by position 0.\n" +
                "- The nodes' position of the second level (children of root) must be indicated by natural numbers, starting from 1.\n" +
                "- The nodes' position of the following levels must be indicated by natural numbers separated by dots (.), i.e. 1.1 would be " +
                "the son of node 1, and so on.\n" +
                "- No position must begin or end with a dot. \n" );

        Label eliminationTitle = new Label("Task Elimination");
        eliminationTitle.setFont( Font.font("Verdana", FontWeight.BOLD, 12));
        Label elimination = new Label();
        elimination.setWrapText( true );
        elimination.setText( "- In order to delete a task, you must select the task you want to remove in the tree structure provided on the" +
                "right side of the plugin window and click on the button \"Delete\". Take into account that, depending on the task you delete, " +
                "other tasks will also be affected:\n" +
                "\t- Subtasks of the selected one will also be deleted.\n" +
                "\t- Tasks (and their correspondent subtasks) at the same level than the selected one will be moved to the left, in other words, " +
                "their position for that level will be decreased in one point. I.e.: Delete (Task 1.2) -> Change Position (Task 1.3 -> 1.2).\n");

        Label editionTitle = new Label("Task Edition");
        editionTitle.setFont( Font.font("Verdana", FontWeight.BOLD, 12));
        Label edition = new Label();
        edition.setWrapText( true );
        edition.setText( "- In order to modify a task, you must select the task you want to edit in the tree structure provided on the right side " +
                "of the plugin window and then change its name or plan. Position cannot be modified.\n" +
                "- If you want to modify the usability logging process description for a task, you must click on the item and later on the button \"Logging\".\n");

        Label exportAndImportTitle = new Label("Task Export and Import");
        exportAndImportTitle.setFont( Font.font("Verdana", FontWeight.BOLD, 12));
        Label expImp = new Label();
        expImp.setWrapText( true );
        expImp.setText( "- In order to export the HTA graph you are currently working with and all its tasks, you must click on the button \"Export\" " +
                "and select the destination folder.\n" +
                "- In order to import an existing HTA graph and all its tasks, you must click on the button \"Import\" and select the correspondent file. " +
                "The system only accepts HTA graphs previously performed and exported with the plugin.\n");

        Label loggingUsabilityTitle = new Label("Task Usability Logging");
        loggingUsabilityTitle.setFont( Font.font("Verdana", FontWeight.BOLD, 12));
        Label logging = new Label();
        logging.setWrapText( true );
        logging.setText("- In order to inject the usability logging code to your app, you must select the task you want to control in tree structure " +
                "provided on the right side of the plugin window and then click on the button \"Logging\". Then, you have a couple of options:\n" +
                "\t- You can describe the data you want to collect in the first text area appearing in the window.\n" +
                "\t- In order to get the code in charge of sending the logged data to GAMA server, you must click on the button \"Get code to inject\" " +
                "and manually paste it on your app code, wherever you want to make the call.\n" +
                "\t- The data you want to send to GAMA server must be inserted into a DimensionValue object..........\n");

        Label GAMAtitle = new Label("Project Requirements for Google Analytics");
        GAMAtitle.setFont( Font.font("Verdana", FontWeight.BOLD, 12));
        Label GAMA = new Label();
        GAMA.setWrapText( true );
        GAMA.setText("- In order to be able to use Google Analytics services, you must first set up your project, as it says its official website: " +
                "https://developers.google.com/analytics/devguides/collection/android/v4/ , following the sections \'Set up your project\' and" +
                "\'Create global_tracker.xml\'.");

        VBox vBox = new VBox(creationTitle, creation, eliminationTitle, elimination, editionTitle, edition,
                exportAndImportTitle, expImp, loggingUsabilityTitle, logging, GAMAtitle, GAMA);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setSpacing(10);

        ScrollPane scrollPane = new ScrollPane(vBox);

        stage.setScene(new Scene(scrollPane, 800, 600));
        stage.show();
    }
}
