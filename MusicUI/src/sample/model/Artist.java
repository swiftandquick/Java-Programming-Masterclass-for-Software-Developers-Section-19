package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Artist {

    /* Use SimpleIntegerProperty and SimpleStringProperty because we need to bind later.
    SimpleIntegerProperty:  Provides a full implementation of a Property wrapping a int value.
    SimpleStringProperty:  Provides a full implementation of a Property wrapping a String value.  */
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;


    public Artist() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
    }


    public int getId() {
        return id.get();
    }


    public void setId(int id) {
        this.id.set(id);
    }


    public String getName() {
        return name.get();
    }


    public void setName(String name) {
        this.name.set(name);
    }

}
