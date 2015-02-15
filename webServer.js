// LanIr Web API
// by Joe Forbes <joe.forbes@gmail.com>
// based on lirc_web by Alex Bain <alex@alexba.in>

// Set this to true if you'd like to emulate a list of remotes for development
var DEVELOPER_MODE = true;

var express = require('express');
var lirc_node = require('lirc_node');
var logger = require("./logger");

var webServer = module.exports = express();

if (DEVELOPER_MODE) {
	lirc_node.remotes = { 'Foobar': ['foo', 'bar', 'foo/bar'], 'Spam': ['Spam', 'Eggs', 'Bacon'] };
	logger.debug("lirc_node initialized (developer mode).");
} else {
	lirc_node.init();	
	logger.info("lirc_node initialized.");
}

webServer.start = function(port) {
	webServer.listen(port);
	logger.info("lanirWeb listening on port " + port + ".");
};


webServer.get('/', function(req, res) {
        logger.debug("returning list of remotes");
        var result = {};
        var remotes = [];
        for(var r in lirc_node.remotes){
            var remote = {};
            remote.name = r;
            remote.commands = lirc_node.remotes[r];
            remotes.push(remote);
        }
        result.remotes = remotes;
        res.send(result);
});

webServer.post('/remotes/:remote/:command', function(req, res) {
        logger.debug("in send function");
        logger.debug("remote = " + req.params.remote);
        logger.debug("command = " + req.params.command);
        lirc_node.irsend.send_once(req.params.remote, req.params.command, function() {});
        res.setHeader('Cache-Control', 'no-cache');
        res.send(200);
});
