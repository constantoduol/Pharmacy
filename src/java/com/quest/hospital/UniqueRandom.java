
package com.quest.hospital;

/**
 *
 * @author constant oduol
 * @version 1.0(11/4/12)
 */

/**
 * This class is used to generate random numbers of
 * a given digit length, the numbers are returned in a string format
 * and to use them as numbers then <code>Integer.parseInt()</code> can be
 * used to convert the numbers to integers. 
 * The uniqueRandom class uses the Math.random() method in the Math class.
 */
public class UniqueRandom implements java.io.Serializable {
    private int digitLength;
    
    private static final String [] chars=new String []{"a","b","c","d","e","f",
                                                       "g","h","i","j","k","l",
                                                       "m","n","o","p","q","r",
                                                       "s","t","u","v","w","x",
                                                       "y","z"}; 
    
    public UniqueRandom(int length){
      this.digitLength=length;
    }
    
    /**
     * This method generates unique random digits each time its called, 
     * the length of the string is constant and is determined by the length
     * property passed to the unique random constructor, the string returned can be
     * parsed using <code>Integer.parseInt()</code> or <code>Float.parseFloat()</code> methods to convert it to a number;
     * @return a string containing digits specified by the length property
     *         
     */
    public String nextRandom(){
      StringBuilder buffer=new StringBuilder();
       int count=0;
        while(count<this.digitLength){
            buffer.append(getRandomDigit());
            count++;
        }
        return buffer.toString();
    }
    /**
     * this method generates a random string containing both digits and
     * letters, the length of the string is specified in the UniqueRandom constructor
     * @return a string of random letters and digits
     */
    public String nextMixedRandom(){
       StringBuilder buffer=new StringBuilder();
        int count=0;
         while(count<this.digitLength){
          int decision=getRandomDigit();
          if(decision>5){
              buffer.append(getRandomDigit()); 
              count++;
           }
          else{
            buffer.append(getRandomLetter());
            count++;
          }
         }
        return buffer.toString();
        
    }
    
    private int getRandomDigit(){
       int random = (int)(10*Math.random());
       return random;
    }
    
    private String getRandomLetter(){
       int random = (int)(25*Math.random());
        return chars[random]; 
    }
}
