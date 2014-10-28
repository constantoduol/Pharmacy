/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quest.access.common.mysql;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 *
 * @author connie
 */
public class ConnectionPool {
    private static ConcurrentHashMap<String,Connection> connections=new ConcurrentHashMap();
    public static Connection getConnection(String dbName,String userName,String host,String pass){
        boolean connectionExists=connections.containsKey(dbName);
        if(connectionExists){
            try {
                Connection conn = connections.get(dbName);
                if(conn.isClosed()){
                   conn=createConnection(dbName, host, userName, pass);
                   connections.put(dbName, conn);
                   return conn;
                }
                return conn;
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } 
        else{
           Connection conn= createConnection(dbName, host, userName, pass);
           connections.put(dbName, conn);
           return conn;
        }
    }
     private static Connection createConnection(String dbName, String host, String userName, String pass){
       try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(getJDBCUrl(host)+dbName,userName,pass);
            return connection;
           }
         catch(Exception e){
           throw new RuntimeException(e);
       }
    }
     
        /**
     * returns a jdbc url from a normal url such as localhost ,127.0.0.1 etc.
     * @param normalUrl the url we want to convert to a jdbc url
     */
    private static String getJDBCUrl(String normalUrl){
        return "jdbc:mysql://"+normalUrl+":3306/";
    }
}
