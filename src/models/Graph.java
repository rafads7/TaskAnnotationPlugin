package models;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            addTaskInAPositionWithAlreadyAnotherTask(newTask);
        }
    }
    private void addTaskInAPositionWithAlreadyAnotherTask(Task newTask){
        Task oldTaskInNewPosition = this.tasks.get(newTask.getPosition());
        Task newParent = oldTaskInNewPosition.getParent();//gets the Parent of the oldTask in the new position

        int newPosLastLevel = Integer.valueOf(newTask.getPosition().substring(newTask.getPosition().length()-1));
        int currentTaskPosLastLevel;

        for(Task t : newParent.getSubtasks()){
            currentTaskPosLastLevel = Integer.valueOf(t.getPosition().substring(t.getPosition().length()-1));
            if(newPosLastLevel <= currentTaskPosLastLevel){
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
        newParent.getSubtasks().add(newParent.getSubtasks().indexOf(oldTaskInNewPosition), newTask);
        newTask.setParent(newParent);
        updateGraph(newParent);
    }
    private void updateExistingSubtasks(Task parent){
        String aux1;
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
        List<String> toRemove = new ArrayList<>();
        for(Task t: parent.getSubtasks()){
            this.tasks.put(t.getPosition(), t);
            if(t.hasSubtasks()){
                updateGraph(t);
            }
        }for(String pos : tasks.keySet()){
            if(!pos.equals(tasks.get(pos).getPosition())){
                toRemove.add(pos);
            }
        }
        for(String p : toRemove){
            tasks.remove(p);
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
            moveSubsequentTasks(task);
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
    private void moveSubsequentTasks(Task removedTask) {
        Task parent = removedTask.getParent();
        parent.getSubtasks().remove(removedTask);
        String currentTaskPos, aux1;

        for (Task t : parent.getSubtasks()) {
            if (isNotAPreviousTask(removedTask.getPosition(), t.getPosition())) {
                currentTaskPos = t.getPosition();
                if(currentTaskPos.length() == 1){
                    t.setPosition(String.valueOf(Integer.valueOf(currentTaskPos) - 1));
                }else{
                    aux1 = currentTaskPos.substring(currentTaskPos.length()-1);
                    t.setPosition(parent.getPosition() + "." + String.valueOf(Integer.valueOf(aux1)-1));
                }
                this.tasks.remove(currentTaskPos);
                this.tasks.put(t.getPosition(), t);
                if(t.hasSubtasks()){
                    moveSubsequentSubTasks(t);
                }
            }
        }
    }
    private void moveSubsequentSubTasks(Task parent) {
        String currentTaskPos, aux1;

        for (Task t : parent.getSubtasks()) {
            currentTaskPos = t.getPosition();
            aux1 = currentTaskPos.substring(currentTaskPos.length()-1);
            t.setPosition(parent.getPosition() + "." + aux1);

            this.tasks.remove(currentTaskPos);
            this.tasks.put(t.getPosition(), t);
            if(t.hasSubtasks()){
                moveSubsequentSubTasks(t);
            }
        }
    }
    private boolean isNotAPreviousTask(String toRemove, String toCheck) {
        int remove = Integer.valueOf(toRemove.substring(toRemove.length()-1));
        int check = Integer.valueOf(toCheck.substring(toCheck.length()-1));
        if (remove < check) return true;
        return false;
    }

    public void editTaskPosition(Task selectedTask, String newName, String newPlan, String newPos) {
        String oldPos = selectedTask.getPosition();
        if(oldPos.length() == 1 && Integer.valueOf(oldPos) == 0) {
            utils.Utils.showWarningMessage("You cannot move Root task.");
        }else if(newPos.length() == 1 && Integer.valueOf(newPos) == 0){
                utils.Utils.showWarningMessage("You cannot move to Root.");
        }else{
            editTaskNameAndPlan(getTaskByPosition(oldPos), newName, newPlan);

            if(!oldPos.equals(newPos)){
                if(moveTaskToSubsequentPosition(oldPos, newPos)){ //-->
                    if(taskAlreadyExists(newPos)){
                        if(haveSameParent(oldPos, newPos)){
                            deleteTask(selectedTask);
                            moveTaskToEditToNewPositions(selectedTask,newPos);
                        }else{
                            moveTaskToEditToNewPositions(selectedTask, newPos);
                            deleteTask(selectedTask);
                        }
                    }else{
                        if(newSubsequentPositionCanExistInDiagram(oldPos, newPos)){
                            moveTaskToEditToNewPositions(selectedTask, newPos);
                            deleteTask(selectedTask);
                        }
                    }
                }else{ //<--
                    if(taskAlreadyExists(newPos)){
                        moveSubsequentTasks(selectedTask);
                        selectedTask.setPosition(newPos);
                        if(selectedTask.hasSubtasks()){
                            setSubtasksNewPositions(selectedTask);
                        }
                        addTaskInAPositionWithAlreadyAnotherTask(selectedTask);
                    }else{
                        //moure
                        if(newPreviousPositionCanExistInDiagram(newPos)){
                            tasks.remove(oldPos);
                            synchWithParentAndMoveSubsequentTasks(selectedTask, newPos);
                            tasks.put(selectedTask.getPosition(), selectedTask);

                            if(selectedTask.hasSubtasks()){
                                moveSubsequentSubTasks(selectedTask);
                            }
                        }else{
                            utils.Utils.showWarningMessage("Sorry, there is no parent position for the new position.");
                        }
                    }
                }
            }
        }
    }

    private void moveTaskToEditToNewPositions(Task toMove, String newPos) {
        //toMove.setPosition(newPos);
        addTask(toMove.getName(), newPos, toMove.getPlan());

        String newPosition;
        if(toMove.hasSubtasks()){
            for(Task t : toMove.getSubtasks()){
                newPosition = newPos + "." + t.getPosition().substring(t.getPosition().length()-1);
                moveTaskToEditToNewPositions(t, newPosition);
            }
        }
    }
    

    private void setSubtasksNewPositions(Task parent) {

        String auxParent = parent.getPosition(), auxSubTask;
        for(Task t : parent.getSubtasks()){
            auxSubTask = t.getPosition().substring(t.getPosition().length()-1);
            t.setPosition(auxParent + "." + auxSubTask);

            if(t.hasSubtasks()){
                setSubtasksNewPositions(t);
            }
        }
    }

    private void editTaskNameAndPlan(Task toEdit, String newName, String newPlan){
        toEdit.setName(newName);
        toEdit.setPlan(newPlan);
    }

    private boolean moveTaskToSubsequentPosition(String oldP, String newP){
        String[] oPos = oldP.split("\\.");
        String[] nPos = newP.split("\\.");

        int limit;
        if(oldP.length() < newP.length()){
            limit = oPos.length;
        }else{
            limit = nPos.length;
        }

        for(int i=0; i<limit; i++){
            if(Integer.valueOf(oPos[i]) < Integer.valueOf(nPos[i])){
                return true;
            }
            else if(Integer.valueOf(oPos[i]) > Integer.valueOf(nPos[i])){
                return false;
            }
        }
        if(limit == oPos.length){
            return true;
        }else{
            return false;
        }
    }

    private boolean newPreviousPositionCanExistInDiagram(String newPos){

        String parentOfNewPos = newPos.substring(0, newPos.length() - 2);
        if (tasks.containsKey(parentOfNewPos)) { //if the new position has an existing parent where to belong
            int subTasksSize = tasks.get(parentOfNewPos).getSubtasks().size();
            if (subTasksSize == (Integer.valueOf(newPos.substring(newPos.length() - 1)) - 1)) { //if the new position is just after the last sibling
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    private boolean newSubsequentPositionCanExistInDiagram(String oldPos, String newPos){
        if(haveSameParent(oldPos,newPos)){
            utils.Utils.showWarningMessage("Error: You cannot move the task in position " + oldPos + " to position " + newPos +
                    " because then the previous position to the new one would be empty.");
            return false;
        }else{
            String newParentPosition;
            if(newPos.length() == 1){
                newParentPosition = "0";
            }else{
                newParentPosition = newPos.substring(0, newPos.length() - 2);
            }

            if(taskAlreadyExists(newParentPosition)){
                int newPosAux = Integer.valueOf(newPos.substring(newPos.length()-1));
                if(tasks.get(newParentPosition).getSubtasks().size() == (newPosAux-1)){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
    }

    private boolean haveSameParent(String oldPos, String newPos) {
        String parentOfOldPos;
        String parentOfNewPos;

        if(oldPos.length() == 1 && newPos.length() == 1){
            return true;
        }else if(oldPos.length() == 1 || newPos.length() == 1){
            return false;
        }else {
            parentOfOldPos = oldPos.substring(0, oldPos.length() - 2);
            parentOfNewPos = newPos.substring(0, newPos.length() - 2);

            if(parentOfNewPos.equals(parentOfOldPos)){
                return true;
            }else{
                return false;
            }
        }
    }


    private void synchWithParentAndMoveSubsequentTasks(Task selectedTask, String newPos){
        moveSubsequentTasks(selectedTask);
        selectedTask.setPosition(newPos);

        if(selectedTask.getPosition().length() == 1){
            selectedTask.setParent(tasks.get("0"));
            tasks.get("0").addSubtask(selectedTask);
        }else{
            selectedTask.setParent(tasks.get(selectedTask.getPosition().substring(0, selectedTask.getPosition().length()-2)));
            selectedTask.getParent().addSubtask(selectedTask);
        }
    }


}
