
package com.quest.access.common.mysql;

/**
 *
 * @author constant oduol
 * @version 1.0(17/1/2012)
 */

/**
 * This file defines an exception
 * A non existent column exception is thrown when a
 * call is made to the method<code>com.qaccess.common.getExistingColumn()</code>
 * and the specified parameters to the method call define a non existent column
 * this exception should be caught or program execution is terminated
 */
public class NonExistentColumnException extends Exception  {
      public NonExistentColumnException(){
           super("The specified column does not exist");
      }
}
