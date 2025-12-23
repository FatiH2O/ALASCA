package WifiConnector;

import Wifi.BoxWifiImplementationI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class WifiImplementationOutboundPort extends AbstractOutboundPort implements BoxWifiImplementationI{

	
	private static final long serialVersionUID = 1L;

	public WifiImplementationOutboundPort( ComponentI owner)
			throws Exception {
		super( BoxWifiImplementationI.class ,owner);
		
	}
	@Override
	public void turnOn() throws Exception {
		 ((BoxWifiImplementationI)this.getConnector()).turnOn();		
	}

	@Override
	public void turnOff() throws Exception {
		 ((BoxWifiImplementationI)this.getConnector()).turnOff();	

	}

	@Override
	public void activateWifi() throws Exception {
		 ((BoxWifiImplementationI)this.getConnector()).activateWifi();	

		
	}

	@Override
	public void deactivateWifi() throws Exception {
		 ((BoxWifiImplementationI)this.getConnector()).deactivateWifi();		

	}

	@Override
	public BoxWifiMode getMode() throws Exception {
		return 		 ((BoxWifiImplementationI)this.getConnector()).getMode();

}

	@Override
	public boolean isOn() throws Exception {
		return 		 ((BoxWifiImplementationI)this.getConnector()).isOn();	

	}

}
