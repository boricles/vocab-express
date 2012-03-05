var http = require("http");
var url = require("url");

function start(route, handle) {
  function onRequest(request, response) {

    //console.log(" ----------- url " + request.url);
    var postData = "";
    var pathname = url.parse(request.url).pathname;
    
    var search = url.parse(request.url).search;
    
    
    
    console.log("Request for " + pathname + " received.");

    console.log("Request for ...." + search + " received.");

    request.setEncoding("utf8");

    request.addListener("end", function() {
      route(handle, pathname, response, search);
    });

  }

  http.createServer(onRequest).listen(12192);
  console.log("Server has started.");
}

exports.start = start;
