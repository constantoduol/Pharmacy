
package com.quest.access.common.mysql;

/**
 *
 * @author constant oduol
 * @version 1.0(11/2/2012)
 */

/*
 * This file defines an exception
 * a non existent database exception is thrown when an attempt is made
 * to get an instance of a database that is non existent.
 */
public class NonExistentDatabaseException extends Exception {
     public NonExistentDatabaseException(){
         super("The specified database does not exist");
     }
}
