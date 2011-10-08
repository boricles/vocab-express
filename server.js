var init = require("./lib/init");
var router = require("./lib/router");
var requestHandlers = require("./lib/requestHandlers");

var handle = {}
handle["/explore"] = requestHandlers.explore;

init.start(router.route, handle);

