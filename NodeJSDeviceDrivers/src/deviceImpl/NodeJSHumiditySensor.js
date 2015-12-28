var sensorLib = require('node-dht-sensor'); 
var mqtt=require('mqtt'); 
var client=mqtt.connect('mqtt://test.mosquitto.org:1883');  
var sensor = {
    initialize: function () {
		// Developer: By default HumiditySensor is connected to GPIO4 (i.e. Pin 7)
        return sensorLib.initialize(22, 4); 
    },
    read: function () {
        var readout = sensorLib.read();
        var data={"humidityValue": readout.humidity.toFixed(2)}; 
	client.publish('humidityMeasurement',JSON.stringify(data));  
        console.log("Publishing humidityMeasurement: HumiditySensor");
        setTimeout(function () {
            sensor.read();
        }, 5000);
    }
}; 
if (sensor.initialize()) {
    sensor.read();
} else {
    console.warn('Failed to initialize HumiditySensor');
}