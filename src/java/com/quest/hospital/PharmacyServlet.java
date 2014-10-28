package com.quest.hospital;

import com.quest.access.common.mysql.DataType;
import com.quest.access.common.mysql.Database;
import com.quest.access.common.mysql.Table;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author connie
 */

public class PharmacyServlet extends HttpServlet {
   
     private static Database db; // an instance of the database
     private static ConcurrentHashMap sessions=new ConcurrentHashMap();
     
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String json = request.getParameter("json");
            JSONObject obj=new JSONObject(json);
            JSONObject headers = obj.optJSONObject("request_header");
            String msg=headers.optString("request_msg");
            JSONObject req_obj=(JSONObject)obj.optJSONObject("request_object");
            if("login".equals(msg)){ 
                 doLogin(req_obj, response,msg);  
                 return;
             }  
            else if("change_pass".equals(msg)){
               changePass(req_obj, response, msg);
               return;
            }
            
            String[] currentUser = ensureIntegrity(headers,response, request);
            if(currentUser==null){
                return;
            }
            if("create_user".equals(msg)){
              createUser(req_obj, response, msg); 
            }
            else if("logout".equals(msg)){
              doLogOut(currentUser, response);
            }
            else if("edit_user".equals(msg)){
              editUser(req_obj, response, msg);
            }
            else if("delete_user".equals(msg)){
              String id=req_obj.optString("id");
              if(id.equals("12345678")){
                 return; 
              }
              deleteObject(response, msg,id,"USERS", "ID");
            }
            else if("auto_suggest".equals(msg)){
                autoSuggest(req_obj, response, msg);
            }
            else if("complete_auto_suggest".equals(msg)){
                completeAutoSuggest(req_obj, response, msg);
            }
            else if("delete_drug".equals(msg)){
              String id=req_obj.optString("drug_id");
              deleteObject(response, msg,id,"DRUG_DATA", "ID");
            }
            else if("all_users".equals(msg)){
               String sql="SELECT * FROM USERS";
               allObjects(sql, msg, response);
            }
            else if("create_drug".equals(msg)){
                createDrug(req_obj, response, msg);
            }
            else if("edit_drug".equals(msg)){
              editDrug(req_obj, response, msg);
            }
            else if("all_drugs".equals(msg)){
               String sql="SELECT * FROM DRUG_DATA";
               allObjects(sql, msg, response);
            }
            else if("stock_history".equals(msg)){
                openStockHistory(req_obj, response, msg);
            }
            else if("commit_transaction".equals(msg)){
                transact(req_obj, response, msg,"0");
            }
            else if("drug_reminder".equals(msg)){
               drugReminder(req_obj, response, msg);
            }
             else if("expiry_reminder".equals(msg)){
               expiryReminder(req_obj, response, msg);
            }
            else if("undo_previous_transaction".equals(msg)){
               transact(req_obj, response, msg,"1");
            }
                
        } catch (JSONException ex) {
            Logger.getLogger(PharmacyServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void doLogin(JSONObject obj, HttpServletResponse response,String msg){
        //when a user logs in remember his role, username and random id
        //send this to the client
         String name=obj.optString("username");
         String pass=obj.optString("password");
         JSONObject object=new JSONObject();
         boolean auth=authenticateUser(name, pass);
         try {
            if(auth){
                String role=Database.getValue("ROLE","USERS","USER_NAME",name,db);
                String changePass=Database.getValue("CHANGE_PASS","USERS","USER_NAME",name, db);
                if(changePass.equals("1")){
                  object.put("request_msg","redirect");
                  object.put("url","changePass.html");
                  response.getWriter().print(object); 
                  return;
                }
                Session ses=new Session(); 
                ses.setAttribute("role", role);
                sessions.put(name,ses);
                response.addCookie(new Cookie("user", name));
                response.addCookie(new Cookie("rand", ses.getSessionId()));
                response.addCookie(new Cookie("role", role));
               if(role.equals("pharmacist")){
                   object.put("request_msg","redirect");
                   object.put("url","pharm.html");  
                }
                else  if(role.equals("admin")){
                  object.put("request_msg","redirect");
                  object.put("url","admin.html");
                }
               response.getWriter().print(object);
          }
          else{ 
              object.put("request_msg", msg);
              object.put("response",auth );
              response.getWriter().print(object);
            }
                
         } catch (Exception ex) {
                Logger.getLogger(PharmacyServlet.class.getName()).log(Level.SEVERE, null, ex);
         }
        
    }
    
    
    private void doLogOut(String[] currentUser, HttpServletResponse response){
        try {
            sessions.remove(currentUser[0]);
            JSONObject object=new JSONObject(); 
             object.put("request_msg", "redirect");
             object.put("url","index.html");
             response.getWriter().print(object);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(PharmacyServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void changePass(JSONObject obj, HttpServletResponse response,String msg){
        try {
            String name=obj.optString("username");
            String oldPass=obj.optString("old_password");
            String newPass=obj.optString("new_password");
            JSONObject object=new JSONObject();
            String old_pass=Security.toBase64(Security.makePasswordDigest(name, oldPass.toCharArray()));
            String pass_stored=Database.getValue("PASS_WORD","USERS","USER_NAME",name,db);
            if(old_pass.equals(pass_stored)){
                 byte[] bytes=Security.makePasswordDigest(name,newPass.toCharArray());
                 String passw=Security.toBase64(bytes);
                 Database.executeQuery("UPDATE USERS SET PASS_WORD=? ,CHANGE_PASS=? WHERE USER_NAME=?",db.getDatabaseName(),passw,"0",name);
                 object.put("response",true);
                 object.put("request_msg","redirect");
                 object.put("url","index.html");
                 response.getWriter().print(object);
            }
            else{
                object.put("response",false);  
                response.getWriter().print(object);
             }
        } catch (Exception ex) {
            Logger.getLogger(PharmacyServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void createUser(JSONObject obj, HttpServletResponse response,String msg){
        try {
            String name=obj.optString("username");
            JSONObject object=new JSONObject();
            JSONObject details=new JSONObject();
            if(Database.ifValueExists(name,"USERS","USER_NAME",db)){
               details.put("success", "false");
               details.put("reason", "user already exists");
               object.put("request_msg", msg);
               object.put("response",details);
               response.getWriter().print(object); 
               return;
            }
            String idNo=obj.optString("idno"); 
            String address=obj.optString("address"); 
            String phoneNo=obj.optString("phoneno"); 
            String role=obj.optString("role"); 
            UniqueRandom rand=new UniqueRandom(8);
            String userId=rand.nextRandom();
            UniqueRandom passRand=new UniqueRandom(6);
            String pass=passRand.nextMixedRandom();
            byte[] bytes=Security.makePasswordDigest(name,pass.toCharArray());
            String passw=Security.toBase64(bytes);
            db.doInsert("USERS", new String[]{userId,name,passw,idNo,address,phoneNo,role,"1","!NOW()"});
            details.put("success", "true");
            details.put("pass", pass);
            object.put("request_msg", msg);
            object.put("response",details);
            response.getWriter().print(object);
        } catch (Exception ex) {
            Logger.getLogger(PharmacyServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
     private void addStock(String drugId,String stockQty,String bPrice,String sPrice,Integer type,String narr){
       UniqueRandom rand=new UniqueRandom(20);
       String id=rand.nextMixedRandom();
       Integer qty=Integer.parseInt(stockQty);
       Float price=Float.parseFloat(bPrice);
       Float price1=Float.parseFloat(sPrice);
       Float costBp=qty*price;
       Float costSp=qty*price1;
       //specify profit also
       if(narr.equals("")){
          if(type==1){
             narr="New Stock"; //increase
          }
         else if(type==0){
             narr="Old Stock Disposed"; //reduction 
         }
       }
     
       db.doInsert("STOCK_DATA",new String[]{id,drugId,type.toString(),costBp.toString(),costSp.toString(),stockQty,"0",narr,"!NOW()"});
     }
     
     
     
     
     private void transact(JSONObject obj, HttpServletResponse response,String msg,String type){
        JSONArray drugIds=obj.optJSONArray("drug_ids");
        JSONArray qtys=obj.optJSONArray("qtys");
        //select the bp, sp
        UniqueRandom rand=new UniqueRandom(20);
        for(int x=0; x<drugIds.length(); x++){
           String drugId=drugIds.optString(x);
           JSONObject drugData = db.queryDatabase("SELECT * FROM DRUG_DATA WHERE ID=?",drugId); 
           double bp=drugData.optJSONArray("BP_UNIT_COST").optDouble(0);
           double sp=drugData.optJSONArray("SP_UNIT_COST").optDouble(0);
           String currentQty=qtys.optString(x);
           int drugTotalqty=drugData.optJSONArray("DRUG_QTY").optInt(0);
           int newQty=drugTotalqty-Integer.parseInt(currentQty); //reduce the quantity, customer is buying stock
           Double cost = sp*Integer.parseInt(currentQty);
           Double bPrice=bp*Integer.parseInt(currentQty);
           Double profit= cost-bPrice;
           String narr="";
           if(Integer.parseInt(currentQty)>drugTotalqty && type.equals("0")){
               continue;
               //not enough stock to process the sale
           }
           else if(type.equals("0")){
               narr="Sale to Customer";
               db.doInsert("STOCK_DATA",new String[]{rand.nextMixedRandom(),drugId,type,bPrice.toString(),cost.toString(),currentQty,profit.toString(),narr,"!NOW()"});
           }
           else if(type.equals("1")){
              newQty=drugTotalqty+Integer.parseInt(currentQty); //increase the quantity if customer is returning stock 
              narr="Reversal of sale";
              db.doInsert("STOCK_DATA",new String[]{rand.nextMixedRandom(),drugId,type,bPrice.toString(),cost.toString(),currentQty,"0",narr,"!NOW()"});
           }
           db.executeQuery("UPDATE DRUG_DATA SET DRUG_QTY='"+newQty+"' WHERE ID='"+drugId+"'");
          }
         toClient(response, msg, "success");
        
     }
    
     private void createDrug(JSONObject obj, HttpServletResponse response,String msg){
        try {
            String name=obj.optString("drug_name");
            JSONObject object=new JSONObject();
            JSONObject details=new JSONObject();
            if(Database.ifValueExists(name,"DRUG_DATA","DRUG_NAME",db)){
               details.put("success", "false");
               details.put("reason", "drug already exists");
               object.put("request_msg", msg);
               object.put("response",details);
               response.getWriter().print(object); 
               return;
            }
            String qty=obj.optString("qty"); 
            String type=obj.optString("type"); 
            String bpUnitCost=obj.optString("bp_unit_cost");
            String spUnitCost=obj.optString("sp_unit_cost");
            String lim=obj.optString("drug_remind_limit");
            String expiry=obj.optString("drug_expiry_limit");
            String narr=obj.optString("drug_narration");
            UniqueRandom rand=new UniqueRandom(10);
            String drugId=rand.nextRandom();
            db.doInsert("DRUG_DATA", new String[]{drugId,name,qty,type,bpUnitCost,spUnitCost,lim,expiry,"!NOW()"});
            addStock(drugId,qty, bpUnitCost,spUnitCost,1,narr);
            details.put("success", "true");
            object.put("request_msg", msg);
            object.put("response",details);
            response.getWriter().print(object);
        } catch (Exception ex) {
            Logger.getLogger(PharmacyServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
     
   public void completeAutoSuggest(JSONObject requestData, HttpServletResponse response,String msg){
         try {
             String type = requestData.optString("type");
             String id=requestData.optString("id");
             String tableName="";
             if("users".equals(type)){
                 tableName="USERS";
             }
             else if("drugs".equals(type)){
                 tableName="DRUG_DATA";
              
             }
             StringBuilder builder=new StringBuilder("SELECT * FROM ").append(tableName).append(" WHERE ID=? ");
             JSONObject data = db.queryDatabase(builder.toString(),id);
             JSONObject object=new JSONObject();
             JSONObject details=new JSONObject();
             details.put("success", "true");
             details.put("data", data);
             object.put("request_msg", msg);
             object.put("response",details);
             response.getWriter().print(object);
         } catch (Exception ex) {
             Logger.getLogger(PharmacyServlet.class.getName()).log(Level.SEVERE, null, ex);
         }
    }  
     
   public void autoSuggest(JSONObject requestData, HttpServletResponse response,String msg){
         try {
             String fieldName="";
             String type = requestData.optString("type");
             String like=requestData.optString("like");
             String tableName="";
             if("users".equals(type)){
                 tableName="USERS";
                 fieldName="USER_NAME";
             }
             else if("drugs".equals(type)){
                 tableName="DRUG_DATA";
                 fieldName="DRUG_NAME";
             }
             //select class_name from class_data  where class_name like 'e%';
             StringBuilder builder=new StringBuilder("SELECT ID, ").append(fieldName).append(" FROM ");
             builder.append(tableName).append(" WHERE ").append(fieldName);
             builder.append(" LIKE '").append(like).append("%' LIMIT 10");// ORDER BY ").append(fieldName).append(" ASC");
             JSONObject object=new JSONObject();
             JSONObject details=new JSONObject();
             details.put("success", "true");
             object.put("request_msg", msg);
             object.put("response",details);
             JSONObject data = db.queryDatabase(builder.toString());
             details.put("data", data);
             response.getWriter().print(object);
         } catch (Exception ex) {
             Logger.getLogger(PharmacyServlet.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
  
    
    private String[] ensureIntegrity(JSONObject obj,HttpServletResponse response,HttpServletRequest request){
       String [] currentUser=new String[3];
       try{
        JSONObject object=new JSONObject();
        Cookie[] cookies = request.getCookies();
        if(cookies==null || cookies.length==0){
           object.put("request_msg","redirect");
           object.put("url","index.html");
           response.getWriter().print(object);
           return null;
        }
      
       String username=null;
       String rand = null;
       String role = obj.optString("role");
       
       for(int x=0; x<cookies.length; x++){
          String name=cookies[x].getName();
          String value=cookies[x].getValue();
          if("user".equals(name)){
            username=value; 
            currentUser[0]=username;
          }
          else if(name.equals("rand")){
             rand=value; 
             currentUser[1]=rand;
          }
       }
       //now check whether this is what we have on the server
       Session ses=(Session) sessions.get(username);
       currentUser[2]=role;
       if(ses==null){
         object.put("request_msg","redirect");
         object.put("url","index.html");
         response.getWriter().print(object);  
         return null;
       }
       String userRole=(String) ses.getAttribute("role");
       if(ses.getSessionId().equals(rand) && userRole.equals(role)){
           // this is the right user
           return currentUser;
       }
       else{
                //make the user login again
           object.put("request_msg","redirect");
           object.put("url","index.html");
           response.getWriter().print(object);
           return null;
       }
    }
      catch(Exception e){
        e.printStackTrace();
        return null;
        
      }
    }
    
    
     private boolean authenticateUser(String userName, String pass){
        try{
             String pass_user=Security.toBase64(Security.makePasswordDigest(userName, pass.toCharArray()));
             String pass_stored=Database.getValue("PASS_WORD","USERS","USER_NAME",userName,db);
             if(pass_user.equals(pass_stored)){
                 return true;
              }
             return false;
        
           } catch(Exception e){
              return false; 
        }
    }
     
     
     private void editUser(JSONObject obj, HttpServletResponse response,String msg){
         try {
            String name=obj.optString("username");
            JSONObject object=new JSONObject();
            JSONObject details=new JSONObject();
            String idNo=obj.optString("idno"); 
            String address=obj.optString("address"); 
            String phoneNo=obj.optString("phoneno"); 
            String role=obj.optString("role");
            Database.executeQuery("UPDATE USERS SET ID_NO=? , ADDRESS=?, PHONE=?, ROLE=?  "
                    + "WHERE USER_NAME=?",db.getDatabaseName(),idNo,address,phoneNo,role,name);
            details.put("success", "true");
            object.put("request_msg", msg);
            object.put("response",details);
            response.getWriter().print(object);
        } catch (Exception ex) {
            Logger.getLogger(PharmacyServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
     }
     
       private void editDrug(JSONObject obj, HttpServletResponse response,String msg){
         try {
            String name=obj.optString("drug_name");
            String drugId=obj.optString("id");
            JSONObject object=new JSONObject();
            JSONObject details=new JSONObject();
            String type=obj.optString("type"); 
            String bpUnitCost=obj.optString("bp_unit_cost");
            String qty=obj.optString("qty");
            String spUnitCost=obj.optString("sp_unit_cost");
            String lim=obj.optString("drug_remind_limit");
            String expiry=obj.optString("drug_expiry_limit");
            String narr=obj.optString("drug_narration");
            JSONObject drugData=db.queryDatabase("SELECT BP_UNIT_COST,DRUG_QTY FROM DRUG_DATA WHERE ID=?",drugId);
            Integer storedDrugQty=Integer.parseInt(drugData.optJSONArray("DRUG_QTY").optString(0));
            Integer newDrugQty=Integer.parseInt(qty);
            Database.executeQuery("UPDATE DRUG_DATA SET DRUG_NAME = ? , DRUG_TYPE=?, BP_UNIT_COST=? , SP_UNIT_COST=? , DRUG_REMIND_LIMIT=?, DRUG_QTY=?, DRUG_EXPIRY_LIMIT=?"
                    + "WHERE ID=?",db.getDatabaseName(),name,type,bpUnitCost,spUnitCost,lim,qty,expiry,drugId);
            if(storedDrugQty>newDrugQty){
              //reduction in stock
                Integer theQty= storedDrugQty-newDrugQty;
                addStock(drugId,theQty.toString(),bpUnitCost,spUnitCost,0,narr);
            }
            else{
             //increase in stock
                Integer theQty= newDrugQty-storedDrugQty;
                addStock(drugId,theQty.toString(),bpUnitCost,spUnitCost,1,narr);
            }
            details.put("success", "true");
            object.put("request_msg", msg);
            object.put("response",details);
            response.getWriter().print(object);
        } catch (Exception ex) {
            Logger.getLogger(PharmacyServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
     }
       
        
     
     private void deleteObject(HttpServletResponse response,String msg, String value,String tableName, String column){
          try {
            JSONObject object=new JSONObject();
            JSONObject details=new JSONObject();
            Database.executeQuery("DELETE FROM "+tableName+" WHERE "+column+"=? ",db.getDatabaseName(),value);
            details.put("success", "true");
            object.put("request_msg", msg);
            object.put("response",details);
            response.getWriter().print(object);
        } catch (Exception ex) {
            Logger.getLogger(PharmacyServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
     private void allObjects(String sql,String msg, HttpServletResponse response){
        try {
            JSONObject object=new JSONObject();
            JSONObject details=new JSONObject();
            details.put("success", "true");
            JSONObject data=db.queryDatabase(sql);
            details.put("data",data);
            object.put("request_msg", msg);
            object.put("response",details);
            response.getWriter().print(object);
        } catch (Exception ex) {
            Logger.getLogger(PharmacyServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
     
     private void drugReminder(JSONObject obj, HttpServletResponse response,String msg){
        JSONObject drugData=db.queryDatabase("SELECT * FROM DRUG_DATA WHERE DRUG_QTY <= DRUG_REMIND_LIMIT");
        toClient(response, msg, drugData);
     }
     
     private void expiryReminder(JSONObject obj, HttpServletResponse response,String msg){
        JSONObject drugData=db.queryDatabase("SELECT * FROM DRUG_DATA WHERE DRUG_EXPIRY_LIMIT < NOW()");
        toClient(response, msg, drugData);
     }
     
     private void openStockHistory(JSONObject obj, HttpServletResponse response,String msg){
        String beginDate=obj.optString("begin_date");
        String endDate=obj.optString("end_date");
        String drugId=obj.optString("drug_id");
        JSONObject data;
        if(drugId.equals("none")){
            data= db.queryDatabase("SELECT STOCK_DATA.DRUG_ID,STOCK_COST_BP,STOCK_COST_SP,STOCK_QTY,TRAN_TYPE,STOCK_DATA.PROFIT,STOCK_DATA.NARRATION,STOCK_DATA.CREATED,DRUG_NAME FROM STOCK_DATA,DRUG_DATA WHERE STOCK_DATA.DRUG_ID=DRUG_DATA.ID AND STOCK_DATA.CREATED >= ? AND STOCK_DATA.CREATED <= ? ORDER BY STOCK_DATA.CREATED ASC",beginDate,endDate); 
        }
        else{
          data= db.queryDatabase("SELECT STOCK_DATA.DRUG_ID,STOCK_COST_BP,STOCK_COST_SP,STOCK_QTY,TRAN_TYPE,STOCK_DATA.PROFIT,STOCK_DATA.NARRATION,STOCK_DATA.CREATED,DRUG_NAME FROM STOCK_DATA,DRUG_DATA WHERE STOCK_DATA.DRUG_ID=DRUG_DATA.ID AND DRUG_ID=? AND STOCK_DATA.CREATED >= ? AND STOCK_DATA.CREATED <= ?  ORDER BY STOCK_DATA.CREATED ASC",drugId,beginDate,endDate);  
        }
        toClient(response, msg, data);
     }
     
     
     
     
     private void toClient(HttpServletResponse response, String msg,Object data){
         try {
             JSONObject object=new JSONObject();
              JSONObject details=new JSONObject();
              details.put("success", "true");
              details.put("data",data);
              object.put("request_msg", msg);
              object.put("response",details);  
              response.getWriter().print(object);
         } catch (Exception ex) {
             Logger.getLogger(PharmacyServlet.class.getName()).log(Level.SEVERE, null, ex);
         }
     }
     
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //  processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Hospital servlet";
    }// </editor-fold>
    
    
    
    @Override
    public void init(){
        try {
            String dbName=getServletConfig().getInitParameter("database-name");
            String uName=getServletConfig().getInitParameter("database-username");
            String pass=getServletConfig().getInitParameter("database-password");
            String host=getServletConfig().getInitParameter("database-host");
            Launcher.setDatabaseConnection(uName, host, pass);
            boolean exist=Database.ifDatabaseExists(dbName);
            if(exist){
                db=Database.getExistingDatabase(dbName);
            }
            else{
               db=new Database(dbName); 
            }
            if(!Table.ifTableExists("USERS", db.getDatabaseName())){
                    Table users = db.addTable("ID", "USERS", "VARCHAR(25)");
                    users.addColumns(new String[]{
                      "USER_NAME", "VARCHAR(256)",
                      "PASS_WORD","VARCHAR(512)",
                      "ID_NO","VARCHAR(20)",
                      "ADDRESS","VARCHAR(50)",
                      "PHONE","VARCHAR(50)",
                      "ROLE","VARCHAR(20)",
                      "CHANGE_PASS",DataType.BOOL,
                      "CREATED",DataType.DATETIME
                  }); 
              byte[] bytes=Security.makePasswordDigest("root","pass".toCharArray());
              String passw=Security.toBase64(bytes);
              db.doInsert("USERS", new String[]{"12345678","root",passw,"","","","admin","1","!NOW()"});
                
            }
            
               if(!Table.ifTableExists("DRUG_DATA", db.getDatabaseName())){
                    Table users = db.addTable("ID", "DRUG_DATA", "VARCHAR(10)");
                    users.addColumns(new String[]{
                      "DRUG_NAME", "VARCHAR(256)",
                      "DRUG_QTY",DataType.INT,
                      "DRUG_TYPE","VARCHAR(256)",
                      "BP_UNIT_COST",DataType.FLOAT,
                      "SP_UNIT_COST",DataType.FLOAT,
                      "DRUG_REMIND_LIMIT",DataType.INT,
                      "DRUG_EXPIRY_LIMIT",DataType.DATETIME,
                      "CREATED",DataType.DATETIME
                  });           
            }
           
            if(!Table.ifTableExists("STOCK_DATA", db.getDatabaseName())){
                    Table stock = db.addTable("ID", "STOCK_DATA", "VARCHAR(20)");
                    stock.addColumns(new String[]{
                      "DRUG_ID", "VARCHAR(10)",
                      "TRAN_TYPE",DataType.BOOL,
                      "STOCK_COST_BP",DataType.FLOAT,
                      "STOCK_COST_SP",DataType.FLOAT,
                      "STOCK_QTY",DataType.INT,
                      "PROFIT",DataType.FLOAT,
                      "NARRATION",DataType.TEXT,
                      "CREATED",DataType.DATETIME
                  });           
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                Logger.getLogger(PharmacyServlet.class.getName()).log(Level.SEVERE, null, ex);
                Thread.sleep(10000);
                init();
            } catch (InterruptedException ex1) {
                Logger.getLogger(PharmacyServlet.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
    
}
