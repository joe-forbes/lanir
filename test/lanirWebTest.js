var server = require('../server');
var request = require('supertest');

require("should");
require("blanket");

describe('lanirWeb', function() {

	describe('routes', function() {

		it('should have an index route accessible via GET', function(done) {
			request(server).get('/').expect(200, done);
		});

		it('should have an API route accessible via POST', function(done) {
			request(server).post('/remotes/tv/power').expect(200, done);
		});

	});

	describe('index action', function() {

		it('should return JSON', function(done) {
			request(server).get('/').expect('Content-Type',  /application\/json/, done);
		});

		it('should return JSON in the format { remotes: [{name: ..., commands: [...]}]}', function(done) {
			request(server).get('/').end(function(err, res) {
				if (err) {
					throw err;
				}
				response = JSON.parse(res.text);
				response.should.have.property("remotes").which.is.instanceOf(Array);
				for ( var i in response.remotes) {
					var remote = response.remotes[i];
					remote.should.have.property("name").which.is.of.type("string");
					remote.should.have.property("commands").which.is.instanceOf(Array);
					for ( var s in remote.commands) {
						remote.commands[s].should.be.of.type("string");
					}
				}
				done();
			});
		});

	});

});
