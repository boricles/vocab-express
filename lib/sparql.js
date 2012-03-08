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

function insertVocab(vocabs,value) {
	var index = value.lastIndexOf('#');
	if (index == -1) {
		index = value.lastIndexOf('/');
		value = value.substring(0,index);
	}
	else {
		value = value.substring(0,index);
	}
	var vocabElem = new Object;
	vocabElem.usage = 1;	
	if (vocabs[value]) {
		vocabs[value].usage++;
	}
	else {
		vocabs[value] = vocabElem;
	}
}

function displayVocabularies(response, vocabs) {
	var header = "<tr colspan='1'><th><a id='Vocabularies'></a>Vocabularies</th><th>Usage</th></tr>";
	var content = header;


	for (key in vocabs) {
		content += "<tr colspan='1'><td><a href='"+ key+ "' target='_blank'>" + key +"</a></td><td>" + vocabs[key].usage + "</td></tr>";
	}	
	response.write(content);
	response.end("</table></body></html>");
}

function displayProperties(response,SEPoint,vocabs) {
	var content = "";
	var header = "<tr><th><a id='Properties'></a>Properties</th></tr>";
	content = content + header;
	//Properties
	var sparql = queryProperties;
	var uriBase = SEPoint + query;
	var Url = uriBase + escape(sparql) + format + "";
	rest.get(Url).on('complete', function(data) {

		var jsonObject = eval("("+data+")");
		var rows = jsonObject.results.bindings;
		var localContent;
		var ctemplate = "<tr><td><a href='http://vocab-express.nodester.com/exploreTerm?sparqlEndPoint="+ SEPoint +
					"&hash=%HASH%&term=%ELEMENT%' target='_blank'>%LABEL%</a></td></tr>";		
	
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
			   
		   content = content + localContent.replace("%LABEL%",rows[i].prop.value);

	 	   insertVocab(vocabs,rows[i].prop.value);
		}
		response.write(content);
		displayVocabularies(response,vocabs);
	});
}

function displayClasses (response,SEPoint,vocabs) {
		
	var content = "";	
	//Header
  	fs.readFile('/app/html/header.html', function (err,data) {
		if (err) {
			throw err;
		}
		content = data.toString();
		content = replaceAll(content,"%SPARQLENDPOINT%",SEPoint);
		content += "<a style='text-decoration: none;' href='#Classes'>Classes</a><br/><br/><a style='text-decoration: none;' href='#Properties'>Properties</a><br/><br/><a style='text-decoration: none;' href='#Vocabularies'>Vocabularies</a><br/><br/><br/>";
		var header = "<table><tr><th><a id='Classes'></a>Classes</th></tr>";
		content = content + header;
		response.writeHead(200, {'Content-type':'text/html'});

		//Classes
		var sparql = queryClasses;
		var uriBase = SEPoint + query;
		var Url = uriBase + escape(sparql) + format + "";
		rest.get(Url).on('complete', function(data) {

			var jsonObject = eval("("+data+")");
			var rows = jsonObject.results.bindings;
			var localContent;

			var ctemplate = "<tr><td><a href='http://vocab-express.nodester.com/exploreTerm?sparqlEndPoint="+ SEPoint +
						"&hash=%HASH%&term=%ELEMENT%' target='_blank'>%LABEL%</a></td></tr>";		

			for (var i=0; i<rows.length; i++) {
			   localContent = "";
			   var index = rows[i].clazz.value.lastIndexOf('#');
			   localContent = ctemplate.replace("%ELEMENT%",rows[i].clazz.value);   
			   if (index == -1)
				localContent = localContent.replace("%HASH%","");
			   else
				localContent = localContent.replace("%HASH%",rows[i].clazz.value.substring(index+1));
			   
			   content = content + localContent.replace("%LABEL%",rows[i].clazz.value);
			
                           insertVocab(vocabs,rows[i].clazz.value);			
			
			}
			response.write(content);
		});
	});
}

function displayContent(response,SEPoint) {
	var vocabs = [];
	displayClasses(response,SEPoint,vocabs);
	displayProperties(response,SEPoint,vocabs);
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
