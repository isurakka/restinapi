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
@RequestScoped
public class FuzzSelector implements Serializable
{
    @ManagedProperty(value="#{param.pageId}")
    private String pageId;
    
    public FuzzSelector()
    {
        
    }
    
    public String selectFuzz()
    {
        return "fuzz_options?faces-redirect=true&selectedFuzz=" + pageId;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }
    
    
}
