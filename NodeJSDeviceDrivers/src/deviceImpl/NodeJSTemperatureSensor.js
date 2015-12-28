var sensorLib = require('node-dht-sensor'); 
var mqtt=require('mqtt'); 
var client=mqtt.connect('mqtt://test.mosquitto.org:1883');  
var sensor = {
    initialize: function () {
		//Developer: By default TemperatureSensor is connected to GPIO4 (i.e. Pin 7)
        return sensorLib.initialize(22, 4); 
    },
    read: function () {
        var readout = sensorLib.read();
        var data={"tempValue":readout.temperature.toFixed(2),"unitOfMeasurement":"C"}; 
	client.publish('tempMeasurement',JSON.stringify(data));  
       console.log("Publishing tempMeasurement: TemperatureSensor");
        setTimeout(function () {
            sensor.read();
        }, 5000);
    }
}; 
if (sensor.initialize()) {
    sensor.read();
} else {
    console.warn('Failed to initialize TemperatureSensor');
}