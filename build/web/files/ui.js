var dom={
    /**
     *
     * returns an element with the specified id
     */
   el : function(id){
       var element=document.getElementById(id);
       if(element){
         element.attr=function(name,value){
          this.setAttribute(name,value); 
        };
        element.add=function(elem){
          this.appendChild(elem); 
        };
       }
       return element;
   },
   
   /**
    *creates a new element with the specified tag name
    */
   newEl : function (tag){
      var element=document.createElement(tag);
       element.attr=function(name,value){
          this.setAttribute(name,value); 
       };
       element.add=function(elem){
          this.appendChild(elem); 
       };
      return element;
   },
   
   /**
    *
    * checks whether the document has completely loaded
    */
   ready : function (){
     if(document.readyState==="complete"){
        return true; 
      }
     else{
       return false;
     }
   },
   /**
    * 
    * returns an array of elements with the specified tag name
    *
    */
   tags : function(name){
      return document.getElementsByTagName(name);   
   },
   
   /**
    *waits till the document is ready and then executes the function func
    */
   waitTillReady : function(func){
      var time=setInterval(function(){
          if(dom.ready()){
            clearInterval(time); 
            func();
          }  
      },5); 
      
   },
   
     /**
    *waits till the document is ready and then executes the function func
    */
   waitTillElementReady : function(id,func){
      var time=setInterval(function(){
          if(dom.el(id)){
            clearInterval(time); 
            func();
          }  
      },5); 
      
   }
   
   

}

var funcs={} // namespace for functions



window.cookieStorage = {
    getItem: function (sKey) {
      if (!sKey || !this.hasOwnProperty(sKey)) { return null; }
      return unescape(document.cookie.replace(new RegExp("(?:^|.*;\\s*)" + escape(sKey).replace(/[\-\.\+\*]/g, "\\$&") + "\\s*\\=\\s*((?:[^;](?!;))*[^;]?).*"), "$1"));
    },
    key: function (nKeyId) {
      return unescape(document.cookie.replace(/\s*\=(?:.(?!;))*$/, "").split(/\s*\=(?:[^;](?!;))*[^;]?;\s*/)[nKeyId]);
    },
    setItem: function (sKey, sValue) {
      if(!sKey) { return; }
      document.cookie = escape(sKey) + "=" + escape(sValue) + "; expires=Tue, 19 Jan 2038 03:14:07 GMT; path=/";
      this.length = document.cookie.match(/\=/g).length;
    },
    length: 0,
    removeItem: function (sKey) {
      if (!sKey || !this.hasOwnProperty(sKey)) { return; }
      document.cookie = escape(sKey) + "=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/";
      this.length--;
    },
    hasOwnProperty: function (sKey) {
      return (new RegExp("(?:^|;\\s*)" + escape(sKey).replace(/[\-\.\+\*]/g, "\\$&") + "\\s*\\=")).test(document.cookie);
    }
  };
  window.cookieStorage.length = (document.cookie.match(/\=/g) || window.cookieStorage).length;



var ui={
  /*
  *  ui appends the input
  *
  * value
  * onclick
  * class
  * other attributes
  * 
  */
  element: function(attr){
     var run=function(id){
         var com=dom.newEl(attr.tag);
	 com.attr("id",id);
         delete attr.tag;
         for(var param in attr){
           if(typeof attr[param]==="function"){
             var func=attr[param];
             var str=func.toString();
             var funcBody=str.substring(str.indexOf("("));
             var funcName="function_"+Math.floor(Math.random()*100000000)+" ";
             var newFunc="funcs."+funcName+"= function "+funcName+funcBody;
             eval(newFunc);
             com.attr(param,"funcs."+funcName+"()"); 
           }
	  else if(param==="klass"){
	      com.attr("class",attr[param]);
	   }
          else if(param==="content"){
	      com.innerHTML=attr[param];
	   }
          else{
            com.attr(param,attr[param]);
          }
        }
        if(attr.parent){
          //append this element to the specified parent
            if( typeof attr.parent==="string"){
		 dom.el(attr.parent).appendChild(com);
	      }
	    else {
	         attr.parent.appendChild(com);
	     }
          delete attr.parent;
  	 }
       else{
        dom.tags("body")[0].appendChild(com);
       }	
      return com;	  
     };
	 
       function wait(func,id){
	  var time=setInterval(function(){
          if(dom.ready()){
            clearInterval(time); 
            func(id);
          }  
        },5);
     }
     if(!attr.id){
            // bind this id if none is specified
       attr.id="element_"+Math.floor(Math.random()*1000000);
     } 
     wait(run,attr.id); // run this function only after the document is ready
     return attr.id;
 },
 dropdown : {
     onselectFuncs : [],
     onselectIds : [],
     add : function(areaToAppend,elementId,arrOptions,arrValues){
       var func=function(){
        var contDiv=dom.newEl("div");
        contDiv.attr("class","btn-group");
        var mainId=elementId+"_main";
        contDiv.attr("id",mainId);
        var aLabel=dom.newEl("a");
        aLabel.attr("class","btn");
        aLabel.attr("href","#");
        aLabel.innerHTML=arrOptions[0];
        var id=elementId+"_root";
        if(dom.el(mainId)){
           dom.el(mainId).remove(); 
        }
        aLabel.attr("id",id);
        aLabel.attr("style","width: 150px;");
        aLabel.attr("data-toggle","dropdown");
        aLabel.attr("value",arrValues[0]);
        var aCaret=dom.newEl("a");
        aCaret.attr("class","btn dropdown-toggle");
        aCaret.attr("data-toggle","dropdown");
        aCaret.attr("href","#");
        
        var aSpan=dom.newEl("span");
        aSpan.attr("class","caret");
        aCaret.add(aSpan);
        
        contDiv.add(aLabel);
        contDiv.add(aCaret);
        
        var ul=dom.newEl("ul");
        ul.attr("id",elementId);
        ul.attr("class","dropdown-menu");
        ul.attr("style","text-align : left;max-height: 300px;overflow: auto;width:200px; max-width : 200px");
        contDiv.add(ul);
        for(var x=0; x<arrOptions.length; x++){
           var li=dom.newEl("li");
           li.attr("value",arrValues[x]);
           li.attr("option",arrOptions[x]);
           var liDiv=dom.newEl("li");
           liDiv.attr("class","divider");
           var a=dom.newEl("a");
           a.href="#";
           a.attr("onclick","ui.dropdown.setSelected(\""+elementId+"\",\""+arrValues[x]+"\")");
           a.innerHTML=arrOptions[x];
           li.add(a);
           ul.add(li);
           ul.add(liDiv);
        }
        if(typeof areaToAppend==="string"){
            dom.el(areaToAppend).add(contDiv); 
        }
        else{
           areaToAppend.appendChild(contDiv);  
        }
      };
      if(typeof areaToAppend!=="string"){
          func();
      }
      else{
       dom.waitTillElementReady(areaToAppend,func);
      }
     },
     getSelected : function(id){
        return dom.el(id+"_root").getAttribute("value");
     },
     setSelected : function(id,value){
       var root=dom.el(id); 
       for(var x=0; x<root.children.length; x++){
         var li =root.children[x];
         var val=li.getAttribute("value"); 
         if(val===value){
           var option = li.getAttribute("option");
           var rootLi=dom.el(id+"_root");
           rootLi.attr("value",value);
           rootLi.innerHTML=option;
           break;
         }
       }
      var index=ui.dropdown.onselectIds.indexOf(id);
      if(index===-1){
         return;
      }
      else{
        eval(ui.dropdown.onselectFuncs[index]);
      }
    },
    onselect : function(id,func){
      if(ui.dropdown.onselectIds.indexOf(id)===-1){
           ui.dropdown.onselectIds.push(id);
           ui.dropdown.onselectFuncs.push(func);
      }
    }
 },
 modal : function(title,uiFunc,funcOk,funcCancel){
      var window=dom.el("alert-window");
      if(!window){
        var mainDiv=dom.newEl("div");
        mainDiv.attr("class","modal hide");
        mainDiv.attr("id","alert-window");
        mainDiv.attr("style","height : auto; width : 800px");
        var headerDiv=dom.newEl("div");
        headerDiv.attr("class","modal-header");
        var closeButton=dom.newEl("button");
        closeButton.attr("class","close");
        closeButton.attr("type","button");
        closeButton.attr("data-dismiss","modal");
        closeButton.attr("aria-hidden","true");
        closeButton.innerHTML="&times";
        var h3=dom.newEl("h3");
        h3.attr("id","modal-title");
        
        var bodyDiv=dom.newEl("div");
        bodyDiv.attr("class","modal-body");
        var p=dom.newEl("p");
        p.attr("id","modal-content");
        
        var footerDiv=dom.newEl("div");
        footerDiv.attr("class","modal-footer");
        
        var cancelButton=dom.newEl("button");
        cancelButton.attr("class","btn");
        cancelButton.attr("data-dismiss","modal");
        cancelButton.attr("aria-hidden","true");
        cancelButton.attr("id","cancel_func");
        cancelButton.attr("onclick",funcCancel);
        cancelButton.innerHTML="Cancel";
        
        var okButton=dom.newEl("button");
        okButton.attr("class","btn btn-primary");
        okButton.attr("onclick",funcOk);
        okButton.attr("id","ok_func");
        okButton.innerHTML="OK";
        
        headerDiv.add(closeButton);
        headerDiv.add(h3);
      
        bodyDiv.add(p);
        footerDiv.add(cancelButton);
        footerDiv.add(okButton);
        mainDiv.add(bodyDiv);
        mainDiv.add(headerDiv);
        mainDiv.add(bodyDiv);
        mainDiv.add(footerDiv);
        
        dom.tags("body")[0].appendChild(mainDiv);
        
        
      }
      dom.el("modal-content").innerHTML="";
      dom.el("modal-title").innerHTML=title;
      eval(uiFunc);
      dom.el("ok_func").attr("onclick",funcOk);
      dom.el("cancel_func").attr("onclick",funcCancel);
      $("#alert-window").modal();
 },
 staticmodal : function(areaToAppend,title,uiFunc){
          var html="<div class='static-modal'>"+
             		 "<div class='modal'>"+
             		 "<div class='modal-dialog'>"+
             		 "<div class='modal-content'>"+
             		 "<div class='modal-header'>"+
                     "<h4 class='modal-title' id='modal_title'></h4>"+
                    "</div>"+
                  "<div class='modal-body' id='modal_body'>"+
                  "</div>"+
                "</div><!-- /.modal-content -->"+
              "</div><!-- /.modal-dialog -->"+
            "</div><!-- /.modal -->"+
            "</div>";
           $("#"+areaToAppend).append(html);
           $("#modal_title").html(title);
           eval(uiFunc);
           $("#modal_title").attr("id","");
           $("#modal_body").attr("id","");
    },
   /*
    * 
    * @param {array} headers this is an array containing the headers of the table
    * @param {array} values this is an array containing arrays of the values to be displayed
    * @param  {boolean} includeNums tells us if we should include the first column as numbering
    * @returns {undefined}
    */
   table : function(idToAppend,headers,values,includeNums){
     var table=dom.newEl("table");
     table.attr("class","table table-condensed");
     var id="table_"+Math.floor(Math.random()*1000000);
     table.attr("id",id);
     var tr=dom.newEl("tr");
     if(includeNums){
        headers.unshift("No");
     }
     for(var x=0; x<headers.length; x++){
       var th=dom.newEl("th");
       th.innerHTML=headers[x];
       tr.add(th);
     }
     table.add(tr);
     for(var x=0; x<values[0].length; x++){
        var tr=dom.newEl("tr");
        var numTd=null;
        if(includeNums){
            numTd=dom.newEl("td");
            numTd.innerHTML=(x+1);
            tr.add(numTd);
        }
        for(var y=0; y<values.length; y++){
           var td=dom.newEl("td");
           if(typeof values[y][x]==="string"){
             td.innerHTML=values[y][x];  
           }
           else{
             td.add(values[y][x]);
           }
           tr.add(td);
        }
       table.add(tr);
     }
     dom.el(idToAppend).add(table);
     return id;
   }
 
};







var Ajax={
    /**
     *@param data the data to be pushed to the server
     */
    run:function(data){  
       /*
        * url
        * loadArea
        * data
        * type
        * success
        * error
        */
      
           //show that this page is loading
      if(data.loadArea){
        dom.el(data.loadArea).style.display="block";
      }
      function callback(xhr){
        return function(){
         if (xhr.readyState === 4) {
           if (xhr.status === 200) {
             var resp=xhr.responseText;
             var json=JSON.parse(resp);
             if(data.success!==null){
                   if(data.loadArea){
                    dom.el(data.loadArea).style.display="none";
                   }
                  if(json.request_msg==="redirect"){
                      parent.window.location=json.url;   
                   }
                  else if(json.response.type==="exception"){
                      setInfo(json.response.ex_reason);   
                   }
                  else{
                     data.success(json); 
                   }
               }

           } else {
             if(data.error!==null){
                if(data.loadArea){
                  dom.el(data.loadArea).style.display="none";
                 }
                data.error(data);
             }  
           }
          }
        }
      }
        
      return function(){
           var xhr=getRequestObject();
           if(data.error!=null){
              if(xhr.onerror){
                xhr.onerror=data.error; 
              } 
           }
           sendJSON(data.url,data.data,xhr,callback(xhr),data.type);
     }();
     
    }
 
   
    
};










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
 * used to send json data to the server
 * @param serverUrl the url to the server where the data is to be sent
 * @param json this is the json data to be sent to the server
 * @param request this is the xmlhttp request object
 * @param callback this is the callback function
 * @param type post or get
 */
function sendJSON(serverUrl,json,request,callback,type){
       json="json="+JSON.stringify(json);
       request.onreadystatechange=callback;
       request.open(type, serverUrl, true);
       request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
       request.send(json);   
}





