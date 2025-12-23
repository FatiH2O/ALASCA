package Wifi;

import fr.sorbonne_u.components.connectors.AbstractConnector;

public class BoxWifiConnector extends AbstractConnector implements BoxWifiUserCI{

	@Override
	public void turnOn() throws Exception {
		 ((BoxWifiImplementationI)this.offering).turnOn();
	}

	@Override
	public void turnOff() throws Exception {
		 ((BoxWifiImplementationI)this.offering).turnOff();

	}

	@Override
	public void activateWifi() throws Exception {
		 ((BoxWifiImplementationI)this.offering).activateWifi();

	}

	@Override
	public void deactivateWifi() throws Exception {
		 ((BoxWifiImplementationI)this.offering).deactivateWifi();

	}

	@Override
	public BoxWifiMode getMode() throws Exception {
		return ((BoxWifiImplementationI)this.offering).getMode();

	}

	@Override
	public boolean isOn() throws Exception {
		return 		 ((BoxWifiImplementationI)this.offering).isOn();

	}

}
