var querystring = require("querystring");
var sparql = require("./sparql");
var voidFile = require("./voidFile");
var term = require("./term");
var fs = require("fs");
var index;

var paramHash="hash";
var paramTerm="term";
var paramSPARQLEndPoint = "sparqlEndPoint";
var paramVoidDescriptionFile = "voidFileDescription";

function processParameters(response, search) {
  if (search != null && search != '') {
	  search = search.substring(1);
	  var params = querystring.parse(search);
	  var present = 0;
	  for (key in params) {
		//console.log(key + ' ' + params[key]);
		if (key == paramSPARQLEndPoint) {
			sparql.process(response,params[key]);
			present = 1;
			return;
		}
		else if (key == paramVoidDescriptionFile) {
			voidFile.process(response,params[key]);
			present = 1;
			return;
		}

	  }
	  if (!present) {
		response.writeHead(200, {"Content-Type": "text/plain"});
		response.write("You should specify either a SPARQLEndpoint or a void file description\nFor example\n");
		response.write("http://vocab-express.nodester.com/explore?sparqlEndPoint=http://geo.linkeddata.es/sparql\n");
		response.write("http://vocab-express.nodester.com/explore?voidFileDescription=http://geo.linkeddata.es/data/void.ttl\n");
		response.end();
	  }
  }
  else {
	response.writeHead(200, {"Content-Type": "text/plain"});
	response.write("You should specify either a SPARQLEndpoint or a void file description\nFor example\n");
	response.write("http://vocab-express.nodester.com/explore?sparqlEndPoint=http://geo.linkeddata.es/sparql\n");
	response.write("http://vocab-express.nodester.com/explore?voidFileDescription=http://geo.linkeddata.es/data/void.ttl\n");
	response.end();
 }
}

function processParametersET(response, search) {
  if (search != null && search != '') {
	  search = search.substring(1);
	  var params = querystring.parse(search);
	  var termValue = "";
	  var sparqlEPValue = "";
	  for (key in params) {
		//console.log(key + ' ' + params[key]);
		if (key == paramSPARQLEndPoint) 
			sparqlEPValue = params[key];
		
		if (key == paramTerm) 
			termValue = params[key];

		if (key == paramHash) 
			hashValue = params[key];

	  }
		
	  if (termValue=="" || sparqlEPValue=="") {
		response.writeHead(200, {"Content-Type": "text/plain"});
		response.write("You should specify the SPARQLEndpoint and the vocabulary term you want to explore \nFor example\n");
		response.write("http://vocab-express.nodester.com/exploreTerm?sparqlEndPoint=http://geo.linkeddata.es/sparql&term=http://geo.linkeddata.es/ontology/Laguna\n");
		response.end();
		return;
	  }
	
	  term.process(response,sparqlEPValue,termValue,hashValue);
	  return;

  }
  else {
	response.writeHead(200, {"Content-Type": "text/plain"});
		response.writeHead(200, {"Content-Type": "text/plain"});
		response.write("You should specify the SPARQLEndpoint and the vocabulary term you want to explore \nFor example\n");
		response.write("http://vocab-express.nodester.com/exploreTerm?sparqlEndPoint=http://geo.linkeddata.es/sparql&term=http://geo.linkeddata.es/ontology/Laguna\n");
		response.end();
 }
 return;
}



function explore(response, search) {
  console.log("Request handler 'explore' was called.");
  processParameters(response, search);
}

function exploreTerm(response,search) {
  console.log("Request handler 'exploreTerm' was called.");
  processParametersET(response,search);
}

function start(response, search) {
  console.log("Request handler 'start' was called.");
  response.writeHead(200, {"Content-Type": "text/html"});
  fs.readFile('/app/index.html', function (err, data) {
    if (err) {
        throw err; 
    }
    index = data;
    response.writeHeader(200, {"Content-Type": "text/html"});  
    response.write(index);  
    response.end(); 
  });
}

exports.exploreTerm = exploreTerm;
exports.explore = explore;
exports.start = start;
