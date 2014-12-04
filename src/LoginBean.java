package restinapi;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.ManagedBean;
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
@RequestScoped
@ManagedBean
public class LoginBean implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;   
    private String password;
    private boolean isLoggedIn = false;
    
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
    
    
        
    public void login() {
    try {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/restinapi", "root", "");
        PreparedStatement ps = null;
                
        ps = conn.prepareStatement("select name, password FROM user where name = ? and poass = ?");
        
        ps.setString(1, username);
        ps.setString(2, password);
        
        ResultSet rs = ps.executeQuery();
        
        if (rs.next())
        {
            isLoggedIn = true;
        }
        else {
            isLoggedIn = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Username or password invalid", "Try again!"));
            
        }
    } catch (Exception ex) {
        System.out.println("Error loggin in: " + ex.getMessage());
    }
    
    }
   
    public void logout() {
     HttpSession currentSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
     currentSession.invalidate();
    }

}
