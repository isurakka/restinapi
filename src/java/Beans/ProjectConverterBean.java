/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Entities.ProjectEntity;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 *
 * @author Matti
 */
// Converts requests to legit format in listboxes etc.

@ManagedBean
@FacesConverter(value="projectConverterBean")
public class ProjectConverterBean implements Converter{

    @PersistenceUnit(unitName="restinapiPU")
    EntityManagerFactory emf;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
       return emf.createEntityManager().find(ProjectEntity.class, value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((ProjectEntity) value).getName();
    }
    
}
