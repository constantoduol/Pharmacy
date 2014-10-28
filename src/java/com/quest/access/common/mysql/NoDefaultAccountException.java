
package com.quest.access.common.mysql;

/**
 *
 * @author constant oduol
 * @version 1.0(6/1/2012)
 */

/**
 * This file defines an exception
 * a no default account exception is thrown if
 * no default connection account has been specified to be used to 
 * connect to the mysql server, this exception requires mandatory handling
 */
public class NoDefaultAccountException extends Exception  {
      public NoDefaultAccountException(){
          super("No default user account specified");
      }
      
    
}
