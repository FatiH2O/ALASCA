package fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events;

import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

public class PluginCharger extends AbstractChargerEvent implements EventI {
    public static final String URI = PluginCharger.class.getSimpleName();

    public PluginCharger(Time timeOfOccurrence) {
        super(timeOfOccurrence, null);
    }
}
