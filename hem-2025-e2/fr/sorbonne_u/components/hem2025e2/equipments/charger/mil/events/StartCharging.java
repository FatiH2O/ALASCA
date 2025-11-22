package fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events;

import fr.sorbonne_u.devs_simulation.models.time.Time;

public class StartCharging extends AbstractChargerEvent {
    public static final String URI = StartCharging.class.getSimpleName();

    public StartCharging(Time timeOfOccurrence) {
        super(timeOfOccurrence,null);
    }
}
