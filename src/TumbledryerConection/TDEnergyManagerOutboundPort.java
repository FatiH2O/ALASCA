package TumbledryerConection;



import Tumbledryer.TumbleDryerEnergyManagerCI;
import Tumbledryer.TumbleDryerEnergyManagerI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class TDEnergyManagerOutboundPort extends AbstractOutboundPort implements TumbleDryerEnergyManagerCI{

	private static final long serialVersionUID = 1L;

	public TDEnergyManagerOutboundPort( ComponentI owner)
			throws Exception {
		super((Class<? extends RequiredCI>) TumbleDryerEnergyManagerCI.class, owner);
			}

	
	

	@Override
	public boolean timedShutdown() {
		
		try {
			return ((TumbleDryerEnergyManagerCI)this.getConnector()).timedShutdown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		return false;
		
	}

	

	@Override
	public  double getNeededPowerLevel(int modeIndex) {
		 try {
			return ((TumbleDryerEnergyManagerCI)this.getConnector()).getNeededPowerLevel(modeIndex);
		} catch (Exception e) {
			
			e.printStackTrace();
			return 0;
		}
	}

	

	@Override
	public boolean off() {
		try {
			return ((TumbleDryerEnergyManagerCI)this.getConnector()).off();
		} catch (Exception e) {
			
			e.printStackTrace();
			return false;
		}
	}




	@Override
	public void lowerLevel() {
		
		try {
			((TumbleDryerEnergyManagerCI)this.getConnector()).lowerLevel();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}




	@Override
	public void upperLevel() {
		
		try {
			((TumbleDryerEnergyManagerCI)this.getConnector()).upperLevel();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}




	@Override
	public boolean shutDown() {
		
		try {
			return ((TumbleDryerEnergyManagerCI)this.getConnector()).shutDown();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return false;
	}




	@Override
	public void setPowerLevel(int mode) {
		
		try {
			((TumbleDryerEnergyManagerCI)this.getConnector()).setPowerLevel(mode);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
