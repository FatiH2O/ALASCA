package fr.sorbonne_u.components.hem2025e2.equipments.ac.mil;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ModelExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.AtomicSimulatorI;
import fr.sorbonne_u.devs_simulation.exceptions.NeoSim4JavaException;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;
import fr.sorbonne_u.components.hem2025e2.utils.Electricity;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.events.SwitchOnAC;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.events.SwitchOffAC;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.events.SetPowerAC;

@ModelExternalEvents(imported = {SwitchOnAC.class, SwitchOffAC.class, SetPowerAC.class})
@ModelExportedVariable(name = "currentIntensity", type = Double.class)
@ModelExportedVariable(name = "currentCoolingPower", type = Double.class)
public class ACElectricityModel extends AtomicHIOA {

    private static final long serialVersionUID = 1L;
    public static final String URI = ACElectricityModel.class.getSimpleName();

    public static boolean VERBOSE = true;

    public static enum ACState { ON, COOLING, OFF }

    protected ACState currentState = ACState.OFF;
    protected boolean consumptionHasChanged = false;
    protected double totalConsumption = 0.0;

    @ExportedVariable(type = Double.class)
    protected final Value<Double> currentCoolingPower = new Value<Double>(this);
    @ExportedVariable(type = Double.class)
    protected final Value<Double> currentIntensity = new Value<Double>(this);

    public ACElectricityModel(String uri, TimeUnit simulatedTimeUnit, AtomicSimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
        this.getSimulationEngine().setLogger(new StandardLogger());
    }

    public void setState(ACState s, Time t) {
        ACState old = this.currentState;
        this.currentState = s;
        if (old != s) this.consumptionHasChanged = true;
    }

    public ACState getState() { return this.currentState; }

    public void setCurrentCoolingPower(double p, Time t) {
        double old = this.currentCoolingPower.getValue();
        this.currentCoolingPower.setNewValue(p, t);
        if (p != old) this.consumptionHasChanged = true;
    }

    @Override
    public void initialiseState(Time initialTime) {
        super.initialiseState(initialTime);
        this.currentState = ACState.OFF;
        this.consumptionHasChanged = false;
        this.totalConsumption = 0.0;
        if (VERBOSE) this.logMessage("AC simulation begins.");
    }

    @Override
    public ArrayList<EventI> output() { return null; }

    @Override
    public Duration timeAdvance() {
        if (this.consumptionHasChanged) {
            this.consumptionHasChanged = false;
            return Duration.zero(this.getSimulatedTimeUnit());
        }
        return Duration.INFINITY;
    }

    @Override
    public void userDefinedInternalTransition(Duration elapsedTime) {
        super.userDefinedInternalTransition(elapsedTime);
        Time t = this.getCurrentStateTime();
        if (this.currentState == ACState.ON) {
            this.currentIntensity.setNewValue(this.currentCoolingPower.getValue()/220.0, t);
        } else if (this.currentState == ACState.COOLING) {
            this.currentIntensity.setNewValue(this.currentCoolingPower.getValue()/220.0, t);
        } else {
            this.currentIntensity.setNewValue(0.0, t);
        }
        if (VERBOSE) this.logMessage("AC new consumption: " + this.currentIntensity.getValue());
    }

    @Override
    public void userDefinedExternalTransition(Duration elapsedTime) {
        super.userDefinedExternalTransition(elapsedTime);
        ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
        assert currentEvents != null && currentEvents.size() == 1;
        EventI e = currentEvents.get(0);
        // optional consumption accounting
        this.totalConsumption += Electricity.computeConsumption(elapsedTime, 220.0 * this.currentIntensity.getValue());
        // execute event on model
        ((fr.sorbonne_u.devs_simulation.models.events.Event)e).executeOn(this);
    }

    @Override
    public void endSimulation(Time endTime) {
        Duration d = endTime.subtract(this.getCurrentStateTime());
        this.totalConsumption += Electricity.computeConsumption(d, 220.0 * this.currentIntensity.getValue());
        super.endSimulation(endTime);
    }
}
