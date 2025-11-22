package fr.sorbonne_u.components.hem2025e2.equipments.charger.mil;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.time.Instant;
import java.util.ArrayList;

import fr.sorbonne_u.components.hem2025.tests_utils.SimulationTestStep;
import fr.sorbonne_u.components.hem2025.tests_utils.TestScenario;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.PluginCharger;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.PlugoutCharger;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.StartCharging;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.StopCharging;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.SetTargetPower;

import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.architectures.ArchitectureI;
import fr.sorbonne_u.devs_simulation.hioa.architectures.AtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.interfaces.ModelI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulationReportI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;


public class RunChargerUnitaryMILSimulation
{
	
	public static boolean staticInvariants()
	{
		boolean ret = true;
		ret &= ChargerSimulationConfigurationI.staticInvariants();
		return ret;
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	public static void main(String[] args)
	{
		staticInvariants();
		Time.setPrintPrecision(4);
		Duration.setPrintPrecision(4);

		try {
			Map<String,AbstractAtomicModelDescriptor> atomicModelDescriptors =
																new HashMap<>();

			// charger electricity model (HIOA)
			atomicModelDescriptors.put(
					ChargerElectricityModel.URI,
					AtomicHIOA_Descriptor.create(
							ChargerElectricityModel.class,
							ChargerElectricityModel.URI,
							ChargerSimulationConfigurationI.TIME_UNIT,
							null));
			// unit tester model (drives test scenario)
			atomicModelDescriptors.put(
					ChargerUnitTesterModel.URI,
					AtomicModelDescriptor.create(
							ChargerUnitTesterModel.class,
							ChargerUnitTesterModel.URI,
							ChargerSimulationConfigurationI.TIME_UNIT,
							null));

			Map<String,CoupledModelDescriptor> coupledModelDescriptors =
																new HashMap<>();

			Set<String> submodels = new HashSet<String>();
			submodels.add(ChargerElectricityModel.URI);
			submodels.add(ChargerUnitTesterModel.URI);

			Map<EventSource,EventSink[]> connections =
										new HashMap<EventSource,EventSink[]>();

			connections.put(
					new EventSource(ChargerUnitTesterModel.URI,
									PluginCharger.class),
					new EventSink[] {
							new EventSink(ChargerElectricityModel.URI,
										  PluginCharger.class)
					});
			connections.put(
					new EventSource(ChargerUnitTesterModel.URI,
									PlugoutCharger.class),
					new EventSink[] {
							new EventSink(ChargerElectricityModel.URI,
										  PlugoutCharger.class)
					});
			connections.put(
					new EventSource(ChargerUnitTesterModel.URI,
									StartCharging.class),
					new EventSink[] {
							new EventSink(ChargerElectricityModel.URI,
										  StartCharging.class)
					});
			connections.put(
					new EventSource(ChargerUnitTesterModel.URI,
									StopCharging.class),
					new EventSink[] {
							new EventSink(ChargerElectricityModel.URI,
										  StopCharging.class)
					});
			connections.put(
					new EventSource(ChargerUnitTesterModel.URI,
									SetTargetPower.class),
					new EventSink[] {
							new EventSink(ChargerElectricityModel.URI,
										  SetTargetPower.class)
					});

			// coupled model descriptor
			coupledModelDescriptors.put(
					ChargerCoupledModel.URI,
					new CoupledModelDescriptor(
							ChargerCoupledModel.class,
							ChargerCoupledModel.URI,
							submodels,
							null,
							null,
							connections,
							null));

			// simulation architecture
			ArchitectureI architecture =
					new Architecture(
							ChargerCoupledModel.URI,
							atomicModelDescriptors,
							coupledModelDescriptors,
							ChargerSimulationConfigurationI.TIME_UNIT);

			// create the simulator from the simulation architecture
			SimulatorI se = architecture.constructSimulator();
			SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 0L;

			// run a CLASSICAL test scenario
			CLASSICAL.setUpSimulator(se);
			Time startTime = CLASSICAL.getStartTime();
			Duration d = CLASSICAL.getEndTime().subtract(startTime);
			se.doStandAloneSimulation(startTime.getSimulatedTime(), d.getSimulatedDuration());
			SimulationReportI sr = se.getSimulatedModel().getFinalReport();
			System.out.println(sr);
			System.exit(0);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	// -------------------------------------------------------------------------
	// Test scenarios
	// -------------------------------------------------------------------------

	protected static Instant START_INSTANT = Instant.parse("2025-10-20T12:00:00.00Z");
	protected static Instant END_INSTANT = Instant.parse("2025-10-20T18:00:00.00Z");
	protected static Time START_TIME = new Time(0.0, TimeUnit.HOURS);

	protected static TestScenario CLASSICAL =
		new TestScenario(
			"-----------------------------------------------------\n" +
			"Classical\n\n" +
			"  Gherkin specification\n\n" +
			"    Feature: charger operation\n\n" +
			"      Scenario: plugin and start charging\n" +
			"        Given a charger that is off\n" +
			"        When it is plugged and started\n" +
			"        Then it is charging\n" +
			"      Scenario: set target power\n" +
			"        Given a charger that is charging\n" +
			"        When target power is set\n" +
			"        Then the charger uses the new target power\n" +
			"      Scenario: stop and unplug\n" +
			"        Given a charger that is charging\n" +
			"        When it is stopped and unplugged\n" +
			"        Then it is off\n" +
			"-----------------------------------------------------\n",
			"\n-----------------------------------------------------\n" +
			"End Classical\n" +
			"-----------------------------------------------------",
			START_INSTANT,
			END_INSTANT,
			START_TIME,
			(se, ts) -> {
				HashMap<String, Object> simParams = new HashMap<>();
				simParams.put(
					ModelI.createRunParameterName(
						ChargerUnitTesterModel.URI,
						ChargerUnitTesterModel.TEST_SCENARIO_RP_NAME),
					ts);
				se.setSimulationRunParameters(simParams);
			},
			new SimulationTestStep[]{
				new SimulationTestStep(
					ChargerUnitTesterModel.URI,
					Instant.parse("2025-10-20T13:00:00.00Z"),
					(m, t) -> {
						ArrayList<EventI> ret = new ArrayList<>();
						ret.add(new PluginCharger(t));
						return ret;
					},
					(m, t) -> {}),
				new SimulationTestStep(
					ChargerUnitTesterModel.URI,
					Instant.parse("2025-10-20T13:05:00.00Z"),
					(m, t) -> {
						ArrayList<EventI> ret = new ArrayList<>();
						ret.add(new StartCharging(t));
						return ret;
					},
					(m, t) -> {}),
				new SimulationTestStep(
					ChargerUnitTesterModel.URI,
					Instant.parse("2025-10-20T14:00:00.00Z"),
					(m, t) -> {
						ArrayList<EventI> ret = new ArrayList<>();
						// set target power to 40 W
						ret.add(new SetTargetPower(t, 40.0));
						return ret;
					},
					(m, t) -> {}),
				new SimulationTestStep(
					ChargerUnitTesterModel.URI,
					Instant.parse("2025-10-20T15:00:00.00Z"),
					(m, t) -> {
						ArrayList<EventI> ret = new ArrayList<>();
						// set target power to 20 W
						ret.add(new SetTargetPower(t, 20.0));
						return ret;
					},
					(m, t) -> {}),
				new SimulationTestStep(
					ChargerUnitTesterModel.URI,
					Instant.parse("2025-10-20T16:00:00.00Z"),
					(m, t) -> {
						ArrayList<EventI> ret = new ArrayList<>();
						ret.add(new StopCharging(t));
						return ret;
					},
					(m, t) -> {}),
				new SimulationTestStep(
					ChargerUnitTesterModel.URI,
					Instant.parse("2025-10-20T16:05:00.00Z"),
					(m, t) -> {
						ArrayList<EventI> ret = new ArrayList<>();
						ret.add(new PlugoutCharger(t));
						return ret;
					},
					(m, t) -> {})
			});
}
