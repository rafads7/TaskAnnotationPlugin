package controllers;

import models.Task;
import models.Graph;
import javafx.LoggingUsabilityApplication;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;
import org.apache.commons.lang.StringUtils;
import javafx.Help;
import swing.TaskBox;
import swing.TaskBoxNodeExtentProvider;
import swing.TaskBoxTreePane;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MainGUIController implements Initializable {

    @FXML private TextField taskName, position;
    @FXML private TextArea plan;
    public TreeView<Task> taskTreeView;
    public SwingNode swingNode;
    public Button delete;
    public Button confirm;
    public Button loggingUsability;

    private Graph graph = new Graph();
    private JFrame window;
    private TreeItem<Task> selectedItem;

    private final String NO_PARENT = "no_parent";
    private final String NO_PREVIOUS_SIBLING = "no_previous_sibling";
    private final String NO_ROOT = "no_root";
    private final String SAVE = "Save";

    //Task Creation, Edition and Elimination
    public void confirmButtonManagement(ActionEvent actionEvent) {
        if(correctInputData()){
            if (confirm.getText().equals(SAVE)) {
                editTask();
            } else {
                newTask();
            }
        }else{
            utils.Utils.showWarningMessage( "Error: 'Position' field must be filled with numbers. In order to indicate different levels, use dots (.): \"1.2.1.1\"" );
        }
    }
    private boolean correctInputData() {
        String pos = position.getText();
        if(StringUtils.isNumeric(pos)){
            return true;
        }else{
            for(int i=0; i<pos.length(); i++){
                if(i == 0 || i%2 == 0){
                    if(!StringUtils.isNumeric(String.valueOf(pos.charAt(i)))){
                        return false;
                    }
                }else{
                    if(!String.valueOf(pos.charAt(i)).equals(".")){
                        return false;
                    }
                }
            }
            return true;
        }
    }
    private void newTask() {
        if (taskName.getText().equals("") || taskName.getText() == null || position.getText().equals("") || position.getText() == null) {
            utils.Utils.showWarningMessage("Warning: Sorry, you must indicate a Task Name and Position in order to create or edit one.");
        } else {
            addTaskToDiagram();
        }
    }
    private void editTask() {
//        Task t = graph.getTaskByPosition(position.getText());
//        t.setName(taskName.getText());
//        t.setPlan(plan.getText());
//        t.setPosition(position.getText());
        graph.editTaskPosition(selectedItem.getValue(), taskName.getText(), plan.getText(), position.getText());
        delete.setDisable(true);
        loggingUsability.setDisable(true);
        confirm.setText("New");
//        position.setDisable(false);
        updateControlGUI();
    }
    private void deleteButtonManagement(TreeItem<Task> selectedItem) {
        delete.setDisable(false);
        delete.setOnAction( (ActionEvent event) -> {
            Task task = selectedItem.getValue();
            if (utils.Utils.showWarningCheck("Delete", task.toString())) {
                this.graph.deleteTask(task);
                delete.setDisable(true);
                loggingUsability.setDisable(true);
                confirm.setText("New");
//                position.setDisable(false);
                updateControlGUI();
            }
        });
    }
    private void loggingButtonManagement(TreeItem<Task> selectedItem) {
        loggingUsability.setDisable(false);
        loggingUsability.setOnAction( event -> {
            Task task = selectedItem.getValue();
            LoggingUsabilityApplication loggingApplication = new LoggingUsabilityApplication();
            loggingApplication.setTaskAndStage(task, window);
            loggingApplication.start(new Stage());
        } );

    }
    private void addTaskToDiagram() {
        String addingResult = this.graph.addTask(this.taskName.getText(), this.position.getText(), this.plan.getText());

        if (addingResult.equals(NO_PARENT)) {
            utils.Utils.showWarningMessage("Error: there is no possible parent existing for this position. First create the parent, and later add this one.");
        } else if (addingResult.equals(NO_PREVIOUS_SIBLING)) {
            utils.Utils.showWarningMessage("Error: The position you indicated requires a previous task under the same parent.");
        } else if (addingResult.equals(NO_ROOT)) {
            utils.Utils.showWarningMessage("Error: You should first create a root (position: 0).");
        } else {
            updateControlGUI();
        }
    }

    //GUI
    private void updateControlGUI() {
        graphDiagramManagement();
        updateTaskTreeView();
        taskName.clear();
        position.clear();
        plan.clear();
    }
    public void exportManagement(ActionEvent actionEvent) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose a directory:");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File dir = directoryChooser.showDialog(new Stage());

        try {
            FileOutputStream fileOut = new FileOutputStream( dir.getAbsolutePath() + "\\" + "task.ser" );
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(graph);
            out.close();
            fileOut.close();
        }catch(FileNotFoundException i) {
            i.printStackTrace();
        }catch(IOException i){
            i.printStackTrace();
        }

    }
    public void importManagement(ActionEvent actionEvent) {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        Graph importedGraph;
        try {
            FileInputStream fileIn = new FileInputStream(file.getAbsolutePath());
            ObjectInputStream in = new ObjectInputStream(fileIn);
            importedGraph = (Graph) in.readObject();

            if(importedGraph != null){
                this.graph = importedGraph;
                updateControlGUI();
            }
            in.close();
            fileIn.close();
        }catch(IOException i) {
            //i.printStackTrace();
            return;
        }catch(ClassNotFoundException c) {
            //c.printStackTrace();
            return;
        }catch (NullPointerException n){
            //n.printStackTrace();
            return;
        }

    }
    public void helpManagement(ActionEvent actionEvent) {
        new Help().start(new Stage());
    }

    //Graph Diagram Management
    private void graphDiagramManagement() {

        // setup the tree layout configuration
        double gapBetweenLevels = 50;
        double gapBetweenNodes = 10;
        DefaultConfiguration<TaskBox> configuration = new DefaultConfiguration<TaskBox>( gapBetweenLevels, gapBetweenNodes );

        // create the NodeExtentProvider for swing.TaskBox nodes
        TaskBoxNodeExtentProvider nodeExtentProvider = new TaskBoxNodeExtentProvider();

        //create the tree
        DefaultTreeForTreeLayout<TaskBox> tree = createTreeDiagram();

        if(tree != null){
            // create the layout
            TreeLayout<TaskBox> treeLayout = new TreeLayout<TaskBox>( tree, nodeExtentProvider, configuration );

            // Create a panel that draws the nodes and edges and show the panel
            TaskBoxTreePane panel = new TaskBoxTreePane( treeLayout );

            swingNode.setVisible( true );
            swingNode.setContent( panel );
            swingNode.setVisible( true );
//            SwingUtilities.invokeLater( new Runnable() {
//                @Override
//                public void run() {
//                    swingNode.setContent( panel );
//                }
//            } );
        }else{
            swingNode.setVisible( true );
            swingNode.setContent( new JComponent() {
                @Override
                public void updateUI() {
                    super.updateUI();
                }
            } );
            swingNode.setVisible( true );
//            SwingUtilities.invokeLater( new Runnable() {
//                @Override
//                public void run() {
//                    swingNode.setContent( new JComponent() {
//                        @Override
//                        public void print(Graphics g) {
//                            super.print( g );
//                        }
//                    } );
//                }
//            } );
        }
    }
    private DefaultTreeForTreeLayout<TaskBox> createTreeDiagram() {

        if (graph.getTasks().size() == 0) {
            return null;
        } else {
            Task tRoot = graph.getRootTask();
            TaskBox root = new TaskBox(tRoot.toString2(), setDiagramNodesWidth(tRoot), 40);
            DefaultTreeForTreeLayout<TaskBox> tree = new DefaultTreeForTreeLayout<TaskBox>(root);

            if(tRoot.hasSubtasks()){
                createTreeSubtasks( tree, root, tRoot );
            }
            return tree;
        }
    }
    private void createTreeSubtasks(DefaultTreeForTreeLayout<TaskBox> tree, TaskBox parent, Task task) {
        TaskBox sub;

        for(Task t : task.getSubtasks()){
            sub = new TaskBox(t.toString2(), setDiagramNodesWidth( t ), 40);
            tree.addChild(parent, sub);
            if(t.hasSubtasks()){
                createTreeSubtasks(tree, sub, t);
            }
        }
    }
    private int setDiagramNodesWidth(Task t){
        int posLength = Integer.valueOf(t.getPosition().length());
        int nameLength = Integer.valueOf(t.getName().length());
        if(posLength == 1 && nameLength == 1){
            return 18;
        }
        if(posLength > nameLength){
            return (posLength*10);
        }else{
            return (nameLength*10);
        }
    }

    //Tree Management
    private void updateTaskTreeView() {
        if (graph.getTasks().size() == 0) {
            taskTreeView.setRoot(null);
        } else {
            TreeItem<Task> root = new TreeItem<>(graph.getRootTask());
            root.setExpanded(true);
            if (root.getValue().hasSubtasks()) {
                updateTreeViewSubtasks(root);
            }
            taskTreeView.setRoot(root);
            setItemBehaviour();
        }
    }
    private void updateTreeViewSubtasks(TreeItem<Task> treeParentItem) {
        for (Task t : treeParentItem.getValue().getSubtasks()) {
            TreeItem<Task> item = new TreeItem<>(t);
            treeParentItem.getChildren().add(item);
            item.setExpanded(true);
            if (t.hasSubtasks()) {
                updateTreeViewSubtasks(item);
            }
        }
    }
    private void setItemBehaviour() {
        taskTreeView.getSelectionModel().selectedItemProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            selectedItem = (TreeItem<Task>) newValue;
            if (selectedItem != null) {
                taskName.setText(selectedItem.getValue().getName());
                position.setText(selectedItem.getValue().getPosition());
//                position.setDisable(true);
                plan.setText(selectedItem.getValue().getPlan());
                deleteButtonManagement(selectedItem);
                loggingButtonManagement(selectedItem);
                confirm.setText(SAVE);
            }
        });
    }

    //Initialization
    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        spV.setResizableWithParent( apControl, false );
//        spV.setResizableWithParent( spH, false );
//        spH.setResizableWithParent( ap1, false );
//        spH.setResizableWithParent( taskTreeView, false );
    }
    public MainGUIController(){}
    public void sendMainAppFrame(JFrame w){
        this.window = w;
    }
}


