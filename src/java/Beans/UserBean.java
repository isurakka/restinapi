/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Entities.ProjectEntity;
import Entities.UserEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
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
@SessionScoped
public class UserBean implements java.io.Serializable {
    
    private String username;
    private String password;
    
    @PersistenceUnit(unitName="restinapiPU")
    EntityManagerFactory emf;
    @Resource
    UserTransaction utx;
    
    
    UserEntity currentUser;
    List<ProjectEntity> userProjects;
    ProjectEntity currentProject;
    
   public UserBean()
    {
        
    }
   @PostConstruct
   public void fetchUserInfo()
   {
       System.out.println("Userbean post-construct");
        TypedQuery<UserEntity> userquery = emf.createEntityManager().createNamedQuery("UserEntity.findByName", UserEntity.class);
        
        HttpSession currentSession = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        this.username = (String) currentSession.getAttribute("username");

        userquery.setParameter("name", username);
        this.currentUser = userquery.getSingleResult();

        this.userProjects = new ArrayList<ProjectEntity>(currentUser.getProjectEntityCollection());
        
        currentProject = userProjects.get(0);
      //  TypedQuery<ProjectEntity> projectquery = emf.createEntityManager().createNamedQuery("ProjectEntity.findByUser", ProjectEntity.class);
       // projectquery.setParameter("user_name", this.username);
     //  this.userProjects = projectquery.getResultList();
   }
   
   public  void changeSelectedUserProject(ValueChangeEvent e)
   {
     //  System.out.println(e.getNewValue().toString());
      // this.currentProject = e.getNewValue();
   }
   
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserEntity getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserEntity currentUser) {
        this.currentUser = currentUser;
    }

    public List<ProjectEntity> getUserProjects() {
        return userProjects;
    }

    public void setUserProjects(List<ProjectEntity> userProjects) {
        this.userProjects = userProjects;
    }

    public ProjectEntity getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(ProjectEntity currentProject) {
        this.currentProject = currentProject;
    }
    
}
