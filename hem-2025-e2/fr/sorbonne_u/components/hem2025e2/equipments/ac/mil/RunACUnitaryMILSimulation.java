package fr.sorbonne_u.components.hem2025e2.equipments.ac.mil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.hem2025.tests_utils.SimulationTestStep;
import fr.sorbonne_u.components.hem2025.tests_utils.TestScenario;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.events.SetPowerAC;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.events.SwitchOffAC;
import fr.sorbonne_u.components.hem2025e2.equipments.ac.mil.events.SwitchOnAC;
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
import fr.sorbonne_u.components.hem2025e1.equipments.ac.ACExternalControlI;
import java.time.Instant;
import java.util.ArrayList;

public class RunACUnitaryMILSimulation
{
    public static boolean staticInvariants()
    {
        boolean ret = true;
        ret &= ACSimulationConfigurationI.staticInvariants();
        return ret;
    }

    public static void main(String[] args)
    {
        staticInvariants();
        Time.setPrintPrecision(4);
        Duration.setPrintPrecision(4);

        try {
            Map<String,AbstractAtomicModelDescriptor> atomicModelDescriptors =
                                                    new HashMap<>();

            atomicModelDescriptors.put(
                    ACElectricityModel.URI,
                    AtomicHIOA_Descriptor.create(
                            ACElectricityModel.class,
                            ACElectricityModel.URI,
                            ACSimulationConfigurationI.TIME_UNIT,
                            null));

            atomicModelDescriptors.put(
                    ACUnitTesterModel.URI,
                    AtomicModelDescriptor.create(
                            ACUnitTesterModel.class,
                            ACUnitTesterModel.URI,
                            ACSimulationConfigurationI.TIME_UNIT,
                            null));

            Map<String,CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

            Set<String> submodels = new HashSet<String>();
            submodels.add(ACElectricityModel.URI);
            submodels.add(ACUnitTesterModel.URI);

            Map<EventSource,EventSink[]> connections = new HashMap<EventSource,EventSink[]>();

            connections.put(
                    new EventSource(ACUnitTesterModel.URI, SwitchOnAC.class),
                    new EventSink[] { new EventSink(ACElectricityModel.URI, SwitchOnAC.class) });

            connections.put(
                    new EventSource(ACUnitTesterModel.URI, SwitchOffAC.class),
                    new EventSink[] { new EventSink(ACElectricityModel.URI, SwitchOffAC.class) });

            connections.put(
                    new EventSource(ACUnitTesterModel.URI, SetPowerAC.class),
                    new EventSink[] { new EventSink(ACElectricityModel.URI, SetPowerAC.class) });

            coupledModelDescriptors.put(
                    ACCoupledModel.URI,
                    new CoupledModelDescriptor(
                            ACCoupledModel.class,
                            ACCoupledModel.URI,
                            submodels,
                            null,
                            null,
                            connections,
                            null));

            ArchitectureI architecture = new Architecture(
                    ACCoupledModel.URI,
                    atomicModelDescriptors,
                    coupledModelDescriptors,
                    ACSimulationConfigurationI.TIME_UNIT);

            SimulatorI se = architecture.constructSimulator();
            SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 0L;

            // run CLASSICAL scenario
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
            "    Feature: ac operation\n\n" +
            "      Scenario: ac switched on\n" +
            "        Given an ac that is off\n" +
            "        When it is switched on\n" +
            "        Then it is on\n" +
            "      Scenario: ac set power\n" +
            "        Given an ac that is on\n" +
            "        When it is set a power level\n" +
            "        Then it changes power\n" +
            "      Scenario: ac switched off\n" +
            "        Given an ac that is on\n" +
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
                        ACUnitTesterModel.URI,
                        ACUnitTesterModel.TEST_SCENARIO_RP_NAME),
                    ts);
                se.setSimulationRunParameters(simParams);
            },
            new SimulationTestStep[] {
                new SimulationTestStep(
                    ACUnitTesterModel.URI,
                    Instant.parse("2025-10-20T13:00:00.00Z"),
                    (m, t) -> {
                        ArrayList<EventI> ret = new ArrayList<>();
                        ret.add(new SwitchOnAC(t));
                        return ret;
                    },
                    (m, t) -> {}),
                new SimulationTestStep(
                    ACUnitTesterModel.URI,
                    Instant.parse("2025-10-20T14:00:00.00Z"),
                    (m, t) -> {
                        ArrayList<EventI> ret = new ArrayList<>();
                        double p = 0.5 * ACExternalControlI.MAX_POWER_LEVEL.getData();
                        ret.add(new SetPowerAC(t, new SetPowerAC.PowerValue(p)));
                        return ret;
                    },
                    (m, t) -> {}),
                new SimulationTestStep(
                    ACUnitTesterModel.URI,
                    Instant.parse("2025-10-20T16:00:00.00Z"),
                    (m, t) -> {
                        ArrayList<EventI> ret = new ArrayList<>();
                        ret.add(new SwitchOffAC(t));
                        return ret;
                    },
                    (m, t) -> {})
            });
}