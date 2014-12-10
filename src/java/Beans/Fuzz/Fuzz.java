package Beans.Fuzz;


import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jonah
 */
@ManagedBean
@SessionScoped
public class Fuzz implements Serializable
{
    private String id;
    private String name;
    private String description;
    private boolean lockable_fields;
    private boolean value_field;
    private boolean can_select_method;
    private String fullInfo;
    
    public Fuzz()
    {
        
    }
    
    public Fuzz(String id, String name, String description, boolean lockable_fields, boolean value_field, boolean can_select_method)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lockable_fields = lockable_fields;
        this.value_field = value_field;
        this.can_select_method = can_select_method;
        this.fullInfo = name + " fuzz - " + description;
    } 

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isLockable_fields() {
        return lockable_fields;
    }

    public void setLockable_fields(boolean lockable_fields) {
        this.lockable_fields = lockable_fields;
    }

    public boolean isValue_field() {
        return value_field;
    }

    public void setValue_field(boolean value_field) {
        this.value_field = value_field;
    }

    public String getFullInfo() {
        return fullInfo;
    }

    public void setFullInfo(String fullInfo) {
        this.fullInfo = fullInfo;
    }

    public boolean isCan_select_method() {
        return can_select_method;
    }

    public void setCan_select_method(boolean can_select_method) {
        this.can_select_method = can_select_method;
    }
    
    
}
