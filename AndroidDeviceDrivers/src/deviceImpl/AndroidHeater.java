package deviceImpl;

import iotsuite.android.localmiddleware.IDataListener;
import iotsuite.android.localmiddleware.PubSubsSensingFramework;
import android.app.Activity;
import framework.*;

import logic.*;
import android.os.Bundle;



public class AndroidHeater extends Activity implements IHeater, IDataListener {

	private PubSubsSensingFramework pubSubSensingFramework = null;

	// Device Developer has to update manifest file.
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		
		pubSubSensingFramework = PubSubsSensingFramework.getInstance();

		  
				  
				pubSubSensingFramework.registerForSensorData(this,"Off"); 
			  
		  
				  
				pubSubSensingFramework.registerForSensorData(this,"SetTemp"); 
			   
}   

    
    
    @Override
    public void Off() {
     
     } 
      
     
    
    
    @Override
    public void SetTemp(TempStruct arg ) {
     
     } 
      
      
   
	@Override
	public void onDataReceived(String eventName, Object data) {
		System.out.println("Received Data");

	}
 
}