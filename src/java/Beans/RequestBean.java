/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controllers.ProjectEntityJpaController;
import Controllers.RequestEntityJpaController;
import Entities.ProjectEntity;
import Entities.RequestEntity;
import Entities.UserEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;
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
    @ManagedProperty(value="#{userBean}")
    private UserBean currentUser;
    
    
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
    
        public UserBean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserBean currentUser) {
        this.currentUser = currentUser;
    }
    
    public void makeNewRequestEntity()
    {
        try {
        RequestEntity re = new RequestEntity();
        
        re.setRelativeUri(this.relative_uri);
        re.setMethod(this.method);
        re.setProjectName(this.currentUser.currentProject);
                
        RequestEntityJpaController rejc = new RequestEntityJpaController(this.utx, this.emf);
        rejc.create(re);
        
        } catch (Exception ex)
        {
            System.out.println("Error creating project: " + ex.getMessage());
        }

    }
    
}
