package fr.sorbonne_u.components.hem2025e1.equipments.simpleCharger;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class ChargerInboundPort extends AbstractInboundPort implements ChargerCI {
    private static final long serialVersionUID = 1L;

    public ChargerInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, ChargerCI.class, owner);
    }

    @Override
    public State getState() throws Exception {
        return this.getOwner().handleRequest(o -> ((ChargerI)o).getState());
    }

    @Override
    public void turnOn() throws Exception {
        this.getOwner().handleRequest(o -> { ((ChargerI)o).turnOn(); return null; });
    }

    @Override
    public void turnOff() throws Exception {
        this.getOwner().handleRequest(o -> { ((ChargerI)o).turnOff(); return null; });
    }
}