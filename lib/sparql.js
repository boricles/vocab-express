var rest = require('./restler/restler');
//    sys = require('sys');

var queryClasses = "SELECT DISTINCT ?clazz " +
		    "WHERE { [] a ?clazz } " +
		    "ORDER BY ?clazz";

var queryProperties = "SELECT DISTINCT ?property " + 
		      "WHERE { [] ?property [] } " +
		      "ORDER BY ?property ";

var query = "?query=";




function obtainClasses(response,SEPoint) {
	var sparql = queryClasses;
	var uriBase = SEPoint + query;
	var Url = uriBase + escape(sparql) + "&format=json";
	console.log(Url);
	rest.get(Url).on('complete', function(data,responsed) {
		console.log('---------');
//		console.log(data);
	});




}


function process(response,SEPoint) {
  if (SEPoint == null || SEPoint == '') {
	response.write("You should specify a valid SPARQLEndpoint");
  }
  console.log('enro en process');
  obtainClasses(response,SEPoint);



}


exports.process = process;
