var Lcd = require('lcd'),
// Developer: By default LCD is connected to following GPIO Pin's 
  lcd = new Lcd({
    rs: 12,
    e: 21,
    data: [5,6,17,18],
    cols: 8,
    rows: 2
  });
var mqtt=require('mqtt'); 
var client=mqtt.connect('mqtt://test.mosquitto.org:1883'); 
client.subscribe('SetTemp'); 	
lcd.on('ready', function() {
client.on('message',function(topic,payload){	
//var data=payload.toString('utf8',7);    
   data=JSON.parse(payload);
  
    lcd.setCursor(0, 0);
    lcd.print("Current Room Temperature is "+ data.tempValue);
	
});		
	}); 
// If ctrl+c is hit, free resources and exit. 
process.on('SIGINT', function() {
  lcd.clear();
  lcd.close();
  process.exit();
});