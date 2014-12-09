package Utils;


import java.sql.Connection;
import java.sql.DriverManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Matti
 */
public class DatabaseManager {
    public static String DBurl = "jdbc:mysql://104.236.54.118:3306/restinapi";
    public static String DBuser = "restinapi";
    public static String DBpassword = "ria";
    
    /*
    public static String DBurl = "jdbc:mysql://104.236.54.118/restinapi";
    public static String DBuser = "restinapi";
    public static String DBpassword = "ria";
    */
    public static Connection getConnection()
    {
        try {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(DBurl, DBuser, DBpassword);
        return conn;
            
        }
        catch (Exception ex)
        {
            return null;
        }
    }

}
