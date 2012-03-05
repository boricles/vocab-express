var init = require("./lib/init");
var router = require("./lib/router");
var requestHandlers = require("./lib/requestHandlers");

var handle = {}
handle["/"] = requestHandlers.start;
handle["/explore"] = requestHandlers.explore;
handle["/exploreTerm"] = requestHandlers.exploreTerm;

init.start(router.route, handle);

