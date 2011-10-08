var querystring = require("querystring");
var sparql = require("./sparql");

//this should go to another module

var paramSPARQLEndPoint = "sparqlEndPoint";

function processParameters(response, search) {
  if (search != null && search != '') {
	  search = search.substring(1);
	  var params = querystring.parse(search);
	  var present = 0;
	  for (key in params) {
		console.log(key + ' ' + params[key]);
		if (key == paramSPARQLEndPoint) {
			sparql.process(response,params[key]);
			present = 1;
			return;
		}
	  }
	  if (present) {
		response.writeHead(200, {"Content-Type": "text/plain"});
		response.write("You should specify either a SPARQLEndpoint or a void file description\nFor example\n");
		response.write("vocabexpress?sparqlEndPoint=http://geo.linkeddata.es/sparql\n");
		response.write("vocabexpress?voidDescription=http://geo.linkeddata.es/data/void.ttl\n");
		response.end();
	  }
  }
  else {
	response.writeHead(200, {"Content-Type": "text/plain"});
	response.write("You should specify either a SPARQLEndpoint or a void file description\nFor example\n");
	response.write("vocabexpress?sparqlEndPoint=http://geo.linkeddata.es/sparql\n");
	response.write("vocabexpress?voidDescription=http://geo.linkeddata.es/data/void.ttl\n");
	response.end();
 }

}

function explore(response, search) {
  console.log("Request handler 'explore' was called.");


  processParameters(response, search);



}

exports.explore = explore;
