package Connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.hem2025.bases.RegistrationCI;

public class RegistationCIConnector extends AbstractConnector implements RegistrationCI{

	@Override
	public boolean registered(String uid) throws Exception {
		
		
		return ((RegistrationCI) this.offering).registered(uid);
	}

	@Override
	public boolean register(String uid, String controlPortURI, String xmlControlAdapter) throws Exception {
		// TODO Auto-generated method stub
		return ((RegistrationCI) this.offering).register(uid,controlPortURI,xmlControlAdapter);
	}

	@Override
	public void unregister(String uid) throws Exception {
		
		((RegistrationCI)this.offering).unregister(uid);

	}
	

}
