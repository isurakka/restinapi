package Beans.Fuzz;


import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.RequestScoped;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jonah
 */
@ManagedBean
@SessionScoped
public class FuzzSelector implements Serializable
{
    private String pageId;
    
    public FuzzSelector()
    {
        
    }
    
    public String toSelect()
    {
        return "select_fuzz";
    }
    
    public String selectFuzz(String pageId)
    {
        this.pageId = pageId;
        return "fuzz_options?faces-redirect=true";
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }
    
    
}
