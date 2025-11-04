package Ports;


import Interfaces.TumbleDryerEnergyManagerI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class TDEnergyManagerInboundPort extends AbstractInboundPort implements TumbleDryerEnergyManagerI{

	private static final long serialVersionUID = 1L;

	public TDEnergyManagerInboundPort(String inboundport ,ComponentI owner) 
			throws Exception {
		
		super(inboundport,TDEnergyManagerInboundPort.class, owner);
		
	}

	

	

	@Override
	public boolean timedShutdown() throws Exception {
		
		return ((TumbleDryerEnergyManagerI)this.owner).timedShutdown();

	}

	

	@Override
	public  double getNeededPowerLevel(int modeIndex) throws Exception {
		
		return 	((TumbleDryerEnergyManagerI)this.owner).getNeededPowerLevel(modeIndex);
	}


	@Override
	public boolean off() throws Exception {
		
		return 	((TumbleDryerEnergyManagerI)this.owner).off();
	}





	@Override
	public void lowerLevel() throws Exception {
		
		((TumbleDryerEnergyManagerI)this.owner).lowerLevel();
		
	}





	@Override
	public void upperLevel() throws Exception {
				
		((TumbleDryerEnergyManagerI)this.owner).upperLevel();

	}





	@Override
	public boolean shutDown() throws Exception {
				return 	((TumbleDryerEnergyManagerI)this.owner).shutDown();
	}





	@Override
	public void setPowerLevel(int mode) throws Exception {
		
		((TumbleDryerEnergyManagerI)this.owner).setPowerLevel(mode);
		
	}

}
