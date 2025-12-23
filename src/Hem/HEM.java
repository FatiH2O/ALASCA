package Hem;

// Copyright Jacques Malenfant, Sorbonne Universite.

// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to implement a mock-up
// of household energy management system.
//
// This software is governed by the CeCILL-C license under French law and
// abiding by the rules of distribution of free software.  You can use,
// modify and/ or redistribute the software under the terms of the
// CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
// URL "http://www.cecill.info".
//
// As a counterpart to the access to the source code and  rights to copy,
// modify and redistribute granted by the license, users are provided only
// with a limited warranty  and the software's author,  the holder of the
// economic rights,  and the successive licensors  have only  limited
// liability. 
//
// In this respect, the user's attention is drawn to the risks associated
// with loading,  using,  modifying and/or developing or reproducing the
// software by the user in light of its specific status of free software,
// that may mean  that it is complicated to manipulate,  and  that  also
// therefore means  that it is reserved for developers  and  experienced
// professionals having in-depth computer knowledge. Users are therefore
// encouraged to load and test the software's suitability as regards their
// requirements in conditions enabling the security of their systems and/or 
// data to be ensured and,  more generally, to use and operate it in the 
// same conditions as regards security. 
//
// The fact that you are presently reading this means that you have had
// knowledge of the CeCILL-C license and that you accept its terms.

import fr.sorbonne_u.components.AbstractComponent;


import Wifi.BoxWifi;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.BCMException;
import fr.sorbonne_u.components.exceptions.BCMRuntimeException;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.hem2025.bases.AdjustableCI;
import fr.sorbonne_u.components.hem2025.bases.RegistrationCI;
import fr.sorbonne_u.components.hem2025.tests_utils.TestsStatistics;
import fr.sorbonne_u.components.hem2025e1.equipments.batteries.Batteries;
import fr.sorbonne_u.components.hem2025e1.equipments.batteries.BatteriesCI;
import fr.sorbonne_u.components.hem2025e1.equipments.batteries.BatteriesUnitTester;
import fr.sorbonne_u.components.hem2025e1.equipments.batteries.connections.BatteriesConnector;
import fr.sorbonne_u.components.hem2025e1.equipments.batteries.connections.BatteriesOutboundPort;
import fr.sorbonne_u.components.hem2025e1.equipments.generator.Generator;
import fr.sorbonne_u.components.hem2025e1.equipments.generator.GeneratorCI;
import fr.sorbonne_u.components.hem2025e1.equipments.generator.GeneratorUnitTester;
import fr.sorbonne_u.components.hem2025e1.equipments.generator.connections.GeneratorConnector;
import fr.sorbonne_u.components.hem2025e1.equipments.generator.connections.GeneratorOutboundPort;
import fr.sorbonne_u.components.hem2025e1.equipments.heater.Heater;
import fr.sorbonne_u.components.hem2025e1.equipments.heater.HeaterUnitTester;
import fr.sorbonne_u.components.hem2025e1.equipments.hem.AdjustableOutboundPort;
import fr.sorbonne_u.components.hem2025e1.equipments.hem.HeaterConnector;
import fr.sorbonne_u.components.hem2025e1.equipments.meter.ElectricMeter;
import fr.sorbonne_u.components.hem2025e1.equipments.meter.ElectricMeterCI;

import fr.sorbonne_u.components.hem2025e1.equipments.meter.ElectricMeterUnitTester;
import fr.sorbonne_u.components.hem2025e1.equipments.meter.connections.ElectricMeterConnector;
import fr.sorbonne_u.components.hem2025e1.equipments.meter.connections.ElectricMeterOutboundPort;
import fr.sorbonne_u.components.hem2025e1.equipments.solar_panel.SolarPanel;
import fr.sorbonne_u.components.hem2025e1.equipments.solar_panel.SolarPanelCI;
import fr.sorbonne_u.components.hem2025e1.equipments.solar_panel.SolarPanelUnitTester;
import fr.sorbonne_u.components.hem2025e1.equipments.solar_panel.connections.SolarPanelConnector;
import fr.sorbonne_u.components.hem2025e1.equipments.solar_panel.connections.SolarPanelOutboundPort;
import fr.sorbonne_u.exceptions.ImplementationInvariantException;
import fr.sorbonne_u.exceptions.AssertionChecking;
import fr.sorbonne_u.exceptions.InvariantException;
import fr.sorbonne_u.exceptions.PreconditionException;
import fr.sorbonne_u.utils.aclocks.AcceleratedClock;
import fr.sorbonne_u.utils.aclocks.ClocksServerCI;

import java.io.File;
import java.time.Instant;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import AC.AC;
import ConnectorsGenerator.Parser;
import ConnectorsGenerator.ConnectorGenerator;
import ConnectorsGenerator.Parser.ControlAdapterDescriptor;
import Tumbledryer.TumbleDryer;
import TumbledryerConection.RegistrationCIIP;

// -----------------------------------------------------------------------------
/**
 * The class <code>HEM</code> implements the basis for a household energy
 * management component.
 *
 * <p>
 * <strong>Description</strong>
 * </p>
 * 
 * <p>
 * As is, this component is only a very limited starting point for the actual
 * component. The given code is there only to ease the understanding of the
 * objectives, but most of it must be replaced to get the correct code.
 * Especially, no registration of the components representing the appliances is
 * given.
 * </p>
 * 
 * <p>
 * <strong>Implementation Invariants</strong>
 * </p>
 * 
 * <pre>
 * invariant	{@code
 * true
 * }	// no more invariant
 * </pre>
 * 
 * <p>
 * <strong>Invariants</strong>
 * </p>
 * 
 * <pre>
 * invariant	{@code
 * X_RELATIVE_POSITION >= 0
 * }
 * invariant	{@code
 * Y_RELATIVE_POSITION >= 0
 * }
 * </pre>
 * 
 * <p>
 * Created on : 2021-09-09
 * </p>
 * 
 * @author <a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 * 
 * 
 */

@OfferedInterfaces(offered = { RegistrationCI.class, RegistrationCI.class })

@RequiredInterfaces(required = { ClocksServerCI.class, AdjustableCI.class, ElectricMeterCI.class, BatteriesCI.class,
		SolarPanelCI.class, GeneratorCI.class, })
public class HEM extends AbstractComponent

{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/** when true, methods trace their actions. */
	public static boolean VERBOSE = false;
	/** when tracing, x coordinate of the window relative position. */
	public static int X_RELATIVE_POSITION = 0;
	/** when tracing, y coordinate of the window relative position. */
	public static int Y_RELATIVE_POSITION = 0;

	/** port to connect to the electric meter. */
	protected ElectricMeterOutboundPort meterop;

	/** port to connect to the batteries. */
	protected BatteriesOutboundPort batteriesop;
	/** port to connect to the solar panel. */
	protected SolarPanelOutboundPort solarPanelop;
	/** port to connect to the generator. */
	protected GeneratorOutboundPort generatorop;

	/**
	 * when true, manage the heater in a customised way, otherwise let it register
	 * itself as an adjustable appliance.
	 */
	protected boolean isPreFirstStep;
	/** port to connect to the heater when managed in a customised way. */
	protected AdjustableOutboundPort heaterop;

	/** Registration inboundport pour TD */
	protected RegistrationCIIP registrationIP;

	/** Registration inboundport URI know by all the components */
	public static final String REGISTRATION_IP = "REGISTRATION_IP";
	/** Pour garder en mémoire les composant déja connécté à l'HEM */
	Hashtable<String, AdjustableOutboundPort> registation = new Hashtable<>();

	/**
	 * when true, this implementation of the HEM performs the tests that are planned
	 * in the method execute.
	 */
	protected boolean performTest;
	/** accelerated clock used for the tests. */
	protected AcceleratedClock ac;

	// -------------------------------------------------------------------------
	// Invariants
	// -------------------------------------------------------------------------

	/**
	 * return true if the implementation invariants are observed, false otherwise.
	 * 
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	{@code
	 * hem != null
	 * }
	 * post	{@code
	 * true
	 * }	// no postcondition.
	 * </pre>
	 *
	 * @param hem instance to be tested.
	 * @return true if the implementation invariants are observed, false otherwise.
	 */
	protected static boolean implementationInvariants(HEM hem) {
		assert hem != null : new PreconditionException("hem != null");

		boolean ret = true;
		return ret;
	}

	/**
	 * return true if the invariants are observed, false otherwise.
	 * 
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	{@code
	 * hem != null
	 * }
	 * post	{@code
	 * true
	 * }	// no postcondition.
	 * </pre>
	 *
	 * @param hem instance to be tested.
	 * @return true if the invariants are observed, false otherwise.
	 */
	protected static boolean invariants(HEM hem) {
		assert hem != null : new PreconditionException("hem != null");

		boolean ret = true;
		ret &= AssertionChecking.checkImplementationInvariant(X_RELATIVE_POSITION >= 0, HEM.class, hem,
				"X_RELATIVE_POSITION >= 0");
		ret &= AssertionChecking.checkImplementationInvariant(Y_RELATIVE_POSITION >= 0, HEM.class, hem,
				"Y_RELATIVE_POSITION >= 0");
		return ret;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a household energy manager component.
	 * 
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	{@code
	 * true
	 * }	// no precondition.
	 * post	{@code
	 * true
	 * }	// no postcondition.
	 * </pre>
	 * 
	 * @throws Exception
	 *
	 */
	protected HEM() throws Exception {
		// by default, perform the tests planned in the method execute.
		this(true);
	}

	/**
	 * create a household energy manager component.
	 * 
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	{@code
	 * true
	 * }	// no precondition.
	 * post	{@code
	 * true
	 * }	// no postcondition.
	 * </pre>
	 *
	 * @param performTest if {@code true}, the HEM performs the planned tests,
	 *                    otherwise not.
	 * @throws Exception
	 */
	protected HEM(boolean performTest) throws Exception {
		// 1 standard thread to execute the method execute and 1 schedulable
		// thread that is used to perform the tests
		super(1, 1);

		this.performTest = performTest;

		// by default, consider this execution as one in the pre-first step
		// and manage the heater in a customised way.
		this.isPreFirstStep = true;

		// to register the different components

		this.registrationIP = new RegistrationCIIP(REGISTRATION_IP, this);
		this.registrationIP.publishPort();

		if (VERBOSE) {
			this.tracer.get().setTitle("Home Energy Manager component");
			this.tracer.get().setRelativePosition(X_RELATIVE_POSITION, Y_RELATIVE_POSITION);
			this.toggleTracing();
		}

		assert HEM.implementationInvariants(this)
				: new ImplementationInvariantException("HEM.implementationInvariants(this)");
		assert HEM.invariants(this) : new InvariantException("HEM.invariants(this)");
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public synchronized void start() throws ComponentStartException {
		super.start();

		try {
			this.meterop = new ElectricMeterOutboundPort(this);
			this.meterop.publishPort();
			this.doPortConnection(this.meterop.getPortURI(), ElectricMeter.ELECTRIC_METER_INBOUND_PORT_URI,
					ElectricMeterConnector.class.getCanonicalName());
			this.batteriesop = new BatteriesOutboundPort(this);
			this.batteriesop.publishPort();
			this.doPortConnection(batteriesop.getPortURI(), Batteries.STANDARD_INBOUND_PORT_URI,
					BatteriesConnector.class.getCanonicalName());
			this.solarPanelop = new SolarPanelOutboundPort(this);
			this.solarPanelop.publishPort();
			this.doPortConnection(this.solarPanelop.getPortURI(), SolarPanel.STANDARD_INBOUND_PORT_URI,
					SolarPanelConnector.class.getCanonicalName());
			this.generatorop = new GeneratorOutboundPort(this);
			this.generatorop.publishPort();
			this.doPortConnection(this.generatorop.getPortURI(), Generator.STANDARD_INBOUND_PORT_URI,
					GeneratorConnector.class.getCanonicalName());

			if (this.isPreFirstStep) {
				// in this case, connect using the statically customised
				// heater connector and keep a specific outbound port to
				// call the heater.
				this.heaterop = new AdjustableOutboundPort(this);
				this.heaterop.publishPort();
				this.doPortConnection(this.heaterop.getPortURI(), Heater.EXTERNAL_CONTROL_INBOUND_PORT_URI,
						HeaterConnector.class.getCanonicalName());

			}

		} catch (Throwable e) {
			throw new ComponentStartException(e);
		}

		// ma connecton pour tumbledryer
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public synchronized void execute() throws Exception {

		if (this.performTest) {
			this.logMessage("Electric meter tests start.");
			this.testMeter();
			this.logMessage("Electric meter tests end.");
			this.logMessage("Batteries tests start.");
			this.testBatteries();
			this.logMessage("Batteries tests end.");
			this.logMessage("Solar Panel tests start.");
			this.testSolarPanel();
			this.logMessage("Solar Panel tests end.");
			this.logMessage("Generator tests start.");
			// this.testGenerator();
			this.logMessage("Generator tests end.");
			if (this.isPreFirstStep) {
				// this.scheduleTestHeater();
			}

			this.logMessage("TUMBLEDRYER TEST START");
			this.testTumbleDryer();
			this.logMessage("TUMBLEDRYER TEST END.");

			this.logMessage("AC TEST START");
			this.testAC();
			this.logMessage("AC TEST END");

			this.logMessage("WIFI TEST START");
			this.testWifi();
			this.logMessage("WIFI TEST END");
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 */
	@Override
	public synchronized void finalise() throws Exception {
		this.doPortDisconnection(this.meterop.getPortURI());
		this.doPortDisconnection(this.batteriesop.getPortURI());
		this.doPortDisconnection(this.solarPanelop.getPortURI());
		this.doPortDisconnection(this.generatorop.getPortURI());
		if (this.isPreFirstStep) {
			this.doPortDisconnection(this.heaterop.getPortURI());
		}

		// faudra deconnecter mon tumbledryer
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.meterop.unpublishPort();
			this.batteriesop.unpublishPort();
			this.solarPanelop.unpublishPort();
			this.generatorop.unpublishPort();
			if (this.isPreFirstStep) {
				this.heaterop.unpublishPort();
			}
		} catch (Throwable e) {
			throw new ComponentShutdownException(e);
		}

		// détruire le port d'enregistrement
		try {
			this.registrationIP.unpublishPort();

		} catch (Exception e) {

			e.printStackTrace();
		}
		try {
			this.registrationIP.destroyPort();

		} catch (Exception e) {

			e.printStackTrace();
		}

		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Internal methods
	// -------------------------------------------------------------------------

	/**
	 * test the {@code ElectricMeter} component.
	 * 
	 * <p>
	 * <strong>Description</strong>
	 * </p>
	 * 
	 * <p>
	 * Calls the test methods defined in {@code ElectricMeterUnitTester}.
	 * </p>
	 * 
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	{@code
	 * true
	 * }	// no precondition.
	 * post	{@code
	 * true
	 * }	// no postcondition.
	 * </pre>
	 *
	 * @throws Exception <i>to do</i>.
	 */
	protected void testMeter() throws Exception {
		ElectricMeterUnitTester.runAllTests(this, this.meterop, new TestsStatistics());
	}

	/**
	 * test the {@code Batteries} component.
	 * 
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	{@code
	 * true
	 * }	// no precondition.
	 * post	{@code
	 * true
	 * }	// no postcondition.
	 * </pre>
	 * 
	 * @throws Exception <i>to do</i>.
	 *
	 */
	protected void testBatteries() throws Exception {
		BatteriesUnitTester.runAllTests(this, this.batteriesop, new TestsStatistics());
	}

	/**
	 * test the {@code SolarPanel} component.
	 * 
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	{@code
	 * true
	 * }	// no precondition.
	 * post	{@code
	 * true
	 * }	// no postcondition.
	 * </pre>
	 * 
	 * @throws Exception <i>to do</i>.
	 *
	 */
	protected void testSolarPanel() throws Exception {
		SolarPanelUnitTester.runAllTests(this, this.solarPanelop, new TestsStatistics());
	}

	/**
	 * test the {@code Generator} component.
	 * 
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	{@code
	 * true
	 * }	// no precondition.
	 * post	{@code
	 * true
	 * }	// no postcondition.
	 * </pre>
	 * 
	 * @throws Exception <i>to do</i>.
	 *
	 */
	protected void testGenerator() throws Exception {
		GeneratorUnitTester.runAllTests(this, this.generatorop, new TestsStatistics());
	}

	/**
	 * test the heater.
	 * 
	 * <p>
	 * <strong>Gherkin specification</strong>
	 * </p>
	 * 
	 * <pre>
	 * Feature: adjustable appliance mode management
	 *   Scenario: getting the max mode index
	 *     Given the heater has just been turned on
	 *     When I call maxMode()
	 *     Then the result is its max mode index
	 *   Scenario: getting the current mode index
	 *     Given the heater has just been turned on
	 *     When I call currentMode()
	 *     Then the current mode is its max mode
	 *   Scenario: going down one mode index
	 *     Given the heater is turned on
	 *     And the current mode index is the max mode index
	 *     When I call downMode()
	 *     Then the method returns true
	 *     And the current mode is its max mode minus one
	 *   Scenario: going up one mode index
	 *     Given the heater is turned on
	 *     And the current mode index is the max mode index minus one
	 *     When I call upMode()
	 *     Then the method returns true
	 *     And the current mode is its max mode
	 *   Scenario: setting the mode index
	 *     Given the heater is turned on
	 *     And the mode index 1 is legitimate
	 *     When I call setMode(1)
	 *     Then the method returns true
	 *     And the current mode is 1
	 * Feature: Getting the power consumption given a mode
	 *   Scenario: getting the power consumption of the maximum mode
	 *     Given the heater is turned on
	 *     When I get the power consumption of the maximum mode
	 *     Then the result is the maximum power consumption of the heater
	 * Feature: suspending and resuming
	 *   Scenario: checking if suspended when not
	 *     Given the heater is turned on
	 *     And it has not been suspended yet
	 *     When I check if suspended
	 *     Then it is not
	 *   Scenario: suspending
	 *     Given the heater is turned on
	 *     And it is not suspended
	 *     When I call suspend()
	 *     Then the method returns true
	 *     And the heater is suspended
	 *   Scenario: going down one mode index when suspended
	 *     Given the heater is turned on
	 *     And the heater is suspended
	 *     When I call downMode()
	 *     Then a precondition exception is thrown
	 *   Scenario: going up one mode index when suspended
	 *     Given the heater is turned on
	 *     And the heater is suspended
	 *     When I call upMode()
	 *     Then a precondition exception is thrown
	 *   Scenario: going up one mode index when suspended
	 *     Given the heater is turned on
	 *     And the heater is suspended
	 *     When I call upMode()
	 *     Then a precondition exception is thrown
	 *   Scenario: getting the current mode when suspended
	 *     Given the heater is turned on
	 *     And the heater is suspended
	 *     When I get the current mode
	 *     Then a precondition exception is thrown
	 *   Scenario: checking the emergency
	 *     Given the heater is turned on
	 *     And it has just been suspended
	 *     When I call emergency()
	 *     Then the emergency is between 0.0 and 1.0
	 *   Scenario: resuming
	 *     Given the heater is turned on
	 *     And it is suspended
	 *     When I call resume()
	 *     Then the method returns true
	 *     And the heater is not suspended
	 * </pre>
	 * 
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	{@code
	 * true
	 * }	// no precondition.
	 * post	{@code
	 * true
	 * }	// no postcondition.
	 * </pre>
	 *
	 * @throws Exception <i>to do</i>.
	 */
	protected void testHeater() throws Exception {
		this.logMessage("Heater tests start.");
		TestsStatistics statistics = new TestsStatistics();
		try {
			this.logMessage("Feature: adjustable appliance mode management");
			this.logMessage("  Scenario: getting the max mode index");
			this.logMessage("    Given the heater has just been turned on");
			this.logMessage("    When I call maxMode()");
			this.logMessage("    Then the result is its max mode index");
			final int maxMode = heaterop.maxMode();

			statistics.updateStatistics();

			this.logMessage("  Scenario: getting the current mode index");
			this.logMessage("    Given the heater has just been turned on");
			this.logMessage("    When I call currentMode()");
			this.logMessage("    Then the current mode is its max mode");
			int result = heaterop.currentMode();
			if (result != maxMode) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: going down one mode index");
			this.logMessage("    Given the heater is turned on");
			this.logMessage("    And the current mode index is the max mode index");
			result = heaterop.currentMode();
			if (result != maxMode) {
				this.logMessage("      but was: " + result);
				statistics.failedCondition();
			}
			this.logMessage("    When I call downMode()");
			this.logMessage("    Then the method returns true");
			boolean bResult = heaterop.downMode();
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the current mode is its max mode minus one");
			result = heaterop.currentMode();
			if (result != maxMode - 1) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: going up one mode index");
			this.logMessage("    Given the heater is turned on");
			this.logMessage("    And the current mode index is the max mode index minus one");
			result = heaterop.currentMode();
			if (result != maxMode - 1) {
				this.logMessage("      but was: " + result);
				statistics.failedCondition();
			}
			this.logMessage("    When I call upMode()");
			this.logMessage("    Then the method returns true");
			bResult = heaterop.upMode();
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the current mode is its max mode");
			result = heaterop.currentMode();
			if (result != maxMode) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: setting the mode index");
			this.logMessage("    Given the heater is turned on");
			int index = 1;
			this.logMessage("    And the mode index 1 is legitimate");
			if (index > maxMode) {
				this.logMessage("      but was not!");
				statistics.failedCondition();
			}
			this.logMessage("    When I call setMode(1)");
			this.logMessage("    Then the method returns true");
			bResult = heaterop.setMode(1);
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the current mode is 1");
			result = heaterop.currentMode();
			if (result != 1) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("Feature: Getting the power consumption given a mode");
			this.logMessage("  Scenario: getting the power consumption of the maximum mode");
			this.logMessage("    Given the heater is turned on");
			this.logMessage("    When I get the power consumption of the maximum mode");
			double dResult = heaterop.getModeConsumption(maxMode);
			this.logMessage("    Then the result is the maximum power consumption of the heater");

			statistics.updateStatistics();

			this.logMessage("Feature: suspending and resuming");
			this.logMessage("  Scenario: checking if suspended when not");
			this.logMessage("    Given the heater is turned on");
			this.logMessage("    And it has not been suspended yet");
			this.logMessage("    When I check if suspended");
			bResult = heaterop.suspended();
			this.logMessage("    Then it is not");
			if (bResult) {
				this.logMessage("      but it was!");
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: suspending");
			this.logMessage("    Given the heater is turned on");
			this.logMessage("    And it is not suspended");
			bResult = heaterop.suspended();
			if (bResult) {
				this.logMessage("      but it was!");
				statistics.failedCondition();
				;
			}
			this.logMessage("    When I call suspend()");
			bResult = heaterop.suspend();
			this.logMessage("    Then the method returns true");
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the heater is suspended");
			bResult = heaterop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: going down one mode index when suspended");
			this.logMessage("    Given the heater is turned on");
			this.logMessage("    And the heater is suspended");
			bResult = heaterop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();
				;
			}
			this.logMessage("    When I call downMode()");
			this.logMessage("    Then a precondition exception is thrown");
			boolean old = BCMException.VERBOSE;
			try {
				BCMException.VERBOSE = false;
				heaterop.downMode();
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			} catch (Throwable e) {
			} finally {
				BCMException.VERBOSE = old;
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: going up one mode index when suspended");
			this.logMessage("    Given the heater is turned on");
			this.logMessage("    And the heater is suspended");
			bResult = heaterop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();
				;
			}
			this.logMessage("    When I call upMode()");
			this.logMessage("    Then a precondition exception is thrown");
			old = BCMException.VERBOSE;
			try {
				BCMException.VERBOSE = false;
				heaterop.upMode();
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			} catch (Throwable e) {
			} finally {
				BCMException.VERBOSE = old;
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: setting the mode when suspended");
			this.logMessage("    Given the heater is turned on");
			this.logMessage("    And the heater is suspended");
			bResult = heaterop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();
				;
			}
			this.logMessage("    And the mode index 1 is legitimate");
			if (index > maxMode) {
				this.logMessage("      but was not!");
				statistics.failedCondition();
			}
			this.logMessage("    When I call setMode(1)");
			this.logMessage("    Then a precondition exception is thrown");
			old = BCMException.VERBOSE;
			try {
				BCMException.VERBOSE = false;
				heaterop.upMode();
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			} catch (Throwable e) {
			} finally {
				BCMException.VERBOSE = old;
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: getting the current mode when suspended");
			this.logMessage("    Given the heater is turned on");
			this.logMessage("    And the heater is suspended");
			bResult = heaterop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();
				;
			}
			this.logMessage("    When I get the current mode");
			this.logMessage("    Then a precondition exception is thrown");
			old = BCMException.VERBOSE;
			try {
				BCMException.VERBOSE = false;
				heaterop.currentMode();
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			} catch (Throwable e) {
			} finally {
				BCMException.VERBOSE = old;
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: checking the emergency");
			this.logMessage("    Given the heater is turned on");
			this.logMessage("    And it has just been suspended");
			bResult = heaterop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();
				;
			}
			this.logMessage("    When I call emergency()");
			dResult = heaterop.emergency();
			this.logMessage("    Then the emergency is between 0.0 and 1.0");
			if (dResult < 0.0 || dResult > 1.0) {
				this.logMessage("      but was: " + dResult);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: resuming");
			this.logMessage("    Given the heater is turned on");
			this.logMessage("    And it is suspended");
			bResult = heaterop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();
				;
			}
			this.logMessage("    When I call resume()");
			bResult = heaterop.resume();
			this.logMessage("    Then the method returns true");
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the heater is not suspended");
			bResult = heaterop.suspended();
			if (bResult) {
				this.logMessage("      but it was!");
				statistics.incorrectResult();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		statistics.updateStatistics();
		statistics.statisticsReport(this);

		this.logMessage("Heater tests end.");
	}

	/**
	 * test the TumbleDryer.
	 * 
	 * <p>
	 * <strong>Gherkin specification</strong>
	 * </p>
	 * 
	 * <pre>
	 * Feature: adjustable appliance mode management
	 *   Scenario: getting the max mode index
	 *     Given the TumbleDryer has just been turned on
	 *     When I call maxMode()
	 *     Then the result is its max mode index
	 *   Scenario: getting the current mode index
	 *     Given the TumbleDryer has just been turned on
	 *     When I call currentMode()
	 *     Then the current mode is its max mode
	 *   Scenario: going down one mode index
	 *     Given the TumbleDryer is turned on
	 *     And the current mode index is the max mode index
	 *     When I call downMode()
	 *     Then the method returns true
	 *     And the current mode is its max mode minus one
	 *   Scenario: going up one mode index
	 *     Given the TumbleDryer is turned on
	 *     And the current mode index is the max mode index minus one
	 *     When I call upMode()
	 *     Then the method returns true
	 *     And the current mode is its max mode
	 *   Scenario: setting the mode index
	 *     Given the TumbleDryer is turned on
	 *     And the mode index 1 is legitimate
	 *     When I call setMode(1)
	 *     Then the method returns true
	 *     And the current mode is 1
	 * Feature: Getting the power consumption given a mode
	 *   Scenario: getting the power consumption of the maximum mode
	 *     Given the TumbleDryer is turned on
	 *     When I get the power consumption of the maximum mode
	 *     Then the result is the maximum power consumption of the TumbleDryer
	 * Feature: suspending and resuming
	 *   Scenario: checking if suspended when not
	 *     Given the TumbleDryer is turned on
	 *     And it has not been suspended yet
	 *     When I check if suspended
	 *     Then it is not
	 *   Scenario: suspending
	 *     Given the TumbleDryer is turned on
	 *     And it is not suspended
	 *     When I call suspend()
	 *     Then the method returns true
	 *     And the TumbleDryer is suspended
	 *   Scenario: going down one mode index when suspended
	 *     Given the heater is turned on
	 *     And the heater is suspended
	 *     When I call downMode()
	 *     Then a precondition exception is thrown
	 *   Scenario: going up one mode index when suspended
	 *     Given the TumbleDryer is turned on
	 *     And the TumbleDryer is suspended
	 *     When I call upMode()
	 *     Then a precondition exception is thrown
	 *   Scenario: going up one mode index when suspended
	 *     Given the TumbleDryer is turned on
	 *     And the TumbleDryer is suspended
	 *     When I call upMode()
	 *     Then a precondition exception is thrown
	 *   Scenario: getting the current mode when suspended
	 *     Given the TumbleDryer is turned on
	 *     And the TumbleDryer is suspended
	 *     When I get the current mode
	 *     Then a precondition exception is thrown
	 *   Scenario: checking the emergency
	 *     Given the TumbleDryer is turned on
	 *     And it has just been suspended
	 *     When I call emergency()
	 *     Then the emergency is between 0.0 and 1.0
	 *   Scenario: resuming
	 *     Given the TumbleDryer is turned on
	 *     And it is suspended
	 *     When I call resume()
	 *     Then the method returns true
	 *     And the TumbleDryer is not suspended
	 * </pre>
	 * 
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	{@code
	 * true
	 * }	// no precondition.
	 * post	{@code
	 * true
	 * }	// no postcondition.
	 * </pre>
	 *
	 * @throws Exception <i>to do</i>.
	 */

	protected void testTumbleDryer() throws Exception {
		Thread.sleep(2000);// pour s'assurer que TD est on grace au unitTest

		this.logMessage("TumbleDryer tests start.");
		TestsStatistics statistics = new TestsStatistics();
		try {
			this.logMessage("Feature: adjustable appliance mode management");
			this.logMessage("  Scenario: getting the max mode index");
			this.logMessage("    Given the TumbleDryer has just been turned on");
			this.logMessage("    When I call maxMode()");
			this.logMessage("    Then the result is its max mode index");
			final int maxMode = this.registation.get(TumbleDryer.NUMSERIE).maxMode();

			statistics.updateStatistics();

			this.logMessage("  Scenario: getting the current mode index");
			this.logMessage("    Given the TumbleDryer has just been turned on");
			this.logMessage("    When I call currentMode()");
			this.logMessage("    Then the current mode is its max mode");
			int result = this.registation.get(TumbleDryer.NUMSERIE).currentMode();
			if (result != maxMode) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: going down one mode index");
			this.logMessage("    Given the TumbleDryer is turned on");
			this.logMessage("    And the current mode index is the max mode index");
			result = this.registation.get(TumbleDryer.NUMSERIE).currentMode();
			if (result != maxMode) {
				this.logMessage("      but was: " + result);
				statistics.failedCondition();
			}
			this.logMessage("    When I call downMode()");
			this.logMessage("    Then the method returns true");
			boolean bResult = this.registation.get(TumbleDryer.NUMSERIE).downMode();
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the current mode is its max mode minus one");
			result = this.registation.get(TumbleDryer.NUMSERIE).currentMode();
			if (result != maxMode - 1) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: going up one mode index");
			this.logMessage("    Given the TumbleDryer is turned on");
			this.logMessage("    And the current mode index is the max mode index minus one");
			result = this.registation.get(TumbleDryer.NUMSERIE).currentMode();
			if (result != maxMode - 1) {
				this.logMessage("      but was: " + result);
				statistics.failedCondition();
			}
			this.logMessage("    When I call upMode()");
			this.logMessage("    Then the method returns true");

			bResult = this.registation.get(TumbleDryer.NUMSERIE).upMode();
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the current mode is its max mode");
			result = this.registation.get(TumbleDryer.NUMSERIE).currentMode();
			if (result != maxMode) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: setting the mode index");
			this.logMessage("    Given the TumbleDryer is turned on");
			int index = 1;
			this.logMessage("    And the mode index 1 is legitimate");
			if (index > maxMode) {
				this.logMessage("      but was not!");
				statistics.failedCondition();
			}
			this.logMessage("    When I call setMode(1)");
			this.logMessage("    Then the method returns true");
			bResult = this.registation.get(TumbleDryer.NUMSERIE).setMode(1);
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the current mode is 1");
			result = this.registation.get(TumbleDryer.NUMSERIE).currentMode();
			if (result != 1) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("Feature: Getting the power consumption given a mode");
			this.logMessage("  Scenario: getting the power consumption of the maximum mode");
			this.logMessage("    Given the TumbleDryer is turned on");
			this.logMessage("    When I get the power consumption of the maximum mode");
			double dResult = this.registation.get(TumbleDryer.NUMSERIE).getModeConsumption(maxMode);
			this.logMessage("    Then the result is the maximum power consumption of the TumbleDryer");

			statistics.updateStatistics();

			this.logMessage("Feature: suspending and resuming");
			this.logMessage("  Scenario: checking if suspended when not");
			this.logMessage("    Given the TumbleDryer is turned on");
			this.logMessage("    And it has not been suspended yet");
			this.logMessage("    When I check if suspended");
			bResult = this.registation.get(TumbleDryer.NUMSERIE).suspended();
			this.logMessage("    Then it is not");
			if (bResult) {
				this.logMessage("      but it was!");
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: suspending");
			this.logMessage("    Given the TumbleDryer is turned on");
			this.logMessage("    And it is not suspended");
			bResult = this.registation.get(TumbleDryer.NUMSERIE).suspended();
			if (bResult) {
				this.logMessage("      but it was!");
				statistics.failedCondition();
				;
			}
			this.logMessage("    When I call suspend()");
			bResult = this.registation.get(TumbleDryer.NUMSERIE).suspend();
			this.logMessage("    Then the method returns true");
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the TumbleDryer is suspended");
			bResult = this.registation.get(TumbleDryer.NUMSERIE).suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: going down one mode index when suspended");
			this.logMessage("    Given the TumbleDryer is turned on");
			this.logMessage("    And the TumbleDryer is suspended");
			bResult = this.registation.get(TumbleDryer.NUMSERIE).suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();
				;
			}
			this.logMessage("    When I call downMode()");
			this.logMessage("    Then a precondition exception is thrown");
			boolean old = BCMException.VERBOSE;
			try {
				BCMException.VERBOSE = false;
				this.registation.get(TumbleDryer.NUMSERIE).downMode();
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			} catch (Throwable e) {
			} finally {
				BCMException.VERBOSE = old;
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: going up one mode index when suspended");
			this.logMessage("    Given the TumbleDryer is turned on");
			this.logMessage("    And the TumbleDryer is suspended");
			bResult = this.registation.get(TumbleDryer.NUMSERIE).suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();
				;
			}
			this.logMessage("    When I call upMode()");
			this.logMessage("    Then a precondition exception is thrown");
			old = BCMException.VERBOSE;
			try {
				BCMException.VERBOSE = false;
				this.registation.get(TumbleDryer.NUMSERIE).upMode();
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			} catch (Throwable e) {
			} finally {
				BCMException.VERBOSE = old;
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: setting the mode when suspended");
			this.logMessage("    Given the TumbleDryer is turned on");
			this.logMessage("    And the TumbleDryer is suspended");
			bResult = this.registation.get(TumbleDryer.NUMSERIE).suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();
				;
			}
			this.logMessage("    And the mode index 1 is legitimate");
			if (index > maxMode) {
				this.logMessage("      but was not!");
				statistics.failedCondition();
			}
			this.logMessage("    When I call setMode(1)");
			this.logMessage("    Then a precondition exception is thrown");
			old = BCMException.VERBOSE;
			try {
				BCMException.VERBOSE = false;
				this.registation.get(TumbleDryer.NUMSERIE).upMode();
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			} catch (Throwable e) {
			} finally {
				BCMException.VERBOSE = old;
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: getting the current mode when suspended");
			this.logMessage("    Given the TumbleDryer is turned on");
			this.logMessage("    And the TumbleDryer is suspended");
			bResult = this.registation.get(TumbleDryer.NUMSERIE).suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();
				;
			}
			this.logMessage("    When I get the current mode");
			this.logMessage("    Then a precondition exception is thrown");
			old = BCMException.VERBOSE;
			try {
				BCMException.VERBOSE = false;
				this.registation.get(TumbleDryer.NUMSERIE).currentMode();
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			} catch (Throwable e) {
			} finally {
				BCMException.VERBOSE = old;
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: checking the emergency");
			this.logMessage("    Given the TumbleDryer is turned on");
			this.logMessage("    And it has just been suspended");
			bResult = this.registation.get(TumbleDryer.NUMSERIE).suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();
				;
			}
			this.logMessage("    When I call emergency()");
			dResult = this.registation.get(TumbleDryer.NUMSERIE).emergency();
			this.logMessage("    Then the emergency is between 0.0 and 1.0");
			if (dResult < 0.0 || dResult > 1.0) {
				this.logMessage("      but was: " + dResult);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: resuming");
			this.logMessage("    Given the TumbleDryer is turned on");
			this.logMessage("    And it is suspended");
			bResult = this.registation.get(TumbleDryer.NUMSERIE).suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();
				;
			}
			this.logMessage("    When I call resume()");
			bResult = this.registation.get(TumbleDryer.NUMSERIE).resume();
			this.logMessage("    Then the method returns true");
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the TumbleDryer is not suspended");
			bResult = this.registation.get(TumbleDryer.NUMSERIE).suspended();
			if (bResult) {
				this.logMessage("      but it was!");
				statistics.incorrectResult();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		statistics.updateStatistics();
		statistics.statisticsReport(this);

		this.logMessage("TumbleDryer tests end.");
	}

	/** scénario de test pour AC **/

	protected void testAC() throws Exception {

		Thread.sleep(2000);
		this.logMessage("Air Conditioner tests start.");
		TestsStatistics statistics = new TestsStatistics();
		try {
			this.logMessage("Feature: adjustable appliance mode management");
			this.logMessage("  Scenario: getting the max mode index");
			this.logMessage("    When I call maxMode()");
			final int maxMode = this.registation.get(AC.NUMSERIE).maxMode();

			statistics.updateStatistics();

			this.logMessage("  Scenario: getting the current mode index");
			this.logMessage("    When I call currentMode()");
			int result = this.registation.get(AC.NUMSERIE).currentMode();
			if (result != maxMode) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: going down one mode index");
			result = this.registation.get(AC.NUMSERIE).currentMode();
			if (result != maxMode) {
				this.logMessage("      but was: " + result);
				statistics.failedCondition();
			}
			this.logMessage("    When I call downMode()");
			boolean bResult = this.registation.get(AC.NUMSERIE).downMode();
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			result = this.registation.get(AC.NUMSERIE).currentMode();
			if (result != maxMode - 1) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}
			statistics.updateStatistics();

			this.logMessage("  Scenario: going up one mode index");
			result = this.registation.get(AC.NUMSERIE).currentMode();
			if (result != maxMode - 1) {
				this.logMessage("      but was: " + result);
				statistics.failedCondition();
			}
			this.logMessage("    When I call upMode()");
			bResult = this.registation.get(AC.NUMSERIE).upMode();
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			result = this.registation.get(AC.NUMSERIE).currentMode();
			if (result != maxMode) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}
			statistics.updateStatistics();

			this.logMessage("  Scenario: setting the mode index");
			int index = 1;
			this.logMessage("    And the mode index 1 is legitimate");
			if (index > maxMode) {
				this.logMessage("      but was not!");
				statistics.failedCondition();
			}
			this.logMessage("    When I call setMode(1)");
			bResult = this.registation.get(AC.NUMSERIE).setMode(1);
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			result = this.registation.get(AC.NUMSERIE).currentMode();
			if (result != 1) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}
			statistics.updateStatistics();

			this.logMessage("Feature: Getting the power consumption given a mode");
			this.logMessage("  Scenario: getting the power consumption of the maximum mode");
			double dResult = this.registation.get(AC.NUMSERIE).getModeConsumption(maxMode);
			statistics.updateStatistics();

			this.logMessage("Feature: suspending and resuming");
			this.logMessage("  Scenario: checking if suspended when not");
			bResult = this.registation.get(AC.NUMSERIE).suspended();
			if (bResult) {
				this.logMessage("      but it was!");
				statistics.incorrectResult();
			}
			statistics.updateStatistics();

			this.logMessage("  Scenario: suspending");
			bResult = this.registation.get(AC.NUMSERIE).suspended();
			if (bResult) {
				this.logMessage("      but it was!");
				statistics.failedCondition();
			}
			this.logMessage("    When I call suspend()");
			bResult = this.registation.get(AC.NUMSERIE).suspend();
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			bResult = this.registation.get(AC.NUMSERIE).suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			}
			statistics.updateStatistics();

			this.logMessage("  Scenario: checking the emergency");
			bResult = this.registation.get(AC.NUMSERIE).suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();
			}
			this.logMessage("    When I call emergency()");
			dResult = this.registation.get(AC.NUMSERIE).emergency();

			if (dResult < 0.0 || dResult > 1.0) {
				this.logMessage("      but was: " + dResult);
				statistics.incorrectResult();
			}
			statistics.updateStatistics();

			this.logMessage("  Scenario: resuming");
			bResult = this.registation.get(AC.NUMSERIE).suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();
			}
			this.logMessage("    When I call resume()");
			bResult = this.registation.get(AC.NUMSERIE).resume();
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			bResult = this.registation.get(AC.NUMSERIE).suspended();
			if (bResult) {
				this.logMessage("      but it was!");
				statistics.incorrectResult();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		statistics.updateStatistics();
		statistics.statisticsReport(this);
		this.logMessage("Air Conditioner tests end.");
	}

	// Test wifi
	protected void testWifi() throws Exception {

		Thread.sleep(2000);
		this.logMessage("Wifi tests start.");
		TestsStatistics statistics = new TestsStatistics();
		try {
			this.logMessage("Feature: adjustable appliance (BoxWifi) mode management");

			// === SCENARIO 1: Getting the max mode index ===
			this.logMessage("  Scenario: getting the max mode index");
			this.logMessage("    When I call maxMode()");
			final int maxMode = this.registation.get(BoxWifi.NUMSERIE).maxMode();
			statistics.updateStatistics();

			// === SCENARIO 2: Getting the current mode index ===
			this.logMessage("  Scenario: getting the current mode index");
			this.logMessage("    When I call currentMode()");
			int result = this.registation.get(BoxWifi.NUMSERIE).currentMode();
			// CORRECTION: Mode initial = 0 (OFF)
			if (result != 0) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			} else {
				statistics.updateStatistics();
			}

			// === SCENARIO 3: Going up one mode index ===
			this.logMessage("  Scenario: going up one mode index");
			this.logMessage("    When I call upMode()");
			boolean bResult = this.registation.get(BoxWifi.NUMSERIE).upMode();
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			result = this.registation.get(BoxWifi.NUMSERIE).currentMode();
			// CORRECTION: Après upMode() depuis 0 → devrait être 1
			if (result != 1) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}
			statistics.updateStatistics();

			// === SCENARIO 4: Going up again ===
			this.logMessage("  Scenario: going up one more mode index");
			this.logMessage("    When I call upMode()");
			bResult = this.registation.get(BoxWifi.NUMSERIE).upMode();
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			result = this.registation.get(BoxWifi.NUMSERIE).currentMode();
			// CORRECTION: Après upMode() depuis 1 → devrait être 2
			if (result != 2) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}
			statistics.updateStatistics();

			// === SCENARIO 5: Going to max mode ===
			this.logMessage("  Scenario: going to max mode");
			this.logMessage("    When I call upMode()");
			bResult = this.registation.get(BoxWifi.NUMSERIE).upMode();
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			result = this.registation.get(BoxWifi.NUMSERIE).currentMode();
			// CORRECTION: Après upMode() depuis 2 → devrait être 3 (max)
			if (result != maxMode) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}
			statistics.updateStatistics();

			// === SCENARIO 6: Going down one mode index ===
			this.logMessage("  Scenario: going down one mode index");
			this.logMessage("    When I call downMode()");
			// bResult = this.registation.get(BoxWifi.NUMSERIE).downMode();
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			result = this.registation.get(BoxWifi.NUMSERIE).currentMode();
			// CORRECTION: Après downMode() depuis 3 → devrait être 2
			if (result != 2) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}
			statistics.updateStatistics();

			// === SCENARIO 7: Setting specific mode index ===
			this.logMessage("  Scenario: setting the mode index to 1");
			this.logMessage("    When I call setMode(1)");
			bResult = this.registation.get(BoxWifi.NUMSERIE).setMode(1);
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			result = this.registation.get(BoxWifi.NUMSERIE).currentMode();
			// CORRECTION: Après setMode(1) → devrait être 1
			if (result != 1) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}
			statistics.updateStatistics();

			// === SCENARIO 8: Setting to OFF ===
			this.logMessage("  Scenario: setting the mode index to 0 (OFF)");
			this.logMessage("    When I call setMode(0)");
			//bResult = this.registation.get(BoxWifi.NUMSERIE).setMode(0);
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			result = this.registation.get(BoxWifi.NUMSERIE).currentMode();
			
			// CORRECTION: Après setMode(0) → devrait être 0
			if (result != 0) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}
			statistics.updateStatistics();

			// === FEATURE: Suspending and resuming ===
			this.logMessage("Feature: suspending and resuming");

			// === SCENARIO 9: Checking if suspended when not ===
			this.logMessage("  Scenario: checking if suspended when not");
			bResult = this.registation.get(BoxWifi.NUMSERIE).suspended();
			if (bResult) {
				this.logMessage("      but it was!");
				statistics.incorrectResult();
			}
			statistics.updateStatistics();

			// === SCENARIO 10: Suspending ===
			this.logMessage("  Scenario: suspending");
			// D'abord, aller à un mode actif pour tester la suspension
			this.registation.get(BoxWifi.NUMSERIE).setMode(2); // Mode BOX_ONLY
			bResult = this.registation.get(BoxWifi.NUMSERIE).suspend();
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			bResult = this.registation.get(BoxWifi.NUMSERIE).suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			}
			// Vérifier que currentMode retourne 0 quand suspendu
			result = this.registation.get(BoxWifi.NUMSERIE).currentMode();
			if (result != 0) {
				this.logMessage("      currentMode should be 0 when suspended, but was: " + result);
				statistics.incorrectResult();
			}
			statistics.updateStatistics();

			// === SCENARIO 11: Checking emergency ===
			this.logMessage("  Scenario: checking emergency");
			double dResult = this.registation.get(BoxWifi.NUMSERIE).emergency();
			if (dResult < 0.0 || dResult > 1.0) {
				this.logMessage("      but was: " + dResult);
				statistics.incorrectResult();
			}
			statistics.updateStatistics();

			// === SCENARIO 12: Resuming ===
			this.logMessage("  Scenario: resuming");
			bResult = this.registation.get(BoxWifi.NUMSERIE).resume();
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			bResult = this.registation.get(BoxWifi.NUMSERIE).suspended();
			if (bResult) {
				this.logMessage("      but it was!");
				statistics.incorrectResult();
			}
			// Vérifier qu'on est revenu au mode précédent (2)
			result = this.registation.get(BoxWifi.NUMSERIE).currentMode();
			if (result != 2) {
				this.logMessage("      should return to previous mode 2, but was: " + result);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

		} catch (Exception e) {
			this.logMessage("Exception in testWifi: " + e.getMessage());
			e.printStackTrace();
		}

		statistics.updateStatistics();
		statistics.statisticsReport(this);
		this.logMessage("Wifi tests end.");
	}

	/**
	 * test the {@code Heater} component, in cooperation with the
	 * {@code HeaterTester} component.
	 * 
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	{@code
	 * true
	 * }	// no precondition.
	 * post	{@code
	 * true
	 * }	// no postcondition.
	 * </pre>
	 *
	 */
	protected void scheduleTestHeater() {
		// Test for the heater
		Instant heaterTestStart = this.ac.getStartInstant()
				.plusSeconds((HeaterUnitTester.SWITCH_ON_DELAY + HeaterUnitTester.SWITCH_OFF_DELAY) / 2);
		this.traceMessage("HEM schedules the heater test.\n");
		long delay = this.ac.nanoDelayUntilInstant(heaterTestStart);

		// schedule the switch on heater in one second
		this.scheduleTaskOnComponent(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					testHeater();
				} catch (Throwable e) {
					throw new BCMRuntimeException(e);
				}
			}
		}, delay, TimeUnit.NANOSECONDS);
	}

	protected void scheduleTestAC() {
		Instant acTestStart = this.ac.getStartInstant().plusSeconds(5);
		this.traceMessage("HEM schedules the AC test.\n");
		long delay = this.ac.nanoDelayUntilInstant(acTestStart);

		this.scheduleTaskOnComponent(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					testAC();
				} catch (Exception e) {
					throw new BCMRuntimeException(e);
				}
			}
		}, delay, TimeUnit.NANOSECONDS);
	}

	public boolean registered(String uid) throws Exception {

		AdjustableOutboundPort port = (AdjustableOutboundPort) registation.get(uid);
		return this.isPortConnected(port.getPortURI());
	}

	public boolean register(String uid, String controlPortURI, String xmlControlAdapter) throws Exception {
		try {
			System.out.println("HEM: ENregistrement de " + uid + " port: " + controlPortURI);
			File file = new File(xmlControlAdapter);

			ControlAdapterDescriptor desc = Parser.parse(file);

			Class<?> connector = ConnectorGenerator.makeConnectorClassJavassist(desc);
			registation.putIfAbsent(uid, new AdjustableOutboundPort(this));

			AdjustableOutboundPort port = registation.get(uid);
			port.publishPort();

			this.doPortConnection(port.getPortURI(), controlPortURI, connector.getCanonicalName());

			System.out.println("HEM: rEnregistrement de " + uid + " port: " + controlPortURI + " using  fini");

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public void unregister(String uid) throws Exception {

		// pas encore utiliser 
		AdjustableOutboundPort port = (AdjustableOutboundPort) registation.get(uid);
		this.doPortDisconnection(port.getPortURI());
		port.unpublishPort();
		registation.remove(uid);

	}
}
// -----------------------------------------------------------------------------
