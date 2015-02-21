var should = require("should");
require("blanket");
var util = require("util");
var jjv = require("jjv")();

var remotesSchema = require("../resources/json-schema/remotes.json");

jjv.addSchema("remotes", remotesSchema);

var logger = require("../util/logger");

logger.addTarget({
	targetType : "console"
});

var request = require('supertest');
var proxyquire = require("proxyquire");

var lircNodeStub = {
		init : function() {
			logger.debug("in lircNodeStub.init()");
		},
		remotes : {
			'Foobar' : [ 'foo', 'bar', 'foo/bar' ],
			'Spam' : [ 'Spam', 'Eggs', 'Bacon' ]
		},
		irsend : {
				send_once : function(remote, code, callback) {
					logger.debug("in lircNodeStub.irsend.send_once()");
					callback();
				}
		}
};

var webServer = proxyquire('../webServer', {
	"lirc_node" : lircNodeStub
});

webServer.start(1337);

describe('webServer tests', function() {

	describe('route tests', function() {

		it('should have an index route accessible via GET', function(done) {
			request(webServer).get('/').expect(200, done);
		});

		it('should have an API route accessible via POST', function(done) {
			request(webServer).post('/remotes/tv/power').expect(200, done);
		});

	});

	describe('index return format tests', function() {

		it('should return JSON', function(done) {
			request(webServer).get('/').expect('Content-Type', /application\/json/, done);
			
		});

		it('should return JSON that passes JJV validation', function(done) {
			request(webServer).get('/').end(function(err, res) {
				if (err) {
					throw err;
				}
				logger.debug(res.text);
				response = JSON.parse(res.text);
				var errors = jjv.validate("remotes", response);
				should.not.exist(errors);
				done();
			});
		});

	});

});
