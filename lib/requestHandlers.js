var querystring = require("querystring");
var sparql = require("./sparql");
var fs = require("fs");
var index;


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
		response.write("http://vocab-express.nodester.com/explore?sparqlEndPoint=http://geo.linkeddata.es/sparql\n");
		response.write("http://vocab-express.nodester.com/explore?voidDescription=http://geo.linkeddata.es/data/void.ttl\n");
		response.end();
	  }
  }
  else {
	response.writeHead(200, {"Content-Type": "text/plain"});
	response.write("You should specify either a SPARQLEndpoint or a void file description\nFor example\n");
	response.write("http://vocab-express.nodester.com/explore?sparqlEndPoint=http://geo.linkeddata.es/sparql\n");
	response.write("http://vocab-express.nodester.com/explore?voidDescription=http://geo.linkeddata.es/data/void.ttl\n");
	response.end();
 }

}

function explore(response, search) {
  console.log("Request handler 'explore' was called.");
  processParameters(response, search);
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

exports.explore = explore;
exports.start = start;
