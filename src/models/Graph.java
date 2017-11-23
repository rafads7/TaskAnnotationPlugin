package models;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.Serializable;
import java.util.HashMap;

public class Graph implements Serializable{

    private final String ROOT_POSITION = "0";
    private final String CREATED = "created";
    private final String NO_PARENT = "no_parent";
    private final String NO_ROOT = "no_root";
    private final String NO_PREVIOUS_SIBLING = "no_previous_sibling";
    private HashMap<String, Task> tasks;

    public Graph() {
        this.tasks = new HashMap<>();
    }

    //Getters
    public HashMap<String, Task> getTasks() {
        return tasks;
    }
    public Task getRootTask() {
        return this.tasks.get("0");
    }
    public Task getTaskByPosition(String pos) {
        return this.tasks.get(pos);
    }

    //Task Creation
    public String addTask(String taskName, String position, String plan) {
        Task task = new Task(taskName, position, plan);
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
    private void createNewTaskAndUpdateExistingTasks(Task newTask) {

        if(newTask.getPosition().equals("0")){
            utils.Utils.showWarningMessage("Error: you cannot create a new root.");
        }else{
            Task old = this.tasks.get(newTask.getPosition());
            int newTaskPosLastLevel = Integer.valueOf(newTask.getPosition().substring(newTask.getPosition().length()-1));
            int currentTaskPosLastLevel;

            for(Task t : old.getParent().getSubtasks()){
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
    private void updateExistingSubtasks(Task parent){
        String aux1, aux2, aux3;
        int parentPosLength = parent.getPosition().length();
        for(Task t: parent.getSubtasks()){
            aux1 = t.getPosition().substring(parentPosLength);
            t.setPosition(parent.getPosition() + aux1);
            if(t.hasSubtasks()){
                updateExistingSubtasks(t);
            }
        }
    }
    private void updateGraph(Task parent){
        for(Task t: parent.getSubtasks()){
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
    private boolean hasPreviousSibling(Task task) {
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
    private boolean setParent(Task task) {

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
    public void deleteTask(Task task) {
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
    private void deleteSubtasks(Task task) {
        for (Task subT : task.getSubtasks()) {
            if (subT.hasSubtasks()) {
                deleteSubtasks(subT);
            }
            this.tasks.remove(subT.getPosition());

        }
    }

    //Graph Update
    private void moveFollowingTasks(Task removedTask) {
        Task parent = removedTask.getParent();
        parent.getSubtasks().remove(removedTask);
        String currentTaskPos, aux1, aux2;
        int aux3, parentPosLenghtToCompare = parent.getPosition().length();
        for (Task t : parent.getSubtasks()) {
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
    private void moveFollowingSubTasks(Task parent) {
        String currentTaskPos, aux1, aux2, aux4;
        int aux3, parentPosLenghtToCompare = parent.getPosition().length();

        for (Task t : parent.getSubtasks()) {
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
