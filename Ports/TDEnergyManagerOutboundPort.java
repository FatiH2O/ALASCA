package Ports;



import Interfaces.TumbleDryerEnergyManagerI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class TDEnergyManagerOutboundPort extends AbstractOutboundPort implements TumbleDryerEnergyManagerI{

	private static final long serialVersionUID = 1L;

	public TDEnergyManagerOutboundPort( ComponentI owner)
			throws Exception {
		super((Class<? extends RequiredCI>) TumbleDryerEnergyManagerI.class, owner);
			}

	
	

	@Override
	public boolean timedShutdown() {
		
		try {
			return ((TumbleDryerEnergyManagerI)this.getConnector()).timedShutdown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		return false;
		
	}

	

	@Override
	public  double getNeededPowerLevel(int modeIndex) {
		 try {
			return ((TumbleDryerEnergyManagerI)this.getConnector()).getNeededPowerLevel(modeIndex);
		} catch (Exception e) {
			
			e.printStackTrace();
			return 0;
		}
	}

	

	@Override
	public boolean off() {
		try {
			return ((TumbleDryerEnergyManagerI)this.getConnector()).off();
		} catch (Exception e) {
			
			e.printStackTrace();
			return false;
		}
	}




	@Override
	public void lowerLevel() {
		
		try {
			((TumbleDryerEnergyManagerI)this.getConnector()).lowerLevel();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}




	@Override
	public void upperLevel() {
		
		try {
			((TumbleDryerEnergyManagerI)this.getConnector()).upperLevel();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}




	@Override
	public boolean shutDown() {
		
		try {
			return ((TumbleDryerEnergyManagerI)this.getConnector()).shutDown();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return false;
	}




	@Override
	public void setPowerLevel(int mode) {
		
		try {
			((TumbleDryerEnergyManagerI)this.getConnector()).setPowerLevel(mode);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
