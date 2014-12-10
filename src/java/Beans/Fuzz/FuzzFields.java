/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans.Fuzz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean
@SessionScoped
public class FuzzFields implements Serializable
{
    private List<FuzzRequestField> fields;
    private String fieldName;
    private String fieldValue;
    private boolean isLocked;
    
    public FuzzFields()
    {
        fields = new ArrayList<FuzzRequestField>();
        fields.add(new FuzzRequestField("", "", true));
    }

    public List<FuzzRequestField> getFields() {
        return fields;
    }

    public void setFields(List<FuzzRequestField> fields) {
        this.fields = fields;
    }
    
    public void deleteField(FuzzRequestField field)
    {
        fields.remove(field);
    }
    
    public void addField()
    {
        fields.add(new FuzzRequestField(fieldName, fieldValue, isLocked));
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
