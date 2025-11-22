package fr.sorbonne_u.components.hem2025e2.equipments.charger.mil;

import fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import java.util.ArrayList;

/**
 * ChargerStateModelI interface for charger MIL state models.
 * Defines the contract for state models to be used in charger MIL simulation.
 */
public interface ChargerStateModelI extends AtomicModelI {
    /**
     * Get the current state of the charger (e.g., IDLE, CHARGING).
     */
    Object getState();

    /**
     * Set the current state of the charger.
     */
    void setState(Object state);

    /**
     * Get the time of the current state.
     */
    Time getCurrentStateTime();

    /**
     * Get the list of events that occurred in the current state.
     */
    ArrayList<EventI> getCurrentStateEvents();
}
