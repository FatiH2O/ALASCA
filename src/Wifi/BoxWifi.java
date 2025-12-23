package Wifi;

import java.io.InputStream;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import AC_connections.ACExternalControlJava4InboundPort;
import ConnectorsGenerator.RegistationCIConnector;
import Hem.HEM;
import TumbledryerConection.RegistrationCIOP;
import WifiConnector.BoxWifiExternalControlInboundPort;

import fr.sorbonne_u.exceptions.PreconditionException;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.hem2025.bases.RegistrationCI;

@OfferedInterfaces(offered = { BoxWifiUserCI.class, BoxWifiExternalControlCI.class, BoxWifiImplementationI.class })
@RequiredInterfaces(required = { RegistrationCI.class })

public class BoxWifi extends AbstractComponent implements BoxWifiImplementationI, BoxWifiExternalControlI {

	public static final String EXTERNAL_CONTROL_INBOUND_PORT_URI = "BOX-WIFI-EXTERNAL-CONTROL-INBOUND-PORT-URI";

	// URI du port entrant utiliser dans le connecteur
	public static final String INBOUND_PORT_URI = "BOX-WIFI-INBOUND-PORT-URI";

	// Le port entrant
	protected BoxWifiInboundPort bwip;

	public static final String HEM_REGISTRATION_URI = "HEM-REGISTRATION-PORT-URI";

	public static final String NUMSERIE = "BOX-WIFI-001";

	/** when true, methods trace their actions in console. */
	public static boolean VERBOSE = true;

	/** initial mode of the box WiFi. */
	protected static final BoxWifiMode INITIAL_MODE = BoxWifiMode.OFF;

	/** current mode of the box WiFi. */
	protected BoxWifiMode currentMode;

	protected BoxWifiExternalControlInboundPort externalControlInboundPort;

	/** RegistrationCI outboundport pour appeler la HEM */
	protected RegistrationCIOP registrationOP;

	public static final Measure<Double> BOX_ONLY_POWER = new Measure<Double>(8.0, POWER_UNIT);
	public static final Measure<Double> FULL_ON_POWER = new Measure<Double>(12.0, POWER_UNIT);
	public static final Measure<Double> TENSION = new Measure<Double>(220.0, TENSION_UNIT);

	public static final int X_RELATIVE_POSITION = 2;
	public static final int Y_RELATIVE_POSITION = 2;
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a BoxWifi component.
	 *
	 * @throws Exception if an error occurs.
	 */

	public BoxWifi() throws Exception {
		super(1, 1);
		this.initialise(INBOUND_PORT_URI);
	}

	/**
	 * Constructeur avec URI spécifique du port inbound
	 */
	protected BoxWifi(String boxWifiInboundPortURI) throws Exception {
		super(1, 1);
		this.initialise(boxWifiInboundPortURI);
	}

	/**
	 * create a BoxWifi component with reflection inbound port URI and main inbound
	 * port URI. 1 thread and 0 thread schedular
	 * 
	 * @param reflectionInboundPortURI URI of the reflection inbound port.
	 * @param boxWifiInboundPortURI    URI of the box WiFi inbound port.
	 * @throws Exception if an error occurs.
	 */
	protected BoxWifi(String reflectionInboundPortURI, String boxWifiInboundPortURI) throws Exception {
		super(reflectionInboundPortURI, 1, 1);

		this.initialise(boxWifiInboundPortURI);

	}

	/**
	 * initialise the box WiFi component.
	 *
	 * @param boxWifiInboundPortURI URI of the box WiFi inbound port.
	 * @throws Exception if an error occurs.
	 */
	protected void initialise(String boxWifiInboundPortURI) throws Exception {
		assert boxWifiInboundPortURI != null : 
			new PreconditionException("boxWifiInboundPortURI != null");
		assert !boxWifiInboundPortURI.isEmpty() : 
			new PreconditionException("!boxWifiInboundPortURI.isEmpty()");

		this.currentMode = INITIAL_MODE;
		this.bwip = new BoxWifiInboundPort(boxWifiInboundPortURI, this);
		this.bwip.publishPort();

		if (BoxWifi.VERBOSE) {
			this.tracer.get().setTitle("Box WiFi component");
			this.tracer.get().setRelativePosition(X_RELATIVE_POSITION, Y_RELATIVE_POSITION);
			this.toggleTracing();
		}

	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		System.out.println(" BoxWifi.shutdown() - Cleaning up...");

		try {
			if (this.bwip != null) {
				this.bwip.unpublishPort();
			}

		} catch (Exception e) {
			System.err.println("BoxWifi shutdown failed: " + e.getMessage());
			throw new ComponentShutdownException(e);
		}

		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Services implementation (BoxWifiImplementationI)
	//
	@Override
	public void turnOn() throws Exception {
		this.currentMode = BoxWifiMode.BOX_ONLY;
		if (VERBOSE)
			this.traceMessage("Box allumée (WiFi désactivé).\n");

		// pour que la HEM puisse se connecter à notre AC
		this.externalControlInboundPort = new BoxWifiExternalControlInboundPort(EXTERNAL_CONTROL_INBOUND_PORT_URI,
				this);
		this.externalControlInboundPort.publishPort();

		// s'enregistrer aupres du HEM
		this.registrationOP = new RegistrationCIOP(this);
		this.registrationOP.publishPort();

		this.doPortConnection(registrationOP.getPortURI(), HEM.REGISTRATION_IP,
				RegistationCIConnector.class.getCanonicalName());

		URL url = this.getClass().getResource("/xml/BoxWifi_descriptor.xml");

		this.registrationOP.register(NUMSERIE, EXTERNAL_CONTROL_INBOUND_PORT_URI, url.getPath());

	}

	@Override
	public void turnOff() throws Exception {
		this.currentMode = BoxWifiMode.OFF;
		if (VERBOSE)
			this.traceMessage("Box éteinte.\n");

		// se desenregistrer
		this.registrationOP.unregister(NUMSERIE);
		this.registrationOP.unpublishPort();
		this.registrationOP.destroyPort();

		// détruire le port de connection dynamique avec la hem
		// this.externalControlInboundPort.unpublishPort();
		// this.externalControlInboundPort.destroyPort();
	}

	@Override
	public void activateWifi() throws Exception {
		if (this.currentMode != BoxWifiMode.OFF) {
			this.currentMode = BoxWifiMode.FULL_ON;
			if (VERBOSE)
				this.traceMessage("WiFi activé.\n");
		} else {
			if (VERBOSE)
				this.traceMessage("Impossible : Box éteinte.\n");
		}
	}

	@Override
	public void deactivateWifi() throws Exception {
		if (this.currentMode == BoxWifiMode.FULL_ON) {
			this.currentMode = BoxWifiMode.BOX_ONLY;
			if (VERBOSE)
				this.traceMessage("WiFi désactivé.\n");
		}
	}

	@Override
	public BoxWifiMode getMode() throws Exception {
		if (VERBOSE)
			this.traceMessage("Mode actuel : " + this.currentMode + ".\n");
		return this.currentMode;
	}

	@Override
	public boolean isOn() throws Exception {
		return this.currentMode != BoxWifiMode.OFF;
	}

	@Override
	public double getMaxPowerLevel() throws Exception {
		return 12.0;
	}

	@Override
	public void setCurrentPowerLevel(double powerLevel) throws Exception {
		// Simule une adaptation de la puissance selon la valeur
		if (powerLevel <= 0)
			this.turnOff();
		else if (powerLevel < 5)
			this.deactivateWifi();
		else if (powerLevel < 10)
			this.turnOn();
		else
			this.activateWifi();
		if (VERBOSE)
			this.traceMessage("Puissance actuelle ajustée à " + powerLevel + "W\n");

	}

	@Override
	public double getCurrentPowerLevel() throws Exception {
		switch (this.currentMode) {
		case OFF:
			return 0.0;
		case BOX_ONLY:
			return 8.0;
		case FULL_ON:
			return 12.0;
		default:
			return 0.0;
		}
	}

}
