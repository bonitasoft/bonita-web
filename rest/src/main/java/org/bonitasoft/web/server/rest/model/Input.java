package org.bonitasoft.web.server.rest.model;

public class Input {

    private String name;
    private Object value;

    public Input() {
        // Empty constructor for json serialization
    }
    
    public Input(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
