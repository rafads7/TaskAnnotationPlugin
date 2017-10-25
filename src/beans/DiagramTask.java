package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiagramTask implements Serializable{

    private String name;
    private String position;
    private String plan;
    private DiagramTask parent;
    private List<DiagramTask> subtasks;
    private String usabilityLoggingDescription;
    private HashMap<String, String> references;

    public DiagramTask(String name, String position, String plan) {
        this.name = name;
        this.position = position;
        this.plan = plan;
        this.subtasks = new ArrayList<>();
        this.references = new HashMap<>();
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

    public DiagramTask getParent() {
        return parent;
    }
    public void setParent(DiagramTask parent) {
        this.parent = parent;
    }

    public String getUsabilityLoggingDescription(){
        return this.usabilityLoggingDescription;
    }
    public void setUsabilityLoggingDescription(String usabilityLoggingDescription){
        this.usabilityLoggingDescription = usabilityLoggingDescription;
    }

    public HashMap<String, String> getReferences() {
        return references;
    }

    public List<DiagramTask> getSubtasks() {
        return this.subtasks;
    }
    public boolean hasSubtasks() {
        if(this.subtasks.size()==0)return false;
        return true;
    }
    public void addSubtask(DiagramTask subTask){
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

        DiagramTask that = (DiagramTask) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (position != null ? !position.equals(that.position) : that.position != null) return false;
        if (plan != null ? !plan.equals(that.plan) : that.plan != null) return false;
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
        return subtasks != null ? subtasks.equals(that.subtasks) : that.subtasks == null;
    }
    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (plan != null ? plan.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (subtasks != null ? subtasks.hashCode() : 0);
        return result;
    }
}
