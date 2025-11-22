package fr.sorbonne_u.components.hem2025e2.equipments.charger.mil;

// Reference implementation used to derive this model:
// /mnt/data/ChargerImplementation.java

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.alasca.physical_data.MeasurementUnit;
import fr.sorbonne_u.components.hem2025e1.equipments.charger.ChargerImplementation;
import fr.sorbonne_u.components.hem2025e1.equipments.charger.ChargerStateI;
import fr.sorbonne_u.components.hem2025e2.GlobalReportI;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.PluginCharger;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.PlugoutCharger;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.StartCharging;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.StopCharging;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.SetTargetPower;
import fr.sorbonne_u.components.hem2025e2.utils.Electricity;
import fr.sorbonne_u.devs_simulation.exceptions.MissingRunParameterException;
import fr.sorbonne_u.devs_simulation.exceptions.NeoSim4JavaException;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ModelExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.interfaces.ModelI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.AtomicSimulatorI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulationReportI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;
import fr.sorbonne_u.exceptions.AssertionChecking;

/**
 * The class <code>ChargerElectricityModel</code> defines the MIL model
 * of the electricity consumption of the charger.
 *
 * It follows the structure of HairDryerElectricityModel from the course but
 * models the 4 states present in ChargerImplementation:
 *   OFF, IDLE, CHARGING, SUSPENDED
 *
 * Imported events:
 *   PluginCharger, PlugoutCharger, StartCharging, StopCharging, SetTargetPower
 *
 * Exported variable:
 *   currentIntensity (Double) -- power in the power unit used by the meter
 */
@ModelExternalEvents(imported = {
        PluginCharger.class,
        PlugoutCharger.class,
        StartCharging.class,
        StopCharging.class,
        SetTargetPower.class
})
@ModelExportedVariable(name = "currentIntensity", type = Double.class)
public class ChargerElectricityModel extends AtomicHIOA {
    private static final long serialVersionUID = 1L;

    public static boolean VERBOSE = true;
    public static boolean DEBUG = false;

    public static final String URI = ChargerElectricityModel.class.getSimpleName();

    protected enum ChargerState {
        OFF, IDLE, CHARGING, SUSPENDED
    }

    protected ChargerState currentState = ChargerState.OFF;
    protected boolean consumptionHasChanged = false;

    protected double targetPower;    // in watts, from SetTargetPower or default MAX
    protected double totalConsumption = 0.0; // in kWh

    @ExportedVariable(type = Double.class)
    protected final Value<Double> currentIntensity = new Value<Double>(this);

    // run parameter names (optional)
    public static final String TARGET_POWER_RPNAME = URI + ":TARGET_POWER_W";
    public static final String TENSION_RPNAME = URI + ":TENSION_V";

    // default values (used if run parameters absent)
    protected double defaultTargetPower = ChargerImplementation.MAX_POWER.getData(); // 65.0
    protected double tension = 230.0; // volts (default)

    // -------------------------------------------------------------------------
    // Invariants
    // -------------------------------------------------------------------------
    protected static boolean implementationInvariants(ChargerElectricityModel instance) {
        assert instance != null : new NeoSim4JavaException("Precondition violation: instance != null");

        boolean ret = true;
        ret &= AssertionChecking.checkImplementationInvariant(
                instance.defaultTargetPower > 0.0,
                ChargerElectricityModel.class,
                instance,
                "defaultTargetPower > 0.0");
        ret &= AssertionChecking.checkImplementationInvariant(
                instance.tension > 0.0,
                ChargerElectricityModel.class,
                instance,
                "tension > 0.0");
        ret &= AssertionChecking.checkImplementationInvariant(
                instance.currentState != null,
                ChargerElectricityModel.class,
                instance,
                "currentState != null");
        ret &= AssertionChecking.checkImplementationInvariant(
                !instance.currentIntensity.isInitialised() || instance.currentIntensity.getValue() >= 0.0,
                ChargerElectricityModel.class,
                instance,
                "!currentIntensity.isInitialised() || currentIntensity.getValue() >= 0.0");
        return ret;
    }

    public static boolean staticInvariants() {
        boolean ret = true;
        ret &= AssertionChecking.checkStaticInvariant(
                URI != null && !URI.isEmpty(),
                ChargerElectricityModel.class,
                "URI != null && !URI.isEmpty()");
        return ret;
    }

    protected static boolean invariants(ChargerElectricityModel instance) {
        assert instance != null : new NeoSim4JavaException("Precondition violation: instance != null");
        boolean ret = true;
        ret &= staticInvariants();
        return ret;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------
    public ChargerElectricityModel(String uri, TimeUnit simulatedTimeUnit, AtomicSimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
        // default target power comes from ChargerImplementation.MAX_POWER
        this.targetPower = this.defaultTargetPower;
        // logger
        this.getSimulationEngine().setLogger(new StandardLogger());

        assert implementationInvariants(this) : new NeoSim4JavaException("ChargerElectricityModel.implementationInvariants(this)");
        assert invariants(this) : new NeoSim4JavaException("ChargerElectricityModel.invariants(this)");
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------
    public ChargerState getState() {
        return this.currentState;
    }

    // set state convenience; toggles consumption change flag if needed
    public void setState(ChargerState s) {
        this.currentState = s;
        this.toggleConsumptionHasChanged();
    }

    protected void toggleConsumptionHasChanged() {
        this.consumptionHasChanged = !this.consumptionHasChanged;
    }

    // -------------------------------------------------------------------------
    // DEVS simulation protocol
    // -------------------------------------------------------------------------
    @Override
    public void initialiseState(Time startTime) {
        super.initialiseState(startTime);
        this.currentState = ChargerState.OFF;
        this.consumptionHasChanged = false;
        this.totalConsumption = 0.0;
        this.currentIntensity.initialise(0.0);
        if (VERBOSE) {
            this.logMessage("Charger MIL simulation begins.");
        }

        assert implementationInvariants(this) : new NeoSim4JavaException("ChargerElectricityModel.implementationInvariants(this)");
        assert invariants(this) : new NeoSim4JavaException("ChargerElectricityModel.invariants(this)");
    }

    @Override
    public void initialiseVariables() {
        super.initialiseVariables();
        this.currentIntensity.initialise(0.0);
        assert implementationInvariants(this) : new NeoSim4JavaException("ChargerElectricityModel.implementationInvariants(this)");
        assert invariants(this) : new NeoSim4JavaException("ChargerElectricityModel.invariants(this)");
    }

    @Override
    public ArrayList<EventI> output() {
        // this model does not export events
        return null;
    }

    @Override
    public Duration timeAdvance() {
        Duration ret;
        if (this.consumptionHasChanged) {
            // trigger immediate internal transition to update exported variable
            this.toggleConsumptionHasChanged();
            ret = new Duration(0.0, this.getSimulatedTimeUnit());
        } else {
            ret = Duration.INFINITY;
        }

        assert implementationInvariants(this) : new NeoSim4JavaException("ChargerElectricityModel.implementationInvariants(this)");
        assert invariants(this) : new NeoSim4JavaException("ChargerElectricityModel.invariants(this)");
        return ret;
    }

    @Override
    public void userDefinedInternalTransition(Duration elapsedTime) {
        super.userDefinedInternalTransition(elapsedTime);

        Time t = this.getCurrentStateTime();
        // update currentIntensity based on currentState
        switch (this.currentState) {
            case CHARGING:
                // currentIntensity expressed in power unit (watts) divided by tension => current (A)
                // but we export the power unit as in HairDryer (the meter expects values in its POWER_UNIT)
                // Here we export the power in watts (compatible with Electricity.computeConsumption usage).
                this.currentIntensity.setNewValue(this.targetPower, t);
                break;
            case IDLE:
                // not charging, power = 0.0 (no idle consumption in ChargerImplementation)
                this.currentIntensity.setNewValue(0.0, t);
                break;
            case SUSPENDED:
                this.currentIntensity.setNewValue(0.0, t);
                break;
            case OFF:
            default:
                this.currentIntensity.setNewValue(0.0, t);
                break;
        }

        if (VERBOSE) {
            StringBuffer message = new StringBuffer("internal transition with currentIntensity = ");
            message.append(this.currentIntensity.getValue());
            message.append(" W at ");
            message.append(this.currentIntensity.getTime());
            this.logMessage(message.toString());
        }

        assert implementationInvariants(this) : new NeoSim4JavaException("ChargerElectricityModel.implementationInvariants(this)");
        assert invariants(this) : new NeoSim4JavaException("ChargerElectricityModel.invariants(this)");
    }

    @Override
    public void userDefinedExternalTransition(Duration elapsedTime) {
        super.userDefinedExternalTransition(elapsedTime);

        ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
        if (currentEvents == null || currentEvents.size() == 0) {
            return;
        }

        // by construction we expect one event at a time for this simple model
        Event ce = (Event) currentEvents.get(0);

        // update total consumption for the elapsed time before handling the event
        // currentIntensity value is in watts, Electricity.computeConsumption expects (duration, powerInWatts)
        this.totalConsumption += Electricity.computeConsumption(elapsedTime, this.currentIntensity.getValue());

        if (VERBOSE) {
            StringBuffer message = new StringBuffer("external transition: ");
            message.append(ce.toString());
            this.logMessage(message.toString());
        }

        // handle events by type
        if (ce instanceof PluginCharger) {
            // plugin: OFF -> IDLE
            if (this.currentState == ChargerState.OFF) {
                this.currentState = ChargerState.IDLE;
                this.toggleConsumptionHasChanged();
            }
        } else if (ce instanceof PlugoutCharger) {
            // plugout: any -> OFF
            if (this.currentState != ChargerState.OFF) {
                this.currentState = ChargerState.OFF;
                this.targetPower = this.defaultTargetPower;
                this.toggleConsumptionHasChanged();
            }
        } else if (ce instanceof StartCharging) {
            // start charging: only if plugged (IDLE or SUSPENDED)
            if (this.currentState == ChargerState.IDLE || this.currentState == ChargerState.SUSPENDED) {
                this.currentState = ChargerState.CHARGING;
                // on start, use current targetPower (clamped if necessary)
                if (this.targetPower > ChargerImplementation.MAX_POWER.getData()) {
                    this.targetPower = ChargerImplementation.MAX_POWER.getData();
                }
                this.toggleConsumptionHasChanged();
            }
        } else if (ce instanceof StopCharging) {
            // stop charging: CHARGING -> SUSPENDED
            if (this.currentState == ChargerState.CHARGING) {
                this.currentState = ChargerState.SUSPENDED;
                this.toggleConsumptionHasChanged();
            }
        } else if (ce instanceof SetTargetPower) {
            // set target power event should carry a field 'watts'
            SetTargetPower st = (SetTargetPower) ce;
            double newTarget = st.watts;
            // clamp to max allowed
            if (newTarget > ChargerImplementation.MAX_POWER.getData()) {
                newTarget = ChargerImplementation.MAX_POWER.getData();
            } else if (newTarget < 0.0) {
                newTarget = 0.0;
            }
            this.targetPower = newTarget;
            // if currently charging, update consumption immediately
            if (this.currentState == ChargerState.CHARGING) {
                this.toggleConsumptionHasChanged();
            }
            if (VERBOSE) {
                this.logMessage("SetTargetPower -> targetPower = " + this.targetPower + " W");
            }
        } else {
            throw new RuntimeException(ce + " is not an event that ChargerElectricityModel can receive.");
        }

        assert implementationInvariants(this) : new NeoSim4JavaException("ChargerElectricityModel.implementationInvariants(this)");
        assert invariants(this) : new NeoSim4JavaException("ChargerElectricityModel.invariants(this)");
    }

    @Override
    public void endSimulation(Time endTime) {
        // account for consumption since last event
        Duration d = endTime.subtract(this.getCurrentStateTime());
        this.totalConsumption += Electricity.computeConsumption(d, this.currentIntensity.getValue());

        if (VERBOSE) {
            this.logMessage("simulation ends. total consumption (kWh) = " + this.totalConsumption);
        }
        super.endSimulation(endTime);
    }

    // -------------------------------------------------------------------------
    // Optional DEVS simulation protocol: run parameters
    // -------------------------------------------------------------------------
    @Override
    public void setSimulationRunParameters(Map<String, Object> simParams) throws MissingRunParameterException {
        super.setSimulationRunParameters(simParams);

        String tgtName = ModelI.createRunParameterName(getURI(), TARGET_POWER_RPNAME);
        if (simParams.containsKey(tgtName)) {
            this.defaultTargetPower = (double) simParams.get(tgtName);
            this.targetPower = this.defaultTargetPower;
        }

        String tensionName = ModelI.createRunParameterName(getURI(), TENSION_RPNAME);
        if (simParams.containsKey(tensionName)) {
            this.tension = (double) simParams.get(tensionName);
        }

        assert implementationInvariants(this) : new NeoSim4JavaException("ChargerElectricityModel.implementationInvariants(this)");
        assert invariants(this) : new NeoSim4JavaException("ChargerElectricityModel.invariants(this)");
    }

    // -------------------------------------------------------------------------
    // Optional DEVS simulation protocol: simulation report
    // -------------------------------------------------------------------------
    public static class ChargerElectricityReport implements SimulationReportI, GlobalReportI {
        private static final long serialVersionUID = 1L;
        protected String modelURI;
        protected double totalConsumption;

        public ChargerElectricityReport(String modelURI, double totalConsumption) {
            super();
            this.modelURI = modelURI;
            this.totalConsumption = totalConsumption;
        }

        @Override
        public String getModelURI() {
            return this.modelURI;
        }

        @Override
        public String printout(String indent) {
            StringBuffer sb = new StringBuffer(indent);
            sb.append("---\n");
            sb.append(indent);
            sb.append("|");
            sb.append(this.modelURI);
            sb.append(" report\n");
            sb.append(indent);
            sb.append("|");
            sb.append("total consumption in kwh = ");
            sb.append(this.totalConsumption);
            sb.append(".\n");
            sb.append(indent);
            sb.append("---\n");
            return sb.toString();
        }

        @Override
        public String toString() {
            return this.printout("");
        }
    }

    @Override
    public SimulationReportI getFinalReport() {
        return new ChargerElectricityReport(this.getURI(), this.totalConsumption);
    }
}