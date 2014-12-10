package Beans;


import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

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
public class FuzzManager implements Serializable
{
    private String fuzzRequestMethod;
    private String fuzzRequestUrl;
    
    public FuzzManager()
    {
        
    }

    public String getFuzzRequestMethod() {
        return fuzzRequestMethod;
    }

    public void setFuzzRequestMethod(String fuzzRequestMethod) {
        this.fuzzRequestMethod = fuzzRequestMethod;
    }

    public String getFuzzRequestUrl() {
        return fuzzRequestUrl;
    }

    public void setFuzzRequestUrl(String fuzzRequestUrl) {
        this.fuzzRequestUrl = fuzzRequestUrl;
    }
    
    
}
