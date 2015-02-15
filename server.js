/*
 * server.js - main entry point for the application
 * The purpose of server.js is to read configuration from configuration sources 
 * (defaults, command-line, options files, and so on)
 * and start the webServer.
 */

try {
  var util = require("util");
  var logger = require("./logger");
  var configger = require("./configger");
  var packageJson = require('./package.json');
} catch (e) {
  console.log("Error initializing application", e);
  return;
}

var config = configger.load({http:{port:8080}});

logger.addTargets(config.loggingTargets);

logger.info("lanirWeb version: " + packageJson.version);
logger.debug("config: " + util.inspect(config, {depth: null}));
logger.debug("package.json: " + util.inspect(packageJson,{depth: null}));

var webServer = require('./webServer');
webServer.start(config.webServer.port);