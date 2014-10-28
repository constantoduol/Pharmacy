
package com.quest.hospital;


import java.util.HashMap;

/**
 *
 * @author constant oduol
 * @version 1.0(25/4/12)
 */

/**
 * <p>
 * This file defines a session mechanism for tracking users who are logged in
 * in a specific server, a session object has a unique random id associated with it
 * every time a new session is created a unique id is assigned to it, this id can be
 * obtained by calling the instance method getSessionId().
 * </p>
 * <p>
 * Attributes can be added to a users session using the setAttribute instance method and specifying 
 * an attribute name and an object attribute to be added to a user's session. Attributes can then be 
 * obtained through getAttribute instance method, this is done by specifying the name of the attribute 
 * to be obtained, if the specified attribute does not exist then null is returned.
 * </p>
 * 
 */
public class Session implements java.io.Serializable {
    /*
     * the hash map stores the various attributes in the session
     */
    private HashMap<String,Object> attributes;
    /*
     * this is the unique id of this session
     */
    private String sessionId;
   
    /**
     * creates a new session
     */
    public Session(){
      this.attributes=new HashMap();
      this.sessionId=assignSessionId();
    }
    /**
     * this method adds an attribute to the session
     * @param name the name that is used in storing the attribute, this is the same name 
     * used when retrieving it
     * @param attr the attribute we want to assign in this session
     */
    public void setAttribute(String name, Object attr){
        attributes.put(name, attr);
    }
    /**
     * this method obtains an attribute from the session object
     * @param name the name of the attribute we want to retrieve
     * @return the attribute specified by the attribute name
     */
    public Object getAttribute(String name){
       return attributes.get(name);
    }
    
    /**
     * This method is called to remove a previously bound attribute
     * from the user's session
     */
    public void removeAttribute(String name){
        attributes.remove(name);
    }
    /**
     * this method destroys the session object 
     */
    public void invalidateSession(){
        attributes=null;
    }
    /**
     * 
     * @return the unique id representing this session
     */
    public String getSessionId(){
       return this.sessionId; 
    }
    /**
     * this returns a string representation of a session
     * in the form Session[sessionId]
     */
    @Override
    public String toString(){
        return attributes.toString();
    }
    
    /**
     * 
     * @return a hash map containing all the attributes in this session object
     */
    public HashMap getAttributes(){
        return this.attributes;
    }
    
    // assign each session a unique id
    private String assignSessionId(){
     UniqueRandom ur=new UniqueRandom(30);
       return ur.nextMixedRandom();
    }
}
