package Charger;

import Charger_connections.ChargerControlConnector;
import Charger_connections.ChargerControlOutboundPort;
import Charger_connections.ChargerUserConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import physical_data.fr.sorbonne_u.alasca.physical_data.Measure;
import physical_data.fr.sorbonne_u.alasca.physical_data.SignalData;

@RequiredInterfaces(required = {ChargerControlCI.class, ChargerUserCI.class})
public class ChargerUnitTester extends AbstractComponent {

    protected ChargerControlOutboundPort controlPort;
    protected ChargerUserOutboundPort userPort;

    protected ChargerUnitTester() throws Exception {
        super(1, 0);

        // Active le tracer pour ce composant
        this.tracer.get().setTitle("SmartCharger Tester");
        this.tracer.get().setRelativePosition(2, 1);
        this.toggleTracing();
    }

    @Override
    public synchronized void start() throws ComponentStartException {
        super.start();
        try {
            this.controlPort = new ChargerControlOutboundPort(this);
            this.userPort = new ChargerUserOutboundPort(this);

            this.controlPort.publishPort();
            this.userPort.publishPort();

            this.doPortConnection(
                this.controlPort.getPortURI(),
                Charger.CONTROL_INBOUND_PORT_URI,
                ChargerControlConnector.class.getCanonicalName()
            );

            this.doPortConnection(
                this.userPort.getPortURI(),
                Charger.USER_INBOUND_PORT_URI,
                ChargerUserConnector.class.getCanonicalName()
            );

        } catch (Exception e) {
            throw new ComponentStartException(e);
        }
    }

    @Override
    public synchronized void execute() throws Exception {
        super.execute();
        this.traceMessage("\n=== Début du test du SmartCharger ===\n");

        // --- État initial ---
        if (!this.userPort.isPluggedIn() && !this.controlPort.isCharging()) {
            this.traceMessage("Test 1 : chargeur OFF au démarrage.\n");
        } else {
            this.traceMessage(" Test 1 : état initial incorrect.\n");
        }

        // --- Tentative de charge avant branchement ---
        this.traceMessage("Test 2 : tentative de startCharging() sans plug-in (doit échouer)...\n");
        try {
            this.controlPort.startCharging();
            this.traceMessage(" Test 2 : aucune exception levée (erreur)\n");
        } catch (Exception e) {
            this.traceMessage("Test 2 : exception attendue → " + e.getMessage() + "\n");
        }

        // --- Plug-in ---
        this.traceMessage("Test 3 : branchement de l’utilisateur...\n");
        this.userPort.plugin();
        if (this.userPort.isPluggedIn()) {
            this.traceMessage("Test 3 : chargeur branché.\n");
        } else {
            this.traceMessage(" Test 3 : échec du branchement.\n");
        }

        // --- Démarrage de la charge ---
        this.traceMessage("Test 4 : démarrage de la charge...\n");
        this.controlPort.startCharging();
        if (this.controlPort.isCharging()) {
            this.traceMessage(" Test 4 : charge en cours.\n");
        } else {
            this.traceMessage(" Test 4 : échec du démarrage.\n");
        }

        // --- Lecture du niveau de charge ---
        SignalData<Double> signal = this.controlPort.getChargeLevel();
        this.traceMessage(" Niveau de charge actuel : " + signal.getMeasure().getData() + " %\n");

        // --- Suspension de la charge ---
        this.traceMessage("Test 5 : suspension de la charge...\n");
        this.controlPort.stopCharging();
        if (!this.controlPort.isCharging()) {
            this.traceMessage(" Test 5 : charge suspendue.\n");
        } else {
            this.traceMessage(" Test 5 : charge encore active (erreur).\n");
        }

        // --- Reprise de la charge ---
        this.traceMessage("Test 6 : reprise de la charge...\n");
        this.controlPort.startCharging();
        if (this.controlPort.isCharging()) {
            this.traceMessage(" Test 6 : reprise réussie.\n");
        } else {
            this.traceMessage(" Test 6 : échec de la reprise.\n");
        }

        // --- Débranchement pendant charge ---
        this.traceMessage("Test 7 : débranchement (plugout)...\n");
        this.userPort.plugout();
        if (!this.userPort.isPluggedIn() && !this.controlPort.isCharging()) {
            this.traceMessage("Test 7 : arrêt et débranchement OK.\n");
        } else {
            this.traceMessage(" Test 7 : incohérence après plugout().\n");
        }

        // --- Test 8 : lecture de la puissance instantanée ---
        this.traceMessage("Test 8 : lecture de la puissance actuelle...\n");
        Measure<Double> currentPower = this.controlPort.getCurrentPower();
        this.traceMessage("Test 8 : puissance actuelle = " + currentPower.getData() + " W\n");

        // --- Test 9 : changement de puissance cible ---
        this.traceMessage("Test 9 : changement de puissance cible à 30W...\n");
        this.controlPort.setTargetPower(new Measure<Double>(30.0));
        this.traceMessage(" Test 9 : nouvelle puissance cible appliquée.\n");

        this.traceMessage("=== Fin du test du SmartCharger ===\n");
    }

    @Override
    public synchronized void finalise() throws Exception {
        try {
            this.doPortDisconnection(this.controlPort.getPortURI());
            this.doPortDisconnection(this.userPort.getPortURI());
        } catch (Exception e) {
            this.traceMessage(" Erreur de déconnexion : " + e.getMessage() + "\n");
        }
        super.finalise();
    }

    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        try {
            if (this.controlPort.isPublished()) this.controlPort.unpublishPort();
            if (this.userPort.isPublished()) this.userPort.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }
}
