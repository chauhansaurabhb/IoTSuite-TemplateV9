var upmMQ2 = require("jsupm_gas"); 
// Attach gas sensor to AIO0 
var myMQ2 = new upmMQ2.MQ2(21);
var threshContext = new upmMQ2.thresholdContext;
var mqtt=require('mqtt'); 
var client=mqtt.connect('mqtt://test.mosquitto.org:1883');  
threshContext.averageReading = 0;
threshContext.runningAverage = 0;
threshContext.averagedOver = 2;
// Infinite loop, ends when script is cancelled 
// Repeatedly, take a sample every 2 milliseconds; 
// find the average of 128 samples; and 
// print a running graph of the averages using a resolution of 5 
while(1)
{
    var buffer = new upmMQ2.uint16Array(128);
    var len = myMQ2.getSampledWindow(2, 128, buffer);
    if (len)
    {
        var thresh = myMQ2.findThreshold(threshContext, 30, buffer, len);
        myMQ2.printGraph(threshContext, 5);
        if (thresh) 
	{
	client.publish('smokeMeasurement',JSON.stringify(thresh));  
	console.log("Publishing smokeMeasurement: SmokeDetector");
        //console.log("Threshold is " + thresh);
	}
    }
}
// Print message when exiting  
process.on('SIGINT', function()
{
	console.log("Exiting...");
	process.exit(0);
});