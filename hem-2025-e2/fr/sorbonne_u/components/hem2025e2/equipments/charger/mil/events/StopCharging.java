package fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events;

import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.time.Time;

public class StopCharging extends AbstractChargerEvent {
    public static final String URI = StopCharging.class.getSimpleName();

    public StopCharging(Time timeOfOccurrence) {
        super(timeOfOccurrence,null);
    }
}
