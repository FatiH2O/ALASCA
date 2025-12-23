package Charger;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class ChargerUserInboundPort
extends     AbstractInboundPort
implements  ChargerUserCI
{
    private static final long serialVersionUID = 1L;

    public ChargerUserInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, ChargerUserCI.class, owner);
    }

    @Override
    public boolean isPluggedIn() throws Exception {
        return this.getOwner().handleRequest(o -> ((ChargerUserI)o).isPluggedIn());
    }

    @Override
    public void plugin() throws Exception {
        this.getOwner().handleRequest(o -> { ((ChargerUserI)o).plugin(); return null; });
    }

    @Override
    public void plugout() throws Exception {
        this.getOwner().handleRequest(o -> { ((ChargerUserI)o).plugout(); return null; });
    }
}