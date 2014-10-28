
package com.quest.access.common.mysql;

/**
 *
 * @author constant oduol
 * @version 1.0(16/1/2012)
 */
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class defines methods for interacting with a mysql column, a column can be created by calling the column constructor
 * and supplying the name of the column, the table to create the column, the database to create the 
 * column and the datatype of the column. The column object is meant to emulate the mysql equivalent
 * column. Instances of columns can also be created by calling the Column.getExistingColumn() method,
 * this method returns instances of previously created columns 
 * @see com.qaccess.common.Table
 * @see com.qaccess.common.Database
 * @see #getExistingColumn(java.lang.String,java.lang.String,java.lang.String)
 * 
 */
public class Column implements java.io.Serializable {
    /*
     * this is the name of the column
     */
    private String name;
    /*
     * this is the datatype of the column
     */
    private String dataType;
    /*
     * the table the column is contained in 
     */
    private String tableName;
    /*
     * the database the column is contained in
     */
    private String dbName;
    
    /**
     * This constructor creates a column with the given name and datatype in the 
     * specified table and database, if the column exists it is not recreated
     * @param name the name of the column to be created
     * @param dataType the datatype of the column eg INT, VARCHAR, CHAR etc
     * @param table an object of type table representing the table the column is
     *              to be created in.
     */
    public Column(String name, String dataType, Table table){
        //call the other constructor
            this(name,dataType,table.getTableName(), table.getDatabaseName());   
    }
    
    /**
     * This constructor creates a column with the given name and datatype in the 
     * specified table and database, if the column exists it is not recreated
     * @param name the name of the column
     * @param dataType the datatype of the column eg. INT,CHAR etc
     * @param tableName the name of the table the column is to be created in
     * @param dbName the name of the database the column is to be created in
     */
    public Column(String name, String dataType, String tableName, String dbName){
        //Database.getDefaultConnection();
        this.name=name;
        this.dataType=dataType;
        this.tableName=tableName;
        this.dbName=dbName;
        Database.executeQuery("ALTER TABLE "+this.tableName+" ADD "+name+" "+dataType+" NOT NULL", this.dbName);

      }
   
    /**
     * privately constructs a column object to be used by
     * @see #getExistingColumn(java.lang.String,java.lang.String,java.lang.String)
     * @param name the name of the column
     * @param tableName the name of the table the column is contained in
     * @param dbName the name of the database the column is contained in
     */
    private Column(String name, String tableName, String dbName){
        this.name=name;
        this.tableName=tableName;
        this.dbName=dbName;
    
       }
   
    /**
     * This method returns the name of the column as specified
     * in the database
     */
    public String getColumnName(){
        return this.name;
    }
    
    /**
     * this method returns a string representation of the 
     * datatype of a column
     */
    public String getDataType(){
        return this.dataType;
    }
    
    /**
     * returns the name of the table this column is contained in
     */
    public String getTableName(){
        return this.tableName;
    }
   
    /**
     * returns the name of the database this column is contained in
     */
    public String getDatabaseName(){
        return this.dbName;
    }
    
    /**
     * sets the name of the column to the specified new name, this also changes
     * the name of the column in the database
     * @param newName the new name of the column
     */
    public void setColumnName(String newName){
        Database.executeQuery("ALTER TABLE "+this.tableName+" CHANGE "+this.name+" "+newName+" "+this.dataType+"", this.dbName);
        this.name=newName;
    }
    
    /**
     * this method changes the datatype of the current column to 
     * the specified datatype, the changes are made to the corresponding
     * column in the database
     * @param dataType the new datatype of the column
     */
    public void setDataType(String dataType){
        Database.executeQuery("ALTER TABLE "+this.tableName+" MODIFY "+this.name+" "+dataType+"", this.dbName);
         this.dataType=dataType;
    }
    
    /**
     * this method privately sets the datatype of a column
     */
    private void setType(String newType){
        this.dataType=newType;
    }
    
    
    /**
     * this method checks whether a specified value exists in the column specified by 
     * column name
     * @param columnName the name of the column we want to check if the value exists
     * @param tableName the name of the table the column is contained in
     * @param dbName the name of the database the column is contained in
     * @param value the value we want to know whether it exists
     * 
     */
    public static boolean ifValueExists(String columnName, String tableName, String dbName, String value){
        ResultSet set = Database.executeQuery("SELECT "+columnName+" FROM "+tableName+" WHERE "+columnName+"='"+value+"' ",dbName );
         try{
          while(set.next()){
             return set.getString(columnName)!=null ? true : false;
            }
           set.close();
         }
         catch(SQLException e){
             return false;
             
         }
         return false;
    }
    
    /**
     * this method updates a cell in the column with the specified new value from the previous old value
     * if the specified old value is not found in the column the update is never made
     * @param columnName the name of the column to update
     * @param tableName the name of the table the column is contained in
     * @param dbName the name of the database the column is contained in
     * @param newValue the value we want to set in the column where the old value was
     * @param cond this is the extra condition that defines which row we are updating
     *        eg UPDATE USERS SET USER_NAME='SAM' WHERE USER_NAME='JON' AND cond;
     *       cond can be USER_ID=2, etc;
     * @see #updateCell(java.lang.String, java.lang.String)
     */
    public static void updateCell(String columnName, String tableName, String dbName, String newValue, String cond){
       Database.executeQuery("UPDATE "+tableName+" SET "+columnName+"='"+newValue+"' WHERE "+cond+" ", dbName);
        
        
    }
    
    /**
     * this method updates a single cell in the column from the old value to 
     * the new value as specified by the condition
     * @param newValue the new value we want to set where the old value was
     * @param cond this is the sql condition that specifies which cell is to be updated
     *      the query executed is normally <code> UPDATE tableName SET columnName=newValue WHERE condition</code>
     *      the condition can be <code>Name=sam AND age=20 </code> or <code> Amount=2000</code>
     *      this condition is normally the sql written after the WHERE in normal sql syntax
     */
    
    public void updateCell(String newValue, String cond){
        updateCell(this.name, this.tableName, this.dbName, newValue,cond);
    }
   
    
    /**
     * this method inserts data into the column  specified by columnName by creating a new row in the table this column 
     * is the only one that has data inserted into it other columns have no data inserted into them
     * for example if a table has 3 columns and column 1 is the current column then column one has data inserted but
     * the other columns have null values inserted
     * @param columnName the name of the column we want to insert data
     * @param tableName the name of the table the column is found in
     * @param dbName the name of the database the column is found in
     * @param value the value to be inserted into the cell in the column
     * 
     */
    public static void insertIntoCell(String columnName, String tableName, String dbName, String value){
       Database.executeQuery("INSERT INTO "+tableName+"  ("+columnName+") VALUES('"+value+"') ", dbName);
        
        }
    /**
     * this method inserts data into a single cell in the current column
     * @see #insertIntoCell(java.lang.String, java.lang.String, java.lang.String, java.lang.String) 
     * @param value the value to be inserted into the cell in the column
     * 
     */
    public void insertIntoCell(String value){
        insertIntoCell(this.name, this.tableName, this.dbName, value);
    }
    
    /**
     * this method checks in the current column if the specified value exists
     * @param value the value we want to check in the column to ascertain its existence
     * @see #ifValueExists(java.lang.String, java.lang.String, java.lang.String, java.lang.String) 
     */
    public boolean ifValueExists(String value){
        return ifValueExists(this.name, this.tableName, this.dbName, value);
    }
    
    /**
     * this method checks whether the specified column exists
     * a check is made in the columns table in the information_schema database
     * to ascertain whether the column exists
     * @param name the name of the column
     * @param tableName the name of the table the column is contained in
     * @param dbName the name of the database the column is contained in
     */
    
    public static boolean ifColumnExists(String name, String tableName, String dbName){
       // Database.getDefaultConnection();
        ResultSet set = Database.executeQuery("SELECT table_schema, table_name, column_name "
                + "FROM columns WHERE table_schema=? AND "
                + "table_name=? AND column_name=?  ", "information_schema", dbName,tableName,name);
         try{
           while(set.next()){
               if(set.getString("column_name")!=null){
                   return true;
               }
               set.close();
           }
           return false;
         }
        catch(SQLException e){
            return false;
        } 
        catch(Exception e){
            return false;
        }
    }
    
    /**
     * this method checks whether the specified column exists
     * @param col the column we want to ascertain its existence
     * @see #ifColumnExists(java.lang.String,java.lang.String,java.lang.String)
     */
    public static boolean ifColumnExists(Column col){
        return ifColumnExists(col.getColumnName(),col.getTableName(),col.getDatabaseName());
    }
    
    /**
     * this method returns an instance of an existing column without trying to recreate it,
     * an instance to a column that was previously created can be obtained in this way
     * @param  name the name of the column we want an instance of
     * @param tableName the name of the table where the column is found
     * @param dbName the name of database where the column is found
     * @throws NonExistentColumnException 
     */
    
    public static Column getExistingColumn(String name, String tableName, String dbName) throws NonExistentColumnException{
    if(ifColumnExists(name,tableName,dbName)){
      String dataType="";
        String charLength="";
            //from here we know the column exists
            //get the properties of the column from the information_schema database
        ResultSet set = Database.executeQuery("SELECT table_schema, table_name, column_name, data_type, character_maximum_length FROM columns WHERE table_schema=? AND table_name=? AND column_name=?", "information_schema",dbName,tableName,name);
          try{         
             while(set.next()){
                 dataType=set.getString("data_type");
                   charLength=set.getString("character_maximum_length");
                     dataType= charLength==null ? dataType : dataType+"("+charLength+")";
                    }
                     set.close(); 
                       }
                      catch(SQLException e){
                        }
                Column col=new Column(name,tableName,dbName);
            col.setType(dataType);
         return col;
       }
             
     else{
         throw new NonExistentColumnException();
            }
    }
   
    /**
     * this method is used to drop or delete the specified column completely
     * @param name the name of the column to be dropped or deleted
     * @param tableName the name of the table the column is contained in
     * @param dbName the name of the database the column is contained in
     */
    
    public static void dropColumn(String name, String tableName, String dbName){
        Database.executeQuery("ALTER TABLE "+tableName+" DROP "+name, dbName);
    }
    
    /**
     * this method drops the current column and any further reference to the column
     * generates exceptions
     * @see #dropColumn(java.lang.String, java.lang.String, java.lang.String) 
     */
    
    public void dropColumn(){
        dropColumn(this.name, this.tableName, this.dbName);
        
    }
    
    
    /**
     * This method executes the specified SQL functions and returns
     * the value in a string format
     * They include COUNT, MIN, AVG, MAX etc
     * NB functions which do not relate to columns will not work eg AES_ENCRYPT,
     * the sql query executed is <code>Select function(columnName) from tableName</code>
     * @return the value returned is a string representation of the actual value
     *         returned by the database server 
     */
    
    public String executeFunction(String function){
       return executeFunction(this.name, this.tableName, this.dbName, function);
    }
    
    /**
     * this method executes the specified function on the specified column
     * 
     * @see #executeFunction(java.lang.String)
     * @param columnName the name of the column where the function is executed
     * @param tableName the name of the table the column is contained in
     * @param dbName the name of the database the column is found
     * @param function the name of the function to be executed eg. COUNT, AVG, MIN etc
     */
    
    public static String executeFunction(String columnName, String tableName, String dbName, String function){
       String result="";
        ResultSet set = Database.executeQuery("SELECT "+function+"("+columnName+") FROM "+tableName+"", dbName);
          try{
           while(set.next()){
             result=set.getString(""+function+"("+columnName+")");  
               return result;
             }
             set.close();
            }
        
          catch(SQLException e){
          }
        return result;
    }
    
    /**
     * two columns are equal if they belong to the same database
     * and belong to the same table and share the same name
     * theoretically this is not supposed to be true since a table cannot
     * contain two columns with the same name
     */
    @Override
    public boolean equals(Object obj){
        // we only work with column objects
         if(obj instanceof Column){
            Column col=(Column)obj;
             if(col.getColumnName().equals(this.name) && col.getTableName().equals(this.tableName) && col.getDatabaseName().equals(this.dbName)){
                  return true;
             }
             else{
                return false; 
             }
         }
         return false;
    }
   
    
    /**
     * @return the hashcode of a column object
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 23 * hash + (this.tableName != null ? this.tableName.hashCode() : 0);
        hash = 23 * hash + (this.dbName != null ? this.dbName.hashCode() : 0);
        return hash;
    }
    
    /**
     * this method returns a string representation of a column object
     * the format in which the string is returned is
     * "Column[DatabaseName.tableName.columnName]"
     */
    @Override
    public String toString(){
        return "Column["+this.dbName+"."+this.tableName+"."+this.name+"]";
        
    }
       
    
}
