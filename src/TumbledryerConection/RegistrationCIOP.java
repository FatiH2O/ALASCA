package TumbledryerConection;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.hem2025.bases.RegistrationCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class RegistrationCIOP extends AbstractOutboundPort  implements RegistrationCI{

	private static final long serialVersionUID = 1L;

	public RegistrationCIOP( ComponentI owner) throws Exception {
		super(RegistrationCI.class, owner);
		
	}

	@Override
	public boolean registered(String uid) throws Exception {
		
		return ((RegistrationCI)this.getConnector()).registered(uid);
	}

	@Override
	public boolean register(String uid, String controlPortURI, String xmlControlAdapter) throws Exception {
		
		return  ((RegistrationCI)this.getConnector()).register(uid,controlPortURI,xmlControlAdapter);
	}

	@Override
	public void unregister(String uid) throws Exception {
		
		((RegistrationCI)this.getConnector()).unregister(uid);
		
	}

}
