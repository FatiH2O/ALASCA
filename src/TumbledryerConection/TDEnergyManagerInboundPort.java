package TumbledryerConection;


import Tumbledryer.TumbleDryerEnergyManagerCI;
import Tumbledryer.TumbleDryerEnergyManagerI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class TDEnergyManagerInboundPort extends AbstractInboundPort implements TumbleDryerEnergyManagerCI{

	private static final long serialVersionUID = 1L;

	public TDEnergyManagerInboundPort(String inboundport ,ComponentI owner) 
			throws Exception {
		
		super(inboundport,TumbleDryerEnergyManagerCI.class, owner);
		
	}

	

	@Override
	public boolean timedShutdown() throws Exception {
		
		return ((TumbleDryerEnergyManagerCI)this.owner).timedShutdown();

	}

	

	@Override
	public  double getNeededPowerLevel(int modeIndex) throws Exception {
		
		return 	((TumbleDryerEnergyManagerCI)this.owner).getNeededPowerLevel(modeIndex);
	}


	@Override
	public boolean off() throws Exception {
		
		return 	((TumbleDryerEnergyManagerCI)this.owner).off();
	}





	@Override
	public void lowerLevel() throws Exception {
		
		((TumbleDryerEnergyManagerCI)this.owner).lowerLevel();
		
	}





	@Override
	public void upperLevel() throws Exception {
				
		((TumbleDryerEnergyManagerCI)this.owner).upperLevel();

	}





	@Override
	public boolean shutDown() throws Exception {
				return 	((TumbleDryerEnergyManagerCI)this.owner).shutDown();
	}





	@Override
	public void setPowerLevel(int mode) throws Exception {
		
		((TumbleDryerEnergyManagerCI)this.owner).setPowerLevel(mode);
		
	}

}
