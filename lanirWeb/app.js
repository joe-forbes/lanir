var express = require('express');
var lirc_node = require('lirc_node');

var app = express();
var port = 1337;

lirc_node.init();
console.log("lirc_node initialized.");

app.listen(port);
console.log("lanirWeb listening on port " + port + ".");

app.get('/', function(req, res) {
	console.log("returning list of remotes");
	res.send(lirc_node.remotes);
});

app.get('/:remote/:command', function(req, res) {
	console.log("in send function");
	console.log("remote = " + req.params.remote);
	console.log("command = " + req.params.command);
	lirc_node.irsend.send_once(req.params.remote, req.params.command, function() {});
	res.setHeader('Cache-Control', 'no-cache');
	res.send(200);
});
