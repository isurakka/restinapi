/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans.Fuzz;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean
@SessionScoped
public class FuzzRunner implements Serializable
{
    public FuzzRunner()
    {
        
    }
    
    public String prepareFuzz()
    {
        return "fuzz_report";
    }
}
