package com.quest.access.common.mysql;

/**
 *
 * @author constant oduol
 * @version 1.0(10/1/2012)
 */


/**
 * This file defines the privileges that a mysql user can have
 * these are constants that can be used more easily to define the privileges
 * 
privileges in mysql

ALL- grants all the privileges to the user
SELECT - grants select privilege to a table to a user
USAGE - grants no privilege to the user
ALTER -enables use of alter table
CREATE- enables table and database create
CREATE ROUTINE -enables routine creation
CREATE TABLESPACE-Enable tablespaces and log file groups to be created, altered, or dropped
CREATE TEMPORARY TABLES-Enable use of CREATE TEMPORARY TABLE
CREATE USER -Enable use of CREATE USER, DROP USER, RENAME USER, and REVOKE ALL PRIVILEGES
CREATE VIEW -Enable views to be created or altered
DELETE -Enable use of DELETE
DROP -Enable databases, tables, and views to be dropped
EVENT-Enable use of events for the Event Scheduler
EXECUTE-Enable the user to execute stored routines
FILE-Enable the user to cause the server to read or write files
GRANT OPTION-Enable privileges to be granted to or removed from other accounts
INDEX-Enable indexes to be created or dropped
INSERT-Enable use of INSERT
LOCK TABLES-Enable use of LOCK TABLES on tables for which you have the SELECT privilege
PROCESS-Enable the user to see all processes with SHOW PROCESSLIST
PROXY-Enable user proxying
RELOAD-Enable use of FLUSH operations
REPLICATION CLIENT- Enable the user to ask where master or slave servers are
REPLICATION SLAVE-Enable replication slaves to read binary log events from the master
SELECT-Enable use of SELECT
SHOW DATABASES-Enable SHOW DATABASES to show all databases
SHOW VIEW-Enable use of SHOW CREATE VIEW
SHUTDOWN-Enable use of mysqladmin shutdown
SUPER-Enable use of other administrative operations such as CHANGE MASTER TO, KILL, PURGE BINARY LOGS, SET GLOBAL, and mysqladmin debug command
TRIGGER-Enable trigger operations
UPDATE-Enable use of UPDATE
USAGE-Synonym for “no privileges”

 */



public interface Privilege extends java.io.Serializable {
public static final String  ALL="ALL";  
public static final String  SELECT="SELECT";
public static final String  USAGE="USAGE"; 
public static final String  ALTER="ALTER";
public static final String  CREATE="CREATE";
public static final String  CREATE_ROUTINE="CREATE ROUTINE";
public static final String  CREATE_TABLESPACE="CREATE TABLESPACE";
public static final String  CREATE_TEMPORARY_TABLES="CREATE TEMPORARY TABLES";
public static final String  CREATE_USER="CREATE USER";
public static final String  CREATE_VIEW="CREATE VIEW";
public static final String  DELETE="DELETE";
public static final String  DROP="DROP";      
public static final String  EVENT="EVENT";      
public static final String  EXECUTE="EXECUTE";
public static final String  FILE="FILE";
public static final String  GRANT_OPTION="GRANT OPTION";
public static final String  INDEX="INDEX";
public static final String  INSERT="INSERT";
public static final String  LOCK_TABLES="LOCK TABLES";      
public static final String  PROCESS="PROCESS";
public static final String  PROXY="PROXY";     
public static final String  RELOAD="RELOAD";      
public static final String  REPLICATION_CLIENT="REPLICATION CLIENT";   
public static final String  REPLICATION_SLAVE="REPLICATION SLAVE";
public static final String  SHOW_DATABASES="SHOW DATABASES";
public static final String  SHOW_VIEW="SHOW VIEW";     
public static final String  SHUTDOWN="SHUTDOWN";   
public static final String  SUPER="SUPER";
public static final String  TRIGGER="TRIGGER";
public static final String  UPDATE="UPDATE";

         
}
