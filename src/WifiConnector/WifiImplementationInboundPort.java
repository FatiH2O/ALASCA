package WifiConnector;

import Wifi.BoxWifiImplementationI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class WifiImplementationInboundPort extends AbstractInboundPort implements BoxWifiImplementationI {

	private static final long serialVersionUID = 1L;

	public WifiImplementationInboundPort(Class<? extends OfferedCI> implementedInterface, ComponentI owner)
			throws Exception {
		super(implementedInterface, owner);
	}

	public WifiImplementationInboundPort(ComponentI owner) throws Exception {
		super((Class<? extends OfferedCI>) BoxWifiImplementationI.class, owner);
	}

	@Override
	public void turnOn() throws Exception {
		this.getOwner().handleRequest(
			o -> {
				((BoxWifiImplementationI) o).turnOn();
				return null;
			}
		);
	}

	@Override
	public void turnOff() throws Exception {
		this.getOwner().handleRequest(
			o -> {
				((BoxWifiImplementationI) o).turnOff();
				return null;
			}
		);
	}

	@Override
	public void activateWifi() throws Exception {
		this.getOwner().handleRequest(
			o -> {
				((BoxWifiImplementationI) o).activateWifi();
				return null;
			}
		);
	}

	@Override
	public void deactivateWifi() throws Exception {
		this.getOwner().handleRequest(
			o -> {
				((BoxWifiImplementationI) o).deactivateWifi();
				return null;
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
