var app = require('../app');
var assert = require('assert');
var request = require('supertest');
var sinon = require('sinon');


describe('lanirWeb', function() {

    describe('routes', function() {

        it('should have an index route accessible via GET', function(done) {
            assert(request(app).get('/').expect(200, done));
        });

        it('should have an API route accessible via POST', function(done) {
            assert(request(app).post('/remotes/tv/power').expect(200, done));
        });

    });

    describe('index action', function() {

        it('should return JSON', function(done) {
            assert(request(app).get('/').expect('Content-Type', /application\/json/, done));
        });

        it('should return JSON in the format { remotes: [{name: ..., commands: [...]}]}', function(done) {
			request(app)
			.get('/')
			.end(function(err, res) {
				assert.equal(err, null);
				json = JSON.parse(res.text);
				var keys = Object.keys(json);
				assert(keys.indexOf('remotes') != -1);
				assert(isArray(json.remotes));
				for (var i in json.remotes) {
					var remote = json.remotes[i];
					keys = Object.keys(remote);
					assert(keys.indexOf('name') != -1);
					assert(!isArray(remote.name));
					assert(typeof remote.name != 'object');
					assert(keys.indexOf('commands') != -1);
					assert(isArray(remote.commands));
					for (var s in remote.commands) {
						var string = remote.commands[s];
						assert(!isArray(string));
						assert(typeof string != 'object');
					}
				}
			    done();
			});
        });

    });

});

function isArray(what) {
    return Object.prototype.toString.call(what) === '[object Array]';
}