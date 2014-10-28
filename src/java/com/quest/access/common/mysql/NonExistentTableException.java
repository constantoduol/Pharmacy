
package com.quest.access.common.mysql;

/**
 *
 * @author constant oduol
 * @version 1.0(13/1/2012)
 */

/**
 * This file defines an exception
 * a non existent table exception is thrown when a call to
 * the method <code>com.qaccess.common.Table.getExistingTable(java.lang.String, java.lang.String)</code>
 * is made and the specified table does not exist in the specified database.
 * This exception has to be declared as being thrown by a method or the calling method
 * to be in a try and catch block
 * eg 
 *  <code>
 *    try {
 *        Table table= com.qaccess.common.Table.getExistingTable(table1, test);
 *     }
 *   catch(NonExistentTableException e){
 *      //do something
 *    }
 * </code>
 * or
 * <code>
 *   public void methodName() throws NonExistentTableException{
 *     
 *     }
 * </code>
 */
public class NonExistentTableException extends Exception {
    public NonExistentTableException(){
        super("The specified table does not exist");
    }
}
