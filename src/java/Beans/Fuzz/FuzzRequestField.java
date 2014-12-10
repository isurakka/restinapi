/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans.Fuzz;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean
@SessionScoped
public class FuzzRequestField implements Serializable
{
    private String fieldName;
    private String fieldValue;
    private boolean isLocked;
    
    public FuzzRequestField()
    {
        
    }
    
    public FuzzRequestField(String fieldName, String fieldValue, boolean isLocked)
    {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.isLocked = isLocked;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public boolean isIsLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }
    
    
}
