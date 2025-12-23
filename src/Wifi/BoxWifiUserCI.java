package Wifi;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface  BoxWifiUserCI extends OfferedCI, RequiredCI, BoxWifiImplementationI {
	
	@Override

	public void turnOn() throws Exception;

	@Override

	    /** Éteint complètement la box (WiFi coupé aussi). */
	    public void turnOff() throws Exception;

	@Override

	    /** Active le WiFi (si la box est allumée). */
	    public void activateWifi() throws Exception;

	@Override

	    /** Désactive le WiFi (mais garde la box allumée). */
	    public void deactivateWifi() throws Exception;

	@Override

	    /** Retourne le mode courant de la box. */
	    public BoxWifiMode getMode() throws Exception;

	@Override

	    /** Vérifie si la box est allumée. */
	    public boolean isOn() throws Exception;

}
