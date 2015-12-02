package iotsuite.java.extended.simulated.actuatingframework;

import iotsuite.localmiddleware.IDataListener;
import iotsuite.localmiddleware.PubSubsSensingFramework;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.google.gson.JsonObject;

public class SimulatedHeater implements IDataListener {
	
	private PubSubsSensingFramework pubSubSensingFramework = null;
	static JFrame aWindow = new JFrame("This is Simulated Heater");
	  static JLabel jlabel1 = new JLabel();
	  static boolean flag = true;
	  
	  public SimulatedHeater(){
		  
		  pubSubSensingFramework = PubSubsSensingFramework.getInstance();
		   pubSubSensingFramework.registerForSensorData(this,"Off");  
		   pubSubSensingFramework.registerForSensorData(this,"SetTemp");  
		   
		  if (flag) {
		      flag = false;
		      aWindow.setBounds(50, 100, 400, 150);
		      aWindow.setLayout(new FlowLayout());
		      aWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		      aWindow.add(jlabel1);
		      aWindow.setVisible(true);
		    }
		  
	  }

	@Override
	public void onDataReceived(String eventName, JsonObject data) {
		
		if(eventName.equals("Off"))
		{
			jlabel1.setText("Off command is fired");
			
		}
		if(eventName.equals("SetTemp"))
		{
			
			jlabel1.setText("SetTemp command is fired");
		}
		
	}
	 
}
