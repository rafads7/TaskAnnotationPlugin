package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Task implements Serializable{

    private String name;
    private String position;
    private String plan;
    private Task parent;
    private List<Task> subtasks;
    private LoggingElement loggingElement;

    public Task(String name, String position, String plan) {
        this.name = name;
        this.position = position;
        this.plan = plan;
        this.subtasks = new ArrayList<>();
        this.loggingElement = new LoggingElement();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }

    public String getPlan() {
        return plan;
    }
    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Task getParent() {
        return parent;
    }
    public void setParent(Task parent) {
        this.parent = parent;
    }

    public LoggingElement getLoggingElement() {
        return loggingElement;
    }
    public void setLoggingReference(String tracker, String text) {
        this.loggingElement.addRefernce(tracker, text);
    }
    public void setLoggingElementDescription(String description){
        this.loggingElement.setDescription(description);
    }

    public List<Task> getSubtasks() {
        return this.subtasks;
    }
    public boolean hasSubtasks() {
        if(this.subtasks.size()==0)return false;
        return true;
    }
    public void addSubtask(Task subTask){
        this.subtasks.add(subTask);
    }

    @Override
    public String toString() {
        return position + ": " + name;
    }
    public String toString2() {
        return position + "\n" + name;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task that = (Task) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (position != null ? !position.equals(that.position) : that.position != null) return false;
        if (plan != null ? !plan.equals(that.plan) : that.plan != null) return false;
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
        return subtasks != null ? subtasks.equals(that.subtasks) : that.subtasks == null;
    }


}
