var server = require("./lib/server");
var router = require("./lib/router");
var requestHandlers = require("./lib/requestHandlers");

var handle = {}
handle["/explore"] = requestHandlers.explore;

server.start(router.route, handle);

