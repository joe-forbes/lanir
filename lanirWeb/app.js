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
