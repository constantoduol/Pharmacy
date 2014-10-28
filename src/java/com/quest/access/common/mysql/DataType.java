
package com.quest.access.common.mysql;

/**
 *
 * @author constant oduol
 * @version 1.0(12/1/2012)
 */




/**
 * This file defines constants representing the mysql data types
 * <p>BIT[(M)]-A bit-field type. M indicates the number of bits per value, from 1 to 64. The default is 1 if M is omitted.</p>
 * 
 * <p>TINYINT[(M)] [UNSIGNED] [ZEROFILL]- A very small integer. The signed range is -128 to 127. The unsigned range is 0 to 255.</p>
 * 
 * <p>BOOL or BOOLEAN-These types are synonyms for TINYINT(1). A value of zero is considered false. Nonzero values are considered true:</p> 
 * 
 * <p>SMALLINT[(M)] [UNSIGNED] [ZEROFILL] -A small integer. The signed range is -32768 to 32767. The unsigned range is 0 to 65535.</p>
 * 
 * <p>MEDIUMINT[(M)] [UNSIGNED] [ZEROFILL]-A medium-sized integer. The signed range is -8388608 to 8388607. The unsigned range is 0 to 16777215</p>
 * 
 * <p>INT[(M)] [UNSIGNED] [ZEROFILL]-A normal-size integer. The signed range is -2147483648 to 2147483647. The unsigned range is 0 to 4294967295.</p>
 * 
 * <p>INTEGER[(M)] [UNSIGNED] [ZEROFILL]-This type is a synonym for INT.</p>
 * 
 * <p>BIGINT[(M)] [UNSIGNED] [ZEROFILL]-A large integer. The signed range is -9223372036854775808 to 9223372036854775807. The unsigned range is 0 to 18446744073709551615</p>
 * 
 * <p>SERIAL-BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE</p>
 * 
 * <p> FLOAT[(M,D)] [UNSIGNED] [ZEROFILL]-A small (single-precision) floating-point number. Permissible values are -3.402823466E+38 to -1.175494351E-38, 0, and 1.175494351E-38 to 3.402823466E+38.
 * These are the theoretical limits, based on the IEEE standard. The actual range might be slightly smaller depending on your hardware or operating system.
 * M is the total number of digits and D is the number of digits following the decimal point. If M and D are omitted, values are stored to the limits permitted by the hardware.
 * A single-precision floating-point number is accurate to approximately 7 decimal places</p>
 * 
 * <p>DOUBLE[(M,D)] [UNSIGNED] [ZEROFILL]-A normal-size (double-precision) floating-point number. Permissible values are -1.7976931348623157E+308 to -2.2250738585072014E-308, 0, and 2.2250738585072014E-308 to 1.7976931348623157E+308.
 * These are the theoretical limits, based on the IEEE standard. The actual range might be slightly smaller depending on your hardware or operating system.
 * M is the total number of digits and D is the number of digits following the decimal point. If M and D are omitted, values are stored to the limits permitted by the hardware. 
 * A double-precision floating-point number is accurate to approximately 15 decimal places.UNSIGNED, if specified, disallows negative values.</p>
 * 
 * <p>FLOAT(p) [UNSIGNED] [ZEROFILL]-A floating-point number. p represents the precision in bits, but MySQL uses this value only to determine whether to use FLOAT or DOUBLE for the resulting data type.
 * If p is from 0 to 24, the data type becomes FLOAT with no M or D values. If p is from 25 to 53, the data type becomes DOUBLE with no M or D values.
 * The range of the resulting column is the same as for the single-precision FLOAT or double-precision DOUBLE data types described earlier in this section.FLOAT(p) syntax is provided for ODBC compatibility.</p> 
 * 
 * <p>DECIMAL[(M[,D])] [UNSIGNED] [ZEROFILL]-A packed “exact” fixed-point number. M is the total number of digits (the precision) and D is the number of digits after the decimal point (the scale). 
 * The decimal point and (for negative numbers) the “-” sign are not counted in M. If D is 0, values have no decimal point or fractional part. The maximum number of digits (M) for DECIMAL is 65.
 * The maximum number of supported decimals (D) is 30. If D is omitted, the default is 0. If M is omitted, the default is 10.
 * UNSIGNED, if specified, disallows negative values.All basic calculations (+, -, *, /) with DECIMAL columns are done with a precision of 65 digits.</p>
 * 
 * <p>DATE-A date. The supported range is '1000-01-01' to '9999-12-31'. MySQL displays DATE values in 'YYYY-MM-DD' format, but permits assignment of values to DATE columns using either strings or numbers.</p>
 * 
 * <p>DATETIME-A date and time combination. The supported range is '1000-01-01 00:00:00' to '9999-12-31 23:59:59'. MySQL displays DATETIME values in 'YYYY-MM-DD HH:MM:SS' format, 
 * but permits assignment of values to DATETIME columns using either strings or numbers.</p>
 * 
 * <p>TIMESTAMP-A timestamp. The range is '1970-01-01 00:00:01' UTC to '2038-01-19 03:14:07' UTC. TIMESTAMP values are stored as the number of seconds since the epoch ('1970-01-01 00:00:00' UTC). 
 * A TIMESTAMP cannot represent the value '1970-01-01 00:00:00' because that is equivalent to 0 seconds from the epoch and the value 0 is reserved for representing '0000-00-00 00:00:00', the “zero” TIMESTAMP value.
 * A TIMESTAMP column is useful for recording the date and time of an INSERT or UPDATE operation. By default, the first TIMESTAMP column in a table is automatically set to the date and time of the most recent operation if you do not assign it a value yourself. 
 * You can also set any TIMESTAMP column to the current date and time by assigning it a NULL value. Variations on automatic initialization and update properties are described in Section 10.3.1.1, “TIMESTAMP Properties”.
 * A TIMESTAMP value is returned as a string in the format 'YYYY-MM-DD HH:MM:SS' with a display width fixed at 19 characters. To obtain the value as a number, you should add +0 to the timestamp column.</p>
 * 
 * <p>TIME-A time. The range is '-838:59:59' to '838:59:59'. MySQL displays TIME values in 'HH:MM:SS' format, but permits assignment of values to TIME columns using either strings or numbers.</p>
 * 
 * <p>YEAR[(2|4)]-A year in two-digit or four-digit format. The default is four-digit format. In four-digit format, the permissible values are 1901 to 2155, and 0000. In two-digit format, the permissible values are 70 to 69, representing years from 1970 to 2069. 
 * MySQL displays YEAR values in YYYY format, but permits assignment of values to YEAR columns using either strings or numbers</p>
 * 
 * <p>CHAR[(M)] [CHARACTER SET charset_name] [COLLATE collation_name]-A fixed-length string that is always right-padded with spaces to the specified length when stored. M represents the column length in characters. The range of M is 0 to 255. If M is omitted, the length is 1.</p>
 * VARCHAR(M) [CHARACTER SET charset_name] [COLLATE collation_name]-A variable-length string. M represents the maximum column length in characters. The range of M is 0 to 65,535. The effective maximum length of a VARCHAR is subject to the maximum row size (65,535 bytes,
 * which is shared among all columns) and the character set used. For example, utf8 characters can require up to three bytes per character, so a VARCHAR column that uses the utf8 character set can be declared to be a maximum of 21,844 characters</p>
 *
 * <p>BINARY(M)-The BINARY type is similar to the CHAR type, but stores binary byte strings rather than nonbinary character strings. M represents the column length in bytes.</p>
 *
 * <p> VARBINARY(M)-The VARBINARY type is similar to the VARCHAR type, but stores binary byte strings rather than nonbinary character strings. M represents the maximum column length in bytes</p>
 *
 * <p>TINYBLOB-A BLOB column with a maximum length of 255 (2^8 – 1) bytes. Each TINYBLOB value is stored using a one-byte length prefix that indicates the number of bytes in the value</p>
 *
 * <p>TINYTEXT [CHARACTER SET charset_name] [COLLATE collation_name]-A TEXT column with a maximum length of 255 (2^8 – 1) characters. The effective maximum length is less if the value contains multi-byte characters. 
 * Each TINYTEXT value is stored using a one-byte length prefix that indicates the number of bytes in the value</p>
 * 
 * <p>BLOB[(M)]-A BLOB column with a maximum length of 65,535 (2^16 – 1) bytes. Each BLOB value is stored using a two-byte length prefix that indicates the number of bytes in the value.An optional length M can be given for this type.
 * If this is done, MySQL creates the column as the smallest BLOB type large enough to hold values M bytes long</p>
 * 
 * <p>TEXT[(M)] [CHARACTER SET charset_name] [COLLATE collation_name]-A TEXT column with a maximum length of 65,535 (2^16 – 1) characters. The effective maximum length is less if the value contains multi-byte characters.
 * Each TEXT value is stored using a two-byte length prefix that indicates the number of bytes in the value.
 * An optional length M can be given for this type. If this is done, MySQL creates the column as the smallest TEXT type large enough to hold values M characters long</p>
 * 
 * <p>MEDIUMBLOB-A BLOB column with a maximum length of 16,777,215 (2^24 – 1) bytes. Each MEDIUMBLOB value is stored using a three-byte length prefix that indicates the number of bytes in the value.</p>
 * 
 * <p>MEDIUMTEXT [CHARACTER SET charset_name] [COLLATE collation_name]-A TEXT column with a maximum length of 16,777,215 (224 – 1) characters. The effective maximum length is less if the value contains multi-byte characters. 
 *  Each MEDIUMTEXT value is stored using a three-byte length prefix that indicates the number of bytes in the value.</p>
 *
 * <p> LONGBLOB-A BLOB column with a maximum length of 4,294,967,295 or 4GB (232 – 1) bytes. The effective maximum length of LONGBLOB columns depends on the configured maximum packet size in the client/server protocol and available memory. 
 * Each LONGBLOB value is stored using a four-byte length prefix that indicates the number of bytes in the value</p>
 * 
 * <p>LONGTEXT [CHARACTER SET charset_name] [COLLATE collation_name]-A TEXT column with a maximum length of 4,294,967,295 or 4GB (232 – 1) characters. The effective maximum length is less if the value contains multi-byte characters. 
 * The effective maximum length of LONGTEXT columns also depends on the configured maximum packet size in the client/server protocol and available memory. Each LONGTEXT value is stored using a four-byte length prefix that indicates the number of bytes in the value.</p>
 * 
 * <p>ENUM('value1','value2',...) [CHARACTER SET charset_name] [COLLATE collation_name]-An enumeration. A string object that can have only one value, chosen from the list of values 'value1', 'value2', ..., NULL or the special '' error value. 
 * An ENUM column can have a maximum of 65,535 distinct values. ENUM values are represented internally as integers.</p>
 * 
 * <p>SET('value1','value2',...) [CHARACTER SET charset_name] [COLLATE collation_name]-A set. A string object that can have zero or more values, each of which must be chosen from the list of values 'value1', 'value2', ... A SET column can have a maximum of 64 members. 
 * SET values are represented internally as integers</p>
 * 
 * 
 */
public interface DataType extends java.io.Serializable{
    public static final String BIT="BIT";
    public static final String TINYINT="TINYINT";
    public static final String BOOL="BOOL";
    public static final String SMALLINT="SMALLINT";
    public static final String MEDIUMINT="MEDIUMINT";
    public static final String INT="INT";
    public static final String BIGINT="BIGINT";
    public static final String SERIAL="SERIAL";
    public static final String FLOAT="FLOAT";
    public static final String DOUBLE="DOUBLE";
    public static final String DECIMAL="DECIMAL";
    public static final String DATE="DATE";
    public static final String DATETIME="DATETIME";
    public static final String TIMESTAMP="TIMESTAMP";
    public static final String TIME="TIME";
    public static final String YEAR="YEAR";
    public static final String CHAR="CHAR";
    public static final String VARCHAR="VARCHAR";
    public static final String BINARY="BINARY";
    public static final String VARBINARY="VARBINARY";
    public static final String TINYBLOB="TINYBLOB";
    public static final String TINYTEXT="TINYTEXT";
    public static final String BLOB="BLOB";
    public static final String TEXT="TEXT";
    public static final String MEDIUMBLOB="MEDIUMBLOB";
    public static final String MEDIUMTEXT="MEDIUMTEXT";
    public static final String LONGBLOB="LONGBLOB";
    public static final String LONGTEXT="LONGTEXT";
    public static final String ENUM="ENUM";
    public static final String SET="SET";
    
}
