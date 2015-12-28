var mqtt=require('mqtt'); 
var client=mqtt.connect('mqtt://test.mosquitto.org:1883');
client.subscribe('notifyAlarm');  
console.log("Alarm is ready to receive Fire State"); 
client.on('message',function(topic,payload){	
//sensorMeasurement=JSON.parse(payload); 
var Gpio = require('onoff').Gpio,
// Developer: By default Alarm is connected to GPIO3 (i.e. Pin 5)
  buzzer = new Gpio(3, 'out'); 
setTimeout(function(){
buzzer.unexport();   
},10000);
}); 
function exit() {
console.log("Good Bye!");
  process.exit();
} 
process.on('SIGINT', exit);