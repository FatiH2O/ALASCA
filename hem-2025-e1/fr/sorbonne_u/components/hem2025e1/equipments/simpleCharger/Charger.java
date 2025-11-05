package fr.sorbonne_u.components.hem2025e1.equipments.simpleCharger;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;

@OfferedInterfaces(offered = {ChargerCI.class})
public class Charger
extends     AbstractComponent
implements  ChargerI
{
    public static final String INBOUND_PORT_URI = "CHARGER-IBP-URI";
    protected ChargerInboundPort cip;
    protected State currentState;

    protected Charger() throws Exception {
        super(1, 0);
        this.initialise();
    }

    protected void initialise() throws Exception {
        this.currentState = State.OFF;
        this.cip = new ChargerInboundPort(INBOUND_PORT_URI, this);
        this.cip.publishPort();
        
        this.getTracer().setTitle("Chargeur Simple");
        this.getTracer().setRelativePosition(1, 0);
        this.toggleTracing();
    }

    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        try {
            this.cip.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }

    @Override
    public State getState() throws Exception {
        return this.currentState;
    }

    @Override
    public void turnOn() throws Exception {
        if (this.currentState == State.OFF) {
            this.currentState = State.ON;
        }
    }

    @Override
    public void turnOff() throws Exception {
        if (this.currentState == State.ON) {
            this.currentState = State.OFF;
        }
    }
}