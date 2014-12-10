/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Administrator
 */
@ManagedBean
@SessionScoped
public class FuzzStatic implements Serializable
{
    private String fuzzType;
    
    public FuzzStatic()
    {
        
    }

    public String getFuzzType() {
        return fuzzType;
    }

    public void setFuzzType(String fuzzType) {
        this.fuzzType = fuzzType;
    }
    
    
}
