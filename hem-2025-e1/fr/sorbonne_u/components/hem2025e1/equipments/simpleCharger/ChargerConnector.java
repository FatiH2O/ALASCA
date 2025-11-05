package fr.sorbonne_u.components.hem2025e1.equipments.simpleCharger;

import fr.sorbonne_u.components.connectors.AbstractConnector;

public class ChargerConnector extends AbstractConnector implements ChargerCI {
    @Override
    public State getState() throws Exception {
        return ((ChargerCI)this.offering).getState();
    }

    @Override
    public void turnOn() throws Exception {
        ((ChargerCI)this.offering).turnOn();
    }

    @Override
    public void turnOff() throws Exception {
        ((ChargerCI)this.offering).turnOff();
    }
}