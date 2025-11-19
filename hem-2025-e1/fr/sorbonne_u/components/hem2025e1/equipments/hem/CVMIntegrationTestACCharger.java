package fr.sorbonne_u.components.hem2025e1.equipments.hem;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.hem2025e1.equipments.hem.HEM_AC_Charger;
import fr.sorbonne_u.components.hem2025e1.equipments.meter.ElectricMeter;
import fr.sorbonne_u.components.hem2025e1.equipments.ac.AC;
import fr.sorbonne_u.components.hem2025e1.equipments.charger.Charger;
import fr.sorbonne_u.utils.aclocks.ClocksServer;

public class CVMIntegrationTestACCharger extends AbstractCVM {

    public static final String CLOCK_URI = "ac-charger-clock";

    private static final long TOTAL_DURATION = 120000L;

    public CVMIntegrationTestACCharger() throws Exception {
        super();
        HEM_AC_Charger.VERBOSE = true;
        AC.VERBOSE = true;
        Charger.VERBOSE = true;
        ElectricMeter.VERBOSE = true;
    }

    @Override
    public void deploy() throws Exception {

    	  AbstractComponent.createComponent(
    	            ElectricMeter.class.getCanonicalName(),
    	            new Object[]{}
        );
        Thread.sleep(1000);

       

        // ---- 3) AC ----
        AbstractComponent.createComponent(
            AC.class.getCanonicalName(),
            new Object[]{}
        );
        Thread.sleep(1000);

        // ---- 4) Charger ----
        AbstractComponent.createComponent(
            Charger.class.getCanonicalName(),
            new Object[]{}
        );
        Thread.sleep(1000);

        // ---- 5) HEM AC + Charger ----
        AbstractComponent.createComponent(
            HEM_AC_Charger.class.getCanonicalName(),
            new Object[]{ true }  
        );
        Thread.sleep(2000);

        super.deploy();
    }

    @Override
    public void start() throws Exception {
        super.start();
    }

    @Override
    public void execute() throws Exception {
        super.execute();
    }

    @Override
    public void finalise() throws Exception {
        super.finalise();
    }

    @Override
    public void shutdown() throws Exception {
        super.shutdown();
    }

    public static void main(String[] args) {
        try {
            System.out.println("===== TEST AC + CHARGER =====");

            CVMIntegrationTestACCharger cvm = new CVMIntegrationTestACCharger();

            cvm.startStandardLifeCycle(TOTAL_DURATION);

            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
