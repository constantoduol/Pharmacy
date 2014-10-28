/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quest.hospital;

import com.quest.access.common.mysql.Database;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author connie
 */
public class Test {
    public static void main(String [] args){
          try {
            Database.setDefaultConnection("root", "localhost", "");
            Database db=Database.getExistingDatabase("pharmacy");
            File file=new File("drugs.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            UniqueRandom rand=new UniqueRandom(10);
            while ((line = br.readLine()) != null) {
               // insert the data into the database
                String drugId=rand.nextRandom();
                db.doInsert("DRUG_DATA", new String[]{drugId,line.trim(),"0","","0","0","0","!NOW()","!NOW()"});
            }
            br.close();
        } catch (Exception ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
