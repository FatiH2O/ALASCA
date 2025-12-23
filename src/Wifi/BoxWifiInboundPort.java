package Wifi;

import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.exceptions.PreconditionException;
import fr.sorbonne_u.components.ComponentI;


public class BoxWifiInboundPort extends AbstractInboundPort
implements BoxWifiUserCI {
	  private static final long serialVersionUID = 1L;

	    // Constructeur sans URI
	    public BoxWifiInboundPort(ComponentI owner) throws Exception {
	        super(BoxWifiImplementationI.class, owner);
	        assert owner instanceof BoxWifiImplementationI :
	            new PreconditionException("owner must implement BoxWifiImplementationI");
	    }

	    // Constructeur avec URI
	    public BoxWifiInboundPort(String uri, ComponentI owner) throws Exception {
	        super(uri, BoxWifiImplementationI.class, owner);
	        assert owner instanceof BoxWifiImplementationI :
	            new PreconditionException("owner must implement BoxWifiImplementationI");
	    }

	    // Méthodes exposées

	    @Override
	    public void turnOn() throws Exception {
	        this.getOwner().handleRequest(
	            o -> { ((BoxWifiImplementationI)o).turnOn(); return null; });
	    }

	    @Override
	    public void turnOff() throws Exception {
	        this.getOwner().handleRequest(
	            o -> { ((BoxWifiImplementationI)o).turnOff(); return null; });
	    }

	    @Override
	    public void activateWifi() throws Exception {
	        this.getOwner().handleRequest(
	            o -> { ((BoxWifiImplementationI)o).activateWifi(); return null; });
	    }

	    @Override
	    public void deactivateWifi() throws Exception {
	        this.getOwner().handleRequest(
	            o -> { ((BoxWifiImplementationI)o).deactivateWifi(); return null; });
	    }

	    @Override
	    public BoxWifiMode getMode() throws Exception {
	        return this.getOwner().handleRequest(
	            o -> ((BoxWifiImplementationI)o).getMode());
	    }

	    @Override
	    public boolean isOn() throws Exception {
	        return this.getOwner().handleRequest(
	            o -> ((BoxWifiImplementationI)o).isOn());
	    }

}
