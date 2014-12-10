/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Controllers.ScriptEntityJpaController;
import Entities.ScriptEntity;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class ScriptBean implements java.io.Serializable {
 
    private String beforeScript;
    private String afterScript;

    @PersistenceUnit(unitName="restinapiPU")
    EntityManagerFactory emf;
    @Resource
    UserTransaction utx;
    
    public String getBeforeScript() {
        return beforeScript;
    }

    public void setBeforeScript(String beforeScript) {
        this.beforeScript = beforeScript;
    }

    public String getAfterScript() {
        return afterScript;
    }

    public void setAfterScript(String afterScript) {
        this.afterScript = afterScript;
    }
    
    public void makeNewScriptEntityRequest()
    {
        try {
            ScriptEntity se = new ScriptEntity();
            
            se.setAfterScript(this.afterScript);
            se.setBeforeScript(this.beforeScript);
            
            ScriptEntityJpaController sejc = new ScriptEntityJpaController(this.utx, this.emf);
            
            sejc.create(se);
            
            FacesContext context = FacesContext.getCurrentInstance();
            ProjectBean projectBean = context.getApplication().evaluateExpressionGet(context, "#{projectBean}", ProjectBean.class);
            projectBean.getProjectRequest().setScriptId(se);
            
            
            // TODO: FIX            
            //projectBean.projectRequests.add(re);
        } catch (Exception ex)
        {
            System.out.println("Error creating request script: " + ex.toString());
        }        
    }
    
    public void makeNewScriptEntityProject()
    {
           
        try {
            ScriptEntity se = new ScriptEntity();
            
            se.setAfterScript(this.afterScript);
            se.setBeforeScript(this.beforeScript);
            
            ScriptEntityJpaController sejc = new ScriptEntityJpaController(this.utx, this.emf);
            
            sejc.create(se);
            
            FacesContext context = FacesContext.getCurrentInstance();
            UserBean userBean = context.getApplication().evaluateExpressionGet(context, "#{userBean}", UserBean.class);
            userBean.getCurrentProject().setScriptId(se);
                        
            // TODO: FIX            
            //projectBean.projectRequests.add(re);
        } catch (Exception ex)
        {
            System.out.println("Error creating request script: " + ex.toString());
        }       
    }
}
