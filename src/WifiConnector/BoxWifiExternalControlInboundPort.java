package WifiConnector;

import Wifi.BoxWifiAdjustableAdapterCI;
import Wifi.BoxWifiExternalControlCI;
import Wifi.BoxWifiExternalControlI;
import Wifi.BoxWifiImplementationI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;


public class BoxWifiExternalControlInboundPort
        extends AbstractInboundPort
        implements BoxWifiExternalControlCI { 

    private static final long serialVersionUID = 1L;

    public BoxWifiExternalControlInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, BoxWifiExternalControlCI.class, owner); 
        assert owner instanceof BoxWifiExternalControlCI;
    }

   
    @Override
    public double getMaxPowerLevel() throws Exception {
        return this.getOwner().handleRequest(
            o -> ((BoxWifiExternalControlCI) o).getMaxPowerLevel()
        );
    }

    @Override
    public void setCurrentPowerLevel(double powerLevel) throws Exception {
        this.getOwner().handleRequest(
            o -> {
                ((BoxWifiExternalControlCI) o).setCurrentPowerLevel(powerLevel);
                return null;
            }
        );
    }

    @Override
    public double getCurrentPowerLevel() throws Exception {
        return this.getOwner().handleRequest(
            o -> ((BoxWifiExternalControlCI) o).getCurrentPowerLevel()
        );
    }


    @Override
    public void turnOn() throws Exception {
        this.getOwner().handleRequest(
            o -> {
                ((BoxWifiImplementationI) o).turnOn();
                return false;
            }
        );
    }



	@Override
	public void turnOff() throws Exception {
		this.getOwner().handleRequest(
	            o -> {
	                ((BoxWifiImplementationI) o).turnOff();
	                return false;
	            }
	        );
		
	}


	@Override
	public void activateWifi() throws Exception {
		this.getOwner().handleRequest(
	            o -> {
	                ((BoxWifiImplementationI) o).activateWifi();
	                return false;
	            }
	        );
		
	}


	@Override
	public void deactivateWifi() throws Exception {
			this.getOwner().handleRequest(
		            o -> {
		                ((BoxWifiImplementationI) o).deactivateWifi();
		                return false;
		            }
		        );
	}


	@Override
	public BoxWifiMode getMode() throws Exception {
		return this.getOwner().handleRequest(
	            o -> ((BoxWifiImplementationI) o).getMode()
	        );
	}


	@Override
	public boolean isOn() throws Exception {
		return this.getOwner().handleRequest(
	            o -> ((BoxWifiImplementationI) o).isOn()
	        );
	}

	
   

   
    
   
}