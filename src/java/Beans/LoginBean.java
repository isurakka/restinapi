package Beans;


import Utils.DatabaseManager;
import Utils.BCrypt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.faces.bean.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Matti
 * 
 */
@SessionScoped
@ManagedBean(name = "loginBean")
public class LoginBean implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;   
    private String password;
    private boolean isLoggedIn = false;
    
    
    public LoginBean()
    {
        
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
    
    public void register() {
        try {
        Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = null;
                
        ps = conn.prepareStatement("INSERT INTO  user (name, password) VALUES(?, ?)");
        String hashedPass = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("hash:" + hashedPass);
        ps.setString(1, username);
        ps.setString(2, hashedPass);
        
        int affected = ps.executeUpdate();
        
        if (affected > 0)
        {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registering succeeded!", ""));
                        isLoggedIn = true;
        }
        }
        catch (Exception ex) {
            System.out.println("register error: " + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Registering failed!", "Try again!"));
        }

    }

        
    public String login() {
    try {
        Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = null;
                
        ps = conn.prepareStatement("select name, password FROM user where name = ? ");
        
        ps.setString(1, username);
        
        ResultSet rs = ps.executeQuery();
        
        if (rs.next())
        {            
            String hashedPass = rs.getString("password");
            
            System.out.println("pass: " + password);
            System.out.println("hashedpass: " + hashedPass);
            if (BCrypt.checkpw(password, hashedPass))
            {
                isLoggedIn = true;
                HttpSession currentSession = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
                currentSession.setAttribute("username", username);

                return "main";
            }
        }
      
       isLoggedIn = false;
       FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Username or password invalid", "Try again!"));
       return "";
        
    } catch (Exception ex) {
        System.out.println("Error loggin in: " + ex.toString());
        return "";
    }
    
    }
   
    public String logout() {
     HttpSession currentSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
     currentSession.invalidate();
     
     return "login";
    }

}
