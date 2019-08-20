/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author DANIEL
 */
public class connectionManager {
    
    private static final String USER = "test";
    private static final String PASS = "test";
    private static final String DB = "ControlPaquetes";
    private static final String CONNECTIONSTRING = "jdbc:mysql://localhost:3306/";
    
    private static Connection connection;
    private static Statement statement;
    private static ResultSet result;       
    
    public static void connect(){
        try {
            connection = DriverManager.getConnection(CONNECTIONSTRING+DB,USER,PASS);
            System.out.println("Conectado: " + connection.getCatalog());
            statement = connection.createStatement();
            connection.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void close(){
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(connectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    public static void check(){
        try {
            System.out.println(connection.getCatalog());
        } catch (SQLException ex) {
            Logger.getLogger(connectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static boolean checkStatement(String statementString){
        
        try {
            return statement.execute(statementString);
        } catch (SQLException ex) {
            Logger.getLogger(connectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static ResultSet select(String table, String fields, String conditions){
        result = null;
        String selectStatement = "SELECT " + fields + " FROM " + table + " WHERE " + conditions;
        try {
            PreparedStatement preparedSelect = connection.prepareStatement(selectStatement, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (checkStatement(selectStatement)) {
                result = preparedSelect.executeQuery();
                return result;
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(connectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static void insert(String table, String values){
        String sql = "INSERT INTO %s VALUES (%s)";
        sql = String.format(sql, table, values);
        try {
            PreparedStatement preparedInsert = connection.prepareStatement(sql);
            preparedInsert.executeUpdate();
            System.out.println("Correcto " + sql);
            connection.commit();
        } catch (Exception e) {
            System.out.println("Incorrecto " + sql);
            e.printStackTrace();
        } 
    }
}