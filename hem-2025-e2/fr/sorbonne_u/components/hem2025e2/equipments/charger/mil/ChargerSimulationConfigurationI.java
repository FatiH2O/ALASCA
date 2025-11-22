package fr.sorbonne_u.components.hem2025e2.equipments.charger.mil;


import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.devs_simulation.utils.AssertionChecking;


public interface ChargerSimulationConfigurationI
{
    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    /** time unit used in the charger simulator. */
    public static final TimeUnit TIME_UNIT = TimeUnit.HOURS;

    
    public static boolean staticInvariants()
    {
        boolean ret = true;
        ret &= AssertionChecking.checkStaticInvariant(
                TIME_UNIT != null,
                ChargerSimulationConfigurationI.class,
                "TIME_UNIT != null");
        return ret;
    }
}
