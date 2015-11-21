package factory;

import framework.*;
import android.app.Activity;
import logic.*;
import deviceImpl.*;
import iotsuite.common.GlobalVariable;


public class  TemperatureSensorFactory  { 
   
   public static ITemperatureSensor getTemperatureSensor(String nameTemperatureSensor, Activity context,
     LogicTemperatureSensor obj) {  
	 
			
       if (nameTemperatureSensor.equals(GlobalVariable.deviceAndroidType))  
           return new AndroidTemperatureSensor(context, obj);  
                
        return null;       
    }
}