var should = require("chai").should();
var jjv = require("jjv")();

var remotesSchema = require("../resources/json-schema/remotes_v0.0.1.schema.json");

jjv.addSchema("remotes", remotesSchema);

var logger = require("../util/logger");

logger.addTarget({
  targetType: 'file', targetConfig: {
    level: 'debug',
    filename: './test.log',
    handleExceptions: true,
    json: true,
    maxsize: 5242880, // 5MB
    maxFiles: 5,
    colorize: false
  }
});

var request = require('supertest');
var proxyquire = require("proxyquire");

var lircNodeStub = {
  init: function () {
    logger.debug("in lircNodeStub.init()");
  },
  remotes: {
    'Foobar': ['foo', 'bar', 'foo/bar'],
    'Spam': ['Spam', 'Eggs', 'Bacon']
  },
  irsend: {
    send_once: function (remote, code, callback) {
      logger.debug("in lircNodeStub.irsend.send_once()");
      callback();
    }
  }
};

var webServer = proxyquire('../webServer', {
  "lirc_node": lircNodeStub
});

webServer.start(1337);

describe('webServer tests', function () {

  describe('route tests', function () {

    it('should have an index route accessible via GET', function (done) {
      request(webServer).get('/').expect(200, done);
    });

    it('should have an API route accessible via POST', function (done) {
      request(webServer).post('/remotes/tv/power').expect(200, done);
    });

  });

  describe('index return format tests', function () {

    it('should return JSON', function (done) {
      request(webServer).get('/').expect('Content-Type', /application\/[^;]*\+json;/, done);

    });

    it('should return JSON that passes JJV validation', function (done) {
      request(webServer).get('/').end(function (err, res) {
        if (err) {
          throw err;
        }
        logger.debug(res.text);
        var response = JSON.parse(res.text);
        var errors = jjv.validate("remotes", response);
        should.not.exist(errors);
        done();
      });
    });

    it('should JSON with the correct content-type', function (done) {
      request(webServer).get('/').end(function (err, res) {
        if (err) {
          throw err;
        }
        res.header['content-type'].split(';')[0].trim().should.equal('application/lanir-remotes_v0.0.1+json');
        done();
      });
    });

    it('should JSON with the correct content-type profile property ', function (done) {
      request(webServer).get('/').end(function (err, res) {
        if (err) {
          throw err;
        }
        var contentTypeArray = res.header['content-type'].split(';');
        contentTypeArray.should.contain('profile=http://github.com/joe-forbes/json-schema/lanir-remotes_v0.0.1#');
        done();
      });
    });

  });

});