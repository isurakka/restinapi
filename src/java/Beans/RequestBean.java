/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controllers.ProjectEntityJpaController;
import Controllers.RequestEntityJpaController;
import Entities.ParameterEntity;
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
    
    //@ManagedProperty(value="#{projectBean}")
    private ProjectBean currentProject;
    
    private ParameterEntity requestParameter;
    private List<ParameterEntity> requestParameters;
    
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
        
        
        /*
        if (currentProject == null)
        {
            System.out.println("currentProject is null");
            return;
        }
        
        
        if (currentProject.getProjectRequest() == null)
        {
            System.out.println("currentProject.getProjectRequest() is null");
            return;
        }
        
        if (currentProject.getProjectRequest().getProjectId() == null)
        {
            System.out.println("currentProject.getProjectRequest().getProjectId() is null");
            return;
        }
        
        TypedQuery<ParameterEntity> parameterQuery = emf.createEntityManager().createNamedQuery("RequestEntity.findByRequestId", ParameterEntity.class);
        parameterQuery.setParameter("requestId", this.getCurrentProject().getProjectRequest().getProjectId());
        this.setRequestParameters(new ArrayList<ParameterEntity>(parameterQuery.getResultList()));
        
        */
        
    }
    
    /*
    @PostConstruct
    public void fetchRequestParameters()
    {
        //FacesContext context = FacesContext.getCurrentInstance();
        //ProjectBean projectBean = context.getApplication().evaluateExpressionGet(context, "#{projectBean}", ProjectBean.class);
        
        if (currentProject == null)
        {
            System.out.println("currentProject is null");
            return;
        }
        
        
        if (currentProject.getProjectRequest() == null)
        {
            System.out.println("currentProject.getProjectRequest() is null");
            return;
        }
        
        if (currentProject.getProjectRequest().getProjectId() == null)
        {
            System.out.println("currentProject.getProjectRequest().getProjectId() is null");
            return;
        }
        
        TypedQuery<ParameterEntity> parameterQuery = emf.createEntityManager().createNamedQuery("RequestEntity.findByRequestId", ParameterEntity.class);
        parameterQuery.setParameter("requestId", this.getCurrentProject().getProjectRequest().getProjectId());
        this.setRequestParameters(new ArrayList<ParameterEntity>(parameterQuery.getResultList()));
        
    }
    */
    
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

            // TODO: FIX
            FacesContext context = FacesContext.getCurrentInstance();
            
             ProjectBean projectBean = context.getApplication().evaluateExpressionGet(context, "#{projectBean}", ProjectBean.class);
            projectBean.setProjectRequest(re);
            projectBean.fetchProjectRequests();
            //projectBean.projectRequests.add(re);
        } catch (Exception ex)
        {
            System.out.println("Error creating project: " + ex.getMessage());
        }
    }

    /**
     * @return the requestParameter
     */
    public ParameterEntity getRequestParameter() {
        return requestParameter;
    }

    /**
     * @param requestParameter the requestParameter to set
     */
    public void setRequestParameter(ParameterEntity requestParameter) {
        this.requestParameter = requestParameter;
    }

    /**
     * @return the requestParameters
     */
    public List<ParameterEntity> getRequestParameters() {
        return requestParameters;
    }

    /**
     * @param requestParameters the requestParameters to set
     */
    public void setRequestParameters(List<ParameterEntity> requestParameters) {
        this.requestParameters = requestParameters;
    }

    /**
     * @return the currentProject
     */
    public ProjectBean getCurrentProject() {
        return currentProject;
    }

    /**
     * @param currentProject the currentProject to set
     */
    public void setCurrentProject(ProjectBean currentProject) {
        this.currentProject = currentProject;
    }
    
}
