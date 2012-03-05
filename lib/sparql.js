var rest = require('./restler/restler');

var fs = require('fs');

var queryClasses = "SELECT DISTINCT ?clazz " +
		    "WHERE { [] a ?clazz } " +
		    "ORDER BY ?clazz";

var queryProperties = "SELECT DISTINCT ?prop " + 
		      "WHERE { [] ?prop [] } " +
		      "ORDER BY ?prop ";

var query = "?query=";

var format="&format=json";


function replaceAll(txt, replace, with_this) {
  return txt.replace(new RegExp(replace, 'g'),with_this);
}

function displayProperties(response,SEPoint) {
		var content = "";	

		content = content + " <h2>Properties</h2>";

			//Properties
			var sparql = queryProperties;
			var uriBase = SEPoint + query;
			var Url = uriBase + escape(sparql) + format + "";
			rest.get(Url).on('complete', function(data) {

				var jsonObject = eval("("+data+")");
				var rows = jsonObject.results.bindings;

				var localContent;

				var ctemplate = "<div><a href='http://vocab-express.nodester.com/exploreTerm?sparqlEndPoint="+ SEPoint +
						"&hash=%HASH%&term=%ELEMENT%' target='_blank'>%LABEL%</a></div><br/>";		
		

				for (var i=0; i<rows.length; i++) {

				   localContent = "";
				   localContent = ctemplate.replace("%ELEMENT%",rows[i].prop.value);
				   //content = content + localContent.replace("%LABEL%",rows[i].prop.value);

				   var index = rows[i].prop.value.lastIndexOf('#');
				   localContent = ctemplate.replace("%ELEMENT%",rows[i].prop.value);   
				   if (index == -1)
					localContent = localContent.replace("%HASH%","");
				   else
					localContent = localContent.replace("%HASH%",rows[i].prop.value.substring(index+1));
				   
				   //if (rows[i].label) 
				     //content = content + localContent.replace("%LABEL%",rows[i].label.value);
				   //else 
					content = content + localContent.replace("%LABEL%",rows[i].prop.value);
				}

				response.write(content);
				response.end("</body></html>");			
			});
}

function displayClasses (response,SEPoint) {
		
	var content = "";	
	

	//Header
  	fs.readFile('/app/html/header.html', function (err,data) {
		if (err) {
			throw err;
		}
		content = data.toString();
		content = replaceAll(content,"%SPARQLENDPOINT%",SEPoint);
		content = content + " <h2>Classes</h2>";
		response.writeHead(200, {'Content-type':'text/html'});

		//Classes
		var sparql = queryClasses;
		var uriBase = SEPoint + query;
		var Url = uriBase + escape(sparql) + format + "";
		rest.get(Url).on('complete', function(data) {

			
			var jsonObject = eval("("+data+")");
			var rows = jsonObject.results.bindings;
			var localContent;

			var ctemplate = "<div><a href='http://vocab-express.nodester.com/exploreTerm?sparqlEndPoint="+ SEPoint +
						"&hash=%HASH%&term=%ELEMENT%' target='_blank'>%LABEL%</a></div><br/>";		

			for (var i=0; i<rows.length; i++) {
			   localContent = "";
			   var index = rows[i].clazz.value.lastIndexOf('#');
			   localContent = ctemplate.replace("%ELEMENT%",rows[i].clazz.value);   
			   if (index == -1)
				localContent = localContent.replace("%HASH%","");
			   else
				localContent = localContent.replace("%HASH%",rows[i].clazz.value.substring(index+1));
			   
			   //if (rows[i].label) 
			     //content = content + localContent.replace("%LABEL%",rows[i].label.value);
			   //else 
			     content = content + localContent.replace("%LABEL%",rows[i].clazz.value);							
			}
			
			response.write(content);
						
		});
	});

}

function displayContent(response,SEPoint) {
	displayClasses(response,SEPoint);
	displayProperties(response,SEPoint);
}


function process(response,SEPoint) {
	if (SEPoint == null || SEPoint == '') {
  		fs.readFile('/app/html/error.html', function (err,data) {
		if (err) {
			throw err;
		}
		stringData = data.toString();
		stringData = stringData.replace("%ERROR%","Invalid sparql endpoint specified");
		response.writeHead(200, {'Content-type':'text/html','Content-Length':stringData.length});
		response.write(stringData);
		response.end();
		});
  	}
  	else {
		displayContent(response,SEPoint);		
  	}
}


exports.process = process;
