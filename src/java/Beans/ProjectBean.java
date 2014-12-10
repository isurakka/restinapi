package Beans;


import Controllers.ProjectEntityJpaController;
import Controllers.RequestEntityJpaController;

import Entities.ParameterEntity;
import Controllers.ScriptEntityJpaController;
import Controllers.exceptions.RollbackFailureException;
import Entities.ProjectEntity;
import Entities.RequestEntity;
import Entities.ScriptEntity;
import Entities.UserEntity;
import Utils.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;
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

    private String beforescript;
    private String afterscript;
    
    private String requestNewValue;
    private String requestNewKey;
    
    // from persistence.xml
    @PersistenceUnit(unitName="restinapiPU")
    EntityManagerFactory emf;
    @Resource
    UserTransaction utx;
    
    @ManagedProperty(value="#{userBean}")
    private UserBean currentUser;
    
    @ManagedProperty(value="#{requestBean}")
    private RequestBean currentRequest;

    RequestEntity projectRequest;
    List<RequestEntity> projectRequests;
    
    @PostConstruct
    public void fetchProjectRequests()
    {
        TypedQuery<RequestEntity> userquery = emf.createEntityManager().createNamedQuery("RequestEntity.findByProject", RequestEntity.class);
                
        userquery.setParameter("projectName", this.currentUser.currentProject);

        this.projectRequests = new ArrayList<RequestEntity>(userquery.getResultList());
        
        //if (projectRequests != null && projectRequests.size() > 0)
       // {
        //    projectRequest = projectRequests.get(0);
       // }
    }
    
    public void onChangeSelectedRequest(ValueChangeEvent e) {
        // Hax maybe
        projectRequest = (RequestEntity)e.getNewValue();
        System.out.println("requestchanged, new request int: " + projectRequest.getRequestId());
        //RequestEntityJpaController rejc = new RequestEntityJpaController(utx, emf);
        //currentRequest.setRequestParameters(rejc.findParameters(projectRequest));
        
        /*
        if (getProjectRequest() == null)
        {
            System.out.println("getProjectRequest() is null");
            return;
        }
        
        if (getProjectRequest().getRequestId() == null)
        {
            System.out.println("getProjectRequest().getProjectId() is null");
            return;
        }
        
        TypedQuery<ParameterEntity> parameterQuery = emf.createEntityManager().createNamedQuery("ParameterEntity.findByRequestId", ParameterEntity.class);
        System.out.println("Request id: " + getProjectRequest().getRequestId());
        parameterQuery.setParameter("a", getProjectRequest().getRequestId());
        
        System.out.println(parameterQuery.getResultList().size());
        */
        
        //currentRequest.setRequestParameters(getProjectRequest().getParameterEntityList());
        
        try {
            Connection conn = DatabaseManager.getConnection();
            PreparedStatement ps = null;  
            ps = conn.prepareStatement("SELECT * FROM parameter WHERE request_id = ?");
            ps.setInt(1, projectRequest.getRequestId());

            ResultSet rs = ps.executeQuery();
            
            ArrayList<ParameterEntity> list = new ArrayList<ParameterEntity>();
            while (rs.next())
            {
                System.out.println("LOOP");
                System.out.println(rs.getInt("parameter_id"));
                ParameterEntity entity = new ParameterEntity(rs.getInt("parameter_id"), rs.getString("key"), rs.getString("value"));
                list.add(entity);
            }
            
            //projectRequests = list;
            currentRequest.setRequestParameters(list);
            
            System.out.println("GET PARAMS SUCCESS");
        } catch (Exception ex) {
            System.out.println("GET PARAMS FAIL");
        }
    }
    
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

    public UserBean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserBean currentUser) {
        this.currentUser = currentUser;
    }

    public List<RequestEntity> getProjectRequests() {
        return projectRequests;
    }

    public void setProjectRequests(List<RequestEntity> projectRequests) {
        this.projectRequests = projectRequests;
    }

    public String getBeforescript() {
        return beforescript;
    }

    public void setBeforescript(String beforescript) {
        this.beforescript = beforescript;
    }

    public String getAfterscript() {
        return afterscript;
    }

    public void setAfterscript(String afterscript) {
        this.afterscript = afterscript;
    }
    
        /**
     * @return the projectBean
     */
    public RequestEntity getProjectRequest() {
        return projectRequest;
    }

    /**
     * @param projectBean the projectBean to set
     */
    public void setProjectRequest(RequestEntity projectRequest) {
        this.projectRequest = projectRequest;
    }

    public String getRequestNewValue() {
        return requestNewValue;
    }

    public void setRequestNewValue(String requestNewParameter) {
        this.requestNewValue = requestNewParameter;
    }

    public String getRequestNewKey() {
        return requestNewKey;
    }

    public void setRequestNewKey(String requestNewKey) {
        this.requestNewKey = requestNewKey;
    }
    
    
    
    public ProjectBean() {}
    
    public void makeNewProjectEntity() {
        try {
            TypedQuery<UserEntity> query = emf.createEntityManager().createNamedQuery("UserEntity.findByName", UserEntity.class);

            HttpSession currentSession = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            String currentUserName = (String) currentSession.getAttribute("username");

            query.setParameter("name", currentUserName);
            UserEntity currentUser = query.getSingleResult();

            ProjectEntity pe = new ProjectEntity();
            ScriptEntity se = new ScriptEntity();

            se.setAfterScript(this.afterscript);
            se.setBeforeScript(this.beforescript);
            ScriptEntityJpaController sejc = new ScriptEntityJpaController(this.utx, this.emf);
            sejc.create(se);
            
            pe.setBaseUri(this.baseuri);
            pe.setName(this.name);
            pe.setUserName(currentUser);
            pe.setScriptId(se);
            
            
            ProjectEntityJpaController pejc = new ProjectEntityJpaController(this.utx, this.emf);

            pejc.create(pe);
            FacesContext context = FacesContext.getCurrentInstance();
            UserBean userBean = context.getApplication().evaluateExpressionGet(context, "#{userBean}", UserBean.class);
            userBean.fetchUserInfo();
            
            userBean.setCurrentProject(pe);
            fetchProjectRequests();

        } catch (Exception ex)
        {
            System.out.println("Error creating project: " + ex.getMessage());
        }
    }
    
    
    public void saveRequestBeforeAfterScriptChanges()
    {
        ScriptEntityJpaController sec = new ScriptEntityJpaController(this.utx, this.emf);
        try {
            sec.edit(this.projectRequest.getScriptId());
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ProjectBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ProjectBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
   public void saveRequestChanges()
    {
                    System.out.println("test");

        RequestEntityJpaController rec = new RequestEntityJpaController(this.utx, this.emf);
        try {
            rec.edit(this.projectRequest);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(ProjectBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ProjectBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
   public void addNewKeyValuePairToRequest()
   {
       System.out.println(this.requestNewKey);
      System.out.println(this.requestNewValue);
                  System.out.println("req. id: " + projectRequest.getRequestId());

      
           try {
            Connection conn = DatabaseManager.getConnection();
            PreparedStatement ps = null;  
                        
            ps = conn.prepareStatement("INSERT INTO parameter(`key`, `value`, `request_id`) VALUES(?, ?, ?)");
            ps.setString(1, this.getRequestNewKey());
            ps.setString(2, this.getRequestNewValue());
            ps.setInt(3, projectRequest.getRequestId());

            ps.executeUpdate();
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
      
   }

    /**
     * @return the currentRequest
     */
    public RequestBean getCurrentRequest() {
        return currentRequest;
    }

    /**
     * @param currentRequest the currentRequest to set
     */
    public void setCurrentRequest(RequestBean currentRequest) {
        this.currentRequest = currentRequest;
    }

}
