package Connectors;


import Interfaces.TumbleDryerEnergyManagerI;

import fr.sorbonne_u.components.connectors.AbstractConnector;

public class TDEnergyManagerConnector extends AbstractConnector implements TumbleDryerEnergyManagerI {

	

	@Override
	public boolean timedShutdown() throws Exception {
		
		return ((TumbleDryerEnergyManagerI)this.offering).timedShutdown();		
	}

	@Override
	public double getNeededPowerLevel(int modeIndex) throws Exception {
		
		
	 return ((TumbleDryerEnergyManagerI)this.offering).getNeededPowerLevel(modeIndex);		

	}

	@Override
	public void lowerLevel() throws Exception {
		
		((TumbleDryerEnergyManagerI)this.offering).lowerLevel();		

		
	}

	@Override
	public void upperLevel() throws Exception {
		
		((TumbleDryerEnergyManagerI)this.offering).upperLevel();		

		
	}

	
	@Override
	public boolean off() throws Exception {
		
		return ((TumbleDryerEnergyManagerI)this.offering).off();		
		

	}

	@Override
	public boolean shutDown() throws Exception {
		
		return ((TumbleDryerEnergyManagerI)this.offering).shutDown();
	}

	@Override
	public void setPowerLevel(int mode) throws Exception {
		
		((TumbleDryerEnergyManagerI)this.offering).setPowerLevel(mode);
		
	}

	

}
