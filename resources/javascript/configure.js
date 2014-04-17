function loadFormValues(){
	$.ajax({ // ajax call starts
        url: '/cfg/load',
        dataType: 'json', // Choosing a JSON datatype
        success: function(data) // Variable data contains the data we get from serverside
        {
        	var dataObj = JSON.parse(data);
        	var header = dataObj.header;
        	var headerElement = document.getElementById("configuration.header");
        	if(headerElement != undefined){
        		headerElement.value = header;
        	}
        	var formFields = dataObj.formFields;
        	formFields.map( function(item) {
        		var obj = JSON.parse(item);
        		var parts = obj.value.split("=");
        		var key = parts[0].trim();
        		var value = parts[1];
        		//Update description of element
        		var helpTextElement = document.getElementById(key+".desc");
        		if(helpTextElement != undefined){
        			var comments = obj.comments.split("#");
        			var markupComments = "";
        			for(var j in comments){
        				var comment = comments[j];
        				if(comment == ""){
        					continue;
        				}
        				markupComments = markupComments+comment+"<br>";
        			}
        			helpTextElement.innerHTML = markupComments;
        			var helpTextHiddenDescElement = document.getElementById(key+".desc.hidden");
        			if(helpTextHiddenDescElement != undefined){
            			helpTextHiddenDescElement.value = obj.comments;
        			}
        		}
        		//Update value of element
        		var inputElement = document.getElementById(key);
        		if(inputElement == undefined){
        			inputElement = document.getElementsByName(key)[0];
        		}
        		if(inputElement != undefined){
	        		if(inputElement.getAttribute("type") == "text"){
	        			inputElement.value = value; 
	        		}else if(inputElement.getAttribute("type") == "radio"){
						var name = inputElement.getAttribute("name");
	        			//var value = $('input[name='+name+']:checked').val();
						 $('input[name="' + name+ '"]').val([value]);
	        		}
        		}
        	});
        }
    });

}