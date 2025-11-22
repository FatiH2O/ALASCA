package fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events;

import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

public class PlugoutCharger extends AbstractChargerEvent implements EventI {
    public static final String URI = PlugoutCharger.class.getSimpleName();

    public PlugoutCharger(Time timeOfOccurrence) {
        super(timeOfOccurrence, null);
    }
}
