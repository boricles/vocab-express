var rest = require('./restler/restler');
//    sys = require('sys');

var queryClasses = "SELECT DISTINCT ?class " +
		    "WHERE { [] a ?class } " +
		    "ORDER BY ?class";

var queryProperties = "SELECT DISTINCT ?property " + 
		      "WHERE { [] ?property [] } " +
		      "ORDER BY ?property ";

var query = "?query=";




function obtainClasses(response,SEPoint) {
	var sparql = queryClasses;
	var uriBase = SEPoint + query;
	var Url = uriBase + escape(sparql) + "";
	rest.get(Url).on('complete', function(data) {
		response.writeHead(200, {"Content-Type": "text/plain"});
		response.write(data);
		//response.end();
	});
}

function obtainProperties(response,SEPoint) {
	var sparql = queryProperties;
	var uriBase = SEPoint + query;
	var Url = uriBase + escape(sparql) + "";
	rest.get(Url).on('complete', function(data) {
		//response.writeHead(200, {"Content-Type": "text/plain"});
		response.write(data);
		response.end();
	});
}

function process(response,SEPoint) {
  if (SEPoint == null || SEPoint == '') {
	response.write("You should specify a valid SPARQLEndpoint");
  }

  obtainClasses(response,SEPoint); 
  obtainProperties(response,SEPoint);


}


exports.process = process;
