package models;

import com.google.common.collect.HashBiMap;

import java.io.Serializable;
import java.util.HashMap;

public class LoggingElement implements Serializable{

    private HashMap<String, String> references;
    private String description;

    public LoggingElement(){
        references = new HashMap<>();
    }

    public HashMap<String, String> getReferences() {
        return references;
    }

    public void addRefernce(String type, String reference){
        this.references.put(type, reference);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
