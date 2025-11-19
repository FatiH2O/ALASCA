package fr.sorbonne_u.components.hem2025e2.equipments.ac.mil;

import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.devs_simulation.models.interfaces.ModelI;
import fr.sorbonne_u.devs_simulation.utils.AssertionChecking;

/**
 * Simulation run parameters interface for AC MIL models.
 */
public interface ACSimulationConfigurationI extends ModelI {
    /** time unit used in the AC simulator. */
    public static final TimeUnit TIME_UNIT = TimeUnit.HOURS;

    public static boolean staticInvariants() {
        boolean ret = true;
        ret &= AssertionChecking.checkStaticInvariant(
                TIME_UNIT != null,
                ACSimulationConfigurationI.class,
                "TIME_UNIT != null");
        return ret;
    }
}
