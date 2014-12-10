package Beans.Fuzz;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class FuzzTypes implements Serializable
{
    List<Fuzz> fuzzTypes;
    
    public FuzzTypes()
    {
        fuzzTypes = new ArrayList<Fuzz>();
        
        fuzzTypes.add(new Fuzz("random_fuzz", "Random", "Sends randomized data in given fields that are not locked", true, false, true));
        fuzzTypes.add(new Fuzz("sql_fuzz", "SQL", "Sends various known SQL-injection strings in given fields that are not locked", true, false, true));
        fuzzTypes.add(new Fuzz("file_fuzz", "File", "Sends malformed files", true, false, false));
        fuzzTypes.add(new Fuzz("mutation_fuzz", "Mutation", "Sends accepted data, but with small mutations", true, true, true));
        fuzzTypes.add(new Fuzz("dictionary_fuzz", "Dictionary", "Sends random, well-formed data in given fields that are not locked", true, false, true));
        fuzzTypes.add(new Fuzz("method_fuzz", "Method", "Performs same requests, but with different request-methods", false, true, false));
        fuzzTypes.add(new Fuzz("header_fuzz", "Header", "Sends random and unnecessary headers", false, true, true));
        fuzzTypes.add(new Fuzz("path_fuzz", "Path", "Appends base path with random extensions and peforms requests on those paths", true, false, true));
        fuzzTypes.add(new Fuzz("parameter_fuzz", "Parameter", "Leaves out some given fields and / or adds extra fields to requests", false, false, true));
    }

    public List<Fuzz> getFuzzTypes() {
        return fuzzTypes;
    }

    public void setFuzzTypes(List<Fuzz> fuzzTypes) {
        this.fuzzTypes = fuzzTypes;
    }
    
    public Fuzz getFuzzById(String id)
    {
        for(int i = 0; i < fuzzTypes.size(); i++)
        {
            if(fuzzTypes.get(i).getId().equals(id))
            {
                return fuzzTypes.get(i);
            }
        }
        
        return null;
    }
}
