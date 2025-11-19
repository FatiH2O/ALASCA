package fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.events;

import fr.sorbonne_u.devs_simulation.es.events.ES_Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.exceptions.NeoSim4JavaException;

import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.ACElectricityModel;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.ACEventI;

public class SwitchOffAC extends ES_Event implements ACEventI {

    private static final long serialVersionUID = 1L;

    public SwitchOffAC(Time t) {
        super(t, null);
    }

    @Override
    public boolean hasPriorityOver(EventI e) {
        return true;
    }

    @Override
    public void executeOn(AtomicModelI model) {
        assert model instanceof ACElectricityModel :
                new NeoSim4JavaException("Precondition: model instanceof ACElectricityModel");
        ACElectricityModel ac = (ACElectricityModel) model;
        ac.setState(ACElectricityModel.ACState.OFF, this.getTimeOfOccurrence());
    }
}
