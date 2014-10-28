
package com.quest.access.common.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author constant oduol
 * @version 1.0(11/1/2012)
 */

/**
 * This file defines methods for interacting with a mysql table
 * a table object is created by creating a new instance of this class if
 * the table defined by the table object does not exist it is created in
 * the mysql server.
 * 
 */
public class Table implements java.io.Serializable {
    
    /*
     * this is the name of the table
     */
    private String name;
    /*
     * the unique id of the table
     */
    private int tableId;
    /*
     * this counts the number of initialized tables
     */
    private static int tableCount;
    /*
     * the name of the database where the table is found
     */
    private String dbName;
    
    /**
     * this constructor creates a table with a specified primary/first column
     * the first column is by default the primary key column and has auto increment set 
     * by default, its datatype is INT 
     * @param tableName the name of the table we want to create
     * @param dbName the name of the database the table will be created in
     * @param firstColumn the name of the first column in the table
     * 
     */
    public Table(String firstColumn, String tableName, String dbName){
          //create a table only if it does not exist
          Database.executeQuery("CREATE TABLE IF NOT EXISTS "+tableName+" ("+firstColumn+" INT NOT NULL AUTO_INCREMENT, PRIMARY KEY("+firstColumn+"))", dbName);
          this.name=tableName;
          this.dbName=dbName;
          tableCount++;
          this.tableId=tableCount;
           
         }
   
    /**
     * this constructor creates a table with a specified primary/first column
     * the first column is by default the primary key column and has auto increment set 
     * by default, its datatype is INT 
     * @param tableName the name of the table we want to create
     * @param dbName the name of the database the table will be created in
     * @param firstColumn the name of the first column in the table 
     * @param firstColumnDataType the data type of the first column eg. int for numbers
     * varchar, Text, mediumint etc.
     * @see com.qaccess.common.DataType
     * data types can be specified as strings eg int, varchar(20) or using the constants in datatype
     * e.g. DataType.VARCHAR+"(20)", DataType.INT, DataType.MEDIUMINT
     */
    public Table(String firstColumn, String tableName, String dbName,String firstColumnDataType){
                Database.executeQuery("CREATE TABLE IF NOT EXISTS "+tableName+" ("+firstColumn+" "+firstColumnDataType+",PRIMARY KEY("+firstColumn+"))", dbName);
                this.name=tableName;
                this.dbName=dbName;
                tableCount++;
                this.tableId=tableCount;
    }
    
    /**
     * this method is used to privately create Table objects which are used by
     * @see #getExistingTable(java.lang.String, java.lang.String)
     * @param name the name of the table to be created
     * @param dbName the name of the database where the table should be created
     * @param id this is the unique table id
     */
    
     private Table(String name, String dbName,int id){
         this.name=name;
         this.dbName=dbName;
         this.tableId=id;
    }
    
    /**
     * returns the name of the current table 
     */
    public String getTableName(){
        return this.name;
    }
    
   
    /**
     * this method modifies the name of the table in the database
     * @param newName the new name of this table
     */
    public void setTableName(String newName){
        Database.executeQuery("ALTER TABLE "+this.name+" RENAME "+newName+"", dbName); 
         this.name=newName;
    }
    
    /**
     * returns the name of the database the current table is found in
     */
    public String getDatabaseName(){
        return this.dbName;
    }
    
    /**
     * <p>
     * this method checks in the information_schema database
     * in the tables table to ascertain whether the table exists
     * the following sql code is executed
     * <code>
     * USE information_schema;
     * SELECT TABLE_SCHEMA ,TABLE_NAME FROM tables WHERE TABLE_SCHEMA=name AND TABLE_NAME=dbName;
     * </code>
     * </p>
     * @param name the name of the table we want to ascertain its existence
     * @param dbName the name of the database we want to check for the table
     */
    
    
      public static boolean ifTableExists(String name, String dbName){
            ResultSet set = Database.executeQuery("SELECT TABLE_SCHEMA ,TABLE_NAME FROM tables WHERE TABLE_SCHEMA=? AND TABLE_NAME=? ", "information_schema",dbName,name);
            try {
                while(set.next()){
               // check whether the table exists in the tables table
                 if(set.getString("TABLE_NAME")!=null ){
                    return true;
                }
              }//close the result set
                   set.close();
           return false;
         }
        catch(SQLException e){
            return false;
        }
        
     }
    
    /**
     * this method checks whether a table exists
     * @see #ifTableExists(java.lang.String)
     * @param tb the table object we want to ascertain its existence
     */
    public static boolean ifTableExists(Table tb){
        return ifTableExists(tb.getTableName(),tb.getDatabaseName());
    }
    
    /**
     * this method is used to drop the current table
     * further reference to the table after it has been dropped will generate exceptions
     */
    public void dropTable(){
        Database.executeQuery("DROP TABLE "+this.name, this.dbName);
    }
    
    /**
     * this method is used to delete the specified row in the specified table
     * the executed sql query is<code> DELETE FROM tableName WHERE condition</code>
     * eg. <code>DELETE FROM table1 WHERE ID=1</code>
     * @param tableName the name of the table to delete the row from
     * @param dbName the name of the database the table is contained in
     * @param condition the condition to be checked before deleting the specified rows
     *                  eg. ID=1 AND NAME='mike'
     */
    public static void deleteRow(String tableName, String dbName , String condition){
        Database.executeQuery("DELETE FROM "+tableName+" WHERE "+condition+"", dbName);
    }
   
    /**
     * this method deletes the row or rows that meet the specified condition
     * @see #deleteRow(String, String, String)
     * @param condition the condition to be used in locating the row to be deleted eg ID=1 OR NAME='sam'
     */
    public void deleteRow(String condition){
        deleteRow(this.name, this.dbName, condition);
    }

   /**
    *this method is a factory method that returns an instance of an existing table without trying to recreate it
    *a NonExistentTableException is thrown if the table specified to be existing does not exist 
    *@param name the name of the table we want to get
    *@param dbName the database where the table is found
    *@throws NonExistentTableException 
    */
    public static Table getExistingTable(String name, String dbName) throws NonExistentTableException{
             //check to ensure the table exists
        if(ifTableExists(name,dbName)){
            //count this table
            tableCount++;
            return new Table(name,dbName,tableCount);
            }
        else{
            throw new NonExistentTableException();
        }
   }
    
    
    /**
     * this method returns an instance of the specified column or throws an exception if the
     * column does not exist
     * @param name the name of the column we want to get an instance of
     */
    public Column getColumn(String name) throws NonExistentColumnException{
       return Column.getExistingColumn(name, this.name, this.dbName);
    }
    
    /**
     * this method adds a column to an existing table if the column already 
     * exists it is not replaced
     * @param name the name of the column to be added
     * @param dataType the dataType of the column eg. INT, VARCHAR(30), CHAR(100) etc
     * 
     */
    public Column addColumn(String name, String dataType){
         return new Column(name, dataType, this);
    }
    
    /**
     * this method adds many columns to a table
     * @param str an array containing values to create the column
     *            the array should be in this format 
     *           <code>new String[](columnName,dataType,columnName,dataType,columnName,dataType...)</code>;
     */
    public void addColumns(String[] str){
        for(int x=0; x<str.length; x++){
           if(x%2==0){
             Column col=new Column(str[x],str[x+1],this);
           }
        }
    }
   
    /**
     * two tables are considered equal if they belong to the same database and share the same name
     * the tables are considered to be on the same mysql server since two different servers can have the same databases
     * and the same tables
     */
    @Override
    public boolean equals(Object obj){
       if(obj instanceof Table){
          Table tb=(Table)obj;
             if(this.dbName.equals(tb.getDatabaseName()) && this.name.equals(tb.getTableName())){
                 return true;
             }
             return false;
       }  
       return false;
    }
 
 /**
  *generates a unique hash code for each object of type table
  */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 43 * hash + (this.dbName != null ? this.dbName.hashCode() : 0);
        return hash;
    }
    
    
    /**
     * this method returns a string representation of a table object
     * the format is Table[DatabaseName.TableName]
     */
    
    @Override
    public String toString(){
        return "Table["+this.dbName+"."+this.name+"]"; 
    }
    
    /**
     * this method returns an arraylist containing the names of the columns in this table
     * the sql query executed is 
     * <code>
     * USE information_schema;
     * SELECT  table_schema, table_name, column_name FROM columns WHERE table_schema=dbName AND table_name=tableName;
     * </code>
     * @param tableName the name of the table to get column names
     * @param dbName the name of the database the table is found in.
     */
    
    public  static ArrayList getColumnNames(String tableName, String dbName){
       ArrayList name=new ArrayList();
        ResultSet set = Database.executeQuery("SELECT  table_schema, table_name, column_name FROM columns WHERE table_schema=? AND table_name=?", "information_schema",dbName,tableName);
         try{
          for(;set.next();){
              name.add(set.getString("column_name")); 
              }
              return name;
         }
         catch(SQLException e){
             return name;
         }
    }
    
    
    /**
     * this method returns an arraylist of the names of columns in the current table
     */
    public ArrayList getColumnNames(){
      return getColumnNames(this.name,this.dbName);  
    }
         
}
