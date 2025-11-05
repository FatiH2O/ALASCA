package fr.sorbonne_u.components.hem2025e1.equipments.simpleCharger;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

@RequiredInterfaces(required = {ChargerCI.class})
public class ChargerTester extends AbstractComponent {
    protected ChargerOutboundPort cop;

    protected ChargerTester() throws Exception {
        super(1, 0);

        this.getTracer().setTitle("Testeur de Chargeur");
        this.getTracer().setRelativePosition(0, 1); 
        this.toggleTracing(); 
    }

    @Override
    public synchronized void start() throws ComponentStartException {
        super.start();
        try {
            this.cop = new ChargerOutboundPort(this);
            this.cop.publishPort();
            this.doPortConnection(
                this.cop.getPortURI(),
                Charger.INBOUND_PORT_URI,
                ChargerConnector.class.getCanonicalName());
        } catch (Exception e) {
            throw new ComponentStartException(e);
        }
    }

    @Override
    public synchronized void execute() throws Exception {
        super.execute();
        
        this.logMessage("--- Début du test du Chargeur Simple ---");

        if (this.cop.getState() == ChargerI.State.OFF) {
            this.logMessage("Test 1: OK, le chargeur est bien OFF.");
        } else {
            this.logMessage("Test 1: ERREUR, l'état devrait être OFF.");
        }

        this.logMessage("Test 2: turnOn...");
        this.cop.turnOn();
        if (this.cop.getState() == ChargerI.State.ON) {
            this.logMessage("Test 2: le chargeur est ON.");
        } else {
            this.logMessage("Test 2: ERREUR, l'état devrait être ON.");
        }

        this.logMessage("Test 3: turnOff");
        this.cop.turnOff();
        if (this.cop.getState() == ChargerI.State.OFF) {
            this.logMessage("Test 3: OK, le chargeur est OFF.");
        } else {
            this.logMessage("Test 3: ERREUR, l'état devrait être OFF.");
        }

        this.logMessage("--- Fin du test ---");
    }

    @Override
    public synchronized void finalise() throws Exception {
        this.doPortDisconnection(this.cop.getPortURI());
        super.finalise();
    }
}