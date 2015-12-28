var YQL = require('yqlp');
var mqtt = require('mqtt');
var client = mqtt.connect('mqtt://test.mosquitto.org:1883');
client.subscribe('getTemp');
client.on('message', function(topic, payload) {
	//var data =payload.toString('utf8');
	var sensorMeasurement = JSON.parse(payload);
	//sensorMeasurement=JSON.parse(data);	
	YQL.exec("SELECT * FROM weather.forecast WHERE woeid=@woeid AND u='c'", {
		woeid : sensorMeasurement.woeid
	}, function(error, response) {
		if (error) {
			console.log('Ut oh! Example #1 has messed up:', error);
		} else {
			var results = response.query.results;
			//console.log(results.channel.item.condition.temp);
			var data = {
				"tempValue" : parseFloat(results.channel.item.condition.temp),
				"unitOfMeasurement" : 'C'
			};
			client.publish('weatherMeasurement', JSON.stringify(data));
			console.log("Publishing Data weatherMeasurement: YahooWeatherService ");
		}
	});
});
