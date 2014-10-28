
package com.quest.hospital;

import com.quest.access.common.mysql.Database;
import com.quest.access.common.mysql.NoDefaultAccountException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author constant oduol
 * @version 1.0(4/1/2012)
 */

/**
 * this class has static methods for launching a server
 * database connections and server configuration details are handled by
 * this class
 */
public abstract class Launcher implements java.io.Serializable {     
     public static void setDatabaseConnection(String userName, String host ,String password){
        Database.setDefaultConnection(userName, host, password);
     }
     
     
     
     /**
      * this method reads an xml file containing a servers initialization details
      * @param file the xml file containing the initialization details such as
      *             the port the server is listening on, database password and other
      *              details
      * @return  a hashmap containing the initialization details
      */
     public static Properties readConfigFile(File file){
        Document doc;
        Properties props=null;
        try{
            DocumentBuilder docReader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = docReader.parse(file);
            Element root=doc.getDocumentElement();
            if(!root.getNodeName().toLowerCase().equals("config")){ 
                  throw new Exception("File is not an xml file or the format is invalid");  
               }
            NodeList configs=root.getChildNodes();
            props=new Properties();
            for(int x=0; x<configs.getLength(); x++){
                if(configs.item(x) instanceof Element){
                    Element element = (Element)configs.item(x);
                    if(element.getTagName().equals("database-host")){
                        String server=element.getAttribute("value");
                        if(server.isEmpty()|| server == null){
                              server="localhost";
                          }
                         props.put("database-host", server);
                       }
                    else if(element.getTagName().equals("database-name")){
                        String dbName=element.getAttribute("value");
                        if(dbName.isEmpty()|| dbName == null){
                              dbName="hospital";
                          }
                         props.put("database-name", dbName);
                       }
                       else if(element.getTagName().equals("database-username")){
                          String name = element.getAttribute("value");
                          if(name.isEmpty()|| name == null){
                              name="root";
                          }
                         props.put("database-username",name);
                       }
                       else if(element.getTagName().equals("database-password")){
                          props.put("database-password",element.getAttribute("value"));  
                       }
                 }
             }
              
           return props;
         }
        catch(Exception e){
            return props;
        }
        
     }
     
     
}
