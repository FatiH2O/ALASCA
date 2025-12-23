package Tumbledryer;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

//offered to energy manager 

public interface TumbleDryerEnergyManagerI {
	
	public double getNeededPowerLevel(int modeIndex ) throws Exception;
	
    public void lowerLevel() throws Exception;
    
    public void upperLevel() throws Exception;
    
	public boolean timedShutdown() throws Exception;
		
	public boolean shutDown() throws Exception;
	
	public boolean off() throws Exception;
	
	public void setPowerLevel(int mode) throws Exception;
	
	

}
