// LanIr Web API
// by Joe Forbes <joe.forbes@gmail.com>
// based on lirc_web by Alex Bain <alex@alexba.in>

// Set this to true if you'd like to emulate a list of remotes for development
var DEVELOPER_MODE = true;

var express = require('express');
var lirc_node = require('lirc_node');

var app = module.exports = express();
var port = 1337;

if (DEVELOPER_MODE) {
	lirc_node.remotes = { 'Foobar': ['foo', 'bar', 'foo/bar'], 'Spam': ['Spam', 'Eggs', 'Bacon'] };
	console.log("lirc_node initialized (developer mode).");
} else {
	lirc_node.init();	
	console.log("lirc_node initialized.");
}

app.listen(port);
console.log("lanirWeb listening on port " + port + ".");

app.get('/', function(req, res) {
        console.log("returning list of remotes");
        //translate to something that will deserialize nicely to Java
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

app.post('/remotes/:remote/:command', function(req, res) {
        console.log("in send function");
        console.log("remote = " + req.params.remote);
        console.log("command = " + req.params.command);
        lirc_node.irsend.send_once(req.params.remote, req.params.command, function() {});
        res.setHeader('Cache-Control', 'no-cache');
        res.send(200);
});
