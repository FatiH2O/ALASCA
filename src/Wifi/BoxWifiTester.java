package Wifi;

import Wifi.BoxWifiImplementationI.BoxWifiMode;

import fr.sorbonne_u.utils.aclocks.ClocksServerCI;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

// 🔹 On garde BoxWifiUserCI, car le test parle directement avec la BoxWifi
@RequiredInterfaces(required = {BoxWifiUserCI.class, ClocksServerCI.class})
public class BoxWifiTester extends AbstractComponent {

    protected  BoxWifiOutboundPort bwop;
    protected String wifiInboundPortURI;

    public static boolean VERBOSE = false;
    public static boolean EXCEPTIONS_VERBOSE = false;

    // Constructeurs
    public BoxWifiTester() {
        super(1, 1);
    }

    public BoxWifiTester(Boolean verbose) throws Exception {
        super(1, 0);
        BoxWifiTester.VERBOSE = verbose;
    }


    // 🔹 Initialisation et connexion
    protected void initialise() throws Exception {
    	
        this.bwop = new BoxWifiOutboundPort(this);
        this.bwop.publishPort();

        this.tracer.get().setTitle("BoxWifi Tester");
        this.toggleTracing();
        
        
            this.logMessage("BoxWifiTester initialisé - port outbound publié: " + this.bwop.getPortURI());
        
    }
    
    @Override
    public void start() throws ComponentStartException {
        super.start();
      
     
            this.logMessage(" Unitest boxwifi start");
			try {
				this.initialise();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
            //  Connexion à boxwifi
            try {
				this.doPortConnection(
				    this.bwop.getPortURI(),
				    BoxWifi.INBOUND_PORT_URI,
				    BoxWifiConnector.class.getCanonicalName()
				);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
            
            
            this.logMessage(" Connexion établie avec BoxWifi");
           
    }

    // 🔹 Test 1 : allumer et activer le Wifi
    public void testTurnOnAndActivateWifi() {
        try {
            this.logMessage("Turn ON the BoxWifi");
            this.bwop.turnOn();

            this.logMessage("Activate Wifi");
            this.bwop.activateWifi();

            BoxWifiMode mode = bwop.getMode();
            this.logMessage("Mode actuel : " + mode);

            if (mode != BoxWifiMode.FULL_ON) {
                this.logMessage("ERREUR: mode attendu FULL_ON");
            } else {
                this.logMessage("Test OK : BoxWifi FULL_ON");
            }
        } catch (Exception e) {
            this.logExceptionMessage("testTurnOnAndActivateWifi", e);
        }
    }

    // 🔹 Test 2 : désactivation du Wifi
    public void testDeactivateWifi() {
        try {
            this.logMessage("Turn ON the BoxWifi");
            bwop.turnOn();

            this.logMessage("Activate Wifi");
            bwop.activateWifi();

            this.logMessage("Deactivate Wifi");
            bwop.deactivateWifi();

            BoxWifiMode mode = bwop.getMode();
            this.logMessage("Mode après désactivation : " + mode);

            if (mode != BoxWifiMode.BOX_ONLY) {
                this.logMessage(" ERREUR: mode attendu BOX_ONLY");
            } else {
                this.logMessage("Test OK : désactivation réussie");
            }
        } catch (Exception e) {
            this.logExceptionMessage("testDeactivateWifi", e);
        }
    }

    // 🔹 Test 3 : activer le Wifi quand la Box est OFF
    public void testActivateWifiWhenOff() {
        try {
            this.logMessage("Activate Wifi while Box is OFF");
            bwop.activateWifi();
            this.logMessage("ERREUR: aucune exception levée !");
        } catch (Exception e) {
            this.logMessage("Exception attendue capturée : " + e.getMessage());
        }
    }

    // 🔹 Test 4 : éteindre la BoxWifi
    public void testTurnOffBox() {
        try {
            this.logMessage("Turn ON the BoxWifi");
            bwop.turnOn();

            this.logMessage("Activate Wifi");
            bwop.activateWifi();

            this.logMessage("Turn OFF the BoxWifi");
            bwop.turnOff();

            BoxWifiMode mode = bwop.getMode();
            this.logMessage("Mode après extinction : " + mode);

            if (mode != BoxWifiMode.OFF) {
                this.logMessage(" ERREUR: mode attendu OFF");
            } else {
                this.logMessage(" Test OK : BoxWifi OFF");
            }
        } catch (Exception e) {
            this.logExceptionMessage("testTurnOffBox", e);
        }
    }

    // 🔹 Exécution de tous les tests
    @Override
    public void execute() throws Exception {
    	
        super.execute();
        this.logMessage(">>> Début des tests BoxWifi");

        this.testTurnOnAndActivateWifi();

        this.testDeactivateWifi();
        this.testActivateWifiWhenOff();
       // this.testTurnOffBox();
        
        //pour les teste de la HEM
       // this.testTurnOnAndActivateWifi();
        this.logMessage(">>> Fin des tests BoxWifi");
    }

    // 🔹 Méthodes de fin de cycle
    @Override
    public synchronized void finalise() throws Exception {
        if (this.bwop != null && this.bwop.connected()) {
            this.doPortDisconnection(this.bwop.getPortURI());
        }
        super.finalise();
    }

    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        try {
            if (this.bwop != null)
                this.bwop.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }

    // 🔹 Méthode utilitaire de log d’exception
    private void logExceptionMessage(String testName, Exception e) {
        this.logMessage("Exception dans " + testName + " : " + e.getMessage());
        if (EXCEPTIONS_VERBOSE) e.printStackTrace();
    }
}
