package fr.sorbonne_u.components.hem2025e2.equipments.charger.mil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.components.hem2025.tests_utils.SimulationTestStep;
import fr.sorbonne_u.components.hem2025.tests_utils.TestScenario;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.SwitchOffCharger;
import fr.sorbonne_u.components.hem2025e2.equipments.charger.mil.events.SwitchOnCharger;
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
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulationReportI;
import java.time.Instant;
import java.util.ArrayList;

public class RunChargerUnitaryMILSimulation
{
    public static boolean staticInvariants()
    {
        boolean ret = true;
        ret &= ChargerSimulationConfigurationI.staticInvariants();
        return ret;
    }

    public static void main(String[] args)
    {
        staticInvariants();
        Time.setPrintPrecision(4);
        Duration.setPrintPrecision(4);

        try {
            Map<String,AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();

            atomicModelDescriptors.put(
                    ChargerElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            ChargerElectricityModel.class,
                            ChargerElectricityModel.URI,
                            ChargerSimulationConfigurationI.TIME_UNIT,
                            null));

            atomicModelDescriptors.put(
                    ChargerUnitTesterModel.URI,
                    AtomicModelDescriptor.create(
                            ChargerUnitTesterModel.class,
                            ChargerUnitTesterModel.URI,
                            ChargerSimulationConfigurationI.TIME_UNIT,
                            null));

            Map<String,CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

            Set<String> submodels = new HashSet<String>();
            submodels.add(ChargerElectricityModel.URI);
            submodels.add(ChargerUnitTesterModel.URI);

            Map<EventSource,EventSink[]> connections = new HashMap<EventSource,EventSink[]>();

            connections.put(
                    new EventSource(ChargerUnitTesterModel.URI, SwitchOnCharger.class),
                    new EventSink[] { new EventSink(ChargerElectricityModel.URI, SwitchOnCharger.class) });

            connections.put(
                    new EventSource(ChargerUnitTesterModel.URI, SwitchOffCharger.class),
                    new EventSink[] { new EventSink(ChargerElectricityModel.URI, SwitchOffCharger.class) });

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

            ArchitectureI architecture = new Architecture(
                    ChargerCoupledModel.URI,
                    atomicModelDescriptors,
                    coupledModelDescriptors,
                    ChargerSimulationConfigurationI.TIME_UNIT);

            SimulatorI se = architecture.constructSimulator();
            SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 0L;

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

    protected static Instant START_INSTANT = Instant.parse("2025-10-20T12:00:00.00Z");
    protected static Instant END_INSTANT = Instant.parse("2025-10-20T18:00:00.00Z");
    protected static Time START_TIME = new Time(0.0, TimeUnit.HOURS);

    protected static TestScenario CLASSICAL =
        new TestScenario(
            "-----------------------------------------------------\n" +
            "Classical\n\n" +
            "  Gherkin specification\n\n" +
            "    Feature: charger operation\n\n" +
            "      Scenario: charger switched on\n" +
            "        Given a charger that is off\n" +
            "        When it is switched on\n" +
            "        Then it is on\n" +
            "      Scenario: charger switched off\n" +
            "        Given a charger that is on\n" +
            "        When it is switched off\n" +
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
            new SimulationTestStep[] {
                new SimulationTestStep(
                    ChargerUnitTesterModel.URI,
                    Instant.parse("2025-10-20T13:00:00.00Z"),
                    (m, t) -> {
                        ArrayList<EventI> ret = new ArrayList<>();
                        ret.add(new SwitchOnCharger(t));
                        return ret;
                    },
                    (m, t) -> {}),
                new SimulationTestStep(
                    ChargerUnitTesterModel.URI,
                    Instant.parse("2025-10-20T16:00:00.00Z"),
                    (m, t) -> {
                        ArrayList<EventI> ret = new ArrayList<>();
                        ret.add(new SwitchOffCharger(t));
                        return ret;
                    },
                    (m, t) -> {})
            });
}
