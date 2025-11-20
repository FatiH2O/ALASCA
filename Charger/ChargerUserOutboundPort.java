package Charger;

import fr.sorbonne_u.components.ComponentI;

import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class ChargerUserOutboundPort
extends     AbstractOutboundPort
implements  ChargerUserCI
{
    private static final long serialVersionUID = 1L;

    public ChargerUserOutboundPort(ComponentI owner) throws Exception {
        super(ChargerUserCI.class, owner);
    }

    public ChargerUserOutboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, ChargerUserCI.class, owner);
    }

    @Override
    public boolean isPluggedIn() throws Exception {
        return ((ChargerUserCI)this.getConnector()).isPluggedIn();
    }

    @Override
    public void plugin() throws Exception {
        ((ChargerUserCI)this.getConnector()).plugin();
    }

    @Override
    public void plugout() throws Exception {
        ((ChargerUserCI)this.getConnector()).plugout();
    }
}