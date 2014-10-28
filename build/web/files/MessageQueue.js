/*
 * This file is an implemetation of a message queue
 * to deal with messages from the server and to send messages to
 * the server
 */

/*
 * necessary initialisations
 */

/*
 *  key words that are acted upon when received
 *  display means dispay the info on the information bar
 *  alert means show the message in alert box
 */ 

var trigger={
    exception:{
        mode: "alert",
        header: ""
    },
  
    error: {
        mode:"display",
        header: ""
    }
 
}



var OUT={
    /**
     *@param data the data to be pushed to the server
     *@param applier the function to be called when a response is sent from the server
     *@param loadArea the area to show that the page is loading
     */
    pushOut:function(data,loadArea,applier){  
      document.getElementById(loadArea).style.display="block"
      function callback(request,applier){
        return function(){
         if (request.readyState == 4) {
           if (request.status == 200) {
            var resp=request.responseText;
             var json=JSON.parse(resp);
               for(var x in trigger){
                   if(x==json.request_msg){
                       if(trigger[x].mode=="display"){
                           showInfo(loadArea,trigger[x].header+json.data);
                       }
                       else if(trigger[x].mode=="alert"){
                           alert(trigger[x].header+json.data);
                       }
                       document.getElementById(loadArea).style.display="none"
                      break;
                   }
               }

               if(json.request_msg=="redirect"){
                 window.location=json.url;   
               }
               else if(applier!=null && json.request_msg!="exception" ){
                    document.getElementById(loadArea).style.display="none"
                    applier(json);    
               }

              
           } else {
              showInfo(loadArea,"Server communication error");
           }
          }
        }
      }
        
      return function(){
          var request=getRequestObject();
           sendJSON(data.serverUrl,data.data,request,callback(request,applier));
     }();
     
    }
 
   
    
};



/**
*@param serverUrl the url to the server where the data is being sent
*@param data the data being sent to the server
*/
function Data(serverUrl,data){
    this.serverUrl=serverUrl;
    this.data=data;
}







/*
 * this function returns an xml http object
 */

function getRequestObject(){
    if(window.ActiveXObject){
      return new ActiveXObject("Microsoft.XMLHTTP");  
    }
    else if(window.XMLHttpRequest){
       return new  XMLHttpRequest();
    }
    else{
       return null; 
    }
    
}

/**
 * this functions sends an xml http request to a 
 * specified server url
 * @param serverUrl this is the url on the server where the data sent is processed e.g index.jsp, action.asp etc 
 * @param message the message sent to the server
 * @param data this is the data to be sent to the server if we are using the POST method to send data
 * @param callback this is the function called after this request is executed by the server
 * @param request the request object used to send data to the server
 * @param service the service to be invoked on the server
 */
function sendRequest(serverUrl,message,data,callback,request,service){
    if(data==null || data==""){
        var quest_msg="quest_msg="+message;
          if(service!=null){
            quest_msg=quest_msg+"&quest_svc="+service;
          }

       }
      else {
         quest_msg="&quest_msg="+message;
         if(service!=null){
            quest_msg=quest_msg+"&quest_svc="+service;
          }
         quest_msg=data+quest_msg;  
      }
       request.onreadystatechange=callback
       request.open("POST", serverUrl, true);
       request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
       request.send(quest_msg);  

}




/**
 * used to send json data to the server
 * @param serverUrl the url to the server where the data is to be sent
 * @param json this is the json data to be sent to the server
 * @param request this is the xmlhttp request object
 * @param callback this is the callback function
 */
function sendJSON(serverUrl,json,request,callback){
       json="json="+JSON.stringify(json);
       request.onreadystatechange=callback
       request.open("POST", serverUrl, true);
       request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
       request.send(json);   
}











