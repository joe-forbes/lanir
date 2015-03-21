// LanIr Web API
// by Joe Forbes <joe.forbes@gmail.com>
// based on lirc_web by Alex Bain <alex@alexba.in>

var express = require('express');
var lirc_node = require('lirc_node');
var logger = require("./util/logger");

var webServer = module.exports = express();

webServer.start = function (port) {
  lirc_node.init();
  logger.debug("lirc_node initialized.");
  webServer.listen(port);
  logger.info("lanirWeb listening on port " + port + ".");
};

webServer.get('/', function (req, res) {
  logger.debug("returning list of remotes");
  var result = {};
  var remotes = [];
  for (var r in lirc_node.remotes) {
    if (lirc_node.remotes.hasOwnProperty(r)) {
      var remote = {};
      remote.name = r;
      remote.commands = lirc_node.remotes[r];
      remotes.push(remote);
    }
  }
  result.remotes = remotes;
  res.setHeader('content-type', 'application/lanir-remotes_v0.0.1+json;profile=http://github.com/joe-forbes/json-schema/lanir-remotes_v0.0.1#');
  res.send(result);
});

webServer.post('/remotes/:remote/:command', function (req, res) {
  logger.debug("in send function");
  logger.debug("remote = " + req.params.remote);
  logger.debug("command = " + req.params.command);
  lirc_node.irsend.send_once(req.params.remote, req.params.command, function () {
  });
  res.setHeader('Cache-Control', 'no-cache');
  res.send(200);
});