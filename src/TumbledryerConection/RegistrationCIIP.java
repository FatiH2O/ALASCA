package TumbledryerConection;


import Hem.HEM;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.hem2025.bases.RegistrationCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class RegistrationCIIP extends AbstractInboundPort implements RegistrationCI {

	
	public RegistrationCIIP(String uri, ComponentI owner)
			throws Exception {
		super(uri, RegistrationCI.class, owner);
		
		assert owner instanceof HEM;
	}

	private static final long serialVersionUID = 1L;

	

	@Override
	public boolean registered(String uid) throws Exception {
		
		return ((HEM)this.owner).registered(uid);
	}

	@Override
	public boolean register(String uid, String controlPortURI, String xmlControlAdapter) throws Exception {
		
		
		return ((HEM)this.owner).register(uid,controlPortURI,xmlControlAdapter);
	}

	@Override
	public void unregister(String uid) throws Exception {
		
		((HEM)this.owner).unregister(uid);
	}

}
