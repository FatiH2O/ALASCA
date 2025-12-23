package WifiConnector;

import Wifi.BoxWifiUserCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class WifiUserInboundPort extends AbstractInboundPort implements BoxWifiUserCI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WifiUserInboundPort(Class<? extends OfferedCI> implementedInterface, ComponentI owner) throws Exception {
		super(implementedInterface, owner);
		
	}

	@Override
	public void turnOn() throws Exception {
		this.getOwner().handleRequest(
				o -> {
					((BoxWifiUserCI) o).turnOn();
					return null;
				}
			);
	}

	@Override
	public void turnOff() throws Exception {
		this.getOwner().handleRequest(
				o -> {
					((BoxWifiUserCI) o).turnOff();
					return null;
				}
			);
	}

	@Override
	public void activateWifi() throws Exception {
		this.getOwner().handleRequest(
				o -> {
					((BoxWifiUserCI) o).activateWifi();
					return null;
				}
			);
	}

	@Override
	public void deactivateWifi() throws Exception {
		this.getOwner().handleRequest(
				o -> {
					((BoxWifiUserCI) o).deactivateWifi();
					return null;
				}
			);
	}

	@Override
	public BoxWifiMode getMode() throws Exception {
		return this.getOwner().handleRequest(
				o -> {
					((BoxWifiUserCI) o).getMode();
					return null;
				}
			);
	}

	@Override
	public boolean isOn() throws Exception {
		return this.getOwner().handleRequest(
				o -> {
					((BoxWifiUserCI) o).isOn();
					return null;
				}
			);
	}

}
