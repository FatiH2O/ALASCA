package fr.sorbonne_u.components.hem2025e1.equipments.simpleCharger;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class ChargerOutboundPort extends AbstractOutboundPort implements ChargerCI {
    private static final long serialVersionUID = 1L;

    public ChargerOutboundPort(ComponentI owner) throws Exception {
        super(ChargerCI.class, owner);
    }

    @Override
    public State getState() throws Exception {
        return ((ChargerCI)this.getConnector()).getState();
    }

    @Override
    public void turnOn() throws Exception {
        ((ChargerCI)this.getConnector()).turnOn();
    }

    @Override
    public void turnOff() throws Exception {
        ((ChargerCI)this.getConnector()).turnOff();
    }
}