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

// database class to get over some strange JPA kinks

public class DatabaseManager {
    public static String DBurl = "jdbc:mysql://mysql.labranet.jamk.fi:3306/G8049";
    public static String DBuser = "G8049";
    public static String DBpassword = "E1ma1ScbPEYnn1QleULNvyba2sL6ct1Y";
    
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
