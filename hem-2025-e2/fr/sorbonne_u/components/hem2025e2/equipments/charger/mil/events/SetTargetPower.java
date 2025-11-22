package fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events;

import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.time.Time;

public class SetTargetPower extends AbstractChargerEvent {
    public static final String URI = SetTargetPower.class.getSimpleName();
    public final double watts;

    public SetTargetPower(Time timeOfOccurrence, double watts) {
        super(timeOfOccurrence,null);
        this.watts = watts;
    }

    @Override
    public String toString() {
        return "SetTargetPower(" + this.watts + "W)@" + this.getTimeOfOccurrence();
    }
}
