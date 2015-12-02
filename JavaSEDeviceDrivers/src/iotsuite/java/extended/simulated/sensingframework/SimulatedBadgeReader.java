package iotsuite.java.extended.simulated.sensingframework;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.gson.JsonObject;

import iotsuite.localmiddleware.IDataListener;
import iotsuite.localmiddleware.PubSubsSensingFramework;

public class SimulatedBadgeReader implements IDataListener {
	
	private PubSubsSensingFramework pubSubSensingFramework = null;
	  private JFrame mainFrame;
	  private JLabel headerLabel;
	  private JLabel statusLabel;
	  private JPanel controlPanel;

	       
	String badgeID;
	
	 public SimulatedBadgeReader() {
		 pubSubSensingFramework = PubSubsSensingFramework.getInstance();
		    pubSubSensingFramework.registerForSensorData(this,"BadgeReader");  
		    prepareGUI();
		     showEventDemo();
	
	}


	@Override
	public void onDataReceived(String eventName, JsonObject data) {
		
		if(eventName.equals("BadgeReader"))
		{
			badgeID=data.get("badgeID").getAsString();	
		}
		
	}
	
	private void prepareGUI()
	  {
	  mainFrame= new JFrame("Simulated BadgeReader");
	  mainFrame.setSize(200,200);
	  mainFrame.setLayout(new GridLayout(3,1));
	  
	  headerLabel= new JLabel("",JLabel.CENTER);
	  statusLabel= new JLabel("",JLabel.CENTER);
	  
	  //jtfTextField = new JTextField(5);
	  statusLabel.setSize(350,100);
	  
	  mainFrame.addWindowListener(new WindowAdapter(){
	  
	  public void windowClosing(WindowEvent windowEvent){
	  System.exit(0);
	  }
	  });
	  
	  controlPanel = new JPanel();
	  controlPanel.setLayout(new FlowLayout());
	  mainFrame.add(headerLabel);
	  mainFrame.add(controlPanel);
	  mainFrame.add(statusLabel);

	  mainFrame.setVisible(true);
	  }
	  
	  
	  public void showEventDemo(){
	  

	      
	      JButton badgeDetectedButton = new JButton("badgeDetected");
	       
	      badgeDetectedButton.setActionCommand("badgeDetected");
	       
	      badgeDetectedButton.addActionListener(new ButtonClickListener());
	       
	      controlPanel.add(badgeDetectedButton);
	      
	       

	      
	      JButton badgeDisappearedButton = new JButton("badgeDisappeared");
	       
	      badgeDisappearedButton.setActionCommand("badgeDisappeared");
	       
	      badgeDisappearedButton.addActionListener(new ButtonClickListener());
	       
	      controlPanel.add(badgeDisappearedButton);
	      
	       
	  mainFrame.setVisible(true);
	  }
	  
	  
	  private class ButtonClickListener implements ActionListener{
	  public void actionPerformed(ActionEvent e){
	  

	        boolean badgeDetectedCondition = false;    
	         

	        boolean badgeDisappearedCondition = false;    
	         
	      

	              if(e.getActionCommand().equals("badgeDetected"))
	              {
	              badgeDetectedCondition=true;
	              }
	               

	              if(e.getActionCommand().equals("badgeDisappeared"))
	              {
	              badgeDisappearedCondition=true;
	              }
	               
	  

	       if(badgeDetectedCondition)
	      { 
	      
	       statusLabel.setText(" badgeDetected event is happened.....");
	       
	       JsonObject publishData = new JsonObject();
			publishData.addProperty("badgeID",badgeID);
			publishData.addProperty("badgeEvent", "badgeDetected");
			pubSubSensingFramework.publish("badgeDetected", publishData);
	     
	     }
	    
	      

	       if(badgeDisappearedCondition)
	      { 
	      
	       statusLabel.setText(" badgeDisappeared event is happened.....");
	       JsonObject publishData = new JsonObject();
			publishData.addProperty("badgeID",badgeID);
			publishData.addProperty("badgeEvent", "badgeDisappeared");
			pubSubSensingFramework.publish("badgeDisappeared", publishData);
	     }
	    
	      
	 }
	  }
}
