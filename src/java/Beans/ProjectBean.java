package Beans;


import Controllers.ProjectEntityJpaController;
import Entities.ProjectEntity;
import Entities.UserEntity;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Matti
 */

@ManagedBean
@RequestScoped
public class ProjectBean implements java.io.Serializable {
    
    private String name;
    private String baseuri;

    // from persistence.xml
    @PersistenceUnit(unitName="restinapiPU")
    EntityManagerFactory emf;
    @Resource
    UserTransaction utx;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBaseuri() {
        return baseuri;
    }

    public void setBaseuri(String baseuri) {
        this.baseuri = baseuri;
    }
    
    public ProjectBean() {}
    
    public void makeNewProject() {
        try {
        TypedQuery<UserEntity> query = emf.createEntityManager().createNamedQuery("UserEntity.findByName", UserEntity.class);
        
        HttpSession currentSession = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String currentUserName = (String) currentSession.getAttribute("username");

        query.setParameter("name", currentUserName);
        UserEntity currentUser = query.getSingleResult();
                
        ProjectEntity pe = new ProjectEntity();
        
        pe.setBaseUri(this.baseuri);
        pe.setName(this.name);
        pe.setUserName(currentUser);
        
        ProjectEntityJpaController pejc = new ProjectEntityJpaController(this.utx, this.emf);
        
        pejc.create(pe);
        
        } catch (Exception ex)
        {
            System.out.println("Error creating project: " + ex.getMessage());
        }
    }
    
}
