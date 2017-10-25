package beans;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.Serializable;
import java.util.HashMap;

public class TaskGraph implements Serializable{

    private final String ROOT_POSITION = "0";
    private final String CREATED = "created";
    private final String NO_PARENT = "no_parent";
    private final String NO_ROOT = "no_root";
    private final String NO_PREVIOUS_SIBLING = "no_previous_sibling";
    private HashMap<String, DiagramTask> tasks;

    public TaskGraph() {
        this.tasks = new HashMap<>();
    }

    //Getters
    public HashMap<String, DiagramTask> getTasks() {
        return tasks;
    }
    public DiagramTask getRootTask() {
        return this.tasks.get("0");
    }
    public DiagramTask getTaskByPosition(String pos) {
        return this.tasks.get(pos);
    }

    //Task Creation
    public String addNewTaskToDiagram(TextField taskName, TextField position, TextArea plan) {
        DiagramTask task = new DiagramTask(taskName.getText(), position.getText(), plan.getText());
        if (taskAlreadyExists(task.getPosition())) {
            if(utils.Utils.showWarningCheck("Creation",  task.toString())){
                createNewTaskAndUpdateExistingTasks(task);
                return CREATED;
            }else{
                return "";
            }
        } else {
            if (!taskIsRoot(task.getPosition())) {
                if (!rootExistence()) {
                    return NO_ROOT;
                }
                if (!setParent(task)) {
                    return NO_PARENT;
                }
                if (!hasPreviousSibling(task)) {
                    return NO_PREVIOUS_SIBLING;
                }
                task.getParent().addSubtask(task);
            }
            this.tasks.put(task.getPosition(), task);
            return CREATED;
        }
    }
    private void createNewTaskAndUpdateExistingTasks(DiagramTask newTask) {

        if(newTask.getPosition().equals("0")){
            utils.Utils.showWarningMessage("Error: you cannot create a new root.");
        }else{
            DiagramTask old = this.tasks.get(newTask.getPosition());
            int newTaskPosLastLevel = Integer.valueOf(newTask.getPosition().substring(newTask.getPosition().length()-1));
            int currentTaskPosLastLevel;

            for(DiagramTask t : old.getParent().getSubtasks()){
                currentTaskPosLastLevel = Integer.valueOf(t.getPosition().substring(t.getPosition().length()-1));
                if(newTaskPosLastLevel <= currentTaskPosLastLevel){
                    if(t.getPosition().length() == 1){
                        t.setPosition(String.valueOf(Integer.valueOf(t.getPosition()) + 1));
                    }else{
                        t.setPosition(t.getParent().getPosition() + "." + String.valueOf(currentTaskPosLastLevel+1));
                    }
                    if(t.hasSubtasks()){
                        updateExistingSubtasks(t);
                    }
                }
            }
            old.getParent().getSubtasks().add(old.getParent().getSubtasks().indexOf(old), newTask);
            newTask.setParent(old.getParent());
            updateGraph(old.getParent());
        }
    }
    private void updateExistingSubtasks(DiagramTask parent){
        String aux1, aux2, aux3;
        int parentPosLength = parent.getPosition().length();
        for(DiagramTask t: parent.getSubtasks()){
            aux1 = t.getPosition().substring(parentPosLength);
            t.setPosition(parent.getPosition() + aux1);
            if(t.hasSubtasks()){
                updateExistingSubtasks(t);
            }
        }
    }
    private void updateGraph(DiagramTask parent){
        for(DiagramTask t: parent.getSubtasks()){
            this.tasks.put(t.getPosition(), t);
            if(t.hasSubtasks()){
                updateGraph(t);
            }
        }
    }

    //Task Creation boolean checking
    private boolean rootExistence() {
        return this.tasks.containsKey("0");
    }
    private boolean hasPreviousSibling(DiagramTask task) {
        String position = task.getPosition().substring(task.getPosition().length() - 1);
        if (task.getParent().getSubtasks().size() < Integer.valueOf(position) - 1) {
            return false;
        } else {
            return true;
        }
    }
    private boolean taskAlreadyExists(String pos) {
        if (tasks.containsKey(pos)) {
            return true;
        }
        return false;
    }
    private boolean taskIsRoot(String position) {
        if (position.equals(ROOT_POSITION)) {
            return true;
        }
        return false;
    }
    private boolean setParent(DiagramTask task) {

        if (task.getPosition().length() == 1) {
            task.setParent(this.tasks.get("0"));
            return true;
        } else {
            String parent = task.getPosition().substring(0, task.getPosition().length() - 2);
            if (!parentFound(parent)) {
                return false;
            }
            task.setParent(this.tasks.get(parent));
            return true;
        }
    }
    private boolean parentFound(String parent) {
        return this.tasks.containsKey(parent);
    }

    //Task Elimination
    public void deleteTask(DiagramTask task) {
        if (this.getRootTask().equals(task)) {
            this.tasks.clear();
        }else{
            this.tasks.remove(task.getPosition());
            task.getParent().getSubtasks().remove(task);
            if (task.hasSubtasks()) {
                deleteSubtasks(task);
            }
            moveFollowingTasks(task);
        }
    }
    private void deleteSubtasks(DiagramTask task) {
        for (DiagramTask subT : task.getSubtasks()) {
            if (subT.hasSubtasks()) {
                deleteSubtasks(subT);
            }
            this.tasks.remove(subT.getPosition());

        }
    }

    //Graph Update
    private void moveFollowingTasks(DiagramTask removedTask) {
        DiagramTask parent = removedTask.getParent();
        parent.getSubtasks().remove(removedTask);
        String currentTaskPos, aux1, aux2;
        int aux3, parentPosLenghtToCompare = parent.getPosition().length();
        for (DiagramTask t : parent.getSubtasks()) {
            if (isNotAPreviousTask(removedTask.getPosition(), t.getPosition())) {
                currentTaskPos = t.getPosition();
                if(currentTaskPos.length() == 1){
                    aux3 = Integer.valueOf(currentTaskPos.substring(0)) - 1;
                    t.setPosition(String.valueOf(aux3));
                }else{
                    aux1 = currentTaskPos.substring(0, parentPosLenghtToCompare + 1);
                    aux2 = currentTaskPos.substring(parentPosLenghtToCompare + 1);
                    aux3 = Integer.valueOf(aux2) - 1;
                    t.setPosition((aux1 + String.valueOf(aux3)));
                }
                this.tasks.remove(currentTaskPos);
                this.tasks.put(t.getPosition(), t);
                if(t.hasSubtasks()){
                    moveFollowingSubTasks(t);
                }
            }
        }
    }
    private void moveFollowingSubTasks(DiagramTask parent) {
        String currentTaskPos, aux1, aux2, aux4;
        int aux3, parentPosLenghtToCompare = parent.getPosition().length();

        for (DiagramTask t : parent.getSubtasks()) {
            currentTaskPos = t.getPosition();

            aux1 = currentTaskPos.substring(0, parentPosLenghtToCompare-1);
            aux2 = currentTaskPos.substring(parentPosLenghtToCompare - 1, parentPosLenghtToCompare);
            aux3 = Integer.valueOf(aux2) - 1;
            aux4 = currentTaskPos.substring(parentPosLenghtToCompare);
            t.setPosition((aux1 + String.valueOf(aux3) + aux4));

            this.tasks.remove(currentTaskPos);
            this.tasks.put(t.getPosition(), t);
            if(t.hasSubtasks()){
                moveFollowingSubTasks(t);
            }
        }
    }
    private boolean isNotAPreviousTask(String toRemove, String toCheck) {
        int remove = Integer.valueOf(toRemove.substring(toRemove.length()-1));
        int check = Integer.valueOf(toCheck.substring(toCheck.length()-1));
        if (remove < check) return true;
        return false;
    }
}
