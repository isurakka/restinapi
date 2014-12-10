package Utils;


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
public class FuzzUtil implements Serializable
{
    public FuzzUtil()
    {
        
    }
    
    public String parseViewId(String viewId)
    {
        return viewId.substring(1, viewId.length() - 6);
    }
}
