/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
  window.localStorage.length = (document.cookie.match(/\=/g) || window.localStorage).length;


function addOption(selectbox,text,value){
   var optn=document.createElement("OPTION")
   optn.innerText=text;
   optn.text=text;
   optn.value=value;
   var box=document.getElementById(selectbox)
   box.appendChild(optn);
}
//populate a select box from an array
function populateSelect(selectbox,arrayoptions,arrayvalues){
    if(arrayvalues==null || arrayvalues=="undefined"){
      return;  
    }
 for(var y=0; y<arrayvalues.length; ++y){
  addOption(selectbox,arrayoptions[y],arrayvalues[y])
}

}

function depopulateSelect(selectbox){
   var select=document.getElementById(selectbox);
   while (select.firstChild) {
       select.removeChild(select.firstChild);
     }
    
}

function getMultipleSelect(fld){
  var sel=document.getElementById(fld);
  var values=[];
  for(var x=0; x<sel.options.length;x++){
      if(sel.options[x].selected){
         values.push(sel.options[x].value) 
      }
   }
  return values;
    
}

function getMultipleEntries(fld){
  var sel=document.getElementById(fld).value;
  var values=[];
  var y=0;
  var y1=0;
     for(var x=sel.length+1; x>=0; x--){
       if(sel.charAt(x)=="," && y==0){
           y++;
           y1=x
          }
         else if(sel.charAt(x)==","){
           var ans=sel.slice(x+1,y1);
           if( !(values.indexOf(ans)>-1)){
             values.push(ans);  
           }
           y1=x
       }
       else if(x==0){
          ans=sel.slice(0,y1); 
          if( !(values.indexOf(ans)>-1)){
             values.push(ans);  
           }
       } 
     }
   return values;
}

function getValue(fld){
 return document.getElementById(fld).value;
}

function setValue(fld,val){
  document.getElementById(fld).value=val;
}

function focus(fld){
 document.getElementById(fld).focus();
}

function getLocationBarValue(location){
    location=location+""
    var index=location.indexOf("=");
    if(index==-1){
       return null;
    }
   var name=location.slice(index+1,location.length);
   return name;
}


  
//---------------------------------------------------------------------------------------
	
function completePrivileges(priv,arr) {
		function split( val ) {
			return val.split( /,\s*/ );
		}
		function extractLast( term ) {
			return split( term ).pop();
		}
        
		
		$(priv)
			// don't navigate away from the field on tab when selecting an item
			.bind( "keydown", function( event ) {
				if ( event.keyCode === $.ui.keyCode.TAB &&
						$( this ).data( "autocomplete" ).menu.active ) {
					event.preventDefault();
				}
			})
			.autocomplete({
				minLength: 0,
				source: function( request, response ) {
					// delegate back to autocomplete, but extract the last term
					response( $.ui.autocomplete.filter(
						arr, extractLast( request.term ) ) );
				},
				focus: function() {
					// prevent value inserted on focus
					return false;
				},
				select: function( event, ui ) {
					var terms = split( this.value );
					// remove the current input
					terms.pop();
					// add the selected item
					terms.push( ui.item.value );
					// add placeholder to get the comma-and-space at the end
					terms.push( "" );
					this.value = terms.join( ", " );
					return false;
				}
			});
	}
	
        
     function openPopup(url,name,width,height,resizable,scrollbars,menubar,toolbar,location,directories,status,top,left) {
	var tl = (top && left) ? ',top=' + top +',left=' + left : '';
	var popup = window.open("test.html","test", 'width=' + width + ' height=' + height + 'resizable=' + resizable + 'scrollbars=' + scrollbars + 'menubar=' + menubar + 'toolbar=' + toolbar + 'location=' + location + 'directories=' + directories + ',status=' + status+tl);
	popup.focus();
        return popup;
     }
     
    function disable(id,state){
      document.getElementById(id).disabled=state;
    }

