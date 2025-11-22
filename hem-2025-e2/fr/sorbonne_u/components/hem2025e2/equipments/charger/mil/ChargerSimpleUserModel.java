package fr.sorbonne_u.components.hem2025e2.equipments.charger.mil;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.math3.random.RandomDataGenerator;

import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.PluginCharger;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.PlugoutCharger;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.StartCharging;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.StopCharging;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.SetTargetPower;

import fr.sorbonne_u.devs_simulation.es.events.ES_EventI;
import fr.sorbonne_u.devs_simulation.es.models.AtomicES_Model;
import fr.sorbonne_u.devs_simulation.exceptions.MissingRunParameterException;
import fr.sorbonne_u.devs_simulation.exceptions.NeoSim4JavaException;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.interfaces.ModelI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.AtomicSimulatorI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulationReportI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;
import fr.sorbonne_u.exceptions.AssertionChecking;

/**
 * A simple user model for the charger.
 *
 * The model generates a fixed cycle of events:
 *   Plugin -> StartCharging -> SetTargetPower(40) ->
 *   SetTargetPower(20) -> StopCharging -> Plugout -> (repeat)
 */
@ModelExternalEvents(exported = {
        PluginCharger.class,
        PlugoutCharger.class,
        StartCharging.class,
        StopCharging.class,
        SetTargetPower.class
})
public class ChargerSimpleUserModel extends AtomicES_Model {
    private static final long serialVersionUID = 1L;

    public static final String URI = ChargerSimpleUserModel.class.getSimpleName();
    public static boolean VERBOSE = true;

    // delays between steps (mean duration)
    protected static double STEP_MEAN_DURATION = 5.0 / 60.0;

    protected final RandomDataGenerator rg;

    // invariants
    protected static boolean staticImplementationInvariants() {
        boolean ret = true;
        ret &= AssertionChecking.checkStaticImplementationInvariant(
                STEP_MEAN_DURATION > 0.0,
                ChargerSimpleUserModel.class,
                "STEP_MEAN_DURATION > 0.0");
        return ret;
    }

    protected static boolean implementationInvariants(ChargerSimpleUserModel instance) {
        assert instance != null :
                new NeoSim4JavaException("Precondition violation: instance != null");

        boolean ret = true;
        ret &= staticImplementationInvariants();
        ret &= AssertionChecking.checkImplementationInvariant(
                instance.rg != null,
                ChargerSimpleUserModel.class,
                instance,
                "rg != null");
        return ret;
    }

    protected static boolean staticInvariants() {
        return AssertionChecking.checkStaticInvariant(
                URI != null && !URI.isEmpty(),
                ChargerSimpleUserModel.class,
                "URI != null && !URI.isEmpty()");
    }

    protected static boolean invariants(ChargerSimpleUserModel instance) {
        assert instance != null :
                new NeoSim4JavaException("instance != null");
        return staticInvariants();
    }

    public ChargerSimpleUserModel(String uri, TimeUnit simulatedTimeUnit,
                                  AtomicSimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);

        this.rg = new RandomDataGenerator();
        this.getSimulationEngine().setLogger(new StandardLogger());

        assert implementationInvariants(this);
        assert invariants(this);
    }

    // Utility : random delay
    protected Time computeNext(Time from) {
        double delay = Math.max(
                this.rg.nextGaussian(STEP_MEAN_DURATION, STEP_MEAN_DURATION / 2.0),
                0.1);
        return from.add(new Duration(delay, this.getSimulatedTimeUnit()));
    }

    protected void generateNextEvent() {
        EventI current = this.eventList.peek();
        ES_EventI next = null;

        if (current instanceof PlugoutCharger) {
            Time t2 = computeNext(current.getTimeOfOccurrence());
            next = new PluginCharger(t2);
        } else {
            Time t = computeNext(current.getTimeOfOccurrence());

            if (current instanceof PluginCharger) {
                next = new StartCharging(t);

            } else if (current instanceof StartCharging) {
                next = new SetTargetPower(t, 40.0);

            } else if (current instanceof SetTargetPower) {
                double w = ((SetTargetPower) current).watts;
                if (w == 40.0)
                    next = new SetTargetPower(t, 20.0);
                else
                    next = new StopCharging(t);

            } else if (current instanceof StopCharging) {
                next = new PlugoutCharger(t);
            }
        }

        this.scheduleEvent(next);
    }


    @Override
    public void initialiseState(Time initialTime) {
        super.initialiseState(initialTime);

        this.rg.reSeedSecure();

        // first event: plugin
        Time t = computeNext(this.getCurrentStateTime());
        this.scheduleEvent(new PluginCharger(t));

        this.nextTimeAdvance = this.timeAdvance();
        this.timeOfNextEvent =
                this.getCurrentStateTime().add(this.getNextTimeAdvance());

        if (VERBOSE)
            this.logMessage("Charger user model: simulation begins.");
    }

    @Override
    public ArrayList<EventI> output() {
        if (this.eventList.peek() != null)
            this.generateNextEvent();

        return super.output();
    }

    @Override
    public void endSimulation(Time endTime) {
        if (VERBOSE)
            this.logMessage("Charger user model: simulation ends.");
        super.endSimulation(endTime);
    }

    public static final String MEAN_STEP_RPNAME = "STEP_MEAN_DURATION";

    @Override
    public void setSimulationRunParameters(Map<String, Object> simParams)
            throws MissingRunParameterException {
        super.setSimulationRunParameters(simParams);

        String stepName =
                ModelI.createRunParameterName(getURI(), MEAN_STEP_RPNAME);
        if (simParams.containsKey(stepName)) {
            STEP_MEAN_DURATION = (double) simParams.get(stepName);
        }
    }

    @Override
    public SimulationReportI getFinalReport() {
        return null;
    }
}
