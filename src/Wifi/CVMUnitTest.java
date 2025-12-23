package Wifi;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.BCMException;

public class CVMUnitTest extends AbstractCVM {

    public CVMUnitTest() throws Exception {
        super();
        BoxWifiTester.VERBOSE = true;
        BoxWifi.VERBOSE = true;
    }

    @Override
    public void deploy() throws Exception {
        //  Création du composant principal BoxWifi
        AbstractComponent.createComponent(
            BoxWifi.class.getCanonicalName(),
            new Object[]{} // constructeur par défaut
        );

        // Création du composant testeur relié à l’inbound port de la Box
        AbstractComponent.createComponent(
            BoxWifiTester.class.getCanonicalName(),
            new Object[]{true, BoxWifi.INBOUND_PORT_URI}
        );

        super.deploy();
    }

    public static void main(String[] args) {
        BCMException.VERBOSE = true;
        BoxWifiTester.EXCEPTIONS_VERBOSE = true;
        try {
            CVMUnitTest cvm = new CVMUnitTest();
            cvm.startStandardLifeCycle(10000L);
            Thread.sleep(10000L);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
