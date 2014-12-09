/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;

/**
 *
 * @author Matti
 */
@ManagedBean
@RequestScoped
public class RequestBean implements java.io.Serializable{
    
    private String relative_uri;
    private String method;
    
    private List<String> availableMethods;
    
    @PersistenceUnit(unitName="restinapiPU")
    EntityManagerFactory emf;
    @Resource
    UserTransaction utx;
    
    @PostConstruct
    public void initRequestBean()
    {
        this.availableMethods = new ArrayList<String>();
        this.availableMethods.add("GET");
        this.availableMethods.add("POST");
        this.availableMethods.add("PUT");
        this.availableMethods.add("DELETE");
        this.availableMethods.add("HEAD");
        this.availableMethods.add("GET");
    }
    
    public String getRelative_uri() {
        return relative_uri;
    }

    public void setRelative_uri(String relative_uri) {
        this.relative_uri = relative_uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<String> getAvailableMethods() {
        return availableMethods;
    }

    public void setAvailableMethods(List<String> availableMethods) {
        this.availableMethods = availableMethods;
    }
    
    
}
