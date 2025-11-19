package fr.sorbonne_u.components.hem2025e2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.time.Instant;
import java.util.ArrayList;
import fr.sorbonne_u.components.hem2025.tests_utils.SimulationTestStep;
import fr.sorbonne_u.components.hem2025.tests_utils.TestScenario;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.ACElectricityModel;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.ACUnitTesterModel;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.ACSimulationConfigurationI;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.events.SwitchOnAC;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.events.SwitchOffAC;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.events.SetPowerAC;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.ChargerElectricityModel;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.ChargerUnitTesterModel;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.ChargerSimulationConfigurationI;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.SwitchOnCharger;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.SwitchOffCharger;
import fr.sorbonne_u.components.hem2025e2.equipments.meter.mil.ElectricMeterElectricityModel;
import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.architectures.ArchitectureI;
import fr.sorbonne_u.devs_simulation.hioa.architectures.AtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.architectures.CoupledHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSink;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSource;
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
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

/**
 * Minimal global run driver for only AC and Charger MIL models.
 */
public class RunACChargerOnlySimulation {

    public static boolean staticInvariants() {
        boolean ret = true;
        ret &= ACSimulationConfigurationI.staticInvariants();
        ret &= ChargerSimulationConfigurationI.staticInvariants();
        ret &= GlobalSimulationConfigurationI.staticInvariants();
        return ret;
    }

    public static void main(String[] args) {
        staticInvariants();
        Time.setPrintPrecision(4);
        Duration.setPrintPrecision(4);

        try {
            Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();

            // AC models
            atomicModelDescriptors.put(
                    ACElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            ACElectricityModel.class,
                            ACElectricityModel.URI,
                            GlobalSimulationConfigurationI.TIME_UNIT,
                            null));
            atomicModelDescriptors.put(
                    ACUnitTesterModel.URI,
                    AtomicModelDescriptor.create(
                            ACUnitTesterModel.class,
                            ACUnitTesterModel.URI,
                            GlobalSimulationConfigurationI.TIME_UNIT,
                            null));

            // Charger models
            atomicModelDescriptors.put(
                    ChargerElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            ChargerElectricityModel.class,
                            ChargerElectricityModel.URI,
                            GlobalSimulationConfigurationI.TIME_UNIT,
                            null));
            atomicModelDescriptors.put(
                    ChargerUnitTesterModel.URI,
                    AtomicModelDescriptor.create(
                            ChargerUnitTesterModel.class,
                            ChargerUnitTesterModel.URI,
                            GlobalSimulationConfigurationI.TIME_UNIT,
                            null));

            // Electric meter to aggregate intensities
            atomicModelDescriptors.put(
                    ElectricMeterElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            ElectricMeterElectricityModel.class,
                            ElectricMeterElectricityModel.URI,
                            GlobalSimulationConfigurationI.TIME_UNIT,
                            null));

            // Coupled model descriptor
            Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

            Set<String> submodels = new HashSet<>();
            submodels.add(ACElectricityModel.URI);
            submodels.add(ACUnitTesterModel.URI);
            submodels.add(ChargerElectricityModel.URI);
            submodels.add(ChargerUnitTesterModel.URI);
            submodels.add(ElectricMeterElectricityModel.URI);

            Map<EventSource, EventSink[]> connections = new HashMap<>();

            // AC events from tester to electricity model
            connections.put(
                    new EventSource(ACUnitTesterModel.URI, SwitchOnAC.class),
                    new EventSink[] {new EventSink(ACElectricityModel.URI, SwitchOnAC.class)});
            connections.put(
                    new EventSource(ACUnitTesterModel.URI, SwitchOffAC.class),
                    new EventSink[] {new EventSink(ACElectricityModel.URI, SwitchOffAC.class)});
            connections.put(
                    new EventSource(ACUnitTesterModel.URI, SetPowerAC.class),
                    new EventSink[] {new EventSink(ACElectricityModel.URI, SetPowerAC.class)});

            // Charger events from tester to electricity model
            connections.put(
                    new EventSource(ChargerUnitTesterModel.URI, SwitchOnCharger.class),
                    new EventSink[] {new EventSink(ChargerElectricityModel.URI, SwitchOnCharger.class)});
            connections.put(
                    new EventSource(ChargerUnitTesterModel.URI, SwitchOffCharger.class),
                    new EventSink[] {new EventSink(ChargerElectricityModel.URI, SwitchOffCharger.class)});

            // Variable bindings: intensities -> electric meter
            Map<VariableSource, VariableSink[]> bindings = new HashMap<>();
            bindings.put(
                    new VariableSource("currentIntensity", Double.class, ACElectricityModel.URI),
                    new VariableSink[] {new VariableSink("currentIntensity", Double.class, "currentACIntensity", Double.class, ElectricMeterElectricityModel.URI)});
            bindings.put(
                    new VariableSource("currentIntensity", Double.class, ChargerElectricityModel.URI),
                    new VariableSink[] {new VariableSink("currentIntensity", Double.class, "currentChargerIntensity", Double.class, ElectricMeterElectricityModel.URI)});

            coupledModelDescriptors.put(
                    "ACChargerCoupled",
                    new CoupledHIOA_Descriptor(
                            fr.sorbonne_u.components.hem2025e2.GlobalCoupledModel.class, // reuse class for descriptor compatibility
                            "ACChargerCoupled",
                            submodels,
                            null,
                            null,
                            connections,
                            null,
                            null,
                            null,
                            bindings));

            ArchitectureI architecture = new Architecture(
                    "ACChargerCoupled",
                    atomicModelDescriptors,
                    coupledModelDescriptors,
                    GlobalSimulationConfigurationI.TIME_UNIT);

            SimulatorI se = architecture.constructSimulator();

            // prepare a short test scenario: switch both on then off
            TestScenario scenario = new TestScenario(
                    "AC+Charger simple scenario",
                    "",
                    GlobalSimulationConfigurationI.START_INSTANT,
                    GlobalSimulationConfigurationI.END_INSTANT,
                    GlobalSimulationConfigurationI.START_TIME,
                    (simulationEngine, testScenario, simulationParameters) -> {
                        simulationParameters.put(ModelI.createRunParameterName(ACUnitTesterModel.URI, ACUnitTesterModel.TEST_SCENARIO_RP_NAME), testScenario);
                        simulationParameters.put(ModelI.createRunParameterName(ChargerUnitTesterModel.URI, ChargerUnitTesterModel.TEST_SCENARIO_RP_NAME), testScenario);
                        simulationEngine.setSimulationRunParameters(simulationParameters);
                    },
                    new SimulationTestStep[] {
                        new SimulationTestStep(
                                ACUnitTesterModel.URI,
                                Instant.parse("2025-10-20T12:00:00.00Z"),
                                (m, t) -> { ArrayList<EventI> ev = new ArrayList<>(); ev.add(new SwitchOnAC(t)); return ev; },
                                (m, t) -> {}),
                        new SimulationTestStep(
                                ChargerUnitTesterModel.URI,
                                Instant.parse("2025-10-20T12:00:00.00Z"),
                                (m, t) -> { ArrayList<EventI> ev = new ArrayList<>(); ev.add(new SwitchOnCharger(t)); return ev; },
                                (m, t) -> {}),
                        new SimulationTestStep(
                                ACUnitTesterModel.URI,
                                Instant.parse("2025-10-20T13:00:00.00Z"),
                                (m, t) -> { ArrayList<EventI> ev = new ArrayList<>(); ev.add(new SwitchOffAC(t)); return ev; },
                                (m, t) -> {}),
                        new SimulationTestStep(
                                ChargerUnitTesterModel.URI,
                                Instant.parse("2025-10-20T13:00:00.00Z"),
                                (m, t) -> { ArrayList<EventI> ev = new ArrayList<>(); ev.add(new SwitchOffCharger(t)); return ev; },
                                (m, t) -> {})
                    });

            // tracing
            

            ElectricMeterElectricityModel.VERBOSE = true;
            ElectricMeterElectricityModel.DEBUG = false;

            SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 0L;

            // run
            scenario.setUpSimulator(se, new HashMap<>());
            Time startTime = scenario.getStartTime();
            Duration d = scenario.getEndTime().subtract(startTime);
            se.doStandAloneSimulation(startTime.getSimulatedTime(), d.getSimulatedDuration());

            Object report = se.getFinalReport();
            System.out.println("Simulation finished. Report: " + report);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
