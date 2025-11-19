package fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events;

import fr.sorbonne_u.devs_simulation.es.events.ES_Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.exceptions.NeoSim4JavaException;

import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.ChargerElectricityModel;

public class SwitchOffCharger extends ES_Event implements ChargerEventI {

    private static final long serialVersionUID = 1L;

    public SwitchOffCharger(Time t) { super(t, null); }

    @Override
    public boolean hasPriorityOver(EventI e) { return true; }

    @Override
    public void executeOn(AtomicModelI model) {
        assert model instanceof ChargerElectricityModel :
                new NeoSim4JavaException("Precondition: model instanceof ChargerElectricityModel");
        ChargerElectricityModel c = (ChargerElectricityModel) model;
        c.setState(ChargerElectricityModel.ChargerState.OFF, this.getTimeOfOccurrence());
    }
}
