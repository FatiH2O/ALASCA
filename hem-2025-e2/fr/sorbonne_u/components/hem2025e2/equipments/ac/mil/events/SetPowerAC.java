package fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.events;

import fr.sorbonne_u.devs_simulation.es.events.ES_Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.exceptions.NeoSim4JavaException;

import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.ACElectricityModel;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.ACEventI;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.ACExternalControlI;

public class SetPowerAC extends ES_Event implements ACEventI {

    private static final long serialVersionUID = 1L;

    public static class PowerValue implements EventInformationI {
        private static final long serialVersionUID = 1L;
        protected final double power;

        public PowerValue(double p) {
            assert p >= 0.0 && p <= ACExternalControlI.MAX_POWER_LEVEL.getData();
            this.power = p;
        }

        public double getPower() { return this.power; }
    }

    protected final PowerValue powerValue;

    public SetPowerAC(Time t, EventInformationI content) {
        super(t, content);
        assert content != null && content instanceof PowerValue;
        this.powerValue = (PowerValue) content;
    }

    @Override
    public boolean hasPriorityOver(EventI e) {
        if (e instanceof SwitchOffAC) return true;
        return false;
    }

    @Override
    public void executeOn(AtomicModelI model) {
        assert model instanceof ACElectricityModel :
                new NeoSim4JavaException("Precondition: model instanceof ACElectricityModel");
        ACElectricityModel ac = (ACElectricityModel) model;
        ac.setCurrentCoolingPower(this.powerValue.getPower(), this.getTimeOfOccurrence());
    }
}